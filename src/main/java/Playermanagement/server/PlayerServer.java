package Playermanagement.server;

import Playermanagement.dao.PlayerDAO;
import Playermanagement.dao.PlayerDAOMySQLImpl;
import Playermanagement.dto.PlayerDTO;
import Playermanagement.util.JsonConverter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

            String command = in.readLine();

            if ("ADD_PLAYER".equals(command)) {
                String playerJson = in.readLine();
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
            } else if ("DELETE_PLAYER".equals(command)) {
                String idJson = in.readLine();
                int playerId = Integer.parseInt(idJson);  // Assuming ID is sent as a String in JSON format

                boolean isDeleted = playerDAO.deletePlayerById(playerId);

                if (isDeleted) {
                    out.println("SUCCESS Player with ID " + playerId + " deleted.");
                } else {
                    out.println("ERROR Could not delete player with ID " + playerId);
                }
            } else {
                out.println("ERROR Unknown command.");
            }
        } catch (IOException e) {
            System.err.println("Client handling error: " + e.getMessage());
        }
    }


}
