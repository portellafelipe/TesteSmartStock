package com.example.testesmartstock;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DispensaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProdutoAdapter produtoAdapter;
    private ArrayList<Produto> listaProdutos;
    private FirebaseFirestore db;
    private String nomeUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispensa);

        // Botão de voltar
        ImageView btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> {
            finish(); // Fecha a Activity e volta para a anterior
        });

        recyclerView = findViewById(R.id.recyclerDispensa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaProdutos = new ArrayList<>();
        produtoAdapter = new ProdutoAdapter(listaProdutos);
        recyclerView.setAdapter(produtoAdapter);

        db = FirebaseFirestore.getInstance();
        nomeUsuario = getIntent().getStringExtra("nome_usuario");

        if (nomeUsuario == null || nomeUsuario.isEmpty()) {
            Toast.makeText(this, "Usuário não identificado!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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
                            listaProdutos.add(produto);
                        }

                        // Ordenar pela data de validade mais próxima
                        Collections.sort(listaProdutos, new Comparator<Produto>() {
                            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            @Override
                            public int compare(Produto p1, Produto p2) {
                                try {
                                    Date d1 = sdf.parse(p1.getDataValidade());
                                    Date d2 = sdf.parse(p2.getDataValidade());
                                    return d1.compareTo(d2);
                                } catch (ParseException e) {
                                    return 0;
                                }
                            }
                        });

                        produtoAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Erro ao carregar produtos", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
