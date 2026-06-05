/**
 * 전역 설정 파일 - 키, URL, 숫자 상수만 보유
 *
 * 행정구역 코드/목록 → RegionCode.java
 * HTTP 유틸          → BaseCollector.java
 * Discord 알림       → Discord.java
 */
public class AppConfig {

    // ── 서비스 키 ────────────────────────────────────────────────────────────
    public static final String SERVICE_KEY =
        getEnv("DATA_GO_KR_KEY", null);

    public static final String SERVICE_KEY_NEIS =
        getEnv("NEIS_KEY", null);

    // ── Discord 웹훅 URL ─────────────────────────────────────────────────────
    // 성공 알림 채널 (수집 완료 보고)
    public static final String DISCORD_WEBHOOK_URL =
        getEnv("DISCORD_WEBHOOK_URL", null);

    // 에러 알림 채널 (즉시 조치 필요)
    public static final String DISCORD_WEBHOOK_URL_ERR =
        getEnv("DISCORD_WEBHOOK_URL_ERR", null);

    // ── API URL ──────────────────────────────────────────────────────────────
    public static final String HOSPITAL_URL =
        "https://apis.data.go.kr/B551182/hospInfoServicev2/getHospBasisList";

    public static final String CRIME_URL =
        "https://api.odcloud.kr/api/3074462/v1/uddi:ae109087-8690-4cb5-bda9-a7876a92f3b8";

    public static final String CONVENIENCE_URL =
        "https://apis.data.go.kr/B553077/api/open/sdsc2/storeListInDong";

    public static final String SCHOOL_URL =
        "https://open.neis.go.kr/hub/schoolInfo";

    // ── HTTP 설정 ────────────────────────────────────────────────────────────
    public static final int HTTP_CONNECT_TIMEOUT_MS = 10_000;
    public static final int HTTP_READ_TIMEOUT_MS    = 60_000;
    public static final int API_DELAY_MS            = 300;


    // ── 헬퍼 ────────────────────────────────────────────────────────────────
    private static String getEnv(String key, String fallback) {
        String val = System.getenv(key);
        if (val != null && !val.isBlank()) return val;
        if (fallback != null)             return fallback;
        throw new RuntimeException(
            "환경변수 [" + key + "] 가 설정되지 않았습니다.\n" +
            "  로컬:  export " + key + "=발급받은키\n" +
            "  CI/CD: GitHub Secrets에 " + key + " 등록"
        );
    }
}