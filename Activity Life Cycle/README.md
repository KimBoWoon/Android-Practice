# Activity Life Cycle

## Activity Life Cycle 이해해야하는 이유
* 다른 앱들과 전환할 때 충돌하지 않기 위해
* 자원 낭비를 피하기 위해
* 사용자의 진행 상태를 기억하기 위해

![ActivityLifeCycle](https://github.com/KimBoWoon/Android-Practice/Activity Life Cycle/basic-lifecycle.png)

## onCreate()
* Activity가 시작되어 화면을 구성하는 단계
* 미리 XML파일들을 파싱하고 렌더링하는 단계

## onStart()
* 구성한 Activity를 실행하는 단계
* 사용자에게 Activity를 표시함
* onStop()에서 돌아올 때 onCreate()대신 사용

## onResume()
* Activity가 실행되어 사용자와 상호작용 하는 단계
* Activity가 일시정지 되어 돌아올 때 사용됨

## onPause()
* Intent실행이나 Dialog가 나타나 Activity가 가려졌을 때 일시정지 상태가 됨
* Activity가 다시 재개될 때 onResume()를 호출

## onStop()
* 일시정지 상태와는 다르게 Activity가 전혀 보이지 않고 포커스가 다른 Activity에 있음
* 사용자가 현재 사용하는 앱에서 다른 앱을 실행 시켰을 때 현재 앱은 정지가 됨
* Activity가 재개 될 때는 onRestart()를 호출
* 메모리 누수를 야기시킬 수 있기 때문에 리소스를 해제하는것이 중요
* Activity 개체는 메모리에 계속 유지 됨

## onDestroy()
* 시스템이 Activity를 소멸 시킬 때 실행
* 마지막으로 리소스를 해제할 수 있는 기회

## onSaveInstanceState()
* Activity 중지 작업을 시작할 때 호출
* 현재 Activity에 상태 데이터를 저장할 수 있음
* Key-Value로 저장
* onCreate()에 있는 매개변수를 이용하여 상태 데이터를 얻음

## onRestoreInstanceState()
* onCreate()에서 복원할 수 있지만 onStart()에서 호출
* 시스템은 복원할 저장 상태가 있을 경우에만 호출
* Bundle이 Null인지 확인할 필요가 없음