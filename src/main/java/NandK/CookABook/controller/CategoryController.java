package NandK.CookABook.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import NandK.CookABook.dto.request.CategoryCreationRequest;
import NandK.CookABook.dto.request.CategoryUpdateRequest;
import NandK.CookABook.entity.Category;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.CategoryService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ApiMessage("Tạo danh mục thành công")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryCreationRequest request) {
        return ResponseEntity.ok(this.categoryService.createCategory(request));
    }

    @GetMapping
    @ApiMessage("Lấy danh sách danh mục thành công")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(this.categoryService.getAllCategories());
    }

    @GetMapping("/{categoryId}")
    @ApiMessage("Lấy danh mục thành công")
    public ResponseEntity<Category> getCategoryById(@Valid @PathVariable Long categoryId) throws IdInvalidException {
        Category category = this.categoryService.getCategoryById(categoryId);
        if (category == null) {
            throw new IdInvalidException("Danh mục với id = " + categoryId + " không tồn tại");
        }
        return ResponseEntity.ok(category);
    }

    @PutMapping
    @ApiMessage("Cập nhật danh mục thành công")
    public ResponseEntity<Category> updateCategoryById(@Valid @RequestBody CategoryUpdateRequest request)
            throws IdInvalidException {
        Category category = this.categoryService.updateCategoryById(request);
        if (category == null) {
            throw new IdInvalidException("Danh mục với id = " + request.getId() + " không tồn tại");
        }
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{categoryId}")
    @ApiMessage("Xóa danh mục thành công")
    public ResponseEntity<Void> deleteCategoryById(@Valid @PathVariable Long categoryId) throws IdInvalidException {
        Category category = this.categoryService.getCategoryById(categoryId);
        if (category == null) {
            throw new IdInvalidException("Danh mục với id = " + categoryId + " không tồn tại");
        }
        this.categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.ok(null);
    }
}
