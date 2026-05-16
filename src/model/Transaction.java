/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Akalanka
 */
package model;

public class Transaction {
    private int    transactionId;
    private int    cardId;
    private double amount;
    private String type;
    private String description;

    public Transaction(int transactionId, int cardId,
                       double amount, String type, String description) {
        this.transactionId = transactionId;
        this.cardId        = cardId;
        this.amount        = amount;
        this.type          = type;
        this.description   = description;
    }

    public int    getTransactionId() { return transactionId; }
    public int    getCardId()        { return cardId; }
    public double getAmount()        { return amount; }
    public String getType()          { return type; }
    public String getDescription()   { return description; }
}