package recommend;

public class RecommendActivity {
	public void RecommendAct(int score) {// 모든 요소에서의 가중치를 더하여 최종적으로 활동을 추천하는 메소드
		System.out.println("────────────────────────────────────────────────────────────────────────────────────────");
		if(score >= 100) {
			System.out.println("해당 시간은 외부활동을 권장하지 않습니다. 실내 활동을 추천합니다.");
		}
		else if(score >= 60)
		{
			System.out.println("해당 시간은 단시간의 실외활동을 권장합니다. 주로 실내활동을 추천합니다.");
		}
		else {
			System.out.println("해당 시간은 외부활동에 적합한 날씨입니다.");
		}
	}
}