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
public class SavingAccount extends AbstractAccount{    
    public SavingAccount() {
    }

    public SavingAccount(int id, double balance) {
        this.id = id;
        this.balance = balance;
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
}
