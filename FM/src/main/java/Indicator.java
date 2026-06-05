/**
 * 수집 지표 열거형
 *
 * jsonKey     : regions.json에 저장되는 키 이름
 * reverse     : true = 값이 낮을수록 좋은 점수 (범죄)
 * supportsDong: true = 동 단위 데이터 보유 (범죄는 구 값을 동에 상속)
 * label       : 로그 출력용 한글명
 */
public enum Indicator {

    CRIME       ("crime_count",       true,  false, "범죄 발생"),
    MEDICAL     ("medical_count",     false, true,  "의료시설"),
    SCHOOL      ("school_count",      false, true,  "학교"),
    CONVENIENCE ("convenience_count", false, true,  "편의시설");

    public final String  jsonKey;
    public final boolean reverse;
    public final boolean supportsDong;
    public final String  label;

    Indicator(String jsonKey, boolean reverse, boolean supportsDong, String label) {
        this.jsonKey      = jsonKey;
        this.reverse      = reverse;
        this.supportsDong = supportsDong;
        this.label        = label;
    }
}