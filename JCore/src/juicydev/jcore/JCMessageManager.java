package juicydev.jcore;

import juicydev.jcore.utils.MessageManager;

public class JCMessageManager {
	private static MessageManager instance = new MessageManager(JCore
			.getInstance().getName());

	private JCMessageManager() {
	}

	public static MessageManager getInstance() {
		return instance;
	}
}
