# 실행 규칙

    - 실행 전 지침 확인 완료를 출력한다.
    - 요청하지 않은 건 수정하지 않는다.

# 파일 생성 규칙

    - 모든 파일 및 결과물은 반드시 UTF-8 인코딩으로 저장할 것
    - 출력 시 한글 인코딩이 깨지지 않도록 유의할 것

# flyway 마이그레이션 작성 규칙

    - Flyway 마이그레이션 SQL에서는 외래 키 제약 조건을 생성하지 않는다.
    - 관계 컬럼과 인덱스는 필요하면 유지하되, 관계 무결성은 데이터베이스 제약 조건이 아니라 애플리케이션 로직에서 보장한다.
    - primary key는 컬럼 레벨 primary key 선언한다.
    - SQL 키워드는 대문자로 작성한다.

# DTO 생성 규칙

    - setter 사용을 지양하며, setter보다 builder 패턴을 사용합니다.
    - 모든 DTO는 class의 record 클래스로 정의한다
    - 응답은 [domain]Request로 요청은 [domain]Response 클래스 안에 세부 클래스를 정의한다
        - [domain]Request.class
            - POST: [domain]PostRequest.class
            - PATCH: [domain]PatchRequest.class
            - PUT: [domain]PutRequest.class

        - [domain]Response.class
            - response 클래스는 entity -> dto 변환을 수행하며 static [domain]Response from([entity] entity)를 통해 dto 변환을 수행한다.
            - response의 하위 클래스는 builder 패턴을 포함한다.
            - GET: [domain]OneRequest.class
            - GET(리스트): List<[domain]OneRequest.class