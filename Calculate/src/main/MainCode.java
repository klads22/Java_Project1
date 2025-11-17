package main;

//import ApparentTemp.apparentTemperature;
//import FineDust.*;
//import rain.*;
//import snow.*;
//import UVIndex.*;

public class MainCode {

	public static void main(String[] args) {

		ApparentTemp.ApparentTemperature temperature = new ApparentTemp.ApparentTemperature();
		double temp1 = temperature.CalcWinter(-3, 10);
		//double temp2 = temperature.CalcSummer(35, 70);
		System.out.println((int)temp1);
		//System.out.println((int)temp2);
		
		FineDust.CalculateFineDust dust = new FineDust.CalculateFineDust();
		int currDust = dust.Calc_Dust(50);
		System.out.println(currDust);
		
		uvIndex.CalculateUVIndex UV = new uvIndex.CalculateUVIndex();
		int currUV = UV.Calc_UV(4);
		System.out.println(currUV);
		
		activity.RecommendActivity Activity = new activity.RecommendActivity();
		int totalScore = (int)temp1 + currDust + currUV;
		Activity.RecommendAct(totalScore);
	}

}
