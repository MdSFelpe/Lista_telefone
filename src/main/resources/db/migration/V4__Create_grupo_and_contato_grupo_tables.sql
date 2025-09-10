CREATE TABLE grupo (
     id BIGINT IDENTITY(1,1) PRIMARY KEY,
     nome VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE contato_grupo (
      contato_id BIGINT NOT NULL,
      grupo_id BIGINT NOT NULL,
      PRIMARY KEY (contato_id, grupo_id),
      FOREIGN KEY (contato_id) REFERENCES contato(id),
      FOREIGN KEY (grupo_id) REFERENCES grupo(id)
);