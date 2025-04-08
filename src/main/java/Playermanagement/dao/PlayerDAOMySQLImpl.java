package Playermanagement.dao;



import Playermanagement.dto.PlayerDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAOMySQLImpl implements PlayerDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/player_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Override
    public List<PlayerDTO> getAllPlayers() {
        List<PlayerDTO> players = new ArrayList<>();
        String sql = "SELECT * FROM players";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PlayerDTO player = new PlayerDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getDouble("height"),
                        rs.getDouble("weight"),
                        rs.getString("position"),
                        rs.getString("team"));
                players.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    @Override
    public PlayerDTO getPlayerById(int id) {
        String sql = "SELECT * FROM players WHERE id = ?";
        PlayerDTO player = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    player = new PlayerDTO(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getDouble("height"),
                            rs.getDouble("weight"),
                            rs.getString("position"),
                            rs.getString("team"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return player;
    }

    @Override
    public boolean deletePlayerById(int id) {
        String sql = "DELETE FROM players WHERE id = ?";
        boolean rowDeleted = false;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            rowDeleted = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDeleted;
    }

    @Override
    public PlayerDTO insertPlayer(PlayerDTO player) {
        String sql = "INSERT INTO players (name, age, height, weight, position, team) VALUES (?, ?, ?, ?, ?, ?)";
        ResultSet generatedKeys = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, player.getName());
            pstmt.setInt(2, player.getAge());
            pstmt.setDouble(3, player.getHeight());
            pstmt.setDouble(4, player.getWeight());
            pstmt.setString(5, player.getPosition());
            pstmt.setString(6, player.getTeam());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating player failed, no rows affected.");
            }

            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                player.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating player failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return player;
    }
}