package monster;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import entity.Entity;
import main.GamePanel;
import entity.Player;

public class Mon_MadEntity extends Entity
{
    GamePanel gp;
    Player player;

    public int x, y;
    public float speed;
    public String direction;
    public int life, MaxLife;
    public boolean isAlive = true;

    public void getMadEntityImage()
    {
        try
        {
            madleft1 = ImageIO.read(getClass().getResourceAsStream("/monster3/enleft1.png"));
            madleft2 = ImageIO.read(getClass().getResourceAsStream("/monster3/enleft2.png"));
            madleft3 = ImageIO.read(getClass().getResourceAsStream("/monster3/enleft3.png"));
            madleft4 = ImageIO.read(getClass().getResourceAsStream("/monster3/enleft4.png"));
            madright1 = ImageIO.read(getClass().getResourceAsStream("/monster3/enright1.png"));
            madright2 = ImageIO.read(getClass().getResourceAsStream("/monster3/enright2.png"));
            madright3 = ImageIO.read(getClass().getResourceAsStream("/monster3/enright3.png"));
            madright4 = ImageIO.read(getClass().getResourceAsStream("/monster3/enright4.png"));
            madup1 = ImageIO.read(getClass().getResourceAsStream("/monster3/enup1.png"));
            madup2 = ImageIO.read(getClass().getResourceAsStream("/monster3/enup2.png"));
            madup3 = ImageIO.read(getClass().getResourceAsStream("/monster3/enup3.png"));
            madup4 = ImageIO.read(getClass().getResourceAsStream("/monster3/enup4.png"));
            maddown1 = ImageIO.read(getClass().getResourceAsStream("/monster3/endown1.png"));
            maddown2 = ImageIO.read(getClass().getResourceAsStream("/monster3/endown2.png"));
            maddown3 = ImageIO.read(getClass().getResourceAsStream("/monster3/endown3.png"));
            maddown4 = ImageIO.read(getClass().getResourceAsStream("/monster3/endown4.png"));
            madhit = ImageIO.read(getClass().getResourceAsStream("/monster3/enhit.png"));

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Mon_MadEntity (GamePanel gp)
    {
        this.gp = gp;
        this.player = gp.player; // Get the player reference
//        this.x = -100; // Spawn 50 pixels to the left of the visible area
//        this.y = -100; // You can adjust this to spawn vertically as needed
        this.speed = 3.8F; // Speed of the enemy
        MaxLife = 2;
        life = MaxLife;
        direction = "down";

        getMadEntityImage();
    }

    public void takeDamage(int damage) {
        if (invincible == false) {
            life -= damage; // Decrease life by damage amount
            invincible = true;
        }
        if (life < 0) {
            life = 0; // Ensure life doesn't go below 0

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
        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 10) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // Handle separation from other enemies
        handleSeparation();
    }

    private void handleSeparation() {
        for (Mon_MadEntity enemy3 : gp.madenemies) {
            if (enemy3 != this) { // Don't check against itself
                double separationDistance = 85; // Minimum distance to maintain
                int enemyDeltaX = enemy3.x - x;
                int enemyDeltaY = enemy3.y - y;
                double enemyDistance = Math.sqrt(enemyDeltaX * enemyDeltaX + enemyDeltaY * enemyDeltaY);

                if (enemyDistance < separationDistance) {
                    // Calculate the separation vector
                    double separationX = (x - enemy3.x) / enemyDistance;
                    double separationY = (y - enemy3.y) / enemyDistance;

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
            image = (spriteNum == 1) ? madup1 : (spriteNum == 2) ? madup2 : (spriteNum == 3) ? madup3 : madup4;
        } else if (direction.equals("down")) {
            image = (spriteNum == 1) ? maddown1 : (spriteNum == 2) ? maddown2 : (spriteNum == 3) ? maddown3 : maddown4;
        } else if (direction.equals("left")) {
            image = (spriteNum == 1) ? madleft1 : (spriteNum == 2) ? madleft2 : (spriteNum == 3) ? madleft3 : madleft4;
        } else if (direction.equals("right")) {
            image = (spriteNum == 1) ? madright1 : (spriteNum == 2) ? madright2 : (spriteNum == 3) ? madright3 : madright4;
        }

        if (invincible == true) {
            image = madhit;
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
