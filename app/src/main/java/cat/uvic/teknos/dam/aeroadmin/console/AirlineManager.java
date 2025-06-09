// src/main/java/cat/uvic/teknos/dam/aeroadmin/console/AirlineManager.java
package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;
import cat.uvic.teknos.dam.aeroadmin.model.model.ModelFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.util.Scanner;

public class AirlineManager {
    private final AirlineRepository repo;
    private final ModelFactory      mf;
    private final Scanner           sc;

    public AirlineManager(ModelFactory mf, RepositoryFactory rf, Scanner sc) {
        this.mf   = mf;
        this.sc   = sc;
        this.repo = rf.getAirlineRepository();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor Companyies Aèries ---");
            System.out.println("1. Llistar");
            System.out.println("2. Crear");
            System.out.println("3. Actualitzar");
            System.out.println("4. Eliminar");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");
            String c = sc.nextLine().trim();

            switch (c) {
                case "1":
                    repo.getAll()
                            .forEach(a -> System.out.printf(
                                    "ID: %d | %s | IATA: %s%n",
                                    a.getAirlineId(), a.getName(), a.getIataCode()));
                    break;
                case "2":
                    Airline na = mf.createAirline();
                    System.out.print("Nom: ");     na.setName(sc.nextLine());
                    System.out.print("IATA: ");    na.setIataCode(sc.nextLine());
                    repo.save(na);
                    System.out.println("Creat.");
                    break;
                case "3":
                    System.out.print("ID a actualitzar: ");
                    int iu2 = Integer.parseInt(sc.nextLine());
                    Airline au = repo.get(iu2);
                    if (au!=null) {
                        System.out.print("Nom ("+au.getName()+"): ");
                        au.setName(sc.nextLine());
                        repo.save(au);
                        System.out.println("Actualitzat.");
                    } else {
                        System.out.println("No trobat.");
                    }
                    break;
                case "4":
                    System.out.print("ID a eliminar: ");
                    int id2 = Integer.parseInt(sc.nextLine());
                    Airline ad = repo.get(id2);
                    if (ad!=null) {
                        repo.delete(ad);
                        System.out.println("Eliminat.");
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
