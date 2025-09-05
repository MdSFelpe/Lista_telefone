package service;


import model.Contato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ContatoRepository;

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


    //Criar um novo contato (CREATE)
    public Contato createnewcontact(Contato contato)    {
        //Passo 1: Sanitizar os dados
        sanitizarContato(contato);

        //Passo 2: Validar as regras de negócio
        validarRegrasdeNegocio(contato);

        //Passo 3 : Persistir no banco de dados
        return contatoRepository.save(contato);
    }


    //Ler todos os contatos(READ)
    public List<Contato> listall() {
        return contatoRepository.findAll();
    }


    //Ler um contato especifico (READ)
    public Contato Listuniq(Long id) {
        return contatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado com id: " + id));
    }


    //Atualizar um contato (UPDATE)
    public Contato updateContact(Long id, Contato contatoAtualizado) {
        Contato contatoExistente = buscarp(id);

        // Passo 1: Sanitizar os dados que chegaram
        sanitizarContato(contatoAtualizado);

        // Passo 2: Validar as regras de negócio para os novos dados
        ValidRegrasNegocUpdate(contatoAtualizado, contatoExistente);

        // Atualiza os dados do objeto existente
        contatoExistente.setNome(contatoAtualizado.getNome());
        contatoExistente.setEmail(contatoAtualizado.getEmail());
        contatoExistente.setDataNascimento(contatoAtualizado.getDataNascimento());

        return contatoRepository.save(contatoExistente);
    }

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

    private void validarRegrasDeNegocioParaCriacao(Contato contato) {
        validarIdade(contato);
        validarEmailDuplicadoParaCriacao(contato);
    }

    private void validarRegrasDeNegocioParaUpdate(Contato contatoAtualizado, Contato contatoExistente) {
        validarIdade(contatoAtualizado);
        validarEmailDuplicadoParaUpdate(contatoAtualizado, contatoExistente);
    }


    // -- Lógica validação -- //

    private void validarIdade(Contato contato) {
        if (contato.getDataNascimento() != null) {
            int idade = Period.between(contato.getDataNascimento(), LocalDate.now()).getYears();
            if (idade < 18) {
                throw new IllegalArgumentException("O contato deve ter no mínimo 18 anos.");
            }
        }
    }

    private void validarEmailDuplicadoParaCriacao(Contato contato) {
        if (contatoRepository.existsByemail(contato.getEmail())) {
            throw new IllegalArgumentException("Este e-mail já está em uso.");
        }
    }

    private void validarEmailDuplicadoParaUpdate(Contato contatoAtualizado, Contato contatoExistente) {
        Optional<Contato> contatoEncontrado = contatoRepository.findByemail(contatoAtualizado.getEmail());

        if (contatoEncontrado.isPresent() && !contatoEncontrado.get().getId().equals(contatoExistente.getId())) {
            throw new IllegalArgumentException("Este e-mail já está em uso por outro contato.");
        }
    }

    }

