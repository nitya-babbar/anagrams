
/**
 * Music class to play and stop music.
 * 
 * Nithya Arun and Nitya Babbar
 * 6/5/20
 */

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Music 
{
    // Declaring a Clip object
    private static Clip clip;
    
    /**
     * Method playMusic to play music once during the introduction to the game.
     *
     * @param fileName - will hold the name of the file containing the song
     */
    public static void playMusic(String fileName)
    {
        // Try statement
        try
        {
            // Instantiating a File object called file to get the file
            File file = new File(fileName);
            
            // Instantiating an AudioInputStream object to get the audio from the file
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            
            // Assigning the clip from the AudioSystem class to clip so that
            // it can be started / stopped
            clip = AudioSystem.getClip();
            
            // Opening the audio so that it can be played
            clip.open(audio);
            
            // Starting the audio clip
            clip.start();
            
            // Looping the audio clip infinitely
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (LineUnavailableException|UnsupportedAudioFileException|IOException e)
        {
        }
    }
    /**
     * Method stopMusic to stop the music once the user presses enter.
     */
    public static void stopMusic()
    {
        // Closes the clip
        clip.close();
    }
}
