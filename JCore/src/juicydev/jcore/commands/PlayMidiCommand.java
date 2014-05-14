package juicydev.jcore.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import juicydev.jcore.JCMessageManager;
import juicydev.jcore.JCore;
import juicydev.jcore.Perms;
import juicydev.jcore.utils.FileUtil;
import juicydev.jcore.utils.MessageManager;
import juicydev.jcore.utils.midi.MidiUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;

public class PlayMidiCommand implements CommandExecutor {

	private MessageManager mm = JCMessageManager.getInstance();

	private String midiFolderPath = JCore.getInstance().getDataFolder()
			.getAbsolutePath()
			+ File.separator + "midi";
	private File midiFolder = new File(midiFolderPath);

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (!Perms.PLAYMIDI.has(sender))
			return true;

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("list")) {
				if (midiFolder == null) {
					midiFolder.mkdirs();
					mm.errSender(sender, "There are no files.");
					return true;
				}

				if (midiFolder.listFiles().length == 0) {
					mm.errSender(sender, "There are no files.");
					return true;
				}

				ArrayList<String> names = new ArrayList<String>();
				for (File f : midiFolder.listFiles()) {
					if (f.isFile()) {
						if (MidiUtil.isMidi(f))
							names.add(f.getName());
					}
				}

				mm.infoSender(sender, "Midi Files:");
				for (String name : names)
					mm.msgSender(sender, "    " + name);
				return true;
			}
		}

		if (args.length < 2) {
			mm.errSender(sender, "Usage: /" + label
					+ " <player/all> <filename>");
			mm.errSender(sender, "Usage: /" + label + " list");
			return true;
		}

		String playerName = args[0];
		ArrayList<String> argsList = new ArrayList<String>();
		for (int i = 0; i < args.length; i++) {
			if (i != 0) {
				argsList.add(args[i]);
			}
		}
		String filename = Joiner.on(" ").join(argsList);

		if (playerName.equalsIgnoreCase("all")) {
			Set<Player> players = new HashSet<Player>();
			for (Player player : Bukkit.getOnlinePlayers())
				players.add(player);

			if (filename.equalsIgnoreCase("stop")) {
				MidiUtil.stop(players);
				mm.infoSender(sender, "Stopping music.");
				return true;
			}

			File midi = FileUtil.getFile(filename, midiFolder);
			if (midi == null) {
				mm.errSender(sender, "The file \"" + filename
						+ "\" does not exist.");
				return true;
			}
			if (!midi.exists()) {
				mm.errSender(sender, "The file \"" + filename
						+ "\" does not exist.");
				return true;
			}
			if (!MidiUtil.isMidi(midi)) {
				mm.errSender(sender, "The file \"" + filename
						+ "\" is not a valid midi file.");
				return true;
			}

			MidiUtil.playMidiQuietly(midi, players);
			mm.infoSender(sender, "Playing \"" + filename + "\".");
			for (Player p : players)
				p.sendMessage(ChatColor.GREEN.toString()
						+ ChatColor.BOLD.toString() + "Now Playing: "
						+ FileUtil.getName(midi));
		} else {
			Player player = Bukkit.getPlayer(playerName);
			if (player == null) {
				mm.errSender(sender, "Player \"" + playerName
						+ "\" could not be found.");
				return true;
			}

			Set<Player> players = new HashSet<Player>();
			players.add(player);

			if (filename.equalsIgnoreCase("stop")) {
				MidiUtil.stop(players);
				mm.infoSender(sender, "Stopping music.");
				return true;
			}

			File midi = FileUtil.getFile(filename, midiFolder);
			if (midi == null) {
				mm.errSender(sender, "The file \"" + filename
						+ "\" does not exist.");
				return true;
			}
			if (!midi.exists()) {
				mm.errSender(sender, "The file \"" + filename
						+ "\" does not exist.");
				return true;
			}
			if (!MidiUtil.isMidi(midi)) {
				mm.errSender(sender, "The file \"" + filename
						+ "\" is not a valid midi file.");
				return true;
			}

			MidiUtil.playMidiQuietly(midi, players);
			mm.infoSender(sender, "Playing \"" + filename + "\".");
			for (Player p : players)
				p.sendMessage(ChatColor.GREEN.toString()
						+ ChatColor.BOLD.toString() + "Now Playing: "
						+ FileUtil.getName(midi));
		}

		return true;
	}
}
