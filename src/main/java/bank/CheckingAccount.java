/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

/**
 *
 * @author vohmyanin.ki
 */
public class CheckingAccount extends AbstractAccount{    
    public CheckingAccount() {
        
    }

    public CheckingAccount(int id, double balance, double overdraft) {
        this.id = id;
        this.balance = balance;
        this.overdraft = overdraft;
    }
        
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(double overdraft) {
        this.overdraft = overdraft;
    }      
}
