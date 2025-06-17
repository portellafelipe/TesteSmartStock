package com.example.testesmartstock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextNomeUsuario;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button btnCriarConta;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "Activity carregada!", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        FirebaseApp.initializeApp(this); // Inicializa Firebase
        db = FirebaseFirestore.getInstance(); // Instancia o Firestore

        // Conectando os elementos da interface
        editTextNomeUsuario = findViewById(R.id.editTextNomeUsuario);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        btnCriarConta = findViewById(R.id.btnCriarConta);

        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editTextNomeUsuario.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String senha = editTextSenha.getText().toString().trim();

                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> usuario = new HashMap<>();
                    usuario.put("nome", nome);
                    usuario.put("email", email);
                    usuario.put("senha", senha); // Se for app real, nunca salve senha sem criptografia!

                    db.collection("usuarios")
                            .add(usuario)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(CadastroActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CadastroActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(CadastroActivity.this, "Erro ao criar conta: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                }
            }
        });
    }
}
