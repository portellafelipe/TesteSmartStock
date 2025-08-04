package com.example.testesmartstock;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddProdutoActivity extends AppCompatActivity {

    private EditText editNome, editQuantidade, editUnidade, editDataValidade, editCategoria;
    private Button btnSalvar;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);

        db = FirebaseFirestore.getInstance();
        String nomeUsuario = getIntent().getStringExtra("nome_usuario");

        if (nomeUsuario == null || nomeUsuario.isEmpty()) {
            Toast.makeText(this, "Usuário não identificado!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        editNome = findViewById(R.id.editNomeProduto);
        editQuantidade = findViewById(R.id.editQuantidade);
        editUnidade = findViewById(R.id.editUnidade);
        editDataValidade = findViewById(R.id.editDataValidade);
        editCategoria = findViewById(R.id.editCategoria);
        btnSalvar = findViewById(R.id.btnSalvarProduto);

        btnSalvar.setOnClickListener(v -> {
            String nome = editNome.getText().toString();
            String quantidade = editQuantidade.getText().toString();
            String unidade = editUnidade.getText().toString();
            String validadeBr = editDataValidade.getText().toString(); // DD-MM-AAAA
            String categoria = editCategoria.getText().toString();

            if (nome.isEmpty() || quantidade.isEmpty() || validadeBr.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Converter DD-MM-AAAA → AAAA-MM-DD
            String validadeISO;
            try {
                SimpleDateFormat formatoBr = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                SimpleDateFormat formatoIso = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date data = formatoBr.parse(validadeBr);
                validadeISO = formatoIso.format(data);
            } catch (ParseException e) {
                Toast.makeText(this, "Data inválida! Use o formato DD-MM-AAAA.", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> produto = new HashMap<>();
            produto.put("nome", nome);
            produto.put("quantidade", Integer.parseInt(quantidade));
            produto.put("unidade", unidade);
            produto.put("dataValidade", validadeISO); // formato correto para ordenação
            produto.put("categoria", categoria);

            DocumentReference userDocRef = db.collection("usuarios").document(nomeUsuario);

            userDocRef.set(new HashMap<>()) // garante que o documento do usuário exista
                    .addOnSuccessListener(aVoid -> {
                        userDocRef.collection("dispensa")
                                .add(produto)
                                .addOnSuccessListener(doc -> {
                                    Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Erro ao salvar produto: " + e.getMessage(), Toast.LENGTH_LONG).show()
                                );
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Erro ao criar usuário: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
        });
    }
}
