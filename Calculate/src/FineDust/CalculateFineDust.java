package FineDust;

public class CalculateFineDust {
	public int Calc_Dust(int dust) { // 미세먼지 수치에 따른 가중치 if문
		if(dust >= 0 && dust <=30) {
			return 0;
		}
		else if(dust <= 80){
			return 20;
		}
		else if(dust<=150){
			return 50;
		}
		else {
			return 100;
		}
	}
}
