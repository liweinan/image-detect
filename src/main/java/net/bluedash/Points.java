import javax.swing.*;
import java.awt.*;


public class Points extends JPanel {
    private MultiValueMap<Color, Point> pixels;

    public Points(MultiValueMap<Color, Point> pixels) {
        this.pixels = pixels;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        for (Color color : pixels.keys()) {
            g2d.setColor(color);
            for (Point p : pixels.getValues(color)) {
                g2d.drawLine(p.x, p.y, p.x, p.y);
            }
        }

    }

}
