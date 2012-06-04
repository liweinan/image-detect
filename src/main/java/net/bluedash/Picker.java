import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Picker {

    // Hue偏差允许0.5%
    private static final double Hd = 0.5;

    // Saturation偏差允许0.5%
    private static final double Sd = 0.5;

    // Luminance编差允许1%
    private static final double Ld = 1.0;

    // 图中的绿色样本
    private static final Color GREEN = new Color(46, 189, 102);

    // 将绿色转化到hsl空间
    private static final HSLColor GREEN_HSL = new HSLColor(GREEN);

    // 图中的蓝色样本
    private static final Color BLUE = new Color(87, 91, 212);

    // 将蓝色转化到hsl空间
    private static final HSLColor BLUE_HSL = new HSLColor(BLUE);

    // 图中的红色样本
    private static final Color RED = new Color(238, 48, 50);

    // 将红色转化到hsl空间
    private static final HSLColor RED_HSL = new HSLColor(RED);

    private static final HSLColor[] COLOR_SAMPLES = {GREEN_HSL, BLUE_HSL, RED_HSL};

    // 允许的颜色偏差范围为1.5%
    private static final double SCOPE = 0.15;

    public static void main(String[] args) throws IOException {
        // 读取图片文件
        File file = new File(Picker.class.getClassLoader().getResource("").getPath() + "sample.jpg");
        BufferedImage bufImg = ImageIO.read(file);

        int height = bufImg.getHeight();
        int width = bufImg.getWidth();

        List<Double> deviations = new ArrayList<Double>();

        MultiValueMap<Color, Point> mvm =
                new MultiValueMap<Color, Point>(new ArrayList<Point>());

        // 总像素数
        int totalPixels = width * height;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // 获取当前像素的颜色
                Color rgbColor = new Color(bufImg.getRGB(i, j));

                // 去掉图中所有的白底
                if (rgbColor.equals(Color.WHITE)) {
                    totalPixels -= 1;
                    continue;
                }

                // 转化到HSL空间
                HSLColor color = new HSLColor(rgbColor);

                // 计算颜色偏差
                for (HSLColor sampleColor : COLOR_SAMPLES) {
                    double hDeviation = Math.abs(color.getHue() - sampleColor.getHue());
                    double sDeviation = Math.abs(color.getSaturation() - sampleColor.getSaturation());
                    double lDeviation = Math.abs(color.getLuminance() - sampleColor.getLuminance());

                    double result = Math.sqrt(Hd * hDeviation * hDeviation + Sd
                            * sDeviation * sDeviation + Ld
                            * lDeviation * lDeviation) / 100;

                    if (result < SCOPE) {
                        // 颜色与红，绿，蓝中的一种匹配上了，加入到MultiValueMap当中
                        mvm.addValue(sampleColor.getRGB(), new Point(i, j));
                        // 颜色匹配上了，没有必要再计算别的颜色
                        break;
                    }
                }
            }
        }

        System.out.println("Green Color: " + ((double) mvm.getValues(GREEN).size() / (double) totalPixels * 100) + "%");
        System.out.println("Red Color: " + ((double) mvm.getValues(RED).size() / (double) totalPixels * 100) + "%");
        System.out.println("Blue Color: " + ((double) mvm.getValues(BLUE).size() / (double) totalPixels * 100) + "%");

        JFrame frame = new JFrame("Result");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Points(mvm));
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);



    }

}
