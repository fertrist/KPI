package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Paul Isaichuk on 4/19/15.
 * TOLSA LR_2
 */
public class Postfixer {

    public static final String FILE = "src/resources/examples.txt";

    enum Element {
        WHILE("while", 1),
        DO("do", 2),
        BEGIN("begin", 3),
        END("end", 3),
        ASSIGN(":=", 4),
        COMPARE("<|>|<=|>=", 5),
        ADD("[-+]", 6),
        OPERAND("[a-zA-Z]*[0-9]*", 7);

        private Pattern pattern;
        int priority;

        Element(String regexp, int priority) {
            this.pattern = Pattern.compile(regexp);
            this.priority = priority;
        }
    }

    public static void main(String[] args) {
        List<List<String>> allWords = readWordsFromFile(FILE);
        for (List<String> stWords : allWords) {
            String str = buildPostfixSentence(stWords);
            System.out.println(str);
        }
    }

    public static String buildPostfixSentence(List<String> words) {
        Stack<String> stack = new Stack<String>();
        StringBuilder str = new StringBuilder("");
        for (String word : words) {
            Element e1 = defineElement(word);
            if (e1 == Element.OPERAND) {
                str.append(String.format("[%s]", word));
            } else {
                for(; !stack.isEmpty() && defineElement(stack.firstElement()).priority >= e1.priority;) {
                    str.append(String.format("[%s]", stack.pop()));
                }
                stack.push(word);
            }
        }
        while (!stack.empty()) {
            String word = stack.pop();
            str.append(String.format("[%s]", word));
        }
        return str.toString();
    }

    public static Element defineElement(String word) {
        for (Element element : Element.values()) {
            if (element.pattern.matcher(word).matches()) {
                return element;
            }
        }
        throw new UndefinedElementException(
                String.format("Element %s is not defined as language construction.", word));
    }

    /*
     * Reads words line by line from the file.
     * @param file file
     * @return list of words read
     */
    private static List<List<String>> readWordsFromFile(String file) {
        Scanner scanner = null;
        List<List<String>> allWords = null;
        try {
            scanner = new Scanner(new FileInputStream(new File(file)));
            allWords = new ArrayList<List<String>>();
            StringBuilder str = new StringBuilder("");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                str.append(" ").append(line);
            }
            String code = str.toString();
            String[] statements = code.split(";");
            for (String stmnt : statements) {
                allWords.add(new ArrayList<String>(Arrays.asList(stmnt.trim().split(" "))));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return allWords;
    }

    private static class UndefinedElementException extends RuntimeException {
        public UndefinedElementException(String message) {
            super(message);
        }
    }
}