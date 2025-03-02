package com.droplite.dto;

import com.droplite.constant.FileConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SearchRequestDto {

    private int pageNumber = 1;
    private int pageSize = 10;
    private String pattern;
    private String fileType;
    private String sortField = FileConstants.DEFAULT_SORT_FIELD;
    private String sortOrder = FileConstants.DEFAULT_SORT_ORDER;

}
