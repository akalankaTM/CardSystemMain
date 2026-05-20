/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Akalanka
 */
package dao;

import model.CreditCard;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreditCardDAO {

    public List<CreditCard> getAllCards() {
        List<CreditCard> list = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM credit_cards";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                list.add(new CreditCard(
                    rs.getInt("card_id"),
                    rs.getInt("customer_id"),
                    rs.getString("card_number"),
                    rs.getDouble("credit_limit"),
                    rs.getDouble("current_balance"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public CreditCard getCardById(int cardId) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM credit_cards WHERE card_id = ?");
            ps.setInt(1, cardId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CreditCard(
                    rs.getInt("card_id"),
                    rs.getInt("customer_id"),
                    rs.getString("card_number"),
                    rs.getDouble("credit_limit"),
                    rs.getDouble("current_balance"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public void updateBalance(int cardId, double newBalance) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE credit_cards SET current_balance = ? WHERE card_id = ?");
            ps.setDouble(1, newBalance);
            ps.setInt(2, cardId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void updateStatus(int cardId, String status) {
    try {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "UPDATE credit_cards SET status = ? WHERE card_id = ?");
        ps.setString(1, status);
        ps.setInt(2, cardId);
        ps.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
    }
    
    public void unblockCard(int cardId) {
    try {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "UPDATE credit_cards SET status = 'ACTIVE' WHERE card_id = ? AND current_balance < credit_limit");
        ps.setInt(1, cardId);
        int rows = ps.executeUpdate();
        if (rows == 0) {
            System.out.println("Cannot unblock - balance still at limit.");
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
}