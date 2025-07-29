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
    private Map<String, ArrayList<String>> userHistory = new LinkedHashMap<>();

    public ArrayList<User> getAll() {
        return users;
    }

    public boolean addUser(User user) {
        if (users.contains(user)){
            return false;
        }
        users.add(user);
        return true;
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
    public Map<String, Map<String, Boolean>> compareTwoProducts(String productId1, String productId2) {
        Product product1 = null;
        Product product2 = null;
        for (Product p : productService.getAll()) {
            if (p.getId().equals(productId1)) {
                product1 = p;
            }
            if (p.getId().equals(productId2)) {
                product2 = p;
            }
            if (product1 != null && product2 != null) {
                break;
            }
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
        Map<String, Map<String, Boolean>> result = new LinkedHashMap<>();
        result.put("product id "+product1.getId(), comparison1);
        result.put("product id"+product2.getId(), comparison2);
        return result;
    }


    //5 endpoints
    //3.discount on subscribed users only
    public ArrayList<Product> discount(String userId){
        ArrayList<Product> discount = new ArrayList<>();
        int userIndex = -1;
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getId().equals(userId)){
                userIndex = i;
            }
        }
        if(userIndex == -1){
            return null;
        }

        for(Product p : productService.getAll()){
            if(p.isDiscount20() && users.get(userIndex).isSubscribed()){
                double discountedPrice = p.getPrice() - (p.getPrice() * 0.2);
                Product discountedProduct = new Product(p.getId(),p.getName(),discountedPrice,p.getCategoryID(), p.getProductRate(), p.isDiscount20(), p.isSeasonalProduct(), p.getOffer());
                discount.add(discountedProduct);
            }
        }
        return discount;
    }



    public boolean rateProduct(String productId, String userId , double rating){
        Map<String, ArrayList<Double>> productRatings = productService.getProductRateHistory();
        // 1. Check if the user has a history
        if (!userHistory.containsKey(userId)) {
            System.out.println("User not found.");
            return false;
        }

        // 2. Check if the user has this product in history
        if (!userHistory.get(userId).contains(productId)) {
            return false;
        }

        productRatings.putIfAbsent(productId, new ArrayList<>());
        productRatings.get(productId).add(rating);
        double total = 0;
        for (Double r : productRatings.get(productId)){
            total+=r;
        }
        total = total/productRatings.get(productId).size();

        for (Product p : productService.getAll()){
            if (p.getId().equals(productId)){
                p.setProductRate(total);

            }
        }


        userHistory.get(userId).remove(productId);

        return true;
    }

    public boolean subscribe(String userId){
        for (User u : users){
            if(u.getId().equals(userId)){
                if(u.getBalance() >= 60){
                    u.setSubscribed(true);
                    u.setBalance(u.getBalance()-60);
                    return true;
                }
            }
        }
        return false;
    }



    public Map<String, ArrayList<String>> getUserHistory(){
        return userHistory;
    }

}
