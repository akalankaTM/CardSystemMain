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

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Credit Card System - Login");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // main panel
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(13, 27, 62));

        // header
        JLabel header = new JLabel("CREDIT CARD SYSTEM", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 10));
        main.add(header, BorderLayout.NORTH);

        // form panel
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 12));
        form.setBackground(new Color(13, 27, 62));
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField userField = new JTextField();
        userField.setFont(new Font("Arial", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 180, 216)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font("Arial", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 180, 216)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(new Color(0, 180, 216));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        form.add(userLabel); form.add(userField);
        form.add(passLabel); form.add(passField);
        form.add(new JLabel()); form.add(loginBtn);

        main.add(form, BorderLayout.CENTER);

        // footer
        JLabel footer = new JLabel("Banking & Finance System", SwingConstants.CENTER);
        footer.setForeground(new Color(150, 150, 180));
        footer.setFont(new Font("Arial", Font.ITALIC, 11));
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        main.add(footer, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> {
            String u = userField.getText();
            String p = new String(passField.getPassword());
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?");
                ps.setString(1, u);
                ps.setString(2, p);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    dispose();
                    new MainFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
            }
        });

        add(main);
        setVisible(true);
    }
}