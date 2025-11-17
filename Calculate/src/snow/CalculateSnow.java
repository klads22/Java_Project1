package snow;

public class CalculateSnow {
	public int Calc_Snow(double snow) { // 시간당 적설량에 따른 가중치 if문
		if(snow < 1.0) {
			return 5;
		}
		else if(snow < 3.0) {
			return 20;
		}
		else if(snow < 5.0) {
			return 45;
		}
		else if(snow < 8.0) {
			return 65;
		}
		else {
			return 90;
		}
	}
}
