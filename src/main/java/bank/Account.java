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
public interface Account {
    double refill(double add) throws NegativeOperationException;
    double withdrawal(double deduct) throws NegativeOperationException;
}
