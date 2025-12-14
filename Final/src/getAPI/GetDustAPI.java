package getAPI;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetDustAPI {
	
	public static double currDust;
	
	public static void DustAPI() throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /*URL*/
        urlBuilder.append("/" +  URLEncoder.encode("55536b69756c65653634584f6c4757","UTF-8") ); /*인증키 (sample사용시에는 호출시 제한됩니다.)*/
        urlBuilder.append("/" +  URLEncoder.encode("json","UTF-8") ); /*요청파일타입 (xml,xmlf,xls,json) */
        urlBuilder.append("/" + URLEncoder.encode("RealtimeCityAir","UTF-8")); /*서비스명 (대소문자 구분 필수입니다.)*/
        urlBuilder.append("/" + URLEncoder.encode("1","UTF-8")); /*요청시작위치 (sample인증키 사용시 5이내 숫자)*/
        urlBuilder.append("/" + URLEncoder.encode("5","UTF-8")); /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨)*/
        // 상위 5개는 필수적으로 순서바꾸지 않고 호출해야 합니다.

        // 서비스별 추가 요청 인자이며 자세한 내용은 각 서비스별 '요청인자'부분에 자세히 나와 있습니다.
        urlBuilder.append("/" + URLEncoder.encode("도심권","UTF-8")); /* 서비스별 추가 요청인자들*/
        urlBuilder.append("/" + URLEncoder.encode("종로구","UTF-8")); /* 측정소명 (MSRSTN_NM) */


        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");
        //System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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
        //System.out.println(sb.toString());

        // 1. JSONParser 객체 생성
        JSONParser parser = new JSONParser();

        // sb.toString()에 담긴 JSON 문자열을 파싱하여 최상위 객체(Object)로 변환
        JSONObject jsonObject = (JSONObject) parser.parse(sb.toString());

        // 2. 원하는 데이터가 있는 계층으로 접근 (API 응답 구조 확인 필수!)
        // 서울시 대기정보 API의 JSON 구조: { "RealtimeCityAir": { "row": [ {...}, {...} ] } }

        // 2-1. "RealtimeCityAir" 키로 접근하여 두 번째 계층의 JSONObject를 얻습니다.
        JSONObject realtimeCityAir = (JSONObject) jsonObject.get("RealtimeCityAir");

        // 2-2. "row" 키로 접근하여 실제 데이터 목록이 담긴 JSONArray를 얻습니다.
        // "row"는 측정소별 데이터 객체(JSONObject)들의 리스트입니다.
        JSONArray rowArray = (JSONArray) realtimeCityAir.get("row");

        // 3. 배열(rowArray)을 순회하며 각 측정소의 데이터 추출
        //System.out.println("--- 추출된 실시간 대기 정보 ---");

        for (Object item : rowArray) {
            JSONObject dustData = (JSONObject) item;

            // 1단계: 추출하고자 하는 Object를 가져옵니다.
            Object pmObj = dustData.get("PM");
            String msrstnNm = (String) dustData.get("MSRSTN_NM");

            // 2단계: Null 체크 및 안전한 변환
            String pm10;
            if (pmObj != null) {
                try {
                    // Object를 String으로 변환 후, Double로 파싱합니다.
                    // 이 방식은 값이 Long(정수)이든 Double(실수)이든 모두 처리할 수 있습니다.
                    double doubleValue = Double.parseDouble(pmObj.toString());
                    currDust = doubleValue;
                    pm10 = Double.toString(doubleValue);
                } catch (NumberFormatException e) {
                    // 만약 "데이터없음" 같은 문자열이 들어오면 'N/A'로 처리합니다.
                    pm10 = "N/A";
                }
            } else {
                // 값이 null이면 'N/A'로 처리합니다.
                pm10 = "N/A";
            }

            // 5. 추출된 데이터 출력
            /*System.out.println("------------------------------------");
            System.out.println("측정소: " + msrstnNm);
            System.out.println("미세먼지(PM10): " + pm10 + " µg/m³");
            // ... (다른 항목도 같은 방식으로 처리하면 됩니다.)
            System.out.println("------------------------------------");*/
            
            
            
        }
        
    }
}