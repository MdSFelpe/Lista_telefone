# Agenda de Contatos - Back-end com Console Interativo

Este é um projeto pessoal focado no desenvolvimento back-end de uma API REST para gerenciamento de contatos, utilizando o ecossistema Spring Boot. O objetivo principal é aplicar e demonstrar conceitos de modelagem de dados, lógica de negócio, persistência, validação, segurança básica e versionamento de banco de dados em uma aplicação Java moderna.

Atualmente, a interação com a aplicação é realizada através de uma interface de console interativa, permitindo testar e validar toda a lógica de negócio implementada.

**Status:** 🚧 Em Desenvolvimento 🚧 (Lógica de Negócio e Console Funcional)

**Observação:** Algumas funções podem estar vazias ou em implementação no momento. O projeto está em evolução contínua.

## ✨ Funcionalidades Implementadas (Via Console)

* **CRUD Completo para Contatos:**
    * Adicionar novos contatos com validação de dados (nome, e-mail, data de nascimento).
    * Listar todos os contatos.
    * Buscar um contato específico por ID.
    * Atualizar informações de um contato (total ou parcialmente).
    * Deletar contatos.
* **Gerenciamento de Associações:**
    * Adicionar múltiplos Telefones a um contato.
    * Adicionar múltiplos Endereços a um contato.
    * Marcar um Telefone/Endereço como "Principal".
    * Remover Telefones/Endereços de um contato.
* **Gerenciamento de Grupos (Tags):**
    * Criar novos Grupos.
    * Listar Grupos existentes (incluindo grupos padrão não deletáveis).
    * Associar/Desassociar contatos a múltiplos Grupos (relação Many-to-Many).
* **Gerenciamento de Status:**
    * Alterar o status de um contato (ATIVO, INATIVO, BLOQUEADO).
* **Busca e Filtros:**
    * Buscar contatos por termo (no nome ou e-mail, case-insensitive).
    * Listar contatos filtrando por Status.
    * Listar contatos filtrando por Grupo.
    * Listar contatos que não pertencem a nenhum Grupo.
* **Regras de Negócio e Validações:**
    * Idade mínima para cadastro.
    * Validação de e-mail único.
    * Validação de formato de dados (nome, e-mail, tipo de telefone/endereço).
    * Sanitização e formatação de dados (nome, telefone).
    * Grupos padrão não podem ser excluídos.

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 21 (JDK)
* **Framework Principal:** Spring Boot 3.5.5
* **Persistência:** Spring Data JPA (com Hibernate)
* **Banco de Dados (Desenvolvimento):** H2 Database (em memória)
* **Versionamento de Schema:** Flyway
* **Validação:** Spring Validation (Bean Validation)
* **Segurança:** Spring Security (configuração básica inicial)
* **Utilitários:** Lombok
* **Build e Gerenciamento de Dependências:** Maven

## ▶️ Como Executar Localmente

1.  **Pré-requisitos:**
    * Java JDK 21 instalado.
    * Maven instalado.
    * Git instalado.

2.  **Clonar o Repositório:**
    ```bash
    git clone [https://github.com/MdSFelpe/Projeto1.git](https://github.com/MdSFelpe/Projeto1.git)
    cd Projeto1
    ```

3.  **Executar a Aplicação (via Maven):**
    ```bash
    mvn spring-boot:run
    ```
    *Alternativamente, você pode compilar com `mvn clean package` e rodar o JAR gerado em `target/`.*

4.  **Interagir:**
    Após a inicialização do Spring Boot, o menu interativo da agenda de contatos aparecerá no seu terminal. Siga as opções do menu para utilizar a aplicação.

## 🗄️ Configuração do Banco de Dados (H2)

O projeto está configurado para usar um banco de dados H2 em memória por padrão. Nenhuma configuração adicional é necessária para rodar localmente. O schema do banco é criado e gerenciado automaticamente pelo Flyway a cada inicialização.

Para visualizar o banco de dados enquanto a aplicação está rodando:
1.  Acesse `http://localhost:8080/h2-console` no seu navegador.
2.  Use as seguintes credenciais (definidas em `application.properties`):
    * **JDBC URL:** `jdbc:h2:mem:agendadb`
    * **User Name:** `sa`
    * **Password:** (deixe em branco)

## 🚀 Próximos Passos Planejados

* Implementar Paginação nos resultados da listagem.
* Aprofundar a configuração do Spring Security (ex: usuários no banco, criptografia de senha).
* Criar Testes Automatizados (JUnit 5).
* Implementar Exportação de Contatos para CSV.
* Configurar o projeto para rodar com Docker (Dockerfile e Docker Compose).
* (Opcional) Desenvolver um front-end (React/Angular/Vue) para consumir a API REST exposta pela camada de Controller.

## 👤 Autor

**Fellype Augusto de Oliveira Santos**

* GitHub: [https://github.com/MdSFelpe](https://github.com/MdSFelpe)
* LinkedIn: [https://www.linkedin.com/in/fellype-augusto-1b145326b/](https://www.linkedin.com/in/fellype-augusto-1b145326b/)
