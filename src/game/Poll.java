package game;

import java.util.ArrayList;
import java.util.List;

import roles.Player;

public class Poll {
	
	List<Player> voters;
	List<Player> nominees;
	List<Vote> votes;	
	
	public Poll(List<Player> voters, List<Player> nominees) {
		this.voters = voters;
		this.nominees = nominees;
		this.votes = new ArrayList<Vote>();
	}
	
	/**
	 * Registers a vote.
	 * @param voter the player that issued the vote.
	 * @param target the player the voter voted for.
	 */
	public void registerVote(Player voter, Player target) {
		if (voters.contains(voter) && nominees.contains(target)) {
			votes.add(new Vote(voter, target));
		}
	}
	
	/**
	 * Checks if the poll is conclusive.
	 * @return true, if at least half of the votes (rounded up) share the same target, false otherwise.
	 */
	public boolean isConclusive() {
		int c = 0;
		Player votedPlayer = getVotedPlayer();
		for(Vote v: votes){
		    if(v.getNominee().equals(votedPlayer)){
		        c++;
            }
        }
        return c >= ((votes.size() + 1)/2) && votedPlayer != null;
	}
		
	/**
	 * Returns the target player with the highest amount of votes.
	 * @return the player with the highest amount of votes.
	 */
	public Player getVotedPlayer() {
	    Player votedPlayer = null;
	    int nVotes = 0;
	    for(Player n: nominees){
	        int count = 0;
            for(Vote v: votes){
                if(v.getNominee().equals(n)){
                    count++;
                }
            }
            if(count > nVotes){
                votedPlayer = n;
            }
        }
		return votedPlayer;
	}
	
	public class Vote {
		private final Player voter;
		private final Player nominee;
		
		public Vote(Player voter, Player nominee) {
			this.voter = voter;
			this.nominee = nominee;
		}
		
		public Player getVoter() {
			return this.voter;
		}
		
		public Player getNominee() {
			return this.nominee;
		}
	}
}
