package game.notification;

import phases.Phase;

public interface GameNotification {
	
	/**
	 * Returns the message of this notification.
	 * @return the message of the notification.
	 */
	public String getMessage();
	
	public Phase getPhase();

}
