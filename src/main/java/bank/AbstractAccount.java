/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

import java.util.List;

/**
 *
 * @author vohmyanin.ki
 */
public abstract class AbstractAccount implements Account{
    int id;
    boolean type; //1 - overdraft, 0 - w/o
    double balance;
    double overdraft;

    public double refill(double add) throws NegativeOperationException{
        if (add>0){
            balance += add;
        } else throw new NegativeOperationException("Operation occurs with a negative value!", add);
        return balance;
    }

    public double withdrawal(double deduct)throws NegativeOperationException{
        if (deduct > 0 && deduct <= balance + overdraft){
            balance -= deduct;
        } else throw new NegativeOperationException("Operation occurs with a negative value!", deduct);
        return balance;
    }

    void printAccInfo(List list){
        for (int i=0;i<list.size();i++){
            Object obj = list.get(i);
            if(obj instanceof SavingAccount)
            {
                SavingAccount SavAcc = (SavingAccount) obj;
                System.out.println("            id - " + SavAcc.getId());
                System.out.println("            balance - " + SavAcc.getBalance());
            }
            else if(obj instanceof CheckingAccount)
            {
                CheckingAccount ChAcc = (CheckingAccount) obj;
                System.out.println("            id - " + ChAcc.getId());
                System.out.println("            balance - " + ChAcc.getBalance());
                System.out.println("            overdraft - " + ChAcc.getOverdraft());
            }
            System.out.println(" ");
        }
    }
}
