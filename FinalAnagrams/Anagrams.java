/**
 * Game of Anagrams.
 * NOTE: Best experienced at full screen.
 *
 * Nithya Arun and Nitya Babbar
 * 6/5/20
 */

// Imports
import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;

public class Anagrams
{
    // Instantiating objects
    private static Scanner scan = new Scanner(System.in); // Instantiating a Scanner object to be used for all user inputs
    private static File highScoreFileE = new File("EasyHighScore.txt"); // Instantiating a File object to hold highest score for the easy level
    private static File highScoreFileM = new File("MediumHighScore.txt"); // Instantiating a File object to hold highest score for the medium level
    private static File highScoreFileH = new File("HardHighScore.txt"); // Instantiating a File object to hold highest score for the hard level

    // Initializing variables
    private static boolean finished = false; // Variable that will be set to true once timer runs out
    private static String word = "y"; // Variable to hold user input
    private static int numLetters; // Variable to hold number of letters given based on the level of difficulty

    // Declaring variables
    private static int score; // Variable to keep track of points earned from words
    private static ArrayList<Character> letters; // ArrayList to hold the randomly generated letters
    private static String board; // String that will contain all outputs 
    private static int timeLeft; // Variable to track how much time is left before time runs out
    private static Timer timer; // Timer object called timer that is used to schedule tasks
    private static String level; // Variable to hold to level of difficulty chosen by the user

    /**
     * Method main to be ran by user in order to play the Anagrams game.
     * 
     * @param args - not used
     */
    public static void main(String[] args)
    {
        // While loop allowing user to play as many games as desired before terminating
        while (!word.equalsIgnoreCase("n") && !word.equals("!"))
        {
            // Calling the play() method again if the user selects "y"
            if (word.equalsIgnoreCase("y"))
            {
                play();
            }
            // If the user doesn't select "y", "n", or "!", asks again until they do
            else
            {
                System.out.print("\f"); // Clearing the screen
                board += "\nPlay another game (y/n)? "; // Adding a statement to re-ask to board
                System.out.print(board); // Reprinting board
                word = scan.nextLine(); // Scanning the user's response into word
                board += word; // Adding the response to board
            }
        }

        // If user wishes to discontinue, clearing screen and printing messages
        System.out.print("\f");
        System.out.println("Thank you for playing Anagrams!");
        System.out.println("Current highest score in the easy level: " + readingFile(highScoreFileE));
        System.out.println("Current highest score in the medium level: " + readingFile(highScoreFileM));
        System.out.println("Current highest score in the hard level: " + readingFile(highScoreFileH));
    }

    /**
     * Method play that receives words from user, and prints feedback message based on word.
     */
    private static void play()
    {
        // Calling start game
        startGame();

        if (!finished)
        {
            // Printing letters for user to use
            board += "\n\nYour letters are: ";
            for (int i = 0; i < letters.size(); i++)
            {
                board += letters.get(i) + " ";
            }

            System.out.println("");
            board += "\n";

            // Instantiating new ArrayList to hold all valid words used by the user, so that they are not repeated for points 
            ArrayList<String> wordsUsed = new ArrayList<String>();

            // While loop to continue while there is still time left in the game
            while (!finished)
            {
                // Printing a "- " and adding it to board to prompt for another word
                System.out.print("- ");
                board += "- ";

                // Scanning the letters entered into word
                word = scan.nextLine();

                // Ends the game early if the user wants to
                if (word.equals("!"))
                {
                    finished = true;
                }
                // Shuffling the order of the letters if user requests
                else if (!finished && word.equals("*"))
                {
                    shuffleBoard();
                }
                else
                {
                    // Adding the word to board
                    board += word;

                    // If the word is a real word, the word only contains given letters, it has not been used, and the
                    // length of the word is greater than 2: adds the earned points to score, adds an appropriate
                    // message to board, and adds the word to wordsUsed
                    if (isWord(word) && isValid(word) && !wordsUsed.contains(word.toUpperCase()) && word.length() > 2)
                    {
                        score += word.length() - 2;
                        if (!finished && word.length() == 3)
                        {
                            board += " --- " + (word.length() - 2) + " point!!\n";
                        }
                        else if (!finished)
                        {
                            board += " --- " + (word.length() - 2) + " points!!\n";
                        }
                        wordsUsed.add(word.toUpperCase());
                    }

                    // If the word is 2 letters or less, adds a message to inform the user to board
                    else if (word.length() <= 2)
                    {
                        if (!finished)
                        {
                            board += " --- Word is too short.\n";
                        }
                    }

                    // If the word is not a real word and contains letters that are not given / reuses letters, adds a message 
                    // to inform the user to board
                    else if (!isWord(word) && !isValid(word))
                    {
                        if (!finished)
                        {
                            board += " --- Not an English word and invalid letters.\n";
                        }
                    }

                    // If the word is not a real word, adds a message to inform the user to board
                    else if (!isWord(word))
                    {
                        if (!finished)
                        {
                            board += " --- Not an English word.\n";
                        }
                    }

                    // If the word contains letters that are not given / reuses letters, adds a message to inform the user to board
                    else if (!isValid(word))
                    {
                        if (!finished)
                        {
                            board += " --- The letters are invalid.\n";
                        }
                    }

                    // If the word was already used, adds a message to inform the user to board
                    else
                    {
                        if (!finished)
                        {
                            board += " --- Word already used.\n";
                        }
                    }
                }

                // Clears the screen
                System.out.print("\f");

                // Prints board
                System.out.print(board);
            }
        }
    }

    /**
     * Method startGame is a helper method used to set variables to beginning values right before user begins game, 
     * print a welcome message with the rules of the game, and schedules two timer tasks.
     */
    private static void startGame()
    {
        // Initializing / re-setting values to prepare for the game to begin
        finished = false;
        score = 0;
        timeLeft = 61;
        System.out.print("\f");
        board = "";

        // Playing music
        Music.playMusic("FinalJeopardySong.wav");

        // Printing welcome message
        System.out.println("Welcome to the Anagrams game!!!\nYou will be given a set of letters and 60 seconds to make " +
            "as many 3+ letter words as possible.\nYou will get 1 point for 3-letter words, 2 points for 4-letter words, " +
            "3 points for 5-letter words, and so on.\nType * to shuffle the letters.\nType ! to end the game.\n\nThere are " +
            "three levels of difficulty: easy, medium, and hard.\n");

        // Adding welcome message to board
        board += "Welcome to the Anagrams game!!!\nYou will be given a set of letters and 60 seconds to make " +
            "as many 3+ letter words as possible.\nYou will get 1 point for 3-letter words, 2 points for 4-letter words, " +
            "3 points for 5-letter words, and so on.\nType * to shuffle the letters.\nType ! to end the game.\n\n";

        // Keeps asking the user to pick a level until they enter a valid one or exit the game
        do
        {
            System.out.print("Choose a level (e/m/h): ");
            level = scan.nextLine();
        } 
        while (!(level.equalsIgnoreCase("e") || level.equalsIgnoreCase("m") || level.equalsIgnoreCase("h") || level.equals("!")));

        // Ends the game if the user wants to
        if (level.equals("!"))
        {
            finished = true;
            word = "n";
            Music.stopMusic();
        }
        else
        {
            // Print the level that the user chose
            if (level.equalsIgnoreCase("e"))
            {
                System.out.println("\nLevel chosen: Easy");
            }
            else if (level.equalsIgnoreCase("m"))
            {
                System.out.println("\nLevel chosen: Medium");
            }
            else
            {
                System.out.println("\nLevel chosen: Hard");
            }

            // Generate the random letters based on the level of difficulty chosen by the user
            letters = randomLetters();

            // Scanning to know when user press enter so that timer can begin - and continue execution
            System.out.print("\nPress the enter key when you are ready to begin.");
            String str = scan.nextLine();
            if (str.equals("!"))
            {
                finished = true;
                word = "n";
                Music.stopMusic();
            }

            // Adding the amount of time remaining to board
            board += "Remaining time: 60 seconds";

            // Stopping music once user presses enter
            Music.stopMusic();

            // Instantiating a new timer object
            timer = new Timer();

            // Task to be executed when 60 seconds run out
            TimerTask task = new EndGame();
            timer.schedule(task, 60200);

            // Task to be executed every second in order to update countdown
            TimerTask task2 = new Countdown();
            timer.schedule(task2, 0, 1000);
        }
    }

    /**
     * Method isWord to check if the word entered by the user is a real English word.
     *
     * @param input - the word that will be checked to see if it is a real English word
     * @return boolean - returns true if the words is a real English word, and false otherwise
     */
    private static boolean isWord(String input)
    {
        // Try statement
        try 
        {
            // Reading in a large file using the BufferedReader class
            BufferedReader reader = new BufferedReader(new FileReader("words.txt"));

            // Declaring a String variable that will hold what is read at each line of the file
            String str;

            // While loop to continue going through the file while there are still lines to be read
            while ((str = reader.readLine()) != null) 
            {
                // If the current line is the same as the word passed in, return true
                if (str.equalsIgnoreCase(input)) 
                {
                    return true;
                }
            }

            // Closing the BufferedReader object
            reader.close();
        }
        // Catch statement
        catch (IOException e) 
        {
        }

        // Returns false once the entire file has been read through, and there was no reason to return true
        return false;
    }

    /**
     * Method isValid to check if the word that the user entered only contains characters that are available to them.
     *
     * @param word - The word that the user entered
     * @return - true if the characters are valid, false otherwise
     */
    private static boolean isValid(String word)
    {
        // A copy of the letters ArrayList that is used to make sure the word does not reuse letters
        ArrayList<Character> lettersCopy = new ArrayList(letters);

        // Loops through each character in the word
        for (int i = 0; i < word.length(); i++)
        {
            // If the word contains a character that is not in lettersCopy, than it is invalid
            if (indexOf(lettersCopy, word.charAt(i)) < 0)
            {
                return false;
            }
            else
            {
                // Removes the letter from lettersCopy so it cannot be reused within the same word
                lettersCopy.remove(indexOf(lettersCopy, word.charAt(i)));
            }
        }

        // If the whole word has been searched without finding any invalid characters, then it must be valid
        return true;
    }

    /**
     * Method indexOf to search for an element in an ArrayList and return the index of its first occurrence, and -1 if 
     * it is not in the ArrayList.
     *
     * @param letters - the ArrayList to be searched
     * @param letter - the element to be found
     * @return - the first index of the element or -1 if the ArrayList doesn't contain that element
     */
    private static int indexOf(ArrayList<Character> letters, char letter)
    {
        // Loop through the ArrayList until the element is found
        for (int i = 0; i < letters.size(); i++)
        {
            if (letters.get(i) == Character.toUpperCase(letter))
            {
                return i;
            }
        }

        // Returns -1 if the ArrayList does not contain that character
        return -1;
    }

    /**
     * Method randomLetters to randomly select an appropriate mix of consonants and vowels, shuffle, and return them.
     *
     * @return Character ArrayList - containing the letters that will be used to play
     */
    private static ArrayList<Character> randomLetters()
    {
        String consonants = "BCDFGHJKLMNPQRSTVWXYZ"; // String holding all of the consonants
        String vowels = "AEIOU"; // String holding all of the vowels

        // Set the number of letters based on the level of difficulty
        if (level.equalsIgnoreCase("e"))
        {
            numLetters = 13;
        }
        else if (level.equalsIgnoreCase("m"))
        {
            numLetters = 10;
        }
        else
        {
            numLetters = 7;
        }

        // Instantiating an ArrayList the size of NUM_LETTERS using the Character wrapper class
        // since ArrayLists cannot store primitive types
        ArrayList<Character> letters = new ArrayList<Character>(numLetters);

        // Adding consonants to take up 3/4 of the indices in the letters ArrayList
        for (int i = 0; i < numLetters*3/4; i++)
        {
            letters.add(consonants.charAt((int)(Math.random() * 21)));
        }

        // Adding vowels to take up the remaining indices in the letters ArrayList
        for (int i = 0; i < numLetters-(numLetters*3/4); i++)
        {
            letters.add(vowels.charAt((int)(Math.random() * 5)));
        }

        // Shuffling all the letters so that all the consonants and vowels are mixed well
        shuffleLetters(letters);

        // Returning the letters ArrayList
        return letters;
    }

    /**
     * Method shuffleBoard to use when the user wants to shuffle the letters; makes the new order of letters show up on the screen.
     */
    private static void shuffleBoard()
    {
        // Shuffles the letters in the ArrayList
        shuffleLetters(letters);

        // Contains all the characters on the board before the letters
        String first = board.substring(0, 339);

        // Contains the shuffled letters
        String middle = "";
        for (int i = 0; i < letters.size(); i++)
        {
            middle += letters.get(i) + " ";
        }

        // Contains all the characters on the board after the letters
        String last = board.substring(338 + 2*numLetters, board.length() - 2);

        // Concatenates all three parts back into one string
        board = first + middle + last;

        // Clears and reprints the board
        System.out.print("\f");
        System.out.print(board);
    }

    /**
     * Method shuffleLetters to randomly swap the letters that the user can use to make words
     *
     * @param letters - the current order of the letters
     */
    public static void shuffleLetters(ArrayList<Character> letters) 
    {
        int r; // The random index to be swapped
        char temp; // The letter to be swapped

        // For k = the last index to 1, generate a random number between 0 and k, inclusive.
        // Swap letters[k] and letters[r]
        for (int k = letters.size() - 1; k >= 1; k--)
        {
            r = (int) ((k+1) * Math.random());

            temp = letters.get(k);
            letters.set(k, letters.get(r));
            letters.set(r, temp);
        }
    }

    /**
     * Method checkScoreHighest to use the readingFile method to check if the new score is higher than
     * previous highest score, and the writeFile method to write the new score into the file if it is higher.
     *
     * @param file - the file that will be read from in the if-statement
     * @param points - the score earned on the current turn, which will be compared to the value in the highScoreFile
     */
    public static void checkScoreHighest(File file, int points)
    {
        // If the points earned on this turn is higher than the current highest score in the file,
        // replace the highest score in the file with the score earned on this turn
        if (points > Integer.valueOf(readingFile(file)))
        {
            writeFile(file, points);
        }
    }

    /**
     * Method writeFile to write a value into highScoreFile, which contains the highest score ever
     * scored by the user.
     * 
     * @param file - the file that will be written into
     * @param points - the value to be written into the file
     */
    private static void writeFile(File file, int points)
    {
        // Try statement
        try
        {
            // Instantiating a FileWriter object called writer which will be used to write into the file,
            // setting the second parameter to false in order to replace the current score in the file
            FileWriter writer = new FileWriter (file, false);

            // Adding the score to the file as a String
            writer.append("" + points);

            // Closing the writer
            writer.close();
        }

        // Catch statement in case inputting the score does not work
        catch (IOException e) 
        {
        }
    }

    /**
     * Method readingFile to read and return the value in highScoreFile, which contains the highest score ever
     * scored by the user.
     * 
     * @param file - the file that will be read
     * @return String - the value found in the file
     */
    public static String readingFile(File file)
    {
        // Try statement
        try
        {
            // Instantiating a new scanner which scans the highScoreFile object
            Scanner myReader = new Scanner (file);   

            // Returning the next value read in the file
            return myReader.nextLine();
        }

        // Catch statement which returns a "sorry" message in the case that a file is not found
        catch (FileNotFoundException e) 
        {
            return "Sorry, your high score was not saved.";
        }
    }

    /**
     * Method updateCounter to update the time remaining that shows up on the screen.
     */
    public static void updateCounter()
    {
        // Decrement the amount of time left by 1 second
        timeLeft--;

        // String containing every character before the remaining time
        String first = board.substring(0, 309);

        // String containing the remaining time
        String middle = timeLeft + " seconds";

        // Adds an extra blank space to account for one less digit
        if (timeLeft < 10)
        {
            middle += " ";
        }

        // String containing every character after the remaining time
        String last = board.substring(309 + middle.length());

        // Concatenate all three parts back into one string
        board = first + middle + last;

        // Clears and reprints the board
        System.out.print("\f");
        System.out.print(board);
    }

    /**
     * Method getScore used to return score variable so other classes can utilize it.
     *
     * @return score
     */
    public static int getScore()
    {
        return score;
    }

    /**
     * Method setFinished to set finished to true once the time runs out in the TimerTask class.
     */
    public static void setFinished()
    {
        finished = true;
    }

    /**
     * Method setBoard to replace the board variable to another String in another class.
     *
     * @param b - String to change board to
     */
    public static void setBoard(String b)
    {
        board = b;
    }

    /**
     * Method addToBoard to add a String from another class to the board.
     *
     * @param b - the String to add to board
     */
    public static void addToBoard(String b)
    {
        board += b;
    }

    /**
     * Method getBoard to return the value of board so other classes can utilize it.
     *
     * @return the board String
     */
    public static String getBoard()
    {
        return board;
    }

    /**
     * Method getFinished to return the value of finished so other classes can utilize it.
     *
     * @return the finished boolean
     */
    public static boolean getFinished()
    {
        return finished;
    }

    /**
     * Method getLevel to return the level the user chose so that other classes can utilize it.
     *
     * @return the level String
     */
    public static String getLevel()
    {
        return level;
    }

    /**
     * Method getHighScoreFileE to return the HighScoreFileE file so other classes can utilize it.
     *
     * @return the HighScoreFileE File
     */
    public static File getHighScoreFileE()
    {
        return highScoreFileE;
    }

    /**
     * Method getHighScoreFileM to return the HighScoreFileM file so other classes can utilize it.
     *
     * @return the HighScoreFileM File
     */
    public static File getHighScoreFileM()
    {
        return highScoreFileM;
    }

    /**
     * Method getHighScoreFileH to return the HighScoreFileH file so other classes can utilize it.
     *
     * @return the HighScoreFileH File
     */
    public static File getHighScoreFileH()
    {
        return highScoreFileH;
    }

    /**
     * Method cancelTimer to cancel all of the timer's scheduled tasks once time runs out.
     */
    public static void cancelTimer()
    {
        timer.cancel();
        timer.purge();
    }
}