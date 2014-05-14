package juicydev.jcore.commands;

import juicydev.jcore.JCMessageManager;
import juicydev.jcore.Perms;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayDiscCommand implements CommandExecutor {

	private int[] recordIDs = { 0, 2256, 2257, 2258, 2259, 2260, 2261, 2262,
			2263, 2264, 2265, 2266, 2267 };
	private String[] recordNames = { "Stop", "13", "Cat", "Blocks", "Chirp",
			"Far", "Mall", "Mellohi", "Stal", "Strad", "Ward", "11", "Wait" };

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (!Perms.PLAYDISC.has(sender))
			return true;

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("list"))
				JCMessageManager
						.getInstance()
						.infoSender(
								sender,
								"Music Discs: 13, Cat, Blocks, Chirp, Far, Mall, Mellohi, Stal, Strad, Ward, 11, Wait");
			else {
				if (!(sender instanceof Player)) {
					JCMessageManager.getInstance().errSender(sender,
							"Usage: /" + label + " <disc> [all/player]");
					JCMessageManager.getInstance().errSender(sender,
							"Usage: /" + label + " list");
					return true;
				}
				Player player = (Player) sender;
				String discName = args[0];

				int disc = -1;
				for (int i = 0; i < recordNames.length; i++) {
					if (recordNames[i].equalsIgnoreCase(discName)) {
						disc = i;
						break;
					}
				}

				if (disc < 0) {
					JCMessageManager.getInstance().errSender(sender,
							"Music Disc \"" + discName + "\" not found.");
					return true;
				}

				player.playEffect(player.getLocation(), Effect.RECORD_PLAY,
						recordIDs[disc]);

				if (disc == 0)
					JCMessageManager.getInstance().infoSender(sender,
							"Stopping music.");
				else
					JCMessageManager.getInstance().infoSender(sender,
							"Playing disc \"" + recordNames[disc] + "\".");
			}
			return true;
		}

		if (args.length != 2) {
			JCMessageManager.getInstance().errSender(sender,
					"Usage: /" + label + " <disc> [all/player]");
			JCMessageManager.getInstance().errSender(sender,
					"Usage: /" + label + " list");
			return true;
		}

		String discName = args[0];
		String playerName = args[1];

		if (playerName.equalsIgnoreCase("all")) {
			int disc = -1;
			for (int i = 0; i < recordNames.length; i++) {
				if (recordNames[i].equalsIgnoreCase(discName)) {
					disc = i;
					break;
				}
			}

			if (disc < 0) {
				JCMessageManager.getInstance().errSender(sender,
						"Music Disc \"" + discName + "\" not found.");
				return true;
			}

			for (Player player : Bukkit.getOnlinePlayers()) {
				player.playEffect(player.getLocation(), Effect.RECORD_PLAY,
						recordIDs[disc]);
			}

			if (disc == 0)
				JCMessageManager.getInstance().infoSender(sender,
						"Stopping music for all players.");
			else
				JCMessageManager.getInstance().infoSender(
						sender,
						"Playing disc \"" + recordNames[disc]
								+ "\" to all players.");
		} else {
			Player player = Bukkit.getPlayer(playerName);
			if (player == null) {
				JCMessageManager.getInstance().errSender(sender,
						"Player \"" + playerName + "\" could not be found.");
				return true;
			}

			int disc = -1;
			for (int i = 0; i < recordNames.length; i++) {
				if (recordNames[i].equalsIgnoreCase(discName)) {
					disc = i;
					break;
				}
			}

			if (disc < 0) {
				JCMessageManager.getInstance().errSender(sender,
						"Music Disc \"" + discName + "\" not found.");
				return true;
			}

			player.playEffect(player.getLocation(), Effect.RECORD_PLAY,
					recordIDs[disc]);

			if (disc == 0)
				JCMessageManager.getInstance().infoSender(sender,
						"Stopping music for " + player.getName() + ".");
			else
				JCMessageManager.getInstance().infoSender(
						sender,
						"Playing disc \"" + recordNames[disc] + "\" to "
								+ player.getName() + ".");
		}

		return true;
	}
}
