# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

API do app **Controle de Clientes FTS**. O frontend (React + MUI) fica em `../controle-clientes` e tem seu próprio `CLAUDE.md` com as regras de negócio. Spring Boot 4.1 · Java 21 · PostgreSQL · Flyway · JWT.

## Convenções
- Código, nomes, mensagens de erro e comentários em **português** (igual ao frontend).
- Valores de domínio são enums em `model/enums` (`StatusCliente`, `TipoServico`, `StatusPagamento`, `Recorrencia`, `FormaPagamento`), gravados no banco e trafegados no JSON **pelo nome** (`@Enumerated(STRING)`, ex.: `EM_MANUTENCAO`). O texto exibido fica só no frontend (mapas em `../controle-clientes/src/utils.js`): ao criar ou renomear um valor, mudar nos dois lados. Renomear um valor já gravado exige migration que atualize as linhas.
- `StatusPagamento` só tem `PENDENTE` e `PAGO`: "atrasado" é sempre calculado no frontend.

## Comandos
```sh
./mvnw spring-boot:run                       # sobe a API em :8080 (precisa do Postgres local)
./mvnw test                                  # todos os testes (H2 em memória, sem Postgres)
./mvnw test -Dtest=ClienteApiTest            # uma classe (testes de API com MockMvc + login real)
./mvnw test -Dtest=ClienteApiTest#cicloCompletoDoCliente   # um método
./mvnw compile
```
Banco local (uma vez): `psql postgres -c "create role controle_user login password 'controle_user'"` e `createdb -O controle_user controle_clientes`. Frontend: `npm run dev` em `../controle-clientes` (proxy de `/api` → `localhost:8080`), login `admin`/`admin`.

## Arquitetura
Organizado **por camada**, todos os pacotes sob `com.fts.gerenciamento.controle_clientes_backend`:
`api/controller` (REST) → `service` (regras e transações) → `repository` (Spring Data) → `model` (entidades JPA). Os formatos da API ficam em `api/dto` (records com as validações); `config` tem segurança e o usuário inicial.

- **Agregado Cliente:** `Cliente` tem `Links` (`@Embedded`) e quatro listas `@OneToMany` (cascade + `orphanRemoval`) de `Pagamento`, `Custo`, `DataImportante` e `Nota`. Cada uma tem sua tabela, ligada pelo `cliente_id`. Não existem endpoints por item: a API trafega o **cliente inteiro** (`ClienteDto` com `PagamentoDto` etc.).
- **Todos os ids são `Long` gerados pelo banco** (identity). Ao criar, o `id` enviado é ignorado. No `PUT`, `Cliente.sincronizar` concilia cada lista por id: item com id existente é atualizado, item sem id é criado e item que não veio é apagado. O service usa `saveAndFlush` para os itens novos já voltarem com id na resposta.
- O mapeamento entidade↔DTO fica nas entidades (`preencher` / `paraDto`; os itens implementam `ItemCliente`), sem setters. `ClienteDto` troca listas e `links` nulos por vazios, porque o Hibernate devolve `links` nulo quando todas as colunas estão vazias.
- Regras de negócio (pendente → atrasado, MRR, custo mensal, gerar cobranças do mês) são calculadas **no frontend**; o backend só guarda e valida.
- **Login:** `SecurityConfig` monta uma API stateless. `POST /api/auth/login` (`AuthController`) é o único endpoint público; o resto exige `Authorization: Bearer <JWT>`. O token é HS256 assinado com `app.jwt.segredo` (mín. 32 bytes) e validado pelo resource server OAuth2 do Spring. Senhas em BCrypt na tabela `usuario`; `AdminInicial` cria o usuário de `app.admin.*` se a tabela estiver vazia. CORS liberado só para `app.cors.origens`.
- Erros saem no formato Problem Details (`spring.mvc.problemdetails.enabled`); o frontend mostra o campo `detail`. Use `ResponseStatusException` com mensagem em português.

## Banco
- O schema é do **Flyway** (`src/main/resources/db/migration`); o Hibernate roda com `ddl-auto=validate`. Mudança de tabela = nova migration `V<n>__descricao.sql` (nunca editar uma já aplicada) + ajuste na entidade.
- Nomes de colunas seguem a estratégia padrão do Spring (camelCase → snake_case).
- Os testes usam o perfil `test` (`src/test/resources/application-test.properties`, H2 em modo PostgreSQL), então as migrations precisam rodar tanto no Postgres quanto no H2.

## Configuração
Todas as propriedades em `application.properties` têm padrão de desenvolvimento e podem ser sobrescritas por variáveis de ambiente: `DB_URL`, `DB_USUARIO`, `DB_SENHA`, `JWT_SEGREDO`, `ADMIN_USUARIO`, `ADMIN_SENHA`, `CORS_ORIGENS`. Os padrões (`admin`/`admin`, segredo de dev) não servem para produção.
