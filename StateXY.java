/**
 * Used for storing the states of tables, worker and manager in the 5x5 block of the restaurant.
 *
 */
public class StateXY {

	int x;
	int y;
	
	public StateXY(){
		x=0;
		y=0;
	}
	
	public StateXY(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public StateXY(StateXY a){
		this.x = a.x;
		this.y = a.y;
	} 
	
	public void setXY(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public void setXY(StateXY a){
		this.x = a.x;
		this.y = a.y;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	/*
	 * checks if both x and y of the two objects equal each other and returns true if that is the case
	 */
	public boolean equals(StateXY a){
		if(a.getX()==this.getX() && a.getY() == this.getY()){
			return true;
		}
		else{
			return false;
		}
	}
	/*
	 * moves worker or manager along the given direction
	 */
	public void goAlongDirection(StateXY dir){
		this.x = this.x + dir.x;
		this.y = this.y + dir.y;
		
	}
	/*
	 * returns true if the manager or worker can move along a particular direction
	 */
	public boolean canGoAlongDirection(StateXY dir, int x_limit, int y_limit){
		
		if(this.x+dir.x <= x_limit && this.x+dir.x > 0 && this.y+dir.y <= y_limit && this.y+dir.y > 0)
			return true;
		else 
			return false;
		
	}
	/*
	 * returns string in the format x:y
	 */
	public String toString(){
		return this.x+":"+this.y;
	}
}
