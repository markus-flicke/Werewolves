package game.notification;

import phases.Phase;
import phases.result.PhaseResult;

public class PhaseConcludedNotification implements GameNotification {
	
	private final Phase phase;
	private final PhaseResult phaseResult;
	
	public PhaseConcludedNotification(Phase phase, PhaseResult phaseResult) {
		this.phase = phase;
		this.phaseResult = phaseResult;
	}

	
	/* (non-Javadoc)
	 * @see game.notification.GameNotification#getMessage()
	 */
	@Override
	public String getMessage() {
		return "Concluded phase " + phase.getClass().getSimpleName() + ". " + phaseResult.getMessage();
	}

	/**
	 * Returns the phase result contained in this notification.
	 * @return the phase result.
	 */
	public PhaseResult getPhaseResult() {
		return phaseResult;		
	}
	
	public Phase getPhase() {
		return phase;
	}
}
