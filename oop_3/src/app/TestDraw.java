package app;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestDraw {

    public static final int DELIMITER = 2;

    public static void main(String[] args) throws IOException {
        final CSVProcessor processor = new CSVProcessor();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String fileName;
        boolean success = false;
        int i = 1;
        while(!success) {
            System.out.printf("Enter data file name (#%d): %n", i);
            try {
                fileName = in.readLine();
                processor.loadFile(fileName);
                processor.parse(DELIMITER);
                processor.printArray();
                success = true;
            }catch (FileNotFoundException e) {
                if(i < 3){
                    i++;
                    e.printStackTrace();
                }
                else {
                    throw e;
                }
            }
        }
        in.close();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MyFrame myFrame = new MyFrame();
                DiagramDrawer d = new DiagramDrawer(processor.getArray());
                myFrame.add(d);
                myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                myFrame.setVisible(true);
            }
        });

    }

}
