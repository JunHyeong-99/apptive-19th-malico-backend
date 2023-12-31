package com.apptive.marico.service;
import com.apptive.marico.dto.CareerDto;
import com.apptive.marico.dto.stylist.StylistMypageDto;
import com.apptive.marico.dto.stylist.StylistMypageEditDto;
import com.apptive.marico.dto.stylist.service.ServiceCategoryDto;
import com.apptive.marico.dto.stylist.service.StylistServiceDto;
import com.apptive.marico.dto.stylist.service.StylistServiceResponseDto;
import com.apptive.marico.entity.Career;
import com.apptive.marico.entity.ServiceCategory;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.StylistService;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.CareerRepository;
import com.apptive.marico.repository.ServiceCategoryRepository;
import com.apptive.marico.repository.StylistServiceRepository;
import com.apptive.marico.repository.StylistRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.apptive.marico.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class StylistMypageService {
    private final StylistRepository stylistRepository;
    private final CareerRepository careerRepository;
    private final StylistServiceRepository serviceRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;

    public StylistMypageDto mypage(String userId) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        return StylistMypageDto.toDto(stylist);
    }

    public StylistMypageEditDto getInformation(String userId) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        return StylistMypageEditDto.toDto(stylist);
    }

    public String editInformation(String userId, StylistMypageEditDto stylistMypageEditDto) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        // 기존 career 삭제 후 다음 career 등록
        careerRepository.deleteByStylist(stylist);
        //연관관계 주인을 통한 새로운 career 저장
        List<CareerDto> careerDtoList = stylistMypageEditDto.getCareerDtoList();
        careerDtoList.stream()
                .map(careerDto -> createCareer(careerDto, stylist))
                .forEach(careerRepository::save);
        stylist.editStylist(stylistMypageEditDto);
        return "정상적으로 입력되었습니다.";
    }

    private static Career createCareer(CareerDto careerDto, Stylist stylist) {
        return Career.builder()
                .organizationName(careerDto.getOrganizationName())
                .content(careerDto.getContent())
                .startYear(careerDto.getStartYear())
                .endYear(careerDto.getEndYear())
                .stylist(stylist)
                .build();
    }

    public StylistServiceResponseDto getServiceList(String userId) {
        Stylist stylist = stylistRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<StylistService> serviceList = serviceRepository.findAllByStylist_id(stylist.getId());

        List<StylistServiceDto> stylistServiceDtoList = serviceList.stream()
                .map(StylistServiceDto::toDto)
                .collect(Collectors.toList());

        return new StylistServiceResponseDto(stylistServiceDtoList);
    }

    public String addService(String userId, StylistServiceDto stylistServiceDto) {
        Stylist stylist = stylistRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if (serviceRepository.countByStylist_id(stylist.getId()) >= 5) throw new CustomException(TOO_MANY_SERVICES);

        List<ServiceCategoryDto> serviceCategoryDtoList = stylistServiceDto.getServiceCategoryDtoList();
        StylistService stylistService = StylistService.builder()
                .serviceName(stylistServiceDto.getServiceName())
                .serviceDescription(stylistServiceDto.getServiceDescription())
                .price(stylistServiceDto.getPrice())
                .stylist(stylist)
                .build();
        serviceRepository.save(stylistService);
        for (ServiceCategoryDto serviceCategoryDto : serviceCategoryDtoList) {
            addServiceCategory(serviceCategoryDto, stylistService);
        }
        return "서비스가 등록되었습니다.";
    }

    private void addServiceCategory(ServiceCategoryDto serviceCategoryDto, StylistService stylistService) {
        ServiceCategory serviceCategory = ServiceCategory.builder()
                .serviceType(serviceCategoryDto.getServiceType())
                .connectionType(serviceCategoryDto.getConnectionType())
                .categoryDescription(serviceCategoryDto.getCategoryDescription())
                .stylistService(stylistService)
                .build();
        serviceCategoryRepository.save(serviceCategory);
    }

    public StylistServiceDto getService(String userId, Long service_id) {
        Stylist stylist = stylistRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Optional<StylistService> service = serviceRepository.findById(service_id);
        if (service.isEmpty()) {
            throw new CustomException(SERVICE_NOT_FOUND);
        }
        if (service.get().getStylist() != stylist) {
            throw new CustomException(STYLIST_NOT_MATCH_SERVICE);
        }
        return StylistServiceDto.toDto(service.get());
    }
}