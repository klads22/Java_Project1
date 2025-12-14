package rain;

public class ExtractNumber {
	public static double extractNumber(String value) {

	    if (value == null) return 0.0;

	    String numberOnly = value.replaceAll("[^0-9.]", "");

	    if (numberOnly.isEmpty()) return 0.0;

	    return Double.parseDouble(numberOnly);
	}
}
