import java.util.*;
import org.json.*;

/**
 * NEIS 교육정보 개방 포털 - 학교기본정보 API
 * 시/도별 학교 수집 → 주소에서 구/군 추출 → DS 적재
 *
 * 지표: Indicator.SCHOOL
 * 특이사항: NEIS는 Accept 헤더를 거부하므로 httpGetPlain() 사용
 *           1000건 페이지 제한이 있어 페이징 처리 필요
 */
public class CollectorSchool extends Collector {

    private static final String BASE_URL  = AppConfig.SCHOOL_URL;
    private static final int    PAGE_SIZE = 1000;

    @Override
    public void collect(RegionStore ds) throws Exception {
        String atptCode = RegionCode.NEIS_ATPT.get(ds.sido);

        // 구/군명을 key로 사용
        List<String> districts = Arrays.asList(ds.districts);

        // 1단계: 전체 건수 파악
        String firstUrl = BASE_URL + "?KEY=" + AppConfig.SERVICE_KEY_NEIS
            + "&Type=json&ATPT_OFCDC_SC_CODE=" + atptCode + "&pSize=1&pIndex=1";
        int totalCount = parseTotalCount(httpGetPlain(firstUrl));
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        System.out.println("  전체 학교 수: " + totalCount + "개 (" + totalPages + "페이지)");

        // 2단계: 페이지별 수집
        Map<String, Integer>              districtCount = new LinkedHashMap<>();
        Map<String, Map<String, Integer>> dongCount     = new LinkedHashMap<>();

        for (int page = 1; page <= totalPages; page++) {
            String url = BASE_URL + "?KEY=" + AppConfig.SERVICE_KEY_NEIS
                + "&Type=json&ATPT_OFCDC_SC_CODE=" + atptCode
                + "&pSize=" + PAGE_SIZE + "&pIndex=" + page;

            JSONArray rows = parseRows(httpGetPlain(url));
            if (rows == null) continue;

            for (int i = 0; i < rows.length(); i++) {
                String addr = rows.getJSONObject(i).optString("ORG_RDNMA", "");
                categorize(addr, districts, districtCount, dongCount);
            }
        }

        // 3단계: DS 적재
        int sidoTotal = 0;
        for (Map.Entry<String, Integer> e : districtCount.entrySet()) {
            ds.set(Indicator.SCHOOL, e.getKey(), e.getValue());
            sidoTotal += e.getValue();
        }
        ds.set(Indicator.SCHOOL, ds.sido, sidoTotal);

        dongCount.forEach((district, dongs) ->
            dongs.forEach((dong, cnt) ->
                ds.setDong(Indicator.SCHOOL, district, dong, cnt)));

        System.out.println("  [학교] " + ds.sido + " 전체 학교: " + sidoTotal + "개");
    }

    //주소 문자열에서 구/군을 찾아 카운트에 반영 
    private void categorize(String addr, List<String> districts,
                             Map<String, Integer>              districtCount,
                             Map<String, Map<String, Integer>> dongCount) {
        for (String district : districts) {
            if (addr.contains(district)) {
                districtCount.merge(district, 1, Integer::sum);
                String dong = extractDong(addr, district);
                if (dong != null) {
                    dongCount.computeIfAbsent(district, k -> new LinkedHashMap<>())
                             .merge(dong, 1, Integer::sum);
                }
                return; // 첫 번째로 매칭된 구/군에만 카운트
            }
        }
    }

    /**
     * 주소에서 동/읍/면 이름을 추출
     * 예: "대구광역시 달서구 성당동 81-78" → "성당동"
     */
    private String extractDong(String addr, String district) {
        int idx = addr.indexOf(district);
        if (idx < 0) return null;

        String[] parts = addr.substring(idx + district.length()).trim().split(" ");
        if (parts.length == 0) return null;

        String dong = parts[0].trim();
        return (dong.endsWith("동") || dong.endsWith("가")
             || dong.endsWith("읍") || dong.endsWith("면"))
            ? dong : null;
    }

    private int parseTotalCount(String json) {
        return new JSONObject(json)
            .getJSONArray("schoolInfo")
            .getJSONObject(0)
            .getJSONArray("head")
            .getJSONObject(0)
            .getInt("list_total_count");
    }

    private JSONArray parseRows(String json) {
        JSONObject root = new JSONObject(json);
        if (!root.has("schoolInfo")) return null;

        JSONArray schoolInfo = root.getJSONArray("schoolInfo");
        if (schoolInfo.length() < 2) return null;

        return schoolInfo.getJSONObject(1).getJSONArray("row");
    }
}