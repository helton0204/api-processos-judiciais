package br.com.incaas.controller;

import br.com.incaas.model.ProcessoJudicial;
import br.com.incaas.service.ProcessoJudicialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/processos")
public class ProcessoJudicialController {

    @Autowired
    private ProcessoJudicialService processoJudicialService;

    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Criar um novo processo judicial", description = "Cria um novo processo judicial com os dados fornecidos.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Exemplo de corpo da requisição", required = true, content = @Content(mediaType = "application/json", examples = {
        @ExampleObject(value = "{\n  \"numeroProcesso\": \"3160429-29.0148.7.46.0911\",\n  \"vara\": \"1ª Vara Cível\",\n  \"comarca\": \"São Paulo\",\n  \"assunto\": \"Ação de Cobrança\",\n  \"status\": \"ATIVO\"\n}")
    })))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Processo judicial criado com sucesso")
    })
    @PostMapping
    public ResponseEntity<ProcessoJudicial> criarProcesso(@RequestBody ProcessoJudicial processo) {
        ProcessoJudicial novoProcesso = processoJudicialService.criar(processo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProcesso);
    }

    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Listar todos os processos judiciais", description = "Retorna uma lista de todos os processos judiciais cadastrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de processos retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ProcessoJudicial>> listarProcessos() {
        List<ProcessoJudicial> processos = processoJudicialService.listarTodos();
        return ResponseEntity.ok(processos);
    }

    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Buscar processo judicial por ID", description = "Retorna um processo judicial específico com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Processo judicial encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Processo judicial não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProcessoJudicial> buscarPorId(@PathVariable Long id) {
        ProcessoJudicial processo = processoJudicialService.buscarPorId(id);
        return ResponseEntity.ok(processo);
    }

    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Atualizar um processo judicial", description = "Atualiza os dados de um processo judicial existente com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Processo judicial atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Processo judicial não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProcessoJudicial> atualizar(@PathVariable Long id, @RequestBody ProcessoJudicial processo) {
        ProcessoJudicial processoAtualizado = processoJudicialService.atualizar(id, processo);
        return ResponseEntity.ok(processoAtualizado);
    }

    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Deletar um processo judicial", description = "Remove um processo judicial existente com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Processo judicial deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Processo judicial não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        processoJudicialService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Consultar processos por status", description = "Retorna uma lista de processos judiciais com base no status fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Processos consultados com sucesso")
    })
    @GetMapping("/status")
    public ResponseEntity<List<ProcessoJudicial>> consultarPorStatus(@RequestParam String status) {
        List<ProcessoJudicial> processosFiltrados = processoJudicialService.filtrarPorStatusEComarca(status, null);
        return ResponseEntity.ok(processosFiltrados);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/filtrar")
    public ResponseEntity<List<ProcessoJudicial>> filtrarPorStatusEComarca(@RequestParam String status, @RequestParam String comarca) {
        List<ProcessoJudicial> processosFiltrados = processoJudicialService.filtrarPorStatusEComarca(status, comarca);
        return ResponseEntity.ok(processosFiltrados);
    }
}
