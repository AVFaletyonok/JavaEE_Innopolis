package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с токеном доступа")
public class JwtAuthenticationResponse {
    @Schema(description = "Токен доступа", example = "eyJhbGci0iJTUzUxMiJ9.eyJzdWTi9iJhZH1pbiIsImV4cCI6MIVyMjUwNj...")
    private String token;
}
