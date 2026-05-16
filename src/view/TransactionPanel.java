/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Akalanka
 */

package view;

import dao.CreditCardDAO;
import dao.TransactionDAO;
import model.CreditCard;
import java.awt.*;
import javax.swing.*;

public class TransactionPanel extends JPanel {

    private CreditCardDAO   cardDAO        = new CreditCardDAO();
    private TransactionDAO  transactionDAO = new TransactionDAO();

    public TransactionPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("  New Transaction", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(13, 27, 62));
        title.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 0));

        // form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230)),
            BorderFactory.createEmptyBorder(30, 50, 30, 50)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(10, 10, 10, 10);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.WEST;

        JTextField cardIdField = new JTextField(20);
        JTextField amountField = new JTextField(20);
        JTextField descField   = new JTextField(20);
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"CHARGE", "PAYMENT"});

        styleField(cardIdField);
        styleField(amountField);
        styleField(descField);
        typeBox.setFont(new Font("Arial", Font.PLAIN, 13));
        typeBox.setBackground(Color.WHITE);

        JButton submitBtn = new JButton("SUBMIT TRANSACTION");
        submitBtn.setBackground(new Color(0, 180, 216));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        addRow(form, gbc, 0, "Card ID:",        cardIdField);
        addRow(form, gbc, 1, "Type:",            typeBox);
        addRow(form, gbc, 2, "Amount (LKR):",   amountField);
        addRow(form, gbc, 3, "Description:",     descField);

        gbc.gridx = 1; gbc.gridy = 4;
        form.add(submitBtn, gbc);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(240, 245, 255));
        center.add(form);

        submitBtn.addActionListener(e -> {
            try {
                int    cardId = Integer.parseInt(cardIdField.getText().trim());
                double amount = Double.parseDouble(amountField.getText().trim());
                String type   = (String) typeBox.getSelectedItem();
                String desc   = descField.getText().trim();

                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than 0.");
                    return;
                }

                CreditCard card = cardDAO.getCardById(cardId);
                if (card == null) {
                    JOptionPane.showMessageDialog(this, "Card ID not found.");
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
                    cardDAO.updateBalance(cardId, card.getCurrentBalance() + amount);
                } else {
                    cardDAO.updateBalance(cardId, card.getCurrentBalance() - amount);
                }

                transactionDAO.addTransaction(cardId, amount, type, desc);
                JOptionPane.showMessageDialog(this, "✅ Transaction successful!");
                cardIdField.setText("");
                amountField.setText("");
                descField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid Card ID and Amount.");
            }
        });

        add(title,  BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 180, 216)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
    }

    private void addRow(JPanel p, GridBagConstraints gbc, int row, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(13, 27, 62));
        gbc.gridx = 0; gbc.gridy = row; p.add(lbl,   gbc);
        gbc.gridx = 1; gbc.gridy = row; p.add(field, gbc);
    }
}