package com.example.testesmartstock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    private ArrayList<Produto> listaProdutos;
    private OnProdutoDeleteListener listener;

    // Interface para comunicação com a Activity
    public interface OnProdutoDeleteListener {
        void onDeleteClick(String produtoId, int position);
    }

    public ProdutoAdapter(ArrayList<Produto> listaProdutos, OnProdutoDeleteListener listener) {
        this.listaProdutos = listaProdutos;
        this.listener = listener;
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

        holder.textNome.setText(produto.getNome());
        holder.textCategoria.setText("Categoria: " + produto.getCategoria());
        holder.textQuantidade.setText("Quantidade: " + produto.getQuantidade() + " " + produto.getUnidade());

        // Formatar data de validade para DD-MM-AAAA
        String validadeISO = produto.getDataValidade(); // Ex: 2025-08-06
        String validadeFormatada = validadeISO;
        try {
            SimpleDateFormat formatoISO = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat formatoBR = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date data = formatoISO.parse(validadeISO);
            validadeFormatada = formatoBR.format(data);
        } catch (Exception e) {
            // Em caso de erro, usa o valor original
        }

        holder.textValidade.setText("Validade: " + validadeFormatada);

        // Clique no botão excluir
        holder.btnExcluir.setOnClickListener(v -> {
            if (listener != null && produto.getId() != null) {
                listener.onDeleteClick(produto.getId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        TextView textNome, textCategoria, textQuantidade, textValidade;
        Button btnExcluir;

        public ProdutoViewHolder(View itemView) {
            super(itemView);
            textNome = itemView.findViewById(R.id.textNome);
            textCategoria = itemView.findViewById(R.id.textCategoria);
            textQuantidade = itemView.findViewById(R.id.textQuantidade);
            textValidade = itemView.findViewById(R.id.textValidade);
            btnExcluir = itemView.findViewById(R.id.btnExcluir); // botão no XML
        }
    }
}

