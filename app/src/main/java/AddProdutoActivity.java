package com.example.testesmartstock;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddProdutoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AddProdutoAdapter adapter;
    private ArrayList<NotaFiscalScraper.ProdutoNF> listaProdutos = new ArrayList<>();
    private Button btnSalvarTodos, btnScan;
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

        recyclerView = findViewById(R.id.recyclerAddProdutos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddProdutoAdapter(listaProdutos);
        recyclerView.setAdapter(adapter);

        btnSalvarTodos = findViewById(R.id.btnSalvarTodos);
        btnScan = findViewById(R.id.btnScan);

        // SCAN DO QR CODE
        btnScan.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Aproxime o QR Code");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureActivityPortrait.class);
            barcodeLauncher.launch(options);
        });

        // SALVAR TODOS PRODUTOS
        btnSalvarTodos.setOnClickListener(v -> salvarTodosProdutos());
    }

    // ---------------- SALVAR TODOS ----------------
    private void salvarTodosProdutos() {
        if (listaProdutos.isEmpty()) {
            Toast.makeText(this, "Nenhum produto para salvar.", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference userDocRef = db.collection("usuarios").document(nomeUsuario);
        userDocRef.set(new HashMap<>()) // garante que o documento existe
                .addOnSuccessListener(aVoid -> {
                    for (NotaFiscalScraper.ProdutoNF p : listaProdutos) {

                        // Converter validade se for preenchida (pode estar vazia)
                        String validadeISO = "";
                        if (p.validade != null && !p.validade.isEmpty()) {
                            try {
                                SimpleDateFormat formatoBr = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                SimpleDateFormat formatoIso = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                Date data = formatoBr.parse(p.validade);
                                validadeISO = formatoIso.format(data);
                            } catch (ParseException e) {
                                validadeISO = "";
                            }
                        }

                        Map<String, Object> produto = new HashMap<>();
                        produto.put("nome", p.nome);
                        produto.put("quantidade", p.quantidade);
                        produto.put("unidade", p.unidade);
                        produto.put("dataValidade", validadeISO);
                        produto.put("categoria", p.categoria);

                        userDocRef.collection("dispensa").add(produto);
                    }

                    Toast.makeText(this, "Produtos salvos com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao salvar produtos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
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
                                    listaProdutos.clear();
                                    listaProdutos.addAll(produtos);
                                    adapter.notifyDataSetChanged();

                                    Toast.makeText(this, produtos.size() + " produtos carregados!", Toast.LENGTH_LONG).show();
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
