package juicydev.jcore.utils;

/**
 * @author JuicyDev
 */
public class MathsUtil {

	public static double cirum(double radius) {
		return 2 * Math.PI * Math.abs(radius);
	}

	public static double perimeterEllipse(double a, double b) {
		a = Math.abs(a);
		b = Math.abs(b);

		if (a == b) {
			return cirum(a);
		} else if (a == 0) {
			return 4 * b;
		} else if (b == 0) {
			return 4 * a;
		} else {
			double d1 = Math.PI * (a + b);
			double d2 = sq(a - b);
			double d3 = sq(a + b);

			double sqrt = Math.sqrt(((-3) * (d2 / d3)) + 4);
			double d4 = d2 / (d3 * (sqrt + 10));
			return d1 * ((3 * d4) + 1);
		}
	}

	public static double sq(double d) {
		return power(d, 2);
	}

	public static double power(double d, int power) {
		if (power == 0) {
			return 1;
		} else if (power == 1) {
			return d;
		} else if (power < 0) {
			return 1 / power(d, Math.abs(power));
		}
		double ans = d;
		for (int i = 1; i < power; i++) {
			ans *= d;
		}
		return ans;
	}

	public static int roundUp(double from, int step) {
		if (step == 0)
			return (int) Math.ceil(from);
		return (int) (Math.ceil(from / step) * step);
	}
}
