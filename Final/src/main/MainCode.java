package main;

import getAPI.GetDustAPI;
import getAPI.GetWeatherAPI;
import getAPI.GetUVAPI;
import java.io.IOException;
import org.json.simple.parser.ParseException;

import java.time.LocalDate;
import java.util.Scanner;

public class MainCode {

	public static void main(String[] args) throws IOException, ParseException {

		Scanner scanner = new Scanner(System.in);
		
		apparentTemp.ApparentTemperature temperature = new apparentTemp.ApparentTemperature();
		fineDust.CalculateFineDust dust = new fineDust.CalculateFineDust();
		uvIndex.CalculateUVIndex UV = new uvIndex.CalculateUVIndex();
		rain.CalculateRain Rain = new rain.CalculateRain();
		getAPI.GetWeatherAPI currWeatherStatus = new GetWeatherAPI();
		getAPI.GetDustAPI currDustStatus = new GetDustAPI();
		getAPI.GetUVAPI currUVStatus = new GetUVAPI();
		recommend.RecommendCloth recommendCloth = new recommend.RecommendCloth();
		recommend.RecommendActivity Activity = new recommend.RecommendActivity();
		recommend.RecommendItems Items = new recommend.RecommendItems();
		recommend.PrintWarning Warning = new recommend.PrintWarning();
		
		currWeatherStatus.WeatherAPI();
		currDustStatus.DustAPI();
		currUVStatus.getUvIndexData();
		
		LocalDate now = LocalDate.now();
		int monthValue = now.getMonthValue(); // 날짜에서 월 정보 받아오기
		double temp;
		double[] temps = new double[6];
		
		//월별 체감온도 구분
		if (monthValue >= 5 || monthValue <= 9)//여름 체감온도(5월~9월)
			for(int idx = 0; idx < 6; idx++ )
			{
				temps[idx] = temperature.CalcSummer(GetWeatherAPI.tempResults[idx], GetWeatherAPI.humidResults[idx]);
			}
			
		else //겨울 체감온도(10월~익년 4월)
			for(int idx = 0; idx < 6; idx++ )
			{
				temps[idx] = temperature.CalcWinter(GetWeatherAPI.tempResults[idx],GetWeatherAPI.windResults[idx]);
			} // 겨울 체감온도식을 적용한 체감온도를 temps[]에 0(06시)부터 5(21시)에 각각 가중치 저장
		
		double currDust = dust.Calc_Dust(currDustStatus.currDust);
		int currUV = UV.Calc_UV(currUVStatus.currUV);
		
		System.out.println(" 시각   |  자외선지수 | ");
		System.out.println("────────────────────────────────────────────────────────────────────────────────────────");
		System.out.println(" 06:00 |  " + currUVStatus.currUV06 + "(" + UV.UVLevel + ")");
		System.out.println(" 09:00 |  " + currUVStatus.currUV09 + "(" + UV.UVLevel + ")");
		System.out.println(" 12:00 |  " + currUVStatus.currUV12 + "(" + UV.UVLevel + ")");
		System.out.println(" 15:00 |  " + currUVStatus.currUV15 + "(" + UV.UVLevel + ")");
		System.out.println(" 18:00 |  " + currUVStatus.currUV18 + "(" + UV.UVLevel + ")");
		System.out.println(" 21:00 |  " + currUVStatus.currUV21 + "(" + UV.UVLevel + ")");
		System.out.println("────────────────────────────────────────────────────────────────────────────────────────");
		System.out.println("미세먼지: " + (int)currDustStatus.currDust + "(" + dust.DustLevel + ")");
		System.out.println("────────────────────────────────────────────────────────────────────────────────────────");
		
		
		quit: while(true)
		{
			System.out.println("조회하고 싶은 시간의 번호를 입력해주세요");
			System.out.println("조회 가능 시간: 0번: 종료 | 1번: 06시 | 2번: 09시 | 3번: 12시 | 4번: 15시 | 5번: 18시 | 6번: 21시");
			System.out.print("입력: ");
			
			
			int choiceValue = Integer.parseInt(scanner.nextLine());
			int totalScore;
			
			
			switch(choiceValue) {
				case 0:
					break quit;
				case 1: // 1번(06시) 선택
					totalScore = (int)temps[0] + (int)currDustStatus.currDust + currUVStatus.currUV06
							+ Rain.Calc_Rain(GetWeatherAPI.PCPResults[0]);;
					Activity.RecommendAct(totalScore);
					recommendCloth.Cloth(temps[0]);
					Items.GetItems((int)currDustStatus.currDust, currUVStatus.currUV06,
							GetWeatherAPI.popResults[0] ,GetWeatherAPI.PCPResults[0]);
					Warning.Warning((int)currDustStatus.currDust, currUVStatus.currUV06,
							GetWeatherAPI.maxTemp, GetWeatherAPI.minTemp);
					break;
				case 2: // 2번(09시) 선택
					totalScore = (int)temps[1] + (int)currDustStatus.currDust + currUVStatus.currUV09
							+ Rain.Calc_Rain(GetWeatherAPI.PCPResults[1]);
					Activity.RecommendAct(totalScore);
					recommendCloth.Cloth(temps[1]);
					Items.GetItems((int)currDustStatus.currDust, currUVStatus.currUV09,
							GetWeatherAPI.popResults[1] ,GetWeatherAPI.PCPResults[1]);
					Warning.Warning((int)currDustStatus.currDust, currUVStatus.currUV09,
							GetWeatherAPI.maxTemp, GetWeatherAPI.minTemp);
					break;
				case 3: // 3번(12시) 선택
					totalScore = (int)temps[2] + (int)currDustStatus.currDust + currUVStatus.currUV12 + Rain.Calc_Rain(GetWeatherAPI.PCPResults[2]);
					Activity.RecommendAct(totalScore);
					recommendCloth.Cloth(temps[2]);
					Items.GetItems((int)currDustStatus.currDust, currUVStatus.currUV12, GetWeatherAPI.popResults[2] ,GetWeatherAPI.PCPResults[2]);
					Warning.Warning((int)currDustStatus.currDust, currUVStatus.currUV12, GetWeatherAPI.maxTemp, GetWeatherAPI.minTemp);
					break;
				case 4: // 4번(15시) 선택
					totalScore = (int)temps[3] + (int)currDustStatus.currDust + currUVStatus.currUV15 + Rain.Calc_Rain(GetWeatherAPI.PCPResults[3]);
					Activity.RecommendAct(totalScore);
					recommendCloth.Cloth(temps[3]);
					Items.GetItems((int)currDustStatus.currDust, currUVStatus.currUV15, GetWeatherAPI.popResults[3] ,GetWeatherAPI.PCPResults[3]);
					Warning.Warning((int)currDustStatus.currDust, currUVStatus.currUV15, GetWeatherAPI.maxTemp, GetWeatherAPI.minTemp);
					break;
				case 5: // 5번(18시) 선택
					totalScore = (int)temps[4] + (int)currDustStatus.currDust + currUVStatus.currUV18  + Rain.Calc_Rain(GetWeatherAPI.PCPResults[4]);
					Activity.RecommendAct(totalScore);
					recommendCloth.Cloth(temps[4]);
					Items.GetItems((int)currDustStatus.currDust, currUVStatus.currUV18, GetWeatherAPI.popResults[4] ,GetWeatherAPI.PCPResults[4]);
					Warning.Warning((int)currDustStatus.currDust, currUVStatus.currUV18, GetWeatherAPI.maxTemp, GetWeatherAPI.minTemp);
					break;
				case 6: // 6번(21시) 선택
					totalScore = (int)temps[5] + (int)currDustStatus.currDust + currUVStatus.currUV21  + Rain.Calc_Rain(GetWeatherAPI.PCPResults[5]);
					Activity.RecommendAct(totalScore);
					recommendCloth.Cloth(temps[5]);
					Items.GetItems((int)currDustStatus.currDust, currUVStatus.currUV21, GetWeatherAPI.popResults[5] ,GetWeatherAPI.PCPResults[5]);
					Warning.Warning((int)currDustStatus.currDust, currUVStatus.currUV21, GetWeatherAPI.maxTemp, GetWeatherAPI.minTemp);
					break;
				default:
					System.out.println("다시 입력해주세요.");
					break;
			}
			System.out.println("────────────────────────────────────────────────────────────────────────────────────────");
		}
	}

}