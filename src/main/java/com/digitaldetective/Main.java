
package com.digitaldetective;

import javax.swing.*;
import java.awt.*;
import com.digitaldetective.database.DatabaseConnection;
import com.digitaldetective.database.UserDAO;
import java.sql.SQLException;
import com.digitaldetective.database.CaseDAO;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import com.digitaldetective.database.EvidenceDAO;
import com.digitaldetective.database.AnalysisDAO;
import java.io.IOException;



public class Main {

    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();

        SwingUtilities.invokeLater(() -> createHomeScreen());

    }

    public static void createHomeScreen() {

        // Create the main application window
        JFrame frame = new JFrame("Digital Detective");

        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Header panel
        JPanel header = new JPanel();
        header.setBackground(new Color(20, 35, 60));
        header.setLayout(new GridLayout(2, 1));

        JLabel heading = new JLabel(
            "DIGITAL DETECTIVE",
            SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 30));
        heading.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "Your Digital Investigation Assistant",
            SwingConstants.CENTER
        );

        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(Color.LIGHT_GRAY);

        header.add(heading);
        header.add(subtitle);

        // Center panel
        JPanel center = new JPanel();
        center.setBackground(new Color(240, 244, 249));
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome to Digital Detective!");

        welcome.setFont(new Font("Arial", Font.BOLD, 25));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description = new JLabel(
            "Investigate suspicious emails, URLs and messages."
        );

        description.setFont(new Font("Arial", Font.PLAIN, 16));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button panel
        JPanel buttons = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 20, 20)
        );

        buttons.setBackground(new Color(240, 244, 249));

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton aboutButton = new JButton("About");

        buttons.add(loginButton);
        buttons.add(registerButton);
        buttons.add(aboutButton);

        // Button actions
        loginButton.addActionListener(e ->
            showLoginScreen(frame)
        );

        registerButton.addActionListener(e ->
            showRegisterScreen(frame)
        );

        aboutButton.addActionListener(e ->
            JOptionPane.showMessageDialog(
                frame,
                "Digital Detective is an educational project " +
                "for investigating sample digital evidence."
            )
        );

        // Add components to center panel
        center.add(Box.createVerticalStrut(100));
        center.add(welcome);
        center.add(Box.createVerticalStrut(20));
        center.add(description);
        center.add(Box.createVerticalStrut(40));
        center.add(buttons);

        // Footer
        JLabel footer = new JLabel(
            "Digital Detective | Academic Project",
            SwingConstants.CENTER
        );

        footer.setBorder(
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        );

        // Add panels to the main window
        frame.add(header, BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);
        frame.add(footer, BorderLayout.SOUTH);

        // Display window
        frame.setVisible(true);
    }
    public static void showRegisterScreen(JFrame parent) {

    JDialog dialog = new JDialog(parent, "Create Account", true);

    dialog.setSize(450, 450);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout());

    // Heading
    JLabel heading = new JLabel(
        "Create Your Account",
        SwingConstants.CENTER
    );

    heading.setFont(new Font("Arial", Font.BOLD, 24));
    heading.setBorder(
        BorderFactory.createEmptyBorder(20, 10, 20, 10)
    );

    // Form panel
    JPanel form = new JPanel(new GridLayout(5, 2, 10, 15));
    form.setBorder(
        BorderFactory.createEmptyBorder(20, 30, 20, 30)
    );

    JTextField nameField = new JTextField();
    JTextField emailField = new JTextField();

    JPasswordField passwordField = new JPasswordField();
    JPasswordField confirmField = new JPasswordField();

    form.add(new JLabel("Full Name:"));
    form.add(nameField);

    form.add(new JLabel("Email Address:"));
    form.add(emailField);

    form.add(new JLabel("Password:"));
    form.add(passwordField);

    form.add(new JLabel("Confirm Password:"));
    form.add(confirmField);

    // Buttons
    JButton register = new JButton("Create Account");
    JButton clear = new JButton("Clear");

    form.add(register);
    form.add(clear);

    // Clear form
    clear.addActionListener(e -> {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmField.setText("");
    });

    // Registration validation
    register.addActionListener(e -> {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (name.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirm.isEmpty()) {

            JOptionPane.showMessageDialog(
                dialog,
                "Please fill in all fields."
            );

        } else if (!email.contains("@") || !email.contains(".")) {

            JOptionPane.showMessageDialog(
                dialog,
                "Please enter a valid email address."
            );

        } else if (!password.equals(confirm)) {

            JOptionPane.showMessageDialog(
                dialog,
                "Passwords do not match."
            );

        }  else {
    try {
        boolean registered = UserDAO.registerUser(
            name, email, password
        );

        if (registered) {
            JOptionPane.showMessageDialog(dialog,
                "Registration successful! Your account has been created.");

            dialog.dispose();
        }

    } catch (SQLException ex) {

        if (ex.getMessage() != null
                && ex.getMessage().contains("UNIQUE")) {

            JOptionPane.showMessageDialog(dialog,
                "This email is already registered.");

        } else {
            JOptionPane.showMessageDialog(dialog,
                "Registration failed. Please try again.");
            ex.printStackTrace();
        }
    }
}
    });

    dialog.add(heading, BorderLayout.NORTH);
    dialog.add(form, BorderLayout.CENTER);

    dialog.setVisible(true);
    }
    
public static void showLoginScreen(JFrame parent) {

    JDialog dialog = new JDialog(parent, "User Login", true);

    dialog.setSize(400, 280);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout());

    JLabel heading = new JLabel(
        "Login to Digital Detective",
        SwingConstants.CENTER
    );

    heading.setFont(new Font("Arial", Font.BOLD, 22));
    heading.setBorder(
        BorderFactory.createEmptyBorder(20, 10, 20, 10)
    );

    JPanel form = new JPanel(new GridLayout(3, 2, 10, 15));

    form.setBorder(
        BorderFactory.createEmptyBorder(20, 30, 20, 30)
    );

    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();

    JButton login = new JButton("Login");
    JButton clear = new JButton("Clear");

    form.add(new JLabel("Email Address:"));
    form.add(emailField);

    form.add(new JLabel("Password:"));
    form.add(passwordField);

    form.add(login);
    form.add(clear);

    clear.addActionListener(e -> {
        emailField.setText("");
        passwordField.setText("");
    });

    login.addActionListener(e -> {

        String email = emailField.getText().trim();

        String password =
            new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                dialog,
                "Please enter email and password."
            );

        } else {

            try {

                boolean valid = UserDAO.loginUser(email, password);

                if (valid) {

                    JOptionPane.showMessageDialog(
                        dialog,
                        "Login successful! Welcome to Digital Detective."
                    );

                    dialog.dispose();

                    showDashboard(parent, email);
                } else {

                    JOptionPane.showMessageDialog(
                        dialog,
                        "Invalid email or password."
                    );
                }

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                    dialog,
                    "Unable to connect to the database."
                );

                ex.printStackTrace();
            }
        }
    });

    dialog.add(heading, BorderLayout.NORTH);
    dialog.add(form, BorderLayout.CENTER);

    dialog.setVisible(true);
    }
    
public static void showDashboard(JFrame parent, String email) {

    JFrame dashboard = new JFrame("Digital Detective - Dashboard");

    dashboard.setSize(900, 650);
    dashboard.setLocationRelativeTo(parent);
    dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    dashboard.setLayout(new BorderLayout());

    JLabel heading = new JLabel(
            "INVESTIGATION DASHBOARD",
            SwingConstants.CENTER
    );

    heading.setFont(new Font("Arial", Font.BOLD, 28));
    heading.setForeground(Color.WHITE);
    heading.setOpaque(true);
    heading.setBackground(new Color(20, 35, 60));
    heading.setBorder(
            BorderFactory.createEmptyBorder(25, 10, 25, 10)
    );

    JPanel panel = new JPanel(new GridLayout(5, 2, 20, 20));

    panel.setBackground(new Color(240, 244, 249));
    panel.setBorder(
            BorderFactory.createEmptyBorder(40, 60, 40, 60)
    );

    JButton createCase = new JButton("Create New Case");
    JButton viewCases = new JButton("View My Cases");
    JButton addEvidence = new JButton("Add Evidence");
    JButton viewEvidence = new JButton("View Saved Evidence");

    JButton emailAnalysis = new JButton("Email Analysis");
    JButton urlAnalysis = new JButton("URL Analysis");
    JButton messageAnalysis = new JButton("Message Analysis");

    JButton generateReport = new JButton("Generate Report");
    JButton logout = new JButton("Logout");

    panel.add(createCase);
    panel.add(viewCases);
    panel.add(addEvidence);
    panel.add(viewEvidence);
    panel.add(emailAnalysis);
    panel.add(urlAnalysis);
    panel.add(messageAnalysis);
    panel.add(generateReport);
    panel.add(logout);

    createCase.addActionListener(e ->
            showCreateCaseScreen(dashboard, email)
    );

    viewCases.addActionListener(e ->
            showMyCasesScreen(dashboard, email)
    );

    addEvidence.addActionListener(e ->
            showAddEvidenceScreen(dashboard, email)
    );

    viewEvidence.addActionListener(e ->
            showViewEvidenceScreen(dashboard, email)
    );

    emailAnalysis.addActionListener(e ->
            showAnalysisScreen(dashboard, email, "Email")
    );

    urlAnalysis.addActionListener(e ->
            showAnalysisScreen(dashboard, email, "URL")
    );

    messageAnalysis.addActionListener(e ->
            showAnalysisScreen(dashboard, email, "Message")
    );

    generateReport.addActionListener(e ->
            showGenerateReportScreen(dashboard, email)
    );

    logout.addActionListener(e -> {
        dashboard.dispose();
        createHomeScreen();
    });

    dashboard.add(heading, BorderLayout.NORTH);
    dashboard.add(panel, BorderLayout.CENTER);

    dashboard.setVisible(true);
}
public static void showCreateCaseScreen(JFrame parent, String email) {

    JDialog dialog = new JDialog(parent, "Create New Case", true);
    dialog.setSize(500, 400);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout(10, 10));

    JLabel heading = new JLabel(
            "Create New Investigation Case",
            SwingConstants.CENTER
    );

    heading.setFont(new Font("Arial", Font.BOLD, 20));
    dialog.add(heading, BorderLayout.NORTH);

    JPanel form = new JPanel(new GridLayout(2, 1, 10, 10));
    form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

    JPanel titlePanel = new JPanel(new BorderLayout(5, 5));
    titlePanel.add(new JLabel("Case Title:"), BorderLayout.NORTH);

    JTextField titleField = new JTextField();
    titlePanel.add(titleField, BorderLayout.CENTER);

    JPanel descriptionPanel = new JPanel(new BorderLayout(5, 5));
    descriptionPanel.add(new JLabel("Case Description:"), BorderLayout.NORTH);

    JTextArea descriptionArea = new JTextArea(5, 20);
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);

    descriptionPanel.add(
            new JScrollPane(descriptionArea),
            BorderLayout.CENTER
    );

    form.add(titlePanel);
    form.add(descriptionPanel);

    dialog.add(form, BorderLayout.CENTER);

    JPanel buttons = new JPanel();

    JButton saveButton = new JButton("Save Case");
    JButton cancelButton = new JButton("Cancel");

    buttons.add(saveButton);
    buttons.add(cancelButton);

    dialog.add(buttons, BorderLayout.SOUTH);

    saveButton.addActionListener(e -> {

        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Please enter a case title."
            );
            return;
        }

       try {
    int userId = UserDAO.getUserIdByEmail(email);

    if (userId == -1) {
        JOptionPane.showMessageDialog(
                dialog,
                "User not found."
        );
        return;
    }

    // Display all saved cases in the terminal
    CaseDAO.checkCases(userId);

    boolean success = CaseDAO.createCase(
            userId,
            title,
            description
    );
                if (success) {
                System.out.println("NEW CASE SAVED!");
                CaseDAO.checkCases(userId);
            }

            if (success) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Investigation case created successfully!"
                );
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Unable to create case."
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Database error: " + ex.getMessage()
            );
            ex.printStackTrace();
        }
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
}
public static void showMyCasesScreen(JFrame parent, String email) {

    JDialog dialog = new JDialog(parent, "My Investigation Cases", true);
    dialog.setSize(550, 450);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout(10, 10));

    JLabel heading = new JLabel(
            "My Investigation Cases",
            SwingConstants.CENTER
    );

    heading.setFont(new Font("Arial", Font.BOLD, 20));
    dialog.add(heading, BorderLayout.NORTH);

    JTextArea casesArea = new JTextArea();
    casesArea.setEditable(false);
    casesArea.setFont(new Font("Arial", Font.PLAIN, 14));

    try {
        int userId = UserDAO.getUserIdByEmail(email);

        List<String> cases = CaseDAO.getCasesByUserId(userId);

        if (cases.isEmpty()) {
            casesArea.setText("No investigation cases found.");
        } else {
            for (String caseDetails : cases) {
                casesArea.append(caseDetails);
                casesArea.append("\n\n--------------------------\n\n");
            }
        }

    } catch (SQLException ex) {
        casesArea.setText("Unable to retrieve cases: " + ex.getMessage());
        ex.printStackTrace();
    }

    dialog.add(new JScrollPane(casesArea), BorderLayout.CENTER);

    JButton closeButton = new JButton("Close");
    closeButton.addActionListener(e -> dialog.dispose());

    JPanel bottomPanel = new JPanel();
    bottomPanel.add(closeButton);

    dialog.add(bottomPanel, BorderLayout.SOUTH);

    dialog.setVisible(true);
}
public static void showAddEvidenceScreen(JFrame parent, String email) {

    JDialog dialog = new JDialog(parent, "Add Evidence", true);
    dialog.setSize(550, 450);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout(10, 10));

    JLabel heading = new JLabel(
            "Add Investigation Evidence",
            SwingConstants.CENTER
    );
    heading.setFont(new Font("Arial", Font.BOLD, 20));

    dialog.add(heading, BorderLayout.NORTH);

    JPanel form = new JPanel(new GridLayout(3, 1, 10, 10));
    form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

    JPanel casePanel = new JPanel(new BorderLayout(5, 5));
    casePanel.add(new JLabel("Select Investigation Case:"), BorderLayout.NORTH);

    JComboBox<String> caseCombo = new JComboBox<>();
    Map<Integer, String> caseMap = new LinkedHashMap<>();

    JPanel typePanel = new JPanel(new BorderLayout(5, 5));
    typePanel.add(new JLabel("Evidence Type:"), BorderLayout.NORTH);

    JComboBox<String> typeCombo =
            new JComboBox<>(new String[]{"Email", "URL", "Message"});

    typePanel.add(typeCombo, BorderLayout.CENTER);

    JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
    contentPanel.add(new JLabel("Evidence Content:"), BorderLayout.NORTH);

    JTextArea contentArea = new JTextArea(5, 20);
    contentArea.setLineWrap(true);
    contentArea.setWrapStyleWord(true);

    contentPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);

    form.add(casePanel);
    form.add(typePanel);
    form.add(contentPanel);

    casePanel.add(caseCombo, BorderLayout.CENTER);

    dialog.add(form, BorderLayout.CENTER);

    try {
        int userId = UserDAO.getUserIdByEmail(email);

        caseMap.putAll(CaseDAO.getCaseTitlesByUserId(userId));

        for (String title : caseMap.values()) {
            caseCombo.addItem(title);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(
                dialog,
                "Unable to load cases: " + ex.getMessage()
        );
        dialog.dispose();
        return;
    }

    JPanel buttons = new JPanel();

    JButton saveButton = new JButton("Save Evidence");
    JButton cancelButton = new JButton("Cancel");

    buttons.add(saveButton);
    buttons.add(cancelButton);

    dialog.add(buttons, BorderLayout.SOUTH);

    saveButton.addActionListener(e -> {

        int selectedIndex = caseCombo.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Please create a case first."
            );
            return;
        }

        String content = contentArea.getText().trim();

        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Please enter evidence content."
            );
            return;
        }

        int caseId = (int) caseMap.keySet().toArray()[selectedIndex];

        String evidenceType = (String) typeCombo.getSelectedItem();

        try {
            boolean success = EvidenceDAO.addEvidence(
                    caseId,
                    evidenceType,
                    content
            );

            if (success) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Evidence saved successfully!"
                );
                dialog.dispose();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Database error: " + ex.getMessage()
            );
            ex.printStackTrace();
        }
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
}
public static void showViewEvidenceScreen(JFrame parent, String email) {

    JDialog dialog = new JDialog(parent, "View Saved Evidence", true);

    dialog.setSize(650, 500);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout(10, 10));

    JComboBox<String> caseCombo = new JComboBox<>();
    Map<Integer, String> caseMap = new LinkedHashMap<>();

    try {
        int userId = UserDAO.getUserIdByEmail(email);

        caseMap.putAll(CaseDAO.getCaseTitlesByUserId(userId));

        for (String title : caseMap.values()) {
            caseCombo.addItem(title);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(
                dialog,
                "Unable to load cases: " + ex.getMessage()
        );
        dialog.dispose();
        return;
    }

    JPanel topPanel = new JPanel(new BorderLayout(5, 5));
    topPanel.add(new JLabel("Select Investigation Case:"), BorderLayout.NORTH);
    topPanel.add(caseCombo, BorderLayout.CENTER);

    JTextArea evidenceArea = new JTextArea();
    evidenceArea.setEditable(false);
    evidenceArea.setLineWrap(true);
    evidenceArea.setWrapStyleWord(true);

    JButton viewButton = new JButton("View Evidence");
    JButton closeButton = new JButton("Close");

    viewButton.addActionListener(e -> {

        int index = caseCombo.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Please create a case first."
            );
            return;
        }

        int caseId = (int) caseMap.keySet().toArray()[index];

        try {
            List<String> evidence =
                    EvidenceDAO.getEvidenceByCaseId(caseId);

            evidenceArea.setText("");

            if (evidence.isEmpty()) {
                evidenceArea.setText("No evidence saved for this case.");
            } else {
                for (String item : evidence) {
                    evidenceArea.append(item);
                    evidenceArea.append("\n\n----------------------\n\n");
                }
            }

        } catch (SQLException ex) {
            evidenceArea.setText(
                    "Unable to retrieve evidence: " + ex.getMessage()
            );
        }
    });

    closeButton.addActionListener(e -> dialog.dispose());

    JPanel bottomPanel = new JPanel();
    bottomPanel.add(viewButton);
    bottomPanel.add(closeButton);

    dialog.add(topPanel, BorderLayout.NORTH);
    dialog.add(new JScrollPane(evidenceArea), BorderLayout.CENTER);
    dialog.add(bottomPanel, BorderLayout.SOUTH);

    dialog.setVisible(true);
}
public static void showAnalysisScreen(
        JFrame parent, String email, String type) {

    JDialog dialog = new JDialog(parent, type + " Analysis", true);

    dialog.setSize(650, 550);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout(10, 10));

    JComboBox<String> caseCombo = new JComboBox<>();
    Map<Integer, String> caseMap = new LinkedHashMap<>();

    try {
        int userId = UserDAO.getUserIdByEmail(email);

        caseMap.putAll(CaseDAO.getCaseTitlesByUserId(userId));

        for (String title : caseMap.values()) {
            caseCombo.addItem(title);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(
                dialog,
                "Unable to load cases: " + ex.getMessage()
        );
        dialog.dispose();
        return;
    }

    JPanel topPanel = new JPanel(new BorderLayout(5, 5));
    topPanel.add(new JLabel("Select Investigation Case:"), BorderLayout.NORTH);
    topPanel.add(caseCombo, BorderLayout.CENTER);

    JTextArea contentArea = new JTextArea(6, 40);
    contentArea.setLineWrap(true);
    contentArea.setWrapStyleWord(true);

    JTextArea resultArea = new JTextArea();
    resultArea.setEditable(false);
    resultArea.setLineWrap(true);
    resultArea.setWrapStyleWord(true);

    JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
    inputPanel.add(new JLabel("Enter " + type + " Content:"), BorderLayout.NORTH);
    inputPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);

    JPanel resultPanel = new JPanel(new BorderLayout(5, 5));
    resultPanel.add(new JLabel("Analysis Results:"), BorderLayout.NORTH);
    resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

    JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    centerPanel.add(inputPanel);
    centerPanel.add(resultPanel);

    JButton analyzeButton = new JButton("Analyze");
    JButton closeButton = new JButton("Close");

    analyzeButton.addActionListener(e -> {

        int index = caseCombo.getSelectedIndex();
        String content = contentArea.getText().trim();

        if (index == -1) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Please create a case first."
            );
            return;
        }

        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Please enter content to analyze."
            );
            return;
        }

        int caseId = (int) caseMap.keySet().toArray()[index];

        Analyzer analyzer;

        if (type.equals("Email")) {
            analyzer = new EmailAnalyzer();
        } else if (type.equals("URL")) {
            analyzer = new URLAnalyzer();
        } else {
            analyzer = new MessageAnalyzer();
        }

        String result = analyzer.analyze(content);

        try {
            AnalysisDAO.saveAnalysis(
                    caseId,
                    type,
                    content,
                    result
            );

            resultArea.setText(result);

            JOptionPane.showMessageDialog(
                    dialog,
                    "Analysis completed and results saved successfully!"
            );

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Unable to save analysis: " + ex.getMessage()
            );
            ex.printStackTrace();
        }
    });

    closeButton.addActionListener(e -> dialog.dispose());

    JPanel bottomPanel = new JPanel();
    bottomPanel.add(analyzeButton);
    bottomPanel.add(closeButton);

    dialog.add(topPanel, BorderLayout.NORTH);
    dialog.add(centerPanel, BorderLayout.CENTER);
    dialog.add(bottomPanel, BorderLayout.SOUTH);

    dialog.setVisible(true);
}

public static void showGenerateReportScreen(JFrame parent, String email) {

    JDialog dialog = new JDialog(parent, "Generate Report", true);

    dialog.setSize(500, 300);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout(10, 10));

    JComboBox<String> caseCombo = new JComboBox<>();
    Map<Integer, String> caseMap = new LinkedHashMap<>();

    try {
        int userId = UserDAO.getUserIdByEmail(email);
        caseMap.putAll(CaseDAO.getCaseTitlesByUserId(userId));

        for (String title : caseMap.values()) {
            caseCombo.addItem(title);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(
                dialog,
                "Unable to load cases: " + ex.getMessage()
        );
        dialog.dispose();
        return;
    }

    JComboBox<String> reportCombo = new JComboBox<>(
            new String[]{
                "Email",
                "URL",
                "Message",
                "All"
            }
    );

    JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));

    centerPanel.add(new JLabel("Select Case:"));
    centerPanel.add(caseCombo);
    centerPanel.add(new JLabel("Select Report Type:"));
    centerPanel.add(reportCombo);

    JButton generateButton = new JButton("Generate Report");
    JButton closeButton = new JButton("Close");

    generateButton.addActionListener(e -> {

        int index = caseCombo.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Please create a case first."
            );
            return;
        }

        int caseId = (int) caseMap.keySet().toArray()[index];

        String reportType = (String) reportCombo.getSelectedItem();

        try {
            String path = ReportGenerator.generateReport(
                    caseId,
                    reportType
            );

            JOptionPane.showMessageDialog(
                    dialog,
                    "Report generated successfully!\n\nSaved at:\n" + path
            );

        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Unable to generate report: " + ex.getMessage()
            );
            ex.printStackTrace();
        }
    });

    closeButton.addActionListener(e -> dialog.dispose());

    JPanel bottomPanel = new JPanel();
    bottomPanel.add(generateButton);
    bottomPanel.add(closeButton);

    dialog.add(centerPanel, BorderLayout.CENTER);
    dialog.add(bottomPanel, BorderLayout.SOUTH);

    dialog.setVisible(true);
}


}
