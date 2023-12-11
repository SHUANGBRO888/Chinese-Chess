package chessGame.Util;

import javax.sound.sampled.*;
import java.io.File;

public class SoundUtil {
    // Method to play a sound file continuously.
    public void playSound(String musicLocation) {
        try {
            // Create a File object from the given file path.
            File musicPath = new File(musicLocation);

            // Check if the file exists.
            if (musicPath.exists()) {
                // Get an AudioInputStream from the file.
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);

                // Obtain a Clip to play the sound.
                Clip clip = AudioSystem.getClip();

                // Open the audio stream and start playing the sound.
                clip.open(audioInput);
                clip.start();

                // Loop the sound continuously.
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                // Print an error message if the file is not found.
                System.out.println("File not found: " + musicLocation);
            }
        } catch (Exception ex) {
            // Print the stack trace if an exception occurs.
            ex.printStackTrace();
        }
    }

    // Method to play a music file once.
    public void playMusic(String musicLocation) {
        try {
            // Create a File object from the given file path.
            File musicPath = new File(musicLocation);

            // Check if the file exists.
            if (musicPath.exists()) {
                // Get an AudioInputStream from the file.
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);

                // Obtain a Clip to play the music.
                Clip clip = AudioSystem.getClip();

                // Open the audio stream and start playing the music.
                clip.open(audioInput);
                clip.start();               
            } else {
                // Print an error message if the file is not found.
                System.out.println("File not found: " + musicLocation);
            }
        } catch (Exception ex) {
            // Print the stack trace if an exception occurs.
            ex.printStackTrace();
        }
    }

}




