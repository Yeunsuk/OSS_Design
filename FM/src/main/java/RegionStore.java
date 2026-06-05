import org.json.*;
import java.io.*;
import java.nio.file.*;
import java.time.YearMonth;
import java.util.*;

/**
 * 중앙 데이터 저장소
 * Indicator enum을 키로 사용
 */
public class RegionStore {

    private static final String OUTPUT_PATH = "docs/data/regions.json";

    public final String   sido;
    public final String[] districts;

    private final Map<Indicator, Map<String, Double>> rawData    = new EnumMap<>(Indicator.class);
    private final Map<Indicator, Map<String, Double>> scores     = new EnumMap<>(Indicator.class);
    private final Map<Indicator, Map<String, Double>> dongData   = new EnumMap<>(Indicator.class);
    private final Map<Indicator, Map<String, Double>> dongScores = new EnumMap<>(Indicator.class);
    private final Map<String, Set<String>>            dongRegistry = new LinkedHashMap<>();
    private final Map<String, Double>                 totalScore   = new LinkedHashMap<>();

    public RegionStore(String sido) {
        this.sido      = sido;
        this.districts = RegionCode.DISTRICTS.get(sido);  // AppConfig → RegionCode

        for (Indicator ind : Indicator.values()) {
            rawData.put(ind,    new LinkedHashMap<>());
            scores.put(ind,     new LinkedHashMap<>());
            dongData.put(ind,   new LinkedHashMap<>());
            dongScores.put(ind, new LinkedHashMap<>());
        }
    }

    // ── 적재 ─────────────────────────────────────────────────────────────────

    public void set(Indicator ind, String region, double value) {
        rawData.get(ind).put(region, value);
    }

    public void setDong(Indicator ind, String district, String dong, double value) {
        dongRegistry.computeIfAbsent(district, k -> new LinkedHashSet<>()).add(dong);
        dongData.get(ind).put(dongKey(district, dong), value);
    }

    public void setScore(Indicator ind, String region, double score) {
        scores.get(ind).put(region, score);
    }

    public void setDongScore(Indicator ind, String district, String dong, double score) {
        dongScores.get(ind).put(dongKey(district, dong), score);
    }

    // ── 조회 ─────────────────────────────────────────────────────────────────

    public Map<String, Double> getData(Indicator ind)       { return rawData.get(ind);    }
    public Map<String, Double> getScores(Indicator ind)     { return scores.get(ind);     }
    public Map<String, Double> getDongData(Indicator ind)   { return dongData.get(ind);   }

    // ── JSON 저장 ─────────────────────────────────────────────────────────────

    public void save() throws Exception {
        Files.createDirectories(Paths.get("docs/data"));

        JSONObject root;
        try {
            root = new JSONObject(new String(Files.readAllBytes(Paths.get(OUTPUT_PATH))));
        } catch (Exception e) {
            root = new JSONObject()
                .put("meta",    new JSONObject()
                    .put("last_updated",  YearMonth.now().toString())
                    .put("score_version", "1.0"))
                .put("regions", new JSONObject());
        }

        root.getJSONObject("regions").put(sido, buildSidoJson());
        root.getJSONObject("meta").put("last_updated", YearMonth.now().toString());

        try (FileWriter fw = new FileWriter(OUTPUT_PATH)) {
            fw.write(root.toString(2));
        }
        System.out.println("저장 완료: " + OUTPUT_PATH + " [" + sido + "]");
    }

    private JSONObject buildSidoJson() {
        JSONObject districtsObj = new JSONObject();
        for (String district : districts) {
            districtsObj.put(district, buildDistrictJson(district));
        }
        return new JSONObject()
            .put("level",       "sido")
            .put("indicators",  buildIndicators(sido))
            .put("total_score", totalScore.getOrDefault(sido, 0.0))
            .put("districts",   districtsObj);
    }

    private JSONObject buildDistrictJson(String district) {
        JSONObject obj = new JSONObject()
            .put("level",       "sigungu")
            .put("indicators",  buildIndicators(district))
            .put("total_score", totalScore.getOrDefault(district, 0.0));

        Set<String> dongs = dongRegistry.getOrDefault(district, Collections.emptySet());
        if (!dongs.isEmpty()) {
            JSONObject dongsObj = new JSONObject();
            for (String dong : dongs) {
                dongsObj.put(dong, new JSONObject().put("indicators", buildDongIndicators(district, dong)));
            }
            obj.put("dongs", dongsObj);
        }
        return obj;
    }

    private JSONObject buildIndicators(String region) {
        JSONObject obj = new JSONObject();
        for (Indicator ind : Indicator.values()) {
            obj.put(ind.jsonKey, new JSONObject()
                .put("value", rawData.get(ind).getOrDefault(region, -1.0))
                .put("score", scores.get(ind).getOrDefault(region, 0.0)));
        }
        return obj;
    }

    private JSONObject buildDongIndicators(String district, String dong) {
        String key = dongKey(district, dong);
        JSONObject obj = new JSONObject();
        for (Indicator ind : Indicator.values()) {
            double value = ind.supportsDong
                ? dongData.get(ind).getOrDefault(key, -1.0)
                : rawData.get(ind).getOrDefault(district, -1.0);
            double score = ind.supportsDong
                ? dongScores.get(ind).getOrDefault(key, 0.0)
                : scores.get(ind).getOrDefault(district, 0.0);
            obj.put(ind.jsonKey, new JSONObject().put("value", value).put("score", score));
        }
        return obj;
    }

    // ── 유틸 ─────────────────────────────────────────────────────────────────

    private String dongKey(String district, String dong) {
        return district + ":" + dong;
    }
}