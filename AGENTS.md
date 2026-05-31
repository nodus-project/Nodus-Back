# 실행 규칙

    - 실행 전 지침 확인 완료를 출력한다.

# 파일 생성 규칙

    - 모든 파일 및 결과물은 반드시 UTF-8 인코딩으로 저장할 것
    - 출력 시 한글 인코딩이 깨지지 않도록 유의할 것

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