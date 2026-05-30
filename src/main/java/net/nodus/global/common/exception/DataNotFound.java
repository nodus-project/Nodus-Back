package net.nodus.global.common.exception;

import net.nodus.global.error.ErrorCode;

public class DataNotFound extends GlobalException {

    public DataNotFound(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }

    public static final String USER_NOT_FOUND = "유저 정보를 찾을 수 없습니다.";
    public static final String SITE_NOT_FOUND = "사이트 정보를 찾을 수 없습니다.";
}
