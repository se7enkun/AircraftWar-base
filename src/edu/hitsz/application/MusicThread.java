package edu.hitsz.application;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicThread extends Thread {
    private String filename;
    private boolean loop;
    private Clip clip;

    public MusicThread(String filename, boolean loop) {
        this.filename = filename;
        this.loop = loop;
    }

    @Override
    public void run() {
        try {
            File audioFile = new File(filename);
            if (!audioFile.exists()) return;

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // 停止播放方法
    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}