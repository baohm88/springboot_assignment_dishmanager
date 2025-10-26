package com.t2404e.dishmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DishDTO {

    @Schema(description = "Tên món ăn", example = "Phở bò Hà Nội")
    @NotBlank
    @Size(min = 8, max = 250, message = "Name must be between 8 - 250 characters long")
    private String name;

    @Schema(description = "Mô tả chi tiết món ăn", example = "Phở bò nấu theo kiểu truyền thống, nước dùng ngọt thanh từ xương bò")
    @NotBlank(message = "Description is required")
    private String description;

    @Schema(description = "URL ảnh minh họa của món ăn", example = "https://picsum.photos/200?pho")
    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    @Schema(description = "Giá tiền của món ăn", example = "3.50")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be > 0")
    private Double price;

    @Schema(description = "ID của danh mục mà món ăn thuộc về", example = "1")
    @NotNull(message = "Category ID is required")
    private Long categoryId;


    @Schema(description = "Trạng thái của món ăn", example = "ON_SALE", allowableValues = {"ON_SALE", "STOPPED", "DELETED"})
    private String status;
}
