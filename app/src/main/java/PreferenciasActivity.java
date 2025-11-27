package com.example.testesmartstock;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PreferenciasActivity extends AppCompatActivity {

    private CheckBox checkNotificacoes, checkTemaEscuro, checkSom, checkOrdenar;
    private Spinner spinnerDias;
    private Button btnSalvarPrefs;
    private ImageView btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        checkNotificacoes = findViewById(R.id.checkNotificacoes);
        spinnerDias = findViewById(R.id.spinnerDias);
        checkTemaEscuro = findViewById(R.id.checkTemaEscuro);
        checkSom = findViewById(R.id.checkSom);
        checkOrdenar = findViewById(R.id.checkOrdenar);
        btnSalvarPrefs = findViewById(R.id.btnSalvarPrefs);
        btnVoltar = findViewById(R.id.btnVoltar);

        String[] dias = {"3 dias", "5 dias", "7 dias", "10 dias", "15 dias"};
        spinnerDias.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dias));

        btnSalvarPrefs.setOnClickListener(v ->
                Toast.makeText(this, "Preferências salvas!", Toast.LENGTH_SHORT).show()
        );

        btnVoltar.setOnClickListener(v -> finish());
    }
}
