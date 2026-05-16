/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Akalanka
 */

package view;

import model.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ReportPanel extends JPanel {

    private DefaultTableModel tableModel;

    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("  Credit Utilization Report", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(13, 27, 62));
        title.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 0));

        String[] cols = {"Customer", "Card Number", "Limit (LKR)", "Balance (LKR)", "Utilization %", "Risk Level"};
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

        // risk color renderer
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Arial", Font.BOLD, 12));
                switch (String.valueOf(v)) {
                    case "CRITICAL": setForeground(new Color(200, 0,   0));   break;
                    case "HIGH":     setForeground(new Color(220, 100, 0));   break;
                    case "MODERATE": setForeground(new Color(180, 150, 0));   break;
                    default:         setForeground(new Color(0,   150, 0));   break;
                }
                return this;
            }
        });

        JButton loadBtn = new JButton("🔄 Load Report");
        loadBtn.setBackground(new Color(13, 27, 62));
        loadBtn.setForeground(Color.WHITE);
        loadBtn.setFont(new Font("Arial", Font.BOLD, 13));
        loadBtn.setFocusPainted(false);
        loadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(240, 245, 255));
        bottom.add(loadBtn);

        loadBtn.addActionListener(e -> loadReport());

        add(title,                  BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom,                 BorderLayout.SOUTH);

        loadReport();
    }

    private void loadReport() {
        tableModel.setRowCount(0);
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql =
                "SELECT cu.name, cc.card_number, cc.credit_limit, cc.current_balance, " +
                "ROUND((cc.current_balance / cc.credit_limit) * 100, 2) AS utilization, " +
                "CASE " +
                "  WHEN (cc.current_balance / cc.credit_limit) >= 0.90 THEN 'CRITICAL' " +
                "  WHEN (cc.current_balance / cc.credit_limit) >= 0.75 THEN 'HIGH' " +
                "  WHEN (cc.current_balance / cc.credit_limit) >= 0.50 THEN 'MODERATE' " +
                "  ELSE 'LOW' END AS risk " +
                "FROM credit_cards cc " +
                "JOIN customers cu ON cc.customer_id = cu.customer_id " +
                "JOIN transactions t ON t.card_id = cc.card_id " +
                "GROUP BY cc.card_id";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getString("card_number"),
                    String.format("%.2f", rs.getDouble("credit_limit")),
                    String.format("%.2f", rs.getDouble("current_balance")),
                    rs.getDouble("utilization") + "%",
                    rs.getString("risk")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}