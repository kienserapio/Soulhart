package main;

import entity.Player;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    GamePanel gp;
    Player player;
    public int x;
    public int y;

    public MouseHandler(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && (gp.gameState == gp.playState || gp.gameState == gp.pressState)) {
                player.startAttack(); // Call a method in the Player class to handle the attack logic
                player.isAttacking = true;
        }
    }
}