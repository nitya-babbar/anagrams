/**
 * Updates the countdown.
 *
 * Nithya Arun and Nitya Babbar
 * 6/5/2020
 */

import java.util.TimerTask;

public class Countdown extends TimerTask
{
    /**
     * Method run called by the timer in 1 second intervals. Overrides the abstract method called "run" in the TimerTask class.
     */
    public void run()
    {
        // Calls the updateCounter() method of the Anagrams class while the game is not finished
        if (!Anagrams.getFinished())
        {
            Anagrams.updateCounter();
        }
        // If the game is finished, cancels all of the timer's scheduled tasks
        else
        {
            Anagrams.cancelTimer();
        }
    }
}