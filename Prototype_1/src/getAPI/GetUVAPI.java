package getAPI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;

// JSON 파싱을 위해 org.json.simple 라이브러리 사용
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetUVAPI {
	
	public static int currUV;
	
	public static void getUvIndexData() throws IOException, ParseException {
        String baseUrl = "https://apihub.kma.go.kr/api/typ02/openApi/LivingWthrIdxServiceV3/getUVIdxV3";
        String pageNo = "1";
        String numOfRows = "10";
        String dataType = "JSON"; // XML 대신 JSON으로 요청
        String areaNo = "1111000000"; // 종로구 홍지문 코드
        //String time = "2025112218"; //
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        String time = now.format(formatter);
        String authKey = "-eWo4MAxRhqlqODAMdYaDQ";
        
        int hour = now.getHour();
        int hValue = (hour/3)*3;
        String hKey = String.format("h%02d", hValue);

        String urlString = String.format("%s?pageNo=%s&numOfRows=%s&dataType=%s&areaNo=%s&time=%s&authKey=%s",
                baseUrl, pageNo, numOfRows, dataType, areaNo, time, authKey);

        URL url = new URL(urlString);
        //System.out.println("요청 URL: " + urlString); // 최종 요청 URL 확인

        HttpURLConnection con = null;
        BufferedReader in = null;

        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json"); // JSON 응답을 기대함을 표시

            int responseCode = con.getResponseCode();
            //System.out.println("\n응답 코드: " + responseCode); // 200이면 성공

            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else { // 오류 응답 처리
                in = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // 3. JSON 문자열 파싱
            //System.out.println("\n--- API 응답 (JSON 문자열) ---");
            //System.out.println(response.toString());

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response.toString());

            JSONObject apiResponse = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) apiResponse.get("body");
            /*if (body == null) {
                System.out.println("API body is null");
                return;
            }*/
            JSONObject items = (JSONObject) body.get("items");
            JSONArray itemArray = (JSONArray) items.get("item");

            if (itemArray != null) {
                //System.out.println("\n--- 추출된 자외선 지수 정보 ---");
                for (Object itemObj : itemArray) {
                    JSONObject item = (JSONObject) itemObj;
                    String areaCode = (String) item.get("areaNo");
                    String date = (String) item.get("date");
                    String uvIndex00 = (String) item.get(hKey);

                    //System.out.println("지역 코드: " + areaCode + ", 날짜: " + date + ", 15시 자외선 지수: " + uvIndex00);
                    currUV = Integer.parseInt(uvIndex00);
                }
            } else {
                System.out.println("API 응답에 유효한 자외선 지수 데이터(item)가 없습니다.");
            }

        } finally {
            // 5. 자원 정리
            if (in != null) {
                in.close();
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        getUvIndexData();
    }
}
