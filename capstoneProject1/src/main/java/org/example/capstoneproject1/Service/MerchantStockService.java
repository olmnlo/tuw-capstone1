package org.example.capstoneproject1.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Model.Merchant;
import org.example.capstoneproject1.Model.MerchantStock;
import org.example.capstoneproject1.Model.Product;
import org.example.capstoneproject1.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                System.out.println("merchant found");
                for (User u : userService.getAll()){
                    if(u.getId().equals(userId)){
                        System.out.println("user found");
                        for (Product p : productService.getAll()){
                            if(p.getId().equals(productId)){
                                System.out.println("product found");
                                if(p.getPrice() <= u.getBalance() && p.isSeasonalProduct()){
                                    u.setBalance(u.getBalance()-(p.getPrice()-(p.getPrice()*p.getOffer())));
                                    m.setStock(m.getStock()-1);
                                    m.setSoldProducts(m.getSoldProducts()+1);
                                    return 1; // thank you for buying come again
                                }else if(p.getPrice() <= u.getBalance()){
                                    u.setBalance(u.getBalance()-p.getPrice());
                                    m.setStock(m.getStock()-1);
                                    m.setSoldProducts(m.getSoldProducts()+1);
                                    return 1; // thank you for buying come again
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
    public ArrayList<String> merchantPerformance(String userId, boolean quality) {
        User foundUser = null;
        for (User u : userService.getAll()) {
            if (u.getId().equals(userId)) {
                foundUser = u;
                break;
            }
        }

        if (foundUser == null || !foundUser.getRole().equals("Admin")) {
            return null;
        }

        Map<String, Double> merchantScoreMap = new HashMap<>();

        Map<String, Integer> merchantStockMap = new HashMap<>();

        int maxSold = 0;
        for (MerchantStock stock : stocks){
            maxSold = Math.max(maxSold,stock.getSoldProducts());
        }


        for (MerchantStock stock : stocks) {
            String merchantId = stock.getMerchantId();

            double rate = stock.getMerchantRate();
            int sold = stock.getSoldProducts();
            int currentStock = stock.getStock();

            if (sold == 0) sold = 1;

            double normalizedSold = (double) sold / maxSold;
            double ratio;
            if (quality) {
                ratio =(rate * 0.7) + (normalizedSold * 0.3);
            }else {
                ratio =(rate * 0.5) + (normalizedSold * 0.5);
            }
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

        ArrayList<String> output = new ArrayList<>();

        for (Map.Entry<String, Double> entry : entryList) {
            String merchantId = entry.getKey();
            double ratio = entry.getValue();
            int stock = merchantStockMap.getOrDefault(merchantId, 0);

            output.add("MerchantId: " + merchantId + ", Score: " + String.format("%.2f", ratio) + ", Stock: " + stock);
        }

        return output;
    }


}
