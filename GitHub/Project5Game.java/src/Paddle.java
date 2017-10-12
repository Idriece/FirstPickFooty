public class Paddle {

	//Declare instance variables
	private int x;
	private int width;
	private int height;
	private int speed;
	private int direction;
	private int courtWidth;

	public Paddle (int courtWidth)  {
	
		width = 100;
		height = 10;
		speed = 0;
		direction = 0;
		if(courtWidth >= width)
		{
			this.courtWidth = courtWidth;
		}
		x = (this.courtWidth / 2) - (width / 2);
	}

	public Paddle (int width, int courtWidth)  {
	
		if(width < 0)
		{
			this.width = 100;
		}
		else
		{
			this.width = width;
		}
		if(courtWidth < 0 || courtWidth < width)
		{
			this.courtWidth = 700;
		}
		else
		{
			this.courtWidth = courtWidth;
		}
		height = 10;
		speed = 0;
		direction = 0;
		x = (this.courtWidth / 2) - (width / 2);
		
	}

	public int getX() { 

		return x;
	}  
	
	public int getDirection() {

		return direction;
	}
	
	public int getSpeed() {

		return speed;
	}

	public int getWidth() {

		return width;
	}
		
	public int getHeight() {

		return height;
	}  
	
	public void setDirection(int direction)   {

		if(direction >= -1 && direction <= 1)
		{
			this.direction = direction;
		}
	}
	
	public void setSpeed(int speed)   {

		if(speed >= 1)
		{
			this.speed = speed;
		}
	}

	public void move() {
		
		if(x >= 0 && x + width <= courtWidth)
		{
			if(direction == -1)
			{
				x = x - speed;
			}
			else if(direction == 1)
			{
				x = x + speed;
			}
			else if(direction == 0)
			{
				x = x;
			}
		}
		else
		{
			direction = -direction;
			if(direction == -1)
			{
				x = x - speed;
			}
			else if(direction == 1)
			{
				x = x + speed;
			}
			else if(direction == 0)
			{
				x = x;
			}
		}

	}
	
	public boolean isCollision(Ball aBall) {
	
		boolean isCollision;
		if(aBall.getX() + aBall.getRadius() >= x  && aBall.getX() + aBall.getRadius() <= x + width && aBall.getY() - aBall.getRadius() <= height)
		{
			isCollision = true;
		}
		//else if(aBall.getY() + aBall.getRadius() <= height)
		//{
			//isCollision = true;
		//}
		else
		{
			isCollision = false;
		}
		return isCollision;
	}
	
	public void center()  {
	
		x = (this.courtWidth / 2) - (width / 2);
	}

	public String toString() {
		
		return "x: " + x + "paddle width: " + width;
	}

} 
