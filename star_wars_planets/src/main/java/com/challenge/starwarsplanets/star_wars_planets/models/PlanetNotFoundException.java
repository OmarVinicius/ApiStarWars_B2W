package com.challenge.starwarsplanets.star_wars_planets.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Esta classe representa a exceção que pode ocorrer (caracterizado pelo erro
 * 404) caso um planeta não seja encontrado no banco de dados.
 *
 * @author Omar Vinicius Rolim
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlanetNotFoundException extends RuntimeException {

    /**
     * Construtor da classe PlanetNotFoundException.
     * @param exception Descrição da exceção levantada.
     */
    public PlanetNotFoundException(String exception) {
        super(exception);
    }
}
