import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class PlayerInfoViewer extends JFrame implements ActionListener {
    // GUI Components
    JTextField tName, tNumber, tPosition, tGoals, tSearch;
    JButton bAdd, bShowAll, bSearch, bClear, bDeleteAll;
    JLabel IblTitle, IName, INumber, IPosition, IGoals, ISearch;
    JTextArea display;
    
    String fileName = "players.csv";

    PlayerInfoViewer() {
        // JFrame setup
        setTitle("Sports Team Player Info Viewer");
        setSize(550, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Title Label
        IblTitle = new JLabel("Sports Team Player Info Viewer");
        IblTitle.setBounds(150, 20, 300, 25);
        IblTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        add(IblTitle);

        // Input Labels and TextFields
        IName = new JLabel("Name:");
        INumber = new JLabel("Jersey Number:");
        IPosition = new JLabel("Position:");
        IGoals = new JLabel("Goals Scored:");
        
        tName = new JTextField();
        tNumber = new JTextField();
        tPosition = new JTextField();
        tGoals = new JTextField();

        IName.setBounds(90, 70, 130, 25);
        INumber.setBounds(90, 110, 130, 25);
        IPosition.setBounds(90, 150, 130, 25);
        IGoals.setBounds(90, 190, 130, 25);

        tName.setBounds(250, 70, 150, 25);
        tNumber.setBounds(250, 110, 150, 25);
        tPosition.setBounds(250, 150, 150, 25);
        tGoals.setBounds(250, 190, 150, 25);

        add(IName); add(tName);
        add(INumber); add(tNumber);
        add(IPosition); add(tPosition);
        add(IGoals); add(tGoals);

        // Buttons
        bAdd = new JButton("Add Player");
        bShowAll = new JButton("Show All");
        bSearch = new JButton("Search");
        bClear = new JButton("Clear");
        bDeleteAll = new JButton("Delete All");
        
        ISearch = new JLabel("Search Name/Num:");
        tSearch = new JTextField();

        bAdd.setBounds(30, 240, 100, 30);
        bShowAll.setBounds(140, 240, 100, 30);
        bSearch.setBounds(390, 280, 100, 30);
        bClear.setBounds(250, 240, 100, 30);
        bDeleteAll.setBounds(360, 240, 100, 30);
        
        ISearch.setBounds(30, 280, 120, 30);
        tSearch.setBounds(160, 280, 220, 30);

        add(bAdd); add(bShowAll); add(bSearch);
        add(bClear); add(bDeleteAll);
        add(ISearch); add(tSearch);

        // Display Area
        display = new JTextArea();
        display.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(display);
        scrollPane.setBounds(50, 330, 450, 200);
        add(scrollPane);

        // Register Listeners
        bAdd.addActionListener(this);
        bShowAll.addActionListener(this);
        bSearch.addActionListener(this);
        bClear.addActionListener(this);
        bDeleteAll.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bAdd) {
            addPlayer();
        } else if (e.getSource() == bShowAll) {
            showAllPlayers();
        } else if (e.getSource() == bSearch) {
            searchPlayer();
        } else if (e.getSource() == bClear) {
            clearFields();
        } else if (e.getSource() == bDeleteAll) {
            deleteAll();
        }
    }

    // Method to add a new player to the CSV file
    void addPlayer() {
        try {
            String name = tName.getText().trim();
            String number = tNumber.getText().trim();
            String position = tPosition.getText().trim();
            String goals = tGoals.getText().trim();

            if (name.isEmpty() || number.isEmpty() || position.isEmpty() || goals.isEmpty()) {
                display.setText("Error: All fields must be filled.");
                return;
            }

            FileWriter fw = new FileWriter(fileName, true); // Append mode
            fw.write(name + "," + number + "," + position + "," + goals + "\n");
            fw.close();

            display.setText("Player added successfully: " + name);
            // Clear input fields after successful addition
            tName.setText("");
            tNumber.setText("");
            tPosition.setText("");
            tGoals.setText("");

        } catch (IOException e) {
            display.setText("Error adding player: File I/O issue.");
        } catch (Exception e) {
            display.setText("An unexpected error occurred while adding member.");
        }
    }

    // Method to display all players from the CSV file
    void showAllPlayers() {
        display.setText("");
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            display.append("--- ALL PLAYERS ---\n");
            display.append("Name, Jersey No., Position, Goals Scored\n");
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    display.append(line + "\n");
                }
            }
            display.append("-------------------\n");
            br.close();
            
        } catch (FileNotFoundException e) {
            display.setText("Error: Data file 'players.csv' not found.");
        } catch (IOException e) {
            display.setText("Error reading player data.");
        }
    }

    // Method to search for a specific player by Name or Jersey Number
    void searchPlayer() {
        display.setText("");
        String searchTerm = tSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            display.setText("Please enter a name or jersey number to search.");
            return;
        }

        List<String> results = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    // Search by Name (data[0]) or Jersey Number (data[1])
                    if (data[0].equalsIgnoreCase(searchTerm) || data[1].equals(searchTerm)) {
                        results.add(line);
                    }
                }
            }
            br.close();

            if (results.isEmpty()) {
                display.setText("No player found matching: " + searchTerm);
            } else {
                display.append("--- SEARCH RESULTS ---\n");
                display.append("Name, Jersey No., Position, Goals Scored\n");
                for (String player : results) {
                    display.append(player + "\n");
                }
                display.append("---------------------\n");
            }
            
        } catch (FileNotFoundException e) {
            display.setText("Error: Data file 'players.csv' not found.");
        } catch (IOException e) {
            display.setText("Error reading player data during search.");
        }
    }

    // Method to clear all input fields and the display area
    void clearFields() {
        tName.setText("");
        tNumber.setText("");
        tPosition.setText("");
        tGoals.setText("");
        tSearch.setText("");
        display.setText("Input fields cleared.");
    }

    // Method to delete all records from the CSV file
    void deleteAll() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete ALL player records?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if(dialogResult == JOptionPane.YES_OPTION){
            try {
                // Open file in non-append mode (overwrite) and write nothing
                FileWriter fw = new FileWriter(fileName, false); 
                fw.write("");
                fw.close();
                display.setText("All player details deleted.");
            } catch (IOException e) {
                display.setText("Error deleting data: File I/O issue.");
            }
        } else {
            display.setText("Deletion cancelled.");
        }
    }

    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread-safe GUI creation
        SwingUtilities.invokeLater(() -> new PlayerInfoViewer());
    }
}