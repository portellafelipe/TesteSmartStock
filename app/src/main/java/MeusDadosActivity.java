package com.example.testesmartstock;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MeusDadosActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail;
    private Button btnSalvarAlteracoes;
    private ImageView btnVoltar;

    private FirebaseFirestore db;
    private String nomeUsuario; // ID do documento no Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meusdados);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        btnSalvarAlteracoes = findViewById(R.id.btnSalvarAlteracoes);
        btnVoltar = findViewById(R.id.btnVoltar);

        nomeUsuario = getIntent().getStringExtra("nome_usuario");

        if (nomeUsuario == null || nomeUsuario.isEmpty()) {
            Toast.makeText(this, "Usuário inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();

        carregarDados();

        btnSalvarAlteracoes.setOnClickListener(v -> salvarAlteracoes());
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void carregarDados() {
        db.collection("usuarios")
                .document(nomeUsuario)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String nome = document.getString("nome");
                        String email = document.getString("email");

                        editTextUsername.setText(nome);
                        editTextEmail.setText(email);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
                );
    }

    private void salvarAlteracoes() {
        String novoNome = editTextUsername.getText().toString().trim();

        if (novoNome.isEmpty()) {
            Toast.makeText(this, "Digite o nome", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("usuarios")
                .document(nomeUsuario)
                .update("nome", novoNome)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Nome atualizado!", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show()
                );
    }
}
