# MVC, MVP, MVVP

# MVC (Model-View-Controller)
* Model, View, Controller 각각에 대한 역할을 구분하여 독립적인 기능을 수행하도록 설계

![MVC](https://github.com/KimBoWoon/Android-Practice/blob/master/MVP/MVC.png)

## Model
* 데이터를 가진 객체
* 데이터에 접근하는 DAO 역할
* 라이브러리 세트가 될 수 있음

## View
* 사용자와 상호작용을 하는 UI Layer
* Controller에서 무슨 작업이 진행되는지 모르고 결과만 업데이트 됨
* Model에 변경된 데이터를 사용해 업데이트 진행

## Controller
* 사용자에게 입력을 받아 해당하는 Model을 선택해 입력을 처리하고 결과애 따라 View를 선택하여 업데이트
* Controller이 직접 View에 개입하지 않음
* 사용자의 모든 입력을 받아서 처리
* 하나의 Controller는 여러개의 View를 가질 수 있음

## MVC 패턴을 사용할 경우
* View와 Model간의 의존성이 낮으면 낮을 수록 좋은 구성이며 View와 Model간 의존성을 완전히 없앨 수 없다는 한계를 가진다

# MVP (Model-View-Presenter)
* MVC에서 파생된 것으로 Model과 View간의 의존성 문제를 해결하기 위해 설계

![MVP](https://github.com/KimBoWoon/Android-Practice/blob/master/MVP/MVP.png)

## Model
* 데이터를 가진 객체
* 연산 및 비지니스 로직을 처리
* Model과 View의 의존성이 사라짐

## View
* 사용자와 상호작용을 하는 UI Layer
* 사용자의 입력을 받음

## Presenter
* Model과 View를 연결
* Model에서 처리한 결과를 받아서 직접 View를 업데이트
* Presenter와 View는 1:1 관계를 가지고 있음
* Presenter과 View간의 의존성이 큼
* Presenter에 있는 메소드만 Unit Testing를 진행하면 된다

## MVP Pattern을 사용하면
* Unit Test 용이
* View와 Controller의 문제점을 해결 한거 같지만 View와 Presenter의 의존성이 있다
* MVC에 비해 필요한 Class의 수가 증가

# MVVM (Model-View-ViewModel)
* MVC에서 파생
* Model과 View 사이의 의존성 뿐 만 아니라 View와 Controller간의 의존성도 고려함
* 각 Layer가 완전히 독립적으로 작성되고 테스트 될 수 있도록 설계된 Architecture Pattern

![MVVM](https://github.com/KimBoWoon/Android-Practice/blob/master/MVP/MVVM.png)

## Model
* 데이터를 가진 객체
* Model이 변경되면 해당하는 ViewModel을 사용하는 View가 자동으로 업데이트

## View
* 사용자와 상호작용을 하는 UI Layer
* 사용자의 입력을 받아 ViewModel에 전달
* View는 자신이 사용할 ViewModel를 선택할 수 있음

## ViewModel
* View를 나타내주기 위한 Model이자 Presentation Logic을 처리하는 역할
* Model를 기준으로 Presentation Logic에 따라 서로 다르게 구현

## MVVM Pattern을 사용하면
* View가 Model이나 ViewModel과 의존성 없이 독립적
* 반복되는 이벤트 핸들러와 비즈니스 로직을 캡슐화하여 관리할 수 있어서 재사용성 뛰어남
* ViewModel 설계하는 것이 쉽지 않음
* View에 대한 처리가 복잡해질수록 ViewModel이 거대해져 오버스펙이 될 수 있음
* 플랫폼에 제한적