package playermanagement;

import org.junit.jupiter.api.Test;
import Playermanagement.dto.PlayerDTO;
import Playermanagement.util.JsonConverter;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerManagementAppTest {

    @Test
    public void testConvertPlayerToJson() {
        PlayerDTO player = new PlayerDTO(1, "John Doe", 25, 1.80, 75.5, "Forward", "Team A");
        String json = JsonConverter.playerToJson(player);

        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"name\":\"John Doe\""));
        assertTrue(json.contains("\"age\":25"));
        assertTrue(json.startsWith("{") && json.endsWith("}"));
    }

    @Test
    public void testConvertPlayerListToJson() {
        List<PlayerDTO> players = Arrays.asList(
                new PlayerDTO(1, "Player 1", 20, 1.75, 70.0, "Mid", "Team X"),
                new PlayerDTO(2, "Player 2", 22, 1.80, 75.0, "Def", "Team Y")
        );

        String json = JsonConverter.playersToJson(players);

        assertTrue(json.contains("Player 1"));
        assertTrue(json.contains("Player 2"));
        assertTrue(json.startsWith("[") && json.endsWith("]"));
        assertEquals(2, json.split("\\{").length - 1);
    }

    @Test
    public void testEmptyListToJson() {
        List<PlayerDTO> emptyList = Arrays.asList();
        String json = JsonConverter.playersToJson(emptyList);
        assertEquals("[]", json);
    }
}