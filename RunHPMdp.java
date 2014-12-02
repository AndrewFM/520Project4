import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class provides a simple main for running on cat and mouse
 * data.
 */
public class RunHPMdp {

    /** This is a simple main.  When invoked, it does the following:
     * (1) loads an MDP from a file named in the command line; (2)
     * runs value iteration and policy iteration; (3) runs policy
     * evaluation on the policy returned by policy iteration; (4)
     * prints out the optimal policy and utilities returned by both
     * value iteration and policy iteration; and (5) animates the cat
     * chasing the mouse, depending on the command-line arguments.
     * (Note that this animation will almost certainly crash if run on
     * MDP's other than those for a cat chasing a mouse.)
     *
     * <p>The command-line arguments should consist of a possible list
     * of options, followed by the name of a data file containing a
     * description of the MDP.  By default, a GUI (graphical user
     * interface) based animation will be invoked.  However, this can
     * be changed with the appropriate command-line options: Using the
     * "<tt>-b</tt> option will run the GUI while simultaneously
     * printing a transcript of all states visited.  Using the <tt>-p
     * &lt;num&gt;</tt> option will not invoke the GUI at all but will
     * instead run the MDP for <tt>&lt;num&gt;</tt> steps, while printing
     * the results.  Finally, using the <tt>-n</tt> option will
     * neither invoke the GUI nor print any results.
     *
     * <p>It is okay to change this main as you wish.  However, your
     * code should still work properly when using this one.
     */
    public static void main(String argv[])
	throws FileNotFoundException, IOException {

	double discount = 0.95;

	// parse options
	Options options = null;
	try {
	    options = new Options(argv);
	} catch (Exception e) {
	    printCommandLineError();
	    return;
	}

	// build MDP
	Mdp mdp = new Mdp(options.filename);

	// run value iteration
	ValueIteration vpi = new ValueIteration(mdp, discount);

	// run policy iteration
	PolicyIteration ppi = new PolicyIteration(mdp, discount);

	// evaluate returned policy
	double[] util =
	    (new PolicyEvaluation(mdp, discount, ppi.policy)).utility;

	// print results
	System.out.println("Optimal policies:");
	for(int s = 0; s < mdp.numStates; s++)
	    System.out.printf(" %-12s  %-4s  %-4s  %17.12f  %17.12f\n",
			      mdp.stateName[s],
			      mdp.actionName[vpi.policy[s]],
			      mdp.actionName[ppi.policy[s]],
			      vpi.utility[s],
			      util[s]);
	System.out.println();
	System.out.println();


	// animate cat chasing mouse
	RestaurantAnimator animator = new RestaurantAnimator(mdp);
	MdpSimulator simulator = new FixedPolicySimulator(mdp, ppi.policy);
	animator.animateGuiOnly(simulator);

    return;

    }

    // private stuff for parsing command line options and printing
    // error messages

    private static class Options {
	private String filename = null;
//	private int mode = GUI_ONLY;
//	private int anim_steps = 0;

	private Options(String argv[]) {
	    for (int i = 0; i < argv.length; i++) {
		if (filename == null) {
		    filename = argv[i];
		} else
		    throw new RuntimeException("filename specified twice");
	    }
	    if (filename == null)
		throw new RuntimeException("no filename specified");
	}

    }

    private static void printCommandLineError() {
	System.err.println("error parsing command-line arguments.");
	System.err.println("arguments: <filename>");
    }

}
