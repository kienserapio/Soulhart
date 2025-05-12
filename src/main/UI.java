package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    KeyHandler keyh;
    LevelManager levelManager;
    Font pixelmania, pixelmania_50, pixelmix, pixelmix_50, subtext;
    public int commandNum = 0;
    public int upgradeNum = 0;
    public int pauseNum = 0;
    public int cutsceneIndex = 0;
    private String[] cutsceneTexts = {
            "YOU'RE IN THE AFTERLIFE NOW...",
            "COLLECT SOULS TO BRING YOURSELF BACK...",
            "HOWEVER...",
            "EMBRACE YOURSELF WITH WAVES OF SOULEATERS!"};
    private String displayedText = ""; // Incrementally revealed text
    private int charIndex = 0; // Tracks which character to add next
    private long lastUpdateTime = 0; // Time tracking for revealing characters
    private long revealDelay = 100; // Delay in milliseconds between each letter
    public boolean isFullyDisplayed = false; // Track when text is fully displayed
    public int offsetY; // Start off-screen at the top
    public boolean slidingDown = true;
    private int animationFrame = 0; // Tracks the current animation frame
    private long lastFrameTime = 0; // Tracks the time of the last frame update
    private long frameDelay = 100; // Delay in milliseconds between frames


    private BufferedImage dash, speed, soul, title;
//    private BufferedImage an1, an2, an3, an4, an5, an6, an7, an8, an9;
    private BufferedImage buttons, start, tutorial, credits, quit, lives;
    private BufferedImage upgrades1, upgrades2, upgrades3;
    private BufferedImage pause, pause1, pause2;
    private BufferedImage credit, controls;
    private BufferedImage gameover, pressE;

    public UI(GamePanel gp, KeyHandler keyh, LevelManager levelManager) {
        this.levelManager = levelManager; // Use the shared instance
        this.gp = gp;
        this.keyh = keyh;

        this.offsetY = -gp.screenHeight;

        try {
            InputStream is = getClass().getResourceAsStream("/font/Pixelmania.ttf");
            pixelmania = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/pixelmix.ttf");
            pixelmix = Font.createFont(Font.TRUETYPE_FONT, is);
            dash = ImageIO.read(getClass().getResourceAsStream("/object/dash.png"));
            speed = ImageIO.read(getClass().getResourceAsStream("/object/speed.png"));
            title = ImageIO.read(getClass().getResourceAsStream("/object/title.png"));
//            an1 = ImageIO.read(getClass().getResourceAsStream("/object/player-1.png.png"));
//            an2 = ImageIO.read(getClass().getResourceAsStream("/object/player-2.png.png"));
//            an3 = ImageIO.read(getClass().getResourceAsStream("/object/player-3.png.png"));
//            an4 = ImageIO.read(getClass().getResourceAsStream("/object/player-4.png.png"));
//            an5 = ImageIO.read(getClass().getResourceAsStream("/object/player-5.png.png"));
//            an6 = ImageIO.read(getClass().getResourceAsStream("/object/player-6.png.png"));
//            an7 = ImageIO.read(getClass().getResourceAsStream("/object/player-7.png.png"));
//            an8 = ImageIO.read(getClass().getResourceAsStream("/object/player-8.png.png"));
//            an9 = ImageIO.read(getClass().getResourceAsStream("/object/player-9.png.png"));
            buttons = ImageIO.read(getClass().getResourceAsStream("/object/buttons.png"));
            start = ImageIO.read(getClass().getResourceAsStream("/object/start.png"));
            tutorial = ImageIO.read(getClass().getResourceAsStream("/object/tutorial.png"));
            credits = ImageIO.read(getClass().getResourceAsStream("/object/credits.png"));
            quit = ImageIO.read(getClass().getResourceAsStream("/object/quit.png"));
            lives = ImageIO.read(getClass().getResourceAsStream("/object/lives.png"));
            upgrades1 = ImageIO.read(getClass().getResourceAsStream("/object/upgrades1.png"));
            upgrades2 = ImageIO.read(getClass().getResourceAsStream("/object/upgrades2.png"));
            upgrades3 = ImageIO.read(getClass().getResourceAsStream("/object/upgrades3.png"));
            soul = ImageIO.read(getClass().getResourceAsStream("/object/soul.png"));
            pause1 = ImageIO.read(getClass().getResourceAsStream("/object/pause1.png"));
            pause2 = ImageIO.read(getClass().getResourceAsStream("/object/pause2.png"));
            pause = ImageIO.read(getClass().getResourceAsStream("/object/pause.png"));
            credit = ImageIO.read(getClass().getResourceAsStream("/object/credit.png"));
            controls = ImageIO.read(getClass().getResourceAsStream("/object/controls.png"));
            gameover = ImageIO.read(getClass().getResourceAsStream("/object/gameover.png"));
            pressE = ImageIO.read(getClass().getResourceAsStream("/object/pressE.png"));


//            background = ImageIO.read(getClass().getResourceAsStream("/object/blackground.png"));

        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pixelmix_50 = pixelmix.deriveFont(30f);
        pixelmania_50 = pixelmania.deriveFont(75f);
        subtext = pixelmix.deriveFont(15f);
    }
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen(g2);
        }
        if (gp.gameState == gp.titleState) {
            drawTitleScreen(g2);
        }
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawLevel();
            drawSoul(g2);

        }
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen(g2);
        }
        if (gp.gameState == gp.upgradeState) {
            drawUpgradeScreen(g2);
        }
        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen(g2);
        }
        if (gp.gameState == gp.tutorialState) {
            drawTutorialScreen(g2);
        }
        if (gp.gameState == gp.gameoverState) {
            drawGameOver(g2);
        }
        if (gp.gameState == gp.pressState) {
            drawPressScreen(g2);
        }
    }

    public void drawPlayerLife() {
//        int x = 50;
//        int y = 25;
        int i = 0;

        long currentTime = System.currentTimeMillis();
        double hoverSpeed = 0.005;
        int hoverRange = 5;

        int hoverOffset = (int) (Math.sin(currentTime * hoverSpeed) * hoverRange);
        int imagex = 50;
        int imagey = 25 + hoverOffset;

        while (i < gp.player.life) {
            g2.drawImage(lives, imagex, imagey, gp.tileSize, gp.tileSize, null);
            i++;
            imagex += gp.tileSize;
        }
    }

    public void drawLevel() {
        String levelText = "" + (levelManager.currentLevelIndex + 1); // Level starts at 1 for display

        g2.setFont(pixelmix_50);
        g2.setColor(Color.DARK_GRAY);
        int textLength = (int) g2.getFontMetrics().getStringBounds(levelText, g2).getWidth();
        int x = gp.screenWidth / 2 - textLength / 2;

        g2.setColor(Color.DARK_GRAY);
        g2.fillRoundRect(603, 40, 70, 50, 30, 35);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(603, 33, 70, 50, 35, 35);
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(603, 33, 70, 50, 25, 25);

        g2.setColor(Color.decode("#285f7c")); // Use the custom hex color here
        g2.drawString(levelText, x, 70); // Draw the string at position (20, 40)    }
    }

    public void drawSoul (Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(pixelmix_50);
        g2.setColor(Color.DARK_GRAY);

        String soulText = "" + (gp.soul);

        //SOULS
        g2.drawImage(soul, 50, 135, gp.tileSize, gp.tileSize, null);
        g2.drawString(soulText, 150, 190);

    }

    public void drawTitleScreen(Graphics2D g2) {
        String text;
        int x, y, textLength;

        long currentTime = System.currentTimeMillis();
        double hoverSpeed = 0.005;
        int hoverRange = 10;

        int hoverOffset = (int) (Math.sin(currentTime * hoverSpeed) * hoverRange);

        // LOGO
        int imagex = gp.screenWidth / 2 - 1280 / 2 - 5;
        int baseImageY = gp.screenHeight / 2 - 260; // Base position
        int imagey = baseImageY + hoverOffset; // Apply hover offset
        g2.drawImage(title, imagex, imagey, 1280, 128, null);

        // BUTTONS
        x = gp.screenWidth / 2 - 1280 / 2;
        y = gp.screenHeight / 2 - 500;
        g2.drawImage(buttons, x, y, 1280, 960, null);
        if (commandNum == 0) {
            g2.drawImage(start, x, y, 1280, 960, null);
        }
        if (commandNum == 1) {
            y = y - 1;
            g2.drawImage(tutorial, x, y, 1280, 960, null);
        }
        if (commandNum == 2) {
            y = y - 1;
            g2.drawImage(credits, x, y, 1280, 960, null);
        }
        if (commandNum == 3) {
            y = y - 1;
            g2.drawImage(quit, x, y, 1280, 960, null);
        }
    }

    public void drawPauseScreen(Graphics2D g2) {
        this.g2 = g2;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(0, 0, gp.screenWidth, gp.screenHeight, 0, 0);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));


        int x = gp.screenWidth/2 - 1280/2;
        int y = 0;
        g2.drawImage(pause, x, y, gp.screenWidth, gp.screenHeight, null);
        if (pauseNum == 0) {
            g2.drawImage(pause1, x, y, gp.screenWidth, gp.screenHeight, null);
        }
        if (pauseNum == 1) {
            g2.drawImage(pause2, x, y, gp.screenWidth, gp.screenHeight, null);
        }
    }

    public void drawUpgradeScreen(Graphics2D g2) {
        this.g2 = g2;

        // Animate sliding
        if (slidingDown && offsetY < 0) {
            offsetY += 20; // Adjust the speed of sliding
            if (offsetY >= 0) {
                offsetY = 0; // Stop at the final position
                slidingDown = false; // Stop the animation
            }
        }
        // Apply offset to the entire screen
        g2.translate(0, offsetY);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(0, 0, gp.screenWidth, gp.screenHeight, 0, 0);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        int x = gp.screenWidth / 2 - 1280 / 2;
        int y = 0;
        g2.drawImage(upgrades1, x, y, 1280, 960, null);

        if (upgradeNum == 0) {
            g2.drawImage(upgrades1, x, y, 1280, 960, null);
        }
        if (upgradeNum == 1) {
            g2.drawImage(upgrades2, x, y, 1280, 960, null);
        }
        if (upgradeNum == 2) {
            g2.drawImage(upgrades3, x, y, 1280, 960, null);
        }

        g2.setFont(pixelmix_50);
        g2.setColor(Color.WHITE);

        String soulText = "" + (gp.soul);

        //SOULS
        g2.drawImage(soul, 50, 135, gp.tileSize, gp.tileSize, null);
        g2.drawString(soulText, 150, 190);
        g2.translate(0, -offsetY);
    }


    public void drawDialogueScreen(Graphics2D g2) {
        this.g2 = g2;

        long currentTime = System.currentTimeMillis();
        double hoverSpeed = 0.005;
        int hoverRange = 10;

        int hoverOffset = (int) (Math.sin(currentTime * hoverSpeed) * hoverRange);

        g2.setFont(pixelmix_50);

        String currentText = cutsceneTexts[cutsceneIndex];

        int textLength = (int) g2.getFontMetrics().getStringBounds(currentText, g2).getWidth();
        int x = gp.screenWidth / 2 - textLength / 2;
        int y = gp.screenHeight / 2 - 50;
        int imagey = y + hoverOffset; // Apply hover offset


        // Update displayedText based on time
        if (!isFullyDisplayed && System.currentTimeMillis() - lastUpdateTime > revealDelay) {
            if (charIndex < currentText.length()) {
                displayedText += currentText.charAt(charIndex);
                charIndex++;
                lastUpdateTime = System.currentTimeMillis();
            } else {
                isFullyDisplayed = true; // Mark as fully displayed
                lastUpdateTime = System.currentTimeMillis(); // Start the delay for transitioning
            }
        }

        g2.drawString(displayedText, x, imagey);

        // Handle automatic transition to the next text
        handleCutsceneLogic();
    }

    public void drawCreditsScreen(Graphics2D g2) {
        this.g2 = g2;
        int x = gp.screenWidth/2 - 1280/2;
        int y = 0;
        g2.drawImage(credit, x, y, gp.screenWidth, gp.screenHeight, null);
    }

    public void drawTutorialScreen(Graphics2D g2) {
        this.g2 = g2;
        int x = gp.screenWidth/2 - 1280/2;
        int y = 0;
        g2.drawImage(controls, x, y, gp.screenWidth, gp.screenHeight, null);
    }

    public void drawGameOver(Graphics2D g2) {
        this.g2 = g2;
        int x = gp.screenWidth/2 - 1280/2;
        int y = 0;
        if (slidingDown && offsetY < 0) {
            offsetY += 20; // Adjust the speed of sliding
            if (offsetY >= 0) {
                offsetY = 0; // Stop at the final position
                slidingDown = false; // Stop the animation
            }
        }
        // Apply offset to the entire screen
        g2.translate(0, offsetY);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(0, 0, gp.screenWidth, gp.screenHeight, 0, 0);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2.drawImage(gameover, x, y, gp.screenWidth, gp.screenHeight, null);
    }

    public void drawPressScreen(Graphics2D g2) {
        this.g2 = g2;
        int x = 0;
        int y = 0;

        long currentTime = System.currentTimeMillis();
        double hoverSpeed = 0.005;
        int hoverRange = 10;

        int hoverOffset = (int) (Math.sin(currentTime * hoverSpeed) * hoverRange);

        // LOGO
        int baseImageY = gp.screenHeight / 2 - 600; // Base position
        int imagey = baseImageY + hoverOffset; // Apply hover offset
        g2.drawImage(pressE, x, imagey, gp.screenWidth, gp.screenHeight, null);
    }

    public void handleCutsceneLogic() {
        if (isFullyDisplayed) {
            // Automatically transition after a delay
            if (System.currentTimeMillis() - lastUpdateTime > 1000) { // 1 second delay
                if (cutsceneIndex < cutsceneTexts.length - 1) {
                    cutsceneIndex++;
                    resetDialogue();
                } else {
                    // End the cutscene and transition back to playState
                    gp.gameState = gp.playState;
                    resetDialogue();
                    cutsceneIndex = 0; // Reset for next time
                }
            }
        }
    }

    // Reset method to handle transitions
    void resetDialogue() {
        displayedText = "";
        charIndex = 0;
        isFullyDisplayed = false;
        lastUpdateTime = 0;
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5,width-10,height-10, 25, 25);

    }
}
