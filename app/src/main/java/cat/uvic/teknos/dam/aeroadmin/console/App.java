package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.ModelFactory;
import cat.uvic.teknos.dam.aeroadmin.model.impl.ModelFactoryImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.JdbcRepositoryFactory;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.util.Objects;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Banner.show();
        System.out.println("Welcome to the AeroAdmin back-office! Type:");
        System.out.println("1 - Manage Aircraft");
        System.out.println("2 - Manage Airlines");
        System.out.println("3 - Manage Pilots");
        System.out.println("4 - Manage Flights");
        System.out.println("5 - Exit");

        RepositoryFactory repositoryFactory = new JdbcRepositoryFactory();
        ModelFactory modelFactory = new ModelFactoryImpl();
        Scanner scanner = new Scanner(System.in);

        String command;
        while (!(command = scanner.nextLine()).equalsIgnoreCase("exit")) {
            switch (command) {
                case "1":
                    new AircraftManager(modelFactory, repositoryFactory, scanner).run();
                    break;
                case "2":
                    new AirlineManager(modelFactory, repositoryFactory, scanner).run();
                    break;
                case "3":
                    new PilotManager(modelFactory, repositoryFactory, scanner).run();
                    break;
                case "4":
                    new FlightManager(modelFactory, repositoryFactory, scanner).run();
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        System.out.println("Exiting...");
    }
}