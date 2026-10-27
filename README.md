# controle-clientes-backend

API do app **Controle de Clientes FTS** (frontend em `../controle-clientes`). Spring Boot 4 · Java 21 · PostgreSQL · JWT.

## Rodar localmente

1. PostgreSQL rodando em `localhost:5432` com o banco criado (uma vez só):
   ```sh
   psql postgres -c "create role controle_user login password 'controle_user'"
   createdb -O controle_user controle_clientes
   ```
2. Subir a API (porta 8080). As tabelas são criadas pelo Flyway na primeira execução:
   ```sh
   ./mvnw spring-boot:run
   ```
3. No frontend: `npm run dev` e abrir http://localhost:5173 — login `admin` / `admin`.

Testes: `./mvnw test` (usam H2 em memória, não precisam do Postgres).

## Endpoints

| Método | Caminho | Descrição |
|---|---|---|
| POST | `/api/auth/login` | `{usuario, senha}` → `{token}` (único endpoint sem token) |
| GET | `/api/clientes` | lista todos os clientes |
| GET | `/api/clientes/{id}` | um cliente |
| POST | `/api/clientes` | cria (o `id` é gerado pelo banco e devolvido na resposta) |
| PUT | `/api/clientes/{id}` | salva o cliente inteiro; nas listas internas, item sem `id` é criado, com `id` é atualizado e o que não vier é apagado |
| DELETE | `/api/clientes/{id}` | exclui |

Demais chamadas exigem o header `Authorization: Bearer <token>`. Erros seguem o formato Problem Details (`detail` traz a mensagem).

## Configuração (variáveis de ambiente)

| Variável | Padrão (dev) | Uso |
|---|---|---|
| `DB_URL`, `DB_USUARIO`, `DB_SENHA` | banco local `controle_clientes` | conexão com o Postgres |
| `JWT_SEGREDO` | valor de desenvolvimento | chave dos tokens (mín. 32 caracteres) — **trocar em produção** |
| `ADMIN_USUARIO`, `ADMIN_SENHA` | `admin` / `admin` | usuário criado se o banco não tiver nenhum |
| `CORS_ORIGENS` | `http://localhost:5173` | endereços do frontend autorizados |
