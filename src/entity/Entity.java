package entity;

import java.awt.image.BufferedImage;

public class Entity {

    public int x, y;
    public int speed;

    public boolean invincible;
    public int invincibleCounter;

    public int maxLife;
    public int life;

    public BufferedImage up1, up2, up3, up4, up5, down1, down2, down3, down4, down5, left1, left2, left3, left4, right1, right2, right3, right4;
    public BufferedImage idleup1, idleup2, idleup3, idleup4, idledown1, idledown2, idledown3, idledown4, idleleft1, idleleft2, idleleft3, idleleft4, idleright1, idleright2, idleright3, idleright4;
    public BufferedImage dashdown1, dashdown2, dashdown3, dashup1, dashup2, dashup3, dashleft1, dashleft2, dashright1, dashright2;
    public BufferedImage adown1, adown2, adown3, adown4, adown5, adown6, aleft1, aleft2, aleft3, aleft4, aleft5, aleft6, aright1, aright2, aright3, aright4, aright5, aright6, aup1, aup2, aup3, aup4, aup5, aup6;
    public BufferedImage darkleft1, darkleft2, darkleft3, darkleft4, darkright1, darkright2, darkright3, darkright4, darkup1, darkup2, darkup3, darkup4, darkdown1, darkdown2, darkdown3, darkdown4;
    public BufferedImage fastleft1, fastleft2, fastleft3, fastleft4, fastright1, fastright2, fastright3, fastright4, fastup1, fastup2, fastup3, fastup4, fastdown1, fastdown2, fastdown3, fastdown4;
    public BufferedImage madleft1, madleft2, madleft3, madleft4, madright1, madright2, madright3, madright4, madup1, madup2, madup3, madup4, maddown1, maddown2, maddown3, maddown4, madhit;
    public BufferedImage pdown1, pdown2, pdown3, pdown4, pleft1, pleft2, pleft3, pleft4, pright1, pright2, pright3, pright4, pright5, pright6, pup1, pup2, pup3, pup4;

    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int attackSpriteNum = 1;
}
