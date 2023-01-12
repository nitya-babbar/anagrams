/**
 * Ends the game when the time limit is up.
 *
 * Nithya Arun and Nitya Babbar
 * 6/5/2020
 */

import java.util.TimerTask;
import java.io.IOException;
import java.io.FileNotFoundException;

public class EndGame extends TimerTask
{
    /**
     * Method run called when the timer runs out. Overrides the abstract method called "run" in the TimerTask class.
     */
    public void run()
    {
        // Removes the "- " before printing the final score
        Anagrams.setBoard(Anagrams.getBoard().substring(0, Anagrams.getBoard().length()-2));

        // Adds final score to board
        Anagrams.addToBoard("\nScore: " + Anagrams.getScore());

        // Clears screen
        System.out.print("\f");

        // If the player chose the easy level
        if (Anagrams.getLevel().equalsIgnoreCase("e"))
        {
            // Checks if the new score is higher than user's prior highest score using the checkScoreHighest() method in the Anagrams class
            Anagrams.checkScoreHighest(Anagrams.getHighScoreFileE(), Anagrams.getScore());
            // Adds the highest score ever achieved by user (whether or not it was this turn) to board
            Anagrams.addToBoard("\nYour highest score ever in the easy level is: " + Anagrams.readingFile(Anagrams.getHighScoreFileE()));
        }

        // If the player chose the medium level
        else if (Anagrams.getLevel().equalsIgnoreCase("m"))
        {
            // Checks if the new score is higher than user's prior highest score using the checkScoreHighest() method in the Anagrams class
            Anagrams.checkScoreHighest(Anagrams.getHighScoreFileM(), Anagrams.getScore());
            // Adds the highest score ever achieved by user (whether or not it was this turn) to board
            Anagrams.addToBoard("\nYour highest score ever in the medium level is: " + Anagrams.readingFile(Anagrams.getHighScoreFileM()));
        }

        // If the player chose the hard level
        else if (Anagrams.getLevel().equalsIgnoreCase("h"))
        {
            // Checks if the new score is higher than user's prior highest score using the checkScoreHighest() method in the Anagrams class
            Anagrams.checkScoreHighest(Anagrams.getHighScoreFileH(), Anagrams.getScore());
            // Adds the highest score ever achieved by user (whether or not it was this turn) to board
            Anagrams.addToBoard("\nYour highest score ever in the hard level is: " + Anagrams.readingFile(Anagrams.getHighScoreFileH()));
        }

        // Adds the oppurtunity for user to play again to board
        Anagrams.addToBoard("\n\nPlay another game (y/n)? ");

        // Prints board
        System.out.print(Anagrams.getBoard());

        // Sets finished variable in Anagrams class to true so that words are no longer accepted
        Anagrams.setFinished();
    }
}
 