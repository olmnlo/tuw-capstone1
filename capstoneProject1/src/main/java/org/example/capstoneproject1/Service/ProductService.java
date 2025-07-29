package org.example.capstoneproject1.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Model.Category;
import org.example.capstoneproject1.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ProductService {

    private ArrayList<Product> products = new ArrayList<>();

    private Map<String, ArrayList<Double>> productRateHistory = new LinkedHashMap<>();


    private final CategoryService categoryService;

    public ArrayList<Product> getAll() {
        return products;
    }

    public int addProduct(Product product) {
        for (Category c : categoryService.getAll()){
            if(c.getId().equals(product.getCategoryID())){
                product.setProductRate(0);
                products.add(product);
                return 1;
            }
        }
        return -1;
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

    public Map<String, ArrayList<Double>> getProductRateHistory(){
        return productRateHistory;
    }
}
