import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SingleChannelQueueingSystemGUI extends JFrame implements ActionListener {
    private JLabel terminationOptionLabel, valueLabel;
    private JComboBox<String> terminationOptionCombo;
    private JTextField valueField;
    private JButton startButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public SingleChannelQueueingSystemGUI() {
        // Set up the GUI
        setTitle("Single Channel Queueing System Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
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
        valueLabel = new JLabel("Value:");
        valueField = new JTextField();
        inputPanel.add(valueLabel);
        inputPanel.add(valueField);

        // Create the start button
        startButton = new JButton("Start Simulation");
        startButton.addActionListener(this);

        // Add the input panel and start button to the GUI
        add(inputPanel, BorderLayout.NORTH);
        add(startButton, BorderLayout.SOUTH);

        // Create the table
        tableModel = new DefaultTableModel(new String[]{"CUSTOMER NO.", "INTERARRIVAL TIME (MINS)", "ARRIVAL TIME (MINS)", "SERVICE TIME (MINS)", "TIME SERVICE BEGINS", "WAITING TIME", "TIME SERVICE ENDS", "CUSTOMER SPENDS IN SYSTEM", "IDLE TIME OF SERVER (MINS)"}, 0);
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < 9; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set the grid color to be the same as the background color
        table.setGridColor(table.getBackground());

        // Add the table to a scroll pane and add the scroll pane to the GUI
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

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
        tableModel.addRow(new Object[]{"", totalInterarrivalTime, "", totalServiceTime, "", totalWaitingTime, "", totalCustomerSpends, totalIdleTimeOfServer});

        // Calculate performance metrics
        double avgWaitingTime = (double) totalWaitingTime / (customerNumber - 1);
        String formatAvgWaitingTime = String.format("%.2f", avgWaitingTime);
        double probCustomerWaits = (double) numOfWaitingCustomers / (customerNumber - 1) * 100;
        String formatProbCustomerWaits = String.format("%.2f%%", probCustomerWaits);
        double propIdleTime = (double) totalIdleTimeOfServer / timeServiceEnds * 100;
        String formatPropIdleTime = String.format("%.2f%%", propIdleTime);
        double avgServiceTime = (double) totalServiceTime / (customerNumber - 1);
        String formatAvgServiceTime = String.format("%.2f", avgServiceTime);
        double avgInterarrivalTime = (double) totalInterarrivalTime / (customerNumber - 1);
        String formatAvgInterarrivalTime = String.format("%.2f", avgInterarrivalTime);
        double avgQueueWaitTime = (double) totalWaitingTime / numOfWaitingCustomers;
        String formatAvgQueueWaitTime = String.format("%.2f", avgQueueWaitTime);
        double avgCustomerSpends = (double) totalCustomerSpends / (customerNumber - 1);
        String formatAvgCustomerSpends = String.format("%.2f", avgCustomerSpends);

        tableModel.addRow(new Object[]{"Average waiting time for a customer: ", "" + formatAvgWaitingTime + " minutes"});
        tableModel.addRow(new Object[]{"Probability that a customer waits: ", "" + formatProbCustomerWaits});
        tableModel.addRow(new Object[]{"Proportion of time server is idle: ", "" + formatPropIdleTime});
        tableModel.addRow(new Object[]{"Average service time per customer: ", "" + formatAvgServiceTime + " minutes"});
        tableModel.addRow(new Object[]{"Average interarrival time between customers: ", "" + formatAvgInterarrivalTime + " minutes"});
        tableModel.addRow(new Object[]{"Average queue wait time for a customer: ", "" + formatAvgQueueWaitTime + " minutes"});
        tableModel.addRow(new Object[]{"Average customer spends in the system: ", "" + formatAvgCustomerSpends + " minutes"});
    }
}
