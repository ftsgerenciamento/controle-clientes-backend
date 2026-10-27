package com.fts.gerenciamento.controle_clientes_backend.model;

import com.fts.gerenciamento.controle_clientes_backend.api.dto.ClienteDto;
import com.fts.gerenciamento.controle_clientes_backend.api.dto.CustoDto;
import com.fts.gerenciamento.controle_clientes_backend.api.dto.DataImportanteDto;
import com.fts.gerenciamento.controle_clientes_backend.api.dto.NotaDto;
import com.fts.gerenciamento.controle_clientes_backend.api.dto.PagamentoDto;
import com.fts.gerenciamento.controle_clientes_backend.model.enums.FormaPagamento;
import com.fts.gerenciamento.controle_clientes_backend.model.enums.StatusCliente;
import com.fts.gerenciamento.controle_clientes_backend.model.enums.TipoServico;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String projeto;
    private String cliente;
    private String contatoNome;
    private String telefone;
    private String email;
    private String descricao;
    @Enumerated(EnumType.STRING)
    private TipoServico tipoServico;
    @Enumerated(EnumType.STRING)
    private StatusCliente status;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    @Embedded
    private Links links;
    private BigDecimal valorProjeto;
    private BigDecimal mensalidade;
    private Integer diaVencimento;
    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;
    private String proximaAcao;
    private String escopo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cliente_id", nullable = false)
    private List<Pagamento> pagamentos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cliente_id", nullable = false)
    private List<Custo> custos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cliente_id", nullable = false)
    private List<DataImportante> datas = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cliente_id", nullable = false)
    private List<Nota> notas = new ArrayList<>();

    public Cliente() {
    }

    public void preencher(ClienteDto d) {
        projeto = d.projeto();
        cliente = d.cliente();
        contatoNome = d.contatoNome();
        telefone = d.telefone();
        email = d.email();
        descricao = d.descricao();
        tipoServico = d.tipoServico();
        status = d.status();
        dataInicio = d.dataInicio();
        dataFim = d.dataFim();
        links = d.links();
        valorProjeto = d.valorProjeto();
        mensalidade = d.mensalidade();
        diaVencimento = d.diaVencimento();
        formaPagamento = d.formaPagamento();
        proximaAcao = d.proximaAcao();
        escopo = d.escopo();
        sincronizar(pagamentos, d.pagamentos(), PagamentoDto::id, Pagamento::new);
        sincronizar(custos, d.custos(), CustoDto::id, Custo::new);
        sincronizar(datas, d.datas(), DataImportanteDto::id, DataImportante::new);
        sincronizar(notas, d.notas(), NotaDto::id, Nota::new);
    }

    public ClienteDto paraDto() {
        return new ClienteDto(id, projeto, cliente, contatoNome, telefone, email, descricao, tipoServico, status,
                dataInicio, dataFim, links, valorProjeto, mensalidade, diaVencimento, formaPagamento,
                paraDtos(pagamentos), paraDtos(custos), paraDtos(datas), paraDtos(notas),
                proximaAcao, escopo);
    }

    public Long getId() {
        return id;
    }

    // Item com id já existente é atualizado, sem id (ou com id desconhecido) é criado,
    // e o que não veio na lista é apagado (orphanRemoval). A ordem segue a do DTO.
    private static <E extends ItemCliente<D>, D> void sincronizar(
            List<E> atuais, List<D> novos, Function<D, Long> idDe, Supplier<E> criar) {
        Map<Long, E> existentes = atuais.stream().collect(Collectors.toMap(ItemCliente::getId, e -> e));
        atuais.clear();
        for (D dto : novos) {
            E item = idDe.apply(dto) == null ? null : existentes.get(idDe.apply(dto));
            if (item == null) item = criar.get();
            item.preencher(dto);
            atuais.add(item);
        }
    }

    private static <D> List<D> paraDtos(List<? extends ItemCliente<D>> itens) {
        return itens.stream().map(ItemCliente::paraDto).toList();
    }
}
