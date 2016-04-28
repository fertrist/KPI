package app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVProcessor {

    private List<String> values;

    public CSVProcessor(){
        values = new ArrayList<String>();
    }

    /**
     * Method loads file and retrieves all values from it.
     * @param filePath - file from which values will be retrieved
     * @throws IOException
     */
    public void loadFile(String filePath) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;
            while((line = bufferedReader.readLine()) != null){
                String [] splittedValues = line.split(",");
                for(String value : splittedValues){
                    values.add(value.trim());
                }
            }
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
            int i = 0;
            for(; i < values.size()-1; i++){
                bufferedWriter.write(values.get(i) + ", ");
            }
            bufferedWriter.write(values.get(i) + ".");
            values.clear();
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
        out.writeObject(values);
    }

    /**
     * @param filePath - file where data will be de-serialized from
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void deSerialize(String filePath) throws IOException, ClassNotFoundException {
        ObjectInputStream in;
        in = new ObjectInputStream(new FileInputStream(filePath));
        values = (List<String>) in.readObject();
    }

    public String toString(){
        return values.toString();
    }
}