## Automated Reservation System
**Conceptualization Document**

## Author
- Student ID: 22212068  
- Name: 양은석  
- Email: yes6815@yu.ac.kr  

---

## Revision History

| Date | Version | Description | Author |
|------|--------|------------|--------|
| 03/23/2026 | 1.00 | 초안 | 양은석 |

---

## 📑 Contents

- [1. Business Purpose](#business-purpose)
- [2. System Context Diagram](#system-context-diagram)
- [3. Use Case List](#use-case-list)
- [4. Concept of Operation](#concept-of-operation)
- [5. Problem Statement](#problem-statement)
- [6. Glossary](#glossary)
- [7. References](#references)
  
## 1. Business Purpose

### 1) Project Background

현대의 다양한 웹 서비스에서는 예약(Reservation) 기능이 핵심적인 역할을 한다.
기차표, 영화, 공연, 숙박 등 여러 분야에서 예약 시스템이 존재하지만, 
대부분의 서비스는 사용자가 직접 반복적으로 조건을 입력하고 결과를 확인해야 한다.
특히 인기 있는 예약의 경우 짧은 시간 내에 예약이 마감되기 때문에 사용자는 지속적인 새로고침 및 반복적인 시도를 수행해야 하며,
이 과정에서 다음과 같은 문제가 발생한다:

- 반복적인 수동 작업으로 인한 피로도 증가  
- 실시간 대응 어려움으로 인한 예약 실패  
- 비효율적인 시간 사용  

이러한 문제를 해결하기 위해, 사용자의 입력을 기반으로 반복적인 예약 작업을 자동으로 수행하고, 
성공 시 즉시 사용자에게 알림을 제공하는 시스템의 필요성이 증가하고 있다.
따라서 본 프로젝트에서는 특정 서비스에 종속되지 않고 다양한 웹 기반 예약 시스템에 적용 가능한 **자동 예약 및 알림 시스템**을 설계한다.

---

### 2) Goal

- 웹 기반 자동 예약 시스템 설계  
- 반복 작업 자동화  
- 다양한 예약 시스템 확장 가능 구조 설계  
- 예약 성공 시 실시간 알림 제공  
- 사용자 편의성 향상  

---

### 3) Target Market

- 온라인 예약 시스템 사용자  
- 반복 작업에 불편함을 느끼는 사용자  
- 자동화를 원하는 사용자  

---

## 2. System Context Diagram
<img width="311" height="443" alt="Conceptualization_Diagram" src="https://github.com/user-attachments/assets/8079e65b-558f-411d-ac8e-4ed573941a20" />

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

## 3. Use Case List

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

## 4. Concept of Operation


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


## 5. Problem Statement
본 시스템은 다양한 웹 예약 시스템을 대상으로 자동화를 제공하는 것을 목표로 한다. 이를 위해 다음과 같은 문제들을 고려해야 한다.

### Problem #1: Website Dependency
웹사이트마다 구조가 달라 자동화 로직이 달라질 수 있다.
→ Adapter 기반 설계가 필요하다.

---

### Problem #2: Continuous Request Load
반복적인 요청으로 인해 외부 시스템에 부하를 줄 수 있으며, 차단 가능성이 존재한다.  
→ 사람을 대신하는 수준의 요청으로 간격을 조정한다.

---

### Problem #3: Technical Difficulty
웹 기반 시스템 및 자동화 기술을 공부한다.

---

### Problem #4: Ethical Issue
자동화 시스템이 특정 웹 서비스의 정책에 위반될 가능성이 존재한다.
→ 서비스 마다 제한적 사용 및 제한이 필요하다.

---

### NFRs (Non-Functional Requirements)

- 시스템은 실패 시 재시도 로직을 통해 안정적으로 동작해야 한다  
- 새로운 사이트 추가 시 기존 코드 수정 최소화되야 한다 
- 모듈화된 구조 유지하고, 각 웹사이트별 로직은 독립적으로 관리 가능해야 한다
- 사용자 친화적인 UI 제공한다  
- 대상 웹사이트의 이용 약관을 고려하여 과도한 요청을 방지하고, 정책 위반 가능성을 최소화해야 한다. 

---

## 6. Glossary

| Term | Description |
|------|------------|
| 플랫폼 | 사용자가 예약/구매를 원하는 플랫폼 |
| 티켓 | 기차표나 영화표 같은 사용자가 원하는 목표 |
| 예약 | 사용자의 목표를 위해 수행하는 작업, 최종적으로 예약 혹은 확인을 뜻함 |
| 서버 | 사용자의 작업을 대신하기 위함 |

---

## 7. References

- [Selenium](https://www.selenium.dev/)
- [Spring Boot](https://spring.io/projects/spring-boot)


