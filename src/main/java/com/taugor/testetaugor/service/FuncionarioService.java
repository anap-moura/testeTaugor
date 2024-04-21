package com.taugor.testetaugor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taugor.testetaugor.model.Funcionario;
import com.taugor.testetaugor.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> findById(Long id) {
        return funcionarioRepository.findById(id);
    }

    public Funcionario cadastrarFuncionario(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public Optional<Funcionario> atualizarFuncionario(Long id, Funcionario funcionario) {
        Optional<Funcionario> funcionarioExistente = funcionarioRepository.findById(id);
        if (funcionarioExistente.isPresent()) {
            funcionario.setId(id);
            return Optional.of(funcionarioRepository.save(funcionario));
        } else {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        funcionarioRepository.deleteById(id);
    }

}
