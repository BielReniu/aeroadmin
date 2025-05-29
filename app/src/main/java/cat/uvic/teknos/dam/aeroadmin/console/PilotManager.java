package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.ModelFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class PilotManager {
    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;
    private final Scanner scanner;

    public PilotManager(ModelFactory modelFactory, RepositoryFactory repositoryFactory, Scanner scanner) {
        this.modelFactory = modelFactory;
        this.repositoryFactory = repositoryFactory;
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("Pilot manager, type:");
        System.out.println("1 - to show the list of all pilots");
        System.out.println("2 - to show a pilot");
        System.out.println("3 - to save a new pilot");
        System.out.println("exit - to quit");

        var repository = repositoryFactory.getPilotRepository();

        String command = "";
        while (!Objects.equals(command = scanner.nextLine(), "exit")) {
            switch (command) {
                case "1":
                    var pilots = repository.getAll();
                    System.out.println(AsciiTable.getTable(pilots, Arrays.asList(
                            new Column().header("ID").with(p -> Integer.toString(((Pilot)p).getPilotId())),
                            new Column().header("First Name").with(p -> ((Pilot)p).getFirstName()),
                            new Column().header("Last Name").with(p -> ((Pilot)p).getLastName()),
                            new Column().header("Nationality").with(p -> ((Pilot)p).getNationality())
                    )));

                    break;

                case "2":
                    System.out.println("Enter pilot ID:");
                    int id = Integer.parseInt(scanner.nextLine());
                    var pilot = repository.get(id);
                    if (pilot != null) {
                        System.out.println(AsciiTable.getTable(Arrays.asList(pilot), Arrays.asList(
                                new Column().header("ID").with(p -> Integer.toString(((Pilot)p).getPilotId())),
                                new Column().header("First Name").with(p -> ((Pilot)p).getFirstName()),
                                new Column().header("Last Name").with(p -> ((Pilot)p).getLastName()),
                                new Column().header("Date of Birth").with(p -> ((Pilot)p).getDateOfBirth().toString()),
                                new Column().header("Nationality").with(p -> ((Pilot)p).getNationality())
                        )));

                    } else {
                        System.out.println("Pilot not found.");
                    }
                    break;

                case "3":
                    var newPilot = modelFactory.createPilot();

                    System.out.println("Enter First Name:");
                    newPilot.setFirstName(scanner.nextLine());

                    System.out.println("Enter Last Name:");
                    newPilot.setLastName(scanner.nextLine());

                    System.out.println("Enter Date of Birth (YYYY-MM-DD):");
                    newPilot.setDateOfBirth(java.time.LocalDate.parse(scanner.nextLine()));

                    System.out.println("Enter Nationality:");
                    newPilot.setNationality(scanner.nextLine());

                    repository.save(newPilot);
                    System.out.println("Pilot saved.");
                    break;

                default:
                    System.out.println("Invalid command");
                    break;
            }

            System.out.println("\nType next command (1, 2, 3 or exit):");
        }
    }
}
