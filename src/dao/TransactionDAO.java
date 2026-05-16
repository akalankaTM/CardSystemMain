/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Akalanka
 */
package dao;

import model.DatabaseConnection;
import model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void addTransaction(int cardId, double amount,
                                String type, String description) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO transactions (card_id, amount, type, description) " +
                "VALUES (?, ?, ?, ?)");
            ps.setInt(1, cardId);
            ps.setDouble(2, amount);
            ps.setString(3, type);
            ps.setString(4, description);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT * FROM transactions");
            while (rs.next()) {
                list.add(new Transaction(
                    rs.getInt("transaction_id"),
                    rs.getInt("card_id"),
                    rs.getDouble("amount"),
                    rs.getString("type"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }
}