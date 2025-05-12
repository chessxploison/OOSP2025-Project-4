package com.vsuet;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DataStorageAPI {
    private Connection connection;
    private Map<Integer, String> readOnlyCache; // Кэш для read-only данных

    public DataStorageAPI(String dbUrl, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl, user, password);
        this.readOnlyCache = new HashMap<>();
    }

    // Метод для загрузки данных из БД
    public String loadData(int id) throws SQLException {
        // Проверяем кэш
        if (readOnlyCache.containsKey(id)) {
            return readOnlyCache.get(id);
        }

        // Если нет в кэше, загружаем из БД
        String query = "SELECT data FROM my_table WHERE id = ? AND is_read_only = true";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String data = rs.getString("data");
                readOnlyCache.put(id, data); // Кэшируем данные
                return data;
            }
        }
        return null; // Данные не найдены
    }

    // Метод для сохранения данных в БД
    public void saveData(int id, String data) throws SQLException {
        String query = "UPDATE my_table SET data = ?, is_read_only = false WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, data);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    // Метод для выгрузки результатов
    public void exportResults() throws SQLException {
        // Здесь можно реализовать логику выгрузки результатов
        // Например, формирование отчета на основе данных из БД или кэша
    }

    // Закрытие соединения с БД
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}