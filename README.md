# 🚇 jwp-subway-path

---

## 🤼 페어

---

| <img src="https://avatars.githubusercontent.com/u/77482065?v=4" alt="" width=150> | <img src="https://avatars.githubusercontent.com/u/106813090?v=4" alt="" width=150/> |
|:---------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------:|
|                         [디노](https://github.com/jjongwa)                          |                          [후추](https://github.com/Combi153)                          | |

## 🦖 기능 요구사항

---

### API

- 노선
    - 등록 : Post /lines
    - 조회 : Get
        - 전체 조회 /lines/stations
        - 특정 노선 조회 : /lines/{lineId}/stations

- 역
    - 등록 : Post /stations

- 노선과 역
    - 등록 : Post /lines/{lineId}/stations
    - 삭제 : Delete /lines/{lineId}/stations/{stationId}

### 비즈니스 규칙

- 노선에 역 등록
    - 등록되는 역의 위치는 자유롭게 지정 가능하다.
        - A-B-C 역이 등록되어 있는 노선에 D역을 등록할 경우 역과 역 사이, A역 앞과 C역 뒤에도 등록을 할 수 있다.
    - 거리 정보를 포함한다.
        - 거리 정보는 양의 정수로 제한된다.
    - 노선에 역이 하나도 등록되지 않은 상황에서 **최초 등록 시 두 역을 동시에 등록해야 한다.**
    - 하나의 역은 여러 노선에 등록될 수 있다.
    - 노선은 갈래길을 가질 수 없다.
    - 노선 가운데 역이 등록될 경우 거리 정보를 고려해야 한다.
        - 거리가 3km인 A-C 노선 사이에 B 역을 등록할 때 다음을 만족한다.
            - `A-B 거리` + `B-C 거리` = 3km
            - `A-B 거리 > 0`  && `B-C 거리 > 0`

- 노선의 역 제거
    - 노선에서 중간 역을 제거할 경우 노선의 역 순서와 거리정보가 재배치된다.
        - A-B-C-D 역이 있는 노선에서 C역이 제거되는 경우 A-B-D 순으로 재배치된다.
        - A-B가 2km, B-C가 3km, C-D가 4km인 경우 C역이 제거되면 B-D 거리가 7km가 된다.
    - 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거된다.

## 🧂 구현할 기능 목록

---

### domain

- [x] 역 (Station)
    - [x] 이름을 갖는다. (name)

- [x] 구간 (Section)
    - [x] 두 개의 역으로 구성된다.
    - [x] 두 역 사이의 길이를 갖는다.
    - [x] 역을 포함하는지 여부를 확인한다.
    - [x] 역을 교체한다.

- [x] 구간들 (Sections)
    - [x] 여러 구간으로 구성된다.
    - [x] 역을 추가한다.
    - [x] 역을 삭제한다.

- [x] 노선 (Line)
    - [x] 이름을 갖는다.
    - [x] 색을 갖는다.
    - [x] 구간을 관리한다.

### API

- [x] 노선에 역 등록
    - Post `/lines/{lineId}/stations`
    - Request body : stationId

- [x] 노선의 역 제거
    - Delete `/lines/{lineId}/stations/{stationId}`

- [x] 노선의 역 조회
    - Get `/lines/{lineId}/stations`

- [x] 모든 노선의 역 조회
    - Get `/lines/stations`

## 📀 데이터베이스

```sql
create table if not exists STATION
(
    id   bigint auto_increment not null,
    name varchar(255)          not null unique
    );

create table if not exists LINE
(
    id    bigint auto_increment not null,
    name  varchar(255)          not null unique,
    color varchar(20)           not null
    );

create table if not exists SECTION
(
    id       bigint auto_increment not null,
    line_id  bigint                not null,
    from_id  bigint                not null,
    to_id    bigint                not null,
    distance bigint                not null
);
```
