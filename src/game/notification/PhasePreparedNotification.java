package game.notification;

import phases.Phase;

public class PhasePreparedNotification implements GameNotification {
	
	final private Phase phase;

	public PhasePreparedNotification(Phase phase) {
		this.phase = phase;
	}
	
	/* (non-Javadoc)
	 * @see game.notification.GameNotification#getMessage()
	 */
	@Override
	public String getMessage() {
		return "Prepared phase " + phase.getClass().getSimpleName() + ".";
	}
	
	public Phase getPhase() {
		return phase;
	}

}
