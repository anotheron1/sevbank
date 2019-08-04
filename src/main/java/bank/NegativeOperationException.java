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
public class NegativeOperationException extends Exception {
    private double number;
    public double getNumber(){return number;}
    public NegativeOperationException(String message, double num){     
        super(message);
        number=num;
    }
}
