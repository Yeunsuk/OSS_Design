import java.io.*;
import java.net.*;
import org.json.*;

public abstract class Collector {
    public abstract void collect(RegionStore ds) throws Exception;

    //Accept: application/json 헤더 포함 GET 요청,공공데이터포털 API에 사용합니다.    
    protected String httpGet(String urlStr) throws Exception {
        return request(urlStr, true);
    }
    
    //Accept 헤더 없이 GET 요청, NEIS처럼 Accept 헤더를 거부하는 API에 사용합니다.
    protected String httpGetPlain(String urlStr) throws Exception {
        return request(urlStr, false);
    }

    //서버 점검 등 일시적 네트워크 오류(연결 타임아웃 등) 대비 재시도
    private String request(String urlStr, boolean withAccept) throws Exception {
        IOException lastError = null;

        for (int attempt = 1; attempt <= AppConfig.HTTP_MAX_ATTEMPTS; attempt++) {
            try {
                return doRequest(urlStr, withAccept);
            } catch (IOException e) {
                lastError = e;
                System.err.println("  [HTTP 재시도 " + attempt + "/" + AppConfig.HTTP_MAX_ATTEMPTS
                    + "] " + e.getMessage());
                if (attempt < AppConfig.HTTP_MAX_ATTEMPTS) {
                    Thread.sleep(AppConfig.HTTP_RETRY_DELAY_MS);
                }
            }
        }

        throw lastError;
    }

    private String doRequest(String urlStr, boolean withAccept) throws IOException {
        //URL url = new URL(urlStr);
        URL url = URI.create(urlStr).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(AppConfig.HTTP_CONNECT_TIMEOUT_MS);
        conn.setReadTimeout(AppConfig.HTTP_READ_TIMEOUT_MS);

        if (withAccept) {
            conn.setRequestProperty("Accept", "application/json");
        }

        int code = conn.getResponseCode();
        InputStream is = (code >= 200 && code < 300)
            ? conn.getInputStream()
            : conn.getErrorStream();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) sb.append(line);
            return sb.toString();
        } finally {
            conn.disconnect();
        }
    }
    
    //API 연속 호출 시 서버 부하 방지용 딜레이
    protected void delay() throws InterruptedException {
        Thread.sleep(AppConfig.API_DELAY_MS);
    }

    //root에 key(JSONObject)가 없으면 API 에러 응답 내용을 담아 예외를 던집니다.
    protected JSONObject requireObject(JSONObject root, String key) {
        if (!root.has(key)) throw new RuntimeException(describeApiError(root, key));
        return root.getJSONObject(key);
    }

    //root에 key(JSONArray)가 없으면 API 에러 응답 내용을 담아 예외를 던집니다.
    protected JSONArray requireArray(JSONObject root, String key) {
        if (!root.has(key)) throw new RuntimeException(describeApiError(root, key));
        return root.getJSONArray(key);
    }

    //공공데이터포털 공통 에러 포맷(cmmMsgHeader)을 우선 인식하고, 아니면 원본 응답 일부를 담아 반환합니다.
    private String describeApiError(JSONObject root, String expectedKey) {
        if (root.has("cmmMsgHeader")) {
            JSONObject hdr = root.getJSONObject("cmmMsgHeader");
            String reason = hdr.optString("returnAuthMsg", hdr.optString("errMsg", "알 수 없음"));
            String code   = hdr.optString("returnReasonCode", "?");
            return "공공데이터포털 API 오류: " + reason + " (code=" + code + ")";
        }
        String raw = root.toString();
        if (raw.length() > 300) raw = raw.substring(0, 300) + "...";
        return "API 응답에 '" + expectedKey + "' 필드가 없습니다. 원본 응답: " + raw;
    }
}