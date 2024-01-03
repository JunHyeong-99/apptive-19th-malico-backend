package com.apptive.marico.dto.stylist;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DeleteStyleDto {
    List<Long> deleteStyleIdList = new ArrayList<>();
}
