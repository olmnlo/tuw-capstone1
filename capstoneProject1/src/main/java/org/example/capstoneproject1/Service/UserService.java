package org.example.capstoneproject1.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Model.Product;
import org.example.capstoneproject1.Model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ProductService productService;

    private ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> getAll() {
        return users;
    }

    public void addUser(User user) {
        user.setHistoryProducts(new ArrayList<>());
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
    //2: compare two products
    public Map<Product, Map<String, Boolean>> compareTwoProducts(String productId1, String productId2) {
        Product product1 = null;
        Product product2 = null;

        for (Product p : productService.getAll()) {
            if (p.getId().equals(productId1)) {
                product1 = p;
            }
            if (p.getId().equals(productId2)) {
                product2 = p;
            }
            if (product1 != null && product2 != null) break;
        }

        if (product1 == null || product2 == null) {
            return null;
        }

        Map<String, Boolean> comparison1 = new LinkedHashMap<>();
        Map<String, Boolean> comparison2 = new LinkedHashMap<>();
        if (product1.getPrice() < product2.getPrice()) {
            comparison1.put("price", true);
            comparison2.put("price", false);
        } else if (product1.getPrice() > product2.getPrice()) {
            comparison1.put("price", false);
            comparison2.put("price", true);
        } else {
            comparison1.put("price", true);
            comparison2.put("price", true);
        }

        if (product1.getProductRate() > product2.getProductRate()) {
            comparison1.put("rate", true);
            comparison2.put("rate", false);
        } else if (product1.getProductRate() < product2.getProductRate()) {
            comparison1.put("rate", false);
            comparison2.put("rate", true);
        } else {
            comparison1.put("rate", true);
            comparison2.put("rate", true);
        }

        Map<Product, Map<String, Boolean>> result = new LinkedHashMap<>();
        result.put(product1, comparison1);
        result.put(product2, comparison2);

        return result;
    }


    //5 endpoints
    //3. buy again
    public ArrayList<Product> buyAgain(String userId){
        int userIndex = 0;
        for (int i = 0; i < users.size(); i++){
            if(users.get(i).getId().equals(userId)){
                userIndex = i;
                break;
            }
        }
        if(userIndex == 0){
            return null;
        }
        ArrayList<Product> history = new ArrayList<>();
        for (Product p : productService.getAll()){
            for (String u_p : users.get(userIndex).getHistoryProducts()){
                if(p.getId().equals(u_p)){
                    history.add(p);
                    break;
                }
            }
        }
        return history;
    }

}
