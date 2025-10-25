package org.com.revenda.infrastructure.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.usecase.*;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.enums.StatusVeiculo;
import org.com.revenda.infrastructure.web.dto.request.CadastrarVeiculoRequest;
import org.com.revenda.infrastructure.web.dto.request.VenderVeiculoRequest;
import org.com.revenda.infrastructure.web.dto.response.VeiculoResponse;
import org.com.revenda.infrastructure.web.dto.response.VendaResponse;
import org.com.revenda.infrastructure.web.mapper.VeiculoDtoMapper;
import org.com.revenda.infrastructure.web.mapper.VendaDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Veículos", description = "Operações relacionadas aos veículos")
public class VeiculoController {

    private final CadastrarVeiculoUseCase cadastrarVeiculoUseCase;
    private final EditarVeiculoUseCase editarVeiculoUseCase;
    private final VenderVeiculoUseCase venderVeiculoUseCase;
    private final ListarVeiculosPorStatusUseCase listarVeiculosPorStatusUseCase;
    private final ListarTodosVeiculosUseCase listarTodosVeiculosUseCase;
    private final BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase;
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
                ? listarTodosVeiculosUseCase.execute()
                : listarVeiculosPorStatusUseCase.execute(parseStatus(statusParam));

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

        Veiculo veiculo = buscarVeiculoPorIdUseCase.execute(id);
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
        Veiculo veiculoCadastrado = cadastrarVeiculoUseCase.execute(veiculo);
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
        Veiculo veiculoEditado = editarVeiculoUseCase.execute(id, veiculo);
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

        var venda = venderVeiculoUseCase.execute(id, request.cpfCliente(), request.nomeCliente(), request.valorVenda());
        VendaResponse response = vendaMapper.toResponse(venda);

        log.info("Venda realizada com sucesso. Código pagamento: {}", venda.getCodigoPagamento());

        return ResponseEntity.ok(response);
    }
}

