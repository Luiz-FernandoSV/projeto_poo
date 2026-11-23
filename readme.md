# Sistema de Controle de Produtos e Comandas

**Controle de Comandas e Produtos**

## Integrantes

- Luiz Fernando dos Santos Vieira


## Tema escolhido

Sistema de gerenciamento de produtos e comandas para um estabelecimento do tipo bar.

## Descrição do problema resolvido

O projeto tem como objetivo facilitar o gerenciamento de produtos e comandas de um bar, centralizando o cadastro e a consulta de produtos e o controle dos pedidos realizados pelos clientes.

Sem um sistema centralizado, o estabelecimento pode enfrentar dificuldades para manter os produtos atualizados, controlar sua disponibilidade, registrar os itens consumidos em cada comanda e acompanhar os valores totais dos pedidos.

A aplicação permite cadastrar, consultar, atualizar e excluir produtos, além de abrir e gerenciar comandas, adicionar ou remover produtos, acompanhar os itens registrados e atualizar o valor total da comanda. Também permite controlar a disponibilidade dos produtos e o status das comandas.

## Entidades implementadas

- **Produto:** representa os produtos oferecidos pelo estabelecimento, contendo informações como identificador, nome, descrição, preço, categoria e disponibilidade.
- **Comanda:** representa uma comanda do estabelecimento, armazenando informações como identificador, número, datas de abertura e fechamento, valor total e status.
- **ItemComanda:** representa um produto associado a uma comanda, registrando a quantidade e o preço unitário do produto no pedido.

## Tecnologias utilizadas

- Java
- JavaFX
- JDBC
- MariaDB
- Gradle

## Instruções para execução

### Pré-requisitos

- JDK compatível com a versão configurada no projeto.
- Gradle ou Gradle Wrapper incluído no projeto.
- MariaDB instalado e em execução.
- Banco de dados `bd_controle_comanda` criado e configurado.

### Configuração do banco de dados

1. Inicie o servidor MariaDB.
2. Crie o banco de dados `bd_controle_comanda`.
3. Execute o script SQL de criação das tabelas disponibilizado no projeto.
4. Confira as configurações de conexão com o banco de dados nas classes DAO, ajustando endereço, porta, usuário e senha conforme o ambiente.

### Executando a aplicação

Na pasta raiz do projeto, execute:

```bash
./gradlew run
```

No Windows, utilize:

```powershell
gradlew.bat run
```

Caso o Gradle Wrapper não esteja disponível, utilize o Gradle instalado na máquina:

```bash
gradle run
```

## Vídeo de apresentação

[Link para o vídeo no YouTube — público ou não listado]
