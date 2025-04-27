package Playermanagement.server;

import Playermanagement.dao.PlayerDAO;
import Playermanagement.dao.PlayerDAOMySQLImpl;
import Playermanagement.dto.PlayerDTO;
import Playermanagement.util.JsonConverter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PlayerServer {
    private static final int PORT = 5000;
    private static PlayerDAO playerDAO = new PlayerDAOMySQLImpl();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Handle each client in a new thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Read the incoming JSON string
            String requestJson = in.readLine();

            if (requestJson != null) {
                // Manually parse the action and data from the JSON string
                String action = extractJsonValue(requestJson, "action");
                String data = extractJsonValue(requestJson, "data");

                // Handle different actions based on the parsed action
                if ("ADD_PLAYER".equals(action)) {
                    String playerJson = data;  // Assuming 'data' is the JSON representation of the player
                    PlayerDTO player = JsonConverter.jsonToPlayer(playerJson);

                    if (player != null) {
                        PlayerDTO insertedPlayer = playerDAO.insertPlayer(player);
                        if (insertedPlayer != null) {
                            out.println("SUCCESS " + JsonConverter.playerToJson(insertedPlayer));
                        } else {
                            out.println("ERROR Could not insert player.");
                        }
                    } else {
                        out.println("ERROR Invalid player data received.");
                    }
                } else if ("DELETE_PLAYER".equals(action)) {
                    try {
                        int playerId = Integer.parseInt(data);  // Parse the player ID as an integer from the string
                        boolean isDeleted = playerDAO.deletePlayerById(playerId);

                        if (isDeleted) {
                            out.println("SUCCESS Player with ID " + playerId + " deleted.");
                        } else {
                            out.println("ERROR Could not delete player with ID " + playerId);
                        }
                    } catch (NumberFormatException e) {
                        out.println("ERROR Invalid player ID format.");
                    }
                } else if ("GET_IMAGES_LIST".equals(action)) {
                    // Handle the request to get images list
                    List<String> imageFiles = getImageFiles();
                    String imagesJson = toJson(imageFiles); // Convert list to JSON format
                    out.println(imagesJson); // Send list of image filenames
                } else if ("EXIT".equals(action)) {
                    // Client is exiting, notify and close the connection
                    out.println("Server: Client disconnected.");
                    System.out.println("Client disconnected.");
                    return;  // Close the connection and end the session
                } else {
                    out.println("ERROR Unknown command.");
                }
            } else {
                out.println("ERROR Invalid request format.");
            }
        } catch (IOException e) {
            System.err.println("Client handling error: " + e.getMessage());
        }
    }

    // Helper method to extract the value for a given key in a simple JSON string
    private static String extractJsonValue(String jsonString, String key) {
        String keyPattern = "\"" + key + "\":\"";  // Key with opening quote and colon
        int startIndex = jsonString.indexOf(keyPattern) + keyPattern.length();
        int endIndex = jsonString.indexOf("\"", startIndex);

        if (startIndex == -1 || endIndex == -1) {
            return null;  // Return null if the key is not found
        }

        return jsonString.substring(startIndex, endIndex);  // Extract and return the value
    }

    // Get a list of image files (dummy implementation)
    private static List<String> getImageFiles() {
        List<String> imageFiles = new ArrayList<>();

        // Hardcoded list of image files for testing purposes
        imageFiles.add("image1.jpg");
        imageFiles.add("image2.jpg");
        imageFiles.add("image3.png");

        return imageFiles;
    }

    // Convert list to JSON format (manual implementation)
    private static String toJson(List<String> imageFiles) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        for (int i = 0; i < imageFiles.size(); i++) {
            jsonBuilder.append("\"").append(imageFiles.get(i)).append("\"");
            if (i < imageFiles.size() - 1) {
                jsonBuilder.append(", ");
            }
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}
