/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Akalanka
 */
package view;

import dao.CardManagementDAO;
import dao.CustomerDAO;
import model.CreditCard;
import model.Customer;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class CardManagementPanel extends JPanel {

    private DefaultTableModel tableModel;
    private CardManagementDAO cardDAO    = new CardManagementDAO();
    private CustomerDAO       customerDAO = new CustomerDAO();
    private JTable table;

    public CardManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("  Card Management", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(13, 27, 62));
        title.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 0));

        String[] cols = {"Card ID", "Customer", "Card Number", "Limit (LKR)", "Credits(LKR)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setGridColor(new Color(200, 210, 230));
        table.setSelectionBackground(new Color(0, 180, 216));
        table.setSelectionForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(13, 27, 62));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 13));

        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                setForeground("ACTIVE".equals(v) ? new Color(0, 150, 0) : new Color(200, 0, 0));
                return this;
            }
        });

        JButton addBtn    = makeButton("+ New Card",      new Color(0, 150, 0));
        JButton limitBtn  = makeButton("Edit Limit",    new Color(13, 27, 62));
        JButton toggleBtn = makeButton("Block/Unblock", new Color(180, 100, 0));
        JButton deleteBtn = makeButton("Delete",        new Color(180, 0, 0));
        JButton refreshBtn = makeButton("Refresh",      new Color(13, 27, 62));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(240, 245, 255));
        bottom.add(refreshBtn);
        bottom.add(addBtn);
        bottom.add(limitBtn);
        bottom.add(toggleBtn);
        bottom.add(deleteBtn);

        refreshBtn.addActionListener(e -> loadData());

        addBtn.addActionListener(e -> showAddCardDialog());

        limitBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a card first."); return; }
            showEditLimitDialog(row);
        });

        toggleBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a card first."); return; }
            int    cardId  = (int)    tableModel.getValueAt(row, 0);
            String current = (String) tableModel.getValueAt(row, 5);
            String next = current.equals("ACTIVE") ? "BLOCKED" : "ACTIVE";

// if trying to unblock check available credit
if (next.equals("ACTIVE")) {
    double limit   = Double.parseDouble(tableModel.getValueAt(row, 3).toString().replace(",", ""));
    double balance = Double.parseDouble(tableModel.getValueAt(row, 4).toString().replace(",", ""));
    double available = limit - balance;
    if (available <= 0) {
        JOptionPane.showMessageDialog(this,
            "Cannot unblock! No available credit.\nCustomer must make a payment first.");
        return;
    }
}

int confirm = JOptionPane.showConfirmDialog(this,
    "Change status to " + next + "?", "Confirm", JOptionPane.YES_NO_OPTION);
if (confirm == JOptionPane.YES_OPTION) {
    cardDAO.updateStatus(cardId, next);
    loadData();
}
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a card first."); return; }
            int cardId = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete card ID " + cardId + "? This also deletes its transactions.",
                "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cardDAO.deleteCard(cardId);
                loadData();
            }
        });

        add(title,                  BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom,                 BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<CreditCard> cards     = cardDAO.getAllCards();
        List<Customer>   customers = customerDAO.getAllCustomers();
        for (CreditCard card : cards) {
            String customerName = customers.stream()
                .filter(c -> c.getCustomerId() == card.getCustomerId())
                .map(Customer::getName)
                .findFirst().orElse("Unknown");
            tableModel.addRow(new Object[]{
                card.getCardId(),
                customerName,
                maskCardNumber(card.getCardNumber()),
                String.format("%.2f", card.getCreditLimit()),
                String.format("%.2f", card.getCurrentBalance()),
                card.getStatus()
            });
        }
    }

    private void showAddCardDialog() {
        List<Customer> customers = customerDAO.getAllCustomers();
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customers found. Add a customer first.");
            return;
        }

        JComboBox<String> customerBox = new JComboBox<>();
        for (Customer c : customers) {
            customerBox.addItem(c.getCustomerId() + " - " + c.getName());
        }
        JTextField cardNumberField = new JTextField(20);
        JTextField limitField      = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Customer:"));   panel.add(customerBox);
        panel.add(new JLabel("Card Number:")); panel.add(cardNumberField);
        panel.add(new JLabel("Credit Limit (LKR):")); panel.add(limitField);

        styleCombo(customerBox);
        styleField(cardNumberField);
        styleField(limitField);

        int result = JOptionPane.showConfirmDialog(this, panel,
            "New Card", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int    selectedIndex = customerBox.getSelectedIndex();
                int    customerId    = customers.get(selectedIndex).getCustomerId();
                String cardNumber    = cardNumberField.getText().trim();
                double limit         = Double.parseDouble(limitField.getText().trim());

                if (cardNumber.isEmpty() || limit <= 0) {
                    JOptionPane.showMessageDialog(this, "Enter a valid card number and limit.");
                    return;
                }
                cardDAO.addCard(customerId, cardNumber, limit);
                loadData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credit limit must be a number.");
            }
        }
    }

    private void showEditLimitDialog(int row) {
        int    cardId       = (int)    tableModel.getValueAt(row, 0);
        String currentLimit = (String) tableModel.getValueAt(row, 3);

        JTextField limitField = new JTextField(currentLimit, 20);
        styleField(limitField);

        JPanel panel = new JPanel(new GridLayout(1, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("New Limit (LKR):"));
        panel.add(limitField);

        int result = JOptionPane.showConfirmDialog(this, panel,
            "Edit Credit Limit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double newLimit = Double.parseDouble(limitField.getText().trim());
                if (newLimit <= 0) {
                    JOptionPane.showMessageDialog(this, "Limit must be greater than 0.");
                    return;
                }
                cardDAO.updateLimit(cardId, newLimit);
                loadData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid number.");
            }
        }
    }

    private String maskCardNumber(String number) {
        if (number == null || number.length() < 4) return number;
        return "**** **** **** " + number.substring(number.length() - 4);
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 180, 216)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    private void styleCombo(JComboBox<?> box) {
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setBackground(Color.WHITE);
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }
}