import java.util.*;
import org.json.*;

/**
 * 소상공인진흥공단 상가(상권)정보 API
 * 구/군별 편의시설 수 집계 → DS 적재
 *
 * 지표: Indicator.CONVENIENCE
 * 편의시설 = 편의점(G20405) + 슈퍼마켓(G20404) + 대형마트(G20402)
 * 흐름: 구/군 × 업종코드 조합으로 totalCount 파악 후 동별 집계
 */
public class CollectorConvenience extends Collector {

    private static final String   BASE_URL     = AppConfig.CONVENIENCE_URL;
    private static final String[] UPJONG_CODES = { "G20405", "G20404", "G20402" };

    @Override
    public void collect(RegionStore ds) throws Exception {
        Map<String, String> districtCode = RegionCode.CONVENIENCE_SIGUNGU.get(ds.sido);

        for (Map.Entry<String, String> entry : districtCode.entrySet()) {
            String district = entry.getKey();
            String signguCd = entry.getValue();

            Map<String, Integer> dongCount     = new LinkedHashMap<>();
            int                  districtTotal = 0;

            for (String upjongCd : UPJONG_CODES) {
                try {
                    districtTotal += collectByUpjong(district, signguCd, upjongCd, dongCount);
                    delay();
                } catch (Exception e) {
                    System.out.println("  오류 [" + district + "/" + upjongCd + "]: " + e.getMessage());
                }
            }

            ds.set(Indicator.CONVENIENCE, district, districtTotal);
            dongCount.forEach((dong, cnt) ->
                ds.setDong(Indicator.CONVENIENCE, district, dong, cnt));

            System.out.println("  " + district + ": " + districtTotal
                + "개 (" + dongCount.size() + "개 동)");
        }

        // 시/도 합계
        double sidoTotal = ds.getData(Indicator.CONVENIENCE).values()
            .stream().mapToDouble(Double::doubleValue).sum();
        ds.set(Indicator.CONVENIENCE, ds.sido, sidoTotal);

        System.out.println("  [편의] " + ds.sido + " 전체: " + (int) sidoTotal + "개");
    }

    /**
     * 단일 (구/군 + 업종) 조합의 편의시설을 수집합니다.
     * @return 해당 조합의 총 건수
     */
    private int collectByUpjong(String district, String signguCd, String upjongCd,
                                 Map<String, Integer> dongCount) throws Exception {
        // 1단계: totalCount만 조회
        String countUrl = BASE_URL
            + "?serviceKey=" + AppConfig.SERVICE_KEY
            + "&pageNo=1&numOfRows=1&type=json"
            + "&divId=signguCd&key=" + signguCd
            + "&indsSclsCd=" + upjongCd;

        JSONObject countRoot = parseJson(httpGet(countUrl));
        int total = requireObject(countRoot, "body").optInt("totalCount", 0);

        if (total == 0) return 0;

        // 2단계: 전체 수신 → 동별 집계
        String fullUrl = BASE_URL
            + "?serviceKey=" + AppConfig.SERVICE_KEY
            + "&pageNo=1&numOfRows=" + total + "&type=json"
            + "&divId=signguCd&key=" + signguCd
            + "&indsSclsCd=" + upjongCd;

        JSONObject fullRoot = parseJson(httpGet(fullUrl));
        JSONArray items = requireObject(fullRoot, "body").getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            String dongNm = items.getJSONObject(i).optString("adongNm", "");
            if (!dongNm.isEmpty()) {
                dongCount.merge(dongNm, 1, Integer::sum);
            }
        }

        return total;
    }
}