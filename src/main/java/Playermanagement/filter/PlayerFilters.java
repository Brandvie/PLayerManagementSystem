package Playermanagement.filter;


public class PlayerFilters {
    // Filter by minimum age
    public static PlayerFilter ageAtLeast(int minAge) {
        return player -> player.getAge() >= minAge;
    }

    // Filter by position
    public static PlayerFilter positionEquals(String position) {
        return player -> player.getPosition().equalsIgnoreCase(position);
    }

    // Filter by team
    public static PlayerFilter teamEquals(String team) {
        return player -> player.getTeam().equalsIgnoreCase(team);
    }

    // Filter by height range
    public static PlayerFilter heightBetween(double min, double max) {
        return player -> player.getHeight() >= min && player.getHeight() <= max;
    }

    // Combine multiple filters
    public static PlayerFilter and(PlayerFilter... filters) {
        return player -> {
            for (PlayerFilter filter : filters) {
                if (!filter.test(player)) return false;
            }
            return true;
        };
    }
}
