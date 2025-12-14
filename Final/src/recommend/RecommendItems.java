package recommend;

import java.util.ArrayList;
import java.util.List;

public class RecommendItems {
	
	public void GetItems(int dust, int uv, int pop, double rain) {
		boolean hasRecommendation = false;
		List<String> recommendItems = new ArrayList<>();
		if (dust >= 80)
		{
			recommendItems.add("마스크");
			hasRecommendation = true;
		}
		if (uv >= 4)
		{
			recommendItems.add("자외선 차단제");
			hasRecommendation = true;
		}
		if (pop >= 30 || rain > 0.0)
		{
			recommendItems.add("우산");
			hasRecommendation = true;
		}
		if (hasRecommendation)
		{
			String result = String.join(", ", recommendItems);
			System.out.println("추천 물품은 " + result + "입니다.");
		}
	}
	
}
