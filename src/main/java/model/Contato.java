package model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity // Anotação que informa ao JPA que esta classe corresponde a uma tabela no banco.
@Table(name = "contato") // Especifica o nome da tabela
@Data // Anotação do Lombok para gerar Getters, Setters, toString, etc...

public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Informa que o banco de dados irá gerar o valor do ID (usando --
    // IDENTITY(equivale ao Auto_Incremet do Mysql) do SQL Server).
    private Long id;

    @Pattern(regexp = "^[a-zA-ZÀ-ú\\s]+$", message = "O nome deve conter apenas letras e espaços.")
    @NotBlank(message = "O nome não pode ser vazio ou nulo.")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres.")
    @Column(nullable = false) // Mapeia para uma coluna, 'nullable = false' significa que não pode ser nula.
    private String nome;

    @NotBlank(message = "O e-mail não pode ser vazio ou nulo.")
    @Email(message = "O formato do e-mail é inválido.")
    @Column(unique = true, nullable = false) // O e-mail deve ser único no banco de dados e não pode ser nulo
    private String email;


    @NotNull(message = "A data de nascimento não pode ser nula.")
    private LocalDate dataNascimento;

    // Relacionamento: Um Contato pode ter muitos Telefones.
    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Telefone> telefones;

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos;
}
