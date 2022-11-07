package ru.akirakozov.sd.refactoring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class DBUtil {

    public static void makeDBRequest(String sqlQuery) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sqlQuery);
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer getNumberByQuery(String sqlQuery) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            Integer result = null;
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            stmt.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Product> getProductsByQuery(String sqlQuery) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            List<Product> productList = new ArrayList<>();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int price = resultSet.getInt("price");
                productList.add(new Product(name, price));
            }
            stmt.close();
            return productList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
