package app;

import javax.swing.*;
import java.awt.*;

public class LabelComponent extends JPanel {
    public static int fontSize = 24;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        Font sansSerif = new Font("SansSerif", Font.BOLD, fontSize);
        g.setFont(sansSerif);
        g.drawString("Name: ", MESSAGE_X, MESSAGE_Y);
        g.drawString("Surname: ", MESSAGE_X + X_OFFSET, MESSAGE_Y);
        g.drawString("Cell: ", MESSAGE_X + 2*X_OFFSET, MESSAGE_Y);
        setBackground(new Color(150,150,150));
    }

    public static final int MESSAGE_X = 50;
    public static final int MESSAGE_Y = 30;
    public static final int X_OFFSET = 10 * fontSize;
}
