package DataAnalysis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class AbstractBarChart extends JPanel {
    private static final long serialVersionUID = -4175998745125394822L;
    protected int[] values;
    protected String[] labels;
    protected Color[] colors;
    protected String title;

    public AbstractBarChart(Map<String, Integer> data, String title) {
        setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        setLayout(null);
        this.values = data.values().stream().mapToInt(Integer::intValue).toArray();
        this.labels = data.keySet().toArray(String[]::new);
        this.colors = new Color[]{Color.red, Color.blue, Color.green, Color.yellow, Color.orange};
        this.title = title;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (values == null || values.length == 0) return;

        double minValue = 0, maxValue = 0;
        for (int v : values) {
            if (minValue > v) minValue = v;
            if (maxValue < v) maxValue = v;
        }

        Dimension dim = getSize();
        int panelWidth = dim.width, panelHeight = dim.height;
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

        g.setFont(labelFont);
        g.drawString("Count", 10, panelHeight / 2);

        int top = titleFontMetrics.getHeight();
        int bottom = labelFontMetrics.getHeight();
        if (maxValue == minValue) return;
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

    public void display() {
        JFrame frame = new JFrame();
        frame.getContentPane().add(this);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
