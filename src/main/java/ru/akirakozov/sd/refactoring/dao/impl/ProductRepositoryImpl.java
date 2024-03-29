package ru.akirakozov.sd.refactoring.dao.impl;

import ru.akirakozov.sd.refactoring.dao.ProductRepository;
import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    private static final String CONNECTION_STRING = "jdbc:sqlite:test.db";

    public ProductRepositoryImpl() {
        try (Connection c = DriverManager.getConnection(CONNECTION_STRING)) {
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
    public void addProduct(Product product) {
        try (Connection c = DriverManager.getConnection(CONNECTION_STRING)) {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Exception while getting product list from database: " + e.getMessage());
        }
    }

    @Override
    public List<Product> findAll() {
        try {
            return runQueryAndGetProductList("SELECT * FROM PRODUCT");
        } catch (SQLException e) {
            throw new RuntimeException("Exception while getting products from database: " + e.getMessage());
        }
    }

    private List<Product> runQueryAndGetProductList(String query) throws SQLException {
        try (Connection c = DriverManager.getConnection(CONNECTION_STRING)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
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
    public Product findProductWithMaxPrice() {
        try {
            List<Product> resultList = runQueryAndGetProductList("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
            return resultList.isEmpty() ? null : resultList.get(0);
        } catch (SQLException e) {
            throw new RuntimeException("Exception while executing query 'max': " + e.getMessage());
        }
    }

    @Override
    public Product findProductWithMinPrice() {
        try {
            List<Product> resultList = runQueryAndGetProductList("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
            return resultList.isEmpty() ? null : resultList.get(0);
        } catch (SQLException e) {
            throw new RuntimeException("Exception while executing query 'min': " + e.getMessage());
        }
    }

    @Override
    public long getProductSum() {
        try {
            return runQueryAndGetLong("SELECT SUM(price) FROM PRODUCT");
        } catch (SQLException e) {
            throw new RuntimeException("Exception while executing query 'sum': " + e.getMessage());
        }
    }

    private long runQueryAndGetLong(String query) throws SQLException {
        try (Connection c = DriverManager.getConnection(CONNECTION_STRING)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            long result;
            if (rs.next()) {
                result = rs.getLong(1);
            } else {
                throw new SQLException("Query did not return result");
            }
            rs.close();
            stmt.close();
            return result;
        }
    }

    @Override
    public long getProductCount() {
        try {
            return runQueryAndGetLong("SELECT COUNT(*) FROM PRODUCT");
        } catch (SQLException e) {
            throw new RuntimeException("Exception while executing query 'count': " + e.getMessage());
        }
    }
}
