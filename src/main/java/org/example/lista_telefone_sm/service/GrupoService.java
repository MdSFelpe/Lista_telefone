package org.example.lista_telefone_sm.service;

import org.example.lista_telefone_sm.model.Grupo;
import org.example.lista_telefone_sm.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GrupoService {
    @Autowired
    private GrupoRepository grupoRepository;

    public Grupo criargrupo(Grupo grupo){

    return grupoRepository.save(grupo);
    }

    @Transactional(readOnly = true)
    public List<Grupo> listartodos(){
        return grupoRepository.findAll();
    }



    public Grupo buscarPorId(Long id){
        return grupoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo %d encontrado com id : " +id));
    }


    public void deletarGrupo(Long grupoId) {

        Grupo grupo = buscarPorId(grupoId);


        if (!grupo.isDeletavel()) {
            throw new UnsupportedOperationException("O grupo '" + grupo.getNome() + "' é um grupo padrão e не pode ser deletado.");
        }


        grupoRepository.delete(grupo);
    }

}
