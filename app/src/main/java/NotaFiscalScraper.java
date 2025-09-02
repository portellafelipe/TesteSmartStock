package com.example.testesmartstock;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import android.util.Log;

/**
 * Faz o scraping da página pública da NFC-e do PR.
 * Extrai apenas o nome, quantidade e unidade de cada item.
 */
public class NotaFiscalScraper {

    public static class ProdutoNF {
        public String nome;
        public String quantidade;
        public String unidade;

        public ProdutoNF(String nome, String quantidade, String unidade) {
            this.nome = nome;
            this.quantidade = quantidade;
            this.unidade = unidade;
        }
    }

    /** Retorna a lista de produtos encontrados na NFC-e do PR. */
    public static List<ProdutoNF> extrairProdutos(String url) {
        List<ProdutoNF> itens = new ArrayList<>();

        try {
            Connection.Response response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Android) AppleWebKit/537.36 Chrome/120 Mobile Safari/537.36")
                    .timeout(15000)
                    .ignoreHttpErrors(true)
                    .execute();

            Log.d("SCRAPER", "HTTP Status: " + response.statusCode());

            Document doc = response.parse();

            if (doc == null) {
                Log.e("SCRAPER", "Documento retornou null!");
                return itens; // retorna lista vazia
            }

            Elements produtos = doc.select("tr[id^=Item]");
            Log.d("SCRAPER", "Qtd de produtos encontrados no HTML: " + produtos.size());
            for (Element prod : produtos) {
                String nome = prod.select("span.txtTit2").text();
                String qtd = prod.select("span.Rqtd").text().replace("Qtde.:", "").trim();
                String un = prod.select("span.RUN").text().replace("UN:", "").trim();

                ProdutoNF p = new ProdutoNF(nome, qtd, un);
                itens.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SCRAPER", "Erro ao extrair produtos: " + e.getMessage());
        }

        return itens;
    }

}

