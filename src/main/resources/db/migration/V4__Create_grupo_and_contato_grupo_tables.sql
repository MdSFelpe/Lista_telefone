CREATE TABLE grupo (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       nome VARCHAR(50) NOT NULL UNIQUE,
       deletavel BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE contato_grupo (
        contato_id BIGINT NOT NULL,
        grupo_id BIGINT NOT NULL,

        PRIMARY KEY (contato_id, grupo_id),
        FOREIGN KEY (contato_id) REFERENCES contato(id) ON DELETE CASCADE,
        FOREIGN KEY (grupo_id) REFERENCES grupo(id) ON DELETE CASCADE
);