import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class GenerateMDP_Restaurant {
	
	
	//number of blocks in x and y axis
	public static int num_blocks_x = 5;
	public static int num_blocks_y = 5;
	/*
	 * Generates the MDP file for the 5x5 restaurant with tables at (1,3), (5,4), (5,2) and worker starting at (1,1) and manager at (1,5)
	 */
	public void GenerateMDP(){
		System.out.println("Starting generation of MDP");

		String state = "";
		String actionStates = "";
		int reward;
		StateXY worker = new StateXY();
		StateXY manager = new StateXY();
		StateXY table1 = new StateXY();
		StateXY table2 = new StateXY();
		StateXY table3 = new StateXY();
		try {
			PrintWriter pw = new PrintWriter("data/HPMdp.txt");
			
			//set of all possible states with associated rewards according to the MDP formulation
			//outside 2 loops for worker
			for(int i=1;i<=num_blocks_x;i++){
				for(int j=1;j<=num_blocks_y;j++){
					worker.setXY(i,j);
					//inner 2 loops for manager
					for(int k=1; k<=num_blocks_x;k++){
						for(int l=1;l<=num_blocks_y;l++){
							manager.setXY(k,l);
							//last loop for whether the 3 tables are dirty or not.
							for(int m=0;m<=7;m++){
								if(m<=3){
									table1.setXY(1, 3);
								}
								else{
									table1.setXY(0,0);
								}
								
								if(m%4<=1){
									table2.setXY(5, 4);
								}
								else{
									table2.setXY(0,0);
								}
								
								if(m%2==0){
									table3.setXY(5, 2);
								}
								else{
									table3.setXY(0, 0);
								}
								if((worker.equals(table1) || worker.equals(table2) || worker.equals(table3)) && worker.equals(manager)){
									reward = 20;
								}
								else if((worker.equals(table1) || worker.equals(table2) || worker.equals(table3))){
									reward = 10;
								}
								else if(worker.equals(manager)){
									reward = -10;
								}
								else{
									reward = 0;
								}
								
								state = worker.toString()+":"+manager.toString()+":"+table1.toString()+":"+table2.toString()+":"+table3.toString();
								state =state.replaceAll("0", "");
								state = state+"  "+reward;
								pw.write(state+"\n");
							}
							
							
						}
					}
					
				}
			}
			
			//Start State for the MDP
			pw.write("\n"+"1:1:1:5:::::\n\n");
			
			//T(s,a,s') - set of all possible states after action a on state s(actions include moving north, south, east, west, staying)
			for(int i=1;i<=num_blocks_x;i++){
				for(int j=1;j<=num_blocks_y;j++){
					worker.setXY(i,j);
					//inner 2 loops for manager
					for(int k=1; k<=num_blocks_x;k++){
						for(int l=1;l<=num_blocks_y;l++){
							manager.setXY(k,l);
							//last loop for whether the 3 tables are dirty or not.
							for(int m=0;m<=7;m++){
								if(m<=3){
									table1.setXY(1, 3);
								}
								else{
									table1.setXY(0,0);
								}
								
								if(m%4<=1){
									table2.setXY(5, 4);
								}
								else{
									table2.setXY(0,0);
								}
								
								if(m%2==0){
									table3.setXY(5, 2);
								}
								else{
									table3.setXY(0, 0);
								}
								state = worker.toString()+":"+manager.toString()+":"+table1.toString()+":"+table2.toString()+":"+table3.toString();
								state = state.replaceAll("0", "");
								actionStates = calculateStates(worker, manager, table1, table2, table3, 'N');
								pw.write(state + " N "+actionStates+"\n");
								actionStates = calculateStates(worker, manager, table1, table2, table3, 'S');
								pw.write(state + " S "+actionStates+"\n");
								actionStates = calculateStates(worker, manager, table1, table2, table3, 'E');
								pw.write(state + " E "+actionStates+"\n");
								actionStates = calculateStates(worker, manager, table1, table2, table3, 'W');
								pw.write(state + " W "+actionStates+"\n");
								actionStates = calculateStates(worker, manager, table1, table2, table3, 'O');
								pw.write(state + " O "+actionStates+"\n");

							}
							
							
						}
					}
					
				}
			}
			pw.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String []args){
		GenerateMDP_Restaurant gMDP = new GenerateMDP_Restaurant();
		gMDP.GenerateMDP();
		
	}
	
	/*
	 * Returns the different possible states upon action 'direction' on the state (worker:manager:table1:table2:table3)
	 */
	public String calculateStates(StateXY worker,StateXY manager,StateXY table1,StateXY table2,StateXY table3, char direction){
		String states = "";
		String state = "";
		StateXY managerDup = new StateXY(manager);
		StateXY north = new StateXY(0, 1);
		StateXY south = new StateXY(0, -1);
		StateXY east = new StateXY(1, 0);
		StateXY west = new StateXY(-1, 0);
		StateXY rest = new StateXY(0,0);
		
		//probabilites - hardcoded - do not change
		int probability_multiplier = 1;
		float prob1 = (float) 0.20;
		float prob2 = (float) 0.18;
		float prob3 = (float) 0.02;
		float prob4 = (float) 0.162;
		float prob5 = (float) 0.018;
		float prob6 = (float) 0.002;
		float prob7 = (float) 0.1458;
		float prob8 = (float) 0.0162;
		float prob9 = (float) 0.0018;
		float prob10 = (float) 0.0002;
		
		
		//String.format("%.4f",prob10)
		
		switch(direction){
			case 'N': if(worker.canGoAlongDirection(north, num_blocks_x, num_blocks_y))
							worker.goAlongDirection(north);
					break;
			case 'S': if(worker.canGoAlongDirection(south, num_blocks_x, num_blocks_y))
							worker.goAlongDirection(south);
					break;
			case 'E': if(worker.canGoAlongDirection(east, num_blocks_x, num_blocks_y))
							worker.goAlongDirection(east);
					break;
			case 'W': if(worker.canGoAlongDirection(west, num_blocks_x, num_blocks_y))
							worker.goAlongDirection(west);
					break;
			case 'O':
					break;
		}

		StateXY[] directions = new StateXY[5];
		directions[0] = north;
		directions[1] = south;
		directions[2] = east;
		directions[3] = west;
		directions[4] = rest;
		//for all the directions manager can move along, calculate the different probabilites depending on which tables are dirty
		for(StateXY moveAlong : directions){
			
			managerDup.setXY(manager);
			
			if(!managerDup.canGoAlongDirection(moveAlong, num_blocks_x, num_blocks_y)){
				probability_multiplier += 1;
				continue;
			}
			//Add probabilites of other states where movement was not possible to state where manager stays
			if(moveAlong.equals(rest)){
				prob1 = prob1 * probability_multiplier;
				prob2 = prob2 * probability_multiplier;
				prob3 = prob3 * probability_multiplier;
				prob4 = prob4 * probability_multiplier;
				prob5 = prob5 * probability_multiplier;
				prob6 = prob6 * probability_multiplier;
				prob7 = prob7 * probability_multiplier;
				prob8 = prob8 * probability_multiplier;
				prob9 = prob9 * probability_multiplier;
				prob10 = prob10 * probability_multiplier;
			}
		
			
			managerDup.goAlongDirection(moveAlong);
			//tables - check if worker moved to any dirty table
			
			//All 3 tables are dirty: Then there's 5 outcomes, each with probability 20%, for each of the moves the manager can make.
			if(table1.x != 0 && table2.x != 0 && table3.x != 0){
				
				if(!worker.equals(table1)){
					state = worker.toString()+":"+managerDup.toString()+":"+table1.toString();
				}
				else{
					state = worker.toString()+":"+managerDup.toString()+"::";
				}
				
				if(!worker.equals(table2)){
					state = state + ":"+table2.toString();
				}
				else{
					state = state + "::";
				}
				
				if(!worker.equals(table3)){
					state = state + ":"+table3.toString()+" ";
				}
				else{
					state = state + ":: ";
				}
				
				//adding along with probability
				states = states + state + prob1 + " ";
			}
			//Two tables are dirty, one of them are clean: Then there's 10 outcomes, 5 of which are for the table becoming dirty & the managerDup moving (2% each), and 5 of which are for the table staying clean & the managerDup moving (18% each).
			//two tables are dirty(1 and 2)
			else if(table1.x != 0 && table2.x != 0){
				
				if(!worker.equals(table1)){
					state = worker.toString()+":"+managerDup.toString()+":"+table1.toString();
				}
				else{
					state = worker.toString()+":"+managerDup.toString()+"::";
				}
				
				if(!worker.equals(table2)){
					state = state + ":"+table2.toString();
				}
				else{
					state = state + "::";
				}
				
				states = states + state+":x " + prob2 + " ";
				states = states + state+":y " + prob3 + " ";
				
				states = states.replaceAll("x", ":");
				states = states.replaceAll("y", "1:3");
			}
			//two tables are dirty(1 and 3)
			else if(table1.x != 0 && table3.x != 0){
				
				if(!worker.equals(table1)){
					state = worker.toString()+":"+managerDup.toString()+":"+table1.toString();
				}
				else{
					state = worker.toString()+":"+managerDup.toString()+"::";
				}
				
				state = state + ":x";
				
				if(!worker.equals(table3)){
					state = state + ":"+table3.toString()+" ";
				}
				else{
					state = state + ":: ";
				}
				
				states = states + state + prob2 +" ";
				states = states + state.replaceAll("x", "y") + prob3 +" ";
				
				states = states.replaceAll("x", ":");
				states = states.replaceAll("y", "5:2");
			}
			//two tables are dirty(2 and 3)
			else if(table1.x != 0 && table3.x != 0){
				
				state = worker.toString()+":"+managerDup.toString()+":x";
				
				if(!worker.equals(table2)){
					state = state + ":"+table2.toString();
				}
				else{
					state = state + "::";
				}				
				
				if(!worker.equals(table3)){
					state = state + ":"+table3.toString()+" ";
				}
				else{
					state = state + ":: ";
				}
				
				states = states + state + prob2 + " ";
				states = states + state.replaceAll("x", "y") + prob3 + " ";
				
				states = states.replaceAll("x", ":");
				states = states.replaceAll("y", "5:4");
			}
			//One tables is dirty, the other two are clean: Then there's 20 outcomes. 5 for both staying clean (16.2%), 5 for both becoming dirty (0.2%), and 10 for one of the two becoming dirty (1.8%).
			//one table is dirty(1)
			else if(table1.x != 0){
				
				if(!worker.equals(table1)){
					state = worker.toString()+":"+managerDup.toString()+":"+table1.toString();
				}
				else{
					state = worker.toString()+":"+managerDup.toString()+"::";
				}
				
				state = state + ":a";
				state = state + ":x ";
				
				states = states + state + prob4 + " " + state.replaceAll("a","b") + prob5 + " " + state.replaceAll("x", "y") + prob6 + " " + state.replaceAll("b", "a") + prob5 + " ";
				states = states.replaceAll("a", ":");
				states = states.replaceAll("b", "5:4");
				states = states.replaceAll("x", ":");
				states = states.replaceAll("y", "5:2");
			}
			//one table is dirty(2)
			else if(table2.x != 0){
				
				state = worker.toString()+":"+managerDup.toString()+":a";
				
				if(!worker.equals(table2)){
					state = state + ":"+table2.toString();
				}
				else{
					state = state + "::";
				}		
				
				state = state + ":x ";
				
				states = states + state + prob4 + " " + state.replaceAll("a","b") + prob5 + " " + state.replaceAll("x", "y") + prob6 + " " + state.replaceAll("b", "a") + prob5 + " ";
				states = states.replaceAll("a", ":");
				states = states.replaceAll("b", "1:3");
				states = states.replaceAll("x", ":");
				states = states.replaceAll("y", "5:2");
			}
			//one table is dirty(3)
			else if(table3.x != 0){
				
				state = worker.toString()+":"+managerDup.toString()+":a:x";
				
				if(!worker.equals(table3)){
					state = state + ":"+table3.toString()+" ";
				}
				else{
					state = state + ":: ";
				}		
				
				states = states + state + prob4 + " " + state.replaceAll("a","b") + prob5 + " " + state.replaceAll("x", "y") + prob6 + " " + state.replaceAll("b", "a") + prob5 + " ";
				states = states.replaceAll("a", ":");
				states = states.replaceAll("b", "1:3");
				states = states.replaceAll("x", ":");
				states = states.replaceAll("y", "5:4");
			}
			//All 3 tables are clean: Then there's 40 outcomes. 5 for all staying clean (%14.58), 15 for one of the three becoming dirty (%1.62), 15 for two of the three becoming dirty (%0.18), and 5 for all becoming dirty (%0.02).
			//all 3 are clean
			else{
				state = worker.toString()+":"+managerDup.toString()+":a:m:x ";
				
				states = states + state + prob7 + " " + state.replaceAll("x","y") + prob8 + " " + state.replaceAll("m","n") + prob9 + " " + state.replaceAll("y", "x") + prob8 + " " + state.replaceAll("a", "b") + prob9 + " " + state.replaceAll("n", "m") + prob8 + " " + state.replaceAll("x", "y") + prob9 + " " + state.replaceAll("m", "n") + String.format("%.4f",prob10) + " ";
				states = states.replaceAll("a", ":");
				states = states.replaceAll("b", "1:3");
				states = states.replaceAll("m", ":");
				states = states.replaceAll("n", "5:4");
				states = states.replaceAll("x", ":");
				states = states.replaceAll("y", "5:2 ");
				break;
			}
			
		}
	
		
		return states;
	}
}
