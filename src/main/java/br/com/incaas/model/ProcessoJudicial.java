package br.com.incaas.model;

import br.com.incaas.enums.StatusProcesso;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Entity
@Data
public class ProcessoJudicial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "\\d{7}-\\d{2}\\.\\d{4}\\.\\d{1}\\.\\d{2}\\.\\d{4}", message = "Número do processo deve seguir o formato 0000000-00.0000.0.00.0000")
    @Column(unique = true, nullable = false)
    private String numeroProcesso;

    @Column(nullable = false)
    private String vara;

    @Column(nullable = false)
    private String comarca;

    @Column(nullable = false)
    private String assunto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProcesso status;

    @JsonIgnore
    @OneToMany(mappedBy = "processoJudicial", cascade = CascadeType.ALL)
    private List<Audiencia> audiencias;

    public ProcessoJudicial(Long id, String numeroProcesso, String vara, String comarca, String assunto, StatusProcesso status) {
        this.id = id;
        this.numeroProcesso = numeroProcesso;
        this.vara = vara;
        this.comarca = comarca;
        this.assunto = assunto;
        this.status = status;
    }

    public ProcessoJudicial() {
        
    }
}
