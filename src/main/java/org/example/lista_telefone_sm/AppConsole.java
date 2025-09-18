package org.example.lista_telefone_sm;

import org.example.lista_telefone_sm.dto.ContatoUpdateDTO;
import org.example.lista_telefone_sm.model.*;
import org.example.lista_telefone_sm.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
public class AppConsole implements CommandLineRunner {
    @Autowired
    private ContatoService contatoService;


    private final Scanner sc = new Scanner(System.in);
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void run(String... args) throws Exception {
        // Loop infinito para manter o menu rodando
        while (true) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    adicionarContato();
                    break;
                case 2:
                    listarContatos();
                    break;

                case 4:
                    handleListarSemGrupo();
                    break;
                case 5:
                    atualizarContato();
                    break;
                case 6:
                    deletarContato();
                    break;
                case 0:
                    System.out.println("Encerrando a aplicação...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

    }

    private void exibirMenu() {
        System.out.println("\n--- Agenda de Contatos ---");
        System.out.println("1. Adicionar novo contato");
        System.out.println("2. Listar todos os contatos");
        System.out.println("3. Listar por GRUPO/TAG");
        System.out.println("4. Listar contatos SEM GRUPO");
        System.out.println("5. Atualizar um contato");
        System.out.println("6. Deletar um contato");
        System.out.println("0. Sair");
        System.out.print("Digite sua opção: ");
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void adicionarContato() {
        try {
            System.out.println("\n--- Adicionar Novo Contato ---");
            Contato novoContato = new Contato();

            System.out.print("Nome: ");
            novoContato.setNome(sc.nextLine());

            System.out.print("E-mail: ");
            novoContato.setEmail(sc.nextLine());

            System.out.print("Data de Nascimento (dd/MM/yyyy): ");
            novoContato.setDataNascimento(LocalDate.parse(sc.nextLine(), fmt));

            // Lembrete: A lógica para adicionar telefones e endereços entraria aqui.
            // Por enquanto, eles serão criados como listas vazias.

            contatoService.criarcontato(novoContato);
            System.out.println(" Contato criado com sucesso! ");

        } catch (IllegalArgumentException e) {
            System.out.println("!!! Erro ao criar contato: " + e.getMessage());
        } catch (Exception e) {
            // Captura outros erros, como formato de data inválido.
            System.out.println("!!! Erro inesperado ou dados em formato inválido. Tente novamente.");
        }

    }


    private void listarContatos() {
        System.out.println("\n=== Lista de Contatos ===");
        List<Contato> contatos = contatoService.listartodos();
        if (contatos.isEmpty()) {
            System.out.println("Nenhum contato cadastrado.");
        } else {
            contatos.forEach(contato -> {
                System.out.println("====================");
                System.out.println("ID: " + contato.getId());
                System.out.println("Nome: " + contato.getNome());
                System.out.println("Email: " + contato.getEmail());
                System.out.println("Data de Nascimento: " + (contato.getDataNascimento() != null ? contato.getDataNascimento().format(fmt) : "N/A"));
                System.out.println("Status: " + contato.getStatus());
            });
        }
    }


    // =========================================== ATUALIZAÇÃO DE DADOS ============================================= //
    private void atualizarContato() {
        System.out.println("\n=== Atualizar Contato ===");
        System.out.print("Digite o ID do contato que deseja atualizar (ou 0 para voltar): ");

        Long id;
        try {
            id = Long.parseLong(sc.nextLine());
            if (id == 0) return;
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Por favor, digite um número.");
            return;
        }

        try {
            // Primeiro, se faz a um busca e é exibido o contato para o usuário ter certeza de quem está editando
            Contato contato = contatoService.buscarPorId(id);
            System.out.println("Contato encontrado: " + contato.getNome());


            while (true) {
                exibirSubMenuAtualizacao(contato);
                int opcao = lerOpcao();

                if (opcao == 0) {
                    System.out.println("\n=== DADOS FINAIS DO CONTATO ===");
                    exibirDetalhesContato(contato);
                    break; // Sai do loop de atualização e volta ao menu principal
                }

                switch (opcao) {

                    case 1:
                        contato = handleAlterarNome(contato);
                        break;


                    case 2:
                        contato = handleAlterarEmail(contato);
                        break;


                    case 3:

                        System.out.println("Funcionalidade não implementada.");
                        break;

                    case 4:
                        contato = handleAlterarStatus(contato);
                        break;


                    case 5:
                        contato = handleAdicionarTelefone(contato);
                        break;

                    case 6:
                        contato = handleRemoverTelefone(contato);
                        break;

                    case 7:
                        contato = handleDefinirTelefonePrincipal(contato);
                        break;

                    case 8:
                        contato = handleAdicionarEndereco(contato);
                        break;

                    case 9:
                        contato = handleRemoverEndereco(contato);
                        break;

                    case 10:
                        contato = handleDefinirEnderecoPrincipal(contato);
                        break;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }
        } catch (RuntimeException e) {
            System.out.println("!!! Erro: " + e.getMessage());
        }
    }


    private void exibirSubMenuAtualizacao(Contato contato) {
        System.out.println("\n--- O que você deseja atualizar para '" + contato.getNome() + "'? ---");
        System.out.print("=============Contato============");
        System.out.println("1. Alterar Nome");
        System.out.println("2. Alterar E-mail");
        System.out.println("3. Alterar Data de Nascimento");
        System.out.println("4. Alterar Status do Contato");
        System.out.println("\n==========Telefone============");
        System.out.println("5. Adicionar um novo telefone");
        System.out.println("6. Remover um telefone existente");
        System.out.println("7. Definir telefone principal");
        System.out.println("\n==========Endereço=============");
        System.out.println("8. Adicionar um novo endereço");
        System.out.println("9. Remover um endereço existente");
        System.out.println("10. Definir endereço principal");

        System.out.println("0. Concluir atualização (Voltar ao Menu Principal)");
        System.out.print("Digite sua opção: ");
    }

    private void exibirDetalhesContato(Contato contato) {
        System.out.println("--------------------");
        System.out.println("ID: " + contato.getId());
        System.out.println("Nome: " + contato.getNome());
        System.out.println("Email: " + contato.getEmail());
        System.out.println("Data de Nascimento: " + (contato.getDataNascimento() != null ? contato.getDataNascimento().format(fmt) : "N/A"));
        System.out.println("Status: " + contato.getStatus());

        System.out.println("Telefones:");
        if (contato.getTelefones() == null || contato.getTelefones().isEmpty()) {
            System.out.println("  (Nenhum telefone cadastrado)");
        } else {
            contato.getTelefones().forEach(tel ->
                    System.out.println("  - ID: " + tel.getId() + " | " + tel.getTipo() + ": " + tel.getNumero() + (tel.isPrincipal() ? " (Principal)" : ""))
            );
        }

        System.out.println("Endereços:");
        if (contato.getEnderecos() == null || contato.getEnderecos().isEmpty()) {
            System.out.println("  (Nenhum endereço cadastrado)");
        } else {
            contato.getEnderecos().forEach(endereco -> {
                StringBuilder linhaEndereco = new StringBuilder();

                linhaEndereco.append("  - ID: ").append(endereco.getId());
                linhaEndereco.append(" | ").append(endereco.getTipo());
                if (endereco.isPrincipal()) {
                    linhaEndereco.append(" (Principal)");
                }
                linhaEndereco.append("\n    CEP: ").append(endereco.getCep());
                linhaEndereco.append(", Rua: ").append(endereco.getRua());
                linhaEndereco.append(", N°: ").append(endereco.getNumero());

                System.out.println(linhaEndereco.toString());

            });
        }
    }
// ===========================================XXXXXXXXXX============================================================= //

    private void deletarContato() {
        try {
            System.out.println("\n--- Deletar Contato ---");
            System.out.print("Digite o ID do contato a ser deletado: ");
            Long id = Long.parseLong(sc.nextLine());


            Contato contatoParaDeletar = contatoService.buscarPorId(id);

            contatoService.deletarContato(id);

            System.out.println("Contato '" + contatoParaDeletar.getNome() + "' (ID: " + contatoParaDeletar.getId() + ") deletado com sucesso! ");

        } catch (RuntimeException e) {
            System.out.println("Erro ao deletar: " + e.getMessage());
        }
    }

    private Contato handleAlterarNome(Contato contato) {
        System.out.print("Digite o novo nome: ");
        String novoNome = sc.nextLine();

        ContatoUpdateDTO dto = new ContatoUpdateDTO();
        dto.setNome(novoNome);

        try {
            Contato contatoAtualizado = contatoService.atualizarParcialmente(contato.getId(), dto);
            System.out.println("Nome atualizado com sucesso! ");
            return contatoAtualizado;
        } catch (Exception e) {
            System.out.println(" Erro ao atualizar nome: " + e.getMessage());
            return contato;
        }
    }


    private Contato handleAlterarEmail(Contato contato) {
        System.out.print("Digite o novo e-mail: ");
        String novoEmail = sc.nextLine();

        ContatoUpdateDTO dto = new ContatoUpdateDTO();
        dto.setEmail(novoEmail);

        try {
            Contato contatoAtualizado = contatoService.atualizarParcialmente(contato.getId(), dto);
            System.out.println(" E-mail atualizado com sucesso! ");
            return contatoAtualizado;
        } catch (Exception e) {
            System.out.println(" Erro ao atualizar e-mail: " + e.getMessage());
            return contato;
        }
    }

    private Contato handleAdicionarTelefone(Contato contato) {
        System.out.println("--- Adicionar Novo Telefone ---");
        System.out.print("Digite o número do novo telefone: ");
        String numero = sc.nextLine();
        System.out.print("Digite o tipo (CELULAR, CASA, TRABALHO): ");
        String tipoInput = sc.nextLine();

        try {
            TipoTelefone tipo = TipoTelefone.valueOf(tipoInput.toUpperCase());
            Telefone novoTelefone = new Telefone();
            novoTelefone.setNumero(numero);
            novoTelefone.setTipo(tipo);

            Contato contatoAtualizado = contatoService.adicionarTelefone(contato.getId(), novoTelefone);
            System.out.println(" Telefone adicionado com sucesso! ");
            return contatoAtualizado;
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de telefone inválido. Use CELULAR, CASA ou TRABALHO.");
            return contato;
        }
    }

    private Contato handleRemoverTelefone(Contato contato) {
        if (contato.getTelefones().isEmpty()) {
            System.out.println("Este contato não possui telefones para remover.");
            return contato;
        }
        System.out.print("Digite o ID do telefone a ser removido: ");
        try {
            Long telefoneId = Long.parseLong(sc.nextLine());
            Contato contatoAtualizado = contatoService.removerTelefone(contato.getId(), telefoneId);
            System.out.println("Telefone removido com sucesso! ");
            return contatoAtualizado;
        } catch (Exception e) {
            System.out.println(" ID inválido ou erro ao remover.");
            return contato;
        }
    }

    private Contato handleDefinirTelefonePrincipal(Contato contato) {
        if (contato.getTelefones().isEmpty()) {
            System.out.println("Este contato não possui telefones.");
            return contato;
        }
        System.out.print("Digite o ID do telefone que será o principal: ");
        try {
            Long telefoneId = Long.parseLong(sc.nextLine());
            Contato contatoAtualizado = contatoService.definirTelefonePrincipal(contato.getId(), telefoneId);
            System.out.println("Telefone principal definido com sucesso! ");
            return contatoAtualizado;
        } catch (Exception e) {
            System.out.println("ID inválido ou erro ao definir principal.");
            return contato;
        }
    }

    private Contato handleAlterarStatus(Contato contato) {
        System.out.print("Digite o novo status (ATIVO, INATIVO, BLOQUEADO): ");
        String statusInput = sc.nextLine();

        try {
            // Converte a String do usuário para o nosso Enum
            StatusContato novoStatus = StatusContato.valueOf(statusInput.toUpperCase());

            Contato contatoAtualizado = contatoService.alterarStatus(contato.getId(), novoStatus);
            System.out.println(" Status atualizado com sucesso! ");
            return contatoAtualizado;

        } catch (IllegalArgumentException e) {
            System.out.println(" Status inválido. Use ATIVO, INATIVO ou BLOQUEADO.");
            return contato; // Retorna o contato original sem alteração
        }
    }

    private void handleListarSemGrupo() {
        List<Contato> contatos = contatoService.listarContatosSemGrupo();
        System.out.println("\n--- Contatos Sem Grupo Associado ---");
        exibirListaDeContatos(contatos);
    }

    private void exibirListaDeContatos(List<Contato> contatos) {
        if (contatos.isEmpty()) {
            System.out.println("Nenhum contato encontrado para este critério.");
        } else {
            contatos.forEach(this::exibirDetalhesContato);
        }
    }

    private Contato handleAdicionarEndereco(Contato contato) {
        System.out.println("--- Adicionar Novo Endereço ---");

        System.out.print("Digite o CEP: ");
        String cep = sc.nextLine();
        System.out.print("Digite a Rua: ");
        String rua = sc.nextLine();
        System.out.print("Digite o Número: ");
        String numero = sc.nextLine();
        System.out.print("Digite o Tipo (RESIDENCIAL, COMERCIAL): ");
        String tipoInput = sc.nextLine();

        try {
            TipoEndereco tipo = TipoEndereco.valueOf(tipoInput.toUpperCase());
            Endereco novoEndereco = new Endereco();
            novoEndereco.setCep(cep);
            novoEndereco.setRua(rua);
            novoEndereco.setNumero(numero);
            novoEndereco.setTipo(tipo);

            Contato contatoAtualizado = contatoService.adicionarEndereco(contato.getId(), novoEndereco);
            System.out.println(" Endereço adicionado com sucesso! ");
            return contatoAtualizado;
        } catch (IllegalArgumentException e) {
            System.out.println(" Tipo de endereço inválido. Use RESIDENCIAL ou COMERCIAL.");
            return contato;
        }
    }

    private Contato handleRemoverEndereco(Contato contato) {
        if (contato.getEnderecos().isEmpty()) {
            System.out.println("Este contato não possui endereços para remover.");
            return contato;
        }
        System.out.println("Endereços atuais:");
        contato.getEnderecos().forEach(end ->
                System.out.println("  - ID: " + end.getId() + " | " + end.getTipo() + ": " + end.getRua() + ", " + end.getNumero())
        );
        System.out.print("Digite o ID do endereço a ser removido: ");
        try {
            Long enderecoId = Long.parseLong(sc.nextLine());
            Contato contatoAtualizado = contatoService.removerEndereco(contato.getId(), enderecoId);
            System.out.println(" Endereço removido com sucesso! ");
            return contatoAtualizado;
        } catch (Exception e) {
            System.out.println("!!! ID inválido ou erro ao remover.");
            return contato;
        }
    }

    private Contato handleDefinirEnderecoPrincipal(Contato contato) {
        if (contato.getEnderecos().isEmpty()) {
            System.out.println("Este contato não possui endereços.");
            return contato;
        }
        System.out.println("Endereços atuais:");
        contato.getEnderecos().forEach(end ->
                System.out.println("  - ID: " + end.getId() + " | " + end.getTipo() + ": " + end.getRua() + (end.isPrincipal() ? " (Principal)" : ""))
        );
        System.out.print("Digite o ID do endereço que será o principal: ");
        try {
            Long enderecoId = Long.parseLong(sc.nextLine());
            Contato contatoAtualizado = contatoService.definirEnderecoPrincipal(contato.getId(), enderecoId);
            System.out.println(" Endereço principal definido com sucesso! ");
            return contatoAtualizado;
        } catch (Exception e) {
            System.out.println("!!! ID inválido ou erro ao definir principal.");
            return contato;
        }
    }


}