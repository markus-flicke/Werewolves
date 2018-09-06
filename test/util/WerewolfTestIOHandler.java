package util;

import java.util.HashMap;
import java.util.Map;

import game.io.WerewolfIOHandler;
import game.notification.GameNotification;

public class WerewolfTestIOHandler implements WerewolfIOHandler {
	
	Map<String, String> stringVotes = new HashMap<String, String>();
	private WerewolfTestInputListener inputListener;
	private WerewolfTestOutputListener outputListener;
	
	public void registerInputListener(WerewolfTestInputListener inputListener) {
		this.inputListener = inputListener;
	}
	
	public void registerOutputListener(WerewolfTestOutputListener outputListener) {
		this.outputListener = outputListener;
	}

	public void queueVote(String voter, String target) {
		stringVotes.put(voter, target);
	}
	
	@Override
	public Map<String, String> gatherVotes() {
		if (inputListener != null) {
			inputListener.handleInputNotification();
		}
		
		Map<String, String> results = new HashMap<String, String>(stringVotes);
		stringVotes.clear();
		return results;
	}
	
	
	@Override
	public void handleNotification(GameNotification notification) {
		if (outputListener != null) {
			outputListener.handleOutputNotification();
		}
		System.out.println(notification.getMessage());
	}

}
