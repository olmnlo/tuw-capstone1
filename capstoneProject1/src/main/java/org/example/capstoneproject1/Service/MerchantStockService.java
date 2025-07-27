package org.example.capstoneproject1.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Model.MerchantStock;
import org.example.capstoneproject1.Model.Product;
import org.example.capstoneproject1.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantStockService {

    private final UserService userService;
    private final ProductService productService;
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
                                if(p.getPrice() <= u.getBalance()){
                                    u.setBalance(u.getBalance()-p.getPrice());
                                    m.setStock(m.getStock()-1);
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

}
