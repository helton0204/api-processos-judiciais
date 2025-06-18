package br.com.incaas.service;

import br.com.incaas.enums.StatusProcesso;
import br.com.incaas.model.ProcessoJudicial;
import br.com.incaas.repository.ProcessoJudicialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessoJudicialServiceTest {

    @Mock
    private ProcessoJudicialRepository processoJudicialRepository;

    @InjectMocks
    private ProcessoJudicialService processoJudicialService;

    private ProcessoJudicial processo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        processo = new ProcessoJudicial(
            1L,
            "3160429-29.0148.7.46.0911",
            "1ª Vara Cível",
            "São Paulo",
            "Ação de Cobrança",
            StatusProcesso.ATIVO
        );

        when(processoJudicialRepository.findById(processo.getId())).thenReturn(Optional.of(processo));
        when(processoJudicialRepository.findAll()).thenReturn(Collections.singletonList(processo));
    }

    @Test
    void deveSalvarProcessoJudicial() {
        when(processoJudicialRepository.save(processo)).thenReturn(processo);

        ProcessoJudicial resultado = processoJudicialService.salvarProcesso(processo);

        assertNotNull(resultado);
        assertEquals(processo.getId(), resultado.getId());
    }

    @Test
    void deveListarTodosOsProcessosJudiciais() {
        List<ProcessoJudicial> processos = processoJudicialService.listarProcessos();

        assertNotNull(processos);
        assertEquals(1, processos.size());
        assertEquals(processo, processos.get(0));
    }

    @Test
    void deveFiltrarProcessosPorStatusEComarca() {
        when(processoJudicialRepository.findByStatusAndComarca(StatusProcesso.ATIVO.name(), "São Paulo"))
            .thenReturn(Collections.singletonList(processo));

        List<ProcessoJudicial> processosFiltrados = processoJudicialService.filtrarPorStatusEComarca(StatusProcesso.ATIVO.name(), "São Paulo");

        assertNotNull(processosFiltrados);
        assertEquals(1, processosFiltrados.size());
        assertEquals(processo, processosFiltrados.get(0));
    }

    @Test
    void deveBuscarProcessoPorId() {
        ProcessoJudicial resultado = processoJudicialService.buscarPorId(processo.getId());

        assertNotNull(resultado);
        assertEquals(processo.getId(), resultado.getId());
    }

    @Test
    void deveAtualizarProcessoJudicial() {
        ProcessoJudicial atualizado = new ProcessoJudicial(
            processo.getId(),
            "1234567-89.0123.4.56.7890",
            "2ª Vara Criminal",
            "Rio de Janeiro",
            "Ação Penal",
            StatusProcesso.SUSPENSO
        );

        when(processoJudicialRepository.save(atualizado)).thenReturn(atualizado);

        ProcessoJudicial resultado = processoJudicialService.atualizar(processo.getId(), atualizado);

        assertNotNull(resultado);
        assertEquals(atualizado.getNumeroProcesso(), resultado.getNumeroProcesso());
        assertEquals(atualizado.getVara(), resultado.getVara());
        assertEquals(atualizado.getComarca(), resultado.getComarca());
        assertEquals(atualizado.getAssunto(), resultado.getAssunto());
        assertEquals(atualizado.getStatus(), resultado.getStatus());
    }

    @Test
    void deveDeletarProcessoJudicial() {
        doNothing().when(processoJudicialRepository).deleteById(processo.getId());

        assertDoesNotThrow(() -> processoJudicialService.deletar(processo.getId()));

        verify(processoJudicialRepository, times(1)).deleteById(processo.getId());
    }
}
