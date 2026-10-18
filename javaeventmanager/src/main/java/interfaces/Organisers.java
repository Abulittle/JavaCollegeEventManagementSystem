package interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import functions.DBConnection;
import functions.Event;
import main.Main;

public class Organisers extends JFrame {

    private final JPanel panel;
    private final Main main;

    private String organiserId;
    private String organiserName;

    private static final Color BACKGROUND =
        new Color(18, 18, 18);

    private static final Color CARD =
        new Color(28, 28, 28);

    private static final Color RED =
        new Color(230, 55, 55);

    private static final Color WHITE =
        new Color(245, 245, 245);

    public Organisers(Main main) {

        this.main = main;

        setTitle(
            "College Event Management System"
        );

        setSize(1200, 800);

        setLocationRelativeTo(null);

        panel =
            new JPanel(
                new BorderLayout()
            );

        panel.setBackground(
            BACKGROUND
        );

        add(
            panel,
            BorderLayout.CENTER
        );

        setDefaultCloseOperation(
            DISPOSE_ON_CLOSE
        );
    }

    protected JButton createBackButton(
        Runnable action
    ) {

        JButton backButton =
            new JButton("← Back");

        backButton.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        backButton.setFocusPainted(false);

        backButton.addActionListener(
            e -> action.run()
        );

        return backButton;
    }

    private JPanel createHeader(
        String title,
        Runnable backAction
    ) {

        JPanel topPanel =
            new JPanel(
                new GridLayout(1, 3)
            );

        topPanel.setBackground(
            BACKGROUND
        );

        topPanel.setBorder(
            BorderFactory.createEmptyBorder(
                25,
                35,
                25,
                35
            )
        );

        JPanel leftPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.LEFT,
                    0,
                    0
                )
            );

        JPanel centerPanel =
            new JPanel(
                new GridBagLayout()
            );

        JPanel rightPanel =
            new JPanel();

        leftPanel.setOpaque(false);
        centerPanel.setOpaque(false);
        rightPanel.setOpaque(false);

        if (backAction != null) {

            leftPanel.add(
                createBackButton(
                    backAction
                )
            );
        }

        JLabel titleLabel =
            new JLabel(
                title,
                SwingConstants.CENTER
            );

        titleLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                30
            )
        );

        titleLabel.setForeground(
            RED
        );

        centerPanel.add(
            titleLabel
        );

        topPanel.add(leftPanel);
        topPanel.add(centerPanel);
        topPanel.add(rightPanel);

        return topPanel;
    }

    public void loginPage() {

        panel.removeAll();

        panel.add(
            createHeader(
                "ORGANISER LOGIN",
                () -> {
                    main.start();
                    main.setVisible(true);
                    dispose();
                }
            ),
            BorderLayout.NORTH
        );

        JPanel container =
            new JPanel(
                new GridBagLayout()
            );

        container.setBackground(
            BACKGROUND
        );

        JPanel loginPanel =
            new JPanel(
                new GridBagLayout()
            );

        loginPanel.setBackground(
            CARD
        );

        loginPanel.setBorder(
            BorderFactory.createEmptyBorder(
                40,
                55,
                40,
                55
            )
        );

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets =
            new Insets(
                10,
                12,
                10,
                12
            );

        gbc.fill =
            GridBagConstraints.HORIZONTAL;

        JLabel username =
            new JLabel(
                "ORGANISER ID"
            );

        username.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        username.setForeground(
            WHITE
        );

        JTextField usernameText =
            new JTextField(18);

        usernameText.putClientProperty(
            "JTextField.placeholderText",
            "Enter organiser ID"
        );

        gbc.gridx = 0;
        gbc.gridy = 0;

        loginPanel.add(
            username,
            gbc
        );

        gbc.gridx = 1;

        loginPanel.add(
            usernameText,
            gbc
        );

        JLabel password =
            new JLabel(
                "PASSWORD"
            );

        password.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        password.setForeground(
            WHITE
        );

        JPasswordField passwordText =
            new JPasswordField(18);

        passwordText.putClientProperty(
            "JTextField.placeholderText",
            "Enter password"
        );

        gbc.gridx = 0;
        gbc.gridy = 1;

        loginPanel.add(
            password,
            gbc
        );

        gbc.gridx = 1;

        loginPanel.add(
            passwordText,
            gbc
        );

        JButton login =
            new JButton(
                "LOGIN"
            );

        login.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                15
            )
        );

        login.setPreferredSize(
            new Dimension(
                140,
                42
            )
        );

        login.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;

        gbc.anchor =
            GridBagConstraints.CENTER;

        loginPanel.add(
            login,
            gbc
        );

        container.add(
            loginPanel
        );

        panel.add(
            container,
            BorderLayout.CENTER
        );

        login.addActionListener(e -> {

            String enteredId =
                usernameText
                    .getText()
                    .trim();

            String enteredPassword =
                new String(
                    passwordText
                        .getPassword()
                );

            if (
                enteredId.equals("admin") &&
                enteredPassword.equals("admin")
            ) {

                Admin admin =
                    new Admin(this);

                setVisible(false);

                admin.setVisible(true);

                admin.homePage();

                return;
            }

            if (
                enteredId.isEmpty() ||
                enteredPassword.isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                    this,
                    "Enter organiser ID and password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            String sql =
                "SELECT organiser_id, organiser_name " +
                "FROM organisers " +
                "WHERE organiser_id = ? " +
                "AND password = ?";

            try (
                Connection connection =
                    DBConnection.getConnection();

                PreparedStatement statement =
                    connection.prepareStatement(
                        sql
                    )
            ) {

                statement.setString(
                    1,
                    enteredId
                );

                statement.setString(
                    2,
                    enteredPassword
                );

                ResultSet result =
                    statement.executeQuery();

                if (result.next()) {

                    organiserId =
                        result.getString(
                            "organiser_id"
                        );

                    organiserName =
                        result.getString(
                            "organiser_name"
                        );

                    homePage();

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Invalid organiser ID or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "Database connection failed",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    public void homePage() {

        panel.removeAll();

        panel.add(
            createHeader(
                "ORGANISER PANEL",
                () -> loginPage()
            ),
            BorderLayout.NORTH
        );

        JPanel homePanel =
            new JPanel(
                new GridBagLayout()
            );

        homePanel.setBackground(
            BACKGROUND
        );

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets =
            new Insets(
                12,
                12,
                12,
                12
            );

        JLabel welcome =
            new JLabel(
                "Welcome, " + organiserName
            );

        welcome.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                20
            )
        );

        welcome.setForeground(
            WHITE
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;

        homePanel.add(
            welcome,
            gbc
        );

        JButton addEvents =
            new JButton(
                "Add Events"
            );

        styleMainButton(
            addEvents
        );

        gbc.gridy = 1;

        homePanel.add(
            addEvents,
            gbc
        );

        JButton deleteEvents =
            new JButton(
                "Delete Events"
            );

        styleMainButton(
            deleteEvents
        );

        gbc.gridy = 2;

        homePanel.add(
            deleteEvents,
            gbc
        );

        JButton viewRegistrations =
            new JButton(
                "View Registrations"
            );

        styleMainButton(
            viewRegistrations
        );

        gbc.gridy = 3;

        homePanel.add(
            viewRegistrations,
            gbc
        );

        panel.add(
            homePanel,
            BorderLayout.CENTER
        );

        addEvents.addActionListener(
            e -> addEvents()
        );

        deleteEvents.addActionListener(
            e -> deleteEvents(false)
        );

        viewRegistrations.addActionListener(
            e -> viewRegistrations()
        );

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
                17
            )
        );

        button.setPreferredSize(
            new Dimension(
                240,
                50
            )
        );

        button.setFocusPainted(false);

        button.putClientProperty(
            "JButton.buttonType",
            "roundRect"
        );
    }

    public void addEvents() {

        panel.removeAll();

        panel.add(
            createHeader(
                "ADD EVENTS",
                () -> homePage()
            ),
            BorderLayout.NORTH
        );

        JPanel formPanel =
            new JPanel(
                new GridBagLayout()
            );

        formPanel.setBackground(
            BACKGROUND
        );

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets =
            new Insets(
                8,
                8,
                8,
                8
            );

        gbc.fill =
            GridBagConstraints.HORIZONTAL;

        JLabel eventId =
            createLabel(
                "Event ID"
            );

        JTextField idText =
            new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
            eventId,
            gbc
        );

        gbc.gridx = 1;

        formPanel.add(
            idText,
            gbc
        );

        JLabel eventName =
            createLabel(
                "Event Name"
            );

        JTextField eventText =
            new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 1;

        formPanel.add(
            eventName,
            gbc
        );

        gbc.gridx = 1;

        formPanel.add(
            eventText,
            gbc
        );

        JLabel date =
            createLabel(
                "Date"
            );

        JTextField dateText =
            new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 2;

        formPanel.add(
            date,
            gbc
        );

        gbc.gridx = 1;

        formPanel.add(
            dateText,
            gbc
        );

        JLabel venue =
            createLabel(
                "Venue"
            );

        JTextField venueText =
            new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 3;

        formPanel.add(
            venue,
            gbc
        );

        gbc.gridx = 1;

        formPanel.add(
            venueText,
            gbc
        );

        JButton addButton =
            new JButton(
                "ADD EVENT"
            );

        addButton.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        addButton.setFocusPainted(false);

        addButton.putClientProperty(
            "JButton.buttonType",
            "roundRect"
        );

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        gbc.anchor =
            GridBagConstraints.CENTER;

        formPanel.add(
            addButton,
            gbc
        );

        panel.add(
            formPanel,
            BorderLayout.CENTER
        );

        addButton.addActionListener(e -> {

            String id =
                idText.getText().trim();

            String name =
                eventText.getText().trim();

            String dateValue =
                dateText.getText().trim();

            String venueValue =
                venueText.getText().trim();

            if (
                id.isEmpty() ||
                name.isEmpty() ||
                dateValue.isEmpty() ||
                venueValue.isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                    this,
                    "Fill all fields",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            Event event =
                new Event(
                    id,
                    dateValue,
                    name,
                    venueValue,
                    organiserId
                );

            event.addEvent();

            idText.setText("");
            eventText.setText("");
            dateText.setText("");
            venueText.setText("");
        });

        panel.revalidate();
        panel.repaint();
    }

    public void deleteEvents(boolean set) {

        panel.removeAll();

        panel.add(
            createHeader(
                "DELETE EVENTS",
                () -> homePage()
            ),
            BorderLayout.NORTH
        );

        JPanel formPanel =
            new JPanel(
                new GridBagLayout()
            );

        formPanel.setBackground(
            BACKGROUND
        );

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets =
            new Insets(
                10,
                10,
                10,
                10
            );

        gbc.fill =
            GridBagConstraints.HORIZONTAL;

        JLabel eventId =
            createLabel(
                "Event ID"
            );

        JTextField deleteText =
            new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
            eventId,
            gbc
        );

        gbc.gridx = 1;

        formPanel.add(
            deleteText,
            gbc
        );

        JButton deleteButton =
            new JButton(
                "DELETE EVENT"
            );

        deleteButton.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        deleteButton.setFocusPainted(false);

        deleteButton.putClientProperty(
            "JButton.buttonType",
            "roundRect"
        );

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        gbc.anchor =
            GridBagConstraints.CENTER;

        formPanel.add(
            deleteButton,
            gbc
        );

        panel.add(
            formPanel,
            BorderLayout.CENTER
        );

        deleteButton.addActionListener(e -> {

            String id =
                deleteText.getText().trim();

            if (id.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Enter an event ID",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            Event event =
                new Event(
                    id,
                    "",
                    "",
                    "",
                    organiserId
                );

            int rows =
                event.deleteEvent(set);

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Event deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Event not found.",
                    "Delete Event",
                    JOptionPane.WARNING_MESSAGE
                );
            }

            deleteText.setText("");
        });

        panel.revalidate();
        panel.repaint();
    }

    public void viewRegistrations() {

    panel.removeAll();

    panel.add(
        createHeader(
            "EVENT REGISTRATIONS",
            () -> homePage()
        ),
        BorderLayout.NORTH
    );

    JPanel contentPanel =
        new JPanel(
            new BorderLayout()
        );

    contentPanel.setBackground(
        BACKGROUND
    );

    JPanel formPanel =
        new JPanel(
            new GridBagLayout()
        );

    formPanel.setBackground(
        BACKGROUND
    );

    GridBagConstraints gbc =
        new GridBagConstraints();

    gbc.insets =
        new Insets(
            10,
            10,
            10,
            10
        );

    gbc.fill =
        GridBagConstraints.HORIZONTAL;

    JLabel eventIdLabel =
        createLabel(
            "Event ID"
        );

    JTextField eventIdText =
        new JTextField(15);

    gbc.gridx = 0;
    gbc.gridy = 0;

    formPanel.add(
        eventIdLabel,
        gbc
    );

    gbc.gridx = 1;

    formPanel.add(
        eventIdText,
        gbc
    );

    JButton viewButton =
        new JButton(
            "VIEW STUDENTS"
        );

    viewButton.setFont(
        new Font(
            "Arial",
            Font.BOLD,
            14
        )
    );

    viewButton.setFocusPainted(false);

    viewButton.putClientProperty(
        "JButton.buttonType",
        "roundRect"
    );

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;

    gbc.anchor =
        GridBagConstraints.CENTER;

    formPanel.add(
        viewButton,
        gbc
    );

    contentPanel.add(
        formPanel,
        BorderLayout.NORTH
    );

    JPanel resultsPanel =
        new JPanel(
            new BorderLayout()
        );

    resultsPanel.setBackground(
        BACKGROUND
    );

    contentPanel.add(
        resultsPanel,
        BorderLayout.CENTER
    );

    panel.add(
        contentPanel,
        BorderLayout.CENTER
    );

        viewButton.addActionListener(e -> {

            String eventId =
                eventIdText
                    .getText()
                    .trim();

            if (eventId.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Enter an event ID",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            String eventSql =
                "SELECT event_name, event_date, venue " +
                "FROM events " +
                "WHERE event_id = ? " +
                "AND organiser_id = ?";

            String studentSql =
                "SELECT s.student_id, " +
                "s.student_name, " +
                "s.email, " +
                "r.registration_date " +
                "FROM registrations r " +
                "JOIN students s " +
                "ON r.student_id = s.student_id " +
                "WHERE r.event_id = ? " +
                "ORDER BY s.student_name";

            try (
                Connection connection =
                    DBConnection.getConnection();

                PreparedStatement eventStatement =
                    connection.prepareStatement(
                        eventSql
                    )
            ) {

                eventStatement.setString(
                    1,
                    eventId
                );

                eventStatement.setString(
                    2,
                    organiserId
                );

                ResultSet eventResult =
                    eventStatement.executeQuery();

                if (!eventResult.next()) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Event not found or you do not have access to this event.",
                        "Event Not Found",
                        JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                String eventName =
                    eventResult.getString(
                        "event_name"
                    );

                String eventDate =
                    eventResult.getString(
                        "event_date"
                    );

                String venue =
                    eventResult.getString(
                        "venue"
                    );

                JPanel resultContent =
                    new JPanel(
                        new BorderLayout()
                    );

                resultContent.setBackground(
                    BACKGROUND
                );

                resultContent.setBorder(
                    BorderFactory.createEmptyBorder(
                        20,
                        70,
                        20,
                        70
                    )
                );

                JLabel eventInfo =
                    new JLabel(
                        "<html><b>"
                        + eventName
                        + "</b> &nbsp;&nbsp; "
                        + eventDate
                        + " &nbsp;&nbsp; "
                        + venue
                        + "</html>"
                    );

                eventInfo.setForeground(
                    WHITE
                );

                eventInfo.setFont(
                    new Font(
                        "Arial",
                        Font.PLAIN,
                        17
                    )
                );

                resultContent.add(
                    eventInfo,
                    BorderLayout.NORTH
                );

                JPanel studentsPanel =
                    new JPanel(
                        new GridLayout(
                            0,
                            4
                        )
                    );

                studentsPanel.setBackground(
                    BACKGROUND
                );

                addTableLabel(
                    studentsPanel,
                    "STUDENT ID"
                );

                addTableLabel(
                    studentsPanel,
                    "NAME"
                );

                addTableLabel(
                    studentsPanel,
                    "EMAIL"
                );

                addTableLabel(
                    studentsPanel,
                    "REGISTERED ON"
                );

                try (
                    PreparedStatement studentStatement =
                        connection.prepareStatement(
                            studentSql
                        )
                ) {

                    studentStatement.setString(
                        1,
                        eventId
                    );

                    ResultSet studentResult =
                        studentStatement.executeQuery();

                    int count = 0;

                    while (
                        studentResult.next()
                    ) {

                        addTableLabel(
                            studentsPanel,
                            studentResult.getString(
                                "student_id"
                            )
                        );

                        addTableLabel(
                            studentsPanel,
                            studentResult.getString(
                                "student_name"
                            )
                        );

                        addTableLabel(
                            studentsPanel,
                            studentResult.getString(
                                "email"
                            )
                        );

                        addTableLabel(
                            studentsPanel,
                            studentResult.getString(
                                "registration_date"
                            )
                        );

                        count++;
                    }

                    if (count == 0) {

                        JLabel noStudents =
                            new JLabel(
                                "No students registered for this event.",
                                SwingConstants.CENTER
                            );

                        noStudents.setForeground(
                            WHITE
                        );

                        noStudents.setFont(
                            new Font(
                                "Arial",
                                Font.PLAIN,
                                16
                            )
                        );

                        resultContent.add(
                            noStudents,
                            BorderLayout.CENTER
                        );

                    } else {

                        JScrollPane scrollPane =
                            new JScrollPane(
                                studentsPanel
                            );

                        scrollPane.setBorder(
                            null
                        );

                        scrollPane
                            .getVerticalScrollBar()
                            .setUnitIncrement(
                                16
                            );

                        resultContent.add(
                            scrollPane,
                            BorderLayout.CENTER
                        );
                    }
                }

                resultsPanel.removeAll();

                resultsPanel.add(
                    resultContent,
                    BorderLayout.CENTER
                );

                resultsPanel.revalidate();
                resultsPanel.repaint();

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "Failed to retrieve registrations.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private JLabel createLabel(
        String text
    ) {

        JLabel label =
            new JLabel(text);

        label.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        label.setForeground(
            WHITE
        );

        return label;
    }

    private void addTableLabel(
        JPanel panel,
        String text
    ) {

        JLabel label =
            new JLabel(
                text,
                SwingConstants.CENTER
            );

        label.setForeground(
            WHITE
        );

        label.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        label.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(50, 50, 50)
                ),
                BorderFactory.createEmptyBorder(
                    10,
                    5,
                    10,
                    5
                )
            )
        );

        panel.add(label);
    }
}
