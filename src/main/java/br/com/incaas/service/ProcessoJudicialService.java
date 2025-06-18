package br.com.incaas.service;

import br.com.incaas.model.ProcessoJudicial;
import br.com.incaas.repository.ProcessoJudicialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessoJudicialService {

    @Autowired
    private ProcessoJudicialRepository repository;

    public ProcessoJudicial salvarProcesso(ProcessoJudicial processo) {
        return repository.save(processo);
    }

    public List<ProcessoJudicial> listarProcessos() {
        return repository.findAll();
    }

    public List<ProcessoJudicial> filtrarPorStatusEComarca(String status, String comarca) {
        return repository.findByStatusAndComarca(status, comarca);
    }

    public ProcessoJudicial buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));
    }

    public List<ProcessoJudicial> listarTodos() {
        return repository.findAll();
    }

    public ProcessoJudicial criar(ProcessoJudicial processo) {
        try {
            return repository.save(processo);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Já existe um processo judicial com o número " + processo.getNumeroProcesso() + ". Por favor, utilize um número único.");
        }
    }

    public ProcessoJudicial atualizar(Long id, ProcessoJudicial processo) {
        ProcessoJudicial processoExistente = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));
        processoExistente.setNumeroProcesso(processo.getNumeroProcesso());
        processoExistente.setVara(processo.getVara());
        processoExistente.setComarca(processo.getComarca());
        processoExistente.setAssunto(processo.getAssunto());
        processoExistente.setStatus(processo.getStatus());
        return repository.save(processoExistente);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
