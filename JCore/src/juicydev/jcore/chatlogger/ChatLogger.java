package juicydev.jcore.chatlogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import juicydev.jcore.JCore;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatLogger implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent event) {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int seconds = cal.get(Calendar.SECOND);
		int minutes = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR);

		String fileName = String.valueOf(day) + "-" + String.valueOf(month + 1)
				+ "-" + String.valueOf(year);
		String secondsStr = String.valueOf(seconds < 10 ? "0" + seconds
				: seconds);
		String minutesStr = String.valueOf(minutes < 10 ? "0" + minutes
				: minutes);
		String hourStr = String.valueOf(hour < 10 ? "0" + hour : hour);
		String joinedTime = hourStr + ":" + minutesStr + ":" + secondsStr;

		File logFolder = new File(JCore.getInstance().getDataFolder()
				.getAbsolutePath()
				+ File.separator + "logs");
		logFolder.mkdirs();

		File yearFolder = new File(logFolder, String.valueOf(year));
		yearFolder.mkdirs();
		String monthShort = new SimpleDateFormat("MMM").format(cal.getTime());
		File monthFolder = new File(yearFolder, monthShort);
		monthFolder.mkdirs();
		File chatFolder = new File(monthFolder, "chat");
		chatFolder.mkdirs();

		File file = new File(chatFolder, fileName + ".log");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String msg = ChatColor.stripColor(event.getFormat())
				.replace("%1$s", event.getPlayer().getDisplayName())
				.replace("%2$s", ChatColor.stripColor(event.getMessage()));

		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(file, true)))) {
			out.println("[" + joinedTime + "] " + msg);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int seconds = cal.get(Calendar.SECOND);
		int minutes = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR);

		String fileName = String.valueOf(day) + "-" + String.valueOf(month + 1)
				+ "-" + String.valueOf(year);
		String secondsStr = String.valueOf(seconds < 10 ? "0" + seconds
				: seconds);
		String minutesStr = String.valueOf(minutes < 10 ? "0" + minutes
				: minutes);
		String hourStr = String.valueOf(hour < 10 ? "0" + hour : hour);
		String joinedTime = hourStr + ":" + minutesStr + ":" + secondsStr;

		File logFolder = new File(JCore.getInstance().getDataFolder()
				.getAbsolutePath()
				+ File.separator + "logs");
		logFolder.mkdirs();

		File yearFolder = new File(logFolder, String.valueOf(year));
		yearFolder.mkdirs();
		String monthShort = new SimpleDateFormat("MMM").format(cal.getTime());
		File monthFolder = new File(yearFolder, monthShort);
		monthFolder.mkdirs();
		File cmdsFolder = new File(monthFolder, "cmds");
		cmdsFolder.mkdirs();

		File file = new File(cmdsFolder, fileName + ".log");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String msg = event.getMessage();

		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(file, true)))) {
			out.println("[" + joinedTime + "] " + event.getPlayer().getName()
					+ ": " + msg);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
