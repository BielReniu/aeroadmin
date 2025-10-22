package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.enums.FlightStatus;
import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.FlightRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class FlightManager {
    private final FlightRepository flightRepository;
    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;
    private final Scanner sc;

    public FlightManager(RepositoryFactory rf, Scanner sc) {
        this.sc = sc;
        this.flightRepository = rf.getFlightRepository();
        this.aircraftRepository = rf.getAircraftRepository();
        this.airlineRepository = rf.getAirlineRepository();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor de Vols ---");
            System.out.println("1. Llistar vols");
            System.out.println("2. Crear vol");
            System.out.println("3. Eliminar vol");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": listAll(); break;
                case "2": create();  break;
                case "3": delete();  break;
                case "0": exit = true; break;
                default:  System.out.println("Opció invàlida.");
            }
        }
    }

    private void printFlight(Flight f) {
        String airlineName = (f.getAirline() != null) ? f.getAirline().getAirlineName() : "N/A";
        String aircraftModel = (f.getAircraft() != null) ? f.getAircraft().getModel() : "N/A";
        System.out.printf("ID: %d | Num: %s | Sortida: %s | Arribada: %s | Aerolínia: %s | Avió: %s%n",
                f.getFlightId(), f.getFlightNumber(), f.getDepartureAirport(), f.getArrivalAirport(), airlineName, aircraftModel);
    }

    private void listAll() {
        System.out.println("\n--- Llista de Vols ---");
        Set<Flight> flights = flightRepository.getAll();
        if (flights.isEmpty()) {
            System.out.println("No hi ha vols per mostrar.");
        } else {
            flights.forEach(this::printFlight);
        }
    }

    private void create() {
        System.out.println("\n--- Nou Vol ---");
        try {
            Flight newFlight = flightRepository.create();

            System.out.print("Número de vol: ");
            newFlight.setFlightNumber(sc.nextLine());

            System.out.print("Aeroport de sortida (codi IATA): ");
            newFlight.setDepartureAirport(sc.nextLine());

            System.out.print("Aeroport d'arribada (codi IATA): ");
            newFlight.setArrivalAirport(sc.nextLine());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            System.out.print("Data i hora de sortida (yyyy-MM-dd HH:mm): ");
            newFlight.setScheduledDeparture(LocalDateTime.parse(sc.nextLine(), formatter));

            System.out.print("Data i hora d'arribada (yyyy-MM-dd HH:mm): ");
            newFlight.setScheduledArrival(LocalDateTime.parse(sc.nextLine(), formatter));

            // Llistar i seleccionar Aerolínia
            System.out.println("\n--- Aerolínies Disponibles ---");
            var airlines = new ArrayList<>(airlineRepository.getAll());
            airlines.forEach(a -> System.out.printf("  ID: %d | Nom: %s%n", a.getAirlineId(), a.getAirlineName()));
            System.out.print("Selecciona l'ID de l'aerolínia: ");
            int airlineId = Integer.parseInt(sc.nextLine());
            Airline selectedAirline = airlineRepository.get(airlineId);
            if (selectedAirline == null) {
                System.out.println("❌ Aerolínia no trobada. Operació cancel·lada.");
                return;
            }
            newFlight.setAirline(selectedAirline);

            // Llistar i seleccionar Avió
            System.out.println("\n--- Avions Disponibles ---");
            var aircrafts = new ArrayList<>(aircraftRepository.getAll());
            aircrafts.forEach(a -> System.out.printf("  ID: %d | Model: %s | Matrícula: %s%n", a.getAircraftId(), a.getModel(), a.getRegistrationNumber()));
            System.out.print("Selecciona l'ID de l'avió: ");
            int aircraftId = Integer.parseInt(sc.nextLine());
            Aircraft selectedAircraft = aircraftRepository.get(aircraftId);
            if (selectedAircraft == null) {
                System.out.println("❌ Avió no trobat. Operació cancel·lada.");
                return;
            }
            newFlight.setAircraft(selectedAircraft);

            newFlight.setStatus(FlightStatus.SCHEDULED); // Per defecte, un vol nou està programat

            flightRepository.save(newFlight);
            System.out.println("✅ Vol creat correctament.");

        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número.");
        } catch (DateTimeParseException e) {
            System.err.println("Error: El format de la data no és correcte. Ha de ser 'yyyy-MM-dd HH:mm'.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }

    private void delete() {
        System.out.println("\n--- Eliminar Vol ---");
        System.out.print("Introdueix l'ID del vol a eliminar: ");
        try {
            int id = Integer.parseInt(sc.nextLine());
            Flight flight = flightRepository.get(id);
            if (flight != null) {
                flightRepository.delete(flight);
                System.out.println("✅ Vol eliminat correctament.");
            } else {
                System.out.println("❌ No s'ha trobat cap vol amb aquest ID.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }
}