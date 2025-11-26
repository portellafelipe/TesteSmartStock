package com.example.testesmartstock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import android.util.Log;

public class HomeActivity extends AppCompatActivity {

    private TextView textSaudacao;
    private ImageView imageAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textSaudacao = findViewById(R.id.textSaudacao);
        imageAvatar = findViewById(R.id.imageAvatar); // Avatar

        String nomeUsuario = getIntent().getStringExtra("nome_usuario");
        if (nomeUsuario != null) {
            textSaudacao.setText("Olá, seja bem-vindo • " + nomeUsuario);
        }

        // Clicar no avatar leva para PerfilActivity
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
            Intent intent = new Intent(HomeActivity.this,DispensaActivity.class);
            intent.putExtra("nome_usuario", nomeUsuario);
            startActivity(intent);
        });

        Button btnEscanear = findViewById(R.id.btnEscanear);
        btnEscanear.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,AddProdutoActivity.class);
            intent.putExtra("nome_usuario", nomeUsuario);
            startActivity(intent);
        });

        Button btnPlano = findViewById(R.id.btnPlano);
        btnPlano.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlanosActivity.class);

            startActivity(intent);
        });
    }
    private void carregarAlertas() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String nomeUsuario = getIntent().getStringExtra("nome_usuario");
        Log.d("USER", "Usuário carregado: " + nomeUsuario);


        // 1. Inicia as Views com os IDs corretos do XML
        TextView alertaVencidos = findViewById(R.id.alertaVencidos);
        // Renomeamos para refletir que este campo é usado para 'Próximos de Vencer'
        TextView alertaProximosVencer = findViewById(R.id.alertaBaixoEstoque);

        // Validação inicial para evitar NullPointer
        if (alertaVencidos == null || alertaProximosVencer == null || nomeUsuario == null) {
            return; // Sai se não puder carregar as Views ou se não houver nome de usuário
        }

        db.collection("usuarios").document(nomeUsuario)
                .collection("dispensa")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    int vencidos = 0;
                    int proximos = 0;

                    // 2. Uso de ZonedDateTime para melhor precisão e timezone (boa prática)
                    // Usando o método antigo para compatibilidade com seu código:
                    SimpleDateFormat formatoISO = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat formatoBR = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                    // Zera as horas/minutos para comparar apenas as datas de forma mais justa
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    Date hoje = cal.getTime();

                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String dataValidade = doc.getString("dataValidade");
                        Log.d("DATA", "Validade recebida: " + dataValidade);

                        if (dataValidade != null && !dataValidade.isEmpty()) {
                            try {
                                Date validade = null;

                                try {
                                    validade = formatoISO.parse(dataValidade);
                                } catch (ParseException ignored) {
                                    validade = formatoBR.parse(dataValidade);
                                }

                                if (validade != null) {
                                    // Subtrai hoje para obter a diferença em milissegundos
                                    long diff = validade.getTime() - hoje.getTime();
                                    // Converte para dias
                                    long dias = diff / (1000 * 60 * 60 * 24);

                                    if (dias < 0) {
                                        vencidos++;
                                    } else if (dias <= 10) {
                                        proximos++;
                                    }
                                }
                            } catch (Exception ignored) {
                                // Ignora produtos com data inválida
                            }
                        }
                    }


                    // 3. Atualiza textos (Ajustado o texto de saída do alertaProximosVencer)
                    // Alerta Vencidos (Caixa Vermelha)
                    if (vencidos > 0) {
                        // Texto: "X produto(s) vencido(s) • confira agora"
                        alertaVencidos.setText(vencidos + " produto" + (vencidos > 1 ? "s" : "") + " vencido" + (vencidos > 1 ? "s" : "") + " • confira agora");
                    } else {
                        alertaVencidos.setText("Nenhum produto vencido");
                    }

                    // Alerta Próximos de Vencer (Caixa Amarela)
                    if (proximos > 0) {
                        // Texto: "X produto(s) próximo(s) do vencimento"
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


