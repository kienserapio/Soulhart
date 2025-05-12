package main;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {

    Clip clip;

    public void setFile(int i) {
        try {
            // Use a switch or other logic to choose your audio file
            String filePath;
            switch (i) {
                case 1: filePath = "res/sound/dash.wav"; break;
                case 2: filePath = "res/sound/mainmenu.wav"; break;
                case 3: filePath = "res/sound/kill.wav"; break;
                case 4: filePath = "res/sound/attack.wav"; break;
                case 5: filePath = "res/sound/click.wav"; break;
                case 6: filePath = "res/sound/playstate.wav"; break;
                case 7: filePath = "res/sound/select.wav"; break;
                case 8: filePath = "res/sound/gameover.wav"; break;
                case 9: filePath = "res/sound/hit.wav"; break;
                case 10: filePath = "res/sound/nextlevel.wav"; break;
                case 11: filePath = "res/sound/walk.wav"; break;
                default: filePath = null;
            }

            if (filePath != null) {
                File soundFile = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            } else {
                System.err.println("Error: Invalid sound ID.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Could not load sound file.");
            clip = null; // Ensure the clip is null on error
        }
    }

    public void play() {
        if (clip != null) {
            clip.start();
        } else {
            System.err.println("Error: Clip is null.");
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.err.println("Error: Clip is null.");
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0); // Reset the clip to the beginning
        }
    }
}
