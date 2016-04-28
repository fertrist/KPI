package app;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class DiagramDrawer extends JPanel{

    private double[][] data;
    public static int SCALE_POINTS;
    public static int BLOCK_WIDTH;
    /** (0;0) */
    public static final int X;
    public static final int Y;

    /** matches how many pixels need for 1 degree of data value */
    public static double FACTOR;

    private Color[] colors = new Color[] {
           Color.BLUE, Color.RED, Color.GREEN
    };

    static {
        X = MyFrame.WIDTH_OFFSET;
        Y = MyFrame.FRAME_HEIGHT - MyFrame.HEIGHT_OFFSET;
    }

    public DiagramDrawer(double[][] data) {
        this.data = data;
        SCALE_POINTS = (int) Math.round(max());
        FACTOR = (Y - MyFrame.HEIGHT_OFFSET) / SCALE_POINTS;
        BLOCK_WIDTH = (MyFrame.FRAME_WIDTH - MyFrame.WIDTH_OFFSET * 4) / (data.length * 3) ;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        //vertical line
        g2.drawLine(X, MyFrame.HEIGHT_OFFSET,
                X, Y + (int)FACTOR + MyFrame.HEIGHT_OFFSET);

        //horizontal line
        g2.drawLine(X - MyFrame.WIDTH_OFFSET, Y,
                MyFrame.FRAME_WIDTH - MyFrame.WIDTH_OFFSET, Y);

        //vertical scale
        int y = Y;
        for(int i = 0; i <= SCALE_POINTS; i++, y -= FACTOR) {
            g2.drawString(String.format("%d", i), X - 20, y);
            g2.drawLine(X - 3, y, X + 3, y);
        }

        //build a chart
        int x = X + MyFrame.WIDTH_OFFSET;
        for (double[] array : data) {
            for (int i = 0; i < array.length; i++) {
                double value = array[i];
                Color color = colors[i];
                double leftX = x;
                double topY = Y - value * FACTOR;
                double height = Y - (Y - value * FACTOR);
                Rectangle2D rect = new Rectangle2D.Double(leftX, topY, BLOCK_WIDTH, height);
                g2.setColor(color);
                g2.fill(rect);
                g2.setColor(Color.BLACK);
                g2.draw(rect);
                x += BLOCK_WIDTH;
            }
            x += BLOCK_WIDTH;
        }
    }

    /**
     * @return - max value from all data values
     */
    private double max() {
        double max = 0;
        for(double[] row : data) {
            for(double item : row){
                if(item > max) max = item;
            }
        }
        return max;
    }
}
