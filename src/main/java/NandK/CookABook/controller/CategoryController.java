package NandK.CookABook.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import NandK.CookABook.dto.request.category.CategoryCreationRequest;
import NandK.CookABook.dto.request.category.CategoryUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.entity.Category;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.CategoryService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryCreationRequest request)
            throws IdInvalidException {
        boolean isCategoryNameExist = this.categoryService.isCategoryNameExist(request.getName());
        if (isCategoryNameExist) {
            throw new IdInvalidException(
                    "Danh mục với tên = " + request.getName() + " đã tồn tại, vui lòng sử dụng tên khác");
        }
        return ResponseEntity.ok(this.categoryService.createCategory(request));
    }

    @GetMapping("/all")
    @ApiMessage("Lấy danh sách danh mục thành công")
    public ResponseEntity<ResultPagination> getAllCategories(
            @Filter Specification<Category> spec, Pageable pageable) {
        return ResponseEntity.ok(this.categoryService.getAllCategories(spec, pageable));
    }

    @GetMapping("/{categoryId}")
    @ApiMessage("Lấy danh mục thành công")
    public ResponseEntity<Category> getCategoryById(@Valid @PathVariable Long categoryId)
            throws IdInvalidException {
        Category category = this.categoryService.getCategoryById(categoryId);
        if (category == null) {
            throw new IdInvalidException("Danh mục với id = " + categoryId + " không tồn tại");
        }
        return ResponseEntity.ok(category);
    }

    @PutMapping
    @ApiMessage("Cập nhật danh mục thành công")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody CategoryUpdateRequest request)
            throws IdInvalidException {
        Category category = this.categoryService.getCategoryById(request.getId());
        if (category == null) {
            throw new IdInvalidException("Danh mục với id = " + request.getId() + " không tồn tại");
        }
        boolean isCategoryNameExist = this.categoryService.isCategoryNameExist(request.getName());
        if (isCategoryNameExist) {
            throw new IdInvalidException(
                    "Danh mục với tên = " + request.getName() + " đã tồn tại, vui lòng sử dụng tên khác");
        }
        category = this.categoryService.updateCategory(request);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{categoryId}")
    @ApiMessage("Xóa danh mục thành công")
    public ResponseEntity<Void> deleteCategoryById(@Valid @PathVariable Long categoryId)
            throws IdInvalidException {
        Category category = this.categoryService.getCategoryById(categoryId);
        if (category == null) {
            throw new IdInvalidException("Danh mục với id = " + categoryId + " không tồn tại");
        }
        this.categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.ok(null);
    }
}
