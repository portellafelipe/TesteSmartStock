package com.example.testesmartstock;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PlanosActivity extends AppCompatActivity {

    private Button buttonSubscribe;
    private boolean isSubscribed = false; // substitui depois pela verificação real

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planos);

        buttonSubscribe = findViewById(R.id.buttonSubscribe);

        if (isSubscribed) {
            buttonSubscribe.setText("Plano ativo");
            buttonSubscribe.setEnabled(false);
        }

        buttonSubscribe.setOnClickListener(v -> {
            // Aqui vai abrir a tela/pagamento
            Toast.makeText(this, "Abrindo checkout...", Toast.LENGTH_SHORT).show();
        });
    }
}
