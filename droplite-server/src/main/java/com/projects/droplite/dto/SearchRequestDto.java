package com.projects.droplite.dto;

import com.projects.droplite.constant.Constants;
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
    private String sortField = Constants.DEFAULT_SORT_FIELD;
    private String sortOrder = Constants.DEFAULT_SORT_ORDER;

}
