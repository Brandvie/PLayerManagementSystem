package Playermanagement.client;

import Playermanagement.dto.PlayerDTO;
import Playermanagement.util.JsonConverter;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class PlayerClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nClient Menu:");
            System.out.println("1. Add Player");
            System.out.println("2. Delete Player by ID");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addPlayer();
                    break;
                case 2:
                    deletePlayerById();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addPlayer() {
        System.out.print("Enter player name: ");
        String name = scanner.nextLine();

        System.out.print("Enter player age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter player height (in meters): ");
        double height = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        System.out.print("Enter player weight (in kg): ");
        double weight = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        System.out.print("Enter player position: ");
        String position = scanner.nextLine();

        System.out.print("Enter player team: ");
        String team = scanner.nextLine();

        // Create PlayerDTO object
        PlayerDTO player = new PlayerDTO();
        player.setName(name);
        player.setAge(age);
        player.setHeight(height);
        player.setWeight(weight);
        player.setPosition(position);
        player.setTeam(team);

        // Convert to JSON (using your JsonConverter class)
        String jsonRequest = JsonConverter.playerToJson(player);

        try {
            // send jsonRequest to server and receive response (you already have code for communication)
            String responseJson = sendRequestToServer("ADD_PLAYER", jsonRequest);

            // Handle server response
            if (responseJson.contains("error")) {
                System.out.println("Error adding player: " + responseJson);
            } else {
                PlayerDTO addedPlayer = JsonConverter.jsonToPlayer(responseJson);
                System.out.println("Player added successfully!");
                System.out.println("New Player Details:");
                System.out.println(addedPlayer);
            }
        } catch (Exception e) {
            System.out.println("Error communicating with server: " + e.getMessage());
        }
    }

    private static void deletePlayerById() {
        System.out.print("Enter player ID to delete: ");
        int playerId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        try {
            // Send delete request to server
            String responseJson = sendRequestToServer("DELETE_PLAYER", String.valueOf(playerId));

            // Handle server response
            if (responseJson.contains("SUCCESS")) {
                System.out.println("Player deleted successfully.");
            } else {
                System.out.println("Error deleting player: " + responseJson);
            }
        } catch (Exception e) {
            System.out.println("Error communicating with server: " + e.getMessage());
        }
    }

    private static String sendRequestToServer(String action, String data) throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // Server IP and port
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Create a request JSON object
        String requestJson = "{ \"action\": \"" + action + "\", \"data\": \"" + data + "\" }";
        out.println(requestJson); // send request to server

        // Read the server response
        String response = in.readLine();

        socket.close();
        return response;
    }
}

