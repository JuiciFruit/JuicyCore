package juicydev.jcore.utils.location;

import java.util.ArrayList;
import java.util.List;

import juicydev.jcore.utils.MathsUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Utility class with methods for drawing accurate shapes of locations.
 * <p>
 * Useful for plotting effects where a BlockIterator or Player.getLineOfSight()
 * is just not accurate enough
 * 
 * @author JuicyDev
 */
public class LocationUtil {

	/**
	 * Internal method for determining the largest of 3 doubles
	 */
	private static int findHighest(double x, double y, double z) {
		if ((x >= y) && (x >= z))
			return 1;
		if ((y >= x) && (y >= z))
			return 2;
		return 3;
	}

	/**
	 * Internal method for determining the smallest of 3 doubles
	 */
	private static int findSmallest(double x, double y, double z) {
		if ((x <= y) && (x <= z))
			return 1;
		if ((y <= x) && (y <= z))
			return 2;
		return 3;
	}

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
			break;
		}

		return line;
	}

	/**
	 * Draw a list of locations at points on a rectangle along the x, y or z
	 * plane bound by point 1 and point 2
	 * <p>
	 * Note: Will use the lowest value for the difference between the
	 * coordinates for the plane
	 * 
	 * @param loc1
	 *            Point 1
	 * @param loc2
	 *            Point 2
	 * @param step
	 *            Size of step between locations
	 * @return List of locations for rectangle
	 */
	public static List<Location> drawRectangle(Location loc1, Location loc2,
			double step) {
		return drawRectangle(loc1, loc2, step, false);
	}

	/**
	 * Draw a list of locations at points on a rectangle along the x, y or z
	 * plane bound by point 1 and point 2
	 * <p>
	 * Note: Will use the lowest value for the difference between the
	 * coordinates for the plane
	 * 
	 * @param loc1
	 *            Point 1
	 * @param loc2
	 *            Point 2
	 * @param step
	 *            Size of step between locations
	 * @param linesAcross
	 *            True for lines across the rectangle
	 * @return List of locations for rectangle
	 */
	public static List<Location> drawRectangle(Location loc1, Location loc2,
			double step, boolean linesAcross) {
		List<Location> locs = new ArrayList<Location>();
		double yDiff = loc1.getY() - loc2.getY();

		if (yDiff == 0) {
			double xDiff = loc1.getX() - loc2.getX();
			double zDiff = loc1.getZ() - loc2.getZ();
			Location p1 = loc1.clone().subtract(xDiff, 0.0D, 0.0D);
			Location p2 = loc2.clone().add(xDiff, 0.0D, 0.0D);
			locs.addAll(drawPointToPoint(step, loc1, p1, loc2, p2));
			double incr = step * (zDiff / Math.abs(zDiff));
			if (linesAcross) {
				for (double d = incr; Math.abs(d) < Math.abs(zDiff); d += incr) {
					locs.addAll(drawLine(loc1.clone().subtract(0.0D, 0.0D, d),
							p1.clone().subtract(0.0D, 0.0D, d), step));
				}
			}
			return locs;
		}
		Location p1 = loc1.clone().subtract(0.0D, yDiff, 0.0D);
		Location p2 = loc2.clone().add(0.0D, yDiff, 0.0D);
		locs.addAll(drawPointToPoint(step, loc1, p1, loc2, p2));
		if (linesAcross) {
			List<Location> l1 = drawLine(loc1, p2, step);
			List<Location> l2 = drawLine(p1, loc2, step);
			for (int i = 0; i < l1.size(); i++) {
				locs.addAll(drawLine(l1.get(i), l2.get(i), step));
			}
		}
		return locs;
	}

	/**
	 * Connects the locations point to point and the final location to the first
	 * 
	 * @param locs
	 *            Locations to connect
	 * @param step
	 *            Size of step between locations
	 * @return List of locations for the connections
	 * @throws IllegalArgumentException
	 *             If there are fewer than 2 locations to connect
	 */
	public static List<Location> drawPointToPoint(List<Location> locs,
			double step) {
		List<Location> points = new ArrayList<Location>();

		if (locs.size() <= 1)
			throw new IllegalArgumentException(
					"Connect connect locations if there are fewer than 2 to connect");
		else {
			for (int i = 0; i < locs.size(); i++) {
				if (i == locs.size() - 1)
					points.addAll(drawLine(locs.get(i), locs.get(0), step));
				else
					points.addAll(drawLine(locs.get(i), locs.get(i + 1), step));
			}
		}
		return points;
	}

	/**
	 * Connects the locations point to point and the final location to the first
	 * 
	 * @param step
	 *            Size of step between locations
	 * @param locs
	 *            Locations to connect
	 * @return List of locations forming the connections
	 * @throws IllegalArgumentException
	 *             If there are fewer than 2 locations to connect
	 */
	public static List<Location> drawPointToPoint(double step, Location... locs) {
		List<Location> points = new ArrayList<Location>();

		if (locs.length <= 1)
			throw new IllegalArgumentException(
					"Connect connect locations if there are fewer than 2 to connect");
		else {
			for (int i = 0; i < locs.length; i++) {
				if (i == locs.length - 1)
					points.addAll(drawLine(locs[i], locs[0], step));
				else
					points.addAll(drawLine(locs[i], locs[i + 1], step));
			}
		}
		return points;
	}

	/**
	 * Draw a list of locations at points on a cuboid, bound by point 1 and
	 * point 2
	 * 
	 * @param loc1
	 *            Point 1
	 * @param loc2
	 *            Point 2
	 * @param step
	 *            Size of step between locations
	 * @return List of locations for cuboid
	 */
	public static List<Location> drawCuboid(Location loc1, Location loc2,
			double step) {
		return drawCuboid(loc1, loc2, step, false);
	}

	/**
	 * Draw a list of locations at points on a cuboid, bound by point 1 and
	 * point 2
	 * 
	 * @param loc1
	 *            Point 1
	 * @param loc2
	 *            Point 2
	 * @param step
	 *            Size of step between locations
	 * @param yStep
	 *            Vertical step size of locations
	 * @param linesAcross
	 *            True for lines along sizes and top of cuboid
	 * @return List of locations for cuboid
	 */
	public static List<Location> drawCuboid(Location loc1, Location loc2,
			double step, boolean linesAcross) {
		List<Location> locs = new ArrayList<Location>();

		double xDiff = loc1.getX() - loc2.getX();
		double yDiff = loc1.getY() - loc2.getY();
		double zDiff = loc1.getZ() - loc2.getZ();

		if (xDiff == 0 || yDiff == 0 || zDiff == 0)
			return drawRectangle(loc1, loc2, step);

		Location p1 = loc1.clone().subtract(0.0D, yDiff, 0.0D);
		Location p2 = loc2.clone().add(0.0D, yDiff, 0.0D);

		locs.addAll(drawRectangle(loc1, p2, step, linesAcross));
		locs.addAll(drawRectangle(loc2, p1, step, linesAcross));

		locs.addAll(drawLine(loc1, p1, step));
		locs.addAll(drawLine(loc2, p2, step));

		Location p3 = loc1.clone().subtract(xDiff, 0.0D, 0.0D);
		Location p4 = loc2.clone().add(0.0D, 0.0D, zDiff);
		locs.addAll(drawLine(p3, p4, step));

		Location p5 = loc1.clone().subtract(0.0D, 0.0D, zDiff);
		Location p6 = loc2.clone().add(xDiff, 0.0D, 0.0D);
		locs.addAll(drawLine(p5, p6, step));

		if (linesAcross) {
			double incr = step * (yDiff / Math.abs(yDiff));
			for (double offset = incr; Math.abs(offset) < Math.abs(yDiff); offset += incr) {
				if (Math.abs(offset) < Math.abs(yDiff)) {
					Location m1 = loc1.clone().subtract(0.0D, offset, 0.0D);
					Location m2 = p2.clone().subtract(0.0D, offset, 0.0D);
					locs.addAll(drawRectangle(m1, m2, step));
				}
			}
		}
		return locs;
	}

	/**
	 * Draw a list of locations at points on an ellipse
	 * <p>
	 * Note: Will use the highest values for the dimensions
	 * 
	 * @param centre
	 *            Centre of ellipse
	 * @param dim
	 *            Dimensions of ellipse
	 * @param step
	 *            Size of step between locations
	 * @return List of locations for ellipse
	 */
	public static List<Location> drawEllipse(Location centre, Dim dim,
			double step) {
		List<Location> locs = new ArrayList<Location>();

		double bigR1 = 0;
		double bigR2 = 0;
		switch (findSmallest(dim.getX(), dim.getY(), dim.getZ())) {
		case 1:
			bigR1 = dim.getY();
			bigR2 = dim.getZ();
			break;
		case 2:
			bigR1 = dim.getX();
			bigR2 = dim.getZ();
			break;
		case 3:
			bigR1 = dim.getX();
			bigR2 = dim.getY();
			break;
		}
		double perim = MathsUtil.perimeterEllipse(bigR1, bigR2);
		double deltaTheta = step / perim;

		for (double i = 0.0D; i < 1.0D; i += deltaTheta) {
			double x = centre.getX();
			double y = centre.getY();
			double z = centre.getZ();

			switch (findSmallest(dim.getX(), dim.getY(), dim.getZ())) {
			case 1:
				y += Math.cos(i * 2.0D * Math.PI) * dim.getY();
				z += Math.sin(i * 2.0D * Math.PI) * dim.getZ();
				break;
			case 2:
				x += Math.cos(i * 2.0D * Math.PI) * dim.getX();
				z += Math.sin(i * 2.0D * Math.PI) * dim.getZ();
				break;
			case 3:
				x += Math.cos(i * 2.0D * Math.PI) * dim.getX();
				y += Math.sin(i * 2.0D * Math.PI) * dim.getY();
				break;
			}
			locs.add(new Location(centre.getWorld(), x, y, z));
		}
		return locs;
	}

	/**
	 * Draw a list of locations at points on a sphere, uses ellipsoid generator
	 * with equal dimensions
	 * 
	 * @param centre
	 *            Centre location of the sphere
	 * @param radius
	 *            Radius of the Sphere
	 * @param step
	 *            Size of step between locations
	 * @return List of locations for sphere
	 */
	public static List<Location> drawSphere(Location centre, double radius,
			double step) {
		return drawEllipsoid(centre, new Dim(radius, radius, radius), step);
	}

	/**
	 * Draw a list of locations at points on an ellipsoid
	 * 
	 * @param centre
	 *            Centre location of the ellipsoid
	 * @param dim
	 *            Dimensions of the ellipsoid
	 * @param step
	 *            Size of step between locations
	 * @return List of locations for ellipsoid
	 */
	public static List<Location> drawEllipsoid(Location centre, Dim dim,
			double step) {
		List<Location> locs = new ArrayList<Location>();
		World world = centre.getWorld();

		locs.addAll(drawEllipse(centre, new Dim(0.0D, dim.getY(), dim.getZ()),
				step));
		locs.addAll(drawEllipse(centre, new Dim(dim.getX(), 0.0D, dim.getZ()),
				step));
		locs.addAll(drawEllipse(centre, new Dim(dim.getX(), dim.getY(), 0.0D),
				step));

		for (double offset = step; offset < dim.getY(); offset += step) {
			Location centre1 = new Location(world, centre.getX(), centre.getY()
					- offset, centre.getZ());
			Location centre2 = new Location(world, centre.getX(), centre.getY()
					+ offset, centre.getZ());
			double difference = Math.abs(centre1.getY() - centre.getY());
			double radiusRatio = Math.cos(Math.asin(difference / dim.getY()));

			double rx = dim.getX() * radiusRatio;
			double rz = dim.getZ() * radiusRatio;

			locs.addAll(drawEllipse(centre1, new Dim(rx, 0.0D, rz), step));
			locs.addAll(drawEllipse(centre2, new Dim(rx, 0.0D, rz), step));
		}
		return locs;
	}

	/**
	 * Draw a list of locations at points on a cylinder
	 * 
	 * @param centre
	 *            Centre location of the lowest level
	 * @param radius
	 *            Radius of the cylinder
	 * @param height
	 *            of the cylinder
	 * @param step
	 *            Size of step between locations
	 * @param verticalLines
	 *            True for vertical lines at 4 edges
	 * @return List of locations for cylinder
	 */
	public static List<Location> drawCylinder(Location centre, double radius,
			double height, double step, boolean verticalLines) {
		return drawCylinder(centre, new Dim(radius, height, radius), step,
				step, verticalLines);
	}

	/**
	 * Draw a list of locations at points on a cylinder
	 * 
	 * @param centre
	 *            Centre location of the lowest level
	 * @param dim
	 *            Dimensions of the cylinder
	 * @param step
	 *            Size of step between locations
	 * @param verticalLines
	 *            True for vertical lines at 4 edges
	 * @return List of locations for cylinder
	 */
	public static List<Location> drawCylinder(Location centre, Dim dim,
			double step, boolean verticalLines) {
		return drawCylinder(centre, dim, step, step, verticalLines);
	}

	/**
	 * Draw a list of locations at points on a cylinder
	 * 
	 * @param centre
	 *            Centre location of the lowest level
	 * @param radius
	 *            Radius of the cylinder
	 * @param Height
	 *            of the cylinder
	 * @param step
	 *            Size of step between locations
	 * @param yStep
	 *            Vertical step size of the locations
	 * @param verticalLines
	 *            True for vertical lines at 4 edges
	 * @return List of locations for cylinder
	 */
	public static List<Location> drawCylinder(Location centre, double radius,
			double height, double step, double yStep, boolean verticalLines) {
		return drawCylinder(centre, new Dim(radius, height, radius), step,
				yStep, verticalLines);
	}

	/**
	 * Draw a list of locations at points on a cylinder
	 * 
	 * @param centre
	 *            Centre location of the lowest level
	 * @param dim
	 *            Dimensions of the cylinder
	 * @param step
	 *            Size of step between locations
	 * @param yStep
	 *            Vertical step size of the locations
	 * @param verticalLines
	 *            True for vertical lines at 4 edges
	 * @return List of locations for cylinder
	 */
	public static List<Location> drawCylinder(Location centre, Dim dim,
			double step, double yStep, boolean verticalLines) {
		List<Location> locs = new ArrayList<Location>();

		locs.addAll(drawEllipse(centre.clone().add(0.0D, 0.0D, 0.0D), new Dim(
				dim.getX(), 0.0D, dim.getZ()), step));
		locs.addAll(drawEllipse(centre.clone().add(0.0D, dim.getY(), 0.0D),
				new Dim(dim.getX(), 0.0D, dim.getZ()), step));

		for (double d = yStep; d < dim.getY(); d += yStep) {
			if (d < dim.getY())
				locs.addAll(drawEllipse(centre.clone().add(0.0D, d, 0.0D),
						new Dim(dim.getX(), 0.0D, dim.getZ()), step));
		}

		if (verticalLines) {
			Location p1 = new Location(centre.getWorld(), centre.getX(),
					centre.getY(), centre.getZ() + dim.getZ());
			Location p2 = new Location(centre.getWorld(), centre.getX(),
					centre.getY(), centre.getZ() - dim.getZ());
			Location p3 = new Location(centre.getWorld(), centre.getX()
					+ dim.getX(), centre.getY(), centre.getZ());
			Location p4 = new Location(centre.getWorld(), centre.getX()
					- dim.getX(), centre.getY(), centre.getZ());

			locs.addAll(drawLine(p1, p1.clone().add(0.0D, dim.getY(), 0.0D),
					yStep / 2));
			locs.addAll(drawLine(p2, p2.clone().add(0.0D, dim.getY(), 0.0D),
					yStep / 2));
			locs.addAll(drawLine(p3, p3.clone().add(0.0D, dim.getY(), 0.0D),
					yStep / 2));
			locs.addAll(drawLine(p4, p4.clone().add(0.0D, dim.getY(), 0.0D),
					yStep / 2));
		}

		return locs;
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
			double maxDistance, double step) {
		return getLineOfSightHand(player, maxDistance, step, false);
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
	 * @param onlyAir
	 *            True to do line of sight through air only
	 * @return List of locations in line
	 */
	public static List<Location> getLineOfSightHand(Player player,
			double maxDistance, double step, boolean onlyAir) {
		float newYaw = player.getEyeLocation().getYaw() + 40F;
		Location loc1 = player.getEyeLocation().clone();
		loc1.setYaw(newYaw);
		Vector v = loc1.getDirection().normalize().multiply(0.5);
		loc1 = loc1.add(v);

		Location loc2 = getTargetLocation(player, maxDistance, onlyAir);

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
	public static List<Location> getLineOfSight(Player player,
			double maxDistance, double step) {
		return getLineOfSight(player, maxDistance, step, false);
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
	 * @param onlyAir
	 *            True to do line of sight through air only
	 * @return List of locations in line
	 */
	public static List<Location> getLineOfSight(Player player,
			double maxDistance, double step, boolean onlyAir) {
		Location loc1 = player.getEyeLocation();
		Location loc2 = getTargetLocation(player, maxDistance, onlyAir);
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
	public static Location getTargetLocation(Player player, double maxDistance) {
		return getTargetLocation(player, maxDistance, false);
	}

	/**
	 * Get the exact location of the player's target
	 * 
	 * @param player
	 *            Player
	 * @param maxDistance
	 *            Maximum distance to look
	 * @param onlyAir
	 *            True to do line of sight through air only
	 * @return Exact location of the player's target
	 */
	public static Location getTargetLocation(Player player, double maxDistance,
			boolean onlyAir) {
		Location loc = player.getEyeLocation();

		Vector v = loc.getDirection().normalize().multiply(0.05D);
		for (double d = 0.0D; d < maxDistance; d += 0.05D) {
			loc.add(v);
			if (onlyAir && !loc.getBlock().getType().equals(Material.AIR))
				return loc;
			else if (loc.getBlock().getType().isSolid()
					&& !loc.getBlock().getType().isTransparent()
					&& !loc.getBlock().isLiquid())
				return loc;
		}
		return loc;
	}
}
