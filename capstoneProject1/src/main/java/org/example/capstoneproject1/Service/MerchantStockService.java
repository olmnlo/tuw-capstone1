package org.example.capstoneproject1.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MerchantStockService {

    private final UserService userService;
    private final ProductService productService;
    private final MerchantService merchantService;
    private ArrayList<MerchantStock> stocks = new ArrayList<>();

    public ArrayList<MerchantStock> getAll() {
        return stocks;
    }

    public int addMerchantStock(MerchantStock stock) {
        if(stocks.contains(stock)){
            return -3;
        }
        for (Merchant m : merchantService.getAll()){
            if(m.getId().equals(stock.getMerchantId())){
                for (Product p : productService.getAll()){
                    if(p.getId().equals(stock.getProductId())){
                        stock.setSoldProducts(0);
                        stocks.add(stock);
                        return 1;
                    }
                }
                return -2; // product not found
            }
        }
        return -1; // merchant not found

    }

    public boolean updateMerchantStock(String id, MerchantStock updatedStock) {
        for (int i = 0; i < stocks.size(); i++) {
            if (stocks.get(i).getId().equals(id)) {
                updatedStock.setMerchantRate(stocks.get(i).getMerchantRate());
                updatedStock.setSoldProducts(stocks.get(i).getSoldProducts());
                stocks.set(i, updatedStock);
                return true;
            }
        }
        return false;
    }

    public boolean deleteMerchantStock(String id) {
        return stocks.removeIf(stock -> stock.getId().equals(id));
    }


    //Create endpoint where merchant can add more stocks of product to a
    //merchant Stock
    //• this endpoint should accept a product id and merchant id and the amount of additional
    //stock.

    public boolean addToStock(String productId, String merchantId, int amount){
        for(MerchantStock m : stocks){
            if(m.getProductId().equals(productId) && m.getMerchantId().equals(merchantId)){
                m.setStock(m.getStock()+amount);
                return true;
            }
        }
        return false;
    }


    //12-
    //this endpoint should accept user id, product id, merchant id.•check if all the given ids are valid or not
    //•check if the merchant has the product in stock or return bad request.
    //•reduce the stock from the MerchantStock.
    //•deducted the price of the product from the user balance.
    //•if balance is less than the product price returns bad request.

    public int buyDirectly(String userId, String productId, String merchantId) {
        for (MerchantStock m : stocks){
            if(m.getMerchantId().equals(merchantId)){
                for (User u : userService.getAll()){
                    if(u.getId().equals(userId)){
                        for (Product p : productService.getAll()){
                            if(p.getId().equals(productId)){
                                if (p.getPrice() <= u.getBalance() && p.isSeasonalProduct() && p.isDiscount20() && m.getStock() > 0 ){
                                    double discount = p.getOffer()+0.2;
                                    u.setBalance(u.getBalance()-(p.getPrice()-(p.getPrice()*discount)));
                                    m.setStock(m.getStock()-1);
                                    m.setSoldProducts(m.getSoldProducts()+1);

                                    if (!userService.getUserHistory().containsKey(userId)) {
                                        userService.getUserHistory().put(userId, new ArrayList<>());
                                    }
                                    if (!userService.getUserHistory().get(userId).contains(p.getId())) {
                                        userService.getUserHistory().get(userId).add(p.getId());
                                    }

                                    return 1; // thank you for buying come again

                                }else if(p.getPrice() <= u.getBalance() && p.isSeasonalProduct() && m.getStock() > 0){
                                    u.setBalance(u.getBalance()-(p.getPrice()-(p.getPrice()*p.getOffer())));
                                    m.setStock(m.getStock()-1);
                                    m.setSoldProducts(m.getSoldProducts()+1);

                                    if (!userService.getUserHistory().containsKey(userId)) {
                                        userService.getUserHistory().put(userId, new ArrayList<>());
                                    }
                                    if (!userService.getUserHistory().get(userId).contains(p.getId())) {
                                        userService.getUserHistory().get(userId).add(p.getId());
                                    }

                                    return 1; // thank you for buying come again
                                }else if(p.getPrice() <= u.getBalance() && p.isDiscount20() && m.getStock() > 0){
                                    u.setBalance(u.getBalance()-(p.getPrice()*0.2));
                                    m.setStock(m.getStock()-1);
                                    m.setSoldProducts(m.getSoldProducts()+1);

                                    if (!userService.getUserHistory().containsKey(userId)) {
                                        userService.getUserHistory().put(userId, new ArrayList<>());
                                    }
                                    if (!userService.getUserHistory().get(userId).contains(p.getId())) {
                                        userService.getUserHistory().get(userId).add(p.getId());
                                    }


                                    return 1; // thank you for buying come again
                                }else if (p.getPrice() <= u.getBalance()){
                                    u.setBalance(u.getBalance()-p.getPrice());
                                    m.setStock(m.getStock()-1);
                                    m.setSoldProducts(m.getSoldProducts()+1);

                                    if (!userService.getUserHistory().containsKey(userId)) {
                                        userService.getUserHistory().put(userId, new ArrayList<>());
                                    }
                                    if (!userService.getUserHistory().get(userId).contains(p.getId())) {
                                        userService.getUserHistory().get(userId).add(p.getId());
                                    }

                                }else {
                                    return 2; // user balance less than product price
                                }
                            }
                        }
                        return 3; // product not found
                    }
                }
                return 4; // user not found
            }
        }
        return 5; // merchant not found
    }




    //5 endpoints
    //4. seasonal products
    public int seasonalProducts(String merchantId, String productId, double discount){
        if (discount >= 1){
            discount = discount/100;
        }else if (discount < 0){
            return -1;
        }
        for (MerchantStock m : stocks){
            if(m.getMerchantId().equals(merchantId)){
                if(m.getProductId().equals(productId)){
                    for (Product p : productService.getAll()){
                        if(p.getId().equals(productId)){
                            p.setOffer(discount);
                            p.setSeasonalProduct(!m.isSeasonalProduct());
                            m.setSeasonalProduct(!m.isSeasonalProduct());
                        }
                    }
                    return 1; //product now updated successfully: it is in seasonal product offers now
                }
                return 2; // product not found
            }
        }
        return 3; // merchant not found
    }




    //5 endpoints
    //5. merchants performance
    public Map<String, Object> merchantPerformance(String userId) {
        User foundUser = null;
        for (User u : userService.getAll()) {
            if (u.getId().equals(userId)) {
                foundUser = u;
                break;
            }
        }

        if (foundUser == null) {
            return null;
        }

        Map<String, Double> merchantScoreMap = new HashMap<>();

        Map<String, Integer> merchantStockMap = new HashMap<>();

        int maxSold = 0;
        for (MerchantStock stock : stocks){
            maxSold = Math.max(maxSold,stock.getSoldProducts());
        }
        if(maxSold == 0){
            maxSold = 1;
        }

        for (MerchantStock stock : stocks) {
            String merchantId = stock.getMerchantId();
            for (Product p : productService.getAll()){
                if (p.getId().equals(stock.getProductId())){
                    stock.setMerchantRate(p.getProductRate());
                }
            }

            double rate = stock.getMerchantRate();
            int sold = stock.getSoldProducts();
            int currentStock = stock.getStock();

            if (sold == 0) sold = 1;

            double normalizedSold = (double) sold / maxSold;
            double ratio;

                ratio =(rate * 0.7) + (normalizedSold * 0.3);

            if (merchantScoreMap.containsKey(merchantId)) {
                double oldRatio = merchantScoreMap.get(merchantId);
                merchantScoreMap.put(merchantId, (oldRatio + ratio) / 2);

                int oldStock = merchantStockMap.get(merchantId);
                merchantStockMap.put(merchantId, oldStock + currentStock);
            } else {
                merchantScoreMap.put(merchantId, ratio);
                merchantStockMap.put(merchantId, currentStock);
            }
        }

        ArrayList<Map.Entry<String, Double>> entryList = new ArrayList<>(merchantScoreMap.entrySet());

        for (int i = 0; i < entryList.size(); i++) {
            for (int j = i + 1; j < entryList.size(); j++) {
                if (entryList.get(j).getValue() > entryList.get(i).getValue()) {
                    Map.Entry<String, Double> temp = entryList.get(i);
                    entryList.set(i, entryList.get(j));
                    entryList.set(j, temp);
                }
            }
        }

        Map<String, Object> output = new LinkedHashMap<>();

        for (Map.Entry<String, Double> entry : entryList) {
            String merchantId = entry.getKey();
            double ratio = entry.getValue();
            int stock = merchantStockMap.getOrDefault(merchantId, 0);
            Map<String, Object> merchantInfo = new LinkedHashMap<>();
            merchantInfo.put("Score", String.format("%.2f", ratio));
            merchantInfo.put("Stock", stock);
            output.put("merchant id "+merchantId, merchantInfo);
        }

        return output;
    }


    public int toggleDiscount20(String merchantId, String productId){
        for (MerchantStock m : stocks){
            if(m.getMerchantId().equals(merchantId)){
                if(m.getProductId().equals(productId)) {
                    for (Product p : productService.getAll()) {
                        if (p.getId().equals(productId)) {
                            p.setDiscount20(!p.isDiscount20());
                            return 1; //discount updated
                        }
                    }
                }
                    return -2; // product not found
            }
        }

        return -1; //merchant not found
    }


}
