package com.warManagementGUI.util;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public abstract class AbstractDetailsFrame extends JFrame {
    protected static final Font LABEL_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20);
    protected static final Font BUTTON_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15);
    protected static final Color TEXT_COLOR = Color.WHITE;
    protected static final Color BG_COLOR = new Color(0, 64, 64);
    protected JPanel contentPane;

    public AbstractDetailsFrame(String title, int width, int height) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, width, height);
        contentPane = new JPanel();
        contentPane.setBackground(BG_COLOR);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);
    }

    protected JLabel createLabel(String text, int x, int y, int w, int h) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(TEXT_COLOR);
        lbl.setBounds(x, y, w, h);
        contentPane.add(lbl);
        return lbl;
    }

    /**
     * Create a label with custom font.
     */
    protected JLabel createLabel(String text, Font font, int x, int y, int w, int h) {
        JLabel lbl = createLabel(text, x, y, w, h);
        lbl.setFont(font);
        return lbl;
    }

    /**
     * Create a label with custom font and color.
     */
    protected JLabel createLabel(String text, Font font, Color fg, int x, int y, int w, int h) {
        JLabel lbl = createLabel(text, x, y, w, h);
        lbl.setFont(font);
        lbl.setForeground(fg);
        return lbl;
    }

    /**
     * Create an icon label from resource path.
     */
    protected JLabel createIconLabel(String path, int x, int y, int w, int h) {
        JLabel lbl = new JLabel();
        lbl.setIcon(new ImageIcon(getClass().getResource(path)));
        lbl.setBounds(x, y, w, h);
        contentPane.add(lbl);
        return lbl;
    }

    protected JTextField createTextField(int x, int y, int w, int h) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, h);
        tf.setColumns(10);
        contentPane.add(tf);
        return tf;
    }

    /**
     * Create a text field with custom column width.
     */
    protected JTextField createTextField(int x, int y, int w, int h, int columns) {
        JTextField tf = createTextField(x, y, w, h);
        tf.setColumns(columns);
        return tf;
    }

    protected JButton createButton(String text, int x, int y, int w, int h, ActionListener l) {
        JButton btn = new JButton(text);
        btn.setFont(BUTTON_FONT);
        btn.setBackground(Color.BLACK);
        btn.setForeground(TEXT_COLOR);
        btn.setBounds(x, y, w, h);
        btn.addActionListener(l);
        contentPane.add(btn);
        return btn;
    }

    /**
     * Create a button with custom font and default text/ background colors.
     */
    protected JButton createButton(String text, Font font, int x, int y, int w, int h, ActionListener l) {
        return createButton(text, font, TEXT_COLOR, Color.BLACK, x, y, w, h, l);
    }

    /**
     * Create a button with custom font and colors.
     */
    protected JButton createButton(String text, Font font, Color fg, Color bg, int x, int y, int w, int h, ActionListener l) {
        JButton btn = new JButton(text);
        btn.setFont(font);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setBounds(x, y, w, h);
        btn.addActionListener(l);
        contentPane.add(btn);
        return btn;
    }

    /**
     * Create a combo box with default font and colors.
     */
    protected JComboBox<String> createComboBox(String[] items, int x, int y, int w, int h) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(LABEL_FONT);
        cb.setForeground(TEXT_COLOR);
        cb.setBackground(Color.WHITE);
        cb.setBounds(x, y, w, h);
        contentPane.add(cb);
        return cb;
    }

    /**
     * Create a centered text label.
     */
    protected JLabel createTextLabel(String text, Font font, Color fg, int x, int y, int w, int h) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(font);
        lbl.setForeground(fg);
        lbl.setBounds(x, y, w, h);
        contentPane.add(lbl);
        return lbl;
    }

    protected void handleDatabaseError(Exception e, String message) {
        System.err.println(message + ": " + e.getMessage());
        JOptionPane.showMessageDialog(this, message + ": " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    public void display() {
        EventQueue.invokeLater(() -> setVisible(true));
    }
}
