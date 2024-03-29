package com.vvc.domain.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfoVo {
    private String description;
    private Long id;
    private String name;
    private String status;
}
