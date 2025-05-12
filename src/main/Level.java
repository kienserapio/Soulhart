package main;

import entity.Entity;
import java.util.HashMap;
import java.util.Map;

public class Level {
    public int levelNumber;
    public Map<Class<? extends Entity>, Integer> enemyTypes; // Maps enemy class to spawn count
    public int spawnInterval; // Time interval for spawning enemies

    public Level(int levelNumber, int spawnInterval) {
        this.levelNumber = levelNumber;
        this.enemyTypes = new HashMap<>();
        this.spawnInterval = spawnInterval;
    }

    public void addEnemyType(Class<? extends Entity> enemyClass, int count) {
        enemyTypes.put(enemyClass, count);
    }

    public int getTotalEnemyCount() {
        int totalEnemies = 0;
        for (int count : enemyTypes.values()) {
            totalEnemies += count; // Sum up the number of enemies for each type
        }
        return totalEnemies;
    }
    public int getLevelNumber() {
        return levelNumber;
    }

    public Map<Class<? extends Entity>, Integer> getEnemyTypes() {
        return enemyTypes;
    }

    public int getSpawnInterval() {
        return spawnInterval;
    }
}