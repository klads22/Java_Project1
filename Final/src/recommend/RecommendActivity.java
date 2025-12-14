package recommend;

public class RecommendActivity {
	public void RecommendAct(int score) { // 모든 요소에서의 가중치를 더하여 최종적으로 활동을 추천하는 메소드
		if(score >= 100) {
			System.out.println("추천 활동은 실내활동입니다.");
		}
		else if(score >= 60)
		{
			System.out.println("추천 활동은 실내활동 또는 단시간 실외활동입니다.");
		}
		else {
			System.out.println("추천 활동은 모든 활동입니다.");
		}
	}
}
