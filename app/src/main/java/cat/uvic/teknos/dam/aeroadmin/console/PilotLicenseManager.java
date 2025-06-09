// src/main/java/cat/uvic/teknos/dam/aeroadmin/console/PilotLicenseManager.java
package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.enums.LicenseType;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotLicense;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotLicenseRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.util.Scanner;

public class PilotLicenseManager {
    private final PilotLicenseRepository repo;
    private final Scanner               sc;

    public PilotLicenseManager(RepositoryFactory rf, Scanner sc) {
        this.sc   = sc;
        this.repo = rf.getPilotLicenseRepository();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor Llicències Pilot ---");
            System.out.println("1. Llistar");
            System.out.println("2. Crear");
            System.out.println("3. Actualitzar");
            System.out.println("4. Eliminar");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");
            String c = sc.nextLine().trim();

            switch (c) {
                case "1":
                    repo.getAll()
                            .forEach(l -> System.out.println(
                                    "PID: "+l.getPilotId()+
                                            ", Tipus: "+l.getLicenseType()));
                    break;
                case "2":
                    PilotLicense nl = repo.create();
                    System.out.print("Tipus (ATPL/CPL): ");
                    nl.setLicenseType(LicenseType.valueOf(sc.nextLine()));
                    repo.save(nl);
                    System.out.println("Llicència creada.");
                    break;
                case "3":
                    System.out.print("ID a actualizar: ");
                    int iu = Integer.parseInt(sc.nextLine());
                    PilotLicense pu = repo.get(iu);
                    if (pu!=null) {
                        System.out.print("Tipus ("+pu.getLicenseType()+"): ");
                        pu.setLicenseType(LicenseType.valueOf(sc.nextLine()));
                        repo.save(pu);
                        System.out.println("Actualitzat.");
                    } else {
                        System.out.println("No trobat.");
                    }
                    break;
                case "4":
                    System.out.print("ID a eliminar: ");
                    int idd = Integer.parseInt(sc.nextLine());
                    PilotLicense pd = repo.get(idd);
                    if (pd!=null) {
                        repo.delete(pd);
                        System.out.println("Eliminat.");
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
