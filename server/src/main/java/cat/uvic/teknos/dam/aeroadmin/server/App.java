package cat.uvic.teknos.dam.aeroadmin.server;

import cat.uvic.teknos.dam.aeroadmin.jpa.repositories.JpaRepositoryFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;
import cat.uvic.teknos.dam.aeroadmin.server.controllers.AirlineController;

public class App {
    public static void main(String[] args) {
        // 1. Inicialitzem les dependències (com la fàbrica de repositoris)
        RepositoryFactory repositoryFactory = new JpaRepositoryFactory(); // O la de JDBC si vols

        // 2. Creem els controladors
        var airlineController = new AirlineController(repositoryFactory.getAirlineRepository());

        // 3. Creem el router i li passem els controladors
        var router = new RequestRouter(airlineController);

        // 4. Creem i engeguem el servidor
        var server = new Server(8082, router); // Escoltarà al port 8080
        server.start();
    }
}