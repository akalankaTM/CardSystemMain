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
import dao.CustomerDAO;
import model.CreditCard;
import model.Customer;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class DashboardPanel extends JPanel {

    private DefaultTableModel tableModel;
    private CreditCardDAO cardDAO     = new CreditCardDAO();
    private CustomerDAO   customerDAO = new CustomerDAO();

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        // title
        JLabel title = new JLabel("  Card Overview", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(13, 27, 62));
        title.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 0));

        // table
        String[] cols = {"Card ID", "Customer", "Card Number", "Limit (LKR)", "Balance (LKR)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setGridColor(new Color(200, 210, 230));
        table.setSelectionBackground(new Color(0, 180, 216));
        table.setSelectionForeground(Color.WHITE);

        // header style
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(13, 27, 62));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 13));

        // status color renderer
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                if ("ACTIVE".equals(v)) {
                    setForeground(new Color(0, 150, 0));
                } else {
                    setForeground(new Color(200, 0, 0));
                }
                return this;
            }
        });

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBackground(new Color(13, 27, 62));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 13));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(240, 245, 255));
        bottom.add(refreshBtn);

        refreshBtn.addActionListener(e -> loadData());

        add(title,                    BorderLayout.NORTH);
        add(new JScrollPane(table),   BorderLayout.CENTER);
        add(bottom,                   BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<CreditCard> cards     = cardDAO.getAllCards();
        List<Customer>   customers = customerDAO.getAllCustomers();
        for (CreditCard card : cards) {
            String customerName = "";
            for (Customer c : customers) {
                if (c.getCustomerId() == card.getCustomerId()) {
                    customerName = c.getName();
                    break;
                }
            }
            tableModel.addRow(new Object[]{
                card.getCardId(),
                customerName,
                card.getCardNumber(),
                String.format("%.2f", card.getCreditLimit()),
                String.format("%.2f", card.getCurrentBalance()),
                card.getStatus()
            });
        }
    }
}