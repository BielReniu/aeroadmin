package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class AircraftManager {

    private final AircraftRepository repository;
    private final AirlineRepository airlineRepository;
    private final Scanner scanner;

    public AircraftManager(RepositoryFactory repositoryFactory, Scanner scanner) {
        this.repository = repositoryFactory.getAircraftRepository();
        this.airlineRepository = repositoryFactory.getAirlineRepository();
        this.scanner = scanner;
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor d'Avions ---");
            System.out.println("1. Llistar avions");
            System.out.println("2. Mostrar avió");
            System.out.println("3. Guardar nou avió");
            System.out.println("4. Eliminar avió");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    listAll();
                    break;
                case "2":
                    show();
                    break;
                case "3":
                    save();
                    break;
                case "4":
                    delete();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Opció invàlida.");
            }
        }
    }

    private void printAircraft(Aircraft a) {
        // Mètode d'ajuda per imprimir un avió de manera consistent
        String airlineName = (a.getAirline() != null) ? a.getAirline().getAirlineName() : "N/A";
        System.out.printf("ID: %d | Model: %s | Fabricant: %s | Matrícula: %s | Aerolínia: %s%n",
                a.getAircraftId(), a.getModel(), a.getManufacturer(), a.getRegistrationNumber(), airlineName);
    }

    private void listAll() {
        System.out.println("\n--- Llista d'Avions ---");
        Set<Aircraft> aircrafts = repository.getAll();
        if (aircrafts.isEmpty()) {
            System.out.println("No hi ha avions per mostrar.");
        } else {
            aircrafts.forEach(this::printAircraft);
        }
    }

    private void show() {
        System.out.print("Introdueix l'ID de l'avió a mostrar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Aircraft aircraft = repository.get(id);
            if (aircraft != null) {
                printAircraft(aircraft);
            } else {
                System.out.println("❌ Avió no trobat.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número.");
        }
    }

    private void delete() {
        System.out.print("Introdueix l'ID de l'avió a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Aircraft aircraft = repository.get(id);
            if (aircraft != null) {
                repository.delete(aircraft);
                System.out.println("✅ Avió eliminat correctament.");
            } else {
                System.out.println("❌ Avió no trobat.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número.");
        }
    }

    private void save() {
        try {
            Aircraft newAircraft = repository.create();

            System.out.print("Model: ");
            newAircraft.setModel(scanner.nextLine());

            System.out.print("Fabricant: ");
            newAircraft.setManufacturer(scanner.nextLine());

            System.out.print("Matrícula: ");
            newAircraft.setRegistrationNumber(scanner.nextLine());

            System.out.print("Any de producció: ");
            newAircraft.setProductionYear(Integer.parseInt(scanner.nextLine()));

            // Llistar companyies disponibles
            System.out.println("\n--- Companyies Aèries disponibles ---");
            Set<Airline> airlineSet = airlineRepository.getAll();
            if (airlineSet.isEmpty()) {
                System.out.println("❌ No hi ha aerolínies registrades. No es pot assignar l'avió. Abortant operació.");
                return;
            }
            // Convertim a ArrayList per poder ordenar per ID si volguéssim
            var airlines = new ArrayList<>(airlineSet);
            airlines.forEach(al ->
                    System.out.printf("  ID: %d | Nom: %s | IATA: %s%n",
                            al.getAirlineId(), al.getAirlineName(), al.getIataCode()));

            // Demanar i validar l'ID de la companyia
            System.out.print("Introdueix l'ID de la Companyia Aèria: ");
            int airlineId = Integer.parseInt(scanner.nextLine().trim());

            Airline airline = airlineRepository.get(airlineId);
            if (airline == null) {
                System.out.println("❌ Companyia no trobada. Abortant operació.");
                return;
            }
            newAircraft.setAirline(airline);

            // Guardar l'avió
            repository.save(newAircraft);
            System.out.println("✅ Avió guardat correctament.");

        } catch (NumberFormatException e) {
            System.err.println("Error: L'any o l'ID de l'aerolínia han de ser números. Operació cancel·lada.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }
}