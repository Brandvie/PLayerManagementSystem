DROP DATABASE IF EXISTS player_db;
CREATE DATABASE player_db;
USE player_db;

CREATE TABLE players (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT,
    height DOUBLE,
    weight DOUBLE,
    position VARCHAR(50),
    team VARCHAR(100)
);

INSERT INTO players (id, name, age, height, weight, position, team) VALUES
(1, 'Lionel Messi', 36, 1.70, 72.0, 'Forward', 'Inter Miami'),
(2, 'Cristiano Ronaldo', 38, 1.87, 85.0, 'Forward', 'Al Nassr'),
(3, 'Neymar Jr', 31, 1.75, 68.0, 'Forward', 'Al Hilal'),
(4, 'Kylian Mbappé', 24, 1.78, 73.0, 'Forward', 'Paris Saint-Germain'),
(5, 'Kevin De Bruyne', 32, 1.81, 70.0, 'Midfielder', 'Manchester City'),
(6, 'Virgil van Dijk', 32, 1.93, 92.0, 'Defender', 'Liverpool'),
(7, 'Robert Lewandowski', 35, 1.85, 81.0, 'Forward', 'Barcelona'),
(8, 'Erling Haaland', 23, 1.94, 88.0, 'Forward', 'Manchester City'),
(9, 'Luka Modrić', 38, 1.72, 66.0, 'Midfielder', 'Real Madrid'),
(10, 'Thibaut Courtois', 31, 2.00, 96.0, 'Goalkeeper', 'Real Madrid');
