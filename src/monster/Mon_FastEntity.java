package monster;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import entity.Entity;
import main.GamePanel;
import entity.Player;

public class Mon_FastEntity extends Entity
{
    GamePanel gp;
    Player player;

    public int x, y;
    public float speed;
    public String direction;
    public int life, MaxLife;
    public boolean isAlive = true;

    public void getFastEntityImage()
    {
        try
        {
            fastleft1 = ImageIO.read(getClass().getResourceAsStream("/monster2/efleft1.png"));
            fastleft2 = ImageIO.read(getClass().getResourceAsStream("/monster2/efleft2.png"));
            fastleft3 = ImageIO.read(getClass().getResourceAsStream("/monster2/efleft3.png"));
            fastleft4 = ImageIO.read(getClass().getResourceAsStream("/monster2/efleft4.png"));
            fastright1 = ImageIO.read(getClass().getResourceAsStream("/monster2/efright1.png"));
            fastright2 = ImageIO.read(getClass().getResourceAsStream("/monster2/efright2.png"));
            fastright3 = ImageIO.read(getClass().getResourceAsStream("/monster2/efright3.png"));
            fastright4 = ImageIO.read(getClass().getResourceAsStream("/monster2/efright4.png"));
            fastup1 = ImageIO.read(getClass().getResourceAsStream("/monster2/efup1.png"));
            fastup2 = ImageIO.read(getClass().getResourceAsStream("/monster2/efup2.png"));
            fastup3 = ImageIO.read(getClass().getResourceAsStream("/monster2/efup3.png"));
            fastup4 = ImageIO.read(getClass().getResourceAsStream("/monster2/efup4.png"));
            fastdown1 = ImageIO.read(getClass().getResourceAsStream("/monster2/efdown1.png"));
            fastdown2 = ImageIO.read(getClass().getResourceAsStream("/monster2/efdown2.png"));
            fastdown3 = ImageIO.read(getClass().getResourceAsStream("/monster2/efdown3.png"));
            fastdown4 = ImageIO.read(getClass().getResourceAsStream("/monster2/efdown4.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Mon_FastEntity (GamePanel gp)
    {
        this.gp = gp;
        this.player = gp.player; // Get the player reference
//        this.x = -100; // Spawn 50 pixels to the left of the visible area
//        this.y = -100; // You can adjust this to spawn vertically as needed
        this.speed = 3.8F; // Speed of the enemy
        MaxLife = 1;
        life = MaxLife;
        direction = "down";

        getFastEntityImage();
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
        for (Mon_FastEntity enemy2 : gp.fastenemies) {
            if (enemy2 != this) { // Don't check against itself
                double separationDistance = 85; // Minimum distance to maintain
                int enemyDeltaX = enemy2.x - x;
                int enemyDeltaY = enemy2.y - y;
                double enemyDistance = Math.sqrt(enemyDeltaX * enemyDeltaX + enemyDeltaY * enemyDeltaY);

                if (enemyDistance < separationDistance) {
                    // Calculate the separation vector
                    double separationX = (x - enemy2.x) / enemyDistance;
                    double separationY = (y - enemy2.y) / enemyDistance;

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
            image = (spriteNum == 1) ? fastup1 : (spriteNum == 2) ? fastup2 : (spriteNum == 3) ? fastup3 : fastup4;
        } else if (direction.equals("down")) {
            image = (spriteNum == 1) ? fastdown1 : (spriteNum == 2) ? fastdown2 : (spriteNum == 3) ? fastdown3 : fastdown4;
        } else if (direction.equals("left")) {
            image = (spriteNum == 1) ? fastleft1 : (spriteNum == 2) ? fastleft2 : (spriteNum == 3) ? fastleft3 : fastleft4;
        } else if (direction.equals("right")) {
            image = (spriteNum == 1) ? fastright1 : (spriteNum == 2) ? fastright2 : (spriteNum == 3) ? fastright3 : fastright4;
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
