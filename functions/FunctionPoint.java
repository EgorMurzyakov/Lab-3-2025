package functions;

public class FunctionPoint {
	private double x;
	private double y;

	public FunctionPoint(double _x, double _y) { // создаёт объект точки с заданными координатами
		this.x = _x;
		this.y = _y;
	}

	public FunctionPoint(FunctionPoint point) { // создаёт объект точки с теми же координатами, что у указанной точки
		this.x = point.x;
		this.y = point.y;
	}
	
	public FunctionPoint() { // создаёт точку с координатами (0; 0)
		this.x = 0;
		this.y = 0;
	}

	public double getX () { // возвращаем значение x
		return x;
	}

	public double getY () { // возвращаем значение y
		return y;
	}

	public void setX (double _x) { // изменяем значение x
		x = _x;
	}
	
	public void setY(double _y) { // изменяем значение y
		y = _y;
	}
}