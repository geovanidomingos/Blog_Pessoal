package com.generation.blogpessoal.Controller;

import com.generation.blogpessoal.Service.UsuarioService;
import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void start(){
        usuarioRepository.deleteAll();

        usuarioService.cadastrarUsuario(new Usuario(0L,"Root", "root@root.com", "rootroot", ""));
    }


    @Test
    @DisplayName("Cadastrar um Usuário")
    public void deveCriarUmUsuario() {
        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Paulo Antunes", "paulo_antunes@email.com.br", "12345678", "https://d2r9epyceweg5n.cloudfront.net/stores/001/573/072/products/vovo-juju-png1-e712e49d25a3a1277916243239597855-1024-1024.png"));
        ResponseEntity<Usuario> corpoResposta = testRestTemplate
                .exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
        assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
        assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
    }

    @Test
    @DisplayName("Não deve permitir duplicação do Usuário")
    public void naoDeveDuplicarUsuario() {
       usuarioService.cadastrarUsuario(new Usuario(0L,"Maria Da Silva", "maria_silva@email.com.br","12345678","https://d2r9epyceweg5n.cloudfront.net/stores/001/573/072/products/vovo-juju-png1-e712e49d25a3a1277916243239597855-1024-1024.png"));
       HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L,"Maria Da Silva", "maria_silva@email.com.br","12345678","https://d2r9epyceweg5n.cloudfront.net/stores/001/573/072/products/vovo-juju-png1-e712e49d25a3a1277916243239597855-1024-1024.png"));

       ResponseEntity<Usuario> corpoResposta = testRestTemplate
               .exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
    }


    @Test
    @DisplayName("Atualizar um Usuário")
    public void deveAtualizarUmUsuario(){
        Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, "Juliana Andrews","juliana_andraws@email.com.br", "juliana123", "https://discord.com/channels/1055235729250320384/1055235729627820074/1072931420579835944"));
        Usuario usuariUpdate = new Usuario(usuarioCadastrado.get().getId(),
        "Juliana Andrews Ramos","juliana_ramos@email.com.br", "juliana123", "https://discord.com/channels/1055235729250320384/1055235729627820074/1072931420579835944");

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuariUpdate);
        ResponseEntity<Usuario> corpoResposta = testRestTemplate
                .withBasicAuth("root@root.com", "rootroot")
                .exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
        assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
    }
    @Test
    @DisplayName("Listar todos os Usuários")
    public void deveMostrarTodososUsuarios(){
        usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123","https://discord.com/channels/1055235729250320384/1055235729627820074/1072931546056634408"));
        usuarioService.cadastrarUsuario(new Usuario(0L, "Ricaedo Marques", "ricardo_marques@email.com.br", "ricardo123", "https://discord.com/channels/1055235729250320384/1055235729627820074/1072931546056634408"));

        ResponseEntity<String> resposta = testRestTemplate
                .withBasicAuth("root@root.com", "rootroot")
                .exchange("/usuarios/all", HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

}
