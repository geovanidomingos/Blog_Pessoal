package com.generation.blogpessoal.controller;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.PrivilegedAction;
import java.util.List;
import java.util.Optional;

@RestController // Deﬁne que a Classe é do tipo RestController, que receberá requisições

@RequestMapping("/postagens") // A anotação @RequestMapping é usada para mapear as solicitações para os Métodos da Classe controladora

@CrossOrigin(origins = "*", allowedHeaders = "*") // A anotação @CrossOrigin indica que a Classe controladora permitirá o recebimento de requisições realizadas de fora do domínio (localhost e futuramente do heroku quando o deploy for realizado) ao qual ela pertence.

public class PostagemController {
    @Autowired //(Injeção de Dependência), é a implementação utilizada pelo Spring Framework para aplicar a Inversão de Controle (IoC) quando for necessário. A Injeção de Dependência define quais Classes serão instanciadas e em quais lugares serão Injetadas quando houver necessidade.
    private PostagemRepository PostagemRepository;
    @Autowired
    private TemaRepository temaRepository;

    @GetMapping // mapeia todas as Requisições HTTP GET, enviadas para um endereço específico, chamado endpoint, dentro do Recurso Postagem
    public ResponseEntity<List<Postagem>> getAll(){
        return ResponseEntity.ok(PostagemRepository.findAll());
    }

    //Vamos implementar o Método getById(Long id) na Classe Postagem Controller, que retornará um Objeto específico persistido no Banco de dados, identificado pelo id
    // (Identificador único do Objeto). Traçando um paralelo com o MySQL, seria o equivalente a instrução: SELECT * FROM tb_postagens where id = id;

    @GetMapping("/{id}")
    public ResponseEntity<Postagem> getById(@PathVariable Long id){
        return PostagemRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }
   // Vamos implementar o Método getByTitulo(String titulo) na Classe Postagem Controller. Traçando um paralelo com o MySQL, seria o equivalente a
    //instrução: SELECT * FROM tb_postagens where titulo like "%titulo%";.


    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
        return ResponseEntity.ok(PostagemRepository.findAllByTituloContainingIgnoreCase(titulo));
    }
    /*CADASTRAR*/
    //Vamos implementar o Método post(Postagem postagem) na Classe Postagem Controller. Traçando um paralelo com o MySQL, seria o
    //equivalente a instrução: INSERT INTO tb_postagens (titulo, texto, data) VALUES ("Título", "Texto", CURRENT_TIMESTAMP());.

    @PostMapping
    public  ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
        if (temaRepository.existsById(postagem.getTema().getId()))
             return ResponseEntity.status(HttpStatus.CREATED).body(PostagemRepository.save(postagem));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /*ATUALIZAR*/
    //Vamos implementar o Método put(Postagem postagem) na Classe Postagem Controller. Observe que ele é muito parecido com o Método post.
    //Traçando um paralelo com o MySQL, seria o equivalente a instrução: UPDATE tb_postagens SET titulo = "titulo", texto = "texto", data = CURRENT_TIMESTAMP() WHERE id = id;.

    @PutMapping
    public  ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {
        if (PostagemRepository.existsById(postagem.getId())){

            if (temaRepository.existsById(postagem.getTema().getId()))
                return ResponseEntity.status(HttpStatus.OK).body(PostagemRepository.save(postagem));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /*DELETAR*/
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        Optional<Postagem> postagem = PostagemRepository.findById(id);
        if(postagem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        PostagemRepository.deleteById(id);
    }



}

