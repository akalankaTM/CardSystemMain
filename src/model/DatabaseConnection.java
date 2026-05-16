/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Akalanka
 */

package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection = null;

    private DatabaseConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
               String url = "jdbc:mysql://localhost:3306/credit_card_db";
                String user = "root";
                String pass = "admin123";  // put your MySQL password here
                connection = DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
                System.out.println("DB Error: " + e.getMessage());
            }
        }
        return connection;
    }
}