package com.example.testesmartstock;

public class Produto {
    private String id;          // ID do documento no Firestore
    private String nome;
    private String quantidade;
    private String unidade;
    private String dataValidade;
    private String categoria;

    public Produto() {
        // Construtor vazio necessário pro Firestore
    }

    public Produto(String nome, String quantidade, String unidade, String dataValidade, String categoria) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.dataValidade = dataValidade;
        this.categoria = categoria;
    }

    // ---- GETTERS E SETTERS ----
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getQuantidade() { return quantidade; }
    public void setQuantidade(String quantidade) { this.quantidade = quantidade; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public String getDataValidade() { return dataValidade; }
    public void setDataValidade(String dataValidade) { this.dataValidade = dataValidade; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}


