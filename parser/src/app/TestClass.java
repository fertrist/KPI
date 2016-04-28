package app;

import java.io.*;
import java.text.ParseException;

public class TestClass {

    static String fileName1 = "email.txt";
    static String fileName2 = "markup.log";

    public static void main(String [] args) throws IOException, ParseException {
        ParserEmail parser = new ParserEmail(fileName1);
        //parser.parseOneByOne();
        System.out.println(parser.parse());

        ParserMarkup parserMarkup = new ParserMarkup(fileName2);
        parserMarkup.parseOneByOne();
    }
}
