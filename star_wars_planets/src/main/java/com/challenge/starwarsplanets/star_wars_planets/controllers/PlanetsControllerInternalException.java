package com.challenge.starwarsplanets.star_wars_planets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Esta classe representa a exceção que pode ocorrer (caracterizado pelo erro
 * 500) durante a execução de alguma operação do controlador.
 *
 * @author Omar Vinicius Rolim
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PlanetsControllerInternalException extends RuntimeException {

    /**
     * Construtor da classe PlanetsControllerInternalException.
     * @param exception Descrição da exceção levantada.
     */
    public PlanetsControllerInternalException(String exception) {
        super(exception);
    }
}
