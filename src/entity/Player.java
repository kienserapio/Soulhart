package entity;

import main.MouseHandler;
import main.Sound;
import monster.Mon_DarkEntity;
import monster.Mon_FastEntity;
import monster.Mon_MadEntity;

import main.GamePanel;
import main.KeyHandler;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    MouseHandler mouseHandler;
    Sound sound = new Sound();

    public int attackWidth = 70, attackHeight = 70;
    public int x, y;
    public int speed;
    public String direction;

    private boolean isDashing = false;
    private int dashSpeed = 25;
    private int dashDuration = 10;
    public int dashCooldown = 60;
    private int dashTimer = 0;
    private int cooldownTimer = 0;
    private int idleCounter = 0;
    private int dashCounter = 0;

    public boolean isAttacking = false;
    private int attackCounter = 0;
    private int attackDuration = 20; // Adjust as needed for the animation speed
    private int attackDurationCounter = 0; // Counter for attack duration

    private boolean isFlashing = false;
    private int flashCounter = 0;
    private final int FLASH_DURATION = 10; // Duration for 0.5 seconds (assuming 60 FPS)

    private int particleCounter = 0;
    private int particleFrame = 0; // Tracks which particle frame to use
    private final int PARTICLE_ANIMATION_SPEED = 6; // Adjust the speed of particle animation

    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseHandler) {
        this.gp = gp;
        this.keyH = keyH;
        this.mouseHandler = mouseHandler;
        gp.addMouseMotionListener(mouseHandler);
        gp.addMouseListener(mouseHandler);

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 1280 / 2;
        y = 960 / 2;
        speed = 6;
        direction = "down";

        maxLife = 3;
        life = 3;

        gp.soul = 0;
        invincible = false;
        invincibleCounter = 0;
    }

    public void getPlayerImage() {
        try {
            //WALKING
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/up2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/player/up3.png"));
            up4 = ImageIO.read(getClass().getResourceAsStream("/player/up4.png"));
            up5 = ImageIO.read(getClass().getResourceAsStream("/player/up5.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/down2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/player/down3.png"));
            down4 = ImageIO.read(getClass().getResourceAsStream("/player/down4.png"));
            down5 = ImageIO.read(getClass().getResourceAsStream("/player/down5.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/left2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/player/left3.png"));
            left4 = ImageIO.read(getClass().getResourceAsStream("/player/left4.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/right2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/player/right3.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/player/right4.png"));
            //IDLE
            idleup1 = ImageIO.read(getClass().getResourceAsStream("/player/idleup1.png"));
            idleup2 = ImageIO.read(getClass().getResourceAsStream("/player/idleup2.png"));
            idleup3 = ImageIO.read(getClass().getResourceAsStream("/player/idleup3.png"));
            idleup4 = ImageIO.read(getClass().getResourceAsStream("/player/idleup4.png"));
            idledown1 = ImageIO.read(getClass().getResourceAsStream("/player/idledown1.png"));
            idledown2 = ImageIO.read(getClass().getResourceAsStream("/player/idledown2.png"));
            idledown3 = ImageIO.read(getClass().getResourceAsStream("/player/idledown3.png"));
            idledown4 = ImageIO.read(getClass().getResourceAsStream("/player/idledown4.png"));
            idleleft1 = ImageIO.read(getClass().getResourceAsStream("/player/idleleft1.png"));
            idleleft2 = ImageIO.read(getClass().getResourceAsStream("/player/idleleft2.png"));
            idleleft3 = ImageIO.read(getClass().getResourceAsStream("/player/idleleft3.png"));
            idleleft4 = ImageIO.read(getClass().getResourceAsStream("/player/idleleft4.png"));
            idleright1 = ImageIO.read(getClass().getResourceAsStream("/player/idleright1.png"));
            idleright2 = ImageIO.read(getClass().getResourceAsStream("/player/idleright2.png"));
            idleright3 = ImageIO.read(getClass().getResourceAsStream("/player/idleright3.png"));
            idleright4 = ImageIO.read(getClass().getResourceAsStream("/player/idleright4.png"));
            //DASH
            dashup1 = ImageIO.read(getClass().getResourceAsStream("/player/dashup1.png"));
            dashup2 = ImageIO.read(getClass().getResourceAsStream("/player/dashup2.png"));
            dashup3 = ImageIO.read(getClass().getResourceAsStream("/player/dashup3.png"));
            dashdown1 = ImageIO.read(getClass().getResourceAsStream("/player/dashdown1.png"));
            dashdown2 = ImageIO.read(getClass().getResourceAsStream("/player/dashdown2.png"));
            dashdown3 = ImageIO.read(getClass().getResourceAsStream("/player/dashdown3.png"));
            dashleft1 = ImageIO.read(getClass().getResourceAsStream("/player/dashleft1.png"));
            dashleft2 = ImageIO.read(getClass().getResourceAsStream("/player/dashleft2.png"));
            dashright1 = ImageIO.read(getClass().getResourceAsStream("/player/dashright1.png"));
            dashright2 = ImageIO.read(getClass().getResourceAsStream("/player/dashright2.png"));
            //ATTACK
            aup1 = ImageIO.read(getClass().getResourceAsStream("/attack/aup1.png"));
            aup2 = ImageIO.read(getClass().getResourceAsStream("/attack/aup2.png"));
            aup3 = ImageIO.read(getClass().getResourceAsStream("/attack/aup3.png"));
            aup4 = ImageIO.read(getClass().getResourceAsStream("/attack/aup4.png"));
            aup5 = ImageIO.read(getClass().getResourceAsStream("/attack/aup5.png"));
            aup6 = ImageIO.read(getClass().getResourceAsStream("/attack/aup6.png"));
            adown1 = ImageIO.read(getClass().getResourceAsStream("/attack/adown1.png"));
            adown2 = ImageIO.read(getClass().getResourceAsStream("/attack/adown2.png"));
            adown3 = ImageIO.read(getClass().getResourceAsStream("/attack/adown3.png"));
            adown4 = ImageIO.read(getClass().getResourceAsStream("/attack/adown4.png"));
            adown5 = ImageIO.read(getClass().getResourceAsStream("/attack/adown5.png"));
            adown6 = ImageIO.read(getClass().getResourceAsStream("/attack/adown6.png"));
            aleft1 = ImageIO.read(getClass().getResourceAsStream("/attack/aleft1.png"));
            aleft2 = ImageIO.read(getClass().getResourceAsStream("/attack/aleft2.png"));
            aleft3 = ImageIO.read(getClass().getResourceAsStream("/attack/aleft3.png"));
            aleft4 = ImageIO.read(getClass().getResourceAsStream("/attack/aleft4.png"));
            aleft5 = ImageIO.read(getClass().getResourceAsStream("/attack/aleft5.png"));
            aleft6 = ImageIO.read(getClass().getResourceAsStream("/attack/aleft6.png"));
            aright1 = ImageIO.read(getClass().getResourceAsStream("/attack/aright1.png"));
            aright2 = ImageIO.read(getClass().getResourceAsStream("/attack/aright2.png"));
            aright3 = ImageIO.read(getClass().getResourceAsStream("/attack/aright3.png"));
            aright4 = ImageIO.read(getClass().getResourceAsStream("/attack/aright4.png"));
            aright5 = ImageIO.read(getClass().getResourceAsStream("/attack/aright5.png"));
            aright6 = ImageIO.read(getClass().getResourceAsStream("/attack/aright6.png"));
            //PARTICLES
            pup1 = ImageIO.read(getClass().getResourceAsStream("/particle/pup1.png"));
            pup2 = ImageIO.read(getClass().getResourceAsStream("/particle/pup2.png"));
            pup3 = ImageIO.read(getClass().getResourceAsStream("/particle/pup3.png"));
            pup4 = ImageIO.read(getClass().getResourceAsStream("/particle/pup4.png"));
            pdown1 = ImageIO.read(getClass().getResourceAsStream("/particle/pdown1.png"));
            pdown2 = ImageIO.read(getClass().getResourceAsStream("/particle/pdown2.png"));
            pdown3 = ImageIO.read(getClass().getResourceAsStream("/particle/pdown3.png"));
            pdown4 = ImageIO.read(getClass().getResourceAsStream("/particle/pdown4.png"));
            pleft1 = ImageIO.read(getClass().getResourceAsStream("/particle/pleft1.png"));
            pleft2 = ImageIO.read(getClass().getResourceAsStream("/particle/pleft2.png"));
            pleft3 = ImageIO.read(getClass().getResourceAsStream("/particle/pleft3.png"));
            pleft4 = ImageIO.read(getClass().getResourceAsStream("/particle/pleft4.png"));
            pright1 = ImageIO.read(getClass().getResourceAsStream("/particle/pright1.png"));
            pright2 = ImageIO.read(getClass().getResourceAsStream("/particle/pright2.png"));
            pright3 = ImageIO.read(getClass().getResourceAsStream("/particle/pright3.png"));
            pright4 = ImageIO.read(getClass().getResourceAsStream("/particle/pright4.png"));
            pright5 = ImageIO.read(getClass().getResourceAsStream("/particle/pright5.png"));
            pright6 = ImageIO.read(getClass().getResourceAsStream("/particle/pright6.png"));



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Rectangle attackCollisionArea() {
        if (direction == "right") {
            return new Rectangle(x + attackWidth, y, gp.tileSize + attackWidth, gp.tileSize); // Assuming the player's size is the same as tileSize
        } else if (direction == "left") {
            return new Rectangle(x - attackWidth, y, gp.tileSize - attackWidth, gp.tileSize); // Assuming the player's size is the same as tileSize
        } else if (direction == "up") {
            return new Rectangle(x, y - attackHeight, gp.tileSize, gp.tileSize - attackHeight); // Assuming the player's size is the same as tileSize
        } else if (direction == "down") {
            return new Rectangle(x, y + attackHeight, gp.tileSize, gp.tileSize + attackHeight); // Assuming the player's size is the same as tileSize
        }
        return new Rectangle(x, y, 0, 0); // Assuming the player's size is the same as tileSize
    }

    public Rectangle getCollisionArea() {
        return new Rectangle(x + 5, y + 5, gp.tileSize - 20, gp.tileSize - 10); // Assuming the player's size is the same as tileSize
    }

    public void takeDamage(int damage) {
        if (invincible == false) {
            playSE(9);
            life -= damage; // Decrease life by damage amount
            invincible = true;
            isFlashing = true; // Start flashing effect
            flashCounter = 0;  // Reset the flash counter
        }
        if (life < 0) {
            life = 0; // Ensure life doesn't go below 0
        }
    }

    public void update() {
        boolean moving = false;

        if (cooldownTimer > 0) {
            cooldownTimer--;
        }

        if (isDashing) {
            dashTimer--;
            if (dashTimer <= 0) {
                isDashing = false;
                cooldownTimer = dashCooldown;
            }
        } else if (keyH.spacePressed && cooldownTimer == 0) {
            playSE(1);
            isDashing = true;
            dashTimer = dashDuration;
            dashCounter = 0;
            spriteNum = 1;
        }

        int currentSpeed = isDashing ? dashSpeed : speed;

        if (mouseHandler != null) {
            int mouseRelativeX = mouseHandler.x - x;
            int mouseRelativeY = mouseHandler.y - y;
            if (mouseRelativeY < 0 && Math.abs(mouseRelativeX) < Math.abs(mouseRelativeY)) {
                direction = "up";   // Mouse is above the player
            } else if (mouseRelativeY > 0 && Math.abs(mouseRelativeX) < Math.abs(mouseRelativeY)) {
                direction = "down"; // Mouse is below the player
            } else if (mouseRelativeX < 0 && Math.abs(mouseRelativeX) > Math.abs(mouseRelativeY)) {
                direction = "left"; // Mouse is to the left of the player
            } else if (mouseRelativeX > 0 && Math.abs(mouseRelativeX) > Math.abs(mouseRelativeY)) {
                direction = "right"; // Mouse is to the right of the player
            }
        } else {
            System.out.println("MouseHandler is not initialized correctly!");
        }
        // MOVEMENT
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                if (y - currentSpeed >= 100) {
                    y -= currentSpeed;
                }
            }
            if (keyH.downPressed) {
                if (y + currentSpeed <= gp.screenHeight - gp.tileSize - 15) {
                    y += currentSpeed;
                }
            }
            if (keyH.leftPressed) {
                if (x - currentSpeed >= 0) {
                    x -= currentSpeed;
                }
            }
            if (keyH.rightPressed) {
                if (x + currentSpeed <= gp.screenWidth - gp.tileSize) {
                    x += currentSpeed;
                }
            }

            moving = true;

            if (!isDashing) {
                spriteCounter++;
                if (spriteCounter > 6) {
                    spriteNum++;
                    if (spriteNum > 4) {
                        spriteNum = 1;
                    }
                    spriteCounter = 0;
                }
            }
        }

        // Update idle animation
        if (!moving && !isDashing) {
            idleCounter++;
            if (idleCounter > 6) {
                spriteNum++;
                if (spriteNum > 4) {
                    spriteNum = 1;
                }
                idleCounter = 0;
            }
        } else {
            idleCounter = 0; // Reset idle counter when moving
        }

        // Update dash animation
        if (isDashing) {
            dashCounter++;
            if (dashCounter > 10) { // Adjust dash animation speed as needed
                spriteNum++;
                if (spriteNum > 3) { // Assuming 3 frames for dash animation
                    spriteNum = 1;
                }
                dashCounter = 0;
            }
        }
        if (keyH.attackKeyPressed && !isAttacking && cooldownTimer == 0) {
            startAttack();
        }

        // Update attack animation
        if (isAttacking) {
            attackDurationCounter++; // Increment the attack duration counter
            attackCounter++; // Increment the frame counter for the attack animation

            // If enough time has passed, switch the attack frame
            if (attackCounter > 4) { // Change sprite every 8 frames
                attackSpriteNum++;
                if (attackSpriteNum > 7) { // Limit to 6 attack frames
                    attackSpriteNum = 1; // Reset to first frame
                }
                attackCounter = 0; // Reset the attack frame counter

            }

            // End the attack after the set duration
            if (attackDurationCounter > attackDuration) {
                endAttack();
            }
        }

        //EFFECTS
        if (isFlashing) {
            flashCounter++;
            if (flashCounter > FLASH_DURATION) {
                isFlashing = false; // End the flashing effect
            }
        }

        // PARTICLE ATTACK
        if (isAttacking) {
            attackCounter++;
            particleCounter++;

            // Update particle animation frame
            if (particleCounter > 4) {  // Update the frame after 5 iterations
                particleFrame++;

                if (particleFrame > 7) {  // Loop back to frame 1 after frame 4
                    particleFrame = 1;
                }
                particleCounter = 0;  // Reset the particle counter after updating the frame
            }

            //END ATTACK
            if (particleCounter > attackDuration) {
                isAttacking = false;
                attackCounter = 0;
                particleCounter = 0; // Reset the particle counter when the attack ends
            }
        }


        //INVINCIBLE
        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 120) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void startAttack() {
        if (!isAttacking) {
            isAttacking = true; // Start the attack
            attackDurationCounter = 0; // Reset the attack duration counter
            attackCounter = 0; // Reset the frame counter for the attack animation
            attackSpriteNum = 1; // Start from the first attack sprite
            playSE(4);
            gp.triggerCameraShake(10, 5); // Optional: Trigger camera shake during attack
        }
    }

    public void endAttack() {
        isAttacking = false; // End the attack
        attackDurationCounter = 0; // Reset the duration counter
        attackCounter = 0; // Reset the frame counter
        attackSpriteNum = 1; // Reset to the first attack frame
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        //FLASH
        if (isFlashing && (flashCounter / 5) % 2 == 0) { // Toggle red every 5 frames
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g2.setColor(Color.WHITE);
            g2.fillRect(gp.screenWidth / 2 - 1280 / 2, 0, gp.screenWidth, gp.screenHeight); // Red overlay
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        // Use dash sprites if dashing
        if (isDashing) {
            switch (direction) {
                case "up":
                    image = (spriteNum == 1) ? dashup1 : (spriteNum == 2) ? dashup2 : dashup3;
                    break;
                case "down":
                    image = (spriteNum == 1) ? dashdown1 : (spriteNum == 2) ? dashdown2 : dashdown3;
                    break;
                case "left":
                    image = (spriteNum == 1) ? dashleft1 : dashleft2;
                    break;
                case "right":
                    image = (spriteNum == 1) ? dashright1 : dashright2;
                    break;
            }
        }
        // Use walking sprites if moving
        else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            switch (direction) {
                case "up":
                    image = (spriteNum == 1) ? up1 : (spriteNum == 2) ? up2 : (spriteNum == 3) ? up3 : up4;
                    break;
                case "down":
                    image = (spriteNum == 1) ? down1 : (spriteNum == 2) ? down2 : (spriteNum == 3) ? down3 : down4;
                    break;
                case "left":
                    image = (spriteNum == 1) ? left1 : (spriteNum == 2) ? left2 : (spriteNum == 3) ? left3 : left4;
                    break;
                case "right":
                    image = (spriteNum == 1) ? right1 : (spriteNum == 2) ? right2 : (spriteNum == 3) ? right3 : right4;
                    break;
            }
        }
        // Use idle sprites if not moving
        else {
            switch (direction) {
                case "up":
                    image = (spriteNum == 1) ? idleup1 : (spriteNum == 2) ? idleup2 : (spriteNum == 3) ? idleup3 : idleup4;
                    break;
                case "down":
                    image = (spriteNum == 1) ? idledown2 : (spriteNum == 2) ? idledown2 : (spriteNum == 3) ? idledown4 : idledown4;
                    break;
                case "left":
                    image = (spriteNum == 1) ? idleleft2 : (spriteNum == 2) ? idleleft2 : (spriteNum == 3) ? idleleft4 : idleleft4;
                    break;
                case "right":
                    image = (spriteNum == 1) ? idleright2 : (spriteNum == 2) ? idleright2 : (spriteNum == 3) ? idleright4 : idleright4;
                    break;
            }
        }

        //Attacking sprites
        if (isAttacking) {
            switch (direction) {
                case "up":
                    image = (attackSpriteNum == 1) ? aup1 :
                            (attackSpriteNum == 2) ? aup2 :
                                    (attackSpriteNum == 3) ? aup3 :
                                            (attackSpriteNum == 4) ? aup4 :
                                                    (attackSpriteNum == 5) ? aup5 : aup6;
                    break;
                case "down":
                    image = (attackSpriteNum == 1) ? adown1 :
                            (attackSpriteNum == 2) ? adown2 :
                                    (attackSpriteNum == 3) ? adown3 :
                                            (attackSpriteNum == 4) ? adown4 :
                                                    (attackSpriteNum == 5) ? adown5 : adown6;
                    break;
                case "left":
                    image = (attackSpriteNum == 1) ? aleft1 :
                            (attackSpriteNum == 2) ? aleft2 :
                                    (attackSpriteNum == 3) ? aleft3 :
                                            (attackSpriteNum == 4) ? aleft4 :
                                                    (attackSpriteNum == 5) ? aleft5 : aleft6;
                    break;
                case "right":
                    image = (attackSpriteNum == 1) ? aright1 :
                            (attackSpriteNum == 2) ? aright2 :
                                    (attackSpriteNum == 3) ? aright3 :
                                            (attackSpriteNum == 4) ? aright4 :
                                                    (attackSpriteNum == 5) ? aright5 : aright6;
                    break;
            }
        }
        if (invincible == true && (invincibleCounter / 5) % 2 == 0) {
            image = dashup1;
        }

        int shadowWidth = gp.tileSize / 2;  // Width of shadow
        int shadowHeight = gp.tileSize / 4; // Height of shadow
        int shadowX = x + gp.tileSize / 4 - 2;  // Center shadow beneath sprite
        int shadowY = y + gp.tileSize - shadowHeight + 5; // Slightly below the sprite

// PARTICLE
        if (isAttacking) {
            BufferedImage particleImage = null;
            switch (direction) {
                case "up":
                    particleImage = (particleFrame == 1) ? pup1 : (particleFrame == 2) ? pup2 : (particleFrame == 3) ? pup3 : pup4;
                    break;
                case "down":
                    particleImage = (particleFrame == 1) ? pdown1 : (particleFrame == 2) ? pdown2 : (particleFrame == 3) ? pdown3 : pdown4;
                    break;
                case "left":
                    particleImage = (particleFrame == 1) ? pleft1 : (particleFrame == 2) ? pleft2 : (particleFrame == 3) ? pleft3 : pleft4;
                    break;
                case "right":
                    particleImage = (particleFrame == 1) ? pright1 : (particleFrame == 2) ? pright2 : (particleFrame == 3) ? pright3 : (particleFrame == 4) ? pright4 : (particleFrame == 5) ? pright5 : pright6;
                    break;
            }
            if (particleImage != null) {
                if (direction == "right") {
                    g2.drawImage(particleImage, x + attackWidth, y, gp.tileSize, gp.tileSize, null);
                } else if (direction == "left") {
                    g2.drawImage(particleImage, x - attackWidth, y, gp.tileSize, gp.tileSize, null);
                } else if (direction == "up") {
                    g2.drawImage(particleImage, x, y - attackHeight, gp.tileSize, gp.tileSize, null);
                } else if (direction == "down") {
                    g2.drawImage(particleImage, x, y + attackHeight, gp.tileSize, gp.tileSize, null);
                }
            }
        }

        //SHADOW
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2.setColor(Color.BLACK); // Semi-transparent black
        g2.fillOval(shadowX, shadowY, shadowWidth, shadowHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);

        int arrowSize = 10;
        int shadowOffset = 2;  // Shadow offset
        int arrowX = x + gp.tileSize / 4;
        int arrowY = y + gp.tileSize + 5;

        switch (direction) {
            case "up":
                arrowX = x + 37;  // Arrow stays on the same X as the player
                arrowY = y - attackHeight;  // Arrow is above the player
                break;
            case "down":
                arrowX = x + 37;  // Arrow stays on the same X as the player
                arrowY = y + attackHeight + gp.tileSize;  // Arrow is below the player
                break;
            case "left":
                arrowX = x - attackWidth;  // Arrow is to the left of the player
                arrowY = y + 30;  // Arrow stays on the same Y as the player
                break;
            case "right":
                arrowX = x + gp.tileSize + attackWidth;  // Arrow is to the right of the player
                arrowY = y + 30;  // Arrow stays on the same Y as the player
                break;
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
        g2.fillOval(arrowX, arrowY, arrowSize, arrowSize);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        int[] xPoints = new int[3];
        int[] yPoints = new int[3];

        switch (direction) {
            case "up":
                xPoints[0] = arrowX;
                yPoints[0] = arrowY;
                xPoints[1] = arrowX - arrowSize / 2;
                yPoints[1] = arrowY + arrowSize;
                xPoints[2] = arrowX + arrowSize / 2;
                yPoints[2] = arrowY + arrowSize;
                break;
            case "down":
                xPoints[0] = arrowX;
                yPoints[0] = arrowY;
                xPoints[1] = arrowX - arrowSize / 2;
                yPoints[1] = arrowY - arrowSize;
                xPoints[2] = arrowX + arrowSize / 2;
                yPoints[2] = arrowY - arrowSize;
                break;
            case "left":
                xPoints[0] = arrowX;
                yPoints[0] = arrowY;
                xPoints[1] = arrowX + arrowSize;
                yPoints[1] = arrowY - arrowSize / 2;
                xPoints[2] = arrowX + arrowSize;
                yPoints[2] = arrowY + arrowSize / 2;
                break;
            case "right":
                xPoints[0] = arrowX;
                yPoints[0] = arrowY;
                xPoints[1] = arrowX - arrowSize;
                yPoints[1] = arrowY - arrowSize / 2;
                xPoints[2] = arrowX - arrowSize;
                yPoints[2] = arrowY + arrowSize / 2;
                break;
        }

        g2.setColor(Color.DARK_GRAY);
        g2.fillPolygon(xPoints, yPoints, 3);
    }


    private void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }
}