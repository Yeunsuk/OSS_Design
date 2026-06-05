import java.util.*;

/**
 * 행정구역 코드 및 목록 전담 클래스
 *
 * AppConfig의 분리:
 *   AppConfig  → 키, URL, 숫자 상수
 *   RegionCode → 행정구역 데이터 (코드, 목록)
 */
public class RegionCode {

    // ── 시/도별 구/군 목록 ───────────────────────────────────────────────────
    public static final Map<String, String[]> DISTRICTS;

    // ── 심평원 시도 코드 (의료시설 수집) ─────────────────────────────────────
    public static final Map<String, String> HIRA_SIDO;

    // ── NEIS 교육청 코드 (학교 수집) ─────────────────────────────────────────
    public static final Map<String, String> NEIS_ATPT;

    // ── 편의시설 수집용 시군구 코드 ──────────────────────────────────────────
    public static final Map<String, Map<String, String>> CONVENIENCE_SIGUNGU;

    static {
        // ── DISTRICTS ────────────────────────────────────────────────────────
        Map<String, String[]> districts = new LinkedHashMap<>();
        districts.put("대구광역시", new String[]{
            "달서구", "북구", "수성구", "중구", "동구", "서구", "남구", "달성군", "군위군"
        });
        districts.put("부산광역시", new String[]{
            "중구", "서구", "동구", "영도구", "부산진구", "동래구", "남구", "북구",
            "해운대구", "사하구", "금정구", "강서구", "연제구", "수영구", "사상구", "기장군"
        });
        districts.put("서울특별시", new String[]{
            "종로구", "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구", "성북구",
            "강북구", "도봉구", "노원구", "은평구", "서대문구", "마포구", "양천구", "강서구",
            "구로구", "금천구", "영등포구", "동작구", "관악구", "서초구", "강남구", "송파구", "강동구"
        });
        DISTRICTS = Collections.unmodifiableMap(districts);

        // ── HIRA_SIDO ────────────────────────────────────────────────────────
        Map<String, String> hiraSido = new LinkedHashMap<>();
        hiraSido.put("대구광역시", "230000");
        hiraSido.put("부산광역시", "210000");
        hiraSido.put("서울특별시", "110000");
        HIRA_SIDO = Collections.unmodifiableMap(hiraSido);

        // ── NEIS_ATPT ────────────────────────────────────────────────────────
        Map<String, String> neisAtpt = new LinkedHashMap<>();
        neisAtpt.put("대구광역시", "D10");
        neisAtpt.put("부산광역시", "C10");
        neisAtpt.put("서울특별시", "B10");
        NEIS_ATPT = Collections.unmodifiableMap(neisAtpt);

        // ── CONVENIENCE_SIGUNGU ──────────────────────────────────────────────
        Map<String, Map<String, String>> convenience = new LinkedHashMap<>();

        Map<String, String> daegu = new LinkedHashMap<>();
        daegu.put("중구",   "27110"); daegu.put("동구",   "27140"); daegu.put("서구",   "27170");
        daegu.put("남구",   "27200"); daegu.put("북구",   "27230"); daegu.put("수성구", "27260");
        daegu.put("달서구", "27290"); daegu.put("달성군", "27710"); daegu.put("군위군", "27720");
        convenience.put("대구광역시", Collections.unmodifiableMap(daegu));

        Map<String, String> busan = new LinkedHashMap<>();
        busan.put("중구",    "26110"); busan.put("서구",    "26140"); busan.put("동구",    "26170");
        busan.put("영도구",  "26200"); busan.put("부산진구","26230"); busan.put("동래구",  "26260");
        busan.put("남구",    "26290"); busan.put("북구",    "26320"); busan.put("해운대구","26350");
        busan.put("사하구",  "26380"); busan.put("금정구",  "26410"); busan.put("강서구",  "26440");
        busan.put("연제구",  "26470"); busan.put("수영구",  "26500"); busan.put("사상구",  "26530");
        busan.put("기장군",  "26710");
        convenience.put("부산광역시", Collections.unmodifiableMap(busan));

        Map<String, String> seoul = new LinkedHashMap<>();
        seoul.put("종로구",  "11110"); seoul.put("중구",    "11140"); seoul.put("용산구",  "11170");
        seoul.put("성동구",  "11200"); seoul.put("광진구",  "11215"); seoul.put("동대문구","11230");
        seoul.put("중랑구",  "11260"); seoul.put("성북구",  "11290"); seoul.put("강북구",  "11305");
        seoul.put("도봉구",  "11320"); seoul.put("노원구",  "11350"); seoul.put("은평구",  "11380");
        seoul.put("서대문구","11410"); seoul.put("마포구",  "11440"); seoul.put("양천구",  "11470");
        seoul.put("강서구",  "11500"); seoul.put("구로구",  "11530"); seoul.put("금천구",  "11545");
        seoul.put("영등포구","11560"); seoul.put("동작구",  "11590"); seoul.put("관악구",  "11620");
        seoul.put("서초구",  "11650"); seoul.put("강남구",  "11680"); seoul.put("송파구",  "11710");
        seoul.put("강동구",  "11740");
        convenience.put("서울특별시", Collections.unmodifiableMap(seoul));

        CONVENIENCE_SIGUNGU = Collections.unmodifiableMap(convenience);
    }
}