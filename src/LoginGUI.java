import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.sql.*;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;




public class LoginGUI implements ActionListener {
    private JLabel cabinetLabel;
    private JLabel usernameLabel;
    private JTextField usernameTextField;
    private JLabel passwordLabel;
    private JPasswordField passwordTextField;
    private JButton loginButton;
    private JButton clearButton;
    private JLabel successLabel;
    private JLabel backgroundLabel;
    private HashMap<String, String> users;
    private JFrame secondFrame;
    private JTable patientTable; // Table to display patient information
    private JTextField nameField;
    private JTextField lastNameField;
    private JTextField telField;
    private ArrayList<String> patientList;
    private JTable table; // Table for Rendez Vous
    private Object antecedents;

    private static Connection connection;
    private Statement statement;



    public LoginGUI() {
        users = new HashMap<>();
        users.put("boumahra", "nada2003");
        users.put("sayadi", "chaima04");
        users.put("merakchi", "1234");
        users.put("bencheikh", "aya.01");
        users.put("a", "a");

        patientList = new ArrayList<>();

        JPanel panel = new JPanel();
        JFrame frame = new JFrame("se connecter");
        frame.setSize(736, 1104);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        cabinetLabel = new JLabel("Cabinet Medicale Dr.Boumahra", SwingConstants.RIGHT);
        cabinetLabel.setBounds(100, 50, 400, 25);
        cabinetLabel.setFont(new Font(cabinetLabel.getFont().getName(), Font.BOLD, 21));
        panel.add(cabinetLabel);

        usernameLabel = new JLabel("Utilisateur", SwingConstants.RIGHT);
        usernameLabel.setBounds(100, 140, 140, 25);
        usernameLabel.setFont(new Font(usernameLabel.getFont().getName(), Font.BOLD, usernameLabel.getFont().getSize()));
        panel.add(usernameLabel);

        usernameTextField = new JTextField(20);
        usernameTextField.setBounds(270, 140, 165, 25);
        panel.add(usernameTextField);

        passwordLabel = new JLabel("Mot de passe", SwingConstants.RIGHT);
        passwordLabel.setBounds(120, 200, 135, 25);
        passwordLabel.setFont(new Font(passwordLabel.getFont().getName(), Font.BOLD, usernameLabel.getFont().getSize()));
        panel.add(passwordLabel);

        passwordTextField = new JPasswordField(20);
        passwordTextField.setBounds(270, 200, 165, 25);
        panel.add(passwordTextField);

        loginButton = new JButton("Connexion");
        loginButton.addActionListener(this);
        loginButton.setBounds(200, 260, 100, 25);
        loginButton.setBackground(new Color(22, 189, 179));
        panel.add(loginButton);

        clearButton = new JButton("Effacer");
        clearButton.addActionListener(this);
        clearButton.setBounds(310, 260, 80, 25);
        clearButton.setBackground(new Color(22, 189, 179));
        panel.add(clearButton);

        successLabel = new JLabel("Success", SwingConstants.CENTER);
        successLabel.setBounds(100, 110, 400, 25);
        successLabel.setFont(new Font(successLabel.getFont().getName(), Font.BOLD, usernameLabel.getFont().getSize()));
        panel.add(successLabel);
        successLabel.setText(null);


        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("C:/Users/hp/IdeaProjects/loginGUI/poo.png"));
            backgroundLabel = new JLabel(new ImageIcon(image));
            backgroundLabel.setBounds(0, 0, 736, 1104);
            panel.add(backgroundLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        // Initialize the second window
        secondFrame = new JFrame("Main Cabinet");
        secondFrame.setSize(736, 1104);
        secondFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel secondPanel = new JPanel();
        secondPanel.setLayout(null);
        secondFrame.add(secondPanel);
        secondFrame.setResizable(false);
        secondFrame.setLocationRelativeTo(null);
        // Button for Patients
        JButton patientsButton = new JButton("Patients");
        patientsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openPatientsFrame();
            }
        });
        patientsButton.setBounds(260, 200, 200, 50);
        patientsButton.setBackground(new Color(22, 189, 179));
        secondPanel.add(patientsButton);

        // Button for Rendez Vous
        JButton rendezVousButton = new JButton("Rendez Vous");
        rendezVousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openRendezVousFrame();
            }
        });
        rendezVousButton.setBounds(260, 300, 200, 50);
        rendezVousButton.setBackground(new Color(22, 189, 179));
        secondPanel.add(rendezVousButton);

        // Set background color for the second window
        secondPanel.setBackground(new Color(182, 238, 237));


        // Button for fiches patients
        JButton fichespatientsButton = new JButton("Fiches Patients");
        fichespatientsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openfichespatientsFrame();
            }
        });
        fichespatientsButton.setBounds(260, 400, 200, 50);
        fichespatientsButton.setBackground(new Color(22, 189, 179));
        secondPanel.add(fichespatientsButton);



        secondFrame.setVisible(false); // Initially hide the second frame
    }


    private void openfichespatientsFrame() {
        JFrame fichespatientsFrame = new JFrame("Fiche Patient");
        fichespatientsFrame.setSize(736, 1104);
        fichespatientsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel fichespatientsPanel = new JPanel();
        fichespatientsPanel.setBackground(new Color(182, 238, 237));
        fichespatientsPanel.setLayout(null); // Set layout to null for absolute positioning
        fichespatientsFrame.add(fichespatientsPanel);
        fichespatientsFrame.setResizable(false);
        fichespatientsFrame.setLocationRelativeTo(null);
        // Labels and text fields for patient's name and last name
        JLabel nomLabel = new JLabel("Nom:");
        nomLabel.setBounds(50, 50, 100, 25);
        fichespatientsPanel.add(nomLabel);

        JTextField nomTextField = new JTextField();
        nomTextField.setBounds(150, 50, 200, 25);
        fichespatientsPanel.add(nomTextField);

        JLabel prenomLabel = new JLabel("Prenom:");
        prenomLabel.setBounds(50, 100, 100, 25);
        fichespatientsPanel.add(prenomLabel);

        JTextField prenomTextField = new JTextField();
        prenomTextField.setBounds(150, 100, 200, 25);
        fichespatientsPanel.add(prenomTextField);

        // Text area to display patient information with line wrapping
        JTextArea patientInfoTextArea = new JTextArea();
        patientInfoTextArea.setEditable(false); // Make it read-only
        patientInfoTextArea.setFont(new Font(patientInfoTextArea.getFont().getName(), Font.BOLD, 16)); // Set font to bold and bigger
        patientInfoTextArea.setLineWrap(true); // Enable line wrapping
        patientInfoTextArea.setWrapStyleWord(true); // Wrap at word boundaries
        JScrollPane scrollPane = new JScrollPane(patientInfoTextArea);
        scrollPane.setBounds(50, 185, 600, 570); // Set bounds for the panel
        fichespatientsPanel.add(scrollPane);

        // Button to search for patient information
        JButton searchButton = new JButton("Chercher");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the entered patient name and last name
                String nom = nomTextField.getText();
                String prenom = prenomTextField.getText();

                // Retrieve patient information based on Nom and Prenom
                String query = "SELECT * FROM Patient WHERE Nom = ? AND Prenom = ?";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, nom);
                    preparedStatement.setString(2, prenom);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Display patient information in the text area
                    if (resultSet.next()) {
                        String patientInfo = "\n \n \n                                             FICHE PATIENT"
                                + "\n \n \n     ● Nom: " + resultSet.getString("Nom") + "\n"
                                + "\n     ● Prenom: " + resultSet.getString("Prenom") + "\n"
                                + "\n     ● Telephone: " + resultSet.getString("Telephone") + "\n"
                                + "\n     ● Antecedents: " + resultSet.getString("Antecedents") + "\n"
                                + "\n     ● Observations: " + resultSet.getString("Observations") + "\n"
                                + "\n     ● Ordonnance: " + resultSet.getString("Ordonnance") + "\n";
                        patientInfoTextArea.setText(patientInfo);
                    } else {
                        patientInfoTextArea.setText("Patient pas trouvé!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // Handle SQL exception
                }
            }
        });
        searchButton.setBounds(150, 150, 100, 25);
        searchButton.setBackground(new Color(22, 189, 179));
        fichespatientsPanel.add(searchButton);

        // Button to clear input fields and text area
        JButton clearButton = new JButton("Effacer");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Clear entered name and last name
                nomTextField.setText("");
                prenomTextField.setText("");
                // Clear patient information text area
                patientInfoTextArea.setText("");
            }
        });
        clearButton.setBounds(260, 150, 100, 25);
        clearButton.setBackground(new Color(22, 189, 179));
        fichespatientsPanel.add(clearButton);

        // Button to print patient information
        JButton printButton = new JButton("Imprimer");
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String patientInfo = patientInfoTextArea.getText();
                printText(patientInfo);
            }
        });
        printButton.setBounds(370, 150, 100, 25);
        printButton.setBackground(new Color(22, 189, 179));
        fichespatientsPanel.add(printButton);

        fichespatientsFrame.setVisible(true);
    }

    // Method to print text
    private void printText(String text) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Print Data");

        job.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {
                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.setFont(new Font("Arial", Font.PLAIN, 12));

                // Write "Dr.BOUMAHRA" at the top left
                g2.drawString("Dr.BOUMAHRA", 100, 50);

                // Write the date below "Dr.BOUMAHRA"
                // Get the current date and time
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String currentDate = dateFormat.format(calendar.getTime());
                g2.drawString(" Le: " + currentDate, 100, 70);

                // Split the text into lines to fit within the printable area
                String[] lines = text.split("\\n");
                int lineHeight = g2.getFontMetrics().getHeight();
                int y = 100; // Initial y position

                // Draw each line of text
                for (String line : lines) {
                    g2.drawString(line, 100, y);
                    y += lineHeight; // Move to the next line
                }

                // Write "signature et cachet" at the bottom right
                FontMetrics metrics = g2.getFontMetrics();
                String signatureText = "signature et cachet";
                int signatureWidth = metrics.stringWidth(signatureText);
                int pageWidth = (int) pf.getImageableWidth();
                int pageHeight = (int) pf.getImageableHeight();
                int signatureX = pageWidth - signatureWidth - 100;
                int signatureY = pageHeight - 100; // Adjust vertical position
                g2.drawString(signatureText, signatureX, signatureY);

                return Printable.PAGE_EXISTS;
            }
        });

        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void openPatientsFrame() {
        JFrame patientsFrame = new JFrame("Patients");
        patientsFrame.setSize(736, 1104);
        patientsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        patientsFrame.setResizable(false);
        JPanel patientsPanel = new JPanel();
        patientsPanel.setBackground(new Color(182, 238, 237));
        patientsPanel.setLayout(null); // Set layout to null for absolute positioning
        patientsFrame.add(patientsPanel);
        patientsFrame.setLocationRelativeTo(null);
        // Labels and text fields for patient information
        JLabel nameLabel = new JLabel("Nom:");
        nameLabel.setBounds(50, 50, 100, 25);
        patientsPanel.add(nameLabel);

        JTextField nomTextField = new JTextField();
        nomTextField.setBounds(150, 50, 200, 25);
        patientsPanel.add(nomTextField);

        JLabel lastNameLabel = new JLabel("Prenom:");
        lastNameLabel.setBounds(50, 100, 100, 25);
        patientsPanel.add(lastNameLabel);

        JTextField prenomTextField = new JTextField();
        prenomTextField.setBounds(150, 100, 200, 25);
        patientsPanel.add(prenomTextField);

        JLabel telLabel = new JLabel("Telephone:");
        telLabel.setBounds(50, 150, 100, 25);
        patientsPanel.add(telLabel);

        JTextField telephoneTextField = new JTextField();
        telephoneTextField.setBounds(150, 150, 200, 25);
        patientsPanel.add(telephoneTextField);

        // Additional patient information
        JLabel antecedentsLabel = new JLabel("Antecedents:");
        antecedentsLabel.setBounds(50, 200, 100, 25);
        patientsPanel.add(antecedentsLabel);

        JTextArea antecedentsTextArea = new JTextArea();
        antecedentsTextArea.setLineWrap(true); // Enable line wrapping
        antecedentsTextArea.setBounds(150, 200, 500, 60);
        patientsPanel.add(antecedentsTextArea);

        JLabel observationsLabel = new JLabel("Observations:");
        observationsLabel.setBounds(50, 270, 100, 25);
        patientsPanel.add(observationsLabel);

        JTextArea observationsTextArea = new JTextArea();
        observationsTextArea.setLineWrap(true); // Enable line wrapping
        observationsTextArea.setBounds(150, 270, 500, 60);
        patientsPanel.add(observationsTextArea);

        JLabel ordonnanceLabel = new JLabel("Ordonnance:");
        ordonnanceLabel.setBounds(50, 340, 100, 25);
        patientsPanel.add(ordonnanceLabel);

        JTextArea ordonnanceTextArea = new JTextArea();
        ordonnanceTextArea.setLineWrap(true); // Enable line wrapping
        ordonnanceTextArea.setBounds(150, 340, 500, 60);
        patientsPanel.add(ordonnanceTextArea);

        // Button for saving patient information
        JButton saveButton = new JButton("Savegarder");
        saveButton.setBounds(300, 410, 100, 30);
        saveButton.setBackground(new Color(22, 189, 179));
        patientsPanel.add(saveButton);

        // Button for deleting selected patient
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.setBounds(150, 410, 100, 30);
        deleteButton.setBackground(new Color(22, 189, 179));
        patientsPanel.add(deleteButton);

        // Button for editing selected patient
        JButton editButton = new JButton("Modifier");
        editButton.setBounds(450, 410, 100, 30);
        editButton.setBackground(new Color(22, 189, 179));
        patientsPanel.add(editButton);

        // Table
        String[] columnNames = {"Nom", "Prenom", "Telephone", "Antecedents", "Observations", "Ordonnance"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        JTable patientTable = new JTable();
        patientTable.setModel(model);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBounds(50, 470, 630, 300);
        patientsPanel.add(scrollPane);

        // Load existing patients into the table
        String query = "SELECT * FROM Patient";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                String prenom = resultSet.getString("Prenom");
                String telephone = resultSet.getString("Telephone");
                String antecedents = resultSet.getString("Antecedents");
                String observations = resultSet.getString("Observations");
                String ordonnance = resultSet.getString("Ordonnance");
                model.addRow(new Object[]{nom, prenom, telephone, antecedents, observations, ordonnance});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Save button action listener
        saveButton.addActionListener(e -> {
            // Retrieve data from text fields
            String nom = nomTextField.getText();
            String prenom = prenomTextField.getText();
            String telephone = telephoneTextField.getText();
            String antecedents = antecedentsTextArea.getText();
            String observations = observationsTextArea.getText();
            String ordonnance = ordonnanceTextArea.getText();

            // Insert new patient into the database
            String insertQuery = "INSERT INTO Patient (Nom, Prenom, Telephone, Antecedents, Observations, Ordonnance) VALUES (?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, nom);
                insertStatement.setString(2, prenom);
                insertStatement.setString(3, telephone);
                insertStatement.setString(4, antecedents);
                insertStatement.setString(5, observations);
                insertStatement.setString(6, ordonnance);
                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    model.addRow(new Object[]{nom, prenom, telephone, antecedents, observations, ordonnance});
                    JOptionPane.showMessageDialog(saveButton, "Patient ajoute avec succes!");
                } else {
                    JOptionPane.showMessageDialog(saveButton, "Echec de l'ajout du patient!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Handle SQL exception
            }

            // Clear text fields after saving
            nomTextField.setText("");
            prenomTextField.setText("");
            telephoneTextField.setText("");
            antecedentsTextArea.setText("");
            observationsTextArea.setText("");
            ordonnanceTextArea.setText("");
        });

        // Delete button action listener
        deleteButton.addActionListener(e -> {
            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow != -1) {
                String nom = (String) model.getValueAt(selectedRow, 0);
                String prenom = (String) model.getValueAt(selectedRow, 1);

                String deleteQuery = "DELETE FROM Patient WHERE Nom = ? AND Prenom = ?";
                try {
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setString(1, nom);
                    deleteStatement.setString(2, prenom);
                    int rowsDeleted = deleteStatement.executeUpdate();
                    if (rowsDeleted > 0) {
                        model.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(deleteButton, "Patient supprimé avec succés!");
                    } else {
                        JOptionPane.showMessageDialog(deleteButton, "échec de la suppression du patient!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // Handle SQL exception
                }

                // Clear input fields after updating
                nomTextField.setText("");
                prenomTextField.setText("");
                telephoneTextField.setText("");
                antecedentsTextArea.setText("");
                observationsTextArea.setText("");
                ordonnanceTextArea.setText("");

            } else {
                JOptionPane.showMessageDialog(deleteButton, "Veuillez slectionner un patient a supprimer !");
            }
        });

        // Add a ListSelectionListener to the JTable
        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Populate the input fields with the selected patient's information
                    nomTextField.setText((String) model.getValueAt(selectedRow, 0));
                    prenomTextField.setText((String) model.getValueAt(selectedRow, 1));
                    telephoneTextField.setText((String) model.getValueAt(selectedRow, 2));
                    antecedentsTextArea.setText((String) model.getValueAt(selectedRow, 3));
                    observationsTextArea.setText((String) model.getValueAt(selectedRow, 4));
                    ordonnanceTextArea.setText((String) model.getValueAt(selectedRow, 5));
                }
            }
        });

// Edit button action listener
        editButton.addActionListener(e -> {
            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get the selected patient's information from input fields
                String nom = nomTextField.getText();
                String prenom = prenomTextField.getText();
                String telephone = telephoneTextField.getText();
                String antecedents = antecedentsTextArea.getText();
                String observations = observationsTextArea.getText();
                String ordonnance = ordonnanceTextArea.getText();

                // Update the selected patient's information in the table and the database
                model.setValueAt(nom, selectedRow, 0);
                model.setValueAt(prenom, selectedRow, 1);
                model.setValueAt(telephone, selectedRow, 2);
                model.setValueAt(antecedents, selectedRow, 3);
                model.setValueAt(observations, selectedRow, 4);
                model.setValueAt(ordonnance, selectedRow, 5);

                // Implement logic to update the patient's information in the database
                String updateQuery = "UPDATE Patient SET Nom=?, Prenom=?, Telephone=?, Antecedents=?, Observations=?, Ordonnance=? WHERE Nom=? AND Prenom=?";
                try {
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, nom);
                    updateStatement.setString(2, prenom);
                    updateStatement.setString(3, telephone);
                    updateStatement.setString(4, antecedents);
                    updateStatement.setString(5, observations);
                    updateStatement.setString(6, ordonnance);
                    updateStatement.setString(7, (String) model.getValueAt(selectedRow, 0)); // Previous Nom
                    updateStatement.setString(8, (String) model.getValueAt(selectedRow, 1)); // Previous Prenom

                    int rowsUpdated = updateStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(editButton, "Patient modifer avec succes!");
                    } else {
                        JOptionPane.showMessageDialog(editButton, "echec de la modification du patient!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // Handle SQL exception
                }

                // Clear input fields after updating
                nomTextField.setText("");
                prenomTextField.setText("");
                telephoneTextField.setText("");
                antecedentsTextArea.setText("");
                observationsTextArea.setText("");
                ordonnanceTextArea.setText("");
            } else {
                JOptionPane.showMessageDialog(editButton, "Veuillez selectionner un patient a modifier!");
            }
        });

        patientsFrame.setVisible(true);
    }


    private void openRendezVousFrame() {
        JFrame rendezVousFrame = new JFrame("Rendez Vous");
        rendezVousFrame.setSize(736, 1104);
        rendezVousFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rendezVousFrame.setResizable(false);
        JPanel rendezVousPanel = new JPanel();
        rendezVousPanel.setBackground(new Color(182, 238, 237));
        rendezVousPanel.setLayout(null); // Set layout to null for absolute positioning
        rendezVousFrame.add(rendezVousPanel);
        rendezVousFrame.setLocationRelativeTo(null);
        // JLabels
        JLabel nomLabel = new JLabel("Nom:");
        nomLabel.setBounds(50, 50, 100, 25);
        rendezVousPanel.add(nomLabel);

        JLabel prenomLabel = new JLabel("Prenom:");
        prenomLabel.setBounds(50, 100, 100, 25);
        rendezVousPanel.add(prenomLabel);

        JLabel dateTimeLabel = new JLabel("Date et Heure:");
        dateTimeLabel.setBounds(50, 150, 150, 25);
        rendezVousPanel.add(dateTimeLabel);

        // JTextFields
        JTextField nomTextField = new JTextField();
        nomTextField.setBounds(150, 50, 200, 25);
        rendezVousPanel.add(nomTextField);

        JTextField prenomTextField = new JTextField();
        prenomTextField.setBounds(150, 100, 200, 25);
        rendezVousPanel.add(prenomTextField);

        // Combo boxes for date and hour selection
        String[] annee = {"2024", "2025", "2026"}; // Sample years, you can adjust these
        JComboBox<String> anneeComboBox = new JComboBox<>(annee);
        anneeComboBox.setBounds(150, 150, 100, 25);
        rendezVousPanel.add(anneeComboBox);

        String[] mois = {"janvier", "fevrier", "mars", "avril", "mai", "juin", "juillet", "aout", "septembre", "octobre", "novembre", "decembre"};
        JComboBox<String> moisComboBox = new JComboBox<>(mois);
        moisComboBox.setBounds(260, 150, 100, 25);
        rendezVousPanel.add(moisComboBox);

        String[] jour = new String[31];
        for (int i = 0; i < 31; i++) {
            jour[i] = String.valueOf(i + 1);
        }
        JComboBox<String> jourComboBox = new JComboBox<>(jour);
        jourComboBox.setBounds(370, 150, 100, 25);
        rendezVousPanel.add(jourComboBox);

        String[] heure = new String[24];
        for (int i = 0; i < 24; i++) {
            heure[i] = String.format("%02d", i);
        }
        JComboBox<String> heureComboBox = new JComboBox<>(heure);
        heureComboBox.setBounds(480, 150, 60, 25);
        rendezVousPanel.add(heureComboBox);

        // Submit Button
        JButton submitButton = new JButton("Sauvegarder");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nom = nomTextField.getText();
                String prenom = prenomTextField.getText();
                String annee = (String) anneeComboBox.getSelectedItem();
                String mois = (String) moisComboBox.getSelectedItem();
                String jour = (String) jourComboBox.getSelectedItem();
                String heure = (String) heureComboBox.getSelectedItem();
                String query1 = "INSERT INTO rendez_vous VALUES ('" + nom + "','" + prenom + "','" + annee + "','" + mois + "','" + jour + "','" + heure + "')";

                try {
                    statement = connection.createStatement();
                    statement.executeUpdate(query1);
                    JOptionPane.showMessageDialog(submitButton, "Insertion reussie :)");
                    refreshTable(); // Refresh the table after insertion

                    // Clear text fields and reset combo boxes
                    nomTextField.setText("");
                    prenomTextField.setText("");
                    anneeComboBox.setSelectedIndex(0);
                    moisComboBox.setSelectedIndex(0);
                    jourComboBox.setSelectedIndex(0);
                    heureComboBox.setSelectedIndex(0);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        submitButton.setBounds(150, 200, 110, 30);
        submitButton.setBackground(new Color(22, 189, 179)); // Set background color
        rendezVousPanel.add(submitButton);

        // Table
        String[] columnNames = {"Nom ", "Prenom", "Annee", "Mois", "Jour", "Heure"};

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        table = new JTable();
        table.setModel(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 250, 600, 400);
        rendezVousPanel.add(scrollPane);

        // Populate table with existing data
        populateTable(model);

        // Delete Button
        JButton deleteButton = new JButton("Effacer");
        deleteButton.setBounds(300, 200, 100, 30);
        deleteButton.setBackground(new Color(22, 189, 179));
        rendezVousPanel.add(deleteButton);

        // Add action listener for the delete button
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String nom = (String) table.getValueAt(selectedRow, 0);
                    String prenom = (String) table.getValueAt(selectedRow, 1);
                    String annee = (String) table.getValueAt(selectedRow, 2);
                    String mois = (String) table.getValueAt(selectedRow, 3);
                    String jour = (String) table.getValueAt(selectedRow, 4);
                    String heure = (String) table.getValueAt(selectedRow, 5);

                    String deleteQuery = "DELETE FROM rendez_vous WHERE nom = ? AND prenom = ? AND annee = ? AND mois = ? AND jour = ? AND heure = ?";
                    try {
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.setString(1, nom);
                        deleteStatement.setString(2, prenom);
                        deleteStatement.setString(3, annee);
                        deleteStatement.setString(4, mois);
                        deleteStatement.setString(5, jour);
                        deleteStatement.setString(6, heure);
                        int rowsDeleted = deleteStatement.executeUpdate();
                        if (rowsDeleted > 0) {
                            DefaultTableModel model = (DefaultTableModel) table.getModel();
                            model.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(deleteButton, "Rendez-vous supprimé avec succees!");
                        } else {
                            JOptionPane.showMessageDialog(deleteButton, "Echec suppression rendez-vous!");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        // Handle SQL exception
                    }
                } else {
                    JOptionPane.showMessageDialog(deleteButton, "Selectionner un rendez-vous a  effacer!");
                }
            }
        });

        rendezVousFrame.setVisible(true);
    }

    // Method to populate the table with existing data from the database
    private void populateTable(DefaultTableModel model) {
        try {
            String query = "SELECT * FROM rendez_vous";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String annee = resultSet.getString("annee");
                String mois = resultSet.getString("mois");
                String jour = resultSet.getString("jour");
                String heure = resultSet.getString("heure");
                model.addRow(new Object[]{nom, prenom, annee, mois, jour, heure});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to refresh the table after data insertion
    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing data
        populateTable(model); // Repopulate the table with updated data
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameTextField.getText();
            char[] passwordChars = passwordTextField.getPassword();
            String password = new String(passwordChars);

            if (users.containsKey(username) && users.get(username).equals(password)) {
                successLabel.setText("connecté!");
                secondFrame.setVisible(true);
                usernameTextField.setText("");
                passwordTextField.setText("");
                successLabel.setText("");
            } else {
                successLabel.setText("Utilisateur ou mot de passe invalide. Veuillez reessayer.");
            }
        } else if (e.getSource() == clearButton) {
            usernameTextField.setText("");
            passwordTextField.setText("");
            successLabel.setText("");
        }
    }

    public static void main(String[] args){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","poo","cabmed");
        }catch (Exception e1) {
            e1.printStackTrace();
        }
        new LoginGUI();

    }}