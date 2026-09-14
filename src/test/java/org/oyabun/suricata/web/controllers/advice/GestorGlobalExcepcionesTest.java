package org.oyabun.suricata.web.controllers.advice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.oyabun.suricata.web.model.RespuestaError;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GestorGlobalExcepcionesTest {

    private final GestorGlobalExcepciones advice = new GestorGlobalExcepciones();

    @DisplayName("Debería mapear los errores de validación a RespuestaError con status 400.")
    @Test
    void testManejarValidacion() throws Exception {
        // arrange
        Dto dto = new Dto(null, 300);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(dto, "dto");
        bindingResult.rejectValue("nombre", "NotNull", "El nombre no puede ser nulo");
        bindingResult.rejectValue("edad", "Max", "La edad no puede superar 200");

        MethodParameter methodParameter = new MethodParameter(
                this.getClass().getDeclaredMethod("metodoDummy", Dto.class), 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        // act
        ResponseEntity<List<RespuestaError>> respuesta = advice.manejarValidacion(ex);

        // assert
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(respuesta.getBody()).hasSize(2);
        assertThat(respuesta.getBody())
                .extracting(RespuestaError::mensaje)
                .containsExactly("La edad no puede superar 200", "El nombre no puede ser nulo");
        assertThat(respuesta.getBody())
                .extracting(RespuestaError::tipoDeError)
                .containsOnly("Bad Request");
    }

    private void metodoDummy(Dto dto) {
    }

    private record Dto(String nombre, Integer edad) {
    }
}
