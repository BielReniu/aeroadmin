package cat.uvic.teknos.dam.aeroadmin.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static RawHttp rawHttp = new Rawhttp();

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);

        var inputStream = new Scanner(new InputStreamReader(socket.getInputStream()));
        var outputStream = new PrintWriter(socket.getOutputStream());

        System.out.println(inputStream.nextLine());

        var scanner = new Scanner(System.in);
        var request = "";
        while (! (request = scanner.nextLine()).equals("exit")) {

            switch (request) {
                case "1":
                    manageAircraft(socket, scanner);

                    break;
            }
            outputStream.println(request);
            outputStream.flush();

            System.out.println(inputStream.nextLine());
        }

        socket.close();
    }

    private static void manageAircraft(Socket socket, Scanner scanner) {

    }
}
