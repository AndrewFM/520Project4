import java.util.*;
import java.io.*;
import javax.swing.*;          
import javax.swing.event.*;          
import javax.swing.border.*;          
import java.awt.*;
import java.awt.event.*;

/**
 * This class can be used to animate the interaction between a cat and
 * mouse.  The MDP is simulated with the sequence of states being
 * determined by a given <tt>MdpSimulator</tt> object.  The results
 * are either printed to standard output (or another chosen print
 * stream), or they are displayed using a GUI (graphical user
 * interface), or both.
 */
public class RestaurantAnimator {

    /**
     * The constructor for this class for printing results to standard
     * output.  This constructor takes as input an <tt>Mdp</tt>, which
     * must correspond to a cat and mouse world; otherwise, the
     * constructor will almost certainly crash.  Any printed results
     * get sent to standard output.
     */
    public RestaurantAnimator(Mdp mdp) {
	this(mdp, System.out);
    }

    /**
     * The constructor for this class for sending printed results to a
     * given print stream.  This constructor takes as input an
     * <tt>Mdp</tt>, which must correspond to a cat and mouse world;
     * otherwise, the constructor will almost certainly crash.  The
     * constructor also takes a second argument specifying a
     * <tt>PrintStream</tt> to which all of the results are to be
     * printed.
     */
    public RestaurantAnimator(Mdp mdp,
			    PrintStream out) {
	this.mdp = mdp;
	this.out = out;

	cmp = new WorkerManagerPosition[mdp.numStates];

	for (int s = 0; s < mdp.numStates; s++) {
		WorkerManagerPosition cm = cmp[s] = new WorkerManagerPosition(s);

	    if (cm.wx < minx) minx = cm.wx;
	    if (cm.mx < minx) minx = cm.mx;
	    if (cm.wy < miny) miny = cm.wy;
	    if (cm.my < miny) miny = cm.my;

	    if (cm.wx > maxx) maxx = cm.wx;
	    if (cm.mx > maxx) maxx = cm.mx;
	    if (cm.wy > maxy) maxy = cm.wy;
	    if (cm.my > maxy) maxy = cm.my;
	}	    

	lenx = maxx - minx + 1;
	leny = maxy - miny + 1;

	manager_legal = new boolean[lenx][leny];
	worker_legal = new boolean[lenx][leny];

	for (int s = 0; s < mdp.numStates; s++) {
		WorkerManagerPosition cm = cmp[s];

	    worker_legal[cm.wx - minx][cm.wy - miny] = true;
	    manager_legal[cm.mx - minx][cm.my - miny] = true;
	}


    }


    /**
     * This method invokes a graphical animation of the given MDP
     * (provided to the constructor) according to the state sequence
     * provided by the given <tt>simulator</tt>.  A transcript of the
     * animation is not generated.
     */
    public void animateGuiOnly(MdpSimulator simulator) {
    	animateGuiOnly(simulator, DEFAULT_GUI_TITLE);
    }


    /**
     * This method invokes a graphical animation of the given MDP
     * (provided to the constructor) according to the state sequence
     * provided by the given <tt>simulator</tt>, using the provided
     * title.  A transcript of the animation is not generated.
     */
    public void animateGuiOnly(MdpSimulator simulator, String title) {
	animateGui(simulator, false, title);
    }


    // private stuff

    private Mdp mdp;
    private PrintStream out;
    private WorkerManagerPosition[] cmp;

    private int minx = Integer.MAX_VALUE;
    private int miny = Integer.MAX_VALUE;
    private int maxx = Integer.MIN_VALUE;
    private int maxy = Integer.MIN_VALUE;
    private int lenx, leny;
    private boolean manager_legal[][];
    private boolean worker_legal[][];

    private static final String MNG_SYMB = "M";
    //private static final String CHEESE_SYMB = "z";
    private static final String TABLE1_SYMB = "T1";
    private static final String TABLE2_SYMB = "T2";
    private static final String TABLE3_SYMB = "T3";
    		
    private static final String WRK_ON_TABLE_SYMB = "W";
    private static final String WRK_OFF_TABLE_SYMB = "w";

    private class WorkerManagerPosition {
	private int wx, wy, mx, my, t1x, t1y, t2x, t2y, t3x, t3y;
	private boolean table1present, table2present, table3present;
	private WorkerManagerPosition(int s) {
	    //String[] coord = mdp.stateName[s][s].split(":");
	    //0:2:4:6:8:10:12:14:16:18
	    wx = Integer.parseInt(mdp.stateName[s].toCharArray()[0]+"");
	    wy = Integer.parseInt(mdp.stateName[s].toCharArray()[2]+"");
	    mx = Integer.parseInt(mdp.stateName[s].toCharArray()[4]+"");
	    my = Integer.parseInt(mdp.stateName[s].toCharArray()[6]+"");
	    
	    if (mdp.stateName[s].toCharArray()[8] == ':') {
	    	table1present = false;
	    	t1x = t1y = 0;
	    	
	    	if(mdp.stateName[s].toCharArray()[10] == ':'){
	    		table2present = false;
	    		t2x = t2y = 0;
	    		
	    		if(mdp.stateName[s].toCharArray()[12] == ':'){
	    			table3present = false;
	    			t3x = t3y = 0;
	    		}
	    		else{
	    			table3present = true;
	    			t3x = Integer.parseInt(mdp.stateName[s].toCharArray()[12]+"");
			    	t3y = Integer.parseInt(mdp.stateName[s].toCharArray()[14]+"");
	    		}
	    	}
	    	else{
	    		table2present = true;
	    		t2x = Integer.parseInt(mdp.stateName[s].toCharArray()[10]+"");
		    	t2y = Integer.parseInt(mdp.stateName[s].toCharArray()[12]+"");
		    	
		    	if(mdp.stateName[s].toCharArray()[14] == ':'){
	    			table3present = false;
	    			t3x = t3y = 0;
	    		}
	    		else{
	    			table3present = true;
	    			t3x = Integer.parseInt(mdp.stateName[s].toCharArray()[14]+"");
			    	t3y = Integer.parseInt(mdp.stateName[s].toCharArray()[16]+"");
	    		}
	    	}
	    } else {
	    	table1present = true;
	    	t1x = Integer.parseInt(mdp.stateName[s].toCharArray()[8]+"");
	    	t1y = Integer.parseInt(mdp.stateName[s].toCharArray()[10]+"");
	    	
	    	if(mdp.stateName[s].toCharArray()[12] == ':'){
	    		table2present = false;
	    		t2x = t2y = 0;
	    		
	    		if(mdp.stateName[s].toCharArray()[14] == ':'){
	    			table3present = false;
	    			t3x = t3y = 0;
	    		}
	    		else{
	    			table3present = true;
	    			t3x = Integer.parseInt(mdp.stateName[s].toCharArray()[14]+"");
			    	t3y = Integer.parseInt(mdp.stateName[s].toCharArray()[16]+"");
	    		}
	    	}
	    	else{
	    		table2present = true;
	    		t2x = Integer.parseInt(mdp.stateName[s].toCharArray()[12]+"");
		    	t2y = Integer.parseInt(mdp.stateName[s].toCharArray()[14]+"");
		    	
		    	if(mdp.stateName[s].toCharArray()[16] == ':'){
	    			table3present = false;
	    			t3x = t3y = 0;
	    		}
	    		else{
	    			table3present = true;
	    			t3x = Integer.parseInt(mdp.stateName[s].toCharArray()[16]+"");
			    	t3y = Integer.parseInt(mdp.stateName[s].toCharArray()[18]+"");
	    		}
	    	}
	    	
	    }
	}
    }
	    
    private String separator = null;

    private boolean first_printed_state = true;

    private void print_state(int s) {
    WorkerManagerPosition cm = cmp[s];
	String indent = "    ";

	if (separator == null) {
	    separator = indent + "+=";
	    for (int x = minx; x <= maxx; x++)
		separator += "==";
	    separator += "+";
	}

	if (first_printed_state) {
	    out.println(separator);
	    first_printed_state = false;
	}

	String wrk_text = ((cm.wx == cm.t1x && cm.wy == cm.t1y) || (cm.wx == cm.t2x && cm.wy == cm.t2y) || (cm.wx == cm.t2x && cm.wy == cm.t2y)
			     ? WRK_ON_TABLE_SYMB
			     : WRK_OFF_TABLE_SYMB);

	for (int y = maxy; y >= miny; y--) {
	    out.print(indent + "| ");
	    for (int x = minx; x <= maxx; x++) {
		out.print((!manager_legal[x-minx][y-miny]
			   && !worker_legal[x-minx][y-miny])
			  ? " "
			  : (x == cm.mx && y == cm.my
			     ? MNG_SYMB
			     : (x == cm.wx && y == cm.wy
				? wrk_text
				: (x == cm.t1x && y == cm.t1y
				   ? TABLE1_SYMB
							: (x == cm.t2x && y == cm.t2y
							   ? TABLE2_SYMB
										: (x == cm.t3x && y == cm.t3y
										   ? TABLE3_SYMB
				   : (manager_legal[x-minx][y-miny]
				      ? "."
				      : "_")))))));
		out.print(" ");
	    }
	    out.println("|");
	}

	out.println(separator);

    }

    // gui animation stuff

    private static final Font font = new Font("SansSerif", Font.BOLD, 36);
    private static final Border grid_border =
	BorderFactory.createLineBorder(Color.black, 1);
    private static final Color color_main_bg = Color.white;
    private static final Color color_mouse_hole_bg = Color.lightGray;
    private static final Color color_blocked_bg = Color.black;
    private static final Color color_fg = Color.black;
    private static final Border empty_border =
	BorderFactory.createEmptyBorder(10,10,10,10);

    private static final int SLIDER_MAX = 10000;
    private static final int DELAY_MIN = 50;
    private static final int DELAY_MAX = 2000;

    private static final String DEFAULT_GUI_TITLE = "Restaurant";

    private void animateGui(final MdpSimulator simulator,
			    final boolean printingOn,
			    final String title) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GuiApp app = new GuiApp(simulator, printingOn);
		Component contents = app.createComponents();
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
            }
        });
    }

    private class GuiApp {

	private MdpSimulator simulator;
	private boolean printingOn;
	private JLabel label[][];
	private javax.swing.Timer timer;
	private JSlider slider;
	private int cur_state;
	private JButton start_button;
	private JButton stop_button;
	private JButton step_button;


	private GuiApp(MdpSimulator simulator, boolean printingOn) {
	    this.simulator = simulator;
	    this.printingOn = printingOn;
	}

	private Component createComponents() {
	    JPanel grid_pane = new JPanel(new GridLayout(leny, lenx));
	    label = new JLabel[lenx][leny];
	    for(int y = leny-1; y >= 0; y--) {
		for(int x = 0; x < lenx; x++) {
		    label[x][y] = new JLabel(" ", SwingConstants.CENTER);
		    if (manager_legal[x][y] || worker_legal[x][y]) {
			label[x][y].setBorder(grid_border);
			label[x][y].setFont(font);
			Color c = (manager_legal[x][y] ?
				   color_main_bg :
				   (worker_legal[x][y] ?
				    color_mouse_hole_bg :
				    color_blocked_bg));
			label[x][y].setBackground(c);
			label[x][y].setForeground(color_fg);
			label[x][y].setOpaque(true);
		    }
		    grid_pane.add(label[x][y]);
		}
	    }

	    grid_pane.setBorder(empty_border);

	    JPanel main_pane = new JPanel();
	    main_pane.setLayout(new BoxLayout(main_pane, BoxLayout.PAGE_AXIS));
	    main_pane.add(grid_pane);

	    JPanel button_pane = new JPanel();

	    button_pane.setBorder(empty_border);

	    main_pane.add(button_pane);

	    step_button = new JButton("step");
	    step_button.addActionListener(new StepActionListener());

	    start_button = new JButton("start");
	    start_button.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			timer.start();
			step_button.setEnabled(false);
			stop_button.setEnabled(true);
			start_button.setEnabled(false);
		    }
		}
					   );

	    stop_button = new JButton("stop");
	    stop_button.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			timer.stop();
			step_button.setEnabled(true);
			stop_button.setEnabled(false);
			start_button.setEnabled(true);
		    }
		}
					  );

	    stop_button.setEnabled(false);

	    button_pane.add(start_button, BorderLayout.LINE_START);
	    button_pane.add(stop_button, BorderLayout.CENTER);
	    button_pane.add(step_button, BorderLayout.LINE_END);

	    slider = new JSlider(JSlider.HORIZONTAL, 0,
				 SLIDER_MAX, SLIDER_MAX/2);

	    slider.setBorder(empty_border);

	    Dictionary<Integer, JLabel> dict =
		new Hashtable<Integer, JLabel>();
	    dict.put(new Integer(0), new JLabel("slow"));
	    dict.put(new Integer(SLIDER_MAX), new JLabel("fast"));
	    slider.setLabelTable(dict);

	    slider.setPaintLabels(true);


	    slider.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
			if (!slider.getValueIsAdjusting()) {
			    set_timer_delay();
			}
		    }
		}
				     );

	    main_pane.add(slider);

	    timer = new javax.swing.Timer(1000, new StepActionListener());

	    set_timer_delay();

	    cur_state = simulator.nextState();
	    show_cur_state();

	    return main_pane;
	}

	private void set_timer_delay() {
	    double v = slider.getValue() / ((double) SLIDER_MAX);
	    int d = ((int) (v*DELAY_MIN + (1. - v)*DELAY_MAX));
	    timer.setDelay(d);
	}

	private class StepActionListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
		cur_state = simulator.nextState();
		show_cur_state();
	    }
	}

	private WorkerManagerPosition cm = null;

	private void show_cur_state() {
	    if (printingOn)
		print_state(cur_state);

	    if (cm != null) {
		label[cm.wx-minx][cm.wy-miny].setText(" ");
		label[cm.mx-minx][cm.my-miny].setText(" ");
		if (cm.table1present) {
		    label[cm.t1x-minx][cm.t1y-miny].setText(" ");
		    label[cm.t1x-minx][cm.t1y-miny].setBackground(color_main_bg);
		}
		if (cm.table2present) {
		    label[cm.t2x-minx][cm.t2y-miny].setText(" ");
		    label[cm.t2x-minx][cm.t2y-miny].setBackground(color_main_bg);
		}
		if (cm.table3present) {
		    label[cm.t3x-minx][cm.t3y-miny].setText(" ");
		    label[cm.t3x-minx][cm.t3y-miny].setBackground(color_main_bg);
		}
	    }


	    cm = cmp[cur_state];
	    if (cm.table1present) {
	    	label[cm.t1x-minx][cm.t1y-miny].setText(TABLE1_SYMB);
	    	label[cm.t1x-minx][cm.t1y-miny].setBackground(Color.yellow);
	    }
	    if (cm.table2present) {
			label[cm.t2x-minx][cm.t2y-miny].setText(TABLE2_SYMB);
			label[cm.t2x-minx][cm.t2y-miny].setBackground(Color.yellow);
	    }
	    if (cm.table3present) {
			label[cm.t3x-minx][cm.t3y-miny].setText(TABLE3_SYMB);
			label[cm.t3x-minx][cm.t3y-miny].setBackground(Color.yellow);
		}
	    String wrk_text = ((cm.wx == cm.t1x && cm.wy == cm.t1y) || (cm.wx == cm.t2x && cm.wy == cm.t2y) || (cm.wx == cm.t3x && cm.wy == cm.t3y)
				 ? WRK_ON_TABLE_SYMB
				 : WRK_OFF_TABLE_SYMB);
	    label[cm.wx-minx][cm.wy-miny].setText(wrk_text);
	    label[cm.mx-minx][cm.my-miny].setText(MNG_SYMB);
	}

    }

}
