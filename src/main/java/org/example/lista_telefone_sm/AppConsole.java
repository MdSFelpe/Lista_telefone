package org.example.lista_telefone_sm;

import org.example.lista_telefone_sm.service.ContatoService;
import org.example.lista_telefone_sm.model.Contato;
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
                case 3:
                    atualizarContato();
                    break;
                case 4:
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
        System.out.println("3. Atualizar um contato");
        System.out.println("4. Deletar um contato");
        System.out.println("0. Sair");
        System.out.print("Digite sua opção: ");
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Retorna um valor inválido se não for um número
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
            System.out.println(">>> Contato criado com sucesso! <<<");

        } catch (IllegalArgumentException e) {
            System.out.println("!!! Erro ao criar contato: " + e.getMessage());
        } catch (Exception e) {
            // Captura outros erros, como formato de data inválido.
            System.out.println("!!! Erro inesperado ou dados em formato inválido. Tente novamente.");
        }

    }

    private void listarContatos() {
        System.out.println("\n--- Lista de Contatos ---");
        List<Contato> contatos = contatoService.listarContatos();
        if (contatos.isEmpty()) {
            System.out.println("Nenhum contato cadastrado.");
        } else {
            contatos.forEach(contato -> {
                System.out.println("--------------------");
                System.out.println("ID: " + contato.getId());
                System.out.println("Nome: " + contato.getNome());
                System.out.println("Email: " + contato.getEmail());
                System.out.println("Data de Nascimento: " + (contato.getDataNascimento() != null ? contato.getDataNascimento().format(fmt) : "N/A"));
            });
        }
    }


    private void atualizarContato() {
        System.out.println("\n--- Atualizar Contato ---");
        // Lembrete: Implementaremos o menu completo de atualização aqui depois.
        System.out.println("Funcionalidade de atualização a ser implementada.");
    }


    private void deletarContato() {
        try {
            System.out.println("\n--- Deletar Contato ---");
            System.out.print("Digite o ID do contato a ser deletado: ");
            Long id = Long.parseLong(sc.nextLine());

            contatoService.deletarContato(id);
            System.out.println(">>> Contato deletado com sucesso! <<<");

        } catch (RuntimeException e) {
            // Captura o erro do buscarPorId se o contato não for encontrado ou se a entrada não for um número
            System.out.println("!!! Erro ao deletar: " + e.getMessage());
        }
    }

}