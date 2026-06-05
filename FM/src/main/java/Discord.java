import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Discord {

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ── 공개 API ─────────────────────────────────────────────────────────────

    //수집기 단위 에러 알림
    public static void notifyError(String sido, String collectorName, Exception e) {
        String msg = String.format(
            "🚨 [%s] %s - %s 수집 실패\\n원인: %s",
            now(), sido, collectorName, escapeJson(e.getMessage())
        );
        send(AppConfig.DISCORD_WEBHOOK_URL_ERR, msg);
    }

    //전체 프로그램 치명적 오류 알림
    public static void notifyFatal(Exception e) {
        String msg = String.format(
            "💀 [%s] 수집 프로그램 비정상 종료\\n원인: %s",
            now(), escapeJson(e.getMessage())
        );
        send(AppConfig.DISCORD_WEBHOOK_URL_ERR, msg);
    }

    // ── 내부 구현 ─────────────────────────────────────────────────────────────
    private static void send(String webhookUrl, String message) {
        if (webhookUrl == null || webhookUrl.isBlank()) {
            System.err.println("[Discord] 웹훅 URL이 설정되지 않았습니다.");
            return;
        }

        try {
            URL url = URI.create(webhookUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(AppConfig.HTTP_CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(AppConfig.HTTP_READ_TIMEOUT_MS);
            conn.setDoOutput(true);

            String payload = String.format("{\"content\": \"%s\"}", message);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes("UTF-8"));
            }

            int code = conn.getResponseCode();
            if (code == 204) {
                System.out.println("[Discord] 전송 성공");
            } else {
                System.err.println("[Discord] 전송 실패 - HTTP " + code);
            }
            conn.disconnect();

        } catch (Exception e) {
            // 웹훅 실패가 메인 로직을 중단시키면 안 됨
            System.err.println("[Discord] 전송 중 예외: " + e.getMessage());
        }
    }

    private static String now() {
        return LocalDateTime.now().format(FMT);
    }

    //JSON 문자열 내 특수문자 이스케이프
    private static String escapeJson(String str) {
        if (str == null) return "null";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "");
    }
}