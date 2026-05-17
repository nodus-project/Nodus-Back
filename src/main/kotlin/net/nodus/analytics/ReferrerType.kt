package net.nodus.analytics

enum class ReferrerType {
    DIRECT, // 이전 페이지 없음
    INTERNAL, // 같은 서비스 안에서 이동
    EXTERNAL, // 외부 사이트에서 유입
    UNKNOWN
}