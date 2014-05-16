package juicydev.jcore.utils.display;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import juicydev.jcore.utils.reflection.ReflectionUtil;
import juicydev.jcore.utils.reflection.ReflectionUtil.PackageType;
import juicydev.jcore.utils.reflection.ReflectionUtil.PacketType;
import juicydev.jcore.utils.reflection.ReflectionUtil.SubPackageType;

import org.bukkit.entity.Player;

public class PacketUtil {

	private static Class<?> obc_CraftPlayer;
	private static Method getHandle;

	private static Field playerConnection;
	private static Method sendPacket;

	static {
		try {
			obc_CraftPlayer = ReflectionUtil.getClass("CraftPlayer",
					SubPackageType.ENTITY);
			getHandle = ReflectionUtil.getMethod(obc_CraftPlayer, "getHandle");

			playerConnection = ReflectionUtil.getField("EntityPlayer",
					PackageType.MINECRAFT_SERVER, "playerConnection");
			sendPacket = ReflectionUtil.getMethod(playerConnection.getType(),
					"sendPacket", ReflectionUtil.getClass("Packet",
							PackageType.MINECRAFT_SERVER));
		} catch (Exception e) {
		}
	}

	/**
	 * Sends a packet to a player
	 * 
	 * @param player
	 *            Player to send packet to
	 * @param packet
	 *            Packet to send
	 */
	public static void sendPacket(Player player, Object packet) {
		try {
			sendPacket.invoke(playerConnection.get(getHandle
					.invoke(obc_CraftPlayer.cast(player))), packet);
		} catch (Exception e) {
			throw new PacketSendingException(
					"Failed to send a packet to player '" + player.getName()
							+ "'", e);
		}
	}

	/**
	 * Sends a packet to a collection of players
	 * 
	 * @param players
	 *            Collection of players to send packets to
	 * @param packet
	 *            Packet to send to players
	 */
	public static void sendPacket(Collection<Player> players, Object packet) {
		for (Player player : players)
			sendPacket(player, packet);
	}

	/**
	 * Gets a new packet instance
	 * 
	 * @param type
	 *            Type of packet
	 * @param args
	 *            Packet constructor arguments
	 * @return Packet instance
	 */
	public static Object getPacket(PacketType type, Object... args) {
		try {
			Class<?>[] paramTypes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				paramTypes[i] = arg.getClass();
			}
			Constructor<?> c_packet = ReflectionUtil.getConstructor(
					type.getPacket(), paramTypes);
			Object packet = c_packet.newInstance(args);
			return packet;
		} catch (Exception e) {
			throw new PacketInstantiationException(
					"Error instantiating packet", e);
		}
	}

	/**
	 * Represents a runtime exception that can be thrown upon packet
	 * instantiation
	 */
	private static final class PacketInstantiationException extends
			RuntimeException {
		private static final long serialVersionUID = -5179311826851333234L;

		/**
		 * @param message
		 *            Message that will be logged
		 * @param cause
		 *            Cause of the exception
		 */
		public PacketInstantiationException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	/**
	 * Represents a runtime exception that can be thrown upon packet sending
	 */
	private static final class PacketSendingException extends RuntimeException {
		private static final long serialVersionUID = 5840716116919917394L;

		/**
		 * @param message
		 *            Message that will be logged
		 * @param cause
		 *            Cause of the exception
		 */
		public PacketSendingException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
