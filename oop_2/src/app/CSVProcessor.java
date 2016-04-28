package app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVProcessor {

    private List<String> strings;
    private String [][] array;

    public CSVProcessor(){
        strings = new ArrayList<String>();
    }

    /**
     * Method loads file and retrieves all strings from it.
     * @param filePath - file from which strings will be retrieved
     * @throws IOException
     */
    public void loadFile(String filePath) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;
            int i = 0;
            while((line = bufferedReader.readLine()) != null){
                strings.add(line);
                i++;
            }
            array = new String[i][2];
        } finally {
            if(bufferedReader != null) bufferedReader.close();
        }
    }

    /**
     * Method removes all data, retaining it in file.
     * @param filePath - file in which data will be retained.
     * @throws IOException
     */
    public void retainToFile(String filePath) throws IOException {
        BufferedWriter bufferedWriter = null;
        try{
            bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            for(String line : strings){
                bufferedWriter.write(line);
            }
            strings.clear();
        }finally {
            if(bufferedWriter != null) bufferedWriter.close();
        }
    }

    /**
     * @param filePath - file where data will be serialized to
     * @throws IOException
     */
    public void serializeTo(String filePath) throws IOException {
        ObjectOutputStream out;
        out = new ObjectOutputStream(new FileOutputStream(filePath));
        out.writeObject(strings);
    }

    /**
     * @param filePath - file where data will be de-serialized from
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void deSerialize(String filePath) throws IOException, ClassNotFoundException {
        ObjectInputStream in;
        in = new ObjectInputStream(new FileInputStream(filePath));
        strings = (List<String>) in.readObject();
    }

    public String toString() {
        StringBuilder str = new StringBuilder("");
        for (String line : strings) {
           str.append(line).append(" ");
        }
        return str.toString();
    }
    public void parse() throws CSVParseException {
        try {
            for (int i = 1; i <= strings.size(); i++) {
                String[] parsed = strings.get(i - 1).split(",");
                if (parsed.length > 2) {
                    throw new CSVParseException(String.format("Extra values detected on line %d. Exit.", i));
                } else if (parsed.length < 2) {
                    throw new CSVParseException(String.format("Not enough values on line %d. Exit.", i));
                } else {
                    array[i - 1][0] = parsed[0].trim();
                    array[i - 1][1] = parsed[1].trim();
                }
            }
        }catch (Exception e){
            throw new CSVParseException(e.getMessage());
        }
    }

    public void printArray(){
        for(int i = 0; i < array.length; i++){
            System.out.printf("%s, %s%n", array[i][0], array[i][1]);
        }
    }
}