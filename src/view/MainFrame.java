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
import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Credit Card Management System");
        setSize(900, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(13, 27, 62));
        header.setPreferredSize(new Dimension(900, 55));

        JLabel title = new JLabel("  💳 Credit Card Management System");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel user = new JLabel("Admin  ");
        user.setFont(new Font("Arial", Font.PLAIN, 13));
        user.setForeground(new Color(0, 180, 216));

        header.add(title, BorderLayout.WEST);
        header.add(user,  BorderLayout.EAST);

        // tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.setBackground(new Color(240, 245, 255));
        tabs.addTab("  Dashboard",   new DashboardPanel());
        tabs.addTab("  Transaction", new TransactionPanel());
        tabs.addTab("  Report",      new ReportPanel());

        add(header, BorderLayout.NORTH);
        add(tabs,   BorderLayout.CENTER);
    }
}