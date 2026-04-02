# 🚀 2단계 - 칸반 보드 관리(수정)

## 진행 방식

- 미션은 **과제 진행 요구 사항**, **기능 요구 사항**, **프로그래밍 요구 사항** 세 가지로 구성되어 있다.
- 세 개의 요구 사항을 만족하기 위해 노력한다. 특히 기능을 구현하기 전에 기능 목록을 만들고, 기능 단위로 커밋 하는 방식으로 진행한다.
- **기능 요구 사항에 기재되지 않은 내용은 스스로 판단하여 구현한다.**

## 과제 진행 요구 사항

- **기능을 구현하기 전 `README.md`에 구현할 기능 목록을 정리**해 추가한다.
- Git의 커밋 단위는 앞 단계에서 **`README.md`**에 정리한 기능 목록 단위로 추가한다.
    - [AngularJS Git Commit Message Conventions](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)을 참고해 커밋 메시지를 작성한다.
## 디자인 시안

[피그마](https://www.figma.com/design/3aBG3UfkTwmHM8BnPyahtT/8%EA%B8%B0-Android-%EB%A0%88%EB%B2%A81-%EB%AF%B8%EC%85%98-%EB%94%94%EC%9E%90%EC%9D%B8?node-id=23136-23&t=xCxwxtFTp8FpfgFV-1)
## 요구 사항 분석

### 1단계 미션 리팩토링: 
- [x] Screen 단위로 StateHolder를 적용하여 하위 컴포넌트들을 Stateless로 만듭니다. 
- [x] 스낵바 표출 책임을 Board -> Project로 이전합니다.
- [x] 테스트 코드가 통과되도록 리팩토링합니다. 
  - [x] 스낵바 표출 책임이 Project로 이전됨에 따라 테스트 코드도 이전
  - [x] 드래그 앤 드랍테스트를 Board로 이전합니다. 

### Domain

- [x] TaskStatus에 Review Status를 추가한다.
  - [x] 태스크 상태 전이 규칙을 추가한다.
      ```
      다음과 같은 상태 전이만 허용합니다.
      To Do
      └─→ In Progress (작업 시작)

        In Progress
        ├─→ To Do (다시 계획)
        └─→ Review (리뷰 요청)
    
        Review
        ├─→ In Progress (수정 필요)
        └─→ Done (승인 완료)
    
        Done
        └─→ To Do (재작업)

      ``` 
- [x] Status별 생성, 삭제 규칙을 추가한다. 
  - [x] 생성 시, 담당자 미지정 가능: To Do
  - [x] 태스크 삭제 가능: To Do, In Progress
    
### UI
    
- [ ] 다이얼로그에 `삭제` 버튼이 추가된다.
- [ ] 태스크 수정 시 스낵바가 표시된다
- [ ] 태스크 삭제 시 스낵바가 표시된다
- [ ] 불가능한 동작 시 스낵바가 표시된다 불가능한 상황은 다음과 같다
  - [ ] Review or Done 상태에서 태스크를 삭제하려고 할 때 스낵바가 표시된다.
  - [ ] Domain에 정의된 태스크 상태 전이 규칙을 따르지 않는 전이를 시도하면 스낵바가 표시된다.
  - [ ] 담당자를 지정하지 않고 In Progress로 전이할 때 스낵바가 표시된다

## 테스트 시나리오

### Domain

- [x] 보드에서 카드를 삭제할 수 있다
- [x] 불가능한 상태 변경 시 카드의 상태가 변경되지 않는다
  - [x] TransitionRule에 정의된 start, target 조건을 만족하면 태스크는 이동할 수 있다
  - [x] TransitionRule에 정의된 start, target 조건을 만족하지 않으면 태스크는 이동할 수 없다 
- [x] Card의 Status가 To do라면, 담당자가 null인 상태로 생성할 수 있다
- [x] 삭제 가능한 상태(TODO, IN_PROGRESS)의 카드는 보드에서 삭제할 수 있다
- [x] 삭제 불가능한 상태(DONE, REVIEW)의 카드는 삭제를 시도해도 보드에 남아있다
- [ ] Card의 Status가 To do이고, 담당자가 null이라면, 다른 상태로 전이할 수 없다

### UI

- [ ] 삭제 버튼을 눌렀을 때 해당 태스크 카드가 삭제된다
- [ ] 태스크 수정 시 스낵바가 표시된다
- [ ] 태스크 삭제 시 스낵바가 표시된다
- [ ] 불가능한 동작 시 스낵바가 표시된다
