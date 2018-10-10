package com.challenge.starwarsplanets.star_wars_planets.controllers;

import com.challenge.starwarsplanets.star_wars_planets.models.Planet;
import com.challenge.starwarsplanets.star_wars_planets.models.PlanetNotFoundException;
import com.challenge.starwarsplanets.star_wars_planets.repositories.PlanetsRepository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Esta classe representa o controlador da API.
 *
 * @author Omar Vinicius Rolim
 */
@RestController
@RequestMapping("/apistarwars/planets")
public class PlanetsController {

    @Autowired
    private PlanetsRepository repository;

    /**
     * Este método é responsável por obter todos os planetas armazenados no
     * banco de dados utilizando a operação HTTP GET.
     * <p>
     * <b>Ex. de requisição: curl -H "Content-Type: application/json" -X GET
     * http://localhost:8080/apistarwars/planets</b></p>
     *
     * @return List&#60;Planet&#62; - Lista contendo todos os planetas.
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Planet> getAllPlanets() {
        return repository.findAll();
    }

    /**
     * Este método é responsável por obter um planeta armazenado no banco de
     * dados utilizando a operação HTTP GET e a partir do seu identificador.
     * <p>
     * <b>Ex. de requisição: curl -H "Content-Type: application/json" -X GET
     * http://localhost:8080/apistarwars/planets/5bbe40950bb3e918ecdbecae</b></p>
     *
     * @param id Identificador do planeta seguindo o formato ObjectId do
     * MongoDB.
     * @return Planet - Instância do planeta, caso tenha sido encontrado.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Planet getPlanetById(@PathVariable("id") String id) {
        Planet found = repository.findById(new ObjectId(id));
        if (found != null) {
            return found;
        } else {
            throw new PlanetNotFoundException("id-" + id);
        }
    }

    /**
     * Este método é responsável por obter um planeta armazenado no banco de
     * dados utilizando a operação HTTP GET e a partir do seu nome.
     * <p>
     * <b>Ex. de requisição: curl -H "Content-Type: application/json" -X GET
     * http://localhost:8080/apistarwars/planets?name=Yavin%20IV</b></p>
     *
     * @param name Nome do planeta.
     * @return Planet - Instância do planeta, caso tenha sido encontrado.
     */
    @RequestMapping(method = RequestMethod.GET, params = "name")
    @ResponseBody
    public Planet getPlanetByName(String name) {
        Planet found = repository.findByName(name);
        if (found != null) {
            return found;
        } else {
            throw new PlanetNotFoundException("name-" + name);
        }
    }

    /**
     * Este método é responsável por criar e inserir um novo planeta ao banco de
     * dados utilizando a operação HTTP POST.
     * <p>
     * <b>Ex. de requisição: curl -H "Content-Type: application/json" -X POST -d
     * {\"name\":\""Yavin IV"\",\"climate\":\"arido\",\"terrain\":\"rochoso\"}
     * http://localhost:8080/apistarwars/planets</b></p>
     *
     * @param planet Instância do planeta a ser criado e contendo as informações
     * do JSON recebido pela requisição HTTP.
     * @return Planet - Instância do planeta criado.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Planet createPlanet(@Valid @RequestBody Planet planet) {
        Planet found = repository.findByName(planet.getName());
        if (found == null) {
            planet.setId(ObjectId.get());
            int countMovies = getFilmsPlanet(planet.getName());
            if (countMovies != -1) {
                planet.setInMovies(countMovies);
                repository.save(planet);
                return planet;
            } else {
                throw new PlanetsControllerInternalException("Error fetching movie from SWAPI API");
            }
        } else {
            throw new PlanetsControllerInternalException("This planet already exists");
        }
    }

    /**
     * Este método é responsável por apagar um planeta do banco de dados
     * utilizando a operação HTTP DELETE.
     * <p>
     * <b>Ex. de requisição: curl -H "Content-Type: application/json" -X DELETE
     * http://localhost:8080/apistarwars/planets/5bbe4f030bb3e911f4b503e8</b></p>
     *
     * @param id Identificador do planeta seguindo o formato ObjectId do
     * MongoDB.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deletePlanet(@PathVariable("id") String id) {
        Planet toDelete = repository.findById(new ObjectId(id));
        if (toDelete != null) {
            repository.delete(toDelete);
        } else {
            throw new PlanetNotFoundException("id-" + id);
        }
    }

    /**
     * Este método é responsável por obter a quantidade de filmes em que um
     * determinado planeta apareceu.
     *
     * @param planetName Nome do planeta buscado.
     */
    private int getFilmsPlanet(String planetName) {
        JSONParser parser = new JSONParser();
        try {
            URL swapi = new URL("https://swapi.co/api/planets/?format=json&search="
                    + planetName.toLowerCase().split(" ")[0]);
            HttpsURLConnection urlconn = (HttpsURLConnection) swapi.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = urlconn.getResponseCode();
            if (responseCode != 200) {
                return -1;
            }

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlconn.getInputStream()))) {

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    try {
                        JSONObject jsonFromString = (JSONObject) parser.parse(inputLine);
                        JSONArray results = (JSONArray) jsonFromString.get("results");
                        for (Object o : results) {
                            JSONObject currentJsonObj = (JSONObject) o;
                            try {
                                JSONArray movies = (JSONArray) currentJsonObj.get("films");
                                return movies.size();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    } catch (ParseException ex) {
                        System.out.println(ex);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
        return -1;
    }
}
