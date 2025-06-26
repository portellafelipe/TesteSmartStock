package com.example.testesmartstock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView textSaudacao;
    private ImageView imageAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textSaudacao = findViewById(R.id.textSaudacao);
        imageAvatar = findViewById(R.id.imageAvatar); // Avatar

        String nomeUsuario = getIntent().getStringExtra("nome_usuario");
        if (nomeUsuario != null) {
            textSaudacao.setText("Olá, seja bem-vindo • " + nomeUsuario);
        }

        // Clique no avatar leva para PerfilActivity
        imageAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
            startActivity(intent);
        });

        Button btnModo = findViewById(R.id.btnModoFuncionamento);
        btnModo.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ModoFuncionamentoActivity.class);
            startActivity(intent);
        });

        Button btnDispensa = findViewById(R.id.btnAcessarDispensa);
        btnDispensa.setOnClickListener(v -> {
            // ação futura: abrir tela de dispensa
        });

        Button btnPlano = findViewById(R.id.btnPlano);
        btnPlano.setOnClickListener(v -> {
            // ação futura: abrir detalhes dos planos
        });
    }
}


