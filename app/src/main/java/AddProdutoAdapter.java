package com.example.testesmartstock;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddProdutoAdapter extends RecyclerView.Adapter<AddProdutoAdapter.ViewHolder> {

    private final List<NotaFiscalScraper.ProdutoNF> listaProdutos;

    public AddProdutoAdapter(List<NotaFiscalScraper.ProdutoNF> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_produto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotaFiscalScraper.ProdutoNF produto = listaProdutos.get(position);

        holder.editNome.setText(produto.nome);
        holder.editQuantidade.setText(produto.quantidade);
        holder.editUnidade.setText(produto.unidade);
        holder.editValidade.setText(produto.validade);
        holder.editCategoria.setText(produto.categoria);

        // Atualiza a lista quando o usuário edita os campos
        holder.editNome.addTextChangedListener(new SimpleWatcher(s -> produto.nome = s));
        holder.editQuantidade.addTextChangedListener(new SimpleWatcher(s -> produto.quantidade = s));
        holder.editUnidade.addTextChangedListener(new SimpleWatcher(s -> produto.unidade = s));
        holder.editValidade.addTextChangedListener(new SimpleWatcher(s -> produto.validade = s));
        holder.editCategoria.addTextChangedListener(new SimpleWatcher(s -> produto.categoria = s));
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText editNome, editQuantidade, editUnidade, editValidade, editCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editNome = itemView.findViewById(R.id.editNomeProduto);
            editQuantidade = itemView.findViewById(R.id.editQuantidade);
            editUnidade = itemView.findViewById(R.id.editUnidade);
            editValidade = itemView.findViewById(R.id.editDataValidade);
            editCategoria = itemView.findViewById(R.id.editCategoria);
        }
    }

    // Classe helper para simplificar TextWatcher
    private static class SimpleWatcher implements TextWatcher {
        private final OnTextChanged onTextChanged;

        SimpleWatcher(OnTextChanged onTextChanged) {
            this.onTextChanged = onTextChanged;
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override public void afterTextChanged(Editable s) {
            onTextChanged.update(s.toString());
        }
    }

    private interface OnTextChanged {
        void update(String s);
    }
}
