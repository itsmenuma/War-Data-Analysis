package DataAnalysis;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

public class BarChartExample extends JPanel {

    private static final long serialVersionUID = -4175998745125394822L;
    private int[] values;
    private String[] labels;
    private Color[] colors;
    private String title;

    public BarChartExample(Map<String, Integer> data, String t) {
    	setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
    	setForeground(new Color(0, 0, 0));
    	setBackground(new Color(255, 255, 255));
    	setLayout(null);
        values = data.values().stream().mapToInt(Integer::intValue).toArray();
        labels = data.keySet().toArray(new String[0]);
        colors = new Color[]{Color.red, Color.blue, Color.green, Color.yellow, Color.orange}; // Adjust color as needed
        title = t;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (values == null || values.length == 0)
            return;

        double minValue = 0;
        double maxValue = 0;
        for (int i = 0; i < values.length; i++) {
            if (minValue > values[i])
                minValue = values[i];
            if (maxValue < values[i])
                maxValue = values[i];
        }

        Dimension dim = getSize();
        int panelWidth = dim.width;
        int panelHeight = dim.height;
        int barWidth = panelWidth / values.length;

        Font titleFont = new Font("SansSerif", Font.BOLD, 20);
        FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
        FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

        int titleWidth = titleFontMetrics.stringWidth(title);
        int y = titleFontMetrics.getAscent();
        int x = (panelWidth - titleWidth) / 2;
        g.setFont(titleFont);
        g.drawString(title, x, y);

        int top = titleFontMetrics.getHeight();
        int bottom = labelFontMetrics.getHeight();
        if (maxValue == minValue)
            return;
        double scale = (panelHeight - top - bottom) / (maxValue - minValue);
        y = panelHeight - labelFontMetrics.getDescent();
        g.setFont(labelFont);

        for (int i = 0; i < values.length; i++) {
            int valueX = i * barWidth + 1;
            int valueY = top;
            int height = (int) (values[i] * scale);
            if (values[i] >= 0)
                valueY += (int) ((maxValue - values[i]) * scale);
            else {
                valueY += (int) (maxValue * scale);
                height = -height;
            }

            g.setColor(colors[i % colors.length]);
            g.fillRect(valueX, valueY, barWidth - 2, height);
            g.setColor(Color.black);
            g.drawRect(valueX, valueY, barWidth - 2, height);
            int labelWidth = labelFontMetrics.stringWidth(labels[i]);
            x = i * barWidth + (barWidth - labelWidth) / 2;
            g.drawString(labels[i], x, y);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Map<String, Integer> data = DatabaseHelper.getPersonnelStatusCount();
        frame.getContentPane().add(new BarChartExample(data, "Personnel Status Count"));

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}