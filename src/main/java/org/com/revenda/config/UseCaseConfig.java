package org.com.revenda.config;

import org.com.revenda.domain.repository.VeiculoRepository;
import org.com.revenda.domain.repository.VendaRepository;
import org.com.revenda.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CadastrarVeiculoUseCase cadastrarVeiculoUseCase(VeiculoRepository veiculoRepository) {
        return new CadastrarVeiculoUseCase(veiculoRepository);
    }

    @Bean
    public EditarVeiculoUseCase editarVeiculoUseCase(VeiculoRepository veiculoRepository) {
        return new EditarVeiculoUseCase(veiculoRepository);
    }

    @Bean
    public BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase(VeiculoRepository veiculoRepository) {
        return new BuscarVeiculoPorIdUseCase(veiculoRepository);
    }

    @Bean
    public ListarTodosVeiculosUseCase listarTodosVeiculosUseCase(VeiculoRepository veiculoRepository) {
        return new ListarTodosVeiculosUseCase(veiculoRepository);
    }

    @Bean
    public ListarVeiculosPorStatusUseCase listarVeiculosPorStatusUseCase(VeiculoRepository veiculoRepository) {
        return new ListarVeiculosPorStatusUseCase(veiculoRepository);
    }

    @Bean
    public ListarVeiculosVendidosUseCase listarVeiculosVendidosUseCase(VendaRepository vendaRepository) {
        return new ListarVeiculosVendidosUseCase(vendaRepository);
    }

    @Bean
    public VenderVeiculoUseCase venderVeiculoUseCase(VeiculoRepository veiculoRepository, VendaRepository vendaRepository) {
        return new VenderVeiculoUseCase(veiculoRepository, vendaRepository);
    }

    @Bean
    public ProcessarPagamentoUseCase processarPagamentoUseCase(VendaRepository vendaRepository, VeiculoRepository veiculoRepository) {
        return new ProcessarPagamentoUseCase(vendaRepository, veiculoRepository);
    }
}
