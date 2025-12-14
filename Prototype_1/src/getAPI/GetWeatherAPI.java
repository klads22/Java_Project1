package getAPI;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Arrays;
import java.util.*;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.time.LocalDate;
import java.time.LocalTime;

public class GetWeatherAPI {
	
	public static double currTemp;
	public static double currSky;
	public static double currPty;
	public static double currWsd;
    public static double currPcp;
    public static double maxTemp;
    public static double minTemp;

    private static final String[] TARGET_TIMES = {"0600", "0900", "1200", "1500", "1800", "2100"};

	public static void WeatherAPI() throws IOException, ParseException {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        DateTimeFormatter formatterD = DateTimeFormatter.ofPattern("yyyyMMdd");  //추출한 날짜를 yyyyMMdd 형식으로 포맷팅
        String formatedDate = date.format(formatterD);   //포맷 적용

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String serviceKey = "8cfcf016d3df4e6d3e13bd8a5dcc1a95d8fc404c96c5c2fe3c4ffca9fe801390"; // 인증키
        String nx = "60";    //위도
        String ny = "127";    //경도 해당 좌표는 상명대학교 종로구 홍지동 좌표임
        String baseDate = formatedDate;    //조회하고싶은 날짜
        String baseTime = "0500";    //API 제공 시간을 입력하면 됨
        String type = "json";    //타입 xml, json 등등 ..
        String numOfRows = "250";    //한 페이지 결과 수

        //전날 23시 부터 153개의 데이터를 조회하면 오늘과 내일의 날씨를 알 수 있음


        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));    /* 타입 */
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));    /* 한 페이지 결과 수 */


        URL url = new URL(urlBuilder.toString());
        //어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
        //System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        //System.out.println("Response code: " + conn.getResponseCode());
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
        
        //System.out.println(result);


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


        //Tree맵을 이용해서 시간 순서대로 정렬
        Map<String, Map<String, String>> weatherTime = new TreeMap<>();

        //배열을 순회하여 원하는 정보 (TMP, SKY)찾기
        for (int i = 0; i < parse_item.size(); i++) {
            JSONObject weatherItem = (JSONObject) parse_item.get(i);
            String category = (String) weatherItem.get("category");
            String fcstTime = (String) weatherItem.get("fcstTime");
            String fcstValue = weatherItem.get("fcstValue").toString();

            // 07시부터 3시간 간격으로 데이터 수집
            if (Arrays.asList(TARGET_TIMES).contains(fcstTime)) {
                // 해당 시간에 대한 맵이 없으면 새로 생성
                weatherTime.putIfAbsent(fcstTime, new HashMap<>());
                // 해당 시간의 Map을 가져와서 카테고리와 값을 저장
                Map<String, String> valueMap = weatherTime.get(fcstTime);

                // Map에 값을 저장하도록 변경
                if (category.equals("TMP")) {
                    valueMap.put("TMP", fcstValue);
                }

                if (category.equals("SKY")) {
                    valueMap.put("SKY", skyMap.getOrDefault(fcstValue, "알 수 없음"));
                }
                if (category.equals("PTY")) {
                    valueMap.put("PTY", PtyMap.getOrDefault(fcstValue, "알 수 없음"));
                }
                if (category.equals("WSD")) {
                    valueMap.put("WSD", fcstValue);
                }
                if (category.equals("REH")) {
                    valueMap.put("REH", fcstValue);
                }
                if (category.equals("PCP")) {
                    valueMap.put("PCP", fcstValue);
                }

            }
            if (category.equals("TMX")) {
                maxTemp = Double.parseDouble(fcstValue);
            }

            if (category.equals("TMN")) {
                minTemp = Double.parseDouble(fcstValue);
            }
        }
        System.out.println("\n===== 종로구 홍지동 날씨 예보 (06:00부터 3시간 간격) =====");
        System.out.printf("기준 날짜: %s, 기준 예보시각: %s\n", baseDate, baseTime);

        System.out.println("────────────────────────────────────────────────────────────────────────────────────────");
        System.out.println(" 시각  |  온도(℃) |    하늘상태    |  풍속(m/s)  |   습도   |  강수확률(형태)  |  강수량");
        System.out.println("────────────────────────────────────────────────────────────────────────────────────────");

        // TreeMap을 순회하며 결과를 출력
        for (String timeKey : weatherTime.keySet()) {
            Map<String, String> values = weatherTime.get(timeKey);

            // 데이터 추출
            String currentTemperature = values.getOrDefault("TMP", "-");
            String currentSkyStatus = values.getOrDefault("SKY", "N/A");
            String currentPtyStatus = values.getOrDefault("PTY", "N/A");
            String currentWsdStatus = values.getOrDefault("WSD", "-");
            String currentRehStatus = values.getOrDefault("REH", "-");
            String currentPcpStatus = values.getOrDefault("PCP", "N/A");
            // 시간 포맷팅
            String formattedTimeStr = timeKey.substring(0, 2) + ":" + timeKey.substring(2);


            System.out.printf("%s  | %7s | %-12s | %12s | %7s | %-12s | %-6s\n",
                    formattedTimeStr, currentTemperature, currentSkyStatus, currentWsdStatus, currentRehStatus, currentPtyStatus, currentPcpStatus);

            currTemp = Double.parseDouble(currentTemperature);
            currWsd = Double.parseDouble(currentWsdStatus);
        }
        System.out.println("──────────────────────────────────────────────────────────────────");

    }
}