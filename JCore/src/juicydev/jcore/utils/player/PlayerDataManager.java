package juicydev.jcore.utils.player;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import juicydev.jcore.JCMessageManager;
import juicydev.jcore.utils.reflection.ReflectionUtil;
import juicydev.jcore.utils.reflection.ReflectionUtil.PackageType;
import juicydev.jcore.utils.reflection.ReflectionUtil.SubPackageType;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Credit to lishid for original class, reflection by JuicyDev
 * 
 * @author lishid, JuicyDev
 */
public class PlayerDataManager {

	private Plugin plugin;
	private String versionString;
	private int version;
	private String playerFolderName;

	public PlayerDataManager(Plugin plugin) {
		this.plugin = plugin;

		this.versionString = this.plugin
				.getServer()
				.getClass()
				.getPackage()
				.getName()
				.substring(
						this.plugin.getServer().getClass().getPackage()
								.getName().lastIndexOf('.') + 1);
		try {
			this.version = Integer.valueOf(versionString.replace("v", "")
					.replace("_", "").replace("R", ""));
		} catch (NumberFormatException e) { // Fallback
			this.version = 173;
		}
		this.playerFolderName = version >= 173 ? "playerdata" : "players";
		;
	}

	/**
	 * @param uuid
	 *            UUID of the player
	 * @return Instance of the player (null if doesn't exist)
	 */
	public Player loadPlayer(UUID uuid) {
		try {
			File playerFolder = new File(
					((World) Bukkit.getWorlds().get(0)).getWorldFolder(),
					playerFolderName);
			if (!playerFolder.exists()) {
				return null;
			}

			OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
			if (player == null
					|| matchUser(Arrays.asList(playerFolder.listFiles()),
							player.getUniqueId().toString()) == null) {
				return null;
			}
			GameProfile profile = new GameProfile(player.getUniqueId(),
					player.getName());

			Class<?> obc_CraftServer = ReflectionUtil.getClass("CraftServer",
					PackageType.CRAFTBUKKIT);
			Object craftServer = obc_CraftServer.cast(Bukkit.getServer());
			Method m_getServer = ReflectionUtil.getMethod(obc_CraftServer,
					"getServer");
			Class<?> nms_MinecraftServer = ReflectionUtil.getClass(
					"MinecraftServer", PackageType.MINECRAFT_SERVER);
			Object minecraftServer = m_getServer.invoke(craftServer);

			Class<?> nms_EntityPlayer = ReflectionUtil.getClass("EntityPlayer",
					PackageType.MINECRAFT_SERVER);
			Class<?> nms_WorldServer = ReflectionUtil.getClass("WorldServer",
					PackageType.MINECRAFT_SERVER);
			Class<?> nms_PlayerInteractManager = ReflectionUtil.getClass(
					"PlayerInteractManager", PackageType.MINECRAFT_SERVER);
			Object worldServer = ReflectionUtil.getMethod(nms_MinecraftServer,
					"getWorldServer", Integer.class).invoke(minecraftServer, 0);

			Constructor<?> c_EntityPlayer = ReflectionUtil.getConstructor(
					nms_EntityPlayer, nms_MinecraftServer, nms_WorldServer,
					GameProfile.class, nms_PlayerInteractManager);
			Constructor<?> c_PlayerInteractManager = ReflectionUtil
					.getConstructor(nms_PlayerInteractManager, nms_WorldServer);
			Object playerInteractManager = c_PlayerInteractManager
					.newInstance(worldServer);

			Object entityPlayer = c_EntityPlayer.newInstance(minecraftServer,
					worldServer, profile, playerInteractManager);

			Class<?> obc_CraftPlayer = ReflectionUtil.getClass("CraftPlayer",
					SubPackageType.ENTITY);
			Method m_getBukkitEntity = ReflectionUtil.getMethod(
					nms_EntityPlayer, "getBukkitEntity");

			Player target = entityPlayer == null ? null
					: (Player) obc_CraftPlayer.cast(m_getBukkitEntity
							.invoke(entityPlayer));

			if (target != null) {
				target.loadData();
				return target;
			}
		} catch (Exception e) {
			JCMessageManager.getInstance().error(e);
		}

		return null;
	}

	/**
	 * @param name
	 *            Name of the player
	 * @return Instance of the player (null if doesn't exist)
	 */
	public Player loadPlayer(String name) {
		try {
			File playerFolder = new File(
					((World) Bukkit.getWorlds().get(0)).getWorldFolder(),
					playerFolderName);
			if (!playerFolder.exists()) {
				return null;
			}

			@SuppressWarnings("deprecation")
			OfflinePlayer player = Bukkit.getOfflinePlayer(name);

			if (player == null
					|| matchUser(Arrays.asList(playerFolder.listFiles()),
							player.getUniqueId().toString()) == null) {
				return null;
			}
			GameProfile profile = new GameProfile(player.getUniqueId(), player.getName());

			Class<?> obc_CraftServer = ReflectionUtil.getClass("CraftServer",
					PackageType.CRAFTBUKKIT);
			Object craftServer = obc_CraftServer.cast(Bukkit.getServer());
			Method m_getServer = ReflectionUtil.getMethod(obc_CraftServer,
					"getServer");
			Class<?> nms_MinecraftServer = ReflectionUtil.getClass(
					"MinecraftServer", PackageType.MINECRAFT_SERVER);
			Object minecraftServer = m_getServer.invoke(craftServer);

			Class<?> nms_EntityPlayer = ReflectionUtil.getClass("EntityPlayer",
					PackageType.MINECRAFT_SERVER);
			Class<?> nms_WorldServer = ReflectionUtil.getClass("WorldServer",
					PackageType.MINECRAFT_SERVER);
			Class<?> nms_PlayerInteractManager = ReflectionUtil.getClass(
					"PlayerInteractManager", PackageType.MINECRAFT_SERVER);
			Object worldServer = ReflectionUtil.getMethod(nms_MinecraftServer,
					"getWorldServer", Integer.class).invoke(minecraftServer, 0);

			Constructor<?> c_EntityPlayer = ReflectionUtil.getConstructor(
					nms_EntityPlayer, nms_MinecraftServer, nms_WorldServer,
					GameProfile.class, nms_PlayerInteractManager);
			Constructor<?> c_PlayerInteractManager = ReflectionUtil
					.getConstructor(nms_PlayerInteractManager, nms_WorldServer);
			Object playerInteractManager = c_PlayerInteractManager
					.newInstance(worldServer);

			Object entityPlayer = c_EntityPlayer.newInstance(minecraftServer,
					worldServer, profile, playerInteractManager);

			Class<?> obc_CraftPlayer = ReflectionUtil.getClass("CraftPlayer",
					SubPackageType.ENTITY);
			Method m_getBukkitEntity = ReflectionUtil.getMethod(
					nms_EntityPlayer, "getBukkitEntity");

			Player target = entityPlayer == null ? null
					: (Player) obc_CraftPlayer.cast(m_getBukkitEntity
							.invoke(entityPlayer));

			if (target != null) {
				target.loadData();
				return target;
			}
		} catch (Exception e) {
			JCMessageManager.getInstance().error(e);
		}

		return null;
	}

	/**	 * 
	 * @param name
	 *            Name of the player
	 * @return Instance of the player (null if doesn't exist)
	 */
	public Player loadPlayerStrict(String name) {
		try {
			File playerfolder = new File(
					((World) Bukkit.getWorlds().get(0)).getWorldFolder(),
					playerFolderName);
			if (!playerfolder.exists()) {
				return null;
			}

			String playername = matchUserStrict(
					Arrays.asList(playerfolder.listFiles()), name);

			if (playername == null) {
				return null;
			}

			return loadPlayer(playername);
		} catch (Exception e) {
			JCMessageManager.getInstance().error(e);
		}

		return null;
	}

	private static String matchUser(Collection<File> container, String search) {
		String found = null;
		if (search == null) {
			return found;
		}
		String lowerSearch = search.toLowerCase();
		int delta = 2147483647;
		for (File file : container) {
			String filename = file.getName();
			String str = filename.substring(0, filename.length() - 4);
			if (str.toLowerCase().startsWith(lowerSearch)) {
				int curDelta = str.length() - lowerSearch.length();
				if (curDelta < delta) {
					found = str;
					delta = curDelta;
				}
				if (curDelta == 0) {
					break;
				}
			}
		}
		return found;
	}

	private static String matchUserStrict(Collection<File> container,
			String search) {
		String found = null;
		if (search == null) {
			return found;
		}
		for (File file : container) {
			String filename = file.getName();
			String str = filename.substring(0, filename.length() - 4);
			if (str.equalsIgnoreCase(search)) {
				found = str;
			}
		}
		return found;
	}
}