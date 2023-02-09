package com.generation.blogpessoal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "tb_postagens")// transformando a class  Postagem em uma tabela

public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // A variavel id será auto encrementada
    private Long id;

    @NotBlank  // A variavel titulo precisa ser obrigatoriamente preenchida
    @Size(min = 4 , max = 100)  // A variavel titulo precisa ter obrigatoriamente no minimo 4 caracteris e no maximo 100
    private String titulo;

    @NotBlank  // A variavel texto precisa ser obrigatoriamente preenchida
    @Size(min = 10 , max = 1000)  // A variavel texto precisa ter obrigatoriamente no minimo 10 caracteris e no maximo 1000
    private String texto;

    @UpdateTimestamp // configura a data com relação a data do sistema
    private LocalDateTime data;

    //A Classe Postagem será o lado N:1, ou seja, Muitas Postagens podem ter apenas Um Tema. Para criar a Relação vamos inserir depois do último Atributo da Classe Postagem

    //A anotação @ManyToOne indica que a Classe Postagem será o lado N:1 e terá um Objeto da Classe Tema, que no modelo Relacional será a Chave Estrangeira na Tabela tb_postagens (tema_id).
   //A anotação @JsonIgnoreProperties indica que uma parte do JSON será ignorado, ou seja, como a Relação entre as Classes será do tipo Bidirecional
    //
    @ManyToOne
    @JsonIgnoreProperties("postagem")
    private Tema tema;
    @ManyToOne
    @JsonIgnoreProperties("postagem")
    private Usuario usuario;

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
