package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.ModelFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class AirlineManager {
    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;
    private final Scanner scanner;

    public AirlineManager(ModelFactory modelFactory, RepositoryFactory repositoryFactory, Scanner scanner) {
        this.modelFactory = modelFactory;
        this.repositoryFactory = repositoryFactory;
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("Airline manager, type:");
        System.out.println("1 - to list all airlines");
        System.out.println("2 - to show an airline");
        System.out.println("3 - to save a new airline");
        System.out.println("Type 'exit' to go back");

        var repository = repositoryFactory.createAirlineRepository();

        String command;
        while (!Objects.equals(command = scanner.nextLine(), "exit")) {
            switch (command) {
                case "1":
                    var airlines = repository.getAll();
                    System.out.println(AsciiTable.getTable(airlines, Arrays.asList(
                            new Column().with(a -> Integer.toString(a.getAirlineId())),
                            new Column().header("Name").with(Airline::getName),
                            new Column().header("IATA Code").with(Airline::getIataCode),
                            new Column().header("ICAO Code").with(Airline::getIcaoCode)
                    )));
                    break;

                case "2":
                    System.out.print("Enter airline ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    var airline = repository.get(id);
                    if (airline != null) {
                        System.out.println(AsciiTable.getTable(Arrays.asList(airline), Arrays.asList(
                                new Column().with(a -> Integer.toString(a.getAirlineId())),
                                new Column().header("Name").with(Airline::getName),
                                new Column().header("IATA Code").with(Airline::getIataCode),
                                new Column().header("ICAO Code").with(Airline::getIcaoCode),
                                new Column().header("Country").with(Airline::getCountry),
                                new Column().header("Website").with(Airline::getWebsite)
                        )));
                    } else {
                        System.out.println("Airline not found.");
                    }
                    break;

                case "3":
                    System.out.println("Name: ");
                    var airlineToSave = modelFactory.createAirline();
                    airlineToSave.setName(scanner.nextLine());

                    System.out.println("IATA Code: ");
                    airlineToSave.setIataCode(scanner.nextLine());

                    System.out.println("ICAO Code: ");
                    airlineToSave.setIcaoCode(scanner.nextLine());

                    System.out.println("Country: ");
                    airlineToSave.setCountry(scanner.nextLine());

                    System.out.println("Website: ");
                    airlineToSave.setWebsite(scanner.nextLine());

                    repository.save(airlineToSave);
                    System.out.println("Airline saved with ID: " + airlineToSave.getAirlineId());
                    break;

                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
    }
}