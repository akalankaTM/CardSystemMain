package view;

import dao.CreditCardDAO;
import dao.CustomerDAO;
import dao.TransactionDAO;
import model.CreditCard;
import model.Customer;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class TransactionPanel extends JPanel {

    private CreditCardDAO  cardDAO        = new CreditCardDAO();
    private CustomerDAO    customerDAO    = new CustomerDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    private JComboBox<String> customerBox = new JComboBox<>();
    private JComboBox<String> cardBox     = new JComboBox<>();
    private JComboBox<String> typeBox     = new JComboBox<>(new String[]{"CHARGE", "PAYMENT"});
    private JTextField amountField        = new JTextField(20);
    private JTextField accountField       = new JTextField(20);
    private JTextField descField          = new JTextField(20);

    private List<Customer>   customers;
    private List<CreditCard> cards;

    public TransactionPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("  New Transaction", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(13, 27, 62));
        title.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 0));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230)),
            BorderFactory.createEmptyBorder(30, 50, 30, 50)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(10, 10, 10, 10);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.WEST;

        styleField(amountField);
        styleField(accountField);
        styleField(descField);
        styleCombo(customerBox);
        styleCombo(cardBox);
        styleCombo(typeBox);

        JButton submitBtn = new JButton("SUBMIT TRANSACTION");
        submitBtn.setBackground(new Color(0, 180, 216));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        addRow(form, gbc, 0, "Customer:",       customerBox);
        addRow(form, gbc, 1, "Card:",           cardBox);
        addRow(form, gbc, 2, "Type:",           typeBox);
        addRow(form, gbc, 3, "Amount (LKR):",  amountField);
        addRow(form, gbc, 4, "To Account No:", accountField);
        addRow(form, gbc, 5, "Description:",   descField);

        gbc.gridx = 1; gbc.gridy = 6;
        form.add(submitBtn, gbc);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(240, 245, 255));
        center.add(form);

        loadCustomers();
        customerBox.addActionListener(e -> loadCardsForSelectedCustomer());
        submitBtn.addActionListener(e -> submitTransaction());

        add(title,  BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    private void loadCustomers() {
        customers = customerDAO.getAllCustomers();
        customerBox.removeAllItems();
        for (Customer c : customers) {
            customerBox.addItem(c.getCustomerId() + " - " + c.getName());
        }
        loadCardsForSelectedCustomer();
    }

    private void loadCardsForSelectedCustomer() {
        cardBox.removeAllItems();
        int index = customerBox.getSelectedIndex();
        if (index < 0 || customers == null) return;

        int customerId = customers.get(index).getCustomerId();
        cards = cardDAO.getAllCards();

        for (CreditCard card : cards) {
            if (card.getCustomerId() == customerId) {
                cardBox.addItem(card.getCardId() + " - " + card.getCardNumber()
                    + " (Available: " + String.format("%.2f",
                    card.getCreditLimit() - card.getCurrentBalance()) + ")");
            }
        }
    }

    private void submitTransaction() {
        try {
            if (cardBox.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(this, "No card selected.");
                return;
            }

            String cardItem = (String) cardBox.getSelectedItem();
            int cardId = Integer.parseInt(cardItem.split(" - ")[0].trim());

            double amount        = Double.parseDouble(amountField.getText().trim());
            String type          = (String) typeBox.getSelectedItem();
            String desc          = descField.getText().trim();
            String accountNumber = accountField.getText().trim();

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.");
                return;
            }

            if (accountNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter account number.");
                return;
            }

            CreditCard card = cardDAO.getCardById(cardId);
            if (card == null) {
                JOptionPane.showMessageDialog(this, "Card not found.");
                return;
            }
            if (!card.getStatus().equals("ACTIVE")) {
                JOptionPane.showMessageDialog(this, "Card is not ACTIVE.");
                return;
            }

            if (type.equals("CHARGE")) {
                double available = card.getCreditLimit() - card.getCurrentBalance();
                if (amount > available) {
                    JOptionPane.showMessageDialog(this,
                        "Credit limit exceeded!\nAvailable: LKR " + String.format("%.2f", available));
                    return;
                }
                double newBalance = card.getCurrentBalance() + amount;
                cardDAO.updateBalance(cardId, newBalance);
                if (newBalance >= card.getCreditLimit()) {
                    cardDAO.updateStatus(cardId, "BLOCKED");
                    JOptionPane.showMessageDialog(this,
                        "Card BLOCKED - Credit limit reached!");
                }
            } else {
                if (card.getCurrentBalance() <= 0) {
                    JOptionPane.showMessageDialog(this,
                        "No outstanding balance. Nothing to pay.");
                    return;
                }
                if (amount > card.getCurrentBalance()) {
                    JOptionPane.showMessageDialog(this,
                        "Payment exceeds balance!\nCurrent Balance: LKR "
                        + String.format("%.2f", card.getCurrentBalance()));
                    return;
                }
                cardDAO.updateBalance(cardId, card.getCurrentBalance() - amount);
            }

            transactionDAO.addTransaction(cardId, amount, type, desc, accountNumber);
            JOptionPane.showMessageDialog(this, "Transaction successful!");

            amountField.setText("");
            accountField.setText("");
            descField.setText("");
            loadCustomers();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid Amount.");
        }
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 180, 216)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
    }

    private void styleCombo(JComboBox<String> box) {
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setBackground(Color.WHITE);
        box.setPreferredSize(new Dimension(220, 30));
    }

    private void addRow(JPanel p, GridBagConstraints gbc, int row, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(13, 27, 62));
        gbc.gridx = 0; gbc.gridy = row; p.add(lbl,   gbc);
        gbc.gridx = 1; gbc.gridy = row; p.add(field, gbc);
    }
}