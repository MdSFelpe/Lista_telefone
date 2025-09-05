-- Criar a tabela contato --
CREATE TABLE IF NOT EXISTS contato (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
    ) ENGINE=InnoDB;


    -- Números de telefone --
CREATE TABLE IF NOT EXISTS telefone (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_telefone VARCHAR(30) NOT NULL,
    tipo VARCHAR(50) NOT NULL,


        --FOREIGN KEYS--
    contato_id BIGINT NOT NULL,
    CONSTRAINT FK_telefone_contato
    FOREIGN KEY (contato_id)
    REFERENCES contato(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB;


    --Endereço --
CREATE TABLE IF NOT EXISTS endereco (
    id BIGINT AUTO_INCRIMENT PRIMARY KEY,
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

)ENGINE=InnoDB;