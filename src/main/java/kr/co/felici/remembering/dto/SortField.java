package kr.co.felici.remembering.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public enum SortField {

    ID("id"),
    NAME("name"),
    DISPLAYORDER("displayOrder");

    private final String databaseFieldName;

}
