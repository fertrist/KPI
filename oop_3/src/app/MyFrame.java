package app;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

    public static int FRAME_WIDTH;
    public static int FRAME_HEIGHT;
    public static int WIDTH_OFFSET;
    public static int HEIGHT_OFFSET;

    public MyFrame() {
        //evaluate appropriate size
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        FRAME_WIDTH = screenSize.width / 2;
        FRAME_HEIGHT = screenSize.height / 2;
        WIDTH_OFFSET = FRAME_WIDTH / 20 + FRAME_WIDTH / 100;
        HEIGHT_OFFSET = FRAME_HEIGHT / 10;
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }
}
