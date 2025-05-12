package main;

import entity.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;


public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean spacePressed = false;
    public boolean escPressed = true;
    public boolean enterPressed = false;
    public boolean attackKeyPressed = false;
    Sound sound = new Sound();
    GamePanel gp;
    Player player;
    UI ui;

    public KeyHandler(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
    }
    public void playMusic(int i) {
        sound.setFile(2);
        sound.play();
        sound.loop();
    }

    public void stopMusic() {
        sound.stop();
    }

    private void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //MENU
        if (gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                playSE(5);
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 3;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                playSE(5);
                if (gp.ui.commandNum > 3) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    playSE(7);
                    gp.gameState = gp.dialogueState;
                }
                if (gp.ui.commandNum == 1) {
                    playSE(7);
                    gp.gameState = gp.tutorialState;
                }
                if (gp.ui.commandNum == 2) {
                    playSE(7);
                    gp.gameState = gp.creditsState;
                }
                if (gp.ui.commandNum == 3) {
                    System.exit(0);
                }
            }
        }

        //GAME
        if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) {
                upPressed = true;
            }
            if (code == KeyEvent.VK_S) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = true;
            }
            if (code == KeyEvent.VK_SPACE) {
                spacePressed = true;
            }
            if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
//            if (code == KeyEvent.VK_E) {
//                gp.gameState = gp.upgradeState;
//            }
            if (code == KeyEvent.VK_ESCAPE) {
                escPressed = true;
                playSE(7);
                gp.gameState = gp.pauseState;
            }
        }
        //PAUSE
        else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.pauseNum--;
                playSE(5);
                if (gp.ui.pauseNum < 0) {
                    gp.ui.pauseNum = 1;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.pauseNum++;
                playSE(5);
                if (gp.ui.pauseNum > 1) {
                    gp.ui.pauseNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.pauseNum == 0) {
                    playSE(7);
                    gp.gameState = gp.playState;
                }
                if (gp.ui.pauseNum == 1) {
                    playSE(7);
                    gp.gameState = gp.titleState;
                }
            }
            if (code == KeyEvent.VK_ESCAPE) {
                playSE(7);
                gp.gameState = gp.playState;
            }
        }

        //UPGRADE
        else if (gp.gameState == gp.upgradeState) {
            if (code == KeyEvent.VK_A) {
                gp.ui.upgradeNum--;
                playSE(5);
                if (gp.ui.upgradeNum < 0) {
                    gp.ui.upgradeNum = 2;
                }
            }
            if (code == KeyEvent.VK_D) {
                gp.ui.upgradeNum++;
                playSE(5);
                if (gp.ui.upgradeNum > 2) {
                    gp.ui.upgradeNum = 0;
                }
            }
            if (code == KeyEvent.VK_SPACE) {
                playSE(7);
                if (gp.ui.upgradeNum == 0) {
                    if (gp.soul < 100) {

                    } else {
                    gp.player.dashCooldown = 30;
                    gp.soul -= 100;
                }
                    gp.gameState = gp.playState;
                }
            }
            if (code == KeyEvent.VK_SPACE) {
                playSE(7);
                if (gp.ui.upgradeNum == 1) {
                    if (gp.soul < 150) {

                    } else {
                        gp.player.speed = 9;
                        gp.soul -= 150;
                    }
                    gp.gameState = gp.playState;
                }
            }
            if (code == KeyEvent.VK_SPACE) {
                playSE(7);
                if (gp.ui.upgradeNum == 2) {
                    if (gp.soul >= 200 && gp.player.life < gp.player.maxLife) { // Check both conditions
                        gp.player.life += 1; // Increment player's life
                        if (gp.player.life > gp.player.maxLife) {
                            gp.player.life = gp.player.maxLife; // Cap life at maxLife (just in case)
                        }
                        gp.soul -= 200; // Deduct soul only if life was increased
                    }
                    gp.gameState = gp.playState; // Return to play state
                }
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.playState;
            }
        }
        //DIALOGUE
        else if (gp.gameState == gp.dialogueState) {
            if (code == KeyEvent.VK_SPACE) {
                gp.gameState = gp.playState;
            }
        }
        //CREDITS
        else if (gp.gameState == gp.creditsState) {
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.titleState;
            }
        }
        //TUTORIAL
        else if (gp.gameState == gp.tutorialState) {
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.titleState;
            }
        }
        //GAME OVER
        else if (gp.gameState == gp.gameoverState) {
            if (code == KeyEvent.VK_SPACE) {
                gp.gameState = gp.titleState;
            }
        }
        //PRESS E
        else if (gp.gameState == gp.pressState) {
            if (code == KeyEvent.VK_W) {
                upPressed = true;
            }
            if (code == KeyEvent.VK_S) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = true;
            }
            if (code == KeyEvent.VK_E) {
                gp.gameState = gp.upgradeState;
            }
            if (code == KeyEvent.VK_SPACE) {
                spacePressed = true;
            }
            if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.playState;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            escPressed = false;
        }
    }

    public void keyTyped(KeyEvent e) {
        // This method is required but not used in this implementation
    }
}

