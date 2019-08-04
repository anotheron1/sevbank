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
public class Bank {
    int id;
    String bankName;
    List<Client> clients;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
    
    public void getBankInfo(){
        System.out.println("Информация про банк:");
        System.out.println("Название банка - " + bankName);
        System.out.println("Клиенты: ");
        printClients(clients);
    }
    
    void printClients(List list){
        for (int i=0;i<list.size();i++){
            Object obj = list.get(i);
            Client cli = (Client) obj;
            cli.getCliInfo();
        }
    }
}
