package org.com.revenda.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.application.service.*;
import org.com.revenda.presentation.dto.request.CadastrarVeiculoRequest;
import org.com.revenda.presentation.dto.request.VenderVeiculoRequest;
import org.com.revenda.presentation.dto.response.VeiculoResponse;
import org.com.revenda.presentation.dto.response.VendaResponse;
import org.com.revenda.presentation.mapper.VeiculoDtoMapper;
import org.com.revenda.presentation.mapper.VendaDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Veículos", description = "Operações relacionadas aos veículos")
public class VeiculoController {

    private final CadastrarVeiculoApplicationService cadastrarVeiculoApplicationService;
    private final EditarVeiculoApplicationService editarVeiculoApplicationService;
    private final VenderVeiculoApplicationService venderVeiculoApplicationService;
    private final ListarVeiculosPorStatusApplicationService listarVeiculosPorStatusApplicationService;
    private final ListarTodosVeiculosApplicationService listarTodosVeiculosApplicationService;
    private final BuscarVeiculoPorIdApplicationService buscarVeiculoPorIdApplicationService;
    private final ListarVeiculosVendidosApplicationService listarVeiculosVendidosApplicationService;
    private final VeiculoDtoMapper veiculoMapper;
    private final VendaDtoMapper vendaMapper;

    @GetMapping
    @Operation(summary = "Listar veículos", description = "Lista todos os veículos ou filtra por status (DISPONIVEL ou VENDIDO)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de veículos"),
        @ApiResponse(responseCode = "400", description = "Status inválido")
    })
    public ResponseEntity<List<VeiculoResponse>> listarVeiculos(
            @Parameter(description = "Status do veículo (opcional)", schema = @io.swagger.v3.oas.annotations.media.Schema(allowableValues = {"DISPONIVEL", "VENDIDO"}))
            @RequestParam(value = "status", required = false) String statusParam) {

        List<Veiculo> veiculos = (statusParam == null || statusParam.trim().isEmpty())
                ? listarTodosVeiculosApplicationService.execute()
                : listarVeiculosPorStatusApplicationService.execute(parseStatus(statusParam));

        return ResponseEntity.ok(veiculoMapper.toResponseList(veiculos));
    }

    private StatusVeiculo parseStatus(String statusParam) {
        try {
            return StatusVeiculo.valueOf(statusParam.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status deve ser DISPONIVEL ou VENDIDO");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veículo por ID", description = "Busca um veículo específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Veículo encontrado"),
        @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    public ResponseEntity<VeiculoResponse> buscarVeiculoPorId(
            @Parameter(description = "ID do veículo") @PathVariable Long id) {

        Veiculo veiculo = buscarVeiculoPorIdApplicationService.execute(id);
        VeiculoResponse response = veiculoMapper.toResponse(veiculo);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo veículo", description = "Cadastra um novo veículo para venda")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Veículo cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<VeiculoResponse> cadastrarVeiculo(
            @Valid @RequestBody CadastrarVeiculoRequest request) {

        Veiculo veiculo = veiculoMapper.toDomain(request);
        Veiculo veiculoCadastrado = cadastrarVeiculoApplicationService.execute(veiculo);
        VeiculoResponse response = veiculoMapper.toResponse(veiculoCadastrado);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar um veículo", description = "Edita os dados de um veículo existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Veículo editado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Veículo não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou veículo não disponível")
    })
    public ResponseEntity<VeiculoResponse> editarVeiculo(
            @Parameter(description = "ID do veículo") @PathVariable Long id,
            @Valid @RequestBody CadastrarVeiculoRequest request) {

        Veiculo veiculo = veiculoMapper.toDomain(request);
        Veiculo veiculoEditado = editarVeiculoApplicationService.execute(id, veiculo);
        VeiculoResponse response = veiculoMapper.toResponse(veiculoEditado);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/venda")
    @Operation(summary = "Vender um veículo", description = "Efetua a venda de um veículo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venda registrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Veículo não encontrado"),
        @ApiResponse(responseCode = "400", description = "Veículo não disponível para venda")
    })
    public ResponseEntity<VendaResponse> venderVeiculo(
            @Parameter(description = "ID do veículo") @PathVariable Long id,
            @Valid @RequestBody VenderVeiculoRequest request) {

        log.info("Iniciando venda do veículo ID: {} para cliente CPF: {}", id, request.cpfCliente());

        var venda = venderVeiculoApplicationService.execute(id, request.cpfCliente(), request.nomeCliente(), request.valorVenda());
        VendaResponse response = vendaMapper.toResponse(venda);

        log.info("Venda realizada com sucesso. Código pagamento: {}", venda.getCodigoPagamento());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendidos")
    @Operation(summary = "Listar veículos vendidos", description = "Lista todos os veículos vendidos com informações da venda")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de veículos vendidos")
    })
    public ResponseEntity<List<org.com.revenda.presentation.dto.response.VendaComVeiculoResponse>> listarVeiculosVendidos() {
        log.info("Listando todos os veículos vendidos");

        var vendas = listarVeiculosVendidosApplicationService.execute();
        var response = vendaMapper.toVendaComVeiculoResponseList(vendas);

        log.info("Total de veículos vendidos: {}", response.size());

        return ResponseEntity.ok(response);
    }
}
