package com.apptive.marico.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //400 BAD_REQUEST 잘못된 요청
    PASSWORD_NOT_MATCH(400, "잘못된 비밀번호입니다."),
    INVALID_ID(400, "아이디는 영문 소문자, 숫자 포함 6~20자 이내이어야 합니다."),
    INVALID_PASSWORD(400, "비밀번호는 영문, 숫자, 특수문자 포함 8~20자 이내이어야 합니다."),
    INVALID_NICKNAME(400, "닉네임은 한글, 영문, 숫자 중 2~10자 이내이어야 합니다."),


    VERIFICATION_CODE_INVAILD(401, "발급 코드가 유효하지 않습니다."),
    EMAIL_DOES_NOT_MATCH(401, "이메일이 일치하지 않습니다."),

    //403 Forbidden 요청이 이해되었지만 서버가 요청을 거부했음
    TOO_MANY_SERVICES(403, "등록할 수 있는 서비스 갯수를 초과했습니다."),

    //404 NOT_FOUND 잘못된 리소스 접근
    EMAIL_NOT_FOUND(404, "존재하지 않는 이메일입니다."),
    USER_NOT_FOUND(404, "존재하지 않는 회원입니다."),
    STYLIST_NOT_FOUND(404, "존재하지 않는 스타일리스트입니다."),
    MEMBER_NOT_FOUND(404, "존재하지 않는 회원입니다."),
    STYLIST_NOT_MATCH_SERVICE(404, "자신이 등록한 서비스가 아닙니다."),
    SERVICE_NOT_FOUND(404, "존재하지 않는 서비스입니다."),

    //409 CONFLICT 중복된 리소스
    ALREADY_SAVED_EMAIL(409, "이미 저장된 이메일입니다."),
    ALREADY_SAVED_ID(409, "이미 저장된 아이디입니다."),
    ALREADY_SAVED_NICKNAME(409, "이미 저장된 닉네임입니다."),

    //419 Authentication Timeout
    VERIFICATION_CODE_TIMEOUT(419, "발급 코드의 인증 시간이 초과 되었습니다."),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!"),
    ROLE_NOT_FOUND(500, "존재하지 않는 ROLE입니다.");

    private final int status;
    private final String message;
}
