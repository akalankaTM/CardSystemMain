/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Akalanka
 */
package model;

public class CreditCard {
    private int    cardId;
    private int    customerId;
    private String cardNumber;
    private double creditLimit;
    private double currentBalance;
    private String status;

    public CreditCard(int cardId, int customerId, String cardNumber,
                      double creditLimit, double currentBalance, String status) {
        this.cardId         = cardId;
        this.customerId     = customerId;
        this.cardNumber     = cardNumber;
        this.creditLimit    = creditLimit;
        this.currentBalance = currentBalance;
        this.status         = status;
    }

    public int    getCardId()         { return cardId; }
    public int    getCustomerId()     { return customerId; }
    public String getCardNumber()     { return cardNumber; }
    public double getCreditLimit()    { return creditLimit; }
    public double getCurrentBalance() { return currentBalance; }
    public String getStatus()         { return status; }
}