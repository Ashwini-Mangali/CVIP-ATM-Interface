import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class ATMInterface extends JFrame {
    private JTextField pinField;
    private JTextArea outputArea;
    private double accountBalance = 1000.0;

    public ATMInterface() {
        super("ATM Interface");

        setLayout(new BorderLayout());

        JPanel pinPanel = new JPanel(new FlowLayout());
        pinPanel.add(new JLabel("Enter PIN: "));
        pinField = new JTextField(4);
        pinPanel.add(pinField);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 3));
        String[] buttonLabels = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "C", "0", "Enter" };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        outputArea = new JTextArea(10, 25);
        outputArea.setEditable(false);

        add(pinPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonText = ((JButton) e.getSource()).getText();

            if (buttonText.equals("Enter")) {
                processEnterButton();
            } else if (buttonText.equals("C")) {
                clearPinField();
            } else {
                appendToPinField(buttonText);
            }
        }
    }

    private void processEnterButton() {
        String enteredPin = pinField.getText();

        if (authenticateUser(enteredPin)) {
            displayMessage("Login successful. Balance: $" + formatCurrency(accountBalance));
            showTransactionMenu();
        } else {
            displayMessage("Incorrect PIN. Please try again.");
            clearPinField();
        }
    }

    private boolean authenticateUser(String enteredPin) {

        return enteredPin.equals("1234");
    }

    private void showTransactionMenu() {
        String[] options = { "Check Balance", "Deposit", "Withdraw", "Logout" };
        int choice = JOptionPane.showOptionDialog(
                this,
                "Select an option:",
                "Transaction Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0:
                displayMessage("Current Balance: $" + formatCurrency(accountBalance));
                break;
            case 1:
                deposit();
                break;
            case 2:
                withdraw();
                break;
            case 3:
                clearPinField();
                displayMessage("Logged out. Thank you!");
                break;
            default:
                break;
        }
    }

    private void deposit() {
        String input = JOptionPane.showInputDialog(this, "Enter deposit amount:");
        try {
            double amount = Double.parseDouble(input);
            accountBalance += amount;
            displayMessage("Deposit successful. New Balance: $" + formatCurrency(accountBalance));
        } catch (NumberFormatException e) {
            displayMessage("Invalid input. Please enter a valid amount.");
        }
    }

    private void withdraw() {
        String input = JOptionPane.showInputDialog(this, "Enter withdrawal amount:");
        try {
            double amount = Double.parseDouble(input);
            if (amount > accountBalance) {
                displayMessage("Insufficient funds.");
            } else {
                accountBalance -= amount;
                displayMessage("Withdrawal successful. New Balance: $" + formatCurrency(accountBalance));
            }
        } catch (NumberFormatException e) {
            displayMessage("Invalid input. Please enter a valid amount.");
        }
    }

    private void appendToPinField(String text) {
        pinField.setText(pinField.getText() + text);
    }

    private void clearPinField() {
        pinField.setText("");
    }

    private void displayMessage(String message) {
        outputArea.append(message + "\n");
    }

    private String formatCurrency(double amount) {
        DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
        return currencyFormat.format(amount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMInterface());
    }
}
