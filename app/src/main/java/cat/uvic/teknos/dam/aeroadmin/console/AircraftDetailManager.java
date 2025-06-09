// src/main/java/cat/uvic/teknos/dam/aeroadmin/console/AircraftDetailManager.java
package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.AircraftDetail;
import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftDetailRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.util.Scanner;

public class AircraftDetailManager {
    private final AircraftDetailRepository repo;
    private final Scanner               sc;

    public AircraftDetailManager(RepositoryFactory rf, Scanner sc) {
        this.sc   = sc;
        this.repo = rf.getAircraftDetailRepository();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor de Detalls d'Avió ---");
            System.out.println("1. Llistar detalls");
            System.out.println("2. Crear nou detall");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");
            String c = sc.nextLine().trim();

            switch (c) {
                case "1":
                    repo.getAll()
                            .forEach(d -> System.out.println(
                                    "AeroID: "+d.getAircraftId()+
                                            ", Pass: "+d.getPassengerCapacity()));
                    break;
                case "2":
                    AircraftDetail nd = repo.create();
                    System.out.print("Capacitat: ");
                    nd.setPassengerCapacity(Integer.parseInt(sc.nextLine()));
                    repo.save(nd);
                    System.out.println("Detall creat.");
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Opció invàlida.");
            }
        }
    }
}
