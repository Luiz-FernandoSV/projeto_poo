CREATE DATABASE IF NOT EXISTS bd_controle_comanda;
USE bd_controle_comanda;

CREATE TABLE produto (
    id_produto BIGINT AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    preco DECIMAL(10,2) NOT NULL,
    categoria INT NOT NULL,
    disponivel TINYINT(1) NOT NULL DEFAULT 1,

    CONSTRAINT pk_produto PRIMARY KEY (id_produto)
);

CREATE TABLE comanda (
    id_comanda BIGINT AUTO_INCREMENT,
    numero BIGINT NOT NULL,
    data_abertura DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_fechamento DATETIME,
    valor_total DOUBLE NOT NULL DEFAULT 0.0,
    status VARCHAR(30) NOT NULL,

    CONSTRAINT pk_comanda PRIMARY KEY (id_comanda),
    CONSTRAINT uk_comanda_numero UNIQUE (numero)
);

CREATE TABLE itemcomanda (
    id_item_comanda BIGINT AUTO_INCREMENT,
    id_comanda BIGINT NOT NULL,
    id_produto BIGINT NOT NULL,
    quantidade INT NOT NULL DEFAULT 1,
    preco_unitario DECIMAL(10,2) NOT NULL,

    CONSTRAINT pk_itemcomanda
        PRIMARY KEY (id_item_comanda),

    CONSTRAINT fk_itemcomanda_comanda
        FOREIGN KEY (id_comanda)
        REFERENCES comanda(id_comanda),

    CONSTRAINT fk_itemcomanda_produto
        FOREIGN KEY (id_produto)
        REFERENCES produto(id_produto),

    CONSTRAINT chk_itemcomanda_quantidade
        CHECK (quantidade > 0),

    CONSTRAINT uk_itemcomanda_produto
        UNIQUE (id_comanda, id_produto)
);