import java.util.HashSet;
import java.util.Iterator;

public class Mini_Max {

	static int goal_Node=-1;
	//static HashSet<State_Node> my_Visited_Fringe_Nodes  = new HashSet<State_Node>();	
	//static HashSet<State_Node> enemy_Visited_Fringe_Nodes = new HashSet<State_Node>();	
	static int coop_Val=10000;//this var is being used to choose the minimum steps routh in cases of equal heurstics
	static int game_Type=SingeltonSimulator.getInstance().getGame_Type();
	
	
	static int alpha_Beta_Decision(State_Node state, int cutOff){
		//my_Visited_Fringe_Nodes.add(state);	
		SingeltonSimulator simulator = SingeltonSimulator.getInstance();
		int[][] Weight_Matrix = simulator.getWeight_Matrix();		//only to check if there is an edge between our node to others
		int edge;
		int minCoop=10000;
		int mini_depth=10000;
		int coopCurValue;
		int next_Step=-1;
		int this_depth=0;

		if (state.getMy_Current_Node() == state.getMy_Goal_Node() || cutOff == 0 || state.getEnemy_Current_Node()==state.getEnemy_Goal_Node() ) {																		
			return state_Heuristic_Value(state,this_depth);
		}
		
		int this_alpha=-10000;
		int this_beta=10000;
		int v = -10000;
		int temp_value=0;
		
		
		for (int i = 1; i < Weight_Matrix.length; i++) {
			
			edge = Weight_Matrix[state.getMy_Current_Node()][i];
			if (edge > 0) { 										//check if smart Agent visited contains the new fringe suppose to use to equals we defined 
				State_Node new_State_Node = new State_Node(state);
				if (!new_State_Node.search_if_Path_lock(state.getNodes()[i], true)) { 					//true mean myself not enemy
					//if we succeed its mean there is no more locks at node i, so we can delete all of them in the NEW STATE .
					new_State_Node.getNodes()[i].getNodeLocks().clear();
					new_State_Node.set_State_Node(i, state.getNodes()[i].getNodeKeys(), true);
					new_State_Node.getNodes()[i].clearNodeKeys();
					//if (!search_if_Visited(new_State_Node, true)) {
						temp_value= min_Value(new_State_Node, this_alpha, this_beta, cutOff-1,this_depth+1);
						coopCurValue=new_State_Node.getCoop_Heuristic_Value();
						if(v<temp_value){ //temp_value= the return heuristic
							v=temp_value;
							next_Step=i;
							minCoop=coopCurValue;
							mini_depth=new_State_Node.get_depth();
						}
						
						else if(v==temp_value){ 
							v=temp_value;
							if(mini_depth>new_State_Node.get_depth()){
								minCoop=coopCurValue;
								mini_depth=new_State_Node.get_depth();
								next_Step=i;
							}
							else if (mini_depth==new_State_Node.get_depth()){
								if(game_Type==1){
									if(coopCurValue>minCoop){
										minCoop=coopCurValue;
										mini_depth=new_State_Node.get_depth();
										next_Step=i;
									}
								}
								else{
									if(coopCurValue<minCoop){
										minCoop=coopCurValue;
										mini_depth=new_State_Node.get_depth();
										next_Step=i;
									}
								}
							}
							
						}
						
						this_alpha=Math.max(this_alpha, v);
						if(this_beta<=this_alpha){
							break;
						}
							
					//}
				}
			}
			//System.out.println();
			//System.out.println("checking alpha_Beta_Decision -------------------my turn going to "+i+" DONE------>V= "+v);
		}
		clear_Mini_Max();
		return next_Step;
		 			
		
	}
	
	static int max_Value(State_Node state,int alpha,int beta,int cutOff,int depth){
		//my_Visited_Fringe_Nodes.add(state);	
		SingeltonSimulator simulator = SingeltonSimulator.getInstance();
		int[][] Weight_Matrix = simulator.getWeight_Matrix();		//only to check if there is an edge between our node to others
		int edge;
		int minCoop=10000;
		int coopCurValue;
		int this_depth=depth;
		int mini_depth=10000;
		
		if (state.getMy_Current_Node() == state.getMy_Goal_Node() || cutOff == 0 || state.getEnemy_Current_Node()==state.getEnemy_Goal_Node() ) {																		
			return state_Heuristic_Value(state,depth);
		}
		
		int this_alpha=alpha;
		int this_beta=beta;
		int v = -10000;
		int temp_value=0;
		
		
		for (int i = 1; i < Weight_Matrix.length; i++) {
			edge = Weight_Matrix[state.getMy_Current_Node()][i];
			if (edge > 0) { 										//check if smart Agent visited contains the new fringe suppose to use to equals we defined 
				State_Node new_State_Node = new State_Node(state);
				if (!new_State_Node.search_if_Path_lock(state.getNodes()[i], true)) { 					//true mean myself not enemy
					//if we succeed its mean there is no more locks at node i, so we can delete all of them in the NEW STATE .
					new_State_Node.getNodes()[i].getNodeLocks().clear();
					new_State_Node.set_State_Node(i, state.getNodes()[i].getNodeKeys(), true);
					new_State_Node.getNodes()[i].clearNodeKeys();
					//System.out.println("Mini_Max-->max_Value ----------->my turn--> checking i= "+i);
					//if (!search_if_Visited(new_State_Node, true)) {
						temp_value= min_Value(new_State_Node, this_alpha, this_beta, cutOff-1,this_depth+1);
						coopCurValue=new_State_Node.getCoop_Heuristic_Value();
						if(v<temp_value){ //temp_value= the return heuristic
							v=temp_value;
							minCoop=coopCurValue;
							mini_depth=new_State_Node.get_depth();
							
						}
						else if(v==temp_value){ 
							v=temp_value;
							if(mini_depth>new_State_Node.get_depth()){
								minCoop=coopCurValue;
								mini_depth=new_State_Node.get_depth();
							}
							else if (mini_depth==new_State_Node.get_depth()){
								if(game_Type==1){
									if(coopCurValue>minCoop){
										minCoop=coopCurValue;
										mini_depth=new_State_Node.get_depth();	
									}
								}
								else{
									if(coopCurValue<minCoop){
										minCoop=coopCurValue;
										mini_depth=new_State_Node.get_depth();
									}
								}
							}
							
						}
						
						this_alpha=Math.max(this_alpha, v);
						if(this_beta<=this_alpha){
							//System.out.println("BBBRRRRREEEEEAAAAAAKKK----> checking Max_Value ---- "+"my turn going to i ----> "+i);
							break;
						}
							
					//}
				}
			}
			//System.out.println("checking MAX_Value -------------------my turn going to "+i+" DONE------>V= "+v);
			//System.out.println();
		}
		state.setCoop_Heuristic_Value(minCoop);
		state.set_depth(mini_depth);
		return v;
		
	}
		
	static int  min_Value(State_Node state,int alpha,int beta,int cutOff,int depth){	// we send alpha and put as beta CHANGED BETA WITH ALPHA
		//enemy_Visited_Fringe_Nodes.add(state);
		SingeltonSimulator simulator = SingeltonSimulator.getInstance();
		int[][] Weight_Matrix = simulator.getWeight_Matrix();		//only to check if there is an edge between our node to others
		int edge;
		int minCoop=10000;
		int coopCurValue;
		int this_depth=depth;
		int mini_depth=10000;
		
		if (state.getMy_Current_Node() == state.getMy_Goal_Node() || cutOff == 0 || state.getEnemy_Current_Node()==state.getEnemy_Goal_Node() ) {																		
			return state_Heuristic_Value(state,depth);
		}
		int this_alpha=alpha;
		int this_beta=beta;
		int v = 10000;
		int temp_value=0;
		
		
		for (int i = 1; i < Weight_Matrix.length; i++) {
			edge = Weight_Matrix[state.getEnemy_Current_Node()][i];
			if (edge > 0) { 																			//check if smart Agent visited contains the new fringe suppose to use to equals we defined 
				State_Node new_State_Node = new State_Node(state);
				if (!new_State_Node.search_if_Path_lock(state.getNodes()[i], false)) { 					//true mean myself not enemy
					new_State_Node.getNodes()[i].getNodeLocks().clear();
					new_State_Node.set_State_Node(i, state.getNodes()[i].getNodeKeys(), false);
					new_State_Node.getNodes()[i].clearNodeKeys();
					//System.out.println("checking Min_Value -------------------------------------------------- enemy turn going to i ----> "+i);
					//if (!search_if_Visited(new_State_Node, false)) {
						
						temp_value= max_Value(new_State_Node, this_alpha, this_beta, cutOff-1,this_depth+1);
						coopCurValue=new_State_Node.getCoop_Heuristic_Value();
							
						if(v>temp_value){ //temp_value= the return heuristic
							v=temp_value;
							minCoop=coopCurValue;
							mini_depth=new_State_Node.get_depth();
						}
						else if(v==temp_value){ 
							v=temp_value;
							if(mini_depth>new_State_Node.get_depth()){
								minCoop=coopCurValue;
								mini_depth=new_State_Node.get_depth();
							}
							else if (mini_depth==new_State_Node.get_depth()){
								if(game_Type==1){
									if(coopCurValue>minCoop){
										minCoop=coopCurValue;
										mini_depth=new_State_Node.get_depth();	
									}
								}
								else{
									if(coopCurValue<minCoop){
										minCoop=coopCurValue;
										mini_depth=new_State_Node.get_depth();
									}
								}
							}
							
						}
						
						this_beta=Math.min(this_beta, v);
						
						if(this_beta<=this_alpha){
						//	System.out.println("BBBRRRRREEEEEAAAAAAKKK----> checking Min_Value ---- "+"enemy turn going to i ----> "+i);
							break;
						}
							
					//}
				}
			}
			//System.out.println("checking Min_Value -------------------enemy turn going to "+i+" DONE------>V= "+v);
			//System.out.println();
		}
		state.setCoop_Heuristic_Value(minCoop);
		state.set_depth(mini_depth);
		return v;
		
	}
						
	
	private static int state_Heuristic_Value(State_Node state,int depth){
		state.print_State_Node();
		SingeltonSimulator simulator = SingeltonSimulator.getInstance();
		int game_Type = simulator.getGame_Type();
		Dijkstra my_dijkstra = new Dijkstra(simulator.getWeight_Matrix(), state.getMy_Current_Node(), state.getNodes(), state.getMy_Goal_Node());
		my_dijkstra.run_Dijkstra_Locks(state.getMy_Graph_Path_Keys());
		int my_Heuristic = my_dijkstra.find_Distance_at_sGroup(state.getMy_Goal_Node());
		Dijkstra enemy_dijkstra = new Dijkstra(simulator.getWeight_Matrix(), state.getEnemy_Current_Node(), state.getNodes(), state.getEnemy_Goal_Node());
		enemy_dijkstra.run_Dijkstra_Locks(state.getEnemy_Graph_Path_Keys());
		int enemy_Heuristic = enemy_dijkstra.find_Distance_at_sGroup(state.getEnemy_Goal_Node());
		int heuristic=-99999;
		state.setCoop_Heuristic_Value((enemy_Heuristic+my_Heuristic));
		state.set_depth(depth);
		switch (game_Type){
			
			case 1://Zero Sum game
				heuristic= (enemy_Heuristic-my_Heuristic);
				//System.out.println("heuristic= (enemy_Heuristic-my_Heuristic);*****************---------------- "+heuristic);
				break;
			case 2://Semi-Co game
				heuristic= (enemy_Heuristic-my_Heuristic);
				break;
			case 3://Full co game
				heuristic = (-1)*(enemy_Heuristic+my_Heuristic);
				break;
	
		}
		return heuristic;
	}
	
	private static void clear_Mini_Max(){
		goal_Node=-1;
		//my_Visited_Fringe_Nodes  = new HashSet<State_Node>();	
		//enemy_Visited_Fringe_Nodes = new HashSet<State_Node>();	
	}
}
