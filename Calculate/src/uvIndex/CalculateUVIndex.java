package uvIndex;

public class CalculateUVIndex {
	public int Calc_UV(int uv) { // 자외선 지수에 따른 가중치 if문
		if(uv < 3) {
			return 0;
		}
		else if(uv < 6) {
			return 15;
		}
		else if(uv < 8) {
			return 40;
		}
		else if(uv < 11) {
			return 75;
		}
		else {
			return 120;		
		}	
	}
}
// https://www.weather.go.kr/w/theme/daily-life/life-weather-index.do
// 생활기상지수 - 자외선지수
