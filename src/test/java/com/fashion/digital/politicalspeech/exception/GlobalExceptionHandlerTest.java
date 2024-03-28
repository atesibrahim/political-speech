package com.fashion.digital.politicalspeech.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private static final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleMethodArgumentNotValid() {

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getMessage()).thenReturn("Some message with default message method validation exception");

        var actual = handler.handleMethodArgumentNotValid(ex, null, null, null);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, actual.getStatusCode());
        assertEquals(" method validation exception", actual.getBody());
    }

    @Test
    void handleMissingServletRequestParameter() {
        final ServletRequestBindingException mock = new ServletRequestBindingException("default message servlet binding error");
        var msg = mock.getMessage();
        var defaultMsg = msg.substring(msg.lastIndexOf("default message") + 15);
        final ResponseEntity<Object> expectedResponse = new ResponseEntity<>(defaultMsg, HttpStatus.BAD_REQUEST);

        var actual = handler.handleServletRequestBindingException(mock, null, null, null);

        assertNotNull(actual);
        assertEquals(expectedResponse, actual);
    }
}