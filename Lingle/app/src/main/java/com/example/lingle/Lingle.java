package com.example.lingle;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Robyn
 */
public class Lingle {

    //different objects to read and write files
    //FileWriter fw = null;
    //BufferedWriter bw = null;
    FileReader fr = null;
    BufferedReader br = null;

    //Lingle variables
    String correct_word, guess;
    boolean guessedIt;
    InputStream dictionary;
    InputStreamReader isr;
    //array to control number of times a char is in the correct word
    private int[] correct_word_charFreq;

    //private dictionary to make Android happy (it does not enjoy having InputStreams closed)
    private List<String> dictionaryList;

    //intialize constructor with default values and a dictionary
    //this dictionary will be the joined dictionary between the two languages
    public Lingle(InputStream dictionary){
        this.correct_word = "";
        this.guess = "";
        this.guessedIt = false;
        this.dictionary = dictionary;
        this.dictionaryList = loadDictionary();
        //this variable is specifically to avoid finding a yellow tile if
        //the correct word ran out of a specific letter before
        //this.correct_word_charFreq = getsCharFreqCorrectWord();
    }

    //takes a word and checks if it's a string of 5 letters with no tildes or numbers
    //returns an int representing the kind of issue it ran into if it did
    //0 -> no issue
    //1 -> not 5 letters
    //2 -> has invalid characters
    public int isWordValid(String word) {
        //string with all the inputs we want to avoid (eñes are okay)
        String unacceptable = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÒÓÔÕÖØÙÚÛÜÝß"
                + "àáâãäåæçèéêëìíîïðòóôõöøùúûüýÿ0123456789ªº\\!|\""
                + "@·#$~%&¬/()=?'¿¡€^`[*+]¨´{};,:._-";

        if (word.length() != 5)
            return 1;
        else if (word.matches(".*["+unacceptable+"].*"))
            return 2;
        else
            return 0;
    }

    //takes a word and checks if it's in the joined dictionary
    //sets the word as a guess if it's fine
    /**
    public boolean isInDictionary(String word) {

        boolean foundIt = false; //variable that will go true if it finds the word given

        try {
            isr = new InputStreamReader(this.dictionary);
            //fr = new FileReader("src/main/assets/"+this.dictionary.getName());
            //gets the name of the file to find it
            br = new BufferedReader(isr);
            //Log.i("isInDictionaryDiccionario", "El nombre del diccionario es "+this.dictionary.toString());
            Log.i("isInDictionaryGuess", "El valor de word aquí es: " +word);

            String reading = "";
            if (br.ready()){
                Log.i("isInDictionaryReady", "El BufferedReader está ready.");
                do {
                    reading = br.readLine();
                    if (reading != null) {
                        if (reading.equals(word)) {
                            foundIt = true; //breaks do while since it found the word
                            setGuess(word); //setting the word as a guess
                        }
                        //Log.i("BuclePalabras", "Los valores de reading son: " +reading);
                    }
                } while (reading != null);
                br.close();
                isr.close();
            } else Log.i("isInDictionaryNotReady", "El BufferedReader no está ready");

        } catch (FileNotFoundException ex) {
            Log.i("isInDictionaryFNFE", "No se ha encontrado el diccionario.");
            //System.out.println("File specified doesn't exist. Error: "+ex);
            ex.printStackTrace();
        } catch (Exception ex) {
            Log.i("isInDictionaryException", "Error general.");
            //System.out.println("Error: "+ex);
            ex.printStackTrace();
        } finally {
            return foundIt; //returns true or false depending on if it's in the dictionary or not
        }
    }*/

    public boolean isInDictionary(String word) {
        boolean foundIt = false;

        //dictionaryList = loadDictionary();

        for (String line : this.dictionaryList) {
            if (line.equals(word)) {
                foundIt = true;
                setGuess(word);
                break;
            }
        }
        return foundIt;
    }

    //workaround since it seems AssetManager does not bode well with reading a file multiple times
    private List<String> loadDictionary() {
        List<String> dictionary = new ArrayList<>();
        try {
            isr = new InputStreamReader(this.dictionary);
            br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                dictionary.add(line);
            }

            //not closing the InputStream since it seems like Android doesn't like that
            //br.close();
            //isr.close();
            //this.dictionary.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictionary;
    }

    //returns a string simulating the yellow and green in wordle
    // - is a black tile
    // / is a yellow tile
    // X is a green tile
    public String returnYellowGreen() {
        //initializing the charFreq everytime a new guess is introduced
        int[] aux_correct_word_charFreq = Arrays.copyOf(correct_word_charFreq, correct_word_charFreq.length);
        //string that we will return initialized as all not matching
        StringBuilder sb = new StringBuilder("-----");

        //first the green matches are processed, to prioritize them over the yellows
        for (int i=0;i<this.guess.length();i++) {
            //checks if the correct word still hasn't run out of a specific letter
            if (aux_correct_word_charFreq[this.guess.charAt(i) - 'A'] > 0){
                if (this.guess.charAt(i) == this.correct_word.charAt(i)){
                    sb.deleteCharAt(i);
                    sb.insert(i, "X"); //writes green on top if it finds the char exactly there
                    aux_correct_word_charFreq[this.guess.charAt(i) - 'A']--;
                }
            }
        }
        //now the yellows are processed, greens are still written on top to avoid
        //a yellow being written on top of an already processed green tile, but
        //green matches don't decrease the char frequency here
        for (int i=0;i<this.guess.length();i++) {
            //checks if the correct word still hasn't run out of a specific letter
            if (aux_correct_word_charFreq[this.guess.charAt(i) - 'A'] > 0){
                if (this.guess.charAt(i) == this.correct_word.charAt(i)){
                    sb.deleteCharAt(i);
                    sb.insert(i, "X"); //makes sure the green tile is not overwritten
                }
                else {
                    sb.deleteCharAt(i);
                    sb.insert(i, "/"); //writes yellow on top if it finds the char exactly there
                    aux_correct_word_charFreq[this.guess.charAt(i) - 'A']--;
                }
            }
        }
        return sb.toString(); //returns a string that looks something like -/-X-
    }

    //only used in this class, provides the number of times a certain letter is found
    //within the correct word, to provide better processing of the guessed word
    private int[] getCharFreqCorrectWord() {
        //will go from A to Ñ in the ASCII table (A is 65, Ñ is 165 (actually 209 or so?), size of 101 (145)
        int[] char_freq = new int[145];
        for (char letter : this.correct_word.toCharArray()) {
            char_freq[letter - 'A']++; //sets A as index 0 in this array
        }
        return char_freq;
    }

    public String getCorrect_word() {
        return correct_word;
    }

    public String getGuess() {
        return guess;
    }

    public boolean isGuessedIt() {
        this.guessedIt = this.guess.equals(this.correct_word); //returns true if they're equal
        return guessedIt;
    }

    //takes a dictionary file between the two possible ones, to make it a real 50% chance for languages
    public void setCorrect_word(InputStream dictionary, int length) { //not set as parameter, but with Math.random
        //generates numbers from 0 to the max length of the dictionary
        int rng = (int)(Math.random() * length); //fix the fact that length gives weird values
        int index = 0; //to increment until you find the value rng

        try {
            isr = new InputStreamReader(dictionary);
            //gets the name of the file to find it
            br = new BufferedReader(isr);

            String reading = "";

            if (br.ready()){
                do { //finds the line number that rng set
                    reading = br.readLine();
                    index++;
                } while (index <= rng);
                this.correct_word = reading; //correct word is set
                this.correct_word_charFreq = getCharFreqCorrectWord();
                br.close();
            }

        } catch (FileNotFoundException ex) {
            System.out.println("File specified doesn't exist. Error: "+ex);
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Error: "+ex);
            ex.printStackTrace();
        }
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public void setGuessedIt(boolean guessedIt) {
        this.guessedIt = guessedIt;
    }
}
