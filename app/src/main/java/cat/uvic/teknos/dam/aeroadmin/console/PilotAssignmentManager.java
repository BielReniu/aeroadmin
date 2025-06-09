// src/main/java/cat/uvic/teknos/dam/aeroadmin/console/PilotAssignmentManager.java
package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.enums.AssignmentRole;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotAssignment;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotAssignmentRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.util.Scanner;

public class PilotAssignmentManager {
    private final PilotAssignmentRepository repo;
    private final Scanner                   sc;

    public PilotAssignmentManager(RepositoryFactory rf, Scanner sc) {
        this.sc   = sc;
        this.repo = rf.getPilotAssignmentRepository();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor Assignacions Pilot ---");
            System.out.println("1. Llistar");
            System.out.println("2. Crear");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");
            String c = sc.nextLine().trim();

            switch (c) {
                case "1":
                    repo.getAll()
                            .forEach(a -> System.out.println(
                                    "AID: "+a.getAssignmentId()+
                                            ", Rol: "+a.getRole()));
                    break;
                case "2":
                    PilotAssignment na = repo.create();
                    System.out.print("Rol: ");
                    na.setRole(AssignmentRole.valueOf(sc.nextLine()));
                    repo.save(na);
                    System.out.println("Assignació creada.");
                    break;
                case "0": exit = true; break;
                default:  System.out.println("Opció invàlida.");
            }
        }
    }
}
