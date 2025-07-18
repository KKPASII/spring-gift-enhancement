# spring-gift-enhancement

0. 기본 코드 준비

1. 엔티티 매핑

`JdbcClient` 기반 코드를 `JPA`로 리팩터링 (도메인 모델 구성 및 객체와 테이블 매핑)

- 엔티티 클래스와 리포지토리 클래스 작성.
    - 아래의 `DDL(Data Definition Language)` 참고

- 객체의 참조와 테이블의 외래 키를 매핑.
  - 객체에서는 참조 사용
  - 테이블에서는 외래 키 사용

- `@DataJpaTest`를 사용하여 학습 테스트