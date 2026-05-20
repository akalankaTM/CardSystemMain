/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Akalanka
 */

package view;

import dao.CustomerDAO;
import model.Customer;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class CustomerPanel extends JPanel {

    private DefaultTableModel tableModel;
    private CustomerDAO customerDAO = new CustomerDAO();
    private JTable table;

    public CustomerPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("  Customer Management", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(13, 27, 62));
        title.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 0));

        String[] cols = {"ID", "Name", "Email", "Phone"};
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

        JButton addBtn    = makeButton("+ Add",    new Color(0, 150, 0));
        JButton editBtn   = makeButton("Edit",   new Color(13, 27, 62));
        JButton deleteBtn = makeButton("Delete", new Color(180, 0, 0));
        JButton refreshBtn = makeButton("Refresh", new Color(13, 27, 62));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(240, 245, 255));
        bottom.add(refreshBtn);
        bottom.add(addBtn);
        bottom.add(editBtn);
        bottom.add(deleteBtn);

        refreshBtn.addActionListener(e -> loadData());

        addBtn.addActionListener(e -> showAddDialog());

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a customer to edit.");
                return;
            }
            showEditDialog(row);
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a customer to delete.");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete customer ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                customerDAO.deleteCustomer(id);
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
        List<Customer> customers = customerDAO.getAllCustomers();
        for (Customer c : customers) {
            tableModel.addRow(new Object[]{
                c.getCustomerId(), c.getName(), c.getEmail(), c.getPhone()
            });
        }
    }

    private void showAddDialog() {
        JTextField nameField  = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);

        JPanel panel = buildFormPanel(
            new String[]{"Name:", "Email:", "Phone:"},
            new JTextField[]{nameField, emailField, phoneField}
        );

        int result = JOptionPane.showConfirmDialog(this, panel,
            "Add Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name  = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }
            customerDAO.addCustomer(name, email, phone);
            loadData();
        }
    }

    private void showEditDialog(int row) {
        int    id    = (int)    tableModel.getValueAt(row, 0);
        String name  = (String) tableModel.getValueAt(row, 1);
        String email = (String) tableModel.getValueAt(row, 2);
        String phone = (String) tableModel.getValueAt(row, 3);

        JTextField nameField  = new JTextField(name,  20);
        JTextField emailField = new JTextField(email, 20);
        JTextField phoneField = new JTextField(phone, 20);

        JPanel panel = buildFormPanel(
            new String[]{"Name:", "Email:", "Phone:"},
            new JTextField[]{nameField, emailField, phoneField}
        );

        int result = JOptionPane.showConfirmDialog(this, panel,
            "Edit Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newName  = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPhone = phoneField.getText().trim();
            if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }
            customerDAO.updateCustomer(id, newName, newEmail, newPhone);
            loadData();
        }
    }

    private JPanel buildFormPanel(String[] labels, JTextField[] fields) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Arial", Font.BOLD, 13));
            fields[i].setFont(new Font("Arial", Font.PLAIN, 13));
            panel.add(lbl);
            panel.add(fields[i]);
        }
        return panel;
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