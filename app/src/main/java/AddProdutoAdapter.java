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

        // 1. REMOVER LISTENERS ANTIGOS (Crucial para reciclagem!)
        holder.editNome.removeTextChangedListener(holder.nomeWatcher);
        holder.editQuantidade.removeTextChangedListener(holder.quantidadeWatcher);
        holder.editUnidade.removeTextChangedListener(holder.unidadeWatcher);
        holder.editValidade.removeTextChangedListener(holder.validadeWatcher);
        holder.editCategoria.removeTextChangedListener(holder.categoriaWatcher);

        // 2. ATUALIZAR VIEWS COM DADOS DO NOVO ITEM
        // Garantir que todos os campos sejam preenchidos (limpa o estado do item anterior)
        holder.editNome.setText(produto.nome);
        holder.editQuantidade.setText(produto.quantidade);
        holder.editUnidade.setText(produto.unidade);
        holder.editValidade.setText(produto.validade);
        holder.editCategoria.setText(produto.categoria);

        // 3. ANEXAR NOVOS LISTENERS, ATUALIZANDO O OBJETO DE DADOS (ProdutoNF)

        // Atualiza a referência de atualização para o PRODUTO atual
        holder.nomeWatcher.setOnTextChanged(s -> produto.nome = s);
        holder.quantidadeWatcher.setOnTextChanged(s -> produto.quantidade = s);
        holder.unidadeWatcher.setOnTextChanged(s -> produto.unidade = s);
        holder.validadeWatcher.setOnTextChanged(s -> produto.validade = s);
        holder.categoriaWatcher.setOnTextChanged(s -> produto.categoria = s);

        // Reanexar os listeners
        holder.editNome.addTextChangedListener(holder.nomeWatcher);
        holder.editQuantidade.addTextChangedListener(holder.quantidadeWatcher);
        holder.editUnidade.addTextChangedListener(holder.unidadeWatcher);
        holder.editValidade.addTextChangedListener(holder.validadeWatcher);
        holder.editCategoria.addTextChangedListener(holder.categoriaWatcher);
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText editNome, editQuantidade, editUnidade, editValidade, editCategoria;
        SimpleWatcher nomeWatcher, quantidadeWatcher, unidadeWatcher, validadeWatcher, categoriaWatcher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editNome = itemView.findViewById(R.id.editNomeProduto);
            editQuantidade = itemView.findViewById(R.id.editQuantidade);
            editUnidade = itemView.findViewById(R.id.editUnidade);
            editValidade = itemView.findViewById(R.id.editDataValidade);
            editCategoria = itemView.findViewById(R.id.editCategoria);

            // Inicializa os Watchers uma única vez por ViewHolder
            nomeWatcher = new SimpleWatcher(s -> {}); // Inicialização vazia
            quantidadeWatcher = new SimpleWatcher(s -> {});
            unidadeWatcher = new SimpleWatcher(s -> {});
            validadeWatcher = new SimpleWatcher(s -> {});
            categoriaWatcher = new SimpleWatcher(s -> {});

            // Anexar inicialmente (poderia ser feito no onBind, mas assim garante que os objetos existem)
            // editNome.addTextChangedListener(nomeWatcher); // Removido para ser mais limpo no onBind
        }
    }

    // Classe helper para simplificar TextWatcher (Modificada para ser reutilizável)
    private static class SimpleWatcher implements TextWatcher {
        private OnTextChanged onTextChanged;

        SimpleWatcher(OnTextChanged onTextChanged) {
            this.onTextChanged = onTextChanged;
        }

        public void setOnTextChanged(OnTextChanged onTextChanged) {
            this.onTextChanged = onTextChanged;
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override public void afterTextChanged(Editable s) {
            if (onTextChanged != null) {
                onTextChanged.update(s.toString());
            }
        }
    }

    private interface OnTextChanged {
        void update(String s);
    }
}
