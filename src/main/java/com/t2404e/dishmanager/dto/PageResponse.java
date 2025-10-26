package com.t2404e.dishmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {


    @Schema(description = "Danh sách dữ liệu hiện tại (theo trang)", example = "[{...}, {...}]")
    private List<T> content;

    @Schema(description = "Số trang hiện tại (bắt đầu từ 1)", example = "1")
    private Integer currentPage;

    @Schema(description = "Số lượng bản ghi mỗi trang", example = "10")
    private Integer pageSize;

    @Schema(description = "Tổng số trang", example = "5")
    private Integer totalPages;

    @Schema(description = "Tổng số bản ghi (toàn bộ)", example = "47")
    private Integer totalElements;
}
