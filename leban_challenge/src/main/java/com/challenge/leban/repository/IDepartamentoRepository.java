package com.challenge.leban.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.challenge.leban.entity.Departamento;

public interface IDepartamentoRepository extends JpaRepository<Departamento, String>, JpaSpecificationExecutor<Departamento> {

}
