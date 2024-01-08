package com.apptive.marico.service;

import com.apptive.marico.dto.NoticeDto;
import com.apptive.marico.dto.NoticeResponseDtoList;
import com.apptive.marico.entity.*;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.NoticeReadStatusRepository;
import com.apptive.marico.repository.NoticeRepository;
import com.apptive.marico.repository.StylistRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.apptive.marico.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    
    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeReadStatusRepository noticeReadStatusRepository;
    private final ModelMapper modelMapper;
    private final StylistRepository stylistRepository;

    public NoticeResponseDtoList loadNoticeList(String userId) {
        List<Notice> noticeList = noticeRepository.findAll();
        List<NoticeDto> noticeDtoList = noticeList.stream()
                .map(NoticeDto::toDto)
                .toList();
        Optional<Member> member = memberRepository.findByUserIdWithNoticeReadStatus(userId);
        Optional<Stylist> stylist = stylistRepository.findByUserIdWithNoticeReadStatus(userId);

        List<NoticeReadStatus> noticeReadStatusList = member.map(Member::getNoticeReadStatuses)
                .orElseGet(() -> stylist.map(Stylist::getNoticeReadStatuses).orElse(null));
        List<Long> readList = null;
        if (noticeReadStatusList != null) {
            readList = noticeReadStatusList.stream().map(noticeReadStatus -> noticeReadStatus.getNotice().getId()).toList();
        }

        return NoticeResponseDtoList.builder().noticeDtoList(noticeDtoList).readNotice(readList).build();
    }

    public NoticeDto loadNotice(String userId, Long notice_id) {
        Optional<Notice> notice = noticeRepository.findById(notice_id);
        if (notice.isEmpty()) throw new CustomException(NOTICE_NOT_FOUND);
        Optional<Member> member = memberRepository.findByUserIdWithNoticeReadStatus(userId);
        Optional<Stylist> stylist = stylistRepository.findByUserIdWithNoticeReadStatus(userId);
        NoticeReadStatus noticeReadStatus = member.filter(m ->
                        isNoticeUnread(m.getNoticeReadStatuses(), notice.get().getId()))
                .map(m ->
                        NoticeReadStatus.builder().notice(notice.get()).member(m).readStatus(true).build())
                .orElseGet(() ->
                        stylist.filter(s ->
                                        isNoticeUnread(s.getNoticeReadStatuses(), notice.get().getId()))
                                .map(s ->
                                        NoticeReadStatus.builder().notice(notice.get()).stylist(s).readStatus(true).build())
                                .orElse(null));
        if (noticeReadStatus != null) noticeReadStatusRepository.save(noticeReadStatus);
        return NoticeDto.toDto(notice.get());
    }

    private boolean isNoticeUnread(List<NoticeReadStatus> noticeReadStatuses, Long noticeId) {
        if(noticeReadStatuses.isEmpty()) return true;
        return noticeReadStatuses.stream()
                .noneMatch(status -> Objects.equals(status.getNotice().getId(), noticeId));
    }


    public String addNotice(Principal principal, NoticeDto noticeDto) {
        if (!hasAdminRole(principal)) throw new CustomException(USER_NOT_ADMIN);
        Optional<Member> member = memberRepository.findByUserId(principal.getName());
        if (member.isEmpty()) throw new CustomException(USER_NOT_FOUND);
        Notice notice = createNotice(noticeDto);
        noticeRepository.save(notice);
        return "공지사항이 등록되었습니다.";
    }

    private Notice createNotice(NoticeDto noticeDto) {
        return Notice.builder()
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .build();
    }

    private boolean hasAdminRole(Principal principal) {
        Optional<Member> member = memberRepository.findByUserId(principal.getName());
        if (member.isEmpty()) throw new CustomException(USER_NOT_ADMIN);
        Collection<? extends GrantedAuthority> authorities = member.get().getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

    }

    public String deleteNotice(Principal principal, Long notice_id) {
        if(!hasAdminRole(principal)) throw new CustomException(USER_NOT_ADMIN);
        Optional<Member> member = memberRepository.findByUserId(principal.getName());
        if(member.isEmpty()) throw new CustomException(USER_NOT_FOUND);
        noticeReadStatusRepository.deleteByNoticeId(notice_id);
        noticeRepository.deleteById(notice_id);
        return "공지사항이 정상적으로 삭제 되었습니다.";
    }
}
