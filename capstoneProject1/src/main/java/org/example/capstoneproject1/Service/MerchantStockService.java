package org.example.capstoneproject1.Service;

import org.example.capstoneproject1.Model.MerchantStock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantStockService {

    private final ArrayList<MerchantStock> stocks = new ArrayList<>();

    public ArrayList<MerchantStock> getAll() {
        return stocks;
    }

    public void addMerchantStock(MerchantStock stock) {
        stocks.add(stock);
    }

    public boolean updateMerchantStock(String id, MerchantStock updatedStock) {
        for (int i = 0; i < stocks.size(); i++) {
            if (stocks.get(i).getId().equals(id)) {
                stocks.set(i, updatedStock);
                return true;
            }
        }
        return false;
    }

    public boolean deleteMerchantStock(String id) {
        return stocks.removeIf(stock -> stock.getId().equals(id));
    }
}
