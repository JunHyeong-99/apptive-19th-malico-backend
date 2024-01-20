package com.apptive.marico.service;

import com.apptive.marico.dto.stylist.StylistDetailDto;
import com.apptive.marico.dto.stylistService.StylistFilterDto;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.StylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.apptive.marico.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final StylistRepository stylistRepository;

    public String filter(StylistFilterDto stylistFilterDto) {
        return "0";
    }

    public StylistDetailDto stylistDetail(Long stylistId) {
        Stylist stylist = stylistRepository.findByIdWithService(stylistId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return StylistDetailDto.toDto(stylist);
    }
}
