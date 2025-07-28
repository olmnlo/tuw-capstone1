package org.example.capstoneproject1.Service;

import org.example.capstoneproject1.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class ProductService {

    private ArrayList<Product> products = new ArrayList<>();

    public ArrayList<Product> getAll() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean updateProduct(String id, Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                products.set(i, updatedProduct);
                return true;
            }
        }
        return false;
    }

    public boolean deleteProduct(String id) {
        for (Product p : products){
            if(p.getId().equals(id)){
                products.remove(p);
                return true;
            }
        }
        return false;
    }


    //5 endpoints with logic
    //1: filter by TOP 10 RATED products logic #1
    public ArrayList<Product> filterByRate(){
        if(products.isEmpty()){
            return null;
        }
        ArrayList<Product> filtered = new ArrayList<>();
        for (int i = 0; i < products.size() - 1; i++) {
            for (int j = 0; j < products.size() - i - 1; j++) {
                if (products.get(j).getProductRate() > products.get(j + 1).getProductRate()) {
                    Product temp = products.get(j);
                    products.set(j, products.get(j + 1));
                    products.set(j + 1, temp);
                }
            }
        }
        for (int i = products.size() - 1; i >= Math.max(0, products.size() - 10); i--){
            filtered.add(products.get(i));
        }
        return filtered;
    }
}
