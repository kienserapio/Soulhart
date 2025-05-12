    package main;

    import entity.Player;
    import monster.Mon_DarkEntity;
    import monster.Mon_FastEntity;
    import monster.Mon_MadEntity;


    import java.util.ArrayList;
    import java.util.List;
    import java.util.Random;
    import javax.imageio.ImageIO;
    import javax.swing.JPanel;
    import java.awt.*;
    import java.awt.image.BufferedImage;

    public class GamePanel extends JPanel implements Runnable {

        final int originalTileSize = 16;
        final int scale = 5;

        public final int tileSize = originalTileSize * scale;
        final int maxScreenCol = 16;
        final int maxScreenRow = 12;
        public final int screenWidth = tileSize * maxScreenCol;  // 1280
        public final int screenHeight = tileSize * maxScreenRow; // 960
        private List<Point> spawnPoints = new ArrayList<>();
        public List<Mon_FastEntity> fastenemies = new ArrayList<>();
        public List<Mon_DarkEntity> enemies = new ArrayList<>();
        public List<Mon_MadEntity> madenemies = new ArrayList<>();
        private Random random = new Random();
        private long lastSpawnTime;
        private final long spawnInterval = 6000;
        private int currentMusic = -1;

        public int soul;

        private BufferedImage BG;
        private BufferedImage BG2, BGSHADOW;

        int FPS = 60;

        public KeyHandler keyH;
        public Player player;
        public LevelManager levelManager;
        Mon_DarkEntity DEmonster;
        Mon_FastEntity FEmonster;
        Mon_MadEntity MEmonster;
        Sound sound = new Sound();
        Thread gameThread;
        MouseHandler mouseHandler;

        //GAME STATES
        public UI ui = new UI(this, keyH, levelManager);
        public int gameState;
        public final int titleState = 0;
        public final int playState = 1;
        public final int pauseState = 2;
        public final int upgradeState = 3;
        public final int dialogueState = 4;
        public final int creditsState = 5;
        public final int tutorialState = 6;
        public final int gameoverState = 7;
        public final int pressState = 8;

        private int shakeDuration = 0;
        private int shakeMagnitude = 0;
        private int shakeTimer = 0;

        private boolean musicPlaying = false;  // Declare the musicPlaying flag

        public GamePanel() {
            levelManager = new LevelManager(this);

            keyH = new KeyHandler(this, null);  // Create KeyHandler before Player

            // Now initialize MouseHandler, passing KeyHandler (and eventually Player)
            mouseHandler = new MouseHandler(this, null);  // Initialize mouseHandler with a temporary Player reference

            // Initialize Player with keyH and mouseHandler (after both are initialized)
            player = new Player(this, keyH, mouseHandler);

            // Now update the mouseHandler with the actual player object
            mouseHandler.player = player;
            keyH.player = player;
            DEmonster = new Mon_DarkEntity(this);
            FEmonster = new Mon_FastEntity(this);
            MEmonster = new Mon_MadEntity(this);
            this.ui = new UI(this, keyH, levelManager);

            this.addMouseListener(mouseHandler);
            this.addMouseMotionListener(mouseHandler);

            this.setPreferredSize(new Dimension(screenWidth, screenHeight));
            this.setBackground(Color.white);
            this.setDoubleBuffered(true);
            this.addKeyListener(keyH);
            this.setFocusable(true);

            spawnPoints.add(new Point(-100, 100)); // Left side, outside the frame
            spawnPoints.add(new Point(screenWidth + 100, 100)); // Right side, outside the frame
            spawnPoints.add(new Point(400, -100)); // Top side, outside the frame
            spawnPoints.add(new Point(400, screenHeight + 100)); // Bottom side, outside the frame
            lastSpawnTime = System.currentTimeMillis();

            try {
                BG = ImageIO.read(getClass().getResourceAsStream("/player/BG.png"));
                BG2 = ImageIO.read(getClass().getResourceAsStream("/player/BG2.png"));
                BGSHADOW = ImageIO.read(getClass().getResourceAsStream("/player/BGSHADOW.png"));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: Unable to load background image.");
            }
        }

        public void setupGame() {
            gameState = titleState; // Start in the title state (Main Menu)
        }

        public void startGameThread() {
            gameThread = new Thread(this);
            gameThread.start();
        }

        @Override
        public void run() {
            double drawInterval = 1000000000 / FPS;
            double delta = 0;
            long lastTime = System.nanoTime();
            long currentTime;
            long timer = 0;
            int drawCount = 0;

            while (gameThread != null) {
                currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / drawInterval;
                timer += (currentTime - lastTime);
                lastTime = currentTime;

                if (delta >= 1) {
                    update();
                    repaint();
                    delta--;
                    drawCount++;
                }
                if (timer >= 1000000000) {
                    drawCount = 0;
                    timer = 0;
                }
            }
        }

        private boolean cutsceneFinished() {
            // Assuming you have a way to track if the cutscene is finished
            return (gameState != dialogueState); // Change this condition based on your cutscene logic
        }

        public void update() {
            if (gameState == titleState) {
                if (currentMusic != 2) {
                    stopMusic();
                    playMusic(2);
                    currentMusic = 2;
                }
            }

            if (gameState == playState) {
                if (currentMusic != 6) {
                    stopMusic();
                    playMusic(6);
                    currentMusic = 6;
                }
                if (player.life == 0) {
                    playSE(8);
                    triggerCameraShake(shakeDuration, shakeMagnitude);
                    gameState = gameoverState;
                    stopMusic();
                }


                if (cutsceneFinished()) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastSpawnTime >= spawnInterval) {
                        levelManager.spawnEnemiesForCurrentLevel(); // Spawn enemies for current level
                        lastSpawnTime = currentTime; // Update spawn timer
                        System.out.println("Attempting to spawn enemies for level: " + levelManager.getCurrentLevel());
                    }
                }

                for (Mon_DarkEntity enemy : enemies) {
                    if (enemy.isAlive) {
                        // Check for collision with the player
                        if (enemy.getCollisionArea().intersects(player.getCollisionArea())) {
                            player.takeDamage(1);
                            enemy.isAlive = true;
                        }
                        if (player.isAttacking == true) {
                            if (player.attackCollisionArea().intersects(enemy.getCollisionArea())) {
                                playSE(3);
                                enemy.isAlive = false;
                                soul += 5;
                                levelManager.enemyDefeated();
                            }
                        }
                    }
                }
                for (Mon_FastEntity enemy : fastenemies) {
                    if (enemy.isAlive) {
                        // Check for collision with the player
                        if (enemy.getCollisionArea().intersects(player.getCollisionArea())) {
                            player.takeDamage(1);
                            enemy.isAlive = true;
                        }
                        if (player.isAttacking == true) {
                            if (player.attackCollisionArea().intersects(enemy.getCollisionArea())) {
                                playSE(3);
                                enemy.isAlive = false;
                                soul += 10;
                                levelManager.enemyDefeated();
                            }
                        }
                    }
                }
                for (Mon_MadEntity enemy : madenemies) {
                    if (enemy.isAlive) {
                        // Check for collision with the player
                        if (enemy.getCollisionArea().intersects(player.getCollisionArea())) {
                            player.takeDamage(1);
                            enemy.isAlive = true;
                        }
                        if (player.isAttacking == true) {
                            if (player.attackCollisionArea().intersects(enemy.getCollisionArea())) {
                                enemy.takeDamage(1);
                            }
                            if (enemy.life == 0) {
                                playSE(3);
                                enemy.isAlive = false;
                                soul += 20;
                                levelManager.enemyDefeated();
                            }
                        }
                    }
                }

                enemies.removeIf(enemy -> !enemy.isAlive);
                fastenemies.removeIf(enemy -> !enemy.isAlive);
                madenemies.removeIf(enemy -> !enemy.isAlive);

                if (enemies.isEmpty() && fastenemies.isEmpty() && madenemies.isEmpty()) {
                    levelManager.levelUp(); // Move to the next level
                }
            }

            else if (gameState == pauseState || gameState == upgradeState) {
                // Do nothing for enemies when in pauseState or upgradeState
                return;  // Skip the rest of the update
            }

            player.update();

            if (gameState != dialogueState) {
                // Only update enemies if not in pauseState or upgradeState
                if (gameState != pauseState && gameState != upgradeState) {
                    for (Mon_DarkEntity enemy : enemies) {
                        enemy.update();
                    }

                    for (Mon_FastEntity enemy2 : fastenemies) {
                        enemy2.update();
                    }

                    for (Mon_MadEntity enemy3 : madenemies) {
                        enemy3.update();
                    }

                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastSpawnTime >= spawnInterval) {
                        levelManager.spawnEnemiesForCurrentLevel(); // Call the LevelManager to spawn enemies
                        lastSpawnTime = currentTime; // Update the last spawn time
                    }

                }
            }
            if (gameState == pressState) {
                if (player.life == 0) {
                    triggerCameraShake(shakeDuration, shakeMagnitude);
                    playSE(8);
                    gameState = gameoverState;
                    stopMusic();
                }

                if (cutsceneFinished()) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastSpawnTime >= spawnInterval) {
                        levelManager.spawnEnemiesForCurrentLevel(); // Spawn enemies for current level
                        lastSpawnTime = currentTime; // Update spawn timer
                        System.out.println("Attempting to spawn enemies for level: " + levelManager.getCurrentLevel());
                    }
                }

                for (Mon_DarkEntity enemy : enemies) {
                    if (enemy.isAlive) {
                        // Check for collision with the player
                        if (enemy.getCollisionArea().intersects(player.getCollisionArea())) {
                            player.takeDamage(1);
                            enemy.isAlive = true;
                        }
                        if (player.isAttacking == true) {
                            if (player.attackCollisionArea().intersects(enemy.getCollisionArea())) {
                                playSE(3);
                                enemy.isAlive = false;
                                soul += 5;
                                levelManager.enemyDefeated();
                            }
                        }
                    }
                }
                for (Mon_FastEntity enemy : fastenemies) {
                    if (enemy.isAlive) {
                        // Check for collision with the player
                        if (enemy.getCollisionArea().intersects(player.getCollisionArea())) {
                            player.takeDamage(1);
                            enemy.isAlive = true;
                        }
                        if (player.isAttacking == true) {
                            if (player.attackCollisionArea().intersects(enemy.getCollisionArea())) {
                                playSE(3);
                                enemy.isAlive = false;
                                soul += 10;
                                levelManager.enemyDefeated();
                            }
                        }
                    }
                }
                for (Mon_MadEntity enemy : madenemies) {
                    if (enemy.isAlive) {
                        // Check for collision with the player
                        if (enemy.getCollisionArea().intersects(player.getCollisionArea())) {
                            player.takeDamage(1);
                            enemy.isAlive = true;
                        }
                        if (player.isAttacking == true) {
                            if (player.attackCollisionArea().intersects(enemy.getCollisionArea())) {
                                enemy.takeDamage(1);
                            }
                            if (enemy.life == 0) {
                                playSE(3);
                                enemy.isAlive = false;
                                soul += 20;
                                levelManager.enemyDefeated();
                            }
                        }
                    }
                }

                enemies.removeIf(enemy -> !enemy.isAlive);
                fastenemies.removeIf(enemy -> !enemy.isAlive);
                madenemies.removeIf(enemy -> !enemy.isAlive);

                if (enemies.isEmpty() && fastenemies.isEmpty() && madenemies.isEmpty()) {
                    levelManager.levelUp(); // Move to the next level
                }
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int offsetX = 0, offsetY = 0;

            if (shakeTimer > 0) {
                offsetX = (int) (Math.random() * shakeMagnitude * 2 - shakeMagnitude);
                offsetY = (int) (Math.random() * shakeMagnitude * 2 - shakeMagnitude);
                shakeTimer--;
            }

            // Apply the offset when rendering
            g2.translate(offsetX, offsetY);

            if (gameState == titleState) {
                player.invincible = false;
                player.setDefaultValues();
                resetEnemies();
                ui.resetDialogue();
                ui.cutsceneIndex = 0;
                ui.draw(g2);
            }
            if (gameState == playState) {
                if (BG != null) {
                    g.drawImage(BG, 0, 0, screenWidth, screenHeight, this);
                }

                player.draw(g2);

                for (Mon_DarkEntity enemy : enemies) {
                    enemy.draw(g2);
                }

                for (Mon_FastEntity enemy2 : fastenemies) {
                    enemy2.draw(g2);
                }

                for (Mon_MadEntity enemy3 : madenemies) {
                    enemy3.draw(g2);
                }

                if (BG2 != null) {
                    g.drawImage(BG2, 0, 0, screenWidth, screenHeight, this);
                }
                if (BGSHADOW != null) {
                    g.drawImage(BGSHADOW, 0, 0, screenWidth, screenHeight, this);
                }
                ui.draw(g2);
            }
            if (gameState == pauseState) {
                // Draw backgrounds and pause screen
                if (BG != null) {
                    g.drawImage(BG, 0, 0, screenWidth, screenHeight, this);
                }
                player.draw(g2);

                for (Mon_DarkEntity enemy : enemies) {
                    enemy.draw(g2);
                }

                for (Mon_FastEntity enemy2 : fastenemies) {
                    enemy2.draw(g2);
                }

                for (Mon_MadEntity enemy3 : madenemies) {
                    enemy3.draw(g2);
                }

                if (BG2 != null) {
                    g.drawImage(BG2, 0, 0, screenWidth, screenHeight, this);
                }
                if (BGSHADOW != null) {
                    g.drawImage(BGSHADOW, 0, 0, screenWidth, screenHeight, this);
                }
                ui.drawPauseScreen(g2);
            }
            if (gameState == upgradeState) {
                // Draw backgrounds and upgrade screen
                if (BG != null) {
                    g.drawImage(BG, 0, 0, screenWidth, screenHeight, this);
                }
                player.draw(g2);

                for (Mon_DarkEntity enemy : enemies) {
                    enemy.draw(g2);
                }

                for (Mon_FastEntity enemy2 : fastenemies) {
                    enemy2.draw(g2);
                }

                for (Mon_MadEntity enemy3 : madenemies) {
                    enemy3.draw(g2);
                }

                if (BG2 != null) {
                    g.drawImage(BG2, 0, 0, screenWidth, screenHeight, this);
                }
                if (BGSHADOW != null) {
                    g.drawImage(BGSHADOW, 0, 0, screenWidth, screenHeight, this);
                }
                ui.drawUpgradeScreen(g2);
            }
            if (gameState == dialogueState) {
                ui.drawDialogueScreen(g2);
            }
            if (gameState == creditsState) {
                ui.drawCreditsScreen(g2);
            }
            if (gameState == tutorialState) {
                ui.drawTutorialScreen(g2);
            }
            if (gameState == gameoverState) {
                if (BG != null) {
                    g.drawImage(BG, 0, 0, screenWidth, screenHeight, this);
                }
                for (Mon_DarkEntity enemy : enemies) {
                    enemy.draw(g2);
                }

                for (Mon_FastEntity enemy2 : fastenemies) {
                    enemy2.draw(g2);
                }

                for (Mon_MadEntity enemy3 : madenemies) {
                    enemy3.draw(g2);
                }
                if (BG2 != null) {
                    g.drawImage(BG2, 0, 0, screenWidth, screenHeight, this);
                }
                if (BGSHADOW != null) {
                    g.drawImage(BGSHADOW, 0, 0, screenWidth, screenHeight, this);
                }
                ui.drawGameOver(g2);
            }
            if (gameState == pressState) {
                if (BG != null) {
                    g.drawImage(BG, 0, 0, screenWidth, screenHeight, this);
                }

                player.draw(g2);

                for (Mon_DarkEntity enemy : enemies) {
                    enemy.draw(g2);
                }

                for (Mon_FastEntity enemy2 : fastenemies) {
                    enemy2.draw(g2);
                }

                for (Mon_MadEntity enemy3 : madenemies) {
                    enemy3.draw(g2);
                }

                if (BG2 != null) {
                    g.drawImage(BG2, 0, 0, screenWidth, screenHeight, this);
                }
                if (BGSHADOW != null) {
                    g.drawImage(BGSHADOW, 0, 0, screenWidth, screenHeight, this);
                }
                ui.drawPressScreen(g2);

                ui.drawPlayerLife();
                ui.drawLevel();
                ui.drawSoul(g2);

                ui.draw(g2);
            }
            g2.translate(-offsetX, -offsetY);

            g2.dispose();
        }

        public void triggerCameraShake(int duration, int magnitude) {
            this.shakeDuration = duration;
            this.shakeMagnitude = magnitude;
            this.shakeTimer = duration;
        }

        public void spawnEnemy() {
            if (enemies.size() < 10) { // Limit the number of enemies
                Point spawnPoint = spawnPoints.get(random.nextInt(spawnPoints.size()));
                boolean overlap = false;

                Mon_DarkEntity tempEnemy = new Mon_DarkEntity(this);
                tempEnemy.x = spawnPoint.x;
                tempEnemy.y = spawnPoint.y;

                // Check for overlap with existing enemies
                for (Mon_DarkEntity enemy : enemies) {
                    if (tempEnemy.getCollisionArea().intersects(enemy.getCollisionArea())) {
                        overlap = true;
                        break;
                    }
                }

                // If there's an overlap, try again
                if (overlap) {
                    spawnEnemy(); // Recursive call to find a new spawn point
                } else {
                    // Create and add the new enemy
                    Mon_DarkEntity newEnemy = new Mon_DarkEntity(this);
                    newEnemy.x = spawnPoint.x;
                    newEnemy.y = spawnPoint.y;
                    enemies.add(newEnemy);
                }
            }
        }

        public void spawnFastEnemy() {
            if (fastenemies.size() < 10) { // Limit the number of enemies
                Point spawnPoint = spawnPoints.get(random.nextInt(spawnPoints.size()));
                boolean overlap = false;

                Mon_FastEntity tempEnemy = new Mon_FastEntity(this);
                tempEnemy.x = spawnPoint.x;
                tempEnemy.y = spawnPoint.y;

                // Check for overlap with existing enemies
                for (Mon_FastEntity enemy2 : fastenemies) {
                    if (tempEnemy.getCollisionArea().intersects(enemy2.getCollisionArea())) {
                        overlap = true;
                        break;
                    }
                }

                // If there's an overlap, try again
                if (overlap) {
                    spawnFastEnemy(); // Recursive call to find a new spawn point
                } else {
                    // Create and add the new enemy
                    Mon_FastEntity newEnemy = new Mon_FastEntity(this);
                    newEnemy.x = spawnPoint.x;
                    newEnemy.y = spawnPoint.y;
                    fastenemies.add(newEnemy);
                }
            }
        }

        public void spawnMadEnemy() {
            if (madenemies.size() < 10) { // Limit the number of enemies
                Point spawnPoint = spawnPoints.get(random.nextInt(spawnPoints.size()));
                boolean overlap = false;

                Mon_MadEntity tempEnemy = new Mon_MadEntity(this);
                tempEnemy.x = spawnPoint.x;
                tempEnemy.y = spawnPoint.y;

                // Check for overlap with existing enemies
                for (Mon_MadEntity enemy3 : madenemies) {
                    if (tempEnemy.getCollisionArea().intersects(enemy3.getCollisionArea())) {
                        overlap = true;
                        break;
                    }
                }

                // If there's an overlap, try again
                if (overlap) {
                    spawnMadEnemy(); // Recursive call to find a new spawn point
                } else {
                    // Create and add the new enemy
                    Mon_MadEntity newEnemy = new Mon_MadEntity(this);
                    newEnemy.x = spawnPoint.x;
                    newEnemy.y = spawnPoint.y;
                    madenemies.add(newEnemy);
                }
            }
        }
        public void resetEnemies() {
            enemies.clear();
            fastenemies.clear();
            madenemies.clear();
        }

        public void playMusic(int i) {
            sound.setFile(i);  // Set the music file (use 2 for main menu music)
            sound.play();
            sound.loop();
            musicPlaying = true;  // Mark music as playing
        }

        public void stopMusic() {
            sound.stop();  // Stop the music
            musicPlaying = false;  // Mark music as stopped
        }

        public void playSE(int i) {
            sound.setFile(3);  // Set sound effects
            sound.play();
        }

        // Add logic to handle state transitions (e.g., when pressing Enter to start the game)
        public void checkUserInput() {
            if (gameState == titleState && keyH.spacePressed) { // Example: Press space to start the game
                gameState = playState;
                stopMusic(); // Stop the menu music
                playMusic(2); // Optionally, play different music for gameplay
                keyH.spacePressed = false; // Reset the key press
            }
        }
    }
