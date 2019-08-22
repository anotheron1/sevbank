package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Bank> bankList;
    private static List<Client> clientList;
    private static List<AbstractAccount> abAccountList;
    private static int bid = 0;
    private static int cid = 0;
    private static int aid = 0;
    private static int chbid;
    private static int chcid;
    private static String chbidLine = "";
    private static String convertLine;
    private static String lineForUpdateClients;
    private static double newbal;
    private static Scanner in;

    public static void main(String[] args) {
        createTable();

        System.out.println("Aloha! At the start you must create a bank.");
        newBank();

        System.out.println("Alright. Next lets create clients of a bank.");
        chooseBank();
        newClient();

        System.out.println("Well done. Create some accounts for that client.");
        chooseClient();
        System.out.println("And now lets create 2 account. One with overdraft, on w/o. Please, choose the type of account: ");
        System.out.println("1 - account with overdraft, /n 0 - account w/o overdraft");
        newAcc();
        System.out.println("And the next one acc: ");
        newAcc();

        System.out.println("Lets see at our accounts: ");
        showAcc();

        System.out.println("All objects has been create succsessfuly.");
        int n, v=0;
            do {
                System.out.println("Choose actions what you wanna do: ");
                System.out.println("1. Add bank");
                System.out.println("2. Add client");
                System.out.println("3. Add account");
                System.out.println("4. Refill account");
                System.out.println("5. Withdraw account");
                System.out.println("6. Exit");
                in = new Scanner(System.in);
                System.out.println("Please, choose one number: ");
                n = in.nextInt();

                try {
                    switch (n) {
                        case 1:
                            addBank();
                            break;
                        case 2:
                            addClient();
                            break;
                        case 3:
                            addAccount();
                            break;
                        case 4:
                            refill();
                            break;
                        case 5:
                            withdrawal();
                            break;
                        case 6:
                        default:
                            v = 784;
                            break;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } while (v!=784);
    }

    private static void createTable() {
        try {
            String createTableSQL = "CREATE TABLE banks("
                    + "bank_id NUMERIC(5) NOT NULL, "
                    + "name VARCHAR(20) NOT NULL, "
                    + "PRIMARY KEY (bank_id) "
                    + ")";
            dbActions(createTableSQL);
            createTableSQL = "CREATE TABLE clients("
                    + "client_id NUMERIC(5) NOT NULL, "
                    + "bank_id NUMERIC(5) NOT NULL, "
                    + "name VARCHAR(20) NOT NULL, "
                    + "age NUMERIC(3) NOT NULL, "
                    + "gender BOOLEAN NOT NULL, " //true - мужчина
                    + "account_id VARCHAR(10), "
                    + "PRIMARY KEY (client_id) "
                    + ")";
            dbActions(createTableSQL);
            createTableSQL = "CREATE TABLE accounts("
                    + "account_id NUMERIC(10) NOT NULL, "
                    + "client_id NUMERIC(5) NOT NULL, "
                    + "account_type BOOLEAN NOT NULL, " //true - с овердрафтом
                    + "balance NUMERIC(20) NOT NULL, "
                    + "overdraft NUMERIC(5), "
                    + "PRIMARY KEY (account_id) "
                    + ")";
            dbActions(createTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showAcc() {
        try{
            String selectTableSQL = "SELECT * from accounts where client_id = ?";
            accountSelect(selectTableSQL, chcid);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        AbstractAccount a = new SavingAccount();
        if(abAccountList.size()>0){
            a.printAccInfo(abAccountList);
        } else {
            System.out.println("No one account exist");
        }
    }

    private static void newAcc() {
        in = new Scanner(System.in);
        System.out.println("With overdraft? Write 'y' or 'n'");
        String aTypeLine = in.nextLine();
        boolean aType = aTypeLine.equals("y");
        double aBal;
        double aOD;
        if (aType){
            System.out.println("Balance: ");
            aBal = in.nextDouble();
            System.out.println("Overdraft: ");
            aOD = in.nextDouble();
        } else {
            System.out.println("Balance: ");
            aBal = in.nextDouble();
            aOD = 0;
        }
        try{
            convertLine = String.valueOf(chcid).concat(String.valueOf(aid));
            int aid = Integer.valueOf(convertLine);
            String insertTableSQL = "INSERT INTO accounts"
                    + "(account_id, client_id, account_type, balance, overdraft) " + "VALUES"
                    + "(?,?,?,?,?)";
            dbActionsWParam(insertTableSQL, aid, chcid, aType, aBal, aOD);
            lineForUpdateClients = aid + ", ";
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        try{
            String updateTableSQL = "UPDATE clients SET account_id = ? WHERE client_id = ?";
            dbActionsWParam(updateTableSQL, lineForUpdateClients, chcid);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        aid++;
    }

    private static void chooseClient() {
        System.out.println("Choose the client to which you want to add account:");
        try{
            String selectTableSQL = "SELECT * from clients where bank_id =" + chbid;
            clientSelect(selectTableSQL);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        if(clientList.size()>0){
            for (Client client : clientList) {
                System.out.println(client.getId() + ". " + client.getName());
            }
        } else {
            System.out.println("No one client exist");
        }

        in = new Scanner(System.in);
        System.out.println("Write the client id: ");
        chcid = in.nextInt();

        for (Client client : clientList) {
            if (chcid == client.getId()) {
                String chcidLine = client.getName();
                System.out.println("You choosen client - " + chcidLine);
            }
        }
    }

    private static void newClient() {
        System.out.println("Now create a client. Please, write his/her name:");

        in = new Scanner(System.in);
        String cName = in.nextLine();
        try{
            String selectTableSQL = "SELECT * from clients where bank_id =" + chbid;
            clientSelect(selectTableSQL);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        if(clientList.size()>0){
            for (Client client : clientList) {
                if (client.getName().equals(cName)) {
                    System.out.println("That client already exist. Write name of the new client: ");
                    cName = in.nextLine();
                }
            }
        }
        System.out.println("Age: ");
        int cAge = in.nextInt();
        System.out.println("Gender - men? Write 'y' or 'n'");
        in.nextLine();
        String cGenderLine = in.nextLine();
        boolean cGender = cGenderLine.equals("y");
        try{
            convertLine = String.valueOf(chbid).concat(String.valueOf(cid));
            int cid = Integer.valueOf(convertLine);
            String insertTableSQL = "INSERT INTO clients"
                    + "(client_id, bank_id, name, age, gender) " + "VALUES"
                    + "(?,?,?,?,?)";
            dbActionsWParam(insertTableSQL, cid, chbid, cName, cAge, cGender);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        cid++;
        System.out.println("Client " + cName + "is pined to bank " + chbidLine);
    }

    private static void newBank() {
        in = new Scanner(System.in);
        System.out.println("Please, write the bank name: ");
        String name = in.nextLine();

        try{
            String insertTableSQL = "INSERT INTO banks"
                    + "(bank_id, name) " + "VALUES"
                    + "(?,?)";
            dbActionsWParam(insertTableSQL, bid, name);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.printf("Bank %s is saved in database on id - " + bid + "/n", name);
        bid++;
    }

    private static void chooseBank() {
        System.out.println("Choose the bank to which you want to add customers:");
        try{
            String selectTableSQL = "SELECT bank_id, name from banks";
            bankSelect(selectTableSQL);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        if(bankList.size()>0){
            for (Bank bank : bankList) {
                System.out.println(bank.getId() + ". " + bank.getBankName());
            }
        } else {
            System.out.println("No one bank exist");
        }
        in = new Scanner(System.in);
        System.out.println("Write the bank id: ");
        chbid = in.nextInt();
        for (Bank bank : bankList) {
            if (chbid == bank.getId()) {
                chbidLine = bank.getBankName();
                System.out.println("You choosen bank - " + bank.getBankName());
            }
        }
    }

    private static Connection getDBConnection() {
        Connection dbConnection = null;
        String DB_CONNECTION = "jdbc:postgresql://localhost:5432/postgres",
                DB_USER = "postgres",
                DB_PASSWORD = "2242";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

    private static void dbActions(String createTableSQL) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    private static void dbActionsWParam(String insertTableSQL, int bid, String name) throws SQLException {
        Connection dbConnection = null;
        PreparedStatement statement = null;

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.prepareStatement(insertTableSQL);
            statement.setLong(1, bid);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    private static void dbActionsWParam(String insertTableSQL, int cid, int chbid, String cName, int cAge, boolean cGender) throws SQLException {
        Connection dbConnection = null;
        PreparedStatement statement = null;

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.prepareStatement(insertTableSQL);
            statement.setInt(1, cid);
            statement.setLong(2, chbid);
            statement.setString(3, cName);
            statement.setLong(4, cAge);
            statement.setBoolean(5, cGender);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    private static void dbActionsWParam(String insertTableSQL, int aid, int chcid, boolean aType, double aBal, double aOD) throws SQLException {
        Connection dbConnection = null;
        PreparedStatement statement = null;

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.prepareStatement(insertTableSQL);
            statement.setLong(1, aid);
            statement.setLong(2, chcid);
            statement.setBoolean(3, aType);
            statement.setDouble(4, aBal);
            statement.setDouble(5, aOD);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    private static void dbActionsWParam(String updateTableSQL, String lineForUpdateClients, int chcid) throws SQLException {
        Connection dbConnection = null;
        PreparedStatement statement = null;

        try{
            String selectTableSQL = "SELECT * from clients";
            clientSelect(selectTableSQL);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        for (Client client : clientList) {
            if (client.getId() == chcid) {
                if (client.getListAcc() != null) {
                    lineForUpdateClients = client.getListAcc().concat(lineForUpdateClients);
                }
            }
        }

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.prepareStatement(updateTableSQL);
            statement.setString(1, lineForUpdateClients);
            statement.setLong(2, chcid);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    private static void dbActionsWParam(String updateTableSQL, Double newref, int chaid) throws SQLException {
        Connection dbConnection = null;
        PreparedStatement statement = null;

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.prepareStatement(updateTableSQL);
            statement.setDouble(1, newref);
            statement.setLong(2, chaid);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    private static List<Bank> bankSelect(String selectTableSQL) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;
        bankList = new ArrayList();

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            ResultSet rs = statement.executeQuery(selectTableSQL);

            while (rs.next()) {
                int bid = rs.getInt("bank_id");
                String name = rs.getString("name");
                Bank bank = new Bank();
                bank.setId(bid);
                bank.setBankName(name);
                bankList.add(bank);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return bankList;
    }

    private static List<Client> clientSelect(String selectTableSQL) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;
        clientList = new ArrayList();

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            ResultSet rs = statement.executeQuery(selectTableSQL);

            while (rs.next()) {
                int cid = rs.getInt("client_id");
                int chbid = rs.getInt("bank_id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                boolean gender = rs.getBoolean("gender");
                String accounts = rs.getString("account_id");
                Client client = new Client();
                client.setId(cid);
                client.setBankId(chbid);
                client.setName(name);
                client.setAge(age);
                client.setGender(gender);
                client.setListAcc(accounts);
                clientList.add(client);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return clientList;
    }

    private static List<AbstractAccount> accountSelect(String selectTableSQL, int chcid) throws SQLException {
        Connection dbConnection = null;
        PreparedStatement statement = null;
        abAccountList = new ArrayList();

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.prepareStatement(selectTableSQL);
            statement.setLong(1, chcid);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int aid = rs.getInt("account_id");
                int client = rs.getInt("client_id");
                boolean aType = rs.getBoolean("account_type");
                double aBal = rs.getDouble("balance");
                double aOD = rs.getDouble("overdraft");
                if(aType){
                    CheckingAccount account = new CheckingAccount();
                    account.setId(aid);
                    account.setBalance(aBal);
                    account.setOverdraft(aOD);
                    abAccountList.add(account);
                } else {
                    SavingAccount account = new SavingAccount();
                    account.setId(aid);
                    account.setBalance(aBal);
                    abAccountList.add(account);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return abAccountList;
    }

    private static void addBank() {
        newBank();
    }

    private static void addClient() {
        chooseBank();
        newClient();
    }

    private static void addAccount() {
        chooseClient();
        newAcc();
    }

    private static void refill() {
        chooseBank();
        chooseClient();
        showAcc();

        double amoref;
        int chaid;
        in = new Scanner(System.in);
        System.out.println("Write the account id: ");
        chaid = in.nextInt();
        System.out.println("Write the amount refill: ");
        amoref = in.nextDouble();

        for (AbstractAccount abstractAccount : abAccountList) {
            if (abstractAccount.id == chaid) {
                try {
                    newbal = abstractAccount.refill(amoref);
                } catch (NegativeOperationException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        try{
            String updateTableSQL = "UPDATE accounts SET balance = ? WHERE account_id = ?";
            dbActionsWParam(updateTableSQL, newbal, chaid);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void withdrawal() {
        chooseBank();
        chooseClient();
        showAcc();

        double amowith;
        int chaid;
        in = new Scanner(System.in);
        System.out.println("Write the account id: ");
        chaid = in.nextInt();
        System.out.println("Write the amount withdrawal: ");
        amowith = in.nextDouble();

        for (AbstractAccount abstractAccount : abAccountList) {
            if (abstractAccount.id == chaid) {
                try {
                    newbal = abstractAccount.withdrawal(amowith);
                } catch (NegativeOperationException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        try{
            String updateTableSQL = "UPDATE accounts SET balance = ? WHERE account_id = ?";
            dbActionsWParam(updateTableSQL, newbal, chaid);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
