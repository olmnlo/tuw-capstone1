package org.example.capstoneproject1.Service;

import org.example.capstoneproject1.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {

    private ArrayList<Category> categories = new ArrayList<>();

    public ArrayList<Category> getAll() {
        return categories;
    }

    public boolean addCategory(Category category) {
        if (categories.contains(category)) {
            return false;
        }
        categories.add(category);
        return true;
    }

    public boolean updateCategory(String id, Category updatedCategory) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(id)) {
                categories.set(i, updatedCategory);
                return true;
            }
        }
        return false;
    }

    public boolean deleteCategory(String id) {
        for (Category c : categories) {
            if (c.getId().equals(id)) {
                categories.remove(c);
                return true;
            }
        }
        return false;
    }
}
