package NandK.CookABook.service;

import NandK.CookABook.entity.Book;
import NandK.CookABook.entity.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.category.CategoryCreationRequest;
import NandK.CookABook.dto.request.category.CategoryUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.category.CategoryCreationResponse;
import NandK.CookABook.dto.response.category.CategoryFoundResponse;
import NandK.CookABook.dto.response.category.CategoryUpdateResponse;
import NandK.CookABook.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public boolean isCategoryNameExist(String name) {
        return this.categoryRepository.existsByName(name);
    }

    public Category createCategory(CategoryCreationRequest request) {
        Category category = new Category();

        category.setName(request.getName());

        return this.categoryRepository.save(category);
    }

    public CategoryCreationResponse convertToCategoryCreationResponse(Category category) {
        CategoryCreationResponse categoryCreationResponse = new CategoryCreationResponse();
        categoryCreationResponse.setId(category.getId());
        categoryCreationResponse.setName(category.getName());
        categoryCreationResponse.setCreatedAt(category.getCreatedAt());
        return categoryCreationResponse;
    }

    public List<String> getBookIdsByCategory(Category category) {
        List<String> bookIds = new ArrayList<>();
        for (Book book : category.getBooks()) {
            bookIds.add(book.getId().toString());
        }
        return bookIds;
    }

    public ResultPagination getAllCategories(Specification<Category> spec, Pageable pageable) {
        Page<Category> categories = this.categoryRepository.findAll(spec, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotalPages(categories.getTotalPages());
        meta.setTotalElements(categories.getTotalElements());

        result.setMeta(meta);

        List<CategoryFoundResponse> listCategories = categories.getContent().stream().map(
                item -> new CategoryFoundResponse(
                        item.getId(),
                        item.getName(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        this.getBookIdsByCategory(item)))
                .collect(Collectors.toList());
        result.setData(listCategories);
        return result;
    }

    public Category getCategoryById(Long categoryId) {
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            return category.get();
        } else {
            return null;
        }
    }

    public Category updateCategory(CategoryUpdateRequest request) {
        Category category = this.getCategoryById(request.getId());
        if (category != null) {
            if (request.getName() != null && !request.getName().isBlank()) {
                category.setName(request.getName());
            }
            return this.categoryRepository.save(category);
        } else {
            return null;
        }
    }

    public CategoryUpdateResponse convertToCategoryUpdateResponse(Category category) {
        CategoryUpdateResponse categoryUpdateResponse = new CategoryUpdateResponse();
        categoryUpdateResponse.setId(category.getId());
        categoryUpdateResponse.setName(category.getName());
        categoryUpdateResponse.setUpdatedAt(category.getUpdatedAt());
        return categoryUpdateResponse;
    }

    public void deleteCategoryById(Long categoryId) {
        // Xóa tất cả các sách liên quan đến danh mục trước khi xóa danh mục
        Category category = this.getCategoryById(categoryId);
        category.getBooks().forEach(book -> book.getCategories().remove(category));
        // Xóa danh mục
        this.categoryRepository.deleteById(categoryId);
    }
}
