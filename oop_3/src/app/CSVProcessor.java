package app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVProcessor {

    private List<String> strings;
    private double[][] array;

    public CSVProcessor() {
        strings = new ArrayList<String>();
    }

    /**
     * Method loads file and retrieves all strings from it.
     *
     * @param filePath - file from which strings will be retrieved
     * @throws java.io.IOException
     */
    public void loadFile(String filePath) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                strings.add(line);
                i++;
            }
        } finally {
            if (bufferedReader != null) bufferedReader.close();
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder("");
        for (String line : strings) {
            str.append(line).append(" ");
        }
        return str.toString();
    }

    /**
     * @param delimiter - defines how many columns
     * @throws CSVParseException - any error is got cast to this type
     */
    public void parse(int delimiter) throws CSVParseException {
        array = new double[strings.size()][delimiter];
        try {
            for (int i = 1; i <= strings.size(); i++) {
                String[] parsed = strings.get(i - 1).split(",");
                if (parsed.length > delimiter) {
                    throw new CSVParseException(
                            String.format("Extra values detected on line %d. Exit.", i));
                } else if (parsed.length < delimiter) {
                    throw new CSVParseException(
                            String.format("Not enough values on line %d. Exit.", i));
                } else {
                    try{
                        for(int j = 0; j < parsed.length; j++) {
                            array[i - 1][j] = Integer.parseInt(parsed[j].trim());
                        }
                    }catch (NumberFormatException n){
                        throw new CSVParseException("Invalid data injected. Native message: " + n
                                .getMessage());
                    }
                }
            }
        } catch (Exception e) {
            throw new CSVParseException(e.getMessage());
        }
    }

    public void printArray() {
        for (double[] anArray : array) {
            String str = "";
            for(double value : anArray){
                str += value + ", ";
            }
            System.out.printf("%s%n", str);
        }
    }

    public double[][] getArray(){
        return array;
    }
}