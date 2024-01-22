package com.apptive.marico.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.apptive.marico.dto.AccountDto;
import com.apptive.marico.dto.mypage.member.MemberMypageDto;
import com.apptive.marico.dto.mypage.member.LikedStylistDto;
import com.apptive.marico.dto.mypage.member.LikedStylistListDto;
import com.apptive.marico.dto.mypage.member.MemberMypageEditDto;
import com.apptive.marico.entity.Like;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.LikeRepository;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import com.apptive.marico.service.auth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.apptive.marico.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberMypageService {
    private final MemberRepository memberRepository;
    private final StylistRepository stylistRepository;
    private final LikeRepository likeRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    private final Logger log = LoggerFactory.getLogger(getClass());
    public MemberMypageDto mypage(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        return MemberMypageDto.toDto(member);
    }


    public LikedStylistListDto findLikedStylist(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        List<Like> likedStylists = likeRepository.findAllByMemberId(member.getId());
        List<LikedStylistDto> likedStylistsDto = new ArrayList<>();


        for (Like like : likedStylists) {
            Stylist stylist = stylistRepository.findById(like.getStylist().getId())
                    .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
            likedStylistsDto.add(LikedStylistDto.toDto(stylist));
        }

        return LikedStylistListDto.toDto(likedStylistsDto);
    }

    public MemberMypageEditDto getInformation(String userId) {
        // 닉네임, 성, 이메일
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        return MemberMypageEditDto.toDto(member);
    }

    @Transactional
    public String updateInformation(String userId, MemberMypageEditDto memberMypageEditDto) {
        Member originalMember = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        Member updateMember = originalMember;

        if (memberMypageEditDto.getNickname() != null)
            updateMember.setNickname(memberMypageEditDto.getNickname());
        if (memberMypageEditDto.getGender() != 0) updateMember.setGender(memberMypageEditDto.getGender());
        if (memberMypageEditDto.getBirthDate() != null)
            updateMember.setBirthDate(memberMypageEditDto.getBirthDate());

        Member member = memberRepository.save(updateMember);
        return "개인정보수정이 정상적으로 완료되었습니다.";
    }

    public String CheckCurrentPassword(String userId, String password) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        // matches 메서드는 원본 패스워드와 인코딩된 패스워드를 비교하여 일치 여부를 확인
        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new CustomException(PASSWORD_NOT_MATCH);
        }
        return "비밀번호가 일치합니다.";
    }

    @Transactional
    public String changePassword(String userId, String newPassword) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        customUserDetailsService.checkPasswordAvailability(newPassword);

        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

        return "비밀번호가 변경되었습니다.";
    }

    @Transactional
    public String changeEmail(String userId, String newEmail) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        if(memberRepository.existsByEmail(newEmail))
            throw new CustomException(ALREADY_SAVED_EMAIL);

        member.setEmail(newEmail);
        Member save = memberRepository.save(member);
        System.out.println(save.getEmail());

        return "이메일이 정상적으로 변경되었습니다.";
    }

    @Transactional
    public String deleteMember(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        memberRepository.delete(member);

        return "회원 탈퇴가 정상적으로 완료되었습니다.";
    }

    @Transactional
    public String changeProfileImage(String userId, MultipartFile profileImage) {
        if(profileImage.isEmpty()){
            throw new CustomException(PREFERRED_STYLE_IMAGE_NOT_EXIST);
        }

        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        String mediaName = createFileName(profileImage.getOriginalFilename()); // 각 파일의 이름을 저장

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(profileImage.getSize());
        objectMetadata.setContentType(profileImage.getContentType());

        System.out.println("for each 진입 : " + mediaName);

        try (InputStream inputStream = profileImage.getInputStream()) {
            // S3에 업로드 및 저장
            amazonS3.putObject(new PutObjectRequest(bucket, mediaName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CustomException(FILE_UPLOAD_ERROR);
        }

        // 접근가능한 URL 가져오기
        String imageUrl = amazonS3.getUrl(bucket, mediaName).toString();

        member.setProfileImage(imageUrl);
        memberRepository.save(member);

        return "프로필 이미지가 정상적으로 수정되었습니다.";
    }

    public String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
    public String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
    public AccountDto loadAccount(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        return AccountDto.builder()
                .bank(member.getBank())
                .accountHolder(member.getAccountHolder())
                .accountNumber(member.getAccountNumber())
                .build();
    }

    @Transactional
    public String addAccount(String userId, AccountDto accountDto) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        member.setAccount(accountDto);
        memberRepository.save(member);
        return "계좌정보가 정상적으로 등록되었습니다.";
    }
}
