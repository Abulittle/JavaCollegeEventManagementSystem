package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

import interfaces.Organisers;
import interfaces.Student;

public class Main extends JFrame {


private final JPanel panel;

private static final Color BACKGROUND = new Color(18, 18, 18);
private static final Color RED = new Color(230, 55, 55);
private static final Color WHITE = new Color(245, 245, 245);
private static final Color LIGHT_GRAY = new Color(170, 170, 170);

public Main() {
    setTitle("College Event Management System");
    setSize(1200, 800);
    setLocationRelativeTo(null);

    panel = new JPanel(new BorderLayout());
    panel.setBackground(BACKGROUND);

    add(panel, BorderLayout.CENTER);

    setDefaultCloseOperation(EXIT_ON_CLOSE);

    start();
}

public void start() {
    panel.removeAll();

    JPanel header = new JPanel(new BorderLayout());

    header.setBackground(BACKGROUND);

    header.setBorder(
        BorderFactory.createEmptyBorder(
            45, 35, 25, 35
        )
    );

    JLabel title = new JLabel(
        "COLLEGE EVENT MANAGEMENT SYSTEM",
        SwingConstants.CENTER
    );

    title.setFont(
        new Font(
            "Arial",
            Font.BOLD,
            32
        )
    );

    title.setForeground(RED);

    header.add(
        title,
        BorderLayout.CENTER
    );

    panel.add(
        header,
        BorderLayout.NORTH
    );

    JPanel homePanel =
        new JPanel(new GridBagLayout());

    homePanel.setBackground(BACKGROUND);

    GridBagConstraints gbc =
        new GridBagConstraints();

    gbc.insets =
        new Insets(
            10, 10, 10, 10
        );

    JLabel welcome =
        new JLabel(
            "Welcome to the Event Management System",
            SwingConstants.CENTER
        );

    welcome.setFont(
        new Font(
            "Arial",
            Font.BOLD,
            24
        )
    );

    welcome.setForeground(WHITE);

    gbc.gridx = 0;
    gbc.gridy = 0;

    homePanel.add(
        welcome,
        gbc
    );

    JLabel description =
        new JLabel(
            "Choose your portal to continue",
            SwingConstants.CENTER
        );

    description.setFont(
        new Font(
            "Arial",
            Font.PLAIN,
            16
        )
    );

    description.setForeground(LIGHT_GRAY);

    gbc.gridy = 1;

    homePanel.add(
        description,
        gbc
    );

    JButton organiserButton =
        new JButton("ORGANISER");

    styleMainButton(
        organiserButton
    );

    gbc.gridy = 2;

    homePanel.add(
        organiserButton,
        gbc
    );

    JButton studentButton =
        new JButton("STUDENT");

    styleMainButton(
        studentButton
    );

    gbc.gridy = 3;

    homePanel.add(
        studentButton,
        gbc
    );

    panel.add(
        homePanel,
        BorderLayout.CENTER
    );

    organiserButton.addActionListener(e -> {

        Organisers organiser = new Organisers(this);

        organiser.loginPage();

        organiser.setVisible(true);

        dispose();
    });

    studentButton.addActionListener(e -> {

        Student student =
            new Student(this);

        student.loginPage();

        student.setVisible(true);

        dispose();
    });

    panel.revalidate();
    panel.repaint();
}

private void styleMainButton(
    JButton button
) {
    button.setFont(
        new Font(
            "Arial",
            Font.BOLD,
            18
        )
    );

    button.setPreferredSize(
        new Dimension(
            260,
            55
        )
    );

    button.setFocusPainted(false);

    button.putClientProperty(
        "JButton.buttonType",
        "roundRect"
    );
}

public static void main(String[] args) {

    try {
        UIManager.put(
            "Button.arc",
            15
        );

        UIManager.put(
            "Component.arc",
            12
        );

        UIManager.put(
            "TextComponent.arc",
            10
        );

        UIManager.put(
            "Button.margin",
            new Insets(
                10, 20, 10, 20
            )
        );

        FlatDarkLaf.setup();

    } catch (Exception e) {
        e.printStackTrace();
    }

    SwingUtilities.invokeLater(() -> {
        Main eventManager =
            new Main();

        eventManager.setVisible(true);
    });
}

}
