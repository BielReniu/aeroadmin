package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.ModelFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;
import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class AircraftManager {
    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;
    private final Scanner scanner;

    public AircraftManager(ModelFactory modelFactory, RepositoryFactory repositoryFactory, Scanner scanner) {
        this.modelFactory = modelFactory;
        this.repositoryFactory = repositoryFactory;
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("Aircraft manager, type:");
        System.out.println("1 - to list all aircraft");
        System.out.println("2 - to show an aircraft");
        System.out.println("3 - to save a new aircraft");
        System.out.println("Type 'exit' to go back");

        var repository = repositoryFactory.createAircraftRepository();

        String command;
        while (!Objects.equals(command = scanner.nextLine(), "exit")) {
            switch (command) {
                case "1":
                    var aircrafts = repository.getAll();
                    System.out.println(AsciiTable.getTable(aircrafts, Arrays.asList(
                            new Column().with(a -> Integer.toString(a.getAircraftId())),
                            new Column().header("Model").with(Aircraft::getModel),
                            new Column().header("Manufacturer").with(Aircraft::getManufacturer),
                            new Column().header("Registration").with(Aircraft::getRegistrationNumber)
                    )));
                    break;

                case "2":
                    System.out.print("Enter aircraft ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    var aircraft = repository.get(id);
                    if (aircraft != null) {
                        System.out.println(AsciiTable.getTable(Arrays.asList(aircraft), Arrays.asList(
                                new Column().with(a -> Integer.toString(a.getAircraftId())),
                                new Column().header("Model").with(Aircraft::getModel),
                                new Column().header("Manufacturer").with(Aircraft::getManufacturer),
                                new Column().header("Registration").with(Aircraft::getRegistrationNumber),
                                new Column().header("Production Year").with(a -> Integer.toString(a.getProductionYear()))
                        )));
                    } else {
                        System.out.println("Aircraft not found.");
                    }
                    break;

                case "3":
                    System.out.println("Description (model): ");
                    var aircraftToSave = modelFactory.createAircraft();
                    aircraftToSave.setModel(scanner.nextLine());

                    System.out.println("Manufacturer: ");
                    aircraftToSave.setManufacturer(scanner.nextLine());

                    System.out.println("Registration number: ");
                    aircraftToSave.setRegistrationNumber(scanner.nextLine());

                    System.out.println("Production year: ");
                    aircraftToSave.setProductionYear(Integer.parseInt(scanner.nextLine()));

                    // TODO: Load real airline from DB
                    var dummyAirline = new cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl();
                    dummyAirline.setAirlineId(1); // Hardcoded for now
                    aircraftToSave.setAirline(dummyAirline);

                    repository.save(aircraftToSave);
                    System.out.println("Aircraft saved with ID: " + aircraftToSave.getAircraftId());
                    break;

                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
    }
}