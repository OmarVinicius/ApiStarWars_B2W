package com.challenge.starwarsplanets.star_wars_planets.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Esta classe representa o planeta com seus dados principais.
 *
 * @author Omar Vinicius Rolim
 */
@Document(collection = "planets")
public class Planet {

    @Id
    public ObjectId id;
    public String name;
    public String climate;
    public String terrain;
    public int inMovies;

    public Planet() {

    }

    /**
     * Construtor da classe Planet.
     * @param id Identificador no padrão ObjectId do MongoDB.
     * @param name Nome do planeta.
     * @param climate Clima do planeta.
     * @param terrain Terreno do planeta.
     * @param inMovies Quantidade de filmes em que o planeta apareceu.
     */
    public Planet(ObjectId id, String name, String climate, String terrain, int inMovies) {
        this.id = id;
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
        this.inMovies = inMovies;
    }

    public String getId() {
        return id.toHexString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public int getInMovies() {
        return inMovies;
    }

    public void setInMovies(int inMovies) {
        this.inMovies = inMovies;
    }

}
