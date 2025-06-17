package com.example.testesmartstock;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView textSaudacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textSaudacao = findViewById(R.id.textSaudacao);

        String nomeUsuario = getIntent().getStringExtra("nome_usuario");
        if (nomeUsuario != null) {
            textSaudacao.setText("Olá, seja bem-vindo • " + nomeUsuario);
        }

        Button btnModo = findViewById(R.id.btnModoFuncionamento);
        btnModo.setOnClickListener(v -> {
        });

        Button btnDispensa = findViewById(R.id.btnAcessarDispensa);
        btnDispensa.setOnClickListener(v -> {
        });

        Button btnPlano = findViewById(R.id.btnPlano);
        btnPlano.setOnClickListener(v -> {
        });
    }
}
