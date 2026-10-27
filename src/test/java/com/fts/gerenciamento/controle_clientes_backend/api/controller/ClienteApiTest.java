package com.fts.gerenciamento.controle_clientes_backend.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClienteApiTest {

    private static final String CLIENTE = """
            {
              "projeto": "Loja Exemplo",
              "cliente": "Exemplo Ltda",
              "tipoServico": "SITE",
              "status": "EM_DESENVOLVIMENTO",
              "dataInicio": "2026-10-01",
              "links": { "site": "https://exemplo.com" },
              "mensalidade": 300,
              "diaVencimento": 10,
              "formaPagamento": "PIX",
              "pagamentos": [ { "mes": "2026-10", "valor": 300, "status": "PENDENTE" } ],
              "custos": [ { "descricao": "domínio", "valor": 40, "recorrencia": "ANUAL" } ],
              "datas": [],
              "notas": [ { "data": "2026-10-27", "texto": "primeira reunião" } ]
            }
            """;

    @Autowired
    MockMvc mvc;

    String token;

    @BeforeEach
    void entrar() throws Exception {
        String resposta = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"admin\",\"senha\":\"admin\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        token = JsonPath.read(resposta, "$.token");
    }

    private MockHttpServletRequestBuilder autenticado(MockHttpServletRequestBuilder req) {
        return req.header(HttpHeaders.AUTHORIZATION, "Bearer " + token).contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void semTokenRetorna401() throws Exception {
        mvc.perform(get("/api/clientes")).andExpect(status().isUnauthorized());
    }

    @Test
    void senhaErradaRetorna401() throws Exception {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"admin\",\"senha\":\"errada\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void cicloCompletoDoCliente() throws Exception {
        String criado = mvc.perform(autenticado(post("/api/clientes")).content(CLIENTE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.custos[0].id").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        Number id = JsonPath.read(criado, "$.id");
        Number idPagamento = JsonPath.read(criado, "$.pagamentos[0].id");

        mvc.perform(autenticado(get("/api/clientes")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projeto").value("Loja Exemplo"))
                .andExpect(jsonPath("$[0].links.site").value("https://exemplo.com"))
                .andExpect(jsonPath("$[0].pagamentos.length()").value(1));

        String atualizado = criado.replace("\"PENDENTE\"", "\"PAGO\"")
                .replaceAll("\"notas\":\\[.*?]", "\"notas\":[]")
                .replace("\"datas\":[]", "\"datas\":[{\"descricao\":\"renovar domínio\",\"data\":\"2027-01-10\"}]");
        mvc.perform(autenticado(put("/api/clientes/" + id)).content(atualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagamentos.length()").value(1))
                .andExpect(jsonPath("$.pagamentos[0].id").value(idPagamento))
                .andExpect(jsonPath("$.pagamentos[0].status").value("PAGO"))
                .andExpect(jsonPath("$.notas.length()").value(0))
                .andExpect(jsonPath("$.datas[0].id").isNumber());

        mvc.perform(autenticado(delete("/api/clientes/" + id))).andExpect(status().isNoContent());
        mvc.perform(autenticado(get("/api/clientes/" + id))).andExpect(status().isNotFound());
    }

    @Test
    void validaCamposObrigatorios() throws Exception {
        mvc.perform(autenticado(post("/api/clientes")).content("{\"projeto\":\"\",\"tipoServico\":\"SITE\",\"status\":\"EM_NEGOCIACAO\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recusaValorForaDoEnum() throws Exception {
        mvc.perform(autenticado(post("/api/clientes")).content("{\"projeto\":\"Teste\",\"tipoServico\":\"SITE\",\"status\":\"prospecção\"}"))
                .andExpect(status().isBadRequest());
    }
}
