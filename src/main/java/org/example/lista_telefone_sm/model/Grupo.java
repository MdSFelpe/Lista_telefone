package org.example.lista_telefone_sm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "grupo")
@Data
@EqualsAndHashCode(exclude = "contatos")
@ToString(exclude = "contatos")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private boolean deletavel;

    @ManyToMany(mappedBy = "grupos")
    private Set<Contato> contatos = new HashSet<>();
}