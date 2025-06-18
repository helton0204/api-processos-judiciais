package br.com.incaas.controller;

import br.com.incaas.model.Audiencia;
import br.com.incaas.service.AudienciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audiencias")
public class AudienciaController {

    @Autowired
    private AudienciaService audienciaService;

    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Listar todas as audiências", description = "Retorna uma lista de todas as audiências cadastradas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de audiências retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Audiencia>> listarTodas() {
        List<Audiencia> audiencias = audienciaService.listarTodas();
        return ResponseEntity.ok(audiencias);
    }

    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Buscar audiência por ID", description = "Retorna uma audiência específica com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audiência encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Audiência não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Audiencia> buscarPorId(@PathVariable Long id) {
        Audiencia audiencia = audienciaService.buscarPorId(id);
        return ResponseEntity.ok(audiencia);
    }

    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Criar uma nova audiência", description = "Cria uma nova audiência com os dados fornecidos.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Exemplo de corpo da requisição", required = true, content = @Content(mediaType = "application/json", examples = {
        @ExampleObject(value = "{\n  \"dataHora\": \"2025-06-26T10:00:00\",\n  \"tipo\": \"JULGAMENTO\",\n  \"local\": \"Sala 1\",\n  \"processoJudicial\": {\n    \"id\": 1\n  }\n}")
    })))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Audiência criada com sucesso")
    })
    @PostMapping
    public ResponseEntity<Audiencia> criar(@RequestBody Audiencia audiencia) {
        Audiencia novaAudiencia = audienciaService.criar(audiencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaAudiencia);
    }

    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Atualizar uma audiência", description = "Atualiza os dados de uma audiência existente com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audiência atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Audiência não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Audiencia> atualizar(@PathVariable Long id, @RequestBody Audiencia audiencia) {
        Audiencia audienciaAtualizada = audienciaService.atualizar(id, audiencia);
        return ResponseEntity.ok(audienciaAtualizada);
    }

    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Deletar uma audiência", description = "Remove uma audiência existente com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Audiência deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Audiência não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        audienciaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Consultar agenda por comarca e dia", description = "Retorna uma lista de audiências agendadas para uma comarca específica em um dia específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agenda consultada com sucesso")
    })
    @GetMapping("/agenda")
    public ResponseEntity<List<Audiencia>> consultarAgendaPorComarcaEDia(@RequestParam String comarca, @RequestParam LocalDate dia) {
        List<Audiencia> audiencias = audienciaService.consultarAgendaPorComarcaEDia(comarca, dia);
        return ResponseEntity.ok(audiencias);
    }
}
