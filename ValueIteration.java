/**
 * This is the template of a class that should run value iteration on
 * a given MDP to compute the optimal policy which is returned in the
 * public <tt>policy</tt> field.  The computed optimal utility is also
 * returned in the public <tt>utility</tt> field.  You need to fill in
 * the constructor.  You may wish to add other fields with other
 * useful information that you want this class to return (for
 * instance, number of iterations before convergence).  You also may
 * add other constructors or methods, provided that these are in
 * addition to the one given below (which is the one that will be
 * automatically tested).  In particular, your code must work properly
 * when run with the <tt>main</tt> provided in <tt>RunCatMouse.java</tt>.
 */
public class ValueIteration {

	private static final double EPSILON = 1.0/Math.pow(10, 8);
	
    /** the computed optimal policy for the given MDP **/
    public int policy[];

    /** the computed optimal utility for the given MDP **/
    public double utility[];

    /**
     * The constructor for this class.  Computes the optimal policy
     * for the given <tt>mdp</tt> with given <tt>discount</tt> factor,
     * and stores the answer in <tt>policy</tt>.  Also stores the
     * optimal utility in <tt>utility</tt>.
     */
    public ValueIteration(Mdp mdp, double discount) {
    	utility = computeUtilityFunction(mdp, discount, null);
    	policy = computePolicyFromUtilities(mdp, utility);	
    }
    
    /**
     * Compute the utility of all states in this MDP in an iterative 
     * fashion, using the Bellman Equation.
     * 
     * @param mdp The MDP to compute utilities of.
     * @param discount The discounting factor for the Bellman Equation.
     * @param policy Optionally, a policy that fixes the actions for each state. This
     * 			     can be null, in which case all actions will be evaluated.
     * @return A Utility Function for the states of the MDP.
     */
    public static double[] computeUtilityFunction(Mdp mdp, double discount, int[] policy) 
    {
    	double delta = Double.MAX_VALUE;
    	double[] utilityNew = new double[mdp.numStates];
    	double[] utilityCurrent = new double[mdp.numStates];
    	
    	for(int i=0; i<mdp.numStates; i++) {
    		utilityNew[i] = 0;
    		utilityCurrent[i] = 0;
    	}
    	
    	//Compute utilities
    	while (delta > EPSILON) {
    		delta = 0;
    		for(int i=0; i<utilityCurrent.length; i++) {
    			double maxAction = -Double.MAX_VALUE;
    			double actionUtility = 0;
    			
    			//If no policy specified, calculate the maximum expected utility out of every available action at this state.
    			if (policy == null) {
	    			for(int j=0; j<mdp.numActions; j++) {
	    				actionUtility = 0;
	    				for(int k=0; k<mdp.nextState[i][j].length; k++)
	    					actionUtility += mdp.transProb[i][j][k]*utilityCurrent[mdp.nextState[i][j][k]];
	    				if (actionUtility > maxAction)
	    					maxAction = actionUtility;
	    			}
    			} 
    			
    			//Otherwise, if a policy was specified, calculate the expected utility of the given action at this state.
    			else {
    				for(int k=0; k<mdp.nextState[i][policy[i]].length; k++)
    					actionUtility += mdp.transProb[i][policy[i]][k]*utilityCurrent[mdp.nextState[i][policy[i]][k]];
    				maxAction = actionUtility;
    			}
    			
    			//Calculate the new utility using the Bellman Equation.
    			utilityCurrent[i] = utilityNew[i];
    			utilityNew[i] = mdp.reward[i]+discount*maxAction;
    			
    			//If necessary, update the value of this iteration's largest change in utility.
    			if (Math.abs(utilityCurrent[i]-utilityNew[i]) > delta)
    				delta = Math.abs(utilityCurrent[i]-utilityNew[i]);
    		}
    	}
    	
    	return utilityNew;
    }
    
    /**
     * Construct an optimal policy by choosing actions for each state that
     * maximize the expected utility of the successor state.
     * 
     * @param mdp The MDP being worked on.
     * @param utilities The utilities of each state.
     * @return An optimal policy based on these utilities.
     */
    public static int[] computePolicyFromUtilities(Mdp mdp, double[] utilities) {
    	int[] returnPolicy = new int[mdp.numStates];
    	
    	for(int i=0; i<mdp.numStates; i++) {
    		double maxUtility = -Double.MAX_VALUE;
    		
    		//Find the action that results in the highest utility.
    		for(int j=0; j<mdp.numActions; j++) {
    			double actionUtility = 0;
    			for(int k=0; k<mdp.nextState[i][j].length; k++)
    				actionUtility += mdp.transProb[i][j][k]*utilities[mdp.nextState[i][j][k]];
    			
    			//If best so far, assign this action as the current argmax.
    			if (actionUtility > maxUtility) {
    				maxUtility = actionUtility;
    				returnPolicy[i] = j;
    			}
    		}
    	}
    	
    	return returnPolicy;
    }

}
