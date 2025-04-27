package Playermanagement.dto;



public class PlayerDTO {
    private int id;
    private String name;
    private int age;
    private double height;
    private double weight;
    private String position;
    private String team;

    // Constructors
    public PlayerDTO(int i, String name, int age, float rating) {}

    public PlayerDTO(int id, String name, int age, double height, double weight, String position, String team) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.position = position;
        this.team = team;
    }

    public PlayerDTO() {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.position = position;
        this.team = team;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", position='" + position + '\'' +
                ", team='" + team + '\'' +
                '}';
    }
    public String toJson() {
        return String.format(
                "{\"id\":%d,\"name\":\"%s\",\"age\":%d,\"height\":%.1f,\"weight\":%.1f,\"position\":\"%s\",\"team\":\"%s\"}",
                this.id,
                this.name.replace("\"", "\\\""),
                this.age,
                this.height,
                this.weight,
                this.position.replace("\"", "\\\""),
                this.team.replace("\"", "\\\"")
        );
    }
}