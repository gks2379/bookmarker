# Bookmarker 📑

북마크를 태그로 분류하여 관리하는 풀스택 웹 애플리케이션입니다.

## 기술 스택

| 구분 | 기술 |
|------|------|
| **Backend** | Spring Boot 4.0, JPA, H2 |
| **Frontend** | React + Vite |
| **API 문서** | Swagger (springdoc-openapi) |

## 실행 방법

### 백엔드
```bash
./gradlew bootRun
```
- API: http://localhost:8082
- Swagger: http://localhost:8082/swagger-ui.html
- H2 Console: http://localhost:8082/h2-console

### 프론트엔드
```bash
cd frontend
npm install
npm run dev
```
- 접속: http://localhost:3000

## API 엔드포인트

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/bookmarks` | 북마크 목록 조회 |
| POST | `/api/bookmarks` | 북마크 생성 |
| GET | `/api/bookmarks/{id}` | 북마크 단건 조회 |
| PUT | `/api/bookmarks/{id}` | 북마크 수정 |
| DELETE | `/api/bookmarks/{id}` | 북마크 삭제 |
| GET | `/api/bookmarks/search?keyword=` | 검색 |
| GET | `/api/bookmarks/tag/{tagName}` | 태그별 조회 |
| GET | `/api/tags` | 전체 태그 조회 |

## 프로젝트 구조

```
├── src/main/java/com/bookmark/api/
│   ├── controller/    # REST API
│   ├── service/       # 비즈니스 로직
│   ├── repository/    # 데이터 접근
│   ├── entity/        # JPA 엔티티
│   ├── dto/           # 요청/응답 객체
│   └── config/        # 설정
└── frontend/          # React 앱
```
