package main;

import entity.Entity;
import monster.Mon_DarkEntity;
import monster.Mon_FastEntity;
import monster.Mon_MadEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LevelManager {
    public List<Level> levels;
    int currentLevelIndex;
    GamePanel gamePanel;
    Sound sound = new Sound();
    private AtomicInteger activeEnemies; // Tracks the number of active enemies
    private boolean isLevelSpawning; // Track if enemies are still spawning
    private boolean levelSpawned; // Track if enemies have already been spawned for the current level
    int enemyCount = 10;

    public LevelManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.levels = new ArrayList<>();
        this.currentLevelIndex = 0; // Start from Level 1 (index 0)
        this.activeEnemies = new AtomicInteger(0); // Initialize counter
        this.isLevelSpawning = false; // Initially, not spawning
        this.levelSpawned = false; // Set the flag to false initially
        initializeLevels();
    }

    private void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }

    public LevelManager(UI ui) {
        this.levels = new ArrayList<>();
    }


    public synchronized void levelUp() {
        if (isLevelSpawning) {
            return; // Don't start level up if enemies are still spawning
        }

        if (enemyCount == 0) { // Ensure level is complete before advancing
            if (currentLevelIndex < levels.size() - 1) { // Ensure we don't exceed the last level
                currentLevelIndex++;
                playSE(10);
                gamePanel.gameState = gamePanel.pressState;
                System.out.println("Starting Level " + getCurrentLevel());
                resetLevel(); // Reset level state before starting the next level
                spawnEnemiesForCurrentLevel();  // Spawn enemies for the next level
            } else {
                System.out.println("Game completed! No more levels.");
            }
        }
    }

    public void initializeLevels() {
        // Example levels with spawn intervals
        Level level1 = new Level(1, 5000); // 5 seconds
        level1.addEnemyType(Mon_DarkEntity.class, enemyCount);
        levels.add(level1);

        Level level2 = new Level(2, 5000); // 5 seconds
        level2.addEnemyType(Mon_FastEntity.class, enemyCount + 10);
        levels.add(level2);

        Level level3 = new Level(3, 6000); // 6 seconds
        level3.addEnemyType(Mon_MadEntity.class, enemyCount + 10);
        levels.add(level3);

        Level level4 = new Level(4, 5000); // 5 seconds
        level4.addEnemyType(Mon_DarkEntity.class, enemyCount + 10);
        levels.add(level4);

        Level level5 = new Level(5, 4000); // 4 seconds
        level5.addEnemyType(Mon_FastEntity.class, enemyCount + 5);
        levels.add(level5);

        Level level6 = new Level(6, 4000); // 4 seconds
        level6.addEnemyType(Mon_MadEntity.class, enemyCount + 5);
        levels.add(level6);

        Level level7 = new Level(7, 3000); // 3 seconds
        level7.addEnemyType(Mon_DarkEntity.class, enemyCount + 10);
        levels.add(level7);

        Level level8 = new Level(8, 3000); // 3 seconds
        level8.addEnemyType(Mon_FastEntity.class, enemyCount + 15);
        levels.add(level8);

        Level level9 = new Level(9, 3000); // 3 seconds
        level9.addEnemyType(Mon_MadEntity.class, enemyCount + 15);
        levels.add(level9);

        Level level10 = new Level(10, 2000); // 2 seconds
        level10.addEnemyType(Mon_MadEntity.class, enemyCount + 20);
        levels.add(level10);
    }

    public int getCurrentLevel() {
        return levels.get(currentLevelIndex).getLevelNumber();
    }

    public void spawnEnemy(Class<? extends Entity> enemyClass) {
        if (enemyClass == Mon_DarkEntity.class) {
            gamePanel.spawnEnemy(); // Call existing method
        } else if (enemyClass == Mon_FastEntity.class) {
            gamePanel.spawnFastEnemy(); // Call existing method
        } else if (enemyClass == Mon_MadEntity.class) {
            gamePanel.spawnMadEnemy(); // Call existing method
        }
        activeEnemies.incrementAndGet();  // Increment active enemy count
    }

    public void enemyDefeated() {
        // Decrease the number of active enemies
        int remainingEnemies = activeEnemies.decrementAndGet();
        System.out.println("Enemy defeated! Remaining active enemies: " + remainingEnemies);

        // Decrease the enemy count (the total number of enemies for the current level)
        enemyCount--;
        System.out.println("Enemies remaining for this level: " + enemyCount);

        // Check if the level is complete
        if (enemyCount == 0) {
            System.out.println("Level " + getCurrentLevel() + " completed!");
            levelUp(); // Advance to the next level
        }
    }

    // This method resets level state before starting the next level
    public void resetLevel() {
        // Reset the spawning flag for the new level
        levelSpawned = false;
    }

    public synchronized void spawnEnemiesForCurrentLevel() {
        if (levelSpawned) {
            return; // Prevent spawning enemies again if already spawned for this level
        }

        if (currentLevelIndex < 0 || currentLevelIndex >= levels.size()) {
            System.err.println("Error: currentLevelIndex is out of bounds. Resetting to level 0.");
            currentLevelIndex = 0; // Reset to the first level or handle as needed
            return;
        }

        Level currentLevel = levels.get(currentLevelIndex);
        System.out.println(currentLevelIndex);
        int spawnInterval = currentLevel.getSpawnInterval();

        System.out.println("Spawning enemies for Level " + getCurrentLevel());
        activeEnemies.set(0); // Reset activeEnemies counter for the new level
        isLevelSpawning = true; // Mark that enemies are spawning

        // Reset the enemy count for the level
        enemyCount = currentLevel.getTotalEnemyCount(); // This should reflect the total number of enemies for the current level

        // Create a new thread to handle sequential enemy spawning
        new Thread(() -> {
            // Iterate through all enemy types and spawn them sequentially
            for (Map.Entry<Class<? extends Entity>, Integer> entry : currentLevel.getEnemyTypes().entrySet()) {
                Class<? extends Entity> enemyClass = entry.getKey();
                int count = entry.getValue();

                for (int i = 0; i < count; i++) {
                    try {
                        Thread.sleep(spawnInterval); // Wait for the spawn interval
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // Handle thread interruption
                    }
                    spawnEnemy(enemyClass); // Spawn the enemy
                }
            }

            isLevelSpawning = false; // Mark spawning complete
            levelSpawned = true; // Set the flag to true to prevent re-spawning
        }).start();
    }
}
