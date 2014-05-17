package juicydev.jcore.utils;

/**
 * @author JuicyDev
 */
public class MathsUtil {

	public static int roundUp(int from, int size) {
		if (size == 0)
			return from;
		return (int) (Math.ceil(from / size) * size);
	}

	public static int roundUp(double from, int size) {
		if (size == 0)
			return (int) Math.ceil(from);
		return (int) (Math.ceil(from / size) * size);
	}
}
