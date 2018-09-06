package game.io;

import java.util.Map;

import game.notification.GameNotification;

public interface WerewolfIOHandler {
	
	public void handleNotification(GameNotification notification);
	
	public Map<String, String> gatherVotes();

}
