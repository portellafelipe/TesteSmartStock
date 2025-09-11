package com.example.testesmartstock;

public class Produto {
    private String id;          // <--- novo campo para guardar o ID do documento
    private String nome;
    private int quantidade;
    private String unidade;
    private String dataValidade;
    private String categoria;

    public Produto() {
        // Construtor vazio necessário pro Firestore
    }

    public Produto(String nome, int quantidade, String unidade, String dataValidade, String categoria) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.dataValidade = dataValidade;
        this.categoria = categoria;
    }

    // ---- NOVOS GET/SET ----
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // ---- GETTERS EXISTENTES ----
    public String getNome() { return nome; }
    public int getQuantidade() { return quantidade; }
    public String getUnidade() { return unidade; }
    public String getDataValidade() { return dataValidade; }
    public String getCategoria() { return categoria; }

    // ---- SETTERS (caso precise atualizar dados no Firestore futuramente) ----
    public void setNome(String nome) { this.nome = nome; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public void setDataValidade(String dataValidade) { this.dataValidade = dataValidade; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}

