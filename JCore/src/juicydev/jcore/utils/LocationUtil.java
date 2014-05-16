package juicydev.jcore.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LocationUtil {

	/**
	 * Draw a line of locations between two locations
	 * 
	 * @param loc1
	 *            Start location
	 * @param loc2
	 *            Finish location
	 * @return List of locations in line (step size 1)
	 */
	public static List<Location> drawLine(Location loc1, Location loc2) {
		return drawLine(loc1, loc2, 1.0D);
	}

	/**
	 * Draw a line of locations between two locations with the specified step
	 * size
	 * 
	 * @param loc1
	 *            Start location
	 * @param loc2
	 *            Finish location
	 * @param step
	 *            Size of step between locations along line
	 * @return List of locations in line
	 */
	public static List<Location> drawLine(Location loc1, Location loc2,
			double step) {
		List<Location> line = new ArrayList<Location>();
		double dx = Math.max(loc1.getX(), loc2.getX())
				- Math.min(loc1.getX(), loc2.getX());
		double dy = Math.max(loc1.getY(), loc2.getY())
				- Math.min(loc1.getY(), loc2.getY());
		double dz = Math.max(loc1.getZ(), loc2.getZ())
				- Math.min(loc1.getZ(), loc2.getZ());
		double x1 = loc1.getX();
		double x2 = loc2.getX();
		double y1 = loc1.getY();
		double y2 = loc2.getY();
		double z1 = loc1.getZ();
		double z2 = loc2.getZ();
		double x = 0;
		double y = 0;
		double z = 0;
		double i = 0;
		double d = Math.abs(step);
		switch (findHighest(dx, dy, dz)) {
		case 1:
			i = 0;
			d = step;
			if (x1 > x2)
				d = -1 * step;
			x = loc1.getX();
			do {
				i += step;
				y = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
				z = z1 + (x - x1) * (z2 - z1) / (x2 - x1);
				line.add(new Location(loc1.getWorld(), x, y, z));
				x += d;
			} while (i <= Math.max(x1, x2) - Math.min(x1, x2));
			break;
		case 2:
			i = 0;
			d = step;
			if (y1 > y2)
				d = -1 * step;
			y = loc1.getY();
			do {
				i += step;
				x = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
				z = z1 + (y - y1) * (z2 - z1) / (y2 - y1);
				line.add(new Location(loc1.getWorld(), x, y, z));
				y += d;
			} while (i <= Math.max(y1, y2) - Math.min(y1, y2));
			break;
		case 3:
			i = 0;
			d = step;
			if (z1 > z2)
				d = -1 * step;
			z = loc1.getZ();
			do {
				i += step;
				y = y1 + (z - z1) * (y2 - y1) / (z2 - z1);
				x = x1 + (z - z1) * (x2 - x1) / (z2 - z1);
				line.add(new Location(loc1.getWorld(), x, y, z));
				z += d;
			} while (i <= Math.max(z1, z2) - Math.min(z1, z2));
		}

		return line;
	}

	/**
	 * Draw a line of locations along the line of sight from the player's hand
	 * 
	 * @param player
	 *            Player
	 * @param maxDistance
	 *            Maximum distance to draw
	 * @param step
	 *            Size of step between locations along line
	 * @return List of locations in line
	 */
	public static List<Location> getLineOfSightHand(Player player,
			int maxDistance, double step) {
		float newYaw = player.getEyeLocation().getYaw() + 40F;
		Location loc1 = player.getEyeLocation().clone();
		loc1.setYaw(newYaw);
		Vector v = loc1.getDirection().normalize().multiply(0.5);
		loc1 = loc1.add(v);

		Location loc2 = getTargetLocation(player, maxDistance);

		return drawLine(loc1, loc2, step);
	}

	/**
	 * Draw a line of locations along the line of sight of the player
	 * 
	 * @param player
	 *            Player
	 * @param maxDistance
	 *            Maximum distance to draw
	 * @param step
	 *            Size of step between locations along line
	 * @return List of locations in line
	 */
	public static List<Location> getLineOfSight(Player player, int maxDistance,
			double step) {
		Location loc1 = player.getEyeLocation();
		Location loc2 = getTargetLocation(player, maxDistance);

		return drawLine(loc1, loc2, step);
	}

	/**
	 * Get the exact location of the player's target
	 * 
	 * @param player
	 *            Player
	 * @param maxDistance
	 *            Maximum distance to look
	 * @return Exact location of the player's target
	 */
	public static Location getTargetLocation(Player player, int maxDistance) {
		Location loc = player.getEyeLocation();

		Vector v = loc.getDirection().normalize().multiply(0.1);

		for (double d = 0.0; d < maxDistance; d += 0.1) {
			loc.add(v);
			if (!loc.getBlock().getType().equals(Material.AIR))
				return loc;
		}

		return loc;
	}

	private static int findHighest(double x, double y, double z) {
		if ((x >= y) && (x >= z))
			return 1;
		if ((y >= x) && (y >= z))
			return 2;
		return 3;
	}
}
