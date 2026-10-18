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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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

public class Student extends JFrame {

    private final JPanel panel;
    private String studentId;
    private String studentName;

    private static final Color BACKGROUND = new Color(18, 18, 18);
    private static final Color CARD = new Color(28, 28, 28);
    private static final Color RED = new Color(230, 55, 55);
    private static final Color WHITE = new Color(245, 245, 245);
    private static final Color LIGHT_GRAY = new Color(170, 170, 170);

    private final Main main;

    public Student(Main main) {
        this.main = main;

        setTitle("College Event Management System");
        setSize(1200, 800);
        setLocationRelativeTo(null);

        panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);

        add(panel, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    protected JButton createBackButton(Runnable action) {
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> action.run());
        return backButton;
    }

    private JPanel createHeader(String title, Runnable backAction) {
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.setBackground(BACKGROUND);
        topPanel.setBorder(
            BorderFactory.createEmptyBorder(25, 35, 25, 35)
        );

        JPanel leftPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 0, 0)
        );

        JPanel centerPanel = new JPanel(
            new GridBagLayout()
        );

        JPanel rightPanel = new JPanel();

        leftPanel.setOpaque(false);
        centerPanel.setOpaque(false);
        rightPanel.setOpaque(false);

        if (backAction != null) {
            leftPanel.add(createBackButton(backAction));
        }

        JLabel titleLabel = new JLabel(
            title,
            SwingConstants.CENTER
        );

        titleLabel.setFont(
            new Font("Arial", Font.BOLD, 30)
        );

        titleLabel.setForeground(RED);

        centerPanel.add(titleLabel);

        topPanel.add(leftPanel);
        topPanel.add(centerPanel);
        topPanel.add(rightPanel);

        return topPanel;
    }

    public void loginPage() {
        panel.removeAll();

        panel.add(
            createHeader(
                "STUDENT LOGIN",
                () -> {
                    main.start();
                    main.setVisible(true);
                    dispose();
                }
            ),
            BorderLayout.NORTH
        );

        JPanel container = new JPanel(
            new GridBagLayout()
        );

        container.setBackground(BACKGROUND);

        JPanel loginPanel = new JPanel(
            new GridBagLayout()
        );

        loginPanel.setBackground(CARD);

        loginPanel.setBorder(
            BorderFactory.createEmptyBorder(
                40, 55, 40, 55
            )
        );

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets =
            new Insets(10, 12, 10, 12);

        gbc.fill =
            GridBagConstraints.HORIZONTAL;

        JLabel loginTitle =
            new JLabel("Welcome Student");

        loginTitle.setFont(
            new Font("Arial", Font.BOLD, 22)
        );

        loginTitle.setForeground(WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        loginPanel.add(
            loginTitle,
            gbc
        );

        JLabel username =
            new JLabel("Student ID");

        username.setForeground(WHITE);

        username.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        JTextField usernameText =
            new JTextField(18);

        usernameText.putClientProperty(
            "JTextField.placeholderText",
            "Enter student ID"
        );

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;

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
            new JLabel("Password");

        password.setForeground(WHITE);

        password.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        JPasswordField passwordText =
            new JPasswordField(18);

        passwordText.putClientProperty(
            "JTextField.placeholderText",
            "Enter password"
        );

        gbc.gridy = 2;
        gbc.gridx = 0;

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
            new JButton("LOGIN");

        login.setFont(
            new Font("Arial", Font.BOLD, 15)
        );

        login.setPreferredSize(
            new Dimension(140, 42)
        );

        login.setFocusPainted(false);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        loginPanel.add(
            login,
            gbc
        );

        container.add(loginPanel);

        panel.add(
            container,
            BorderLayout.CENTER
        );

        login.addActionListener(e -> {

            String enteredId =
                usernameText.getText().trim();

            String enteredPassword =
                new String(
                    passwordText.getPassword()
                );

            if (
                enteredId.isEmpty() ||
                enteredPassword.isEmpty()
            ) {
                JOptionPane.showMessageDialog(
                    this,
                    "Enter your student ID and password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            String sql =
                "SELECT student_id, student_name " +
                "FROM students " +
                "WHERE student_id = ? AND password = ?";

            try (
                Connection connection =
                    DBConnection.getConnection();

                PreparedStatement statement =
                    connection.prepareStatement(sql)
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

                    studentId =
                        result.getString(
                            "student_id"
                        );

                    studentName =
                        result.getString(
                            "student_name"
                        );

                    homePage();

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Invalid student ID or password",
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
                "STUDENT PORTAL",
                () -> loginPage()
            ),
            BorderLayout.NORTH
        );

        JPanel content =
            new JPanel(new GridBagLayout());

        content.setBackground(BACKGROUND);

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets =
            new Insets(10, 10, 10, 10);

        gbc.fill =
            GridBagConstraints.HORIZONTAL;

        String name =
            studentName == null ||
            studentName.isEmpty()
                ? "Student"
                : studentName;

        JLabel welcome =
            new JLabel(
                "Welcome, " + name,
                SwingConstants.CENTER
            );

        welcome.setForeground(WHITE);

        welcome.setFont(
            new Font("Arial", Font.BOLD, 24)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        content.add(
            welcome,
            gbc
        );

        JLabel description =
            new JLabel(
                "Manage your college events",
                SwingConstants.CENTER
            );

        description.setForeground(
            LIGHT_GRAY
        );

        description.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                15
            )
        );

        gbc.gridy = 1;

        content.add(
            description,
            gbc
        );

        JButton registerButton =
            new JButton(
                "Register For Events"
            );

        JButton searchButton =
            new JButton(
                "Search Events"
            );

        JButton viewButton =
            new JButton(
                "View Events"
            );

        JButton myRegistrationsButton =
            new JButton("My Registrations");

        styleMainButton(
            registerButton
        );

        styleMainButton(
            searchButton
        );

        styleMainButton(
            viewButton
        );

        styleMainButton(myRegistrationsButton);

        gbc.gridy = 2;
        gbc.gridwidth = 1;

        content.add(
            registerButton,
            gbc
        );

        gbc.gridy = 3;

        content.add(
            searchButton,
            gbc
        );

        gbc.gridy = 4;

        content.add(
            viewButton,
            gbc
        );

        gbc.gridy = 5;
        content.add(myRegistrationsButton, gbc);

        panel.add(
            content,
            BorderLayout.CENTER
        );

        registerButton.addActionListener(
            e -> registerEvents()
        );

        searchButton.addActionListener(
            e -> searchEvents()
        );

        viewButton.addActionListener(
            e -> viewEvents()
        );

        myRegistrationsButton.addActionListener(
            e -> myRegistrations()
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
                16
            )
        );

        button.setPreferredSize(
            new Dimension(260, 52)
        );

        button.setFocusPainted(false);

        button.putClientProperty(
            "JButton.buttonType",
            "roundRect"
        );
    }

    public void viewEvents() {
        panel.removeAll();

        panel.add(
            createHeader(
                "EVENT LIST",
                () -> homePage()
            ),
            BorderLayout.NORTH
        );

        Event view =
            new Event(
                "",
                "",
                "",
                "",
                ""
            );

        ArrayList<Event> events =
            view.viewEvents();

        JPanel eventsPanel =
            new JPanel();

        eventsPanel.setLayout(
            new BoxLayout(
                eventsPanel,
                BoxLayout.Y_AXIS
            )
        );

        eventsPanel.setBackground(
            BACKGROUND
        );

        eventsPanel.setBorder(
            BorderFactory.createEmptyBorder(
                20,
                70,
                20,
                70
            )
        );

        JPanel columnHeader =
            new JPanel(
                new GridLayout(1, 5)
            );

        columnHeader.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                45
            )
        );

        columnHeader.setBackground(
            CARD
        );

        addTableLabel(
            columnHeader,
            "ID"
        );

        addTableLabel(
            columnHeader,
            "EVENT"
        );

        addTableLabel(
            columnHeader,
            "DATE"
        );

        addTableLabel(
            columnHeader,
            "VENUE"
        );

        addTableLabel(
            columnHeader,
            "ORGANISER"
        );

        eventsPanel.add(
            columnHeader
        );

        eventsPanel.add(
            Box.createVerticalStrut(5)
        );

        for (Event e : events) {

            JPanel eventRow =
                new JPanel(
                    new GridLayout(1, 5)
                );

            eventRow.setMaximumSize(
                new Dimension(
                    Integer.MAX_VALUE,
                    48
                )
            );

            eventRow.setBackground(
                new Color(35, 35, 35)
            );

            addTableLabel(
                eventRow,
                e.getEventId()
            );

            addTableLabel(
                eventRow,
                e.getEventname()
            );

            addTableLabel(
                eventRow,
                e.getEventDate()
            );

            addTableLabel(
                eventRow,
                e.getVenue()
            );

            addTableLabel(
                eventRow,
                e.getOrganiser()
            );

            eventsPanel.add(
                eventRow
            );

            eventsPanel.add(
                Box.createVerticalStrut(3)
            );
        }

        JScrollPane scrollPane =
            new JScrollPane(
                eventsPanel
            );

        scrollPane.setBorder(null);

        scrollPane
            .getVerticalScrollBar()
            .setUnitIncrement(16);

        panel.add(
            scrollPane,
            BorderLayout.CENTER
        );

        panel.revalidate();
        panel.repaint();
    }

    private void searchEvents() {
        panel.removeAll();
        panel.add(createHeader("SEARCH EVENTS", () -> homePage()), BorderLayout.NORTH);

        Event eventView = new Event("", "", "", "", "");
        ArrayList<Event> events = eventView.viewEvents();

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBackground(BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(20, 70, 20, 70));

        JPanel searchBar = new JPanel(new BorderLayout(10, 0));
        searchBar.setBackground(BACKGROUND);
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.putClientProperty("JTextField.placeholderText", "Search by event, venue, or organiser");
        JButton searchButton = new JButton("Search");
        searchButton.setFocusPainted(false);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchButton, BorderLayout.EAST);
        content.add(searchBar, BorderLayout.NORTH);

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(BACKGROUND);
        JLabel resultCount = new JLabel();
        resultCount.setForeground(LIGHT_GRAY);
        resultCount.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        JPanel resultContainer = new JPanel(new BorderLayout());
        resultContainer.setBackground(BACKGROUND);
        resultContainer.add(resultCount, BorderLayout.NORTH);
        resultContainer.add(resultsPanel, BorderLayout.CENTER);
        content.add(resultContainer, BorderLayout.CENTER);

        Runnable updateResults = () -> {
            String query = searchField.getText().trim().toLowerCase();
            resultsPanel.removeAll();
            int matchCount = 0;
            for (Event event : events) {
                String name = safeText(event.getEventname());
                String venue = safeText(event.getVenue());
                String organiser = safeText(event.getOrganiser());
                if (query.isEmpty() || name.toLowerCase().contains(query)
                    || venue.toLowerCase().contains(query)
                    || organiser.toLowerCase().contains(query)) {
                    JPanel row = new JPanel(new GridLayout(1, 5));
                    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
                    row.setBackground(new Color(35, 35, 35));
                    addTableLabel(row, safeText(event.getEventId()));
                    addTableLabel(row, name);
                    addTableLabel(row, safeText(event.getEventDate()));
                    addTableLabel(row, venue);
                    addTableLabel(row, organiser);
                    resultsPanel.add(row);
                    resultsPanel.add(Box.createVerticalStrut(3));
                    matchCount++;
                }
            }
            resultCount.setText(matchCount == 1 ? "1 event found" : matchCount + " events found");
            if (matchCount == 0) {
                JLabel emptyState = new JLabel("No events match your search.", SwingConstants.CENTER);
                emptyState.setForeground(LIGHT_GRAY);
                emptyState.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
                resultsPanel.add(emptyState);
            }
            resultsPanel.revalidate();
            resultsPanel.repaint();
        };

        searchButton.addActionListener(e -> updateResults.run());
        searchField.addActionListener(e -> updateResults.run());
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateResults.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateResults.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateResults.run(); }
        });
        updateResults.run();

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private String safeText(String value) {
        return value == null ? "" : value;
    }
    private void myRegistrations() {
        panel.removeAll();
        panel.add(
            createHeader("MY REGISTRATIONS", () -> homePage()),
            BorderLayout.NORTH
        );

        JPanel registrationsPanel = new JPanel();
        registrationsPanel.setLayout(new BoxLayout(registrationsPanel, BoxLayout.Y_AXIS));
        registrationsPanel.setBackground(BACKGROUND);
        registrationsPanel.setBorder(BorderFactory.createEmptyBorder(20, 70, 20, 70));

        String sql =
            "SELECT e.event_id, e.event_name, e.event_date, e.venue, o.organiser_name " +
            "FROM registrations r " +
            "JOIN events e ON r.event_id = e.event_id " +
            "JOIN organisers o ON e.organiser_id = o.organiser_id " +
            "WHERE r.student_id = ? " +
            "ORDER BY e.event_date";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, studentId);
            try (ResultSet result = statement.executeQuery()) {
                boolean hasRegistrations = false;
                while (result.next()) {
                    hasRegistrations = true;
                    String eventId = result.getString("event_id");
                    String eventName = result.getString("event_name");
                    String eventDate = result.getString("event_date");
                    String venue = result.getString("venue");
                    String organiser = result.getString("organiser_name");

                    JPanel row = new JPanel(new BorderLayout(12, 0));
                    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
                    row.setBackground(CARD);
                    row.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

                    JLabel eventInfo = new JLabel(
                        safeText(eventId) + "   " + safeText(eventName) + "   "
                            + safeText(eventDate) + "   " + safeText(venue) + "   "
                            + safeText(organiser)
                    );
                    eventInfo.setForeground(WHITE);
                    eventInfo.setFont(new Font("Arial", Font.PLAIN, 14));

                    JButton cancelButton = new JButton("CANCEL REGISTRATION");
                    cancelButton.setFocusPainted(false);
                    cancelButton.addActionListener(e -> cancelRegistration(eventId));

                    row.add(eventInfo, BorderLayout.CENTER);
                    row.add(cancelButton, BorderLayout.EAST);
                    registrationsPanel.add(row);
                    registrationsPanel.add(Box.createVerticalStrut(5));
                }

                if (!hasRegistrations) {
                    JLabel emptyState = new JLabel("You are not registered for any events.", SwingConstants.CENTER);
                    emptyState.setForeground(LIGHT_GRAY);
                    registrationsPanel.add(emptyState);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Could not load your registrations.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(registrationsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private void cancelRegistration(String eventId) {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Cancel your registration for this event?",
            "Cancel Registration",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM registrations WHERE event_id = ? AND student_id = ?";
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, eventId);
            statement.setString(2, studentId);
            int deleted = statement.executeUpdate();
            if (deleted > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Registration cancelled.",
                    "My Registrations",
                    JOptionPane.INFORMATION_MESSAGE
                );
                myRegistrations();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "This registration could not be found.",
                    "My Registrations",
                    JOptionPane.WARNING_MESSAGE
                );
                myRegistrations();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Could not cancel the registration.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
    private void registerEvents() {
        panel.removeAll();

        panel.add(
            createHeader(
                "REGISTER FOR EVENTS",
                () -> homePage()
            ),
            BorderLayout.NORTH
        );

        Event view =
            new Event(
                "",
                "",
                "",
                "",
                ""
            );

        ArrayList<Event> events =
            view.viewEvents();

        JPanel eventsPanel =
            new JPanel();

        eventsPanel.setLayout(
            new BoxLayout(
                eventsPanel,
                BoxLayout.Y_AXIS
            )
        );

        eventsPanel.setBackground(
            BACKGROUND
        );

        eventsPanel.setBorder(
            BorderFactory.createEmptyBorder(
                20,
                70,
                20,
                70
            )
        );

        for (Event event : events) {

            JPanel eventRow =
                new JPanel(
                    new BorderLayout()
                );

            eventRow.setMaximumSize(
                new Dimension(
                    Integer.MAX_VALUE,
                    70
                )
            );

            eventRow.setBackground(
                CARD
            );

            eventRow.setBorder(
                BorderFactory.createEmptyBorder(
                    10,
                    20,
                    10,
                    20
                )
            );

            JLabel eventInfo =
                new JLabel(
                    event.getEventId()
                    + "   "
                    + event.getEventname()
                    + "   "
                    + event.getEventDate()
                    + "   "
                    + event.getVenue()
                    + "   "
                    + event.getOrganiser()
                );

            eventInfo.setForeground(
                WHITE
            );

            eventInfo.setFont(
                new Font(
                    "Arial",
                    Font.PLAIN,
                    14
                )
            );

            JButton registerButton =
                new JButton("REGISTER");

            registerButton.setFocusPainted(
                false
            );

            registerButton.addActionListener(
                e -> registerForEvent(event)
            );

            eventRow.add(
                eventInfo,
                BorderLayout.CENTER
            );

            eventRow.add(
                registerButton,
                BorderLayout.EAST
            );

            eventsPanel.add(
                eventRow
            );

            eventsPanel.add(
                Box.createVerticalStrut(5)
            );
        }

        JScrollPane scrollPane =
            new JScrollPane(
                eventsPanel
            );

        scrollPane.setBorder(null);

        scrollPane
            .getVerticalScrollBar()
            .setUnitIncrement(16);

        panel.add(
            scrollPane,
            BorderLayout.CENTER
        );

        panel.revalidate();
        panel.repaint();
    }

    private void registerForEvent(
        Event event
    ) {

        String sql =
            "INSERT INTO registrations " +
            "(event_id, student_id, registration_date) " +
            "VALUES (?, ?, CURDATE())";

        try (
            Connection connection =
                DBConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {

            statement.setString(
                1,
                event.getEventId()
            );

            statement.setString(
                2,
                studentId
            );

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                this,
                "Registered successfully!",
                "Registration",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException ex) {

            if (
                ex.getMessage()
                    .contains("Duplicate entry")
            ) {

                JOptionPane.showMessageDialog(
                    this,
                    "You are already registered for this event.",
                    "Registration",
                    JOptionPane.WARNING_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Registration failed.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        }
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

        panel.add(label);
    }
}