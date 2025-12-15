package getAPI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.net.HttpURLConnection;
import java.net.URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
// JSON 파싱을 위해 org.json.simple 라이브러리 사용
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetUVAPI {
	
	public static int currUV;
    public static int currUV03;
    public static int currUV06;
    public static int currUV09;
    public static int currUV12;
    public static int currUV15;
    public static int currUV18;
    public static int currUV21;

	public static void getUvIndexData() throws IOException, ParseException {
        String baseUrl = "https://apihub.kma.go.kr/api/typ02/openApi/LivingWthrIdxServiceV3/getUVIdxV3";
        String pageNo = "1";
        String numOfRows = "10";
        String dataType = "JSON"; // XML 대신 JSON으로 요청
        String areaNo = "1111000000"; // 종로구 홍지문 코드
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        String time = now.format(formatter);
        String authKey = "-eWo4MAxRhqlqODAMdYaDQ";

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

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response.toString());

            JSONObject apiResponse = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) apiResponse.get("body");

            JSONObject items = (JSONObject) body.get("items");
            JSONArray itemArray = (JSONArray) items.get("item");

            if (itemArray != null) {
                //System.out.println("\n--- 추출된 자외선 지수 정보 ---");
                for (Object itemObj : itemArray) {
                    JSONObject item = (JSONObject) itemObj;
                    String areaCode = (String) item.get("areaNo");
                    String date = (String) item.get("date");
                    String uvIndex00 = (String) item.get("h0");
                    String uvIndex03 = (String) item.get("h3");
                    String uvIndex06 = (String) item.get("h6");
                    String uvIndex09 = (String) item.get("h9");
                    String uvIndex12 = (String) item.get("h12");
                    String uvIndex15 = (String) item.get("h15");
                    String uvIndex18 = (String) item.get("h18");
                    String uvIndex21 = (String) item.get("h21");

                    //System.out.println("지역 코드: " + areaCode + ", 날짜: " + date + ", 15시 자외선 지수: " + uvIndex00);
                    currUV = Integer.parseInt(uvIndex00);
                    currUV03 = Integer.parseInt(uvIndex03);
                    currUV06 = Integer.parseInt(uvIndex06);
                    currUV09 = Integer.parseInt(uvIndex09);
                    currUV12 = Integer.parseInt(uvIndex12);
                    currUV15 = Integer.parseInt(uvIndex15);
                    currUV18 = Integer.parseInt(uvIndex18);
                    currUV21 = Integer.parseInt(uvIndex21);

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