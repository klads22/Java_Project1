import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        // 홈페이지에서 받은 키
        String serviceKey = "8cfcf016d3df4e6d3e13bd8a5dcc1a95d8fc404c96c5c2fe3c4ffca9fe801390";
        String nx = "60";    //위도
        String ny = "127";    //경도 해당 좌표는 상명대학교 종로구 홍지동 좌표임
        String baseDate = "20251105";    //조회하고싶은 날짜
        String baseTime = "1100";    //API 제공 시간을 입력하면 됨
        String type = "json";    //타입 xml, json 등등 ..
        String numOfRows = "150";    //한 페이지 결과 수

        //전날 23시 부터 153개의 데이터를 조회하면 오늘과 내일의 날씨를 알 수 있음


        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));    /* 타입 */
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));    /* 한 페이지 결과 수 */

        /*
         * GET방식으로 전송해서 파라미터 받아오기
         */
        URL url = new URL(urlBuilder.toString());
        //어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
        //System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
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
        System.out.println(result);


        // Json parser를 만들어 만들어진 문자열 데이터를 객체화
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(result);
        // response 키를 가지고 데이터를 파싱
        JSONObject parse_response = (JSONObject) obj.get("response");
        // response 로 부터 body 찾기
        JSONObject parse_body = (JSONObject) parse_response.get("body");
        // body 로 부터 items 찾기
        JSONObject parse_items = (JSONObject) parse_body.get("items");
        // items로 부터 itemlist 를 받기
        JSONArray parse_item = (JSONArray) parse_items.get("item");

        //날씨 코드 해석 Map
        Map<String, String> skyMap = Map.of(
                "1", "맑음",
                "3", "구름 많음",
                "4", "흐림"
        );

        //날씨 코드 해석 Map
        Map<String, String> PtyMap = Map.of(
                "0", "없음",
                "1", "비",
                "2", "비/눈",
                "3", "눈",
                "4", "소나기"
        );
        //최종 출력할 현재 날씨 정보 변수
        String currentTemperature = "N/A";
        String currentSkyStatus = "N/A";
        String currentPtyStatus = "N/A";
        String currentWsdStatus = "N/A";
        String fcstTimeKey = "1200";

        //배열을 순회하여 원하는 정보 (TMP, SKY)찾기
        for (int i = 0; i < parse_item.size(); i++) {
            JSONObject weatherItem = (JSONObject) parse_item.get(i);
            String category = (String) weatherItem.get("category");
            String fcstTime = (String) weatherItem.get("fcstTime");
            String fcstValue = weatherItem.get("fcstValue").toString();

            // 현재 시간(1200)의 값을 추출
            if (fcstTime.equals(fcstTimeKey)) {

                if (category.equals("TMP")) {
                    currentTemperature = fcstValue;
                }

                if (category.equals("SKY")) {
                    currentSkyStatus = skyMap.getOrDefault(fcstValue, "알 수 없음");
                }
                if (category.equals("PTY")) {
                    currentPtyStatus = PtyMap.getOrDefault(fcstValue, "알 수 없음");
                }
                if (category.equals("WSD")) {
                    currentWsdStatus = fcstValue;
                }
            }

            // --- 4. 결과 출력 ---
            System.out.println("\n===== 종로구 홍지동 날씨 정보 =====");
            System.out.printf("조회 기준 시각: %s %s\n", baseDate, baseTime);
            System.out.printf("예보 시각: %s (API 발표 후 첫 예보)\n", fcstTimeKey);
            System.out.println("---------------------------------");
            System.out.printf("현재 온도: %s ℃\n", currentTemperature);
            System.out.printf("하늘 상태: %s\n", currentSkyStatus);
            System.out.printf("강수: %s\n", currentPtyStatus);
            System.out.printf("풍속: %s\n", currentWsdStatus);
            System.out.println("=================================\n");
        }

    }
}