package org.example.lista_telefone_sm.service;


import org.example.lista_telefone_sm.model.Contato;
import org.example.lista_telefone_sm.model.Endereco;
import org.example.lista_telefone_sm.model.Telefone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.lista_telefone_sm.repository.ContatoRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service //  Anotação que marca esta classe como um componente de serviço do Spring.
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;
    // Injeção de Dependência: O Spring vai criar e gerenciar uma instância do
    // ContatoRepository e "injetá-la" aqui para ser usado.


    //========================================CREATE PART============================================================
    //Criar um novo contato (CREATE)
    public Contato criarcontato(Contato contato)    {
        //Passo 1: Sanitizar os dados
        sanitizarContato(contato);

        //Passo 2: Validar as regras de negócio
        validarRegrasDeNegocioParaCriacao(contato);

        //Passo 3 : Persistir no banco de dados
        return contatoRepository.save(contato);
    }


    //========================================READ PART============================================================
    //Ler todos os contatos(READ)
    public List<Contato> listarContatos() {
        return contatoRepository.findAll();
    }


    //Ler um contato especifico (READ)
    public Contato buscarPorId(Long id) {
        return contatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado com id: " + id));
    }

    public List<Contato> buscarPorTermo(String termo) {
        // Se passa o mesmo termo para os dois parâmetros do metodo do repositório.
        return contatoRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(termo, termo);
    }


    //========================================UPDATE PART============================================================
    //Atualizar um contato (UPDATE)
    public Contato updateContact(Long id, Contato contatoAtualizado) {
        Contato contatoExistente = buscarPorId(id);

        // Passo 1: Sanitizar os dados que chegaram
        sanitizarContato(contatoAtualizado);

        // Passo 2: Validar as regras de negócio para os novos dados
        validarRegrasDeNegocioParaUpdate(contatoAtualizado, contatoExistente);

        // Atualiza os dados do objeto existente
        contatoExistente.setNome(contatoAtualizado.getNome());
        contatoExistente.setEmail(contatoAtualizado.getEmail());
        contatoExistente.setDataNascimento(contatoAtualizado.getDataNascimento());

        //Lógica para atualizar as listas (Adicionadas)
        contatoExistente.getTelefones().clear();
        if (contatoAtualizado.getTelefones() != null) {
            for (Telefone tel : contatoAtualizado.getTelefones()) {
                tel.setContato(contatoExistente);
                contatoExistente.getTelefones().add(tel);
            }
        }

        contatoExistente.getEnderecos().clear();
        if (contatoAtualizado.getEnderecos() != null) {
            for (Endereco end : contatoAtualizado.getEnderecos()) {
                end.setContato(contatoExistente);
                contatoExistente.getEnderecos().add(end);
            }
        }

        return contatoRepository.save(contatoExistente);
    }

    //========================================DELETE PART============================================================
    //Deletar um contato (DELETE)
    public void deletarContato(Long id) {
        Contato contatoParaDeletar = buscarPorId(id);
        contatoRepository.delete(contatoParaDeletar);
    }




    // -- Métodos privados para lógica -- //

    private void sanitizarContato(Contato contato) { //Usa como uma verificação de segurança para evitar erros..
        if (contato.getNome() != null) {                //..(NullPointerException) caso o nome não tenha sido informado.
            contato.setNome(contato.getNome().trim().replaceAll("\\s+", " "));
        }
    }



    // -- Lógica validação -- //

    private void validarRegrasDeNegocioParaCriacao(Contato contato) {
        validarIdade(contato);
        validarEmailDuplicadoParaCriacao(contato);
    }

    private void validarRegrasDeNegocioParaUpdate(Contato contatoAtualizado, Contato contatoExistente) {
        validarIdade(contatoAtualizado);
        validarEmailDuplicadoParaUpdate(contatoAtualizado, contatoExistente);
    }

    private void validarIdade(Contato contato) {
        if (contato.getDataNascimento() != null) {
            int idade = Period.between(contato.getDataNascimento(), LocalDate.now()).getYears();
            if (idade < 18) {
                throw new IllegalArgumentException("O contato deve ter no mínimo 18 anos.");
            }
        }
    }

    private void validarEmailDuplicadoParaCriacao(Contato contato) {
        if (contatoRepository.existsByEmail(contato.getEmail())) {
            throw new IllegalArgumentException("Este e-mail já está em uso.");
        }
    }

    private void validarEmailDuplicadoParaUpdate(Contato contatoAtualizado, Contato contatoExistente) {
        Optional<Contato> contatoEncontrado = contatoRepository.findByEmail(contatoAtualizado.getEmail());

        if (contatoEncontrado.isPresent() && !contatoEncontrado.get().getId().equals(contatoExistente.getId())) {
            throw new IllegalArgumentException("Este e-mail já está em uso por outro contato.");
        }
    }

    }

