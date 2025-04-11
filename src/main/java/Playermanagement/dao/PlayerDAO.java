package Playermanagement.dao;



import Playermanagement.dto.PlayerDTO;
import Playermanagement.filter.PlayerFilter;

import java.util.List;

public interface PlayerDAO {
    List<PlayerDTO> getAllPlayers();
    PlayerDTO getPlayerById(int id);
    boolean deletePlayerById(int id);
    PlayerDTO insertPlayer(PlayerDTO player);
    boolean updatePlayer(int id, PlayerDTO player);
    List<PlayerDTO> findPlayersApplyFilter(PlayerFilter filter);
}
