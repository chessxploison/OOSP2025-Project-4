package com.vsuet;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DataStorageAPI api = new DataStorageAPI("jdbc:mysql://localhost:3306/mydatabase", "myuser", "mypassword");
            String data = api.loadData(1);
            System.out.println("Loaded data: " + data);
            api.saveData(2, "New data");
            api.exportResults();
            api.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}