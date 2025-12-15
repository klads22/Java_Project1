package rain;

public class CalculateRain {
	// 비가 확정적으로 오는 상황(=강수량)에서의 가중치 계산
	public int Calc_Rain(double rain) {
		if(rain == 0.0) { // 시간당 1mm 이하 - 가랑비 수준
			return 0;
		}
		else if(rain < 1.0) {
			return 15;
		}
		else if(rain < 3.0) { // 시간당 3mm 이하 - 우산이 필요없지만 어느정도 젖는 수준
			return 50;
		}
		else // 시간당 3mm 이상 - 우산이 필요한 시기 이후
			return 80;
	}
}

