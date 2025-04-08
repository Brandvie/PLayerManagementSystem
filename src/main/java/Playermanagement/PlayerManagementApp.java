package Playermanagement;



import Playermanagement.dao.PlayerDAO;
import Playermanagement.dao.PlayerDAOMySQLImpl;
import Playermanagement.dto.PlayerDTO;

import java.util.List;
import java.util.Scanner;

public class PlayerManagementApp {
    private static final PlayerDAO playerDAO = new PlayerDAOMySQLImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\nPlayer Management System");
            System.out.println("1. List all players");
            System.out.println("2. Find player by ID");
            System.out.println("3. Delete player by ID");
            System.out.println("4. Add new player");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        listAllPlayers();
                        break;
                    case 2:
                        findPlayerById();
                        break;
                    case 3:
                        deletePlayerById();
                        break;
                    case 4:
                        addNewPlayer();
                        break;
                    case 5:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // clear bad input
            }
        }

        System.out.println("Exiting Player Management System. Goodbye!");
        scanner.close();
    }


    private static void listAllPlayers() {
        System.out.println("\nAll Players:");
        List<PlayerDTO> players = playerDAO.getAllPlayers();
        for (PlayerDTO player : players) {
            System.out.println(player);
        }
    }

    private static void findPlayerById() {
        System.out.print("\nEnter player ID to find: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        PlayerDTO player = playerDAO.getPlayerById(id);
        if (player != null) {
            System.out.println("Player found: " + player);
        } else {
            System.out.println("Player with ID " + id + " not found.");
        }
    }

    private static void deletePlayerById() {
        System.out.print("\nEnter player ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        boolean deleted = playerDAO.deletePlayerById(id);
        if (deleted) {
            System.out.println("Player with ID " + id + " was deleted successfully.");
        } else {
            System.out.println("Failed to delete player with ID " + id + " (maybe it doesn't exist).");
        }
    }

    private static void addNewPlayer() {
        System.out.println("\nAdd New Player");

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = scanner.nextInt();

        System.out.print("Enter height (in meters): ");
        double height = scanner.nextDouble();

        System.out.print("Enter weight (in kg): ");
        double weight = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        System.out.print("Enter position: ");
        String position = scanner.nextLine();

        System.out.print("Enter team: ");
        String team = scanner.nextLine();

        PlayerDTO newPlayer = new PlayerDTO(0, name, age, height, weight, position, team);
        PlayerDTO insertedPlayer = playerDAO.insertPlayer(newPlayer);

        System.out.println("New player added with ID: " + insertedPlayer.getId());
    }
}
