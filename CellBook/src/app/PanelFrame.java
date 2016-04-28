package app;

import javax.swing.*;
import java.awt.*;

public class PanelFrame extends JFrame {
    public PanelFrame() {
        setTitle("PhoneBook");
        setName("PhoneBook");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        double height = screenSize.getHeight();
        double width = screenSize.getWidth();
        setSize((int)width/2, (int)height/2);
        setLocationByPlatform(true);
        LabelComponent labelComponent = new LabelComponent();

        // add items to container
        add(labelComponent);


        add(new LabelComponent());
    }
}
