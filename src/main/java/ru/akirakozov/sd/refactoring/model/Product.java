package ru.akirakozov.sd.refactoring.model;

public class Product {
    private final String name;
    private final Long price;

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Product(String name, Long price) {
        this.name = name;
        this.price = price;
    }
}
