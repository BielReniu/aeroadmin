package cat.uvic.teknos.dam.aeroadmin.server;

import cat.uvic.teknos.dam.aeroadmin.jpa.repositories.JpaRepositoryFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;
import cat.uvic.teknos.dam.aeroadmin.server.controllers.AirlineController;
import cat.uvic.teknos.dam.aeroadmin.server.controllers.SecurityController;
import cat.uvic.teknos.dam.aeroadmin.utilities.security.EncryptionUtils;

import java.security.KeyStore;

public class App {
    public static void main(String[] args) {
        try {
            RepositoryFactory repositoryFactory = new JpaRepositoryFactory();

            KeyStore serverKeyStore = EncryptionUtils.loadKeyStore("/server.jks", "123456");

            var airlineController = new AirlineController(repositoryFactory.getAirlineRepository());
            var securityController = new SecurityController(serverKeyStore);

            var router = new RequestRouter(airlineController, securityController);

            var server = new Server(8082, router);
            server.start();

        } catch (Exception e) {
            System.err.println("❌ Error fatal iniciant el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}