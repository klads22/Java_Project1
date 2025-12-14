package fineDust;

public class CalculateFineDust {
	
	public static String DustLevel;
	
	public int Calc_Dust(double dust) { // 미세먼지 수치에 따른 가중치 if문
		if(dust >= 0 && dust <=30) {
			DustLevel = "좋음";
			return 0;
		}
		else if(dust <= 80){
			DustLevel = "보통";
			return 20;
		}
		else if(dust<=150){
			DustLevel = "나쁨";
			return 50;
		}
		else {
			DustLevel = "매우나쁨";
			return 100;
		}
	}
}
