package juicydev.jcore.utils.location;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Used by my location utility class to define the dimensions of shapes
 * 
 * @author JuicyDev
 */
public class Dim implements Cloneable, Serializable {

	private static Random random = new Random();
	private static final long serialVersionUID = 1804990558088792800L;
	private double x;
	private double y;
	private double z;

	public Dim() {
		setX(0.0D);
		setY(0.0D);
		setZ(0.0D);
	}

	public Dim(int x, int y, int z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	public Dim(double x, double y, double z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public Dim zero() {
		setX(0.0D);
		setY(0.0D);
		setZ(0.0D);
		return this;
	}

	public static Dim getMinimum(Dim d1, Dim d2) {
		return new Dim(Math.min(d1.x, d2.x), Math.min(d1.y, d2.y), Math.min(
				d1.z, d2.z));
	}

	public static Dim getMaximum(Dim d1, Dim d2) {
		return new Dim(Math.max(d1.x, d2.x), Math.max(d1.y, d2.y), Math.max(
				d1.z, d2.z));
	}

	public static Dim getRandom(double max) {
		return new Dim(random.nextDouble() * max, random.nextDouble() * max,
				random.nextDouble() * max);
	}

	public Dim copy(Dim dim) {
		setX(dim.getX());
		setY(dim.getY());
		setZ(dim.getZ());
		return this;
	}

	public Dim clone() {
		try {
			return ((Dim) super.clone());
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	public Map<String, Object> serialize() {
		Map<String, Object> result = new LinkedHashMap<String, Object>();

		result.put("x", Double.valueOf(getX()));
		result.put("y", Double.valueOf(getY()));
		result.put("z", Double.valueOf(getZ()));

		return result;
	}

	public static Dim deserialize(Map<String, Object> args) {
		double x = 0.0D;
		double y = 0.0D;
		double z = 0.0D;

		if (args.containsKey("x")) {
			x = ((Double) args.get("x")).doubleValue();
		}
		if (args.containsKey("y")) {
			y = ((Double) args.get("y")).doubleValue();
		}
		if (args.containsKey("z")) {
			z = ((Double) args.get("z")).doubleValue();
		}

		return new Dim(x, y, z);
	}
}