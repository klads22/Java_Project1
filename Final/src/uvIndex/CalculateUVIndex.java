package uvIndex;

public class CalculateUVIndex {
	
	public static String UVLevel;
	
	public int Calc_UV(int uv) { // 자외선 지수에 따른 가중치 if문
		if(uv < 3) {
			UVLevel = "낮음";
			return 0;
		}
		else if(uv < 6) {
			UVLevel = "보통";
			return 15;
		}
		else if(uv < 8) {
			UVLevel = "높음";
			return 40;
		}
		else if(uv < 11) {
			UVLevel = "매우높음";
			return 75;
		}
		else {
			UVLevel = "위험";
			return 120;		
		}	
	}
}
// https://www.weather.go.kr/w/theme/daily-life/life-weather-index.do
// 생활기상지수 - 자외선지수
