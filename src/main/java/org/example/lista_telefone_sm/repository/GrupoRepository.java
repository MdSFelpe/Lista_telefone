package org.example.lista_telefone_sm.repository;


import org.example.lista_telefone_sm.model.Contato;
import org.example.lista_telefone_sm.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo,Long> {




}
