package org.com.revenda.presentation.controller;

import org.com.revenda.domain.entity.StatusPagamento;
import org.com.revenda.domain.usecase.ProcessarPagamentoUseCase;
import org.com.revenda.presentation.dto.request.WebhookPagamentoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para WebhookController")
class WebhookControllerTest {

    @Mock
    private ProcessarPagamentoUseCase processarPagamentoUseCase;

    @InjectMocks
    private WebhookController webhookController;

    @Test
    @DisplayName("Deve processar pagamento confirmado com sucesso")
    void deveProcessarPagamentoConfirmadoComSucesso() {
        // Arrange
        WebhookPagamentoRequest request = new WebhookPagamentoRequest();
        request.setCodigoPagamento("PAG-ABC12345");
        request.setStatus("CONFIRMADO");

        doNothing().when(processarPagamentoUseCase)
            .execute("PAG-ABC12345", StatusPagamento.CONFIRMADO);

        // Act
        ResponseEntity<Void> response = webhookController.processarPagamento(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(processarPagamentoUseCase, times(1))
            .execute("PAG-ABC12345", StatusPagamento.CONFIRMADO);
    }

    @Test
    @DisplayName("Deve processar pagamento cancelado com sucesso")
    void deveProcessarPagamentoCanceladoComSucesso() {
        // Arrange
        WebhookPagamentoRequest request = new WebhookPagamentoRequest();
        request.setCodigoPagamento("PAG-XYZ98765");
        request.setStatus("CANCELADO");

        doNothing().when(processarPagamentoUseCase)
            .execute("PAG-XYZ98765", StatusPagamento.CANCELADO);

        // Act
        ResponseEntity<Void> response = webhookController.processarPagamento(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(processarPagamentoUseCase, times(1))
            .execute("PAG-XYZ98765", StatusPagamento.CANCELADO);
    }

    @Test
    @DisplayName("Deve aceitar status em minúsculas")
    void deveAceitarStatusEmMinusculas() {
        // Arrange
        WebhookPagamentoRequest request = new WebhookPagamentoRequest();
        request.setCodigoPagamento("PAG-ABC12345");
        request.setStatus("confirmado");

        doNothing().when(processarPagamentoUseCase)
            .execute("PAG-ABC12345", StatusPagamento.CONFIRMADO);

        // Act
        ResponseEntity<Void> response = webhookController.processarPagamento(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(processarPagamentoUseCase, times(1))
            .execute("PAG-ABC12345", StatusPagamento.CONFIRMADO);
    }

    @Test
    @DisplayName("Deve lançar exceção para status inválido")
    void deveLancarExcecaoParaStatusInvalido() {
        // Arrange
        WebhookPagamentoRequest request = new WebhookPagamentoRequest();
        request.setCodigoPagamento("PAG-ABC12345");
        request.setStatus("INVALIDO");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> webhookController.processarPagamento(request));

        verify(processarPagamentoUseCase, never())
            .execute(anyString(), any(StatusPagamento.class));
    }
}
