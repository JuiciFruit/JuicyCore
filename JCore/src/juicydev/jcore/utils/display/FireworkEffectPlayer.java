package juicydev.jcore.utils.display;

import java.lang.reflect.Method;
import java.util.Collection;

import juicydev.jcore.utils.reflection.ReflectionUtil;
import juicydev.jcore.utils.reflection.ReflectionUtil.PackageType;
import juicydev.jcore.utils.reflection.ReflectionUtil.PacketType;
import juicydev.jcore.utils.reflection.ReflectionUtil.SubPackageType;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkEffectPlayer {

	private static Class<?> obc_CraftFirework;
	private static Class<?> obc_CraftWorld;

	private static Method obc_CraftFirework_getHandle;
	private static Method obc_CraftWorld_getHandle;

	private static Class<?> nms_World;

	private static Class<?> nms_Entity;
	private static Method nms_World_broadcastEntityEffect;

	// private static Constructor<?> c_PacketPlayOutEntityStatus;

	static {
		try {
			obc_CraftFirework = ReflectionUtil.getClass("CraftFirework",
					SubPackageType.ENTITY);
			obc_CraftWorld = ReflectionUtil.getClass("CraftWorld",
					PackageType.CRAFTBUKKIT);

			obc_CraftFirework_getHandle = ReflectionUtil.getMethod(
					obc_CraftFirework, "getHandle");
			obc_CraftWorld_getHandle = ReflectionUtil.getMethod(obc_CraftWorld,
					"getHandle");

			nms_World = ReflectionUtil.getClass("World",
					PackageType.MINECRAFT_SERVER);

			nms_Entity = ReflectionUtil.getClass("Entity",
					PackageType.MINECRAFT_SERVER);
			nms_World_broadcastEntityEffect = ReflectionUtil.getMethod(
					nms_World, "broadcastEntityEffect", nms_Entity, byte.class);

			// c_PacketPlayOutEntityStatus = ReflectionUtil.getConstructor(
			// PacketType.PLAY_OUT_ENTITY_STATUS.getPacket(), nms_Entity,
			// byte.class);
		} catch (Exception e) {
		}
	}

	public static void playFirework(Location loc, FireworkEffect... fe)
			throws Exception {
		Firework fw = loc.getWorld().spawn(loc, Firework.class);

		FireworkMeta fm = fw.getFireworkMeta();
		fm.clearEffects();
		fm.addEffects(fe);
		fm.setPower(1);
		fw.setFireworkMeta(fm);

		Object craftFirework = obc_CraftFirework.cast(fw);
		Object nmsFirework = obc_CraftFirework_getHandle.invoke(craftFirework);

		Object craftWorld = obc_CraftWorld.cast(loc.getWorld());
		Object nmsWorld = obc_CraftWorld_getHandle.invoke(craftWorld);

		nms_World_broadcastEntityEffect
				.invoke(nmsWorld, nmsFirework, (byte) 17);
		fw.remove();
	}

	public static void playFirework(Player player, Location loc,
			FireworkEffect... fe) throws Exception {
		Firework fw = loc.getWorld().spawn(loc, Firework.class);

		FireworkMeta fm = fw.getFireworkMeta();
		fm.clearEffects();
		fm.addEffects(fe);
		fm.setPower(1);
		fw.setFireworkMeta(fm);

		Object craftFirework = obc_CraftFirework.cast(fw);
		Object nmsFirework = obc_CraftFirework_getHandle.invoke(craftFirework);

		// Object packetPlayOutEntityStatus = c_PacketPlayOutEntityStatus
		// .newInstance(nmsFirework, (byte) 17);

		Object packetPlayOutEntityStatus = PacketUtil.getPacket(
				PacketType.PLAY_OUT_ENTITY_STATUS, nmsFirework, (byte) 17);

		PacketUtil.sendPacket(player, packetPlayOutEntityStatus);
		fw.remove();
	}

	public static void playFirework(Collection<Player> players, Location loc,
			FireworkEffect... fe) throws Exception {
		for (Player player : players)
			playFirework(player, loc, fe);
	}
}