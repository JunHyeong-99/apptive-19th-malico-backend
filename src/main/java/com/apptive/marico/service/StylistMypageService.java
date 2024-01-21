package com.apptive.marico.service;
import com.apptive.marico.dto.AccountDto;
import com.apptive.marico.dto.CareerDto;
import com.apptive.marico.dto.stylist.*;
import com.apptive.marico.dto.stylist.service.ServiceCategoryDto;
import com.apptive.marico.dto.stylist.service.StylistServiceDto;
import com.apptive.marico.dto.stylist.service.StylistServiceResponseDto;
import com.apptive.marico.entity.*;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.*;
import com.apptive.marico.service.auth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
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
    private final StyleRepository styleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final ImageUploadService imageUploadService;
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

    public String editInformation(String userId, MultipartFile profileImage, StylistMypageEditDto stylistMypageEditDto) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        // 기존 career 삭제 후 다음 career 등록
        careerRepository.deleteByStylist(stylist);
        //연관관계 주인을 통한 새로운 career 저장
        List<CareerDto> careerDtoList = stylistMypageEditDto.getCareerDtoList();
        careerDtoList.stream()
                .map(careerDto -> createCareer(careerDto, stylist))
                .forEach(careerRepository::save);
        String image = imageUploadService.upload(profileImage);
        stylistMypageEditDto.setProfile_image(image);
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

        List<StylistService> serviceList = serviceRepository.findAllByStylistId(stylist.getId());

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
        Optional<StylistService> service = serviceRepository.findServiceWithStylistById(service_id);
        if (service.isEmpty()) {
            throw new CustomException(SERVICE_NOT_FOUND);
        }
        if (service.get().getStylist() != stylist) {
            throw new CustomException(STYLIST_NOT_MATCH_SERVICE);
        }
        return StylistServiceDto.toDto(service.get());
    }

    public String editService(String userId, Long serviceId, StylistServiceDto stylistServiceDto) {
        Optional<Stylist> stylist = stylistRepository.findByUserId(userId);
        if (stylist.isEmpty()) throw new CustomException(USER_NOT_FOUND);

        Optional<StylistService> stylistService = serviceRepository.findServiceWithStylistById(serviceId);
        if (stylistService.isEmpty()) throw new CustomException(SERVICE_NOT_FOUND);
        if (!Objects.equals(stylistService.get().getStylist(), stylist.get())) throw new CustomException(STYLIST_NOT_MATCH_SERVICE);

        serviceCategoryRepository.deleteAllByStylistService(stylistService.get());
        List<ServiceCategoryDto> serviceCategoryDtoList = stylistServiceDto.getServiceCategoryDtoList();

        serviceCategoryDtoList.stream()
                .map(categoryDto-> createServiceCategory(categoryDto, stylistService.get()))
                .forEach(serviceCategoryRepository::save);
        stylistService.get().editService(stylistServiceDto);
        return "서비스가 수정되었습니다.";
    }

    private static ServiceCategory createServiceCategory(ServiceCategoryDto categoryDto, StylistService stylistService) {
        return ServiceCategory.builder()
                .serviceType(categoryDto.getServiceType())
                .connectionType(categoryDto.getConnectionType())
                .categoryDescription(categoryDto.getCategoryDescription())
                .stylistService(stylistService)
                .build();
    }

    public StyleDto.DtoList getStyle(String userId) {
        Optional<Stylist> stylist = stylistRepository.findByUserIdWithStyle(userId);
        if(stylist.isEmpty()) throw new CustomException(USER_NOT_FOUND);
        List<Style> styleList = stylist.get().getStyles();
        List<StyleDto> styleDtoList = styleList.stream().map(StyleDto::toDto).collect(Collectors.toList());
        return StyleDto.DtoList.builder().styleDtoList(styleDtoList).build();
    }

    public String addStyle(String userId, StyleDto styleDto) {
        Optional<Stylist> stylist = stylistRepository.findByUserId(userId);
        if (stylist.isEmpty()) throw new CustomException(USER_NOT_FOUND);
        styleRepository.save(Style.builder()
                .image(styleDto.getImage())
                .category(styleDto.getCategory())
                .stylist(stylist.get())
                .build());
        return "스타일이 등록되었습니다.";
    }

    public String deleteStyle(String userId, DeleteStyleDto deleteStyleDto) {
        Optional<Stylist> stylist = stylistRepository.findByUserIdWithStyle(userId);
        if (stylist.isEmpty()) throw new CustomException(USER_NOT_FOUND);

        List<Style> style = stylist.get().getStyles();
        if (style.isEmpty()) throw new CustomException(STYLE_NOT_FOUND);
        Long[] styleIdList = extractStyleIds(style);

        for (Long deleteStyleId : deleteStyleDto.getDeleteStyleIdList()) {
            if (containsValue(styleIdList, deleteStyleId)) {
                styleRepository.deleteById(deleteStyleId);
            }
            else throw new CustomException(STYLE_NOT_FOUND);
        }

        return "STYLE이 삭제 되었습니다.";
    }

    private static Long[] extractStyleIds(List<Style> styleList) {
        Long[] idArray = new Long[styleList.size()];

        for (int i = 0; i < styleList.size(); i++) {
            idArray[i] = styleList.get(i).getId();
        }

        return idArray;
    }
    private static boolean containsValue(Long[] array, long targetValue) {
        for (long element : array) {
            if (element == targetValue) {
                return true;
            }
        }
        return false;
    }

    public String CheckCurrentPassword(String userId, String currentPassword) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        if(!passwordEncoder.matches(currentPassword, stylist.getPassword())){
            throw new CustomException(PASSWORD_NOT_MATCH);
        }
        return "비밀번호가 일치합니다.";
    }

    public String changePassword(String userId, String newPassword) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        customUserDetailsService.checkPasswordAvailability(newPassword);

        stylist.setPassword(passwordEncoder.encode(newPassword));
        stylistRepository.save(stylist);

        return "비밀번호가 변경되었습니다.";
    }


    public String changeEmail(String userId, String newEmail) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        stylist.changeEmail(newEmail);
        stylistRepository.save(stylist);
        return "이메일이 정상적으로 변경되었습니다.";
    }
    public String deleteStylist(String userId) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        stylistRepository.delete(stylist);

        return "회원 탈퇴가 정상적으로 완료되었습니다.";
    }

    public AccountDto loadAccount(String userId) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        return AccountDto.builder()
                .bank(stylist.getBank())
                .accountHolder(stylist.getAccountHolder())
                .accountNumber(stylist.getAccountNumber())
                .build();
    }

    public String addAccount(String userId, AccountDto accountDto) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        stylist.setAccount(accountDto);
        stylistRepository.save(stylist);
        return "계좌정보가 정상적으로 등록되었습니다.";
    }
}