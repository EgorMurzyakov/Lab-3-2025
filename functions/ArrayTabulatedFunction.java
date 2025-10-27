package functions;

public class ArrayTabulatedFunction implements TabulatedFunction{
	private FunctionPoint[] points;
	private int pointsCount;

	public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) { // создаёт объект табулированной функции по заданным левой и правой границе области определения, а также количеству точек для табулирования
		if (leftX >= rightX) {
			throw new IllegalArgumentException("Левая граница области определения должна быть строго меньше правой");
		}

		if (pointsCount < 2) {
			throw new IllegalArgumentException("Количество точек должно быть больше или равно двум");
		}

		this.pointsCount = pointsCount;
		this.points = new FunctionPoint[pointsCount];
		
		double step = (rightX - leftX) / (pointsCount - 1); // шаг
		
		double x;
		for (int i = 0; i < pointsCount; i++) { // создание точек на области определения с равным шагом step
			x = leftX + step * i;
			points[i] = new FunctionPoint(x, 0);
		}
	}

	public ArrayTabulatedFunction(double leftX, double rightX, double[] values) { // аналогичен предыдущему конструктору, но вместо количества точек получает значения функции в виде массива
		if (leftX >= rightX) {
			throw new IllegalArgumentException("Левая граница области определения должна быть строго меньше правой");
		}

		if (values.length < 2) {
			throw new IllegalArgumentException("Количество точек должно быть больше или равно двум");
		}

		this.pointsCount = values.length;
		this.points = new FunctionPoint[values.length];
		
		double step = (rightX - leftX) / (values.length - 1); // шаг
		
		double x;
		for (int i = 0; i < values.length; i++) { // создание точек на области определения с равным шагом step
			x = leftX + step * i;
			points[i] = new FunctionPoint(x, values[i]);
		}
	}

	public double getLeftDomainBorder() {
		return points[0].getX(); // возвращаем абсциссу самой левой (первой) точки
	}

	public double getRightDomainBorder() {
		return points[pointsCount - 1].getX(); // возвращаем абсциссу самой правой (последней) точки	
	}

	public double getFunctionValue(double x) { // возвращаем значение функции в точке x
		if ( x >= points[0].getX() && x <= points[pointsCount - 1].getX() ) {
			int i = 1;
			while (!(points[i].getX() >= x)) { i++; } // ищем индекс точки с абсциссой большей или равной входному значению x
	
			if (Math.abs(points[i - 1].getX() - x) < 1e-10) { // проверяем предыдущую точку на равенство ее абсциссы с входным значением x
				return points[i - 1].getY();
			}
			else if (Math.abs(points[i].getX() - x) < 1e-10) { // проверяем текущую точку на равенство ее абсциссы с входным значением x
				return points[i].getY();
			}
			else { // иначе используем линейную интерполяцию
				double x1 = points[i-1].getX();
				double x2 = points[i].getX();
				double y1 = points[i-1].getY();
				double y2 = points[i].getY();
				return (x - x1) * (y2 - y1) / (x2 - x1) + y1; 
			}
			
		}
		else { // если точка лежит вне области определения функции возвращаем неопределенность
			return Double.NaN;
		}
	}

	public int getPointsCount() { // возвращаем количество точек
		return pointsCount;
	}

	public FunctionPoint getPoint(int index) { // возвращаем копию точки
		if (index < 0 || index >= pointsCount) {
			throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
		}

		return new FunctionPoint(points[index]);
	}

	public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException { // заменяем указанную точку на переданную
		if (index < 0 || index >= pointsCount) {
			throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
		}

		if (index == 0) { // Проверяем граничные случаи
			if (point.getX() < points[index + 1].getX()) {
				points[index] = new FunctionPoint(point);
			}
			else {
				throw new InappropriateFunctionPointException("Координата x заданной точки должна быть меньше следующей");
			}
		}
		else if (index == pointsCount - 1) {
			if (points[index - 1].getX() < point.getX()) {
				points[index] = new FunctionPoint(point);
			}
			else {
				throw new InappropriateFunctionPointException("Координата x заданной точки должна быть больше следующей");
			}
		}
		else {
			if ((point.getX() < points[index + 1].getX()) && (points[index - 1].getX() < point.getX())) {
				points[index] = new FunctionPoint(point);
			}
			else {
				throw new InappropriateFunctionPointException("Координата x заданной точки должна находиться в интервале между соседними точками");
			}
		}
	}

	public double getPointX(int index) { // возвращаем значение абсциссы точки с указанным номером
		if (index < 0 || index >= pointsCount) {
			throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
		}

		return points[index].getX(); 
	}

	public void setPointX(int index, double x) throws InappropriateFunctionPointException { // изменяем значение абсциссы точки с указанным номером
		if (index < 0 || index >= pointsCount) {
			throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
		}

		if (index == 0) { // Проверяем граничные случаи
			if (x < points[index + 1].getX()) {
				points[index].setX(x);
			}
			else {
				throw new InappropriateFunctionPointException("Координата x заданной точки должна быть меньше следующей");
			}
		}
		else if (index == pointsCount - 1) {
			if (points[index - 1].getX() < x) {
				points[index].setX(x);
			}
			else {
				throw new InappropriateFunctionPointException("Координата x заданной точки должна быть больше следующей");
			}
		}
		else {
			if ((x < points[index + 1].getX()) && (points[index - 1].getX() < x)) {
				points[index].setX(x);
			}
			else {
				throw new InappropriateFunctionPointException("Координата x заданной точки должна находиться в интервале между соседними точками");
			}
		}

	}

	public double getPointY(int index) { // возвращаем значение ординаты точки с указанным номером
		if (index < 0 || index >= pointsCount) {
			throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
		}

		return points[index].getY();
	}

	public void setPointY(int index, double y) { // изменяем значение ординаты точки с указанным номером
		if (index < 0 || index >= pointsCount) {
			throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
		}

		points[index].setY(y);
	}

	public void deletePoint(int index) { // удаляем заданную точку
		if (index < 0 || index >= pointsCount) {
			throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
		}

		if (pointsCount < 3) {
			throw new IllegalStateException("Точек в наборе не должно остаться менее двух");
		}

		System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
		points[pointsCount - 1] = null;
		pointsCount--;
	}

	public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException { // добавляем новую точку
		for (int i = 0; i < pointsCount; i++) {
			if (Math.abs(points[i].getX() - point.getX()) < 1e-10) {
				throw new InappropriateFunctionPointException("Координата x новой точки совпадает с абсциссой уже существующей точки");
			}
		}

		if (points.length > pointsCount) { // нет надобности в создании нового массива
			if (point.getX() < points[0].getX()) {
				System.arraycopy(points, 0, points, 1, pointsCount);
				points[0] = new FunctionPoint(point);
				pointsCount++; 
			}
			else if (point.getX() > points[pointsCount - 1].getX()) {
				points[pointsCount] = new FunctionPoint(point);
				pointsCount++;
			}
			else {
				for (int i = 0; i < pointsCount - 1; i++) {
					if ((points[i].getX() < point.getX()) && (points[i + 1].getX() > point.getX())) {
						System.arraycopy(points, i, points, i + 1, pointsCount - i);
						points[i + 1] = new FunctionPoint(point);
						pointsCount++;
					}
				}
			}
		}
		else { // нужно создать массив большей длины
			FunctionPoint[] newArray = new FunctionPoint[pointsCount + 1];

			if (point.getX() < points[0].getX()) {
				System.arraycopy(points, 0, newArray, 1, pointsCount);
				newArray[0] = new FunctionPoint(point);
			}
			else if (point.getX() > points[pointsCount - 1].getX()) {
				System.arraycopy(points, 0, newArray, 0, pointsCount);
				newArray[pointsCount] = new FunctionPoint(point);
			}
			else {
				for (int i = 0; i < pointsCount - 1; i++) {
					if ((points[i].getX() < point.getX()) && (points[i + 1].getX() > point.getX())) {
						System.arraycopy(points, 0, newArray, 0, i + 1);
						System.arraycopy(points, i, newArray, i + 1, pointsCount - i);
						newArray[i + 1] = new FunctionPoint(point);
					}
				}
			}
			pointsCount++;
			points = newArray;	
		}
	}
}