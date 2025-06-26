package com.example.testesmartstock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MeusDadosActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextNomeCompleto, editTextEmail, editTextDataNascimento;
    private Button btnSalvarAlteracoes, btnExcluirConta;
    private ImageView btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meusdados);

        // Conectando os elementos
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextNomeCompleto = findViewById(R.id.editTextNomeCompleto);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextDataNascimento = findViewById(R.id.editTextDataNascimento);

        btnSalvarAlteracoes = findViewById(R.id.btnSalvarAlteracoes);
        btnExcluirConta = findViewById(R.id.btnExcluirConta);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Ação de voltar
        btnVoltar.setOnClickListener(v -> {
            finish(); // Volta para a tela anterior
        });

        // Ação de salvar alterações
        btnSalvarAlteracoes.setOnClickListener(v -> {
            // Aqui você poderia atualizar os dados no Firebase (faremos simples por enquanto)
            Toast.makeText(MeusDadosActivity.this, "Alterações salvas com sucesso!", Toast.LENGTH_SHORT).show();
        });

        // Ação de excluir conta
        btnExcluirConta.setOnClickListener(v -> {
            new AlertDialog.Builder(MeusDadosActivity.this)
                    .setTitle("Excluir conta")
                    .setMessage("Tem certeza que deseja excluir sua conta? Esta ação não pode ser desfeita.")
                    .setPositiveButton("Excluir", (dialog, which) -> {
                        // Aqui colocaria a lógica para excluir do Firebase
                        Toast.makeText(MeusDadosActivity.this, "Conta excluída com sucesso!", Toast.LENGTH_SHORT).show();

                        // Voltar para a tela inicial (Login ou Main)
                        Intent intent = new Intent(MeusDadosActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Se quiser carregar os dados do usuário, pode implementar aqui (via Firebase ou Intent)
    }
}
