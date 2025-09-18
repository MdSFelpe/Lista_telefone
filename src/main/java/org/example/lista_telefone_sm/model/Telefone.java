package org.example.lista_telefone_sm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "telefone")
@Data
@EqualsAndHashCode(exclude = "contato")
@ToString(exclude = "contato")
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O numero de telefone não pode ser vazio")
    @Size(min = 9, message = "O número deve-ter no minimo 9 caracteres")
    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private boolean principal;

    @NotNull(message = "Por favor, insira o tipo do número que foi informado.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTelefone tipo;

    // Relacionamento: Muitos Telefones pertencem a um Contato.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contato_id", nullable = false)
    private Contato contato;

}