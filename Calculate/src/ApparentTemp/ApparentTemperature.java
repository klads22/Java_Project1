package ApparentTemp;

import java.lang.Math;
//import ApparentTemp.CalculateWeight;

public class ApparentTemperature { 
	public double CalcWinter(double temp, double wind) // 겨울철 체감온도 계산식
	{
		double total = 13.12 + (0.6215*temp) - (11.37*Math.pow(wind, 0.16)) + (0.3965*temp*Math.pow(wind, 0.16));
		return CalculateWeight.Calc_Weight(total);
	}
	
	public double CalcSummer(double temp, double humid) // 여름철 체감온도 계산식
	{
		double tw = getTw(temp, humid);
		double total = -0.2442 + (0.55399 * tw) + (0.45535 * temp) - (0.0022 * Math.pow(tw, 2.0)) + (0.00278 * tw * temp) + 3.0;
		return CalculateWeight.Calc_Weight(total);

	}
	public double getTw(double ta, double rh) { // 여름철 체감온도 계산에 필요한 상대습도(Tw) 계산식
        return ta * Math.atan(0.151977 * Math.pow(rh + 8.313659, 0.5)) +
                Math.atan(ta + rh) - Math.atan(rh - 1.67633) +
                (0.00391838 * Math.pow(rh, 1.5) * Math.atan(0.023101 * rh)) - 4.686035;
	}
	// 봄, 가을의 체감온도는 두 계산식에 넣어도 비슷한 결과가 출력됨
}
