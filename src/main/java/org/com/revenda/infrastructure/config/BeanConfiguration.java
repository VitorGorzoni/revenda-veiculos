package org.com.revenda.infrastructure.config;

import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.application.gateway.VendaPersistenceGateway;
import org.com.revenda.application.service.*;
import org.com.revenda.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de beans dos Use Cases (camada de aplicação).
 * Esta classe pertence à camada de infraestrutura e faz a ligação
 * entre os adapters e os use cases.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public CadastrarVeiculoUseCase cadastrarVeiculoUseCase(VeiculoPersistenceGateway veiculoPersistenceGateway) {
        return new CadastrarVeiculoService(veiculoPersistenceGateway);
    }

    @Bean
    public BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase(VeiculoPersistenceGateway veiculoPersistenceGateway) {
        return new BuscarVeiculoPorIdService(veiculoPersistenceGateway);
    }

    @Bean
    public EditarVeiculoUseCase editarVeiculoUseCase(VeiculoPersistenceGateway veiculoPersistenceGateway) {
        return new EditarVeiculoService(veiculoPersistenceGateway);
    }

    @Bean
    public ListarTodosVeiculosUseCase listarTodosVeiculosUseCase(VeiculoPersistenceGateway veiculoPersistenceGateway) {
        return new ListarTodosVeiculosService(veiculoPersistenceGateway);
    }

    @Bean
    public ListarVeiculosPorStatusUseCase listarVeiculosPorStatusUseCase(VeiculoPersistenceGateway veiculoPersistenceGateway) {
        return new ListarVeiculosPorStatusService(veiculoPersistenceGateway);
    }

    @Bean
    public ListarVeiculosDisponiveisUseCase listarVeiculosDisponiveisUseCase(VeiculoPersistenceGateway veiculoPersistenceGateway) {
        return new ListarVeiculosDisponiveisService(veiculoPersistenceGateway);
    }

    @Bean
    public VenderVeiculoUseCase venderVeiculoUseCase(
            VeiculoPersistenceGateway veiculoPersistenceGateway,
            VendaPersistenceGateway vendaPersistenceGateway) {
        return new VenderVeiculoService(veiculoPersistenceGateway, vendaPersistenceGateway);
    }

    @Bean
    public ProcessarPagamentoUseCase processarPagamentoUseCase(
            VendaPersistenceGateway vendaPersistenceGateway,
            VeiculoPersistenceGateway veiculoPersistenceGateway) {
        return new ProcessarPagamentoService(vendaPersistenceGateway, veiculoPersistenceGateway);
    }

    @Bean
    public ListarVeiculosVendidosUseCase listarVeiculosVendidosUseCase(VendaPersistenceGateway vendaPersistenceGateway) {
        return new ListarVeiculosVendidosService(vendaPersistenceGateway);
    }

    @Bean
    public ListarTodasVendasUseCase listarTodasVendasUseCase(VendaPersistenceGateway vendaPersistenceGateway) {
        return new ListarTodasVendasService(vendaPersistenceGateway);
    }

    @Bean
    public ListarVendasPorStatusPagamentoUseCase listarVendasPorStatusPagamentoUseCase(VendaPersistenceGateway vendaPersistenceGateway) {
        return new ListarVendasPorStatusPagamentoService(vendaPersistenceGateway);
    }
}
