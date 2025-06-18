package br.com.incaas.service;

import br.com.incaas.enums.StatusProcesso;
import br.com.incaas.enums.TipoAudiencia;
import br.com.incaas.model.Audiencia;
import br.com.incaas.model.ProcessoJudicial;
import br.com.incaas.repository.AudienciaRepository;
import br.com.incaas.repository.ProcessoJudicialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AudienciaServiceTest {

    @Mock
    private AudienciaRepository audienciaRepository;

    @Mock
    private ProcessoJudicialRepository processoJudicialRepository;

    @InjectMocks
    private AudienciaService audienciaService;

    private ProcessoJudicial processoAtivo;
    private ProcessoJudicial processoArquivado;
    private Audiencia audiencia;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        processoAtivo = new ProcessoJudicial(1L, "3160429-29.0148.7.46.0911", "1ª Vara Cível", "São Paulo", "Ação de Cobrança", StatusProcesso.ATIVO);
        processoArquivado = new ProcessoJudicial(2L, "3160429-29.0148.7.46.0912", "2ª Vara Criminal", "Rio de Janeiro", "Ação Penal", StatusProcesso.ARQUIVADO);

        audiencia = new Audiencia(1L, LocalDateTime.of(2025, 6, 18, 10, 0), TipoAudiencia.JULGAMENTO, "Sala 1", processoAtivo);

        when(processoJudicialRepository.findById(processoAtivo.getId())).thenReturn(Optional.of(processoAtivo));
        when(processoJudicialRepository.findById(processoArquivado.getId())).thenReturn(Optional.of(processoArquivado));
        when(audienciaRepository.findAll()).thenReturn(Collections.singletonList(audiencia));
    }

    @Test
    void deveImpedirSobreposicaoDeAudienciasNoMesmoLocalEHorario() {
        when(processoJudicialRepository.findById(processoAtivo.getId())).thenReturn(Optional.of(processoAtivo));
        when(audienciaRepository.findAll()).thenReturn(Collections.singletonList(audiencia));

        Audiencia novaAudiencia = new Audiencia(null, audiencia.getDataHora(), audiencia.getTipo(), audiencia.getLocal(), processoAtivo);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> audienciaService.criar(novaAudiencia));
        assertEquals("Já existe uma audiência marcada para este local, data e hora na mesma vara.", exception.getMessage());
    }

    @Test
    void deveRestringirAudienciasADiasUteis() {
        when(processoJudicialRepository.findById(processoAtivo.getId())).thenReturn(Optional.of(processoAtivo));

        Audiencia audienciaInvalida = new Audiencia(null, LocalDateTime.of(2025, 6, 21, 10, 0), TipoAudiencia.JULGAMENTO, "Sala 1", processoAtivo);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> audienciaService.criar(audienciaInvalida));
        assertEquals("Audiências só podem ser agendadas em dias úteis", exception.getMessage());
    }

    @Test
    void deveBloquearAgendamentoParaProcessoArquivadoOuSuspenso() {
        when(processoJudicialRepository.findById(2L)).thenReturn(Optional.of(processoArquivado));

        Audiencia audienciaInvalida = new Audiencia(null, LocalDateTime.of(2025, 6, 18, 10, 0), TipoAudiencia.JULGAMENTO, "Sala 1", processoArquivado);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> audienciaService.criar(audienciaInvalida));
        assertEquals("Não é possível agendar audiências para processos arquivados ou suspensos", exception.getMessage());
    }

    @Test
    void deveFiltrarAudienciasPorComarcaEDia() {
        audiencia.setProcessoJudicial(processoAtivo);
        audiencia.getProcessoJudicial().setComarca("Comarca A");
        audiencia.setDataHora(LocalDateTime.of(2025, 6, 18, 10, 0));

        when(audienciaRepository.findAll()).thenReturn(Collections.singletonList(audiencia));

        var resultado = audienciaService.consultarAgendaPorComarcaEDia("Comarca A", LocalDate.of(2025, 6, 18));

        assertEquals(1, resultado.size());
        assertEquals(audiencia, resultado.get(0));
    }
}