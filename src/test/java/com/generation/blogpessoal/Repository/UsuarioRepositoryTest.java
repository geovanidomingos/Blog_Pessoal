package com.generation.blogpessoal.Repository;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @BeforeAll
    void start(){
        usuarioRepository.save(new Usuario(0L,"Geovani Domingos","geovani@mail.com.br","12345678","https://discord.com/channels/1055235729250320384/1055235729627820074/1072931420579835944"));
        usuarioRepository.save(new Usuario(0L,"Geovanii Domingos","geovanii2@mail.com.br","12345678","https://discord.com/channels/1055235729250320384/1055235729627820074/1072931420579835944"));
        usuarioRepository.save(new Usuario(0L,"Adriana Domingos","adriana@mail.com.br","12345678","https://discord.com/channels/1055235729250320384/1055235729627820074/1072931420579835944"));
        usuarioRepository.save(new Usuario(0L,"Paulo Antunes","paulo@mail.com.br","12345678","https://discord.com/channels/1055235729250320384/1055235729627820074/1072931420579835944"));
    }


    @Test
    @DisplayName("Retorna 3 usuario")
    public void deveRetornarUmUsuario() {
        Optional<Usuario> usuario = usuarioRepository.findByUsuario("geovani@mail.com.br");
        assertTrue(usuario.get().getUsuario().equals("geovani@mail.com.br"));
    }

    @Test
    @DisplayName("Retorna 3 usuarios")
    public void deveRetornaTresUsuarios() {
        List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Domingos");
        assertEquals(3, listaDeUsuarios.size());
        assertTrue(listaDeUsuarios.get(0).getNome().equals("Geovani Domingos"));
        assertTrue(listaDeUsuarios.get(1).getNome().equals("Geovanii Domingos"));
        assertTrue(listaDeUsuarios.get(2).getNome().equals("Adriana Domingos"));
    }

    @AfterAll
    public void end (){
        usuarioRepository.deleteAll();

    }

}
