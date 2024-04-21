package com.taugor.testetaugor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taugor.testetaugor.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{

}