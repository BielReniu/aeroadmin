package org.example.app;

import org.example.list.LinkedList;
import static org.example.utilities.StringUtils.join;
import static org.example.utilities.StringUtils.split;
import static org.example.app.MessageUtils.getMessage;

import org.apache.commons.text.WordUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        // Código de conexión a la base de datos MySQL
        String url = "jdbc:mysql://127.0.0.1:3306";  // Host y puerto de la captura
        String user = "root";  // Usuario de la captura
        String password = "";  // Password no especificado en la captura (se pide después)

        try {
            // Intentamos conectarnos a la base de datos
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("¡Conexión exitosa a la base de datos!");

            // Aquí pots interactuar amb la base de dades, per exemple, realitzar consultes...

            conn.close();  // Tancar la connexió després d'usar-la
        } catch (SQLException e) {
            e.printStackTrace();  // Si hi ha algun error, l'imprimim
        }

        // El codi que ja tenies per processar el missatge
        LinkedList tokens;
        tokens = split(getMessage());
        String result = join(tokens);
        System.out.println(WordUtils.capitalize(result));
    }
}
