package ApparentTemp;

public class CalculateWeight {
	public static int Calc_Weight(double temperature) { // 체감온도에 따른 통합형 가중치 if문
		if(temperature <= -15.0 || temperature >=36)
		{
			return 75;
		}
		else if(temperature <= -10.0 || temperature >= 33)
		{
			return 50;
		}
		else if(temperature <= -5.0 || temperature >= 30)
		{
			return 30;
		}
		else
		{
			return 15;
		}
	
	}
}
