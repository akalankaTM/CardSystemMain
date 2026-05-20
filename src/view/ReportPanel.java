/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Akalanka
 */

package view;

import java.awt.*;
import java.sql.Connection;
import javax.swing.*;
import model.DatabaseConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

public class ReportPanel extends JPanel {

    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("  Credit Utilization Report", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(13, 27, 62));
        title.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 0));

        JButton loadBtn = new JButton("Generate Report");
        loadBtn.setBackground(new Color(13, 27, 62));
        loadBtn.setForeground(Color.WHITE);
        loadBtn.setFont(new Font("Arial", Font.BOLD, 13));
        loadBtn.setFocusPainted(false);
        loadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(240, 245, 255));

        JLabel info = new JLabel("Click 'Generate Report' to view Credit Utilization");
        info.setFont(new Font("Arial", Font.PLAIN, 14));
        info.setForeground(new Color(13, 27, 62));
        center.add(info);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(240, 245, 255));
        bottom.add(loadBtn);

        loadBtn.addActionListener(e -> generateReport());

        add(title,  BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void generateReport() {
        try {
            Connection con = DatabaseConnection.getConnection();

            // compile the jrxml file
//            String reportPath = getClass().getClassLoader()
//                .getResource("CreditUtilizationReport.jrxml").getPath();
            //String reportPath = "src/CreditUtilizationReport.jrxml";
            //String reportPath = System.getProperty("user.dir") + "/src/CreditUtilizationReport.jrxml";
            //String reportPath = System.getProperty("user.dir") + "/src/cardsystemmain/CreditUtilizationReport.jrxml";
            //String reportPath = "src/CreditUtilizationReport.jrxml";
            String reportPath = System.getProperty("user.dir") + "/src/CreditUtilizationReport.jrxml";

            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            // fill the report with data from DB
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, null, con);

            // show the report in a viewer window
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Report Error: " + ex.getMessage());
        }
    }
}