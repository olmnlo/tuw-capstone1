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



}
