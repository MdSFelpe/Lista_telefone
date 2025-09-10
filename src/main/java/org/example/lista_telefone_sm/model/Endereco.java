package org.example.lista_telefone_sm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Entity
@Table(name = "endereco")
@Data
public class Endereco {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank(message = "O Cep não pode ser vazio")
        @Size(min = 8, max = 9, message = "O CEP deve ter entre 8 a 9 caracteres.")
        @Column(nullable = false)
        private String cep;

        @NotBlank(message = "Deve-se informar a rua que o contato mora")
        @Column(nullable = false)
        private String rua;


        @NotBlank(message = "Informe o número do lugar em que o contato mora")
        @Column(nullable = false)
        private String numero;


        @Column(nullable = false)
        private boolean principal;

        @NotNull(message = "O tipo de endereço não pode ser nulo")
        @Column(nullable = false)
        @Enumerated(EnumType.STRING) // Diz ao JPA para salvar o NOME do enum ("RESIDENCIAL") no banco, e não o número (0).
        private TipoEndereco tipo;

        // Relacionamento: Um endereço pertence a um Contato.
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "contato_id", nullable = false)
        private Contato contato;
    }

