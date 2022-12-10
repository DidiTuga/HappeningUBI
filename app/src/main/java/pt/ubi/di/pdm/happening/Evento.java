package pt.ubi.di.pdm.happening;

import com.google.firebase.Timestamp;


public class Evento {
    String nome;
    String descricao;
    String local;
    String link;
    Timestamp data;

    public Evento(String nome, String descricao, String local, String link, Timestamp data) {
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.link = link;
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

}
