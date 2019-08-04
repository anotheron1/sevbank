/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

import java.util.*;

/**
 *
 * @author vohmyanin.ki
 */
public class Client {
    int id;
    int bankId;
    private String name;
    int age;
    boolean gender;
    String listAcc;
    List<AbstractAccount> abAcc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getListAcc() {
        return listAcc;
    }

    public void setListAcc(String listAcc) {
        this.listAcc = listAcc;
    }

    public List<AbstractAccount> getAbAcc() {
        return abAcc;
    }

    public void setAbAcc(List<AbstractAccount> abAcc) {
        this.abAcc = abAcc;
    }

    public void getCliInfo(){
        System.out.println("      Информация про клиента:");
        System.out.println("      Имя - " + name);
        System.out.println("      Возраст - " + age);
        if (gender==true) {
            System.out.println("      Пол - мужской");
        } else {
            System.out.println("      Пол - женский");
        }
        System.out.println("      Количество счетов - " + abAcc.size());
        System.out.println("      Счета: ");
        printCliAcc(abAcc);
    }

    void printCliAcc(List list){
        for (int i=0;i<list.size();i++){
            Object obj = list.get(i);
            if(obj instanceof SavingAccount)
            {
                SavingAccount SavAcc = (SavingAccount) obj;
                System.out.println("            id - " + SavAcc.getId());
                System.out.println("            Баланс - " + SavAcc.getBalance());
            }
            else if(obj instanceof CheckingAccount)
            {
                CheckingAccount ChAcc = (CheckingAccount) obj;
                System.out.println("            id - " + ChAcc.getId());
                System.out.println("            Баланс - " + ChAcc.getBalance());
                System.out.println("            Овердрафт - " + ChAcc.getOverdraft());
            }
            System.out.println(" ");
        }
    }
}
