package com.otus.datatable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class DataTableCourse {

    private String name;
    private String dateString;
    private Date date;
}
