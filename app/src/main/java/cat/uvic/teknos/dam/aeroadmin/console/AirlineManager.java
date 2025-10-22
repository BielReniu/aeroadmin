package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.util.Scanner;
import java.util.Set;

public class AirlineManager {
    private final AirlineRepository repo;
    private final Scanner sc;

    public AirlineManager(RepositoryFactory rf, Scanner sc) {
        this.sc = sc;
        this.repo = rf.getAirlineRepository();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor de Companyies Aèries ---");
            System.out.println("1. Llistar");
            System.out.println("2. Crear");
            System.out.println("3. Actualitzar");
            System.out.println("4. Eliminar");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": listAll(); break;
                case "2": create();  break;
                case "3": update();  break;
                case "4": delete();  break;
                case "0": exit = true; break;
                default:  System.out.println("Opció invàlida.");
            }
        }
    }

    private void printAirline(Airline a) {
        System.out.printf("ID: %-3d | Nom: %-30s | IATA: %s%n",
                a.getAirlineId(), a.getAirlineName(), a.getIataCode());
    }

    private void listAll() {
        System.out.println("\n--- Llista de Companyies Aèries ---");
        Set<Airline> airlines = repo.getAll();
        if (airlines.isEmpty()) {
            System.out.println("No hi ha companyies per mostrar.");
        } else {
            airlines.forEach(this::printAirline);
        }
    }

    private void create() {
        System.out.println("\n--- Nova Companyia Aèria ---");
        try {
            Airline newAirline = repo.create();

            System.out.print("Nom: ");
            newAirline.setAirlineName(sc.nextLine());

            System.out.print("Codi IATA: ");
            newAirline.setIataCode(sc.nextLine());

            System.out.print("Codi ICAO: ");
            newAirline.setIcaoCode(sc.nextLine());

            System.out.print("País: ");
            newAirline.setCountry(sc.nextLine());

            System.out.print("Any de fundació (opcional): ");
            String yearInput = sc.nextLine();
            if (!yearInput.isBlank()) {
                newAirline.setFoundationYear(Integer.parseInt(yearInput));
            }

            System.out.print("Pàgina web (opcional): ");
            newAirline.setWebsite(sc.nextLine());

            repo.save(newAirline);
            System.out.println("✅ Companyia creada correctament.");

        } catch (NumberFormatException e) {
            System.err.println("Error: L'any de fundació ha de ser un número.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }

    private void update() {
        System.out.println("\n--- Actualitzar Companyia Aèria ---");
        System.out.print("Introdueix l'ID de la companyia a actualitzar: ");
        try {
            int id = Integer.parseInt(sc.nextLine());
            Airline airline = repo.get(id);

            if (airline != null) {
                System.out.print("Nou nom (anterior: '" + airline.getAirlineName() + "'): ");
                String newName = sc.nextLine();
                if (!newName.isBlank()) {
                    airline.setAirlineName(newName);
                }

                System.out.print("Nou codi IATA (anterior: '" + airline.getIataCode() + "'): ");
                String newIata = sc.nextLine();
                if (!newIata.isBlank()) {
                    airline.setIataCode(newIata);
                }

                // Aquí podries afegir la resta de camps per actualitzar...

                repo.save(airline);
                System.out.println("✅ Companyia actualitzada correctament.");
            } else {
                System.out.println("❌ No s'ha trobat cap companyia amb aquest ID.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }

    private void delete() {
        System.out.println("\n--- Eliminar Companyia Aèria ---");
        System.out.print("Introdueix l'ID de la companyia a eliminar: ");
        try {
            int id = Integer.parseInt(sc.nextLine());
            Airline airline = repo.get(id);
            if (airline != null) {
                repo.delete(airline);
                System.out.println("✅ Companyia eliminada correctament.");
            } else {
                System.out.println("❌ No s'ha trobat cap companyia amb aquest ID.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }
}