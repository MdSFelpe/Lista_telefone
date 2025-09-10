/*

    -- SQL SERVER --
-- Criar a tabela contato --
    CREATE TABLE contato (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        name VARCHAR(100) NOT NULL
        email VARCHAR(100) UNIQUE,
        data_nascimento DATE
);

-- Números de telefone --
    CREATE TABLE telefone (
          id BIGINT IDENTITY(1,1) PRIMARY KEY,
          numero_telefone VARCHAR(30) NOT NULL,
          tipo VARCHAR(50) NOT NULL,

        --FOREIGN KEYS--
          contato_id BIGINT NOT NULL,
          CONSTRAINT FK_telefone_contato
          FOREIGN KEY (contato_id)
          REFERENCES contato(id)
          ON DELETE CASCADE
          ON UPDATE CASCADE
);

    --Endereço--
    CREATE TABLE endereco (
          id BIGINT IDENTITY(1,1) PRIMARY KEY, -- Corrigido de AUTO_INCRIMENT
          cep VARCHAR(15) NOT NULL,
          rua VARCHAR(80) NOT NULL,
          numero_residencia VARCHAR(50) NOT NULL,

        --FOREIGN KEYS --
          contato_id BIGINT NOT NULL,
          CONSTRAINT FK_lista_telefonica
          FOREIGN KEY (contato_id)
          REFERENCES contato(id)
          ON DELETE CASCADE
          ON UPDATE CASCADE
);
 */

                        -- H2 TESTE --
CREATE TABLE contato (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      nome VARCHAR(100) NOT NULL,
      email VARCHAR(100) UNIQUE,
      data_nascimento DATE
);

CREATE TABLE telefone (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      numero VARCHAR(30) NOT NULL,
      tipo VARCHAR(50) NOT NULL,
      contato_id BIGINT NOT NULL,
      FOREIGN KEY (contato_id) REFERENCES contato(id) ON DELETE CASCADE
);

CREATE TABLE endereco (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       cep VARCHAR(15) NOT NULL,
       rua VARCHAR(80) NOT NULL,
       numero VARCHAR(50) NOT NULL,
       tipo VARCHAR(50) NOT NULL,
       contato_id BIGINT NOT NULL,
       FOREIGN KEY (contato_id) REFERENCES contato(id) ON DELETE CASCADE
);