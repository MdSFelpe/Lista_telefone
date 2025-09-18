package org.example.lista_telefone_sm.service;


import org.example.lista_telefone_sm.dto.ContatoUpdateDTO;
import org.example.lista_telefone_sm.model.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.lista_telefone_sm.repository.ContatoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service //  Anotação que marca esta classe como um componente de serviço do Spring.
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private GrupoService grupoService;
    // Injeção de Dependência: O Spring vai criar e gerenciar uma instância do
    // ContatoRepository e "injetá-la" aqui para ser usado.


    //========================================  CREATE =================================================================
    //Criar um novo contato (CREATE)
    @Transactional
    public Contato criarcontato(Contato contato)    {
        //Passo 1: Sanitizar os dados
        sanitizarContato(contato);

        //Passo 2: Validar as regras de negócio
        validarRegrasDeNegocioParaCriacao(contato);

        //Passo 3 : Persistir no banco de dados
        return contatoRepository.save(contato);
    }
    @Transactional
    public Contato adicionarTelefone(Long contatoId, Telefone novoTelefone) {
        Contato contato = buscarPorId(contatoId);
        String numeroFormatado = formatarTelefone(novoTelefone.getNumero());
        novoTelefone.setNumero(numeroFormatado);
        if (contato.getTelefones().isEmpty()) {
            novoTelefone.setPrincipal(true); // Por padrão, o primeiro número cadastrado sempre vai vir como principal
        }
        novoTelefone.setContato(contato);
        contato.getTelefones().add(novoTelefone);
        return contatoRepository.save(contato);
    }

    @Transactional
    public Contato adicionarEndereco(Long contatoId, Endereco novoEndereco) {
        Contato contato = buscarPorId(contatoId);
        if (contato.getEnderecos().isEmpty()) {
            novoEndereco.setPrincipal(true); // mesma lógica de principal do "adicionartelefone"
        }
        novoEndereco.setContato(contato);
        contato.getEnderecos().add(novoEndereco);
        return contatoRepository.save(contato);
    }

    @Transactional
    public Contato adicionarGrupoAoContato(Long contatoId, Long grupoId) {

        Contato contato = buscarPorId(contatoId);
        Grupo grupo = grupoService.buscarPorId(grupoId);

        contato.getGrupos().add(grupo);

        return contatoRepository.save(contato);
    }


    //========================================  READ ===================================================================
    //Ler todos os contatos(READ)
    @Transactional(readOnly = true)
    public List<Contato> listartodos() {
        List<Contato> contatos = contatoRepository.findAll();

        for (Contato contato : contatos) {
            Hibernate.initialize(contato.getTelefones());
            Hibernate.initialize(contato.getEnderecos());
            Hibernate.initialize(contato.getGrupos());
        }

        return contatos;
    }



    //Ler um contato especifico (READ)
    @Transactional(readOnly = true)
    public Contato buscarPorId(Long id) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado com id: " + id));

        Hibernate.initialize(contato.getTelefones());
        Hibernate.initialize(contato.getEnderecos());
        Hibernate.initialize(contato.getGrupos());

        return contato;
    }

    @Transactional(readOnly = true)
    public List<Contato> buscarPorTermo(String termo) {
        // Se passa o mesmo termo para os dois parâmetros do metodo do repositório.
        return contatoRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(termo, termo);
    }

    @Transactional(readOnly = true)
    public List<Contato> listarContatosSemGrupo() {
        return contatoRepository.findByGruposIsEmpty();
    }


    //======================================  UPDATE  ==================================================================
    //Atualizar um contato (UPDATE)
    @Transactional
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

    @Transactional
    public Contato atualizarParcialmente(Long id, ContatoUpdateDTO dto) {

        Contato contatoExistente = buscarPorId(id);


        if (dto.getNome() != null) {
            contatoExistente.setNome(dto.getNome());
        }
        if (dto.getEmail() != null) {
            contatoExistente.setEmail(dto.getEmail());
        }
        if (dto.getDataNascimento() != null) {
            contatoExistente.setDataNascimento(dto.getDataNascimento());
        }


        sanitizarContato(contatoExistente);
        validarRegrasDeNegocioParaUpdate(contatoExistente, contatoExistente);

        return contatoRepository.save(contatoExistente);
    }

    @Transactional
    public Contato definirTelefonePrincipal(Long contatoId, Long telefoneId) {
        Contato contato = buscarPorId(contatoId);
        contato.getTelefones().forEach(tel -> tel.setPrincipal(tel.getId().equals(telefoneId)));
        return contatoRepository.save(contato);
    }
    @Transactional
    public Contato definirEnderecoPrincipal(Long contatoId, Long enderecoId) {
        Contato contato = buscarPorId(contatoId);
        contato.getEnderecos().forEach(end -> end.setPrincipal(end.getId().equals(enderecoId)));
        return contatoRepository.save(contato);
    }
    @Transactional
    public Contato alterarStatus(Long contatoId, StatusContato novoStatus) {
        Contato contato = buscarPorId(contatoId);
        contato.setStatus(novoStatus);
        return contatoRepository.save(contato);
    }

    //======================================  DELETE ===================================================================
    //Deletar um contato (DELETE)
    @Transactional
    public void deletarContato(Long id) {
        Contato contatoParaDeletar = buscarPorId(id);
        contatoRepository.delete(contatoParaDeletar);
    }

    @Transactional
    public Contato removerTelefone(Long contatoId, Long telefoneId) {
        Contato contato = buscarPorId(contatoId);
        contato.getTelefones().removeIf(telefone -> telefone.getId().equals(telefoneId));
        return contatoRepository.save(contato);
    }

    @Transactional
    public Contato removerEndereco(Long contatoId, Long enderecoId) {
        Contato contato = buscarPorId(contatoId);
        contato.getEnderecos().removeIf(endereco -> endereco.getId().equals(enderecoId));
        return contatoRepository.save(contato);
    }

    @Transactional
    public Contato removerGrupoDoContato(Long contatoId, Long grupoId) {

        Contato contato = buscarPorId(contatoId);
        Grupo grupo = grupoService.buscarPorId(grupoId);

        contato.getGrupos().remove(grupo);

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

    private String formatarTelefone(String numero) {
        if (numero == null) {
            return null;
        }

        String numeroApenasDigitos = numero.replaceAll("[^0-9]", "");


        if (numeroApenasDigitos.length() == 11) {
            return numeroApenasDigitos.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        }

        if (numeroApenasDigitos.length() == 10) {
            return numeroApenasDigitos.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }

        return numeroApenasDigitos;
    }

    }

