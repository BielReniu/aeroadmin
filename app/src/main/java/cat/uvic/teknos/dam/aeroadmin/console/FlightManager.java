// src/main/java/cat/uvic/teknos/dam/aeroadmin/console/FlightManager.java
package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.repositories.FlightRepository;
import cat.uvic.teknos.dam.aeroadmin.model.model.ModelFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.util.Scanner;

public class FlightManager {
    private final FlightRepository repo;
    private final ModelFactory     mf;
    private final Scanner          sc;

    public FlightManager(ModelFactory mf, RepositoryFactory rf, Scanner sc) {
        this.mf   = mf;
        this.sc   = sc;
        this.repo = rf.getFlightRepository();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor de Vols ---");
            System.out.println("1. Llistar vols");
            System.out.println("2. Crear vol");
            System.out.println("3. Actualitzar vol");
            System.out.println("4. Eliminar vol");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");
            String c = sc.nextLine().trim();

            switch (c) {
                case "1":
                    repo.getAll()
                            .forEach(f -> System.out.printf(
                                    "ID: %d | Num: %s%n",
                                    f.getFlightId(), f.getFlightNumber()));
                    break;
                case "2":
                    Flight nf = mf.createFlight();
                    System.out.print("Num de vol: "); nf.setFlightNumber(sc.nextLine());
                    System.out.print("Sortida: ");     nf.setDepartureAirport(sc.nextLine());
                    System.out.print("Arribada: ");     nf.setArrivalAirport(sc.nextLine());
                    repo.save(nf);
                    System.out.println("Vol creat.");
                    break;
                case "3":
                    System.out.print("ID a actualitzar: ");
                    int iu = Integer.parseInt(sc.nextLine());
                    Flight fu = repo.get(iu);
                    if (fu!=null) {
                        System.out.print("Num actual ("+fu.getFlightNumber()+"): ");
                        fu.setFlightNumber(sc.nextLine());
                        repo.save(fu);
                        System.out.println("Vol actualitzat.");
                    } else {
                        System.out.println("No trobat.");
                    }
                    break;
                case "4":
                    System.out.print("ID a eliminar: ");
                    int idd = Integer.parseInt(sc.nextLine());
                    Flight fd = repo.get(idd);
                    if (fd!=null) {
                        repo.delete(fd);
                        System.out.println("Vol eliminat.");
                    } else {
                        System.out.println("No trobat.");
                    }
                    break;
                case "0": exit = true; break;
                default:  System.out.println("Opció invàlida.");
            }
        }
    }
}
