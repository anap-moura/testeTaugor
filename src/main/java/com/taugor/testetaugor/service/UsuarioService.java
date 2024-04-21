package com.taugor.testetaugor.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.taugor.testetaugor.model.UsuarioLogin;
import com.taugor.testetaugor.model.Usuario;
import com.taugor.testetaugor.repository.UsuarioRepository;
import com.taugor.testetaugor.security.JwtService;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

        if (usuarioLogin.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário ou senha inválidos", null);
        }

        String email = usuarioLogin.get().getEmail(); // Alterado de "usuario" para "email" conforme sua última atualização
        String senha = usuarioLogin.get().getSenha();

        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            // Comparar a senha fornecida com a senha criptografada armazenada no banco de dados
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(senha, usuario.getSenha())) {

                // Senhas coincidem, autenticação bem-sucedida
                usuarioLogin.get().setId(usuario.getId());
                usuarioLogin.get().setNome(usuario.getNome());
                usuarioLogin.get().setFoto(usuario.getFoto());
                usuarioLogin.get().setToken(gerarToken(usuario.getEmail()));
                usuarioLogin.get().setSenha("");

                return usuarioLogin;
            }
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário ou senha inválidos", null);
    }

    public Optional<Usuario> cadastrarEmail(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O email do usuário é obrigatório", null);
        }

        // Criptografa a senha antes de salvar
        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        // Salva o usuário no banco de dados
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Retorna o usuário com a senha criptografada
        return Optional.of(usuarioSalvo);
    }

    public Optional<Usuario> atualizarEmail(Usuario usuario) {
      
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent()) {
            Usuario usuarioAtualizado = usuarioRepository.save(usuario);
            return Optional.of(usuarioAtualizado);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado", null);
        }
    }

    private String gerarToken(String email) {
        return "Bearer " + jwtService.generateToken(email);
    }


}
