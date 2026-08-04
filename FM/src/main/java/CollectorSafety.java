import java.util.*;
import org.json.*;

/**
 * 경찰청 범죄 발생 지역별 통계 수집기
 *
 * API: 공공데이터포털 odcloud - 범죄 발생 지역별 통계
 * 지표: Indicator.CRIME (낮을수록 좋음)
 */
public class CollectorSafety extends Collector {

    @Override
    public void collect(RegionStore ds) throws Exception {
        // "대구광역시" → "대구" 형태로 축약 (API 응답 키 형식에 맞춤)
        String sidoPrefix = ds.sido
            .replace("광역시", "").replace("특별시", "").replace("자치시", "").trim();

        // 구/군명 → API 응답 키 매핑 동적 생성 (예: "달서구" → "대구 달서구")
        Map<String, String> districtToApiKey = new LinkedHashMap<>();
        for (String district : ds.districts) {
            districtToApiKey.put(district, sidoPrefix + " " + district);
        }

        Map<String, Integer> districtCrimes = fetchCrimeByDistrict(districtToApiKey);

        int sidoTotal = 0;
        for (Map.Entry<String, Integer> e : districtCrimes.entrySet()) {
            ds.set(Indicator.CRIME, e.getKey(), e.getValue());
            sidoTotal += e.getValue();
        }
        ds.set(Indicator.CRIME, ds.sido, sidoTotal);

        System.out.println("  [안전] " + ds.sido + " 총 범죄: " + sidoTotal + "건");
    }

    /**
     * API 1회 호출 → 모든 구/군 범죄 건수 합산
     * (API가 중분류 행으로 제공하므로 구별로 합산)
     */
    private Map<String, Integer> fetchCrimeByDistrict(
            Map<String, String> districtToApiKey) throws Exception {

        String url = AppConfig.CRIME_URL
            + "?page=1&perPage=38&serviceKey=" + AppConfig.SERVICE_KEY;

        String response = httpGet(url);
        JSONObject root = new JSONObject(response);
        JSONArray  data = requireArray(root, "data");

        System.out.println("  수신 행 수: " + data.length() + "개 (범죄 중분류)");

        Map<String, Integer> districtTotal = new LinkedHashMap<>();
        for (String district : districtToApiKey.keySet()) {
            districtTotal.put(district, 0);
        }

        for (int i = 0; i < data.length(); i++) {
            JSONObject row = data.getJSONObject(i);
            for (Map.Entry<String, String> e : districtToApiKey.entrySet()) {
                districtTotal.merge(e.getKey(), row.optInt(e.getValue(), 0), Integer::sum);
            }
        }

        return districtTotal;
    }
}