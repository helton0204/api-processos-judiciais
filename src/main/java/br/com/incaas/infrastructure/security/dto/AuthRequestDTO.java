package br.com.incaas.infrastructure.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Credenciais para autenticação")
public class AuthRequestDTO {

    @NotBlank
    @Schema(example = "admin")
    private String username;

    @NotBlank
    @Schema(example = "admin123")
    private String password;
}
