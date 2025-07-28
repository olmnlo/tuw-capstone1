package org.example.capstoneproject1.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Model.Merchant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private ArrayList<Merchant> merchants = new ArrayList<>();

    public ArrayList<Merchant> getAll() {
        return merchants;
    }

    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
    }

    public boolean updateMerchant(String id, Merchant updatedMerchant) {
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equals(id)) {
                merchants.set(i, updatedMerchant);
                return true;
            }
        }
        return false;
    }

    public boolean deleteMerchant(String id) {
        for (Merchant m : merchants){
            if(m.getId().equals(id)){
                merchants.remove(m);
                return true;
            }
        }
        return false;
    }
}
