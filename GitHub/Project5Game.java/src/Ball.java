import java.util.Random;

public class Ball {


	//Declare instance variables
	private int x;
	private int y;
	private int color;
	private int radius;
	private int speed;
	private int xDir;
	private int yDir;
	private int courtWidth;
	private int courtHeight;

	public Ball(int x, int y,  int speed, int xDir, int yDir, int courtWidth, int courtHeight) {
	
		//this.x = x;
		//this.y = y;
		if(speed < 1 || speed > 5)
		{
			this.speed = 1;
		}
		else
		{
			this.speed = speed;
		}
		if(xDir != 1 && xDir!= -1)
		{
			this.xDir = 1;
		}
		else
		{
			this.xDir = xDir;
		}
		if(yDir != 1 && yDir!= -1)
		{
			this.yDir = 1;
		}
		else
		{
			this.yDir = yDir;
		}
		if(courtWidth <= 2 * radius)
		{
			this.courtWidth = 700;
		}
		else
		{
			this.courtWidth = courtWidth;
		}
		if(courtHeight <= 2 * radius)
		{
			this.courtHeight = 300;
		}
		else
		{
			this.courtHeight = courtHeight;
		}
		if(x > courtWidth || x < 0)
		{
			this.x = courtWidth / 2;
		}
		else
		{
			this.x = x;
		}
		if(y > courtHeight || y < 0)
		{
			this.y = courtHeight / 2;
		}
		else
		{
			this.y = y;
		}
			
		color = 0 + (int)(Math.random() * ((6 - 0) + 1));
		radius = 15 + (int)(Math.random() * ((20 - 15) + 1));
	}
	
	public Ball(int x, int y, int radius, int courtWidth, int courtHeight) {
	
		if(x > courtWidth || x < 0)
		{
			this.x = courtWidth / 2;
		}
		else
		{
			this.x = x;
		}
		if(y > courtHeight || y < 0)
		{
			this.y = courtHeight / 2;
		}
		else
		{
			this.y = y;
		}
		if(radius < 15)
		{
			this.radius = 15;
		}
		else
		{
			this.radius = radius;
		}
		this.courtWidth = courtWidth;
		this.courtHeight = courtHeight;
		speed = 1;
		xDir = 1;
		yDir = 1;
		color = 0 + (int)(Math.random() * ((6 - 0) + 1));

	}
	
	public int  getRadius() {

		return radius;
	}
	
	public int  getX() {

		return x;
	}
	
	public int  getY() {

		return y;
	}
	
	public int getColor() {

		return color;
	}	
	
	public int getSpeed() {

		return speed;
	}
	
	public int getXDir() {

		return xDir;
	}
        
	public int getYDir() {

		return yDir;
	}
    
	public void setColor(int color) {
	
		if(color >= 0 && color <= 6)
		{
			this.color = color;
		}
	}

	public void setX(int x) {
	
		if(x >= 0 && x <= courtWidth)
		{
			this.x = x;
		}
	}
		
	public void setY(int y) {
	
		if(y >= 0 && y <= courtHeight)
		{
			this.y = y;
		}
	}

	public void flipYDir() {
	
		yDir = -yDir;
	}
	
	public void flipXDir() {
	
		xDir = -xDir;
	}

	public void move() {
		
	
		if(x - radius <= 0 || x + radius >= courtWidth)
		{
			flipXDir();
		}
		
		if(y - radius <= 0 || y + radius >= courtHeight)
		{
			flipYDir();
		}
		if(xDir > 0)
		{
			x = x + speed;
		}
		if(xDir < 0)
		{
			x = x - speed;
		}
		if(yDir > 0)
		{
			y = y + speed;
		}
		if(yDir < 0)
		{
			y = y - speed;
		}
		
		
		
		
	}
	
	public boolean isCollision(Ball anotherBall) {

		boolean isCollision;
		if(Math.sqrt(Math.pow(anotherBall.x - x, 2) + Math.pow(anotherBall.y - y, 2)) <= radius + anotherBall.radius)
		{
			isCollision = true;
		}
		else 
			{
			isCollision = false;
			}
		return isCollision;
	}
	
	public void increaseSpeed() {
	
		if(speed < 5)
		{
			speed++;
		}
	}
	
	public void decreaseSpeed() {	
	
		if(speed > 1)
		{
			speed--;
		}
	}
	
	public String toString() {

		return "Radius: " + radius + "\nCenter: " + "(" + x + ", " + y + ")";
	}
    
}
