import java.util.*;
import org.json.*;

/**
 * 건강보험심사평가원 병원정보서비스
 * 시/도별 의료기관 수 집계 → DS 적재
 *
 * 지표: Indicator.MEDICAL
 * 흐름: totalCount 파악 → 페이지 단위 분할 수집 → 구/군+동 동시 집계
 * (대량 요청 한 방에 안 보내고 나눠 보내 서버 부하/타임아웃 완화)
 */
public class CollectorInfra extends Collector {

    private static final int PAGE_SIZE = 100;

    @Override
    public void collect(RegionStore ds) throws Exception {
        String sidoCd = RegionCode.HIRA_SIDO.get(ds.sido);

        // API 응답의 sgguCdNm 접두사 (예: "대구중구", "부산중구")
        // 서울은 접두사 없이 "종로구" 형태로 옴
        String prefix = ds.sido.equals("서울특별시") ? ""
            : ds.sido.replace("광역시", "").replace("특별시", "").replace("자치시", "");

        // sgguCdNm("대구달서구") → 구/군명("달서구") 매핑 동적 생성
        Map<String, String> sgguMap = new LinkedHashMap<>();
        for (String district : ds.districts) {
            sgguMap.put(prefix + district, district);
        }

        // 1단계: 전체 건수 파악
        int totalCount = fetchTotalCount(sidoCd);
        System.out.println("  전체 의료기관 수: " + totalCount + "개");

        delay();

        // 2단계: 페이지 단위로 나눠서 수집
        JSONArray items = fetchAllItems(sidoCd, totalCount);

        // 3단계: 구/군별 + 동별 동시 집계
        Map<String, Integer>              districtCount = new LinkedHashMap<>();
        Map<String, Map<String, Integer>> dongCount     = new LinkedHashMap<>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item     = items.getJSONObject(i);
            String     sgguNm  = item.optString("sgguCdNm", "");
            String     dongNm  = item.optString("emdongNm", "");
            String     district = sgguMap.get(sgguNm);

            if (district == null) continue;

            districtCount.merge(district, 1, Integer::sum);
            if (!dongNm.isEmpty()) {
                dongCount.computeIfAbsent(district, k -> new LinkedHashMap<>())
                         .merge(dongNm, 1, Integer::sum);
            }
        }

        // 4단계: DS 적재
        int sidoTotal = 0;
        for (Map.Entry<String, Integer> e : districtCount.entrySet()) {
            ds.set(Indicator.MEDICAL, e.getKey(), e.getValue());
            sidoTotal += e.getValue();
        }
        ds.set(Indicator.MEDICAL, ds.sido, sidoTotal);

        dongCount.forEach((district, dongs) ->
            dongs.forEach((dong, cnt) ->
                ds.setDong(Indicator.MEDICAL, district, dong, cnt)));

        System.out.println("  [의료] " + ds.sido + " 전체 의료기관: " + sidoTotal + "개");
    }

    private int fetchTotalCount(String sidoCd) throws Exception {
        String url = AppConfig.HOSPITAL_URL
            + "?serviceKey=" + AppConfig.SERVICE_KEY
            + "&sidoCd=" + sidoCd + "&numOfRows=1&pageNo=1";

        JSONObject root = parseJson(httpGet(url));
        return requireObject(requireObject(root, "response"), "body")
            .getInt("totalCount");
    }

    /** PAGE_SIZE 단위로 나눠 전체 페이지를 수집·병합 */
    private JSONArray fetchAllItems(String sidoCd, int totalCount) throws Exception {
        JSONArray all = new JSONArray();
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        for (int page = 1; page <= totalPages; page++) {
            String url = AppConfig.HOSPITAL_URL
                + "?serviceKey=" + AppConfig.SERVICE_KEY
                + "&sidoCd=" + sidoCd
                + "&numOfRows=" + PAGE_SIZE + "&pageNo=" + page;

            JSONArray pageItems = parseItems(httpGet(url));
            for (int i = 0; i < pageItems.length(); i++) {
                all.put(pageItems.getJSONObject(i));
            }

            if (page < totalPages) delay();
        }

        return all;
    }

    /** items 필드가 배열(여러 개)이거나 객체(1개)인 경우를 모두 처리 */
    private JSONArray parseItems(String json) {
        JSONObject root = parseJson(json);
        Object rawItems = requireObject(requireObject(requireObject(root, "response"), "body"), "items")
                              .get("item");

        return (rawItems instanceof JSONArray)
            ? (JSONArray) rawItems
            : new JSONArray().put(rawItems);
    }
}