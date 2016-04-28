package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Paul Isaychuk on 1/2/15.
 * IP-21z
 */
class Gram {

    //public static final String GRAMMAR = "[a]+[b]+[c]+";
    public static final String GRAMMAR = "([a][b])*([c][b])*";
    public static Pattern PATTERN = Pattern.compile(GRAMMAR);
    public static final String FILE = "src/resources/words.txt";

    public static void main(String [] args) {
        //String word = getInputWord();
        List<String> words = readWordsFromFile(FILE);
        for (String word : words) {
            System.out.println(String.format("'%s' belongs to grammar '%s' : %s", word, GRAMMAR,
                    belongsToGrammar(word)));
        }
    }

    /*
     * Retrieves word form input stream.
     * @return given word
     */
    private static String getInputWord() {
        Scanner scanner = null;
        String word = null;
        try {
            scanner = new Scanner(System.in);
            System.out.println("Give a word: ");
            word = scanner.next();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return word;
    }

    /*
     * Defines if the word belongs to a pattern
     * @param word is under analysis
     * @return does work belongs to a pattern
     */
    private static boolean belongsToGrammar(String word) {
        Matcher matcher = PATTERN.matcher(word);
        return matcher.matches();
    }

    /*
     * Reads words line by line from the file.
     * @param file file
     * @return list of words read
     */
    private static List<String> readWordsFromFile(String file) {
        Scanner scanner = null;
        List<String> words = null;
        try {
            scanner = new Scanner(new FileInputStream(new File(file)));
            words = new ArrayList<String>();
            while (scanner.hasNextLine()) {
                words.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return words;
    }
}
