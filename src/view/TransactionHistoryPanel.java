/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Akalanka
 */
package view;

import dao.TransactionDAO;
import model.Transaction;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class TransactionHistoryPanel extends JPanel {

    private DefaultTableModel tableModel;
    private TransactionDAO transactionDAO = new TransactionDAO();
    private JTextField searchField;

    public TransactionHistoryPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("  Transaction History", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(13, 27, 62));
        title.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 0));

        // search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(240, 245, 255));
        JLabel searchLabel = new JLabel("Filter by Card ID:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 13));
        searchLabel.setForeground(new Color(13, 27, 62));
        searchField = new JTextField(10);
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 180, 216)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        JButton searchBtn  = makeButton("Search",     new Color(0, 180, 216));
        JButton clearBtn   = makeButton("Show All",   new Color(13, 27, 62));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(clearBtn);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(240, 245, 255));
        topBar.add(title,       BorderLayout.NORTH);
        topBar.add(searchPanel, BorderLayout.CENTER);

        // table
        String[] cols = {"Txn ID", "Card ID", "Type", "Amount (LKR)", "Description"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setGridColor(new Color(200, 210, 230));
        table.setSelectionBackground(new Color(0, 180, 216));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(13, 27, 62));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 13));

        // color CHARGE red, PAYMENT green
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Arial", Font.BOLD, 12));
                setForeground("CHARGE".equals(v) ? new Color(200, 0, 0) : new Color(0, 150, 0));
                return this;
            }
        });

        // right-align amount column
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(rightAlign);

        searchBtn.addActionListener(e -> {
            String text = searchField.getText().trim();
            if (text.isEmpty()) {
                loadAll();
                return;
            }
            try {
                int cardId = Integer.parseInt(text);
                loadByCard(cardId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Card ID must be a number.");
            }
        });

        clearBtn.addActionListener(e -> {
            searchField.setText("");
            loadAll();
        });

        add(topBar,                 BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadAll();
    }

    private void loadAll() {
        tableModel.setRowCount(0);
        List<Transaction> list = transactionDAO.getAllTransactions();
        for (Transaction t : list) {
            tableModel.addRow(new Object[]{
                t.getTransactionId(),
                t.getCardId(),
                t.getType(),
                String.format("%.2f", t.getAmount()),
                t.getDescription()
            });
        }
    }

    private void loadByCard(int cardId) {
        tableModel.setRowCount(0);
        List<Transaction> list = transactionDAO.getTransactionsByCard(cardId);
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions found for Card ID " + cardId);
        }
        for (Transaction t : list) {
            tableModel.addRow(new Object[]{
                t.getTransactionId(),
                t.getCardId(),
                t.getType(),
                String.format("%.2f", t.getAmount()),
                t.getDescription()
            });
        }
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return btn;
    }
}