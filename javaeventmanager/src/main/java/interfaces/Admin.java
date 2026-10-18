
package interfaces;

import java.awt.BorderLayout;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import functions.DBConnection;

public class Admin extends JFrame {

    private final JPanel panel;
    private final Organisers organiser;

    private static final java.awt.Color BACKGROUND =
        new java.awt.Color(18, 18, 18);

    private static final java.awt.Color CARD =
        new java.awt.Color(28, 28, 28);

    private static final java.awt.Color RED =
        new java.awt.Color(230, 55, 55);

    private static final java.awt.Color WHITE =
        new java.awt.Color(245, 245, 245);

    public Admin(Organisers organiser) {

        this.organiser = organiser;

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

        JPanel centerPanel = new JPanel(new GridBagLayout());
        JPanel rightPanel = new JPanel();

        leftPanel.setOpaque(false);
        centerPanel.setOpaque(false);
        rightPanel.setOpaque(false);

        if (backAction != null) {
            leftPanel.add(createBackButton(backAction));
        }

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(RED);

        centerPanel.add(titleLabel);

        topPanel.add(leftPanel);
        topPanel.add(centerPanel);
        topPanel.add(rightPanel);

        return topPanel;
    }

    public void homePage() {

        panel.removeAll();

        panel.add(
            createHeader(
                "ADMIN PANEL",
                () -> {
                    organiser.loginPage();
                    organiser.setVisible(true);
                    dispose();
                }
            ),
            BorderLayout.NORTH
        );

        JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBackground(BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);

        JLabel welcome = new JLabel("Welcome Admin");

        welcome.setForeground(WHITE);
        welcome.setFont(new Font("Arial", Font.PLAIN, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        homePanel.add(welcome, gbc);

        JButton manageEvents = new JButton("Manage Events");
        JButton manageUsers = new JButton("Manage Users");

        styleMainButton(manageEvents);
        styleMainButton(manageUsers);

        gbc.gridy = 1;
        homePanel.add(manageEvents, gbc);

        gbc.gridy = 2;
        homePanel.add(manageUsers, gbc);

        panel.add(homePanel, BorderLayout.CENTER);

        manageEvents.addActionListener(e -> manageEventsPage());
        manageUsers.addActionListener(e -> manageUsersPage());

        refreshPanel();
    }

    private void manageEventsPage() {

        panel.removeAll();

        panel.add(
            createHeader("MANAGE EVENTS", () -> homePage()),
            BorderLayout.NORTH
        );

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JButton viewEvents = new JButton("View All Events");
        JButton deleteEvents = new JButton("Delete Event");

        styleMainButton(viewEvents);
        styleMainButton(deleteEvents);

        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(viewEvents, gbc);

        gbc.gridy = 1;
        content.add(deleteEvents, gbc);

        panel.add(content, BorderLayout.CENTER);

        viewEvents.addActionListener(e -> viewAllEventsPage());
        deleteEvents.addActionListener(e -> deleteEventPage());

        refreshPanel();
    }

    private void viewAllEventsPage() {

        panel.removeAll();

        panel.add(
            createHeader("ALL EVENTS", () -> manageEventsPage()),
            BorderLayout.NORTH
        );

        String[] columns = {
            "EVENT ID",
            "EVENT NAME",
            "DATE",
            "VENUE",
            "ORGANISER"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        panel.add(scrollPane, BorderLayout.CENTER);

        String sql =
            "SELECT e.event_id, e.event_name, e.event_date, " +
            "e.venue, o.organiser_name " +
            "FROM events e " +
            "LEFT JOIN organisers o " +
            "ON e.organiser_id = o.organiser_id " +
            "ORDER BY e.event_date";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery()
        ) {
            while (result.next()) {
                model.addRow(new Object[] {
                    result.getString("event_id"),
                    result.getString("event_name"),
                    result.getString("event_date"),
                    result.getString("venue"),
                    result.getString("organiser_name")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Failed to load events.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }

        refreshPanel();
    }

    private void deleteEventPage() {

        panel.removeAll();

        panel.add(
            createHeader("DELETE EVENT", () -> manageEventsPage()),
            BorderLayout.NORTH
        );

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(BACKGROUND);
        content.setBorder(
            BorderFactory.createEmptyBorder(20, 40, 30, 40)
        );

        String[] columns = {
            "EVENT ID",
            "EVENT NAME",
            "DATE",
            "VENUE",
            "ORGANISER"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(
            javax.swing.ListSelectionModel.SINGLE_SELECTION
        );
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);

        content.add(scrollPane, BorderLayout.CENTER);

        JButton deleteButton = new JButton("DELETE SELECTED EVENT");
        styleMainButton(deleteButton);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(BACKGROUND);
        bottomPanel.add(deleteButton);

        content.add(bottomPanel, BorderLayout.SOUTH);

        panel.add(content, BorderLayout.CENTER);

        Runnable loadEvents = () -> {

            model.setRowCount(0);

            String sql =
                "SELECT e.event_id, e.event_name, e.event_date, " +
                "e.venue, o.organiser_name " +
                "FROM events e " +
                "LEFT JOIN organisers o " +
                "ON e.organiser_id = o.organiser_id " +
                "ORDER BY e.event_date";

            try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                    connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()
            ) {
                while (result.next()) {
                    model.addRow(new Object[] {
                        result.getString("event_id"),
                        result.getString("event_name"),
                        result.getString("event_date"),
                        result.getString("venue"),
                        result.getString("organiser_name")
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to load events.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        };

        deleteButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                    this,
                    "Select an event to delete.",
                    "No Event Selected",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            selectedRow = table.convertRowIndexToModel(selectedRow);

            String eventId =
                model.getValueAt(selectedRow, 0).toString();

            String eventName =
                model.getValueAt(selectedRow, 1).toString();

            int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Delete \"" + eventName +
                "\" and all its registrations?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmation != JOptionPane.YES_OPTION) {
                return;
            }

            try (Connection connection = DBConnection.getConnection()) {

                connection.setAutoCommit(false);

                try {
                    try (
                        PreparedStatement registrationStatement =
                            connection.prepareStatement(
                                "DELETE FROM registrations WHERE event_id = ?"
                            )
                    ) {
                        registrationStatement.setString(1, eventId);
                        registrationStatement.executeUpdate();
                    }

                    int deleted;

                    try (
                        PreparedStatement eventStatement =
                            connection.prepareStatement(
                                "DELETE FROM events WHERE event_id = ?"
                            )
                    ) {
                        eventStatement.setString(1, eventId);
                        deleted = eventStatement.executeUpdate();
                    }

                    if (deleted == 0) {
                        throw new SQLException("Event not found.");
                    }

                    connection.commit();

                    JOptionPane.showMessageDialog(
                        this,
                        "Event deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    loadEvents.run();

                } catch (SQLException ex) {
                    connection.rollback();
                    throw ex;
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete event.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        });

        loadEvents.run();
        refreshPanel();
    }

    private void manageUsersPage() {

        panel.removeAll();

        panel.add(
            createHeader("MANAGE USERS", () -> homePage()),
            BorderLayout.NORTH
        );

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JButton addStudent = new JButton("Add Student");
        JButton addOrganiser = new JButton("Add Organiser");
        JButton removeStudent = new JButton("Remove Student");
        JButton removeOrganiser = new JButton("Remove Organiser");

        styleMainButton(addStudent);
        styleMainButton(addOrganiser);
        styleMainButton(removeStudent);
        styleMainButton(removeOrganiser);

        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(addStudent, gbc);

        gbc.gridy = 1;
        content.add(addOrganiser, gbc);

        gbc.gridy = 2;
        content.add(removeStudent, gbc);

        gbc.gridy = 3;
        content.add(removeOrganiser, gbc);

        panel.add(content, BorderLayout.CENTER);

        addStudent.addActionListener(e -> addStudentPage());
        addOrganiser.addActionListener(e -> addOrganiserPage());
        removeStudent.addActionListener(e -> removeUserPage(true));
        removeOrganiser.addActionListener(e -> removeUserPage(false));

        refreshPanel();
    }

    private void removeUserPage(boolean removeStudent) {
        String userType = removeStudent ? "STUDENT" : "ORGANISER";
        String fieldLabel = removeStudent ? "Student ID" : "Organiser ID";
        String tableName = removeStudent ? "students" : "organisers";
        String idColumn = removeStudent ? "student_id" : "organiser_id";

        panel.removeAll();
        panel.add(
            createHeader("REMOVE " + userType, () -> manageUsersPage()),
            BorderLayout.NORTH
        );

        JPanel form = createFormPanel();
        JTextField idField = new JTextField(18);
        addFormRow(form, fieldLabel, idField, 0);

        JButton remove = new JButton("REMOVE " + userType);
        styleMainButton(remove);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 10, 10);
        form.add(remove, gbc);
        panel.add(form, BorderLayout.CENTER);

        remove.addActionListener(e -> {
            String userId = idField.getText().trim();
            if (userId.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Enter a " + fieldLabel.toLowerCase() + ".",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                this,
                "Remove this " + userType.toLowerCase() + "? Related registrations"
                    + (removeStudent ? " will also be deleted." : " and events will also be deleted."),
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            try (Connection connection = DBConnection.getConnection()) {
                connection.setAutoCommit(false);
                try {
                    if (removeStudent) {
                        try (PreparedStatement statement = connection.prepareStatement(
                            "DELETE FROM registrations WHERE student_id = ?")) {
                            statement.setString(1, userId);
                            statement.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement statement = connection.prepareStatement(
                            "DELETE FROM registrations WHERE event_id IN "
                                + "(SELECT event_id FROM events WHERE organiser_id = ?)")) {
                            statement.setString(1, userId);
                            statement.executeUpdate();
                        }
                        try (PreparedStatement statement = connection.prepareStatement(
                            "DELETE FROM events WHERE organiser_id = ?")) {
                            statement.setString(1, userId);
                            statement.executeUpdate();
                        }
                    }

                    int deleted;
                    try (PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?")) {
                        statement.setString(1, userId);
                        deleted = statement.executeUpdate();
                    }
                    connection.commit();

                    if (deleted > 0) {
                        JOptionPane.showMessageDialog(
                            this,
                            userType.substring(0, 1) + userType.substring(1).toLowerCase()
                                + " removed successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        idField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            userType.substring(0, 1) + userType.substring(1).toLowerCase()
                                + " not found.",
                            "Not Found",
                            JOptionPane.WARNING_MESSAGE
                        );
                        idField.setText("");
                    }
                } catch (SQLException ex) {
                    connection.rollback();
                    throw ex;
                } finally {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to remove " + userType.toLowerCase() + ".",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        });

        refreshPanel();
    }
    private void addStudentPage() {

        panel.removeAll();

        panel.add(
            createHeader("ADD STUDENT", () -> manageUsersPage()),
            BorderLayout.NORTH
        );

        JPanel form = createFormPanel();

        JTextField studentId = new JTextField(18);
        JTextField studentName = new JTextField(18);
        JTextField email = new JTextField(18);
        JPasswordField password = new JPasswordField(18);

        addFormRow(form, "Student ID", studentId, 0);
        addFormRow(form, "Student Name", studentName, 1);
        addFormRow(form, "Email", email, 2);
        addFormRow(form, "Password", password, 3);

        JButton add = new JButton("ADD STUDENT");
        styleMainButton(add);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 10, 10);

        form.add(add, gbc);

        panel.add(form, BorderLayout.CENTER);

        add.addActionListener(e -> {

            String id = studentId.getText().trim();
            String name = studentName.getText().trim();
            String studentEmail = email.getText().trim();
            String studentPassword = new String(password.getPassword());

            if (
                id.isEmpty() ||
                name.isEmpty() ||
                studentEmail.isEmpty() ||
                studentPassword.isEmpty()
            ) {
                JOptionPane.showMessageDialog(
                    this,
                    "Fill all fields.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String sql =
                "INSERT INTO students " +
                "(student_id, student_name, email, password) " +
                "VALUES (?, ?, ?, ?)";

            try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, id);
                statement.setString(2, name);
                statement.setString(3, studentEmail);
                statement.setString(4, studentPassword);

                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                    this,
                    "Student added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                studentId.setText("");
                studentName.setText("");
                email.setText("");
                password.setText("");

            } catch (SQLException ex) {

                if (ex.getMessage() != null &&
                    ex.getMessage().contains("Duplicate entry")) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Student ID or email already exists.",
                        "Duplicate",
                        JOptionPane.WARNING_MESSAGE
                    );

                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Failed to add student.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    ex.printStackTrace();
                }
            }
        });

        refreshPanel();
    }

    private void addOrganiserPage() {

        panel.removeAll();

        panel.add(
            createHeader("ADD ORGANISER", () -> manageUsersPage()),
            BorderLayout.NORTH
        );

        JPanel form = createFormPanel();

        JTextField organiserId = new JTextField(18);
        JTextField organiserName = new JTextField(18);
        JTextField email = new JTextField(18);
        JPasswordField password = new JPasswordField(18);

        addFormRow(form, "Organiser ID", organiserId, 0);
        addFormRow(form, "Organiser Name", organiserName, 1);
        addFormRow(form, "Email", email, 2);
        addFormRow(form, "Password", password, 3);

        JButton add = new JButton("ADD ORGANISER");
        styleMainButton(add);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 10, 10);

        form.add(add, gbc);

        panel.add(form, BorderLayout.CENTER);

        add.addActionListener(e -> {

            String id = organiserId.getText().trim();
            String name = organiserName.getText().trim();
            String organiserEmail = email.getText().trim();
            String organiserPassword = new String(password.getPassword());

            if (
                id.isEmpty() ||
                name.isEmpty() ||
                organiserEmail.isEmpty() ||
                organiserPassword.isEmpty()
            ) {
                JOptionPane.showMessageDialog(
                    this,
                    "Fill all fields.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String sql =
                "INSERT INTO organisers " +
                "(organiser_id, organiser_name, email, password) " +
                "VALUES (?, ?, ?, ?)";

            try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, id);
                statement.setString(2, name);
                statement.setString(3, organiserEmail);
                statement.setString(4, organiserPassword);

                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                    this,
                    "Organiser added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                organiserId.setText("");
                organiserName.setText("");
                email.setText("");
                password.setText("");

            } catch (SQLException ex) {

                if (ex.getMessage() != null &&
                    ex.getMessage().contains("Duplicate entry")) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Organiser ID or email already exists.",
                        "Duplicate",
                        JOptionPane.WARNING_MESSAGE
                    );

                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Failed to add organiser.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    ex.printStackTrace();
                }
            }
        });

        refreshPanel();
    }

    private JPanel createFormPanel() {

        JPanel form = new JPanel(new GridBagLayout());

        form.setBackground(CARD);
        form.setBorder(
            BorderFactory.createEmptyBorder(40, 60, 40, 60)
        );

        return form;
    }

    private void addFormRow(
        JPanel form,
        String labelText,
        java.awt.Component field,
        int row
    ) {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel(labelText);

        label.setForeground(WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = row;
        form.add(label, gbc);

        gbc.gridx = 1;
        form.add(field, gbc);
    }

    private void styleMainButton(JButton button) {

        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.setPreferredSize(new Dimension(240, 50));
        button.setFocusPainted(false);

        button.putClientProperty(
            "JButton.buttonType",
            "roundRect"
        );
    }

    private void refreshPanel() {
        panel.revalidate();
        panel.repaint();
    }
}
