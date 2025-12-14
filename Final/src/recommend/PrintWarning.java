package recommend;

import java.util.ArrayList;
import java.util.List;

public class PrintWarning {
	public void Warning(int dust, int uv, double max, double min) {
		boolean hasWarning = false;
		List<String> warningLists = new ArrayList<>();
		if (dust >= 80)
		{
			warningLists.add("미세먼지");
			hasWarning = true;
		}
		if (uv >= 4)
		{
			warningLists.add("자외선지수");
			hasWarning = true;
		}
		if (max - min >= 10)
		{
			warningLists.add("일교차");
			hasWarning = true;
		}
		if (hasWarning)
		{
			String result = String.join(", ", warningLists);
			System.out.println("오늘 날씨에서 유의해야 할 점은 " + result + "입니다.");
		}
	}
}
