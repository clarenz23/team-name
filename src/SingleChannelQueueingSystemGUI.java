import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SingleChannelQueueingSystemGUI extends JFrame implements ActionListener {
    private JLabel terminationOptionLabel, valueLabel;
    private JComboBox<String> terminationOptionCombo;
    private JTextField valueField;
    private JButton startButton;
    private JButton endButton;
    private JButton resetButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel avgWaitingTimeLabel, probCustomerWaitsLabel, propIdleTimeLabel,
            avgServiceTimeLabel, avgInterarrivalTimeLabel, avgQueueWaitTimeLabel, avgCustomerSpendsLabel, simulationTerminationLabel;

    public SingleChannelQueueingSystemGUI() {
        // Set up the GUI
        setTitle("Single Channel Queueing System Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1500, 750));
        setLayout(new BorderLayout());

        // Create the input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createTitledBorder("Simulation"));
        inputPanel.setLayout(new GridLayout(2, 2));

        // Add the termination option
        terminationOptionLabel = new JLabel("Termination Option:");
        terminationOptionCombo = new JComboBox<String>(new String[]{"Based on number of customers", "Based on number of minutes"});
        inputPanel.add(terminationOptionLabel);
        inputPanel.add(terminationOptionCombo);

        // Add the value input field
        valueLabel = new JLabel("Enter a number:");
        valueField = new JTextField();
        valueField.setPreferredSize(new Dimension(100, 25));
        inputPanel.add(valueLabel);
        inputPanel.add(valueField);

        // Add a KeyListener to validate the input and display an error message
        valueField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                    // Consume the key event to prevent the invalid character from being entered
                    e.consume();
                }
            }

            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    char c = valueField.getText().charAt(0);
                    if (!(Character.isDigit(c))) {
                        // Display an error message if the input is not a valid digit
                        JOptionPane.showMessageDialog(inputPanel, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Call the actionPerformed method of the startButton when the Enter key is pressed
                        startButton.doClick();
                    }
                }
            }
        });

        // Create the start button
        startButton = new JButton("Start Simulation");
        startButton.addActionListener(this);
        startButton.setPreferredSize(new Dimension(150, 45));

        // Create the end button
        endButton = new JButton("End Simulation");
        endButton.addActionListener(e -> System.exit(0));
        endButton.setPreferredSize(new Dimension(150, 45));

        // Create te reset button
        resetButton = new JButton("Reset Simulation");
        resetButton.addActionListener(e -> {
            valueField.setText("");
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
        });
        resetButton.setPreferredSize(new Dimension(150, 45));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(endButton);
        buttonPanel.add(resetButton);

        // Create the table
        tableModel = new DefaultTableModel(new String[]{"CUSTOMER NO.", "INTERARRIVAL TIME (MINS)", "ARRIVAL TIME (MINS)", "SERVICE TIME (MINS)", "TIME SERVICE BEGINS", "WAITING TIME", "TIME SERVICE ENDS", "CUSTOMER SPENDS IN SYSTEM", "IDLE TIME OF SERVER (MINS)"}, 0);
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Add the table to a scroll pane and add the scroll pane to the GUI
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create the Performance metrics panel
        JPanel statPanel = new JPanel();
        statPanel.setBorder(BorderFactory.createTitledBorder("Performance Metrics"));
        statPanel.setPreferredSize(new Dimension(350,280));
        statPanel.setLayout(new GridLayout(0,1));


        // Add the performance metrics labels
        avgInterarrivalTimeLabel = new JLabel("Average interarrival time:");
        statPanel.add(avgInterarrivalTimeLabel);
        avgInterarrivalTimeLabel.setLabelFor(table);

        avgServiceTimeLabel = new JLabel("Average service time:");
        statPanel.add(avgServiceTimeLabel);
        avgServiceTimeLabel.setLabelFor(table);

        propIdleTimeLabel = new JLabel("Proportion of time server is idle:");
        statPanel.add(propIdleTimeLabel);
        propIdleTimeLabel.setLabelFor(table);

        avgWaitingTimeLabel = new JLabel("Average waiting time:");
        statPanel.add(avgWaitingTimeLabel);
        avgWaitingTimeLabel.setLabelFor(table);

        probCustomerWaitsLabel = new JLabel("Probability that a customer waits:");
        statPanel.add(probCustomerWaitsLabel);
        probCustomerWaitsLabel.setLabelFor(table);

        avgQueueWaitTimeLabel = new JLabel("Average queue wait time:");
        statPanel.add(avgQueueWaitTimeLabel);
        avgQueueWaitTimeLabel.setLabelFor(table);

        avgCustomerSpendsLabel = new JLabel("Average customer spends in system:");
        statPanel.add(avgCustomerSpendsLabel);
        avgCustomerSpendsLabel.setLabelFor(table);

        // Add the performance metrics values
        statPanel.add(new JLabel());
        statPanel.add(new JLabel());
        statPanel.add(new JLabel());
        statPanel.add(new JLabel());
        statPanel.add(new JLabel());
        statPanel.add(new JLabel());
        statPanel.add(new JLabel());
        statPanel.add(new JLabel());

        // Add the input panel and start button to the GUI
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.PAGE_END);
        add(statPanel, BorderLayout.EAST);

        // Pack and display the GUI
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SingleChannelQueueingSystemGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the selected termination option and value
        int option = terminationOptionCombo.getSelectedIndex() + 1;
        int value = 0;
        try {
            value = Integer.parseInt(valueField.getText());
            if (value <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            valueField.setText("");
            return;
        }

        // Clear the table model
        tableModel.setRowCount(0);

        // Simulation variables
        int customerNumber = 1;
        int interarrivalTime = 0;
        int arrivalTime = 0;
        int serviceTime = 0;
        int timeServiceBegins = 0;
        int waitingTime = 0;
        int timeServiceEnds = 0;
        int customerSpendsInSystem = 0;
        int idleTimeOfServer = 0;
        int totalWaitingTime = 0;
        int totalQueueTime = 0;
        int totalServiceTime = 0;
        int totalCustomerSpends = 0;
        int totalInterarrivalTime = 0;
        int numCustomersInQueue = 0;
        int numOfWaitingCustomers = 0;
        int totalIdleTimeOfServer = 0;

        while ((option == 1 && customerNumber <= value) || (option == 2 && totalCustomerSpends < value)) {
            // Generate interarrival time based on distribution
            if (customerNumber == 1) {
                interarrivalTime = 0;
                arrivalTime = 0;
            } else {
                double rand1 = Math.random();
                if (rand1 < 0.125) {
                    interarrivalTime = 1;
                } else if (rand1 < 0.250) {
                    interarrivalTime = 2;
                } else if (rand1 < 0.375) {
                    interarrivalTime = 3;
                } else if (rand1 < 0.500) {
                    interarrivalTime = 4;
                } else if (rand1 < 0.625) {
                    interarrivalTime = 5;
                } else if (rand1 < 0.750) {
                    interarrivalTime = 6;
                } else if (rand1 < 0.875) {
                    interarrivalTime = 7;
                } else {
                    interarrivalTime = 8;
                }
            }

            //Generate service time based on distribution
            double rand2 = Math.random();
            if (rand2 < 0.15) {
                serviceTime = 1;
            } else if (rand2 < 0.45) {
                serviceTime = 2;
            } else if (rand2 < 0.70) {
                serviceTime = 3;
            } else if (rand2 < 0.90) {
                serviceTime = 4;
            } else {
                serviceTime = 5;
            }

            // Calculate values for simulation table
            arrivalTime = arrivalTime + interarrivalTime;
            timeServiceBegins = Math.max(arrivalTime, timeServiceEnds);
            waitingTime = timeServiceBegins - arrivalTime;
            int prevTimeServiceEnds = timeServiceEnds;
            timeServiceEnds = timeServiceBegins + serviceTime;
            customerSpendsInSystem = timeServiceEnds - arrivalTime;

            if (customerNumber > 1) {
                if (arrivalTime > prevTimeServiceEnds) {
                    idleTimeOfServer = arrivalTime - prevTimeServiceEnds;
                } else {
                    idleTimeOfServer = prevTimeServiceEnds - arrivalTime;
                }
            }

            totalInterarrivalTime += interarrivalTime;
            totalServiceTime += serviceTime;

            totalWaitingTime += waitingTime;
            if (waitingTime > 0) {
                numOfWaitingCustomers++;
            }


            totalCustomerSpends += customerSpendsInSystem;
            totalIdleTimeOfServer += idleTimeOfServer;


            // Print simulation table row
            tableModel.addRow(new Object[]{customerNumber, interarrivalTime, arrivalTime, serviceTime, timeServiceBegins, waitingTime, timeServiceEnds, customerSpendsInSystem, idleTimeOfServer});

            // Increment customer number
            customerNumber++;
            numCustomersInQueue--;
        }
        tableModel.addRow(new Object[]{"Total", totalInterarrivalTime, "", totalServiceTime, "", totalWaitingTime, "", totalCustomerSpends, totalIdleTimeOfServer});
        
        // Calculate performance metrics
        double avgWaitingTime = (double) totalWaitingTime / (customerNumber-1);
        String formatAvgWaitingTime = String.format("%.2f", avgWaitingTime);
        double probCustomerWaits = (double) numOfWaitingCustomers / (customerNumber-1) * 100;
        String formatProbCustomerWaits = String.format("%.2f%%", probCustomerWaits);
        double propIdleTime = (double) totalIdleTimeOfServer / timeServiceEnds * 100;
        String formatPropIdleTime = String.format("%.2f%%", propIdleTime);
        double avgServiceTime = (double) totalServiceTime / (customerNumber-1);
        String formatAvgServiceTime = String.format("%.2f", avgServiceTime);
        double avgInterarrivalTime = (double) totalInterarrivalTime / (customerNumber - 1);
        String formatAvgInterarrivalTime = String.format("%.2f", avgInterarrivalTime);
        double avgQueueWaitTime;
        if (numOfWaitingCustomers == 0) {
            avgQueueWaitTime = 0.0;
        } else {
            avgQueueWaitTime = (double) totalWaitingTime / numOfWaitingCustomers;
        }
        String formatAvgQueueWaitTime = String.format("%.2f", avgQueueWaitTime);
        double avgCustomerSpends = (double) totalCustomerSpends/ (customerNumber-1);
        String formatAvgCustomerSpends = String.format("%.2f", avgCustomerSpends);

        avgWaitingTimeLabel.setText("Average Waiting Time: " + formatAvgWaitingTime);
        probCustomerWaitsLabel.setText("Probability that a Customer Waits: " + formatProbCustomerWaits);
        propIdleTimeLabel.setText("Proportion of Idle Time of the Server: " + formatPropIdleTime);
        avgServiceTimeLabel.setText("Average Service Time: " + formatAvgServiceTime);
        avgInterarrivalTimeLabel.setText("Average Interarrival Time: " + formatAvgInterarrivalTime);
        avgQueueWaitTimeLabel.setText("Average Wait Time for Customers who wait: " + formatAvgQueueWaitTime);
        avgCustomerSpendsLabel.setText("Average Customer Spends in the System: " + formatAvgCustomerSpends);

    }
}
