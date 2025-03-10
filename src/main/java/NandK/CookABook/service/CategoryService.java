package NandK.CookABook.service;

import NandK.CookABook.entity.Category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.CategoryCreationRequest;
import NandK.CookABook.dto.request.CategoryUpdateRequest;
import NandK.CookABook.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryCreationRequest request) {
        Category category = new Category();

        category.setName(request.getName());

        return this.categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    public Category getCategoryById(Long categoryId) {
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            return category.get();
        } else {
            return null;
        }
    }

    public Category updateCategoryById(CategoryUpdateRequest request) {
        Category category = this.getCategoryById(request.getId());
        if (category != null) {
            if (request.getName() != null && !request.getName().isBlank()) {
                category.setName(request.getName());
            }
        } else {
            return null;
        }
        return this.categoryRepository.save(category);
    }

    public void deleteCategoryById(Long categoryId) {
        this.categoryRepository.deleteById(categoryId);
    }
}
