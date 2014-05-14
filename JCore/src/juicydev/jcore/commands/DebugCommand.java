package juicydev.jcore.commands;

import java.util.UUID;

import juicydev.jcore.JCMessageManager;
import juicydev.jcore.JCore;
import juicydev.jcore.Perms;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class DebugCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (!Perms.DEBUG.has(sender))
			return true;

		if (sender instanceof ConsoleCommandSender) {
			JCore.debugEnabled = !JCore.debugEnabled;
			sender.sendMessage(JCore.getInstance().getName() + " Debugging: "
					+ (JCore.debugEnabled ? "on" : "off"));
		} else if (sender instanceof Player) {
			UUID uuid = ((Player) sender).getUniqueId();
			if (JCore.debug.contains(uuid))
				JCore.debug.remove(uuid);
			else
				JCore.debug.add(uuid);
			JCMessageManager.getInstance().infoSender(
					sender,
					"Debugging: "
							+ (JCore.debug.contains(uuid) ? "on" : "off"));
		}

		return true;
	}
}
