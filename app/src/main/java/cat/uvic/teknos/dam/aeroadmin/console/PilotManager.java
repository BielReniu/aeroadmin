package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class PilotManager {
    private final PilotRepository pilotRepository;
    private final AirlineRepository airlineRepository;
    private final Scanner sc;

    public PilotManager(RepositoryFactory rf, Scanner sc) {
        this.sc = sc;
        this.pilotRepository = rf.getPilotRepository();
        this.airlineRepository = rf.getAirlineRepository();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor de Pilots ---");
            System.out.println("1. Llistar pilots");
            System.out.println("2. Crear pilot");
            System.out.println("3. Actualitzar pilot");
            System.out.println("4. Eliminar pilot");
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

    private void printPilot(Pilot p) {
        String airlineName = (p.getAirline() != null) ? p.getAirline().getAirlineName() : "N/A";
        System.out.printf("ID: %-3d | Nom: %-25s | Nacionalitat: %-15s | Aerolínia: %s%n",
                p.getPilotId(), p.getFirstName() + " " + p.getLastName(), p.getNationality(), airlineName);
    }

    private void listAll() {
        System.out.println("\n--- Llista de Pilots ---");
        Set<Pilot> pilots = pilotRepository.getAll();
        if (pilots.isEmpty()) {
            System.out.println("No hi ha pilots per mostrar.");
        } else {
            pilots.forEach(this::printPilot);
        }
    }

    private void create() {
        System.out.println("\n--- Nou Pilot ---");
        try {
            Pilot newPilot = pilotRepository.create();

            System.out.print("Nom: ");
            newPilot.setFirstName(sc.nextLine());

            System.out.print("Cognom: ");
            newPilot.setLastName(sc.nextLine());

            System.out.print("Data de naixement (yyyy-MM-dd): ");
            newPilot.setDateOfBirth(LocalDate.parse(sc.nextLine()));

            System.out.print("Nacionalitat: ");
            newPilot.setNationality(sc.nextLine());

            // Llistar i seleccionar Aerolínia
            System.out.println("\n--- Aerolínies Disponibles ---");
            var airlines = new ArrayList<>(airlineRepository.getAll());
            if (airlines.isEmpty()) {
                System.out.println("❌ No hi ha aerolínies registrades. No es pot assignar el pilot.");
                return;
            }
            airlines.forEach(a -> System.out.printf("  ID: %d | Nom: %s%n", a.getAirlineId(), a.getAirlineName()));
            System.out.print("Selecciona l'ID de l'aerolínia: ");
            int airlineId = Integer.parseInt(sc.nextLine());
            Airline selectedAirline = airlineRepository.get(airlineId);
            if (selectedAirline == null) {
                System.out.println("❌ Aerolínia no trobada. Operació cancel·lada.");
                return;
            }
            newPilot.setAirline(selectedAirline);

            pilotRepository.save(newPilot);
            System.out.println("✅ Pilot creat correctament.");

        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID de l'aerolínia ha de ser un número.");
        } catch (DateTimeParseException e) {
            System.err.println("Error: El format de la data no és correcte. Ha de ser 'yyyy-MM-dd'.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }

    private void update() {
        System.out.println("\n--- Actualitzar Pilot ---");
        System.out.print("Introdueix l'ID del pilot a actualitzar: ");
        try {
            int id = Integer.parseInt(sc.nextLine());
            Pilot pilot = pilotRepository.get(id);

            if (pilot != null) {
                System.out.print("Nou nom (anterior: '" + pilot.getFirstName() + "'): ");
                String newFirstName = sc.nextLine();
                if (!newFirstName.isBlank()) {
                    pilot.setFirstName(newFirstName);
                }

                System.out.print("Nou cognom (anterior: '" + pilot.getLastName() + "'): ");
                String newLastName = sc.nextLine();
                if (!newLastName.isBlank()) {
                    pilot.setLastName(newLastName);
                }

                // Aquí podries afegir la resta de camps per actualitzar...

                pilotRepository.save(pilot);
                System.out.println("✅ Pilot actualitzat correctament.");
            } else {
                System.out.println("❌ No s'ha trobat cap pilot amb aquest ID.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }

    private void delete() {
        System.out.println("\n--- Eliminar Pilot ---");
        System.out.print("Introdueix l'ID del pilot a eliminar: ");
        try {
            int id = Integer.parseInt(sc.nextLine());
            Pilot pilot = pilotRepository.get(id);
            if (pilot != null) {
                pilotRepository.delete(pilot);
                System.out.println("✅ Pilot eliminat correctament.");
            } else {
                System.out.println("❌ No s'ha trobat cap pilot amb aquest ID.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }
}