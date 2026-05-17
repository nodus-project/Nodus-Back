package net.nodus.global.error

enum class ErrorCode(val code: Int) {

    SUCCESS(0), // 성공

    // 1000: 사용자 입력 오류 (User Input) - 클라이언트의 요청 자체가 잘못된 경우
    INVALID_INPUT(1000),         // 요청 값이 비즈니스 규칙에 맞지 않음 (예: 유효성 검사 실패)
    MISSING_PARAMETER(1001),     // 필수 파라미터나 Body 값이 누락됨
    INVALID_TYPE(1002),          // 파라미터 타입이 맞지 않음 (예: 숫자에 문자 입력)
    UNAUTHORIZED(1003),          // 인증되지 않은 사용자 (로그인 필요)
    FORBIDDEN(1004),             // 권한이 없는 리소스에 접근 시도

    // 2000: 데이터 관련 오류 (Data) - DB 조회나 데이터 상태 문제
    NOT_FOUND(2000),             // 요청한 리소스를 찾을 수 없음 (404 대응)
    DUPLICATED(2001),            // 이미 존재하는 리소스 (중복 등록 시도)
    DATABASE_ERROR(2002),        // DB 처리 중 예외 발생 (SQL 오류 등)
    DATA_EXPIRED(2003),          // 토큰이나 인증 코드 등 기간이 만료된 데이터
    DATA_INTEGRITY_VIOLATION(2004), // 데이터 제약 조건 위반 (외래키 참조 오류 등)

    // 9000: 서버 내부 오류 (Server) - 서버의 로직이나 환경 문제
    INTERNAL_SERVER_ERROR(9000), // 서버 내부 로직 중 알 수 없는 예외 발생
    EXTERNAL_API_FAILED(9001),   // 구글 OAuth 등 외부 서비스 호출 실패
    NOT_IMPLEMENTED(9002),       // 아직 구현되지 않은 엔드포인트나 기능 호출
    SERVICE_UNAVAILABLE(9003);   // 서버 과부하 또는 점검 중
}