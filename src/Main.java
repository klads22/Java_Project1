// Main.java 파일 (수정)

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;

import getAPI.GetWeatherAPI;
import getAPI.GetWeatherAPI.WeatherResult;
import getAPI.WeatherForecast;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        // 1. GetWeatherAPI 클래스에서 정제된 데이터 객체를 받아옵니다.
        WeatherResult result = GetWeatherAPI.getWeatherRange();
        List<WeatherForecast> forecasts = result.forecasts;

        // 2. 결과 출력
        System.out.println("\n===== 📢 종로구 홍지동 날씨 예보 출력 (Main 클래스 담당) =====");
        System.out.printf("예보 기준 시각: %s %s (프로그램 실행 시점 기준 최신)\n", result.baseDate, result.baseTime);
        System.out.println("-------------------------------------------------------");

        if (forecasts.isEmpty()) {
            System.out.println("조회된 날씨 데이터가 없습니다. (API 오류 또는 범위 미일치)");
            System.out.println("-------------------------------------------------------");
            return;
        }

        System.out.println(" 시각  |  온도(℃) |    하늘상태    |  강수확률(형태)  |  풍속(m/s)");
        System.out.println("-------------------------------------------------------");

        // 3. 반환받은 List를 순회하며 출력합니다.
        for (WeatherForecast wf : forecasts) {
            System.out.printf(" %s | %5s   | %-8s | %-8s | %5s\n",
                    wf.time, wf.temperature, wf.skyStatus, wf.ptyStatus, wf.windSpeed);
        }
        System.out.println("-------------------------------------------------------");
    }
}