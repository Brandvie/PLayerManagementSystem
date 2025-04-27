package Playermanagement.client;

import Playermanagement.dto.PlayerDTO;
import Playermanagement.util.JsonConverter;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
            System.out.println("3. Get Image List");
            System.out.println("4. Exit");
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
                    getImageList();
                    break;
                case 4:
                    exitClient();
                    return; // Exit the client after notifying the server
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

    private static void getImageList() {
        try {
            // Request to get the image list from the server
            String responseJson = sendRequestToServer("GET_IMAGES_LIST", "");

            // Handle server response: a list of image files
            List<String> imageFiles = parseImageListJson(responseJson);

            if (imageFiles != null && !imageFiles.isEmpty()) {
                System.out.println("Available Images:");
                for (int i = 0; i < imageFiles.size(); i++) {
                    System.out.println((i + 1) + ". " + imageFiles.get(i));
                }

                System.out.print("Enter the number of the image to download: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                if (choice >= 1 && choice <= imageFiles.size()) {
                    String imageName = imageFiles.get(choice - 1);
                    downloadImage(imageName);
                } else {
                    System.out.println("Invalid choice.");
                }
            } else {
                System.out.println("No images available.");
            }
        } catch (Exception e) {
            System.out.println("Error communicating with server: " + e.getMessage());
        }
    }

    private static void downloadImage(String imageName) {
        try {
            // Request the image from the server
            String responseJson = sendRequestToServer("GET_IMAGE", imageName);

            // Save the image locally (simulating binary download)
            try (InputStream in = new ByteArrayInputStream(responseJson.getBytes());
                 FileOutputStream out = new FileOutputStream(imageName)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                System.out.println("Image downloaded successfully: " + imageName);
            }
        } catch (Exception e) {
            System.out.println("Error downloading image: " + e.getMessage());
        }
    }

    private static void exitClient() {
        try {
            // Notify the server that the client is exiting
            sendRequestToServer("EXIT", "");

            System.out.println("Exiting client...");
        } catch (IOException e) {
            System.out.println("Error notifying the server: " + e.getMessage());
        }
    }

    private static String sendRequestToServer(String action, String data) throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // Server IP and port
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Create a request JSON object
        // Send the data as a valid number (not a string enclosed in quotes)
        String requestJson = "{ \"action\": \"" + action + "\", \"data\": \"" + data + "\" }";
        out.println(requestJson); // send request to server

        // Read the server response
        String response = in.readLine();

        socket.close();
        return response;
    }

    private static List<String> parseImageListJson(String jsonResponse) {
        List<String> imageFiles = new ArrayList<>();

        // Remove the JSON array brackets and split the items by commas
        jsonResponse = jsonResponse.trim();

        if (jsonResponse.startsWith("[") && jsonResponse.endsWith("]")) {
            String jsonData = jsonResponse.substring(1, jsonResponse.length() - 1).trim();
            String[] items = jsonData.split(",");

            for (String item : items) {
                item = item.trim().replace("\"", ""); // Clean up the strings and remove quotes
                imageFiles.add(item);
            }
        }
        return imageFiles;
    }
}
