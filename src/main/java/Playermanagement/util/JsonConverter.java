package Playermanagement.util;

import Playermanagement.dto.PlayerDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class JsonConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // Feature 7: Convert list of players to JSON
    public static String playersToJson(List<PlayerDTO> players) {
        StringBuilder json = new StringBuilder("[");
        for (PlayerDTO player : players) {
            json.append(playerToJson(player)).append(",");
        }
        if (!players.isEmpty()) {
            json.deleteCharAt(json.length() - 1); // Remove last comma
        }
        json.append("]");
        return json.toString();
    }

    // Feature 8: Convert single player to JSON
    public static String playerToJson(PlayerDTO player) {
        return String.format(
                "{\"id\":%d,\"name\":\"%s\",\"age\":%d,\"height\":%.2f,\"weight\":%.2f,\"position\":\"%s\",\"team\":\"%s\"}",
                player.getId(),
                player.getName(),
                player.getAge(),
                player.getHeight(),
                player.getWeight(),
                player.getPosition(),
                player.getTeam()
        );
    }
    public static PlayerDTO jsonToPlayer(String json) {
        try {
            //found this on stackoverflow.com
            return objectMapper.readValue(json, PlayerDTO.class);
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            return null;
        }
    }
}