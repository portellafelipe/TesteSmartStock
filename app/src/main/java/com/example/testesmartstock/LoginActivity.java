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
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextNomeUsuario;
    private EditText editTextEmail;
    private Button btnEntrar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        editTextNomeUsuario = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        btnEntrar = findViewById(R.id.buttonLogin);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeUsuario = editTextNomeUsuario.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();

                if (nomeUsuario.isEmpty() || email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Consulta no Firestore
                    db.collection("usuarios")
                            .whereEqualTo("nome", nomeUsuario)
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                        // Login bem-sucedido
                                        Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        intent.putExtra("nome_usuario", nomeUsuario);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(LoginActivity.this, "Erro ao acessar o banco de dados: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                }
            }
        });
    }
}
