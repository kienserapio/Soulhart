package monster;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import entity.Entity;
import main.GamePanel;
import entity.Player;

public class Mon_DarkEntity extends Entity
{
    GamePanel gp;
    Player player;

    public int x, y;
    public float speed;
    public String direction;
    public int life, MaxLife;
    public boolean isAlive = true;


    public Mon_DarkEntity (GamePanel gp)
    {
        this.gp = gp;
        this.player = gp.player; // Get the player reference
//        this.x = -100; // Spawn 50 pixels to the left of the visible area
//        this.y = -100; // You can adjust this to spawn vertically as needed
        this.speed = 2.8F; // Speed of the enemy
        MaxLife = 1;
        life = MaxLife;
        direction = "down";

        getDarkEntityImage();
    }

    public void getDarkEntityImage()
    {
        try
        {
            darkleft1 = ImageIO.read(getClass().getResourceAsStream("/monster/eleft1.png"));
            darkleft2 = ImageIO.read(getClass().getResourceAsStream("/monster/eleft2.png"));
            darkleft3 = ImageIO.read(getClass().getResourceAsStream("/monster/eleft3.png"));
            darkleft4 = ImageIO.read(getClass().getResourceAsStream("/monster/eleft4.png"));
            darkright1 = ImageIO.read(getClass().getResourceAsStream("/monster/eright1.png"));
            darkright2 = ImageIO.read(getClass().getResourceAsStream("/monster/eright2.png"));
            darkright3 = ImageIO.read(getClass().getResourceAsStream("/monster/eright3.png"));
            darkright4 = ImageIO.read(getClass().getResourceAsStream("/monster/eright4.png"));
            darkup1 = ImageIO.read(getClass().getResourceAsStream("/monster/eup1.png"));
            darkup2 = ImageIO.read(getClass().getResourceAsStream("/monster/eup2.png"));
            darkup3 = ImageIO.read(getClass().getResourceAsStream("/monster/eup3.png"));
            darkup4 = ImageIO.read(getClass().getResourceAsStream("/monster/eup4.png"));
            darkdown1 = ImageIO.read(getClass().getResourceAsStream("/monster/edown1.png"));
            darkdown2 = ImageIO.read(getClass().getResourceAsStream("/monster/edown2.png"));
            darkdown3 = ImageIO.read(getClass().getResourceAsStream("/monster/edown3.png"));
            darkdown4 = ImageIO.read(getClass().getResourceAsStream("/monster/edown4.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Rectangle getCollisionArea() {
        return new Rectangle(x + 10, y + 10, gp.tileSize - 30, gp.tileSize - 20); // Assuming the enemy's size is the same as tileSize
    }

    public void update() {
        // Calculate the direction towards the player
        int targetX = player.x;
        int targetY = player.y;

        // Calculate the distance to the player
        int deltaX = targetX - x;
        int deltaY = targetY - y;

        // Normalize the direction vector
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (distance > 0) {
            // Determine the direction based on the player's position
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                // Move horizontally
                direction = (deltaX > 0) ? "right" : "left";
            } else {
                // Move vertically
                direction = (deltaY > 0) ? "down" : "up";
            }

            // Move towards the player
            x += (int) (speed * (deltaX / distance));
            y += (int) (speed * (deltaY / distance));
        }

        // Handle sprite animation
        spriteCounter++;
        if (spriteCounter >= 15) { // Change sprite every 15 frames
            spriteNum++;
            if (spriteNum >= 4) {
                spriteNum = 1; // Reset to the first sprite
            }
            spriteCounter = 0; // Reset the counter
        }

        // Handle separation from other enemies
        handleSeparation();
    }

    private void handleSeparation() {
        for (Mon_DarkEntity enemy : gp.enemies) {
            if (enemy != this) { // Don't check against itself
                double separationDistance = 85; // Minimum distance to maintain
                int enemyDeltaX = enemy.x - x;
                int enemyDeltaY = enemy.y - y;
                double enemyDistance = Math.sqrt(enemyDeltaX * enemyDeltaX + enemyDeltaY * enemyDeltaY);

                if (enemyDistance < separationDistance) {
                    // Calculate the separation vector
                    double separationX = (x - enemy.x) / enemyDistance;
                    double separationY = (y - enemy.y) / enemyDistance;

                    // Move the enemy away from the other enemy
                    x += (int) (separationX * speed);
                    y += (int) (separationY * speed);
                }
            }
        }
    }

    public void draw(Graphics2D g2)
    {
        if (!isAlive) {
            // Optionally, apply a fading effect
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // Fading effect
        }

        BufferedImage image = null;

        //movement of monster
        if (direction.equals("up")) {
            image = (spriteNum == 1) ? darkup1 : (spriteNum == 2) ? darkup2 : (spriteNum == 3) ? darkup3 : darkup4;
        } else if (direction.equals("down")) {
            image = (spriteNum == 1) ? darkdown1 : (spriteNum == 2) ? darkdown2 : (spriteNum == 3) ? darkdown3 : darkdown4;
        } else if (direction.equals("left")) {
            image = (spriteNum == 1) ? darkleft1 : (spriteNum == 2) ? darkleft2 : (spriteNum == 3) ? darkleft3 : darkleft4;
        } else if (direction.equals("right")) {
            image = (spriteNum == 1) ? darkright1 : (spriteNum == 2) ? darkright2 : (spriteNum == 3) ? darkright3 : darkright4;
        }

        if (image != null) {
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        }

        // Draw the monster at its current position
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        int shadowWidth = gp.tileSize / 2;  // Width of shadow
        int shadowHeight = gp.tileSize / 4; // Height of shadow
        int shadowX = x + gp.tileSize / 4 - 2;  // Center shadow beneath sprite
        int shadowY = y + gp.tileSize - shadowHeight + 5; // Slightly below the sprite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2.setColor(Color.BLACK); // Semi-transparent black
        g2.fillOval(shadowX, shadowY, shadowWidth, shadowHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }

}

