package main;

import getAPI.GetDustAPI;
import getAPI.GetWeatherAPI;
import getAPI.GetUVAPI;
import java.io.IOException;
import org.json.simple.parser.ParseException;

public class MainCode {

	public static void main(String[] args) throws IOException, ParseException {

		recommend.RecommendActivity Activity = new recommend.RecommendActivity();
		apparentTemp.ApparentTemperature temperature = new apparentTemp.ApparentTemperature();
		fineDust.CalculateFineDust dust = new fineDust.CalculateFineDust();
		uvIndex.CalculateUVIndex UV = new uvIndex.CalculateUVIndex();
		getAPI.GetWeatherAPI currWeatherStatus = new GetWeatherAPI();
		getAPI.GetDustAPI currDustStatus = new GetDustAPI();
		getAPI.GetUVAPI currUVStatus = new GetUVAPI();
		recommend.RecommendCloth recommendCloth = new recommend.RecommendCloth();
		
		currWeatherStatus.WeatherAPI();
		currDustStatus.DustAPI();
		currUVStatus.getUvIndexData();
		
		
		double temp = temperature.CalcWinter(currWeatherStatus.currTemp,currWeatherStatus.currWsd);
		double currDust = dust.Calc_Dust(currDustStatus.currDust);
		int currUV = UV.Calc_UV(currUVStatus.currUV);
		//System.out.println("현재 온도 가중치: " + temp);
		//System.out.println("현재 온도: " + currWeatherStatus.currTemp);
		//System.out.println("현재 미세먼지 가중치: " + currDust);
		//System.out.println("현재 자외선 가중치: " + currUV);
		System.out.println(" 시각  |  자외선지수 | ");
		System.out.println("─────────────────────");

		//System.out.println(" 00:00 |	 " + currUVStatus.currUV + "(" + UV.UVLevel + ")");
		//System.out.println(" 03:00 |	 " + currUVStatus.currUV03 + "(" + UV.UVLevel + ")");
		System.out.println(" 06:00 |	 " + currUVStatus.currUV06 + "(" + UV.UVLevel + ")");
		System.out.println(" 09:00 |	 " + currUVStatus.currUV09 + "(" + UV.UVLevel + ")");
		System.out.println(" 12:00 |	 " + currUVStatus.currUV12 + "(" + UV.UVLevel + ")");
		System.out.println(" 15:00 |	 " + currUVStatus.currUV15 + "(" + UV.UVLevel + ")");
		System.out.println(" 18:00 |	 " + currUVStatus.currUV18 + "(" + UV.UVLevel + ")");
		System.out.println(" 21:00 |	 " + currUVStatus.currUV21 + "(" + UV.UVLevel + ")");
		System.out.println("─────────────────────");

		System.out.println("미세먼지: " + (int)currDustStatus.currDust + "(" + dust.DustLevel + ")");

		int totalScore = (int)temp + (int)currDust + currUV;
		System.out.println("===============================");
		Activity.RecommendAct(totalScore);
		recommendCloth.Cloth(temp);
		
		
	}

}
