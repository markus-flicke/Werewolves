package game.notification;

import phases.Phase;

public class VoteCompletedNotification implements GameNotification {
	
	private final Phase phase;
	
	public VoteCompletedNotification(Phase phase) {
		this.phase = phase;
	}
	
	/* (non-Javadoc)
	 * @see game.notification.GameNotification#getMessage()
	 */
	@Override
	public String getMessage() {
		return "Vote completed in phase " + phase.getClass().getSimpleName() + ".";
	}
	
	public Phase getPhase() {
		return phase;
	}

}
