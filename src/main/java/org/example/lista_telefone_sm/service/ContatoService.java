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


    //========================================  CREATE =================================================================
    //Criar um novo contato (CREATE)
    public Contato criarcontato(Contato contato)    {
        //Passo 1: Sanitizar os dados
        sanitizarContato(contato);

        //Passo 2: Validar as regras de negócio
        validarRegrasDeNegocioParaCriacao(contato);

        //Passo 3 : Persistir no banco de dados
        return contatoRepository.save(contato);
    }

    public Contato adicionarTelefone(Long contatoId, Telefone novoTelefone) {
        Contato contato = buscarPorId(contatoId);
        if (contato.getTelefones().isEmpty()) {
            novoTelefone.setPrincipal(true); // Por padrão, o primeiro número cadastrado sempre vai vir como principal
        }
        novoTelefone.setContato(contato);
        contato.getTelefones().add(novoTelefone);
        return contatoRepository.save(contato);
    }


    public Contato adicionarEndereco(Long contatoId, Endereco novoEndereco) {
        Contato contato = buscarPorId(contatoId);
        if (contato.getEnderecos().isEmpty()) {
            novoEndereco.setPrincipal(true); // mesma lógica de principal do "adicionartelefone"
        }
        novoEndereco.setContato(contato);
        contato.getEnderecos().add(novoEndereco);
        return contatoRepository.save(contato);
    }



    //========================================  READ ===================================================================
    //Ler todos os contatos(READ)
    public List<Contato> listartodos() {
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


    //======================================  UPDATE  ==================================================================
    //Atualizar um contato (UPDATE)
    public Contato atualizarContatoCompleto(Long id, Contato contatoAtualizado) {
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

    public Contato definirTelefonePrincipal(Long contatoId, Long telefoneId) {
        Contato contato = buscarPorId(contatoId);
        contato.getTelefones().forEach(tel -> tel.setPrincipal(tel.getId().equals(telefoneId)));
        return contatoRepository.save(contato);
    }

    public Contato definirEnderecoPrincipal(Long contatoId, Long enderecoId) {
        Contato contato = buscarPorId(contatoId);
        contato.getEnderecos().forEach(end -> end.setPrincipal(end.getId().equals(enderecoId)));
        return contatoRepository.save(contato);
    }

    //======================================  DELETE ===================================================================
    //Deletar um contato (DELETE)
    public void deletarContato(Long id) {
        Contato contatoParaDeletar = buscarPorId(id);
        contatoRepository.delete(contatoParaDeletar);
    }

    public Contato removerTelefone(Long contatoId, Long telefoneId) {
        Contato contato = buscarPorId(contatoId);
        contato.getTelefones().removeIf(telefone -> telefone.getId().equals(telefoneId));
        return contatoRepository.save(contato);
    }

    public Contato removerEndereco(Long contatoId, Long enderecoId) {
        Contato contato = buscarPorId(contatoId);
        contato.getEnderecos().removeIf(endereco -> endereco.getId().equals(enderecoId));
        return contatoRepository.save(contato);
    }



    // -- Métodos privados para lógica -- //

    private void sanitizarContato(Contato contato) { //Usa como uma verificação de segurança para evitar erros..
        if (contato.getNome() != null) {                //..(NullPointerException) caso o nome não tenha sido informado.
            String nomeLimpo = contato.getNome().trim().replaceAll("\\s+", " ");

            String nomeFormatado = formatarNomeProprio(nomeLimpo);

            contato.setNome(nomeFormatado);
        }
    }


    private String formatarNomeProprio(String nome) {
        if (nome == null || nome.isEmpty()) {
            return nome;
        }

        // Divide o nome em palavras
        String[] palavras = nome.toLowerCase().split("\\s");
        StringBuilder nomeFormatado = new StringBuilder();

        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                // Capitaliza a primeira letra e adiciona o resto da palavra
                nomeFormatado.append(Character.toUpperCase(palavra.charAt(0)))
                        .append(palavra.substring(1))
                        .append(" ");
            }
        }

        // Remove o último espaço em branco
        return nomeFormatado.toString().trim();
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

