package br.com.incaas.service;

import br.com.incaas.enums.StatusProcesso;
import br.com.incaas.model.Audiencia;
import br.com.incaas.model.ProcessoJudicial;
import br.com.incaas.repository.AudienciaRepository;
import br.com.incaas.repository.ProcessoJudicialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class AudienciaService {

    @Autowired
    private AudienciaRepository audienciaRepository;

    @Autowired
    private ProcessoJudicialRepository processoJudicialRepository;

    public List<Audiencia> listarTodas() {
        return audienciaRepository.findAll();
    }

    public Audiencia buscarPorId(Long id) {
        return audienciaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Audiência não encontrada"));
    }

    public Audiencia criar(Audiencia audiencia) {
        ProcessoJudicial processo = processoJudicialRepository.findById(audiencia.getProcessoJudicial().getId())
            .orElseThrow(() -> new IllegalArgumentException("Processo judicial não encontrado"));

        audiencia.setProcessoJudicial(processo);
        validarAudiencia(audiencia);
        validarSobreposicaoAudiencia(audiencia);

        return audienciaRepository.save(audiencia);
    }

    public Audiencia atualizar(Long id, Audiencia audiencia) {
        Audiencia existente = audienciaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Audiência não encontrada"));
        existente.setDataHora(audiencia.getDataHora());
        existente.setTipo(audiencia.getTipo());
        existente.setLocal(audiencia.getLocal());
        return audienciaRepository.save(existente);
    }

    public void deletar(Long id) {
        audienciaRepository.deleteById(id);
    }

    private void validarAudiencia(Audiencia audiencia) {
        ProcessoJudicial processo = processoJudicialRepository.findById(audiencia.getProcessoJudicial().getId())
                .orElseThrow(() -> new IllegalArgumentException("Processo judicial não encontrado"));

        // Restringir audiências a dias úteis
        DayOfWeek diaSemana = audiencia.getDataHora().getDayOfWeek();
        if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Audiências só podem ser agendadas em dias úteis");
        }

        // Bloquear agendamento para processos arquivados ou suspensos
        if (processo.getStatus() == StatusProcesso.ARQUIVADO || processo.getStatus() == StatusProcesso.SUSPENSO) {
            throw new IllegalArgumentException("Não é possível agendar audiências para processos arquivados ou suspensos");
        }
    }

    private void validarSobreposicaoAudiencia(Audiencia audiencia) {
        List<Audiencia> audienciasExistentes = audienciaRepository.findAll();
        boolean sobreposicao = audienciasExistentes.stream().anyMatch(a ->
                a.getLocal().equals(audiencia.getLocal()) &&
                        a.getDataHora().equals(audiencia.getDataHora()) &&
                        a.getProcessoJudicial().getVara().equals(audiencia.getProcessoJudicial().getVara())
        );
        if (sobreposicao) {
            throw new IllegalArgumentException("Já existe uma audiência marcada para este local, data e hora na mesma vara.");
        }
    }

    public List<Audiencia> consultarAgendaPorComarcaEDia(String comarca, LocalDate dia) {
        return audienciaRepository.findAll().stream()
            .filter(a -> a.getProcessoJudicial().getComarca().equals(comarca) &&
                    a.getDataHora().toLocalDate().equals(dia))
            .toList();
    }
}
