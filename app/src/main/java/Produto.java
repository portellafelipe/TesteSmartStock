package com.example.testesmartstock;

public class Produto {
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

    public String getNome() { return nome; }
    public int getQuantidade() { return quantidade; }
    public String getUnidade() { return unidade; }
    public String getDataValidade() { return dataValidade; }
    public String getCategoria() { return categoria; }
}
