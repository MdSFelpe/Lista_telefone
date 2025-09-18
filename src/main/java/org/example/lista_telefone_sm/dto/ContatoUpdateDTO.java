package org.example.lista_telefone_sm.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContatoUpdateDTO {


    private String nome;
    private String email;
    private LocalDate dataNascimento;

}
