package com.example.testesmartstock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.util.Log;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    private Context context;
    private List<Produto> listaProdutos;
    private String nomeUsuario;


    public ProdutoAdapter(Context context, List<Produto> listaProdutos, String nomeUsuario) {
        this.context = context;
        this.listaProdutos = listaProdutos;
        this.nomeUsuario = nomeUsuario;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produto, parent, false);
        return new ProdutoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Produto produto = listaProdutos.get(position);

        String caminhoCompleto = "usuarios/" + this.nomeUsuario + "/dispensa/" + produto.getId();
        Log.d("EXCLUIR_PRODUTO", "Caminho de exclusão: " + caminhoCompleto);

        holder.textNome.setText(produto.getNome());
        holder.textCategoria.setText("Categoria: " + produto.getCategoria());
        holder.textQuantidade.setText("Quantidade: " + produto.getQuantidade() + " " + produto.getUnidade());

        // Formatando validade
        String validadeISO = produto.getDataValidade();
        String validadeFormatada = validadeISO;
        try {
            SimpleDateFormat formatoISO = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat formatoBR = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date data = formatoISO.parse(validadeISO);
            validadeFormatada = formatoBR.format(data);
        } catch (Exception ignored) {}

        holder.textValidade.setText("Validade: " + validadeFormatada);

        // Clique longo para excluir
        holder.itemView.setOnLongClickListener(v -> {
            if (produto.getId() != null) {
                Log.d("ADAPTER", "Tentando excluir para o usuário: " + this.nomeUsuario);
                Log.d("ADAPTER", "ID do documento a ser excluído: " + produto.getId());
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("usuarios")
                        .document(this.nomeUsuario)
                        .collection("dispensa")
                        .document(produto.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            listaProdutos.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Produto excluído!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(context, "Erro ao excluir: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        TextView textNome, textCategoria, textQuantidade, textValidade;

        public ProdutoViewHolder(View itemView) {
            super(itemView);
            textNome = itemView.findViewById(R.id.textNome);
            textCategoria = itemView.findViewById(R.id.textCategoria);
            textQuantidade = itemView.findViewById(R.id.textQuantidade);
            textValidade = itemView.findViewById(R.id.textValidade);
        }
    }
}


