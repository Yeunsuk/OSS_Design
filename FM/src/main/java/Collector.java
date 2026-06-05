import java.io.*;
import java.net.*;

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

    private String request(String urlStr, boolean withAccept) throws Exception {
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
}