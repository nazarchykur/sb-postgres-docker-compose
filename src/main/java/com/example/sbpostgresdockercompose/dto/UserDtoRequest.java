package com.example.sbpostgresdockercompose.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "User DTO Request")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDtoRequest {
    @Schema(description = "User's name", example = "John Doe")
    private String name;

    @Schema(description = "User's email", example = "john@example.com")
    private String email;
}
