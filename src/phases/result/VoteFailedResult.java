package phases.result;

public class VoteFailedResult implements PhaseResult {

	/* (non-Javadoc)
	 * @see phases.result.PhaseResult#getMessage()
	 */
	@Override
	public String getMessage() {
		return "The vote failed.";
	}

}
