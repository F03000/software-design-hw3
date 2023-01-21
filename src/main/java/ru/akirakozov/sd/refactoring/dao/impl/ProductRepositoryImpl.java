package ru.akirakozov.sd.refactoring.dao.impl;

import ru.akirakozov.sd.refactoring.dao.ProductRepository;
import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    public ProductRepositoryImpl() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Exception while initializing database: " + e.getMessage());
        }
    }

    @Override
    public List<Product> findAll() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
            List<Product> productList = new ArrayList<>();

            while (rs.next()) {
                String name = rs.getString("name");
                long price = rs.getLong("price");
                productList.add(new Product(name, price));
            }
            rs.close();
            stmt.close();
            return productList;
        }
    }

    @Override
    public void addProduct(Product product) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    @Override
    public Product findProductWithMaxPrice() throws SQLException {

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");

            Product productWithMaxPrice = null;
            while (rs.next()) {
                String name = rs.getString("name");
                long price = rs.getLong("price");
                productWithMaxPrice = new Product(name, price);
            }
            rs.close();
            stmt.close();
            return productWithMaxPrice;
        }
    }

    @Override
    public Product findProductWithMinPrice() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

            Product productWithMaxPrice = null;
            while (rs.next()) {
                String name = rs.getString("name");
                long price = rs.getLong("price");
                productWithMaxPrice = new Product(name, price);
            }
            rs.close();
            stmt.close();
            return productWithMaxPrice;
        }
    }

    @Override
    public Long getProductSum() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
            Long result = null;
            if (rs.next()) {
                result = rs.getLong(1);
            }
            rs.close();
            stmt.close();
            return result;
        }
    }

    @Override
    public Long getProductCount() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
            Long result = null;
            if (rs.next()) {
                result = rs.getLong(1);
            }
            rs.close();
            stmt.close();
            return result;
        }
    }
}
