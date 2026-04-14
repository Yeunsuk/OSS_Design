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
<img width="811" height="193" alt="Conceptualization_Diagram" src="https://github.com/user-attachments/assets/5f698c74-3efd-48f2-b4d3-f872f8c714ab" />


### Description

- **Ticket reservation**: 표 예매 
- **Send Notification**: 예약 알람  
- **Login**: 로그인
- **Ticket search**: 티켓 검색
- **Check seat**: 좌석 확인
- **Input Reaservation info**: 예약정보
- **Stop Reservation**: 예약취소
- **View status**: 현황확인
- **Select platform**: 플랫폼선택
---

## <a id="use-case-list"></a>3. Use Case List

### 1) Ticket reservation
| Actor | Description |
|------|------------|
| System | 티켓을 예매한다. |

### 2) Send Notification
| Actor | Description |
|------|------------|
| System | 티켓을 예매하면 결과를 전송한다. |

### 3) Login
| Actor | Description |
|------|------------|
| System | 입력받은 정보로 로그인을 한다. |

### 4) Ticket search
| Actor | Description |
|------|------------|
| System | 입력받은 정보에 부합하는 티켓을 검색한다. |

### 5) Check seat
| Actor | Description |
|------|------------|
| System | 좌석이 있는지 확인하고, 없으면 다시 검색한다. |

### 6) Input Reservation Info
| Actor | Description |
|------|------------|
| User | 예매할 티켓의 정보와 로그인 정보를 입력한다. |

### 7) Stop Reservation
| Actor | Description |
|------|------------|
| User | 티켓 예매를 중지한다. |

### 8) View status
| Actor | Description |
|------|------------|
| User | 현재 예매 현황을 확인한다. |

### 9) Select platform
| Actor | Description |
|------|------------|
| User | 플랫폼을 선택한다. |

---

## <a id="concept-of-operation"></a>4. Concept of Operation


### 1) Ticket reservation
| Item | Description |
|------|------------|
| Purpose | 티켓을 예약한다. |
| Approach | 티켓을 검색하고 좌석이 있는 것까지 확인됐으면 예약한다. 만약 이 과정 도중에 티켓이 팔려버리면 다시 이전 단계로 돌아간다. |
| Dynamics | 검색조건에 일치하는 티켓을 예약하려는 경우 |
| Goals | 티켓 예약 기능을 구현한다. |

### 2) Send Notification
| Item | Description |
|------|------------|
| Purpose | 예약한 티켓에 대한 정보를 사용자에게 전달한다. |
| Approach | 티켓 예약은 일정 시간이 지나면 좌석이 취소되기에 그 시간 안에 사용자가 구매할 수 있도록 정보를 전달한다. 결과적으로 사용자는 프로그램을 실행시켜두고 다른 일을 하다가 알림이 도착하면 휴대폰으로 결제를 진행할 수 있게 된다. |
| Dynamics | 시스템이 티켓 예약을 완료한 경우 |
| Goals | 알림 전송 기능을 구현한다. |

### 3) Login
| Item | Description |
|------|------------|
| Purpose | 기차 예매가 가능한 사람인지 판단한다. |
| Approach | 시스템이 사용자가 제공한 정보로 로그인을 수행한다. 만일 로그인이 불가하다면 오류 메시지를 출력한다. |
| Dynamics | 로그인을 하는 경우 |
| Goals | 로그인 기능을 구현한다. |

### 4) Ticket search
| Item | Description |
|------|------------|
| Purpose | 티켓을 검색한다. |
| Approach | 사용자가 제공한 정보를 바탕으로 조건을 검색하여 해당하는 티켓을 조회한다. 잘못된 입력을 방지하기 위해 목록에서 선택하는 방식으로 입력받는다. |
| Dynamics | 티켓 정보를 검색할 경우 |
| Goals | 티켓 검색 기능을 구현한다. |

### 5) Check seat
| Item | Description |
|------|------------|
| Purpose | 좌석이 있는지 확인한다. |
| Approach | 티켓 검색 결과를 바탕으로 남아있는 좌석이 있는지 확인한다. 있다면 다음 단계로 진행하고, 없다면 검색을 다시 한다. |
| Dynamics | 여석을 확인하는 경우 |
| Goals | 여석 확인 기능을 구현한다. |

### 6) Input Reservation Info
| Item | Description |
|------|------------|
| Purpose | 사용자의 정보를 제공한다. |
| Approach | 원하는 티켓의 시간이나 로그인 정보 등 필요한 정보를 입력받는다. |
| Dynamics | 사용자가 프로그램을 실행하려는 경우 |
| Goals | 정보 입력 기능을 구현한다. |

### 7) Stop Reservation
| Item | Description |
|------|------------|
| Purpose | 예약을 중지한다. |
| Approach | 사용자의 변심이나 다른 경로로 구한 경우 등 프로그램 중지가 필요한 경우 프로그램을 중지시킨다. |
| Dynamics | 티켓 예약을 중지하려는 경우 |
| Goals | 프로그램 중지 기능을 구현한다. |

### 8) View status
| Item | Description |
|------|------------|
| Purpose | 티켓 예약 현황을 파악한다. |
| Approach | 현재 프로그램 진행도나 실제로 기동하고 있는지 확인이 필요한 경우 시각적으로 표현한다. |
| Dynamics | 티켓 예약 현황을 확인하고 싶은 경우 |
| Goals | 티켓 예약 현황 기능을 구현한다. |

### 9) Select platform
| Item | Description |
|------|------------|
| Purpose | 플랫폼을 선택한다. |
| Approach | 원하는 티켓을 위한 플랫폼을 선택한다. 입력 오류를 방지하기 위해 구현되어 있는 플랫폼 중 선택하는 방식으로 입력을 받는다. |
| Dynamics | 플랫폼을 선택하는 경우 |
| Goals | 플랫폼 선택 기능을 구현한다. |


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
|------|------------|
| 플랫폼 | 사용자가 예약/구매를 원하는 플랫폼 |
| 티켓 | 기차표나 영화표 같은 사용자가 원하는 목표 |
| 예약 | 사용자의 목표를 위해 수행하는 작업, 최종적으로 예약 혹은 확인을 뜻함 |
| 서버 | 사용자의 작업을 대신하기 위함 |

---

## <a id="references"></a>7. References

- [Selenium](https://www.selenium.dev/)
- [Spring Boot](https://spring.io/projects/spring-boot)


