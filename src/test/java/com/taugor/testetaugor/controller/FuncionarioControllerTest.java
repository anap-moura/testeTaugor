package com.taugor.testetaugor.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.taugor.testetaugor.model.Funcionario;
import com.taugor.testetaugor.repository.FuncionarioRepository;
import com.taugor.testetaugor.service.FuncionarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FuncionarioControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @BeforeAll
    void start() {
        funcionarioRepository.deleteAll();

        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Funcionario Teste");
        funcionario.setSexo("M");
        funcionario.setEndereco("Rua Teste, 123");
        funcionario.setTelefone("1122334455");
        funcionario.setFotoPerfil("foto.jpg");
        funcionario.setDataAniversario(LocalDate.of(1990, 1, 1));
        funcionario.setCargo("Analista");
        funcionario.setDataAdmissao(LocalDate.now());
        funcionario.setSetor("TI");
        funcionario.setSalario(3000.00);

        funcionarioService.cadastrarFuncionario(funcionario);
    }

    @Test
    @DisplayName("Cadastrar um Funcionário")
    public void deveCadastrarUmFuncionario() {
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNome("Novo Funcionário");
        novoFuncionario.setSexo("F");
        novoFuncionario.setEndereco("Rua Nova, 456");
        novoFuncionario.setTelefone("9988776655");
        novoFuncionario.setFotoPerfil("foto_nova.jpg");
        novoFuncionario.setDataAniversario(LocalDate.of(1995, 5, 10));
        novoFuncionario.setCargo("Desenvolvedor");
        novoFuncionario.setDataAdmissao(LocalDate.now());
        novoFuncionario.setSetor("Desenvolvimento");
        novoFuncionario.setSalario(4000.00);

        ResponseEntity<Funcionario> resposta = testRestTemplate.postForEntity("/funcionarios/cadastrar", novoFuncionario,
                Funcionario.class);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
    }

    @Test
    @DisplayName("Atualizar um Funcionário")
    public void deveAtualizarUmFuncionario() {
        Funcionario funcionarioExistente = funcionarioRepository.findAll().get(0);
        funcionarioExistente.setNome("Funcionário Atualizado");

        HttpEntity<Funcionario> corpoRequisicao = new HttpEntity<>(funcionarioExistente);

        ResponseEntity<Funcionario> resposta = testRestTemplate
                .exchange("/funcionarios/" + funcionarioExistente.getId(), HttpMethod.PUT, corpoRequisicao,
                        Funcionario.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    @DisplayName("Listar todos os Funcionários")
    public void deveListarTodosFuncionarios() {
        ResponseEntity<String> resposta = testRestTemplate.getForEntity("/funcionarios/all", String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    @DisplayName("Deletar um Funcionário")
    public void deveDeletarUmFuncionario() {
        Funcionario funcionarioExistente = funcionarioRepository.findAll().get(0);

        ResponseEntity<Void> resposta = testRestTemplate.exchange("/funcionarios/" + funcionarioExistente.getId(),
                HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
    }

}
