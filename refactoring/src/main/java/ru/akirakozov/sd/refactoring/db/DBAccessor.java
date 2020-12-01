package ru.akirakozov.sd.refactoring.db;

import java.sql.ResultSet;

public class DBAccessor implements DBInterface {

    @Override
    public void insertProduct(String name, long price) {

    }

    @Override
    public ResultSet selectAllProducts() {
        return null;
    }

    @Override
    public ResultSet selectAllProductsByPriceAsc() {
        return null;
    }

    @Override
    public ResultSet selectAllProductsByPriceDesc() {
        return null;
    }

    @Override
    public long selectProductsPricesSum() {
        return 0;
    }

    @Override
    public long selectProductsCount() {
        return 0;
    }
}
