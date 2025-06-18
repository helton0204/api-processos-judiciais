package br.com.incaas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import br.com.incaas.enums.TipoAudiencia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Audiencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAudiencia tipo;

    @Column(nullable = false)
    private String local;

    @ManyToOne
    @JoinColumn(name = "processo_judicial_id", nullable = false)
    private ProcessoJudicial processoJudicial;

    public Audiencia(Long id, LocalDateTime dataHora, TipoAudiencia tipo, String local, ProcessoJudicial processoJudicial) {
        this.id = id;
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.local = local;
        this.processoJudicial = processoJudicial;
    }

    public Audiencia() {    
        
    }

    public ProcessoJudicial getProcessoJudicial() {
        return processoJudicial;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public TipoAudiencia getTipo() {
        return tipo;
    }

    public String getLocal() {
        return local;
    }
}
