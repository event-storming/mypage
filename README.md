# mypage

mypage 는 CQRS 모형이며, 그중 READ 부분을 담당한다.  
각종 일어난 이벤트들을 받아서, 자신만의 DB 에 저장을 하여 보여주는 서비스이다.  

oauth 서버와 같은 DB 를 바라보며 user 의 상세 정보를 조회 할 수있다.  


### 유저 정보 조회
http http://localhost:8086/users/1@uengine.org  

### 주문 정보 조회
http http://localhost:8086/mypage/order/1@uengine.org  


### 마일리지 정보 조회
http http://localhost:8086/mypage/mileage/1@uengine.org  