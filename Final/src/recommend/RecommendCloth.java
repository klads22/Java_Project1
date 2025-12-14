package recommend;

public class RecommendCloth {
	
	public void Cloth(double temperature) { 
		if(temperature <= 9.0) 
		{
			System.out.println("추천 의상은 두꺼운 외투, 패딩, 기모 후드티입니다.");
		}
		else if(temperature <= 13.0)
		{
			System.out.println("추천 의상은 코트, 항공 점퍼입니다.");

		}
		else if(temperature <= 17.0)
		{
			System.out.println("추천 의상은 얇은 재킷, 블레이저입니다.");

		}
		else if(temperature <= 24.0)
		{
			System.out.println("추천 의상은 긴팔 티셔츠, 맨투맨, 후드티입니다.");
		}
		else
			System.out.println("추천 의상은 얇은 셔츠, 반팔 옷입니다.");
		
		
	}
}
	
