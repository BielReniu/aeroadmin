// src/main/java/cat/uvic/teknos/dam/aeroadmin/console/PilotManager.java
package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotRepository;
import cat.uvic.teknos.dam.aeroadmin.model.model.ModelFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.util.Scanner;

public class PilotManager {
    private final PilotRepository repo;
    private final ModelFactory    mf;
    private final Scanner         sc;

    public PilotManager(ModelFactory mf, RepositoryFactory rf, Scanner sc) {
        this.mf   = mf;
        this.sc   = sc;
        this.repo = rf.getPilotRepository();
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
            String c = sc.nextLine().trim();

            switch (c) {
                case "1":
                    repo.getAll()
                            .forEach(p -> System.out.printf(
                                    "ID: %d | %s %s%n",
                                    p.getPilotId(),
                                    p.getFirstName(),
                                    p.getLastName()));
                    break;
                case "2":
                    Pilot np = mf.createPilot();
                    System.out.print("Nom: "); np.setFirstName(sc.nextLine());
                    System.out.print("Cognom: "); np.setLastName(sc.nextLine());
                    repo.save(np);
                    System.out.println("Pilot creat.");
                    break;
                case "3":
                    System.out.print("ID a actualitzar: ");
                    int iu = Integer.parseInt(sc.nextLine());
                    Pilot pu = repo.get(iu);
                    if (pu!=null) {
                        System.out.print("Nom ("+pu.getFirstName()+"): ");
                        pu.setFirstName(sc.nextLine());
                        repo.save(pu);
                        System.out.println("Pilot actualitzat.");
                    } else {
                        System.out.println("No trobat.");
                    }
                    break;
                case "4":
                    System.out.print("ID a eliminar: ");
                    int idel = Integer.parseInt(sc.nextLine());
                    Pilot pd = repo.get(idel);
                    if (pd!=null) {
                        repo.delete(pd);
                        System.out.println("Pilot eliminat.");
                    } else {
                        System.out.println("No trobat.");
                    }
                    break;
                case "0": exit = true; break;
                default:  System.out.println("Opció invàlida.");
            }
        }
    }
}
