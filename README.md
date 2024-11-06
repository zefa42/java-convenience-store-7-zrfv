# java-convenience-store-precourse
## 프로그램 작동 방식

- [x] **환영 인사 메세지를 출력한다.**
- [ ] **상품명, 가격, 프로모션 이름, 재고를 출력한다.**
  - [ ] resources 에서 상품 목록과 행사 목록을 불러온다.
  - [ ] 불러온 목록을 형식에 맞게 출력한다.
- [x] **구매할 상품과 수량을 입력받는다.**
  - [x] 공백인 경우 예외를 발생시킨다.
  - [ ] 구매할 상품과 수량 형식이 올바르지 않으면 예외를 발생시킨다.
  - [ ] 존재하지 않는 상품을 입력하면 예외를 발생시킨다.
  - [ ] 구매 수량이 재고 수량을 초과하면 예외를 발생시킨다.
  - [ ] 기타 잘못된 입력되면 예외를 발생시킨다.
- [ ] **프로모션 상품이 n+1 인 경우 n개만 사면, 혜택 안내 메시지를 출력한다.**
  - [ ] 무료 상품 추가 여부를 입력받는다.
    - [ ] 공백인 경우 예외를 발생시킨다.
    - [ ] Y 또는 N 이 아니라면 예외를 발생시킨다.
- [ ] **프로모션 재고가 부족하다면, 정가 결제에 대한 안내 메세지를 출력한다.**
  - [ ] 정가 구매 여부를 입력 받는다.
    - [ ] 공백인 경우 예외를 발생시킨다.
    - [ ] Y 또는 N 이 아니라면 예외를 발생시킨다.
- [ ] **멤버십 할인 여부를 입력 받는다.**
  - [ ] 공백인 경우 예외를 발생시킨다.
  - [ ] Y 또는 N 이 아니라면 예외를 발생시킨다.
- [ ] **계산 결과를 영수증으로 출력한다.**
  - [ ] 금액과 수량을 계산한다
  - [ ] 추가 구매 여부를 입력받는다.
    - [ ] 공백인 경우 예외를 발생시킨다.
    - [ ] Y 입력 시 처음부터 시작, N 입력 시 종료