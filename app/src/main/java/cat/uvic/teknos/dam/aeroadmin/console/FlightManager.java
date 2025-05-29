package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.ModelFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.model.enums.FlightStatus;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class FlightManager {
    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;
    private final Scanner scanner;

    public FlightManager(ModelFactory modelFactory, RepositoryFactory repositoryFactory, Scanner scanner) {
        this.modelFactory = modelFactory;
        this.repositoryFactory = repositoryFactory;
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("Flight manager, type:");
        System.out.println("1 - to list all flights");
        System.out.println("2 - to show a flight");
        System.out.println("3 - to save a new flight");
        System.out.println("Type 'exit' to go back");

        var repository = repositoryFactory.createFlightRepository();

        String command;
        while (!Objects.equals(command = scanner.nextLine(), "exit")) {
            switch (command) {
                case "1":
                    var flights = repository.getAll();
                    System.out.println(AsciiTable.getTable(flights, Arrays.asList(
                            new Column().header("ID").with(f -> Integer.toString(((Flight) f).getFlightId())),
                            new Column().header("Flight Number").with(f -> ((Flight) f).getFlightNumber()),
                            new Column().header("Departure Airport").with(f -> ((Flight) f).getDepartureAirport()),
                            new Column().header("Arrival Airport").with(f -> ((Flight) f).getArrivalAirport()),
                            new Column().header("Status").with(f -> ((Flight) f).getStatus().toString())
                    )));
                    break;

                case "2":
                    System.out.print("Enter flight ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    var flight = repository.get(id);
                    if (flight != null) {
                        System.out.println(AsciiTable.getTable(
                                Arrays.asList(flight),
                                Arrays.asList(
                                        new Column().header("ID").with(f -> Integer.toString(((Flight) f).getFlightId())),
                                        new Column().header("Flight Number").with(f -> ((Flight) f).getFlightNumber()),
                                        new Column().header("Departure Airport").with(f -> ((Flight) f).getDepartureAirport()),
                                        new Column().header("Arrival Airport").with(f -> ((Flight) f).getArrivalAirport()),
                                        new Column().header("Scheduled Departure").with(f -> ((Flight) f).getScheduledDeparture().toString()),
                                        new Column().header("Scheduled Arrival").with(f -> ((Flight) f).getScheduledArrival().toString()),
                                        new Column().header("Status").with(f -> ((Flight) f).getStatus().toString())
                                )
                        ));
                    } else {
                        System.out.println("Flight not found.");
                    }
                    break;

                case "3":
                    var flightToSave = modelFactory.createFlight();

                    System.out.println("Flight number: ");
                    flightToSave.setFlightNumber(scanner.nextLine());

                    System.out.println("Departure airport: ");
                    flightToSave.setDepartureAirport(scanner.nextLine());

                    System.out.println("Arrival airport: ");
                    flightToSave.setArrivalAirport(scanner.nextLine());

                    System.out.println("Scheduled departure (YYYY-MM-DDTHH:mm): ");
                    flightToSave.setScheduledDeparture(LocalDateTime.parse(scanner.nextLine()));

                    System.out.println("Scheduled arrival (YYYY-MM-DDTHH:mm): ");
                    flightToSave.setScheduledArrival(LocalDateTime.parse(scanner.nextLine()));

                    System.out.println("Status [SCHEDULED, DELAYED, IN_PROGRESS, COMPLETED, CANCELLED]: ");
                    flightToSave.setStatus(FlightStatus.valueOf(scanner.nextLine().toUpperCase()));

                    // TODO: Load real airline and aircraft from DB
                    var dummyAirline = new cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl();
                    dummyAirline.setAirlineId(1);
                    flightToSave.setAirline(dummyAirline);

                    var dummyAircraft = new cat.uvic.teknos.dam.aeroadmin.model.impl.AircraftImpl();
                    dummyAircraft.setAircraftId(1);
                    flightToSave.setAircraft(dummyAircraft);

                    repository.save(flightToSave);
                    System.out.println("Flight saved with ID: " + flightToSave.getFlightId());
                    break;

                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
    }
}
