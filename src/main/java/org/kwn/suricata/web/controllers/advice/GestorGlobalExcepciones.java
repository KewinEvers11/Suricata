package org.kwn.suricata.web.controllers.advice;

import jakarta.validation.ConstraintViolationException;
import org.kwn.suricata.exceptions.SolicitudMonitoreoNoEncontradaException;
import org.kwn.suricata.web.model.RespuestaError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Comparator;
import java.util.List;

@RestControllerAdvice
public class GestorGlobalExcepciones {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<RespuestaError>> manejarValidacion(MethodArgumentNotValidException ex) {
        List<RespuestaError> errores = ex.getBindingResult().getFieldErrors().stream()
                .sorted(Comparator.comparing(FieldError::getField))
                .map(error -> new RespuestaError(error.getDefaultMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .toList();

        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<RespuestaError>> manejarViolacionDeRestricciones(ConstraintViolationException ex) {
        List<RespuestaError> errores = ex.getConstraintViolations().stream()
                .map(violacion -> new RespuestaError(violacion.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .toList();

        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(SolicitudMonitoreoNoEncontradaException.class)
    public ResponseEntity<RespuestaError> manejarSolicitudNoEncontrada(SolicitudMonitoreoNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new RespuestaError(ex.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase()));
    }
}
