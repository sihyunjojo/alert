# 상추:창고 Alert 서버

# FCM 준비사항
1. Firebase 프로젝트 생성:

- Firebase 콘솔(https://console.firebase.google.com/u/0/)에 접속합니다.
- "프로젝트 추가"를 클릭하여 새 프로젝트를 생성합니다.


2. 서비스 계정 키 파일:
- Firebase 콘솔에서 생성한 프로젝트로 이동합니다.
- 프로젝트 개요 옆의 "프로젝트 설정" > "서비스 계정" 탭으로 이동합니다.
- "새 비공개 키 생성" 버튼을 클릭하여 JSON 형식의 키 파일을 다운로드합니다.
- 이 파일을 프로젝트의 src/main/resources 디렉토리에 firebase-service-account.json이라는 이름으로 저장합니다.
