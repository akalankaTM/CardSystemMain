/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Akalanka
 */
package dao;

import model.Customer;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM customers");
            while (rs.next()) {
                list.add(new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }
    public void addCustomer(String name, String email, String phone) {
    try {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)");
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, phone);
        ps.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

public void updateCustomer(int id, String name, String email, String phone) {
    try {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "UPDATE customers SET name=?, email=?, phone=? WHERE customer_id=?");
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, phone);
        ps.setInt(4, id);
        ps.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

public void deleteCustomer(int id) {
    try {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "DELETE FROM customers WHERE customer_id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
}

