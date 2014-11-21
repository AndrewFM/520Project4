/**
 * This is the template of a class that should run policy iteration on
 * a given MDP to compute the optimal policy which is returned in the
 * public <tt>policy</tt> field.  You need to fill in the constructor.
 * You may wish to add other fields with other useful information that
 * you want this class to return (for instance, number of iterations
 * before convergence).  You also may add other constructors or
 * methods, provided that these are in addition to the one given below
 * (which is the one that will be automatically tested).  In
 * particular, your code must work properly when run with the
 * <tt>main</tt> provided in <tt>RunCatMouse.java</tt>.
 */
public class PolicyIteration {

    /** the computed optimal policy for the given MDP **/
    public int policy[];

    /**
     * The constructor for this class.  Computes the optimal policy
     * for the given <tt>mdp</tt> with given <tt>discount</tt> factor,
     * and stores the answer in <tt>policy</tt>.
     */
    public PolicyIteration(Mdp mdp, double discount) {    	
	    //Generate a random initial policy.
	    policy = new int[mdp.numStates];
	    for(int i=0; i<policy.length; i++) 
	    	policy[i] = (int)Math.floor(Math.random()*mdp.numActions);
	    	
		//Iterate the policy until it stabilizes.
	    boolean utilityChanged = true;
	    PolicyEvaluation peCurrent;
	    PolicyEvaluation peNew = new PolicyEvaluation(mdp, discount, policy);
	    
	    while (utilityChanged != false) {
	    	//Policy Improvement:
	    	peCurrent = peNew;
	    	policy = ValueIteration.computePolicyFromUtilities(mdp, peCurrent.utility);
	    	peNew = new PolicyEvaluation(mdp, discount, policy);
	    	
	    	//Check if the new policy actually changed any of the utilities.
	    	utilityChanged = false;
	    	for(int i=0; i<mdp.numStates; i++) {
	    		if (Math.abs(peCurrent.utility[i]-peNew.utility[i]) != 0) {
	    			utilityChanged = true;
	    			break;
	    		}
	    	}	    	
	    }
    }

}
