# RXJAVA

## Observer Pattern
* 객체를 옵저버에 등록하여 객체의 상태가 변할 때마다 메서드 등을 통해 이벤트를 처리

## Iterator Pattern
* for문 대신 Iterator을 사용해 반복
* for문을 사용하면 자료형에 따라 반복문이 변경될 수 있음
* Iterator는 for문의 단점을 보완함
```
Student s = new Student();

Iterator iter = s.iterator();
while(iter.hasNext()) {
	System.out.println(((Student) iter).getID());
}
```

## 용어 정리
* 이벤트 : 구독자들에게 전달되는 데이터를 의미하며 클릭 이벤트, 상태 이벤트 등이 있음(아이템이라고 부르기도 함)
* 구독 : 구독자가 이벤트를 전달받기 위해 하는 행위
* 관찰 : 구독 보다 넓은 범위의 행위