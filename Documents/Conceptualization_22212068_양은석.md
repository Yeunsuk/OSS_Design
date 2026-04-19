# 1. Conceptualization

- Student ID: 22212068  
- Name: 양은석  
- Email: yes6815@yu.ac.kr
- Project Name: Automated Reservation System
- Project Link: https://github.com/Yeunsuk/OSS_Design/tree/main/Documents

---

## Revision History

| Date | Version | Description | Author |
| --- | --- | --- | --- |
| 03/23/2026 | 1.00 | 초안 | 양은석 |
| 04/14/2026 | 1.01 | 주제변경 | 양은석 |
| 04/19/2026 | 1.02 | 주제변경2 | 양은석 |

---

## 📑 Contents

- [1. Business Purpose](#business-purpose)
- [2. System Context Diagram](#system-context-diagram)
- [3. Use Case List](#use-case-list)
- [4. Concept of Operation](#concept-of-operation)
- [5. Problem Statement](#problem-statement)
- [6. Glossary](#glossary)
- [7. References](#references)
  
## <a id="business-purpose"></a>1. Business Purpose

### 1) Project Background

현대인들에게 주거지 선택은 단순한 ‘거주 공간 확보’를 넘어, 삶의 질과 직결되는 매우 중요한 의사결정 요소로 자리 잡고 있다. 직장과의 거리, 교육 환경, 생활 편의성뿐만 아니라 환경, 치안, 문화 인프라 등 다양한 요소가 복합적으로 작용하며, 이러한 요소들은 장기적인 생활 만족도에 큰 영향을 미친다. 그러나 현실적으로 사용자가 이러한 정보를 종합적으로 수집하고 비교하는 과정은 쉽지 않으며, 제한된 정보에 의존한 채 의사결정을 내리는 경우가 많다.

특히 기존의 부동산 정보 서비스는 주로 ‘가격’, ‘면적’, ‘거래 이력’과 같은 정량적인 부동산 데이터에 집중되어 있어, 실제 거주 환경을 판단하는 데 필요한 다양한 요소를 충분히 반영하지 못한다. 이로 인해 사용자는 단순한 수치 정보와 실제 체감 환경 사이의 괴리를 경험하게 되며, 이는 잘못된 선택으로 이어질 가능성을 높인다. 이러한 배경 속에서 사용자는 다음과 같은 정보 비대칭 문제에 직면해 있다.

- **파편화된 데이터 문제**: 미세먼지와 같은 환경 정보, 범죄율과 같은 치안 정보, 병원·마트·대중교통 등 생활 인프라 정보가 각각 서로 다른 공공기관 및 플랫폼에 분산되어 있다. 사용자는 여러 사이트를 반복적으로 방문하며 정보를 수집해야 하며, 이 과정에서 시간과 노력이 과도하게 소모된다.
- **객관적 지표의 부재**: 현재 많은 정보가 ‘살기 좋은 동네’, ‘인기 지역’과 같은 주관적인 평가에 의존하고 있다. 그러나 개인의 기준에 따라 이러한 평가는 크게 달라질 수 있으며, 이를 객관적으로 비교할 수 있는 표준화된 지표가 부족한 실정이다.
- **실시간성 결여**: 공공 데이터는 지속적으로 갱신되고 있음에도 불구하고, 이를 통합적으로 반영하여 사용자에게 최신 상태로 제공하는 서비스는 제한적이다. 예를 들어, 특정 지역의 환경 지표나 범죄 발생 현황이 변화하더라도, 사용자는 이를 즉각적으로 파악하기 어렵다.
- **개인 맞춤 정보 부족**: 사용자마다 중요하게 생각하는 요소는 다르다. 어떤 사용자는 치안을, 또 다른 사용자는 교통 접근성을 더 중요하게 여길 수 있다. 하지만 기존 서비스는 이러한 개인별 우선순위를 반영하지 못하고 획일적인 정보를 제공하는 경우가 많다.

이러한 문제를 해결하기 위해서는 다양한 출처의 데이터를 통합하고, 이를 사용자 관점에서 재해석하여 제공하는 새로운 접근 방식이 필요하다. 따라서 본 프로젝트에서는 환경, 치안, 생활 인프라 등 여러 공공 API 데이터를 수집·정제·분석하여 하나의 통합된 지표로 가공하고, 이를 기반으로 사용자 맞춤형 ‘주거 점수’를 산출하는 시스템을 설계한다.

해당 시스템은 단순한 정보 제공을 넘어, 사용자가 자신의 선호도에 따라 가중치를 조정하고 지역 간 비교를 직관적으로 수행할 수 있도록 지원한다. 또한 실시간 데이터 반영을 통해 변화하는 지역 환경을 지속적으로 업데이트하며, 사용자에게 보다 신뢰성 있고 시의성 있는 정보를 제공하는 것을 목표로 한다. 이를 통해 사용자는 복잡한 정보 탐색 과정 없이도 합리적이고 효율적인 주거지 선택을 할 수 있을 것으로 기대된다.

---

### 2) Goal

- 데이터 통합 및 지표화
- 실시간 자동 업데이트
- 데이터 시각화
- 개인화 필터링

---

### 3) Target Market

- 신규 거주지 탐색자
- 부동산 투자 및 분석가
- 데이터 기반 의사결정 선호자

---

## <a id="system-context-diagram"></a>2. System Context Diagram
<img width="682" height="142" alt="Conceptualization_Diagram" src="https://github.com/user-attachments/assets/32b6f8a8-836b-4b49-bcc3-8a1e6b71160c" />


### Description

- Search Region : 지역 검색
- Set Priority : 우선순위 설정
- Interface : 인터페이스
- Data Crawling : 데이터 크롤링
- Data Save : 데이터 저장
- Data Update : 데이터 갱신
- Calculation : 계산
- Data Processing : 데이터 가공
---

## <a id="use-case-list"></a>3. Use Case List


### 1) Search Region

| Actor | Description |
| --- | --- |
| System | 지역을 검색한다 |

### 2) Set Priority

| Actor | Description |
| --- | --- |
| System | 우선순위를 설정한다 |

### 3) Interface

| Actor | Description |
| --- | --- |
| System | 화면을 제공한다 |

### 4) Data Crawling

| Actor | Description |
| --- | --- |
| System | 데이터를 갖고온다 |

### 5) Data Save

| Actor | Description |
| --- | --- |
| System | 데이터를 저장한다 |

### 6) Data Update

| Actor | Description |
| --- | --- |
| User | 데이터를 갱신한다 |

### 7) Calculation

| Actor | Description |
| --- | --- |
| User | 데이터를 기반으로 계산한다 |

### 8) Data Processing
| Actor | Description |
| --- | --- |
| User | 데이터를 가공한다 |

---

## <a id="concept-of-operation"></a>4. Concept of Operation


### 1) Search Region

| Item | Description |
| --- | --- |
| Purpose | 지역을 검색한다 |
| Approach | 지역을 입력받고 해당 지역을 표시한다. |
| Dynamics | 조건에 일치하는 지역을 검색하는 경우 |
| Goals | 지역 검색 기능을 구현한다. |

### 2) Set Priority

| Item | Description |
| --- | --- |
| Purpose | 우선순위를 설정한다 |
| Approach | 우선순위를 설정한다. 사람마다 중점으로 보는 것이 다르기에 기준에 맞는 자료를 제공한다. |
| Dynamics | 우선순위를 설정하는 경우 |
| Goals | 우선순위 설정 기능을 구현한다. |

### 3) Interface

| Item | Description |
| --- | --- |
| Purpose | 화면을 제공한다 |
| Approach | 화면을 제공한다. 해당 시스템 가동을 가볍게 하기 위해 기능을 상시 제공이 아니라 on/off형식으로 제공한다. |
| Dynamics | 시스템을 이용하려는 경우 |
| Goals | 화면 출력 기능을 구현한다. |

### 4) Data Crawling

| Item | Description |
| --- | --- |
| Purpose | 데이터를 갖고온다 |
| Approach | 데이터를 갖고온다. 자동으로 데이터가 갱신될 때마다 가져올 수 있도록 설계한다. |
| Dynamics | 데이터를 갖고 오는 경우 |
| Goals | 데이터 크롤링 기능을 구현한다. |

### 5) Data Save

| Item | Description |
| --- | --- |
| Purpose | 데이터를 저장한다 |
| Approach | 데이터를 저장한다. 크롤링한 정보를 저장하고 데이터를 가공한 정보를 저장한다. |
| Dynamics | 데이터를 저장하는 경우 |
| Goals | 데이터 저장 기능을 구현한다 |

### 6) Data Update

| Item | Description |
| --- | --- |
| Purpose | 데이터를 갱신한다 |
| Approach | 데이터를 갱신한다. 기존의 정보를 갱신하여 최신 정보를 반영한다. |
| Dynamics | 데이터를 갱신하는 경우 |
| Goals | 데이터 갱신 기능을 구현한다. |

### 7) Calculation

| Item | Description |
| --- | --- |
| Purpose | 데이터를 기반으로 계산한다 |
| Approach | 데이터를 기반으로 계산한다. 우선순위에 기반하여 계산을 미리 해둔값을 저장한다. |
| Dynamics | 데이터를 계산하는 경우 |
| Goals | 데이터 계산 기능을 구현한다. |

### 8) Data Processing

| Item | Description |
| --- | --- |
| Purpose | 데이터를 가공한다 |
| Approach | 데이터를 가공한다. 각 데이터마다 저장된 형식이 다르기에 전처리를 하고 일관된 형식으로 저장한다. |
| Dynamics | 데이터를 가공하는 경우 |
| Goals | 데이터 가공 기능을 구현한다. |



## <a id="problem-statement"></a>5. Problem Statement
주거 환경 데이터를 다룰 때 발생할 수 있는 현실적인 문제들을 설계 단계에서 정의합니다.

### **Problem #1: Data Heterogeneity**

공공데이터포털, 지자체 API마다 데이터 포맷과 업데이트 주기가 제각각이다.
→데이터 소스별로 독립적인 설계를 통해 통합 데이터 모델을 구축한다.

---

### **Problem #2: Standard**

각 공공데이터 지표의 기준이 제각각이다. 적절하게 환산시킬 방법을 강구한다.

---

### Problem #3: Technical Difficulty

DB를 자동으로 업데이트하고 데이터를 갱신하는 방법을 공부한다.

---

### **Problem #4: Visualization**

전국 단위의 데이터를 한 번에 지도에 표시할 경우 브라우저 부하가 발생한다.
→지역별 계층적 데이터 구조를 설계하여 사용자가 선택한 지역의 데이터만 로드하도록 최적화한다.

---

### NFRs (Non-Functional Requirements)

- 새로운 공공 데이터 소스가 추가되더라도 기존 시각화 로직을 수정하지 않고 데이터 파이프라인만 추가할 수 있는 구조를 유지한다.
- 공공 API 서버 장애로 인해 데이터 수집에 실패할 경우, 직전 단계의 최신 데이터를 유지하여 서비스 중단을 방지한다.
- 데이터 수집 코드와 시각화 코드를 완전히 분리하여 독립적인 수정 및 배포가 가능해야 한다.
- 사용자가 웹 페이지에 접속했을 때 초기 로딩 속도를 줄이기 위해 가공된 데이터 파일의 크기를 최소화한다.
- 복잡한 통계 수치를 직관적인 색상과 아이콘으로 표현하여 일반 사용자도 주거 환경 수준을 쉽게 이해할 수 있도록 설계한다.
  
---

## <a id="glossary"></a>6. Glossary

| Term | Description |
| --- | --- |
| 데이터 | 사용자가 예약/구매를 원하는 플랫폼 |
| 크롤링 | 기차표나 영화표 같은 사용자가 원하는 목표 |
| 가공 | 사용자의 목표를 위해 수행하는 작업, 최종적으로 예약 혹은 확인을 뜻함 |
| 인터페이스 | 사용자의 작업을 대신하기 위함 |

---

## <a id="references"></a>7. References

- [Selenium](https://www.selenium.dev/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Kakao map](https://apis.map.kakao.com/)
