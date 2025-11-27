package com.example.testesmartstock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView textSaudacao;
    private ImageView imageAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textSaudacao = findViewById(R.id.textSaudacao);
        imageAvatar = findViewById(R.id.imageAvatar);

        String nomeUsuario = getIntent().getStringExtra("nome_usuario");
        if (nomeUsuario != null) {
            textSaudacao.setText("Olá, seja bem-vindo • " + nomeUsuario);
        }

        imageAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
            intent.putExtra("nome_usuario", nomeUsuario);
            startActivity(intent);
        });

        Button btnModo = findViewById(R.id.btnModoFuncionamento);
        btnModo.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ModoFuncionamentoActivity.class);
            startActivity(intent);
        });

        Button btnDispensa = findViewById(R.id.btnAcessarDispensa);
        btnDispensa.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, DispensaActivity.class);
            intent.putExtra("nome_usuario", nomeUsuario);
            startActivity(intent);
        });

        Button btnEscanear = findViewById(R.id.btnEscanear);
        btnEscanear.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddProdutoActivity.class);
            intent.putExtra("nome_usuario", nomeUsuario);
            startActivity(intent);
        });

        Button btnPlano = findViewById(R.id.btnPlano);
        btnPlano.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlanosActivity.class);
            startActivity(intent);
        });

        // Chamada inicial (pode ser removida, mas manteremos por segurança)
        carregarAlertas();
    }

    // MÉTODO NOVO: CHAMA carregarAlertas SEMPRE QUE A TELA VOLTA A FICAR VISÍVEL
    @Override
    protected void onResume() {
        super.onResume();
        carregarAlertas();
    }

    private void carregarAlertas() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String nomeUsuario = getIntent().getStringExtra("nome_usuario");

        // NOTE: Use os IDs corretos que definimos no XML
        TextView alertaVencidos = findViewById(R.id.alertaContadorVencidos);
        TextView alertaProximosVencer = findViewById(R.id.alertaProximosVencer);

        if (alertaVencidos == null || alertaProximosVencer == null || nomeUsuario == null) {
            return;
        }

        db.collection("usuarios").document(nomeUsuario)
                .collection("dispensa")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    int vencidos = 0;
                    int proximos = 0;

                    SimpleDateFormat formatoISO = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat formatoBR = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    Date hoje = cal.getTime();

                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String dataValidade = doc.getString("dataValidade");

                        if (dataValidade != null && !dataValidade.isEmpty()) {
                            try {
                                Date validade = null;

                                try {
                                    validade = formatoISO.parse(dataValidade);
                                } catch (ParseException ignored) {
                                    validade = formatoBR.parse(dataValidade);
                                }

                                if (validade != null) {
                                    long diff = validade.getTime() - hoje.getTime();
                                    long dias = diff / (1000 * 60 * 60 * 24);

                                    // Lógica Corrigida para Vencidos (caixa vermelha)
                                    if (diff < 0) {
                                        vencidos++;
                                    }
                                    // Lógica para Próximos (caixa amarela)
                                    else if (dias <= 10) {
                                        proximos++;
                                    }
                                }
                            } catch (Exception ignored) {
                            }
                        }
                    }

                    if (vencidos > 0) {
                        alertaVencidos.setText(vencidos + " produto" + (vencidos > 1 ? "s" : "") + " vencido" + (vencidos > 1 ? "s" : "") + " • confira agora");
                    } else {
                        alertaVencidos.setText("Nenhum produto vencido");
                    }

                    if (proximos > 0) {
                        alertaProximosVencer.setText(proximos + " produto" + (proximos > 1 ? "s" : "") + " próximo" + (proximos > 1 ? "s" : "") + " do vencimento");
                    } else {
                        alertaProximosVencer.setText("Nenhum produto próximo de vencer");
                    }

                })
                .addOnFailureListener(e -> {
                    alertaVencidos.setText("Erro ao carregar alertas");
                    alertaProximosVencer.setText("Erro ao carregar alertas");
                });
    }
}