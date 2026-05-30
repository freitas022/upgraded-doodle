package br.com.freitas.upgradeddoodle.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse extends ErrorResponse {

    private List<FieldErrorDetails> details;

    @Getter
    @AllArgsConstructor
    public static class FieldErrorDetails {
        private String field;
        private String message;
    }
}
