package org.example.capstoneproject1.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Model.Product;
import org.example.capstoneproject1.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ProductService productService;

    private ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> getAll() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public boolean updateUser(String id, User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.set(i, updatedUser);
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                users.remove(user);
                return true;
            }
        }
        return false;
    }

    //filter by category id
    public ArrayList<Product> filterByCategoryId(String categoryId){
        ArrayList<Product> filtered = new ArrayList<>();
        for (Product p : productService.getAll()){
            if(p.getCategoryID().equals(categoryId)){
                filtered.add(p);
            }
        }
        return filtered;
    }

    //filter by price range
    public ArrayList<Product> filterByPriceRange(int min, int max){
        ArrayList<Product> filtered = new ArrayList<>();
        for (Product p : productService.getAll()){
            if(p.getPrice() >= min && p.getPrice() <= max){
                filtered.add(p);
            }
        }
        return filtered;
    }


    //5 endpoints
    //compare two products
    public Map<String, Double> compareTwoProducts(String producId1, String productId2){
        Product product1 = null;
        Product product2 = null;
        Map<String , Double> comparedItem = new LinkedHashMap<>();
        for (Product p : productService.getAll()){
            if(p.getId().equals(producId1)){
                product1 = p;
            }
            if(p.getId().equals(productId2)){
                product2 = p;
            }

            if(product1 != null && product2 != null){
                break;
            }
        }

        if(product1 == null || product2 == null) {
            return null;
        }

        if(product1.getPrice() > product2.getPrice()){
            comparedItem.put("product1 id: "+product1.getId()+" is more Price: ", product1.getPrice());
        }else if (product1.getPrice() < product2.getPrice()){
            comparedItem.put("product2 is more Price: ", product2.getPrice());
        }else {
            comparedItem.put("product1 id: "+product1.getId() + " and product2 id: "+ product2.getId()+ "are equal price", product2.getPrice());
        }
        if(product1.getProductRate() > product2.getProductRate()){
            comparedItem.put("product1 is more Rate: ", product1.getProductRate());
        }else if (product1.getProductRate() < product2.getProductRate()){
            comparedItem.put("product2 is more Rate: ", product2.getProductRate());
        }else {
            comparedItem.put("product1 id: "+product1.getProductRate() + " and product2 id: "+ product2.getId()+ "are equal Rate", product2.getProductRate());
        }
        return comparedItem;
    }

}
