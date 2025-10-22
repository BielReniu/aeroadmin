package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.enums.AssignmentRole;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotAssignment;
import cat.uvic.teknos.dam.aeroadmin.repositories.FlightRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotAssignmentRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

public class PilotAssignmentManager {
    private final PilotAssignmentRepository assignmentRepository;
    private final FlightRepository flightRepository;
    private final PilotRepository pilotRepository;
    private final Scanner sc;

    public PilotAssignmentManager(RepositoryFactory rf, Scanner sc) {
        this.sc = sc;
        this.assignmentRepository = rf.getPilotAssignmentRepository();
        this.flightRepository = rf.getFlightRepository();
        this.pilotRepository = rf.getPilotRepository();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor d'Assignacions de Pilot ---");
            System.out.println("1. Llistar assignacions");
            System.out.println("2. Crear nova assignació");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": listAll(); break;
                case "2": create();  break;
                case "0": exit = true; break;
                default:  System.out.println("Opció invàlida.");
            }
        }
    }

    private void printAssignment(PilotAssignment a) {
        String pilotName = (a.getPilot() != null) ? a.getPilot().getFirstName() + " " + a.getPilot().getLastName() : "N/A";
        String flightNumber = (a.getFlight() != null) ? a.getFlight().getFlightNumber() : "N/A";
        System.out.printf("ID Assignació: %d | Vol: %s | Pilot: %s | Rol: %s%n",
                a.getAssignmentId(), flightNumber, pilotName, a.getRole());
    }

    private void listAll() {
        System.out.println("\n--- Llista d'Assignacions ---");
        Set<PilotAssignment> assignments = assignmentRepository.getAll();
        if (assignments.isEmpty()) {
            System.out.println("No hi ha assignacions per mostrar.");
        } else {
            assignments.forEach(this::printAssignment);
        }
    }

    private void create() {
        System.out.println("\n--- Nova Assignació ---");
        try {
            PilotAssignment newAssignment = assignmentRepository.create();

            // 1. Seleccionar un vol
            System.out.println("\n--- Vols Disponibles ---");
            var flights = new ArrayList<>(flightRepository.getAll());
            flights.forEach(f -> System.out.printf("  ID: %d | Número: %s | Ruta: %s -> %s%n",
                    f.getFlightId(), f.getFlightNumber(), f.getDepartureAirport(), f.getArrivalAirport()));
            System.out.print("Introdueix l'ID del vol: ");
            int flightId = Integer.parseInt(sc.nextLine());
            Flight selectedFlight = flightRepository.get(flightId);
            if (selectedFlight == null) {
                System.out.println("❌ Vol no trobat. Operació cancel·lada.");
                return;
            }
            newAssignment.setFlight(selectedFlight);

            // 2. Seleccionar un pilot
            System.out.println("\n--- Pilots Disponibles ---");
            var pilots = new ArrayList<>(pilotRepository.getAll());
            pilots.forEach(p -> System.out.printf("  ID: %d | Nom: %s %s%n",
                    p.getPilotId(), p.getFirstName(), p.getLastName()));
            System.out.print("Introdueix l'ID del pilot: ");
            int pilotId = Integer.parseInt(sc.nextLine());
            Pilot selectedPilot = pilotRepository.get(pilotId);
            if (selectedPilot == null) {
                System.out.println("❌ Pilot no trobat. Operació cancel·lada.");
                return;
            }
            newAssignment.setPilot(selectedPilot);

            // 3. Seleccionar un rol
            System.out.println("\n--- Rols Disponibles ---");
            Arrays.stream(AssignmentRole.values()).forEach(role -> System.out.println(" - " + role.name()));
            System.out.print("Introdueix el rol (p. ex., CAPTAIN): ");
            newAssignment.setRole(AssignmentRole.valueOf(sc.nextLine().toUpperCase()));

            System.out.print("És el pilot principal (true/false)?: ");
            newAssignment.setLeadPilot(Boolean.parseBoolean(sc.nextLine()));

            System.out.print("Hores assignades (p. ex., 8.5): ");
            newAssignment.setAssignedHours(new BigDecimal(sc.nextLine()));

            assignmentRepository.save(newAssignment);
            System.out.println("✅ Assignació creada correctament.");

        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID o les hores han de ser un número vàlid.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: El rol introduït no és vàlid.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }
}