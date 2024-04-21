package com.taugor.testetaugor.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.taugor.testetaugor.model.Funcionario;
import com.taugor.testetaugor.service.FuncionarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/funcionarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping("/all")
    public ResponseEntity<List<Funcionario>> getAll() {
        return ResponseEntity.ok(funcionarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> findById(@PathVariable Long id) {
        Optional<Funcionario> funcionario = funcionarioService.findById(id);
        return funcionario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Funcionario> postFuncionario(@RequestBody @Valid Funcionario funcionario) {
        Funcionario funcionarioCriado = funcionarioService.cadastrarFuncionario(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioCriado);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> putFuncionario(@PathVariable Long id, @RequestBody @Valid Funcionario funcionario) {
        Optional<Funcionario> funcionarioAtualizado = funcionarioService.atualizarFuncionario(id, funcionario);
        return funcionarioAtualizado.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

      
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Funcionario> funcionario = funcionarioService.findById(id);
        
        if(funcionario.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        
        funcionarioService.deleteById(id);     
    }
}
