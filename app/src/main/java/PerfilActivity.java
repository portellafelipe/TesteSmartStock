package com.example.testesmartstock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {

    private LinearLayout cardMeusDados, cardAssinaturas, cardPreferencias, cardTermos;
    private Button btnSair;
    private TextView textSaudacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        textSaudacao = findViewById(R.id.textSaudacao);

        String nomeUsuario = getIntent().getStringExtra("nome_usuario");
        if (nomeUsuario != null) {
            textSaudacao.setText("Olá • " + nomeUsuario);
        }

        cardMeusDados = findViewById(R.id.cardMeusDados);
        cardAssinaturas = findViewById(R.id.cardAssinaturas);
        cardPreferencias = findViewById(R.id.cardPreferencias);
        cardTermos = findViewById(R.id.cardTermos);
        btnSair = findViewById(R.id.btnSair);

        // Meus Dados
        cardMeusDados.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, MeusDadosActivity.class);
            intent.putExtra("nome_usuario", nomeUsuario);
            startActivity(intent);
        });

        // Assinaturas
        cardAssinaturas.setOnClickListener(v -> {
            startActivity(new Intent(PerfilActivity.this, AssinaturasActivity.class));
        });

        // Preferências
        cardPreferencias.setOnClickListener(v -> {
            startActivity(new Intent(PerfilActivity.this, PreferenciasActivity.class));
        });


        // Termos de uso
        cardTermos.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, TermosActivity.class);
            startActivity(intent);
        });

        // Sair
        btnSair.setOnClickListener(v -> showDialogLogout());
    }


    private void showDialogLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sair")
                .setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    // Levar para MainActivity (Tela inicial de login ou cadastro)
                    Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}