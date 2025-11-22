import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.parser.ParseException; // ParseException 사용을 위해 import
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DustAPI {
    // IOException만 throws하고, ParseException은 내부에서 처리합니다.
    public static void main(String[] args) throws IOException {

        // --- 1. API 요청 URL 생성 (기존 코드 유지) ---
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMinuDustFrcstDspth");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=8cfcf016d3df4e6d3e13bd8a5dcc1a95d8fc404c96c5c2fe3c4ffca9fe801390");
        urlBuilder.append("&" + URLEncoder.encode("returnType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("searchDate", "UTF-8") + "=" + URLEncoder.encode("2025-11-20", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("InformCode", "UTF-8") + "=" + URLEncoder.encode("PM10", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        // --- 2. 응답 데이터 수신 ---
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String result = sb.toString();

        // --- 3. JSON 파싱 및 데이터 추출 (새로운 로직) ---
        JSONParser parser = new JSONParser();

        try {
            // 1. 전체 문자열을 최상위 JSONObject로 파싱
            JSONObject obj = (JSONObject) parser.parse(result);

            // 2. "response" 키에서 객체 추출
            JSONObject parse_response = (JSONObject) obj.get("response");

            // 3. "body" 키에서 객체 추출
            JSONObject parse_body = (JSONObject) parse_response.get("body");

            // 4. "items" 키에서 데이터 배열(JSONArray) 추출
            JSONArray parse_items = (JSONArray) parse_body.get("items");

            if (parse_items == null || parse_items.isEmpty()) {
                System.out.println("\n[알림] 조회된 미세먼지 예보 데이터가 없습니다.");
                return;
            }

            // 5. items 배열의 첫 번째 요소(JSONObject)에서 상세 정보 추출
            // (미세먼지 예보 데이터는 보통 배열 안에 하나의 예보 객체가 들어있습니다.)
            JSONObject item = (JSONObject) parse_items.get(0);

            // 필요한 정보 추출
            String informData = (String) item.get("informData"); // 예보일자
            String dataTime = (String) item.get("dataTime");     // 발표시각
            String informOverall = (String) item.get("informOverall"); // 종합예보
            String informCause = (String) item.get("informCause");   // 예측원인
            String informGrade = (String) item.get("informGrade");   // 등급정보 (지역별)
            String informCode = (String) item.get("informCode");     // PM10/PM25 구분

            System.out.println("\n=== 미세먼지 예보 상세 정보 ===");
            System.out.println("📦 예보 대상 날짜: " + informData);
            System.out.println("🕒 발표 시각: " + dataTime);
            System.out.println("------------------------------------");
            System.out.println("📋 예보 구분: " + informCode);
            System.out.println("📝 종합 예보: " + informOverall);
            System.out.println("🔍 예측 원인: " + informCause);
            System.out.println("📊 지역별 등급: " + informGrade);
            System.out.println("------------------------------------");

        } catch (ParseException e) {
            // JSON 문자열 자체가 잘못되었을 때 발생 (Checked Exception 처리)
            System.err.println("\n[파싱 실패] 수신된 JSON 문자열 형식이 올바르지 않습니다.");
            e.printStackTrace();
        } catch (ClassCastException | NullPointerException e) {
            // 예상한 JSON 구조와 다를 때 발생 (Runtime Exception 처리)
            System.err.println("\n[구조 오류] JSON 데이터 구조가 예상과 다릅니다. 키 이름이나 타입 캐스팅을 확인하세요.");
            e.printStackTrace();
        }
    }
}