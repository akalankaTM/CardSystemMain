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

public class CardManagementDAO {

    public List<CreditCard> getAllCards() {
        List<CreditCard> list = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM credit_cards");
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

    public void addCard(int customerId, String cardNumber, double limit) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO credit_cards (customer_id, card_number, credit_limit, current_balance, status) " +
                "VALUES (?, ?, ?, 0.00, 'ACTIVE')");
            ps.setInt(1, customerId);
            ps.setString(2, cardNumber);
            ps.setDouble(3, limit);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateLimit(int cardId, double newLimit) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE credit_cards SET credit_limit=? WHERE card_id=?");
            ps.setDouble(1, newLimit);
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
                "UPDATE credit_cards SET status=? WHERE card_id=?");
            ps.setString(1, status);
            ps.setInt(2, cardId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteCard(int cardId) {
        try {
            Connection con = DatabaseConnection.getConnection();
            // delete transactions first to avoid FK constraint failure
            PreparedStatement ps1 = con.prepareStatement(
                "DELETE FROM transactions WHERE card_id=?");
            ps1.setInt(1, cardId);
            ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(
                "DELETE FROM credit_cards WHERE card_id=?");
            ps2.setInt(1, cardId);
            ps2.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}