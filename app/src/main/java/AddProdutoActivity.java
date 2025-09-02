package com.example.testesmartstock;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import java.io.IOException;

public class AddProdutoActivity extends AppCompatActivity {

    private EditText editNome, editQuantidade, editUnidade, editDataValidade, editCategoria;
    private Button btnSalvar, btnScan;
    private FirebaseFirestore db;
    private String nomeUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);

        db = FirebaseFirestore.getInstance();
        nomeUsuario = getIntent().getStringExtra("nome_usuario");

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
        btnScan = findViewById(R.id.btnScan);

        // BOTÃO SCAN (QR Code da nota fiscal)
        btnScan.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Aproxime o QR Code");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureActivityPortrait.class);
            barcodeLauncher.launch(options);
        });

        // BOTÃO SALVAR (Manual)
        btnSalvar.setOnClickListener(v -> salvarProdutoManual());
    }

    // ---------------- FUNÇÃO SALVAR MANUAL ----------------
    private void salvarProdutoManual() {
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
        produto.put("dataValidade", validadeISO);
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
    }

    // ---------------- RESULTADO DO QR CODE ----------------
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    String url = result.getContents();


                    new Thread(() -> {
                        try {
                            List<NotaFiscalScraper.ProdutoNF> produtos = NotaFiscalScraper.extrairProdutos(url);

                            runOnUiThread(() -> {
                                if (produtos.isEmpty()) {
                                    Toast.makeText(this, "Nenhum produto encontrado na nota.", Toast.LENGTH_SHORT).show();
                                } else {
                                    NotaFiscalScraper.ProdutoNF p = produtos.get(0);
                                    editNome.setText(p.nome);
                                    editQuantidade.setText(p.quantidade);
                                    editUnidade.setText(p.unidade);

                                    Toast.makeText(this, produtos.size() + " produtos encontrados!", Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Erro ao extrair dados da nota fiscal. Tente novamente.", Toast.LENGTH_LONG).show();
                            });
                        }
                    }).start();
                }
            });
}
