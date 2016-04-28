package app;

import java.io.*;

public class TestIO {

    static final String SERIALIZATION_FILE = "serialized.dat";
    static final String OUTPUT_FILE = "output.dat";

    /**
     * Testing method. Matches all required test cases.
     * @param args - standard syntax
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        final CSVProcessor processor = new CSVProcessor();
        File file = new File(SERIALIZATION_FILE);
        if (file.exists()) {
            //point 1
            processor.deSerialize(SERIALIZATION_FILE);
        } else {
            int i = 1;
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(System.in));
            String filePath;
            boolean success = false;
            while (!success) {
                System.out.printf("Enter file path(attempt#%d): %n", i);
                try {
                    //point 2
                    filePath = in.readLine();
                    //point 3
                    processor.loadFile(filePath);
                    success = true;
                } catch (IOException e) {
                    if (i < 3) {
                        i++;
                        e.printStackTrace();
                    } else{
                        throw e;
                    }
                }
            }
            in.close();
            processor.parse();
            //point 4
            Thread thread = new Thread(){
                //overriding thread method
                public void run() {
                    try {
                        System.out.printf("In parser thread %s...%n", Thread.currentThread().getName());
                        processor.parse();
                        processor.printArray();
                        System.out.println("Finishing parser thread...");
                        this.interrupt();
                    } catch (CSVParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.join();
            System.out.printf("Before parser thread started, in thread %s...%n", Thread.currentThread().getName());
            thread.start();
            processor.serializeTo(SERIALIZATION_FILE);
            System.out.printf("Back to thread %s%n", Thread.currentThread().getName());
        }
        //point 5
        System.out.println(processor.toString());
        //retain data
        processor.retainToFile(OUTPUT_FILE);
    }
}