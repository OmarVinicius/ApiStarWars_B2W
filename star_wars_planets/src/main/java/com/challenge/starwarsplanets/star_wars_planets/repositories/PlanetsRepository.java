package com.challenge.starwarsplanets.star_wars_planets.repositories;

import com.challenge.starwarsplanets.star_wars_planets.models.Planet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Esta interface apresenta as operações de manipulação do repositório.
 *
 * @author Omar Vinicius Rolim
 */
@RepositoryRestResource(collectionResourceRel = "planets", path = "planets")
public interface PlanetsRepository extends MongoRepository<Planet, String> {

    /**
     * Este método é responsável por obter um planeta armazenado no banco de
     * dados a partir do seu identificador.
     *
     * @param id Identificador do planeta seguindo o formato ObjectId do
     * MongoDB.
     * @return Planet - Instância do planeta, caso tenha sido encontrado.
     */
    Planet findById(@Param("id") ObjectId id);

    /**
     * Este método é responsável por obter um planeta armazenado no banco de
     * dados a partir do seu nome.
     *
     * @param name Nome do planeta.
     * @return Planet - Instância do planeta, caso tenha sido encontrado.
     */
    Planet findByName(@Param("name") String name);
}
