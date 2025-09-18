package org.example.lista_telefone_sm.repository;

import org.example.lista_telefone_sm.model.Contato;
import org.example.lista_telefone_sm.model.StatusContato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

    boolean existsByEmail(String email);

    Optional<Contato> findByEmail(String email);

    @Query("SELECT DISTINCT c FROM Contato c LEFT JOIN FETCH c.telefones LEFT JOIN FETCH c.enderecos LEFT JOIN FETCH c.grupos")
    @Override
    List<Contato> findAll();

    @Query("SELECT DISTINCT c FROM Contato c LEFT JOIN FETCH c.telefones LEFT JOIN FETCH c.enderecos LEFT JOIN FETCH c.grupos WHERE c.id = :id")
    @Override
    Optional<Contato> findById(@Param("id") Long id);

    @Query("SELECT DISTINCT c FROM Contato c LEFT JOIN FETCH c.telefones LEFT JOIN FETCH c.enderecos LEFT JOIN FETCH c.grupos WHERE c.status = :status")
    List<Contato> findByStatus(@Param("status") StatusContato status);

    @Query("SELECT DISTINCT c FROM Contato c LEFT JOIN FETCH c.telefones LEFT JOIN FETCH c.enderecos LEFT JOIN FETCH c.grupos WHERE c.grupos IS EMPTY")
    List<Contato> findByGruposIsEmpty();

    @Query("SELECT DISTINCT c FROM Contato c LEFT JOIN FETCH c.telefones LEFT JOIN FETCH c.enderecos LEFT JOIN FETCH c.grupos g WHERE g.id = :grupoId")
    List<Contato> findByGrupos_Id(@Param("grupoId") Long grupoId);

    @Query("SELECT DISTINCT c FROM Contato c LEFT JOIN FETCH c.telefones LEFT JOIN FETCH c.enderecos LEFT JOIN FETCH c.grupos WHERE lower(c.nome) LIKE lower(concat('%', :termo, '%')) OR lower(c.email) LIKE lower(concat('%', :termo, '%'))")
    List<Contato> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("termo") String termo, @Param("termo2") String termo2);
}
