package Playermanagement.filter;

import Playermanagement.dto.PlayerDTO;

@FunctionalInterface
public interface PlayerFilter {
    boolean test(PlayerDTO player);
}