import java.util.*;


//지표별 min-max 정규화 → 0~100점
public class ScoreCalculator {

    public static void calculate(RegionStore ds) {
        System.out.println("\n[점수 계산 중...]");

        for (Indicator ind : Indicator.values()) {
            // 1. 구/군 단위 정규화
            normalizeDistrict(ds, ind);

            // 2. 동 단위 정규화 (supportsDong=true인 지표만)
            if (ind.supportsDong) {
                normalizeDong(ds, ind);
            }
        }

        System.out.println("점수 계산 완료 ✓");
    }

    //구/군 단위 min-max 정규화, 시/도 합계는 정규화 대상에서 제외
    private static void normalizeDistrict(RegionStore ds, Indicator ind) {
        // 시/도 제외, 유효값(≥0)만 추출
        Map<String, Double> districtData = new LinkedHashMap<>();
        for (Map.Entry<String, Double> e : ds.getData(ind).entrySet()) {
            if (!e.getKey().equals(ds.sido) && e.getValue() >= 0) {
                districtData.put(e.getKey(), e.getValue());
            }
        }

        if (districtData.isEmpty()) return;

        double min   = Collections.min(districtData.values());
        double max   = Collections.max(districtData.values());
        double range = max - min;

        for (Map.Entry<String, Double> e : districtData.entrySet()) {
            double score = calcScore(e.getValue(), min, max, range, ind.reverse);
            ds.setScore(ind, e.getKey(), score);
        }
    }

    /**
     * 동 단위 점수 계산
     * 구 점수를 기준으로 ±10 범위 내에서 min-max 정규화
     * key 형식: "달서구:성당동"
     */
    private static void normalizeDong(RegionStore ds, Indicator ind) {
        for (String district : ds.districts) {
            double districtScore = ds.getScores(ind).getOrDefault(district, 50.0);

            // 해당 구에 속한 동 데이터 추출
            Map<String, Double> districtDongs = new LinkedHashMap<>();
            for (Map.Entry<String, Double> e : ds.getDongData(ind).entrySet()) {
                if (e.getKey().startsWith(district + ":") && e.getValue() >= 0) {
                    districtDongs.put(e.getKey(), e.getValue());
                }
            }

            if (districtDongs.isEmpty()) continue;

            double min   = Collections.min(districtDongs.values());
            double max   = Collections.max(districtDongs.values());
            double range = max - min;

            for (Map.Entry<String, Double> e : districtDongs.entrySet()) {
                String key  = e.getKey();
                String dong = key.substring(district.length() + 1); // "달서구:성당동" → "성당동"

                double score;
                if (range == 0) {
                    score = districtScore;
                } else if (ind.reverse) {
                    score = districtScore + 10.0 - (e.getValue() - min) / range * 20.0;
                } else {
                    score = districtScore - 10.0 + (e.getValue() - min) / range * 20.0;
                }

                score = Math.max(0, Math.min(100, score));
                score = Math.round(score * 10.0) / 10.0;
                ds.setDongScore(ind, district, dong, score);
            }
        }
    }

    //min-max 정규화 후 소수점 1자리 반환
    private static double calcScore(double value, double min, double max,
                                    double range, boolean reverse) {
        double score;
        if (range == 0) {
            score = 50.0;
        } else if (reverse) {
            score = (max - value) / range * 100.0;
        } else {
            score = (value - min) / range * 100.0;
        }
        return Math.round(score * 10.0) / 10.0;
    }
}