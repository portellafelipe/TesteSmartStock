package com.example.testesmartstock;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import android.util.Log;
import java.util.List;

public class DispensaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProdutoAdapter adapter;
    private List<Produto> listaProdutos;
    private FirebaseFirestore db;
    private String nomeUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispensa);

        ImageView btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());

        db = FirebaseFirestore.getInstance();

        // 1. Inicializar o nomeUsuario e padronizar
        nomeUsuario = getIntent().getStringExtra("nome_usuario");

        if (nomeUsuario == null || nomeUsuario.isEmpty()) {
            Toast.makeText(this, "Usuário não identificado!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Padroniza para minúsculas e remove acentos
        nomeUsuario = nomeUsuario.toLowerCase().replace("ú", "u");

        Log.d("DISPENSA", "Nome de usuário padronizado: " + nomeUsuario);

        recyclerView = findViewById(R.id.recyclerDispensa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaProdutos = new ArrayList<>();

        // 2. Criar o adaptador DEPOIS
        adapter = new ProdutoAdapter(this, listaProdutos, nomeUsuario);
        recyclerView.setAdapter(adapter);

        carregarProdutos();
    }

    private void carregarProdutos() {
        db.collection("usuarios").document(nomeUsuario)
                .collection("dispensa")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaProdutos.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Produto produto = doc.toObject(Produto.class);
                            produto.setId(doc.getId());
                            listaProdutos.add(produto);
                            Log.d("DISPENSA", "Produto carregado: " + produto.getNome());
                        }

                        Collections.sort(listaProdutos, new Comparator<Produto>() {
                            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            {
                                sdf.setLenient(false);
                            }

                            @Override
                            public int compare(Produto p1, Produto p2) {
                                String v1 = p1.getDataValidade();
                                String v2 = p2.getDataValidade();

                                v1 = (v1 == null) ? "" : v1.trim();
                                v2 = (v2 == null) ? "" : v2.trim();

                                if (v1.isEmpty() && v2.isEmpty()) return 0;
                                if (v1.isEmpty()) return 1;
                                if (v2.isEmpty()) return -1;

                                try {
                                    Date d1 = sdf.parse(v1);
                                    Date d2 = sdf.parse(v2);
                                    if (d1 == null && d2 == null) return 0;
                                    if (d1 == null) return 1;
                                    if (d2 == null) return -1;
                                    return d1.compareTo(d2);
                                } catch (ParseException e) {
                                    boolean v1Ok = true, v2Ok = true;
                                    try { sdf.parse(v1); } catch (Exception ex) { v1Ok = false; }
                                    try { sdf.parse(v2); } catch (Exception ex) { v2Ok = false; }

                                    if (!v1Ok && !v2Ok) return 0;
                                    if (!v1Ok) return 1;
                                    if (!v2Ok) return -1;
                                    return 0;
                                }
                            }
                        });
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("DISPENSA", "Erro ao carregar produtos: ", task.getException());
                    }
                });
    }

    private void excluirProduto(String produtoId, int position) {
        db.collection("usuarios").document(nomeUsuario)
                .collection("dispensa").document(produtoId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    listaProdutos.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Produto excluído!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao excluir: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
