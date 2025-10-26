import functions.FunctionPoint;
import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.InappropriateFunctionPointException;
import functions.FunctionPointIndexOutOfBoundsException;

public class Main {
	public static void main(String[] args) {
		// Тестирование ArrayTabulatedFunction
		System.out.println("=== Тестирование ArrayTabulatedFunction ===");
		testFunction(new ArrayTabulatedFunction(-3, 4, 15));

		System.out.println("\n=== Тестирование LinkedListTabulatedFunction ===");
		testFunction(new LinkedListTabulatedFunction(-3, 4, 15));

		// Тестирование исключений
		System.out.println("\n=== Тестирование исключений ===");
		testExceptions();
	}

	private static void testFunction(TabulatedFunction function) {
		// Заполняем значениями y = x^2 + 2
		for (int i = 0; i < function.getPointsCount(); i++) {
			double x = function.getPointX(i);
			function.setPointY(i, x * x + 2);
		}

		// Выводим начальные значения
		System.out.println("Начальные значения функции:");
		printFunctionValues(function);

		// Тестируем операции с точками
		System.out.println("\nОперации с точками:");
		try {
			// Изменяем первую точку (корректно - x остается в пределах)
			function.setPoint(0, new FunctionPoint(-2.9, 5));
			System.out.println("Успешно изменили точку 0");

			// Изменяем среднюю точку
			function.setPointX(5, -0.5);
			function.setPointY(5, 2.4);
			System.out.println("Успешно изменили точку 5");

			// Добавляем новую точку
			function.addPoint(new FunctionPoint(2.7, 8.35));
			System.out.println("Успешно добавили точку (2.7, 8.35)");

		} catch (InappropriateFunctionPointException e) {
			System.out.println("Ошибка при изменении точки: " + e.getMessage());
		}

		printFunctionValues(function);

		// Тестируем получение значения функции
		System.out.println("\nЗначение функции в различных точках:");
		System.out.println("f(-2.5) = " + function.getFunctionValue(-2.5));
		System.out.println("f(0) = " + function.getFunctionValue(0));
		System.out.println("f(2.3) = " + function.getFunctionValue(2.3));
		System.out.println("f(5) = " + function.getFunctionValue(5)); // За пределами области определения

		// Тестируем удаление точек
		System.out.println("\nУдаление точек:");
		try {
			function.deletePoint(2);
			System.out.println("Успешно удалили точку 2");
			printFunctionValues(function);
		} catch (IllegalStateException | FunctionPointIndexOutOfBoundsException e) {
			System.out.println("Ошибка при удалении точки: " + e.getMessage());
		}
	}

	private static void testExceptions() {
		// Тестирование исключений при создании функций
		System.out.println("1. Тестирование исключений при создании:");
		try {
			new ArrayTabulatedFunction(5, 3, 10); // leftX > rightX
		} catch (IllegalArgumentException e) {
			System.out.println("Поймано исключение: " + e.getMessage());
		}

		try {
			new LinkedListTabulatedFunction(0, 5, 1); // pointsCount < 2
		} catch (IllegalArgumentException e) {
			System.out.println("Поймано исключение: " + e.getMessage());
		}

		// Тестирование исключений при работе с точками
		System.out.println("\n2. Тестирование исключений при работе с точками:");
		TabulatedFunction func = new ArrayTabulatedFunction(0, 5, 3);

		printFunctionValues(func);

		try {
			func.getPoint(-1); // Неверный индекс
		} catch (FunctionPointIndexOutOfBoundsException e) {
			System.out.println("Поймано исключение при getPoint(-1): " + e.getMessage());
		}

		try {
			func.getPoint(5); // Неверный индекс
		} catch (FunctionPointIndexOutOfBoundsException e) {
			System.out.println("Поймано исключение при getPoint(5): " + e.getMessage());
		}

		try {
			// Пытаемся установить точку с X, нарушающим порядок
			func.setPoint(0, new FunctionPoint(3, 10));   // X=2 > следующей точки - ошибка
			func.setPoint(1, new FunctionPoint(4.5, 10)); // Корректно
		} catch (InappropriateFunctionPointException e) {
			System.out.println("Поймано исключение при setPoint: " + e.getMessage());
		}

		try {
			// Пытаемся добавить точку с существующим X
			func.addPoint(new FunctionPoint(2.5, 5));
			func.addPoint(new FunctionPoint(2.5, 10)); // Дублирование X
		} catch (InappropriateFunctionPointException e) {
			System.out.println("Поймано исключение при addPoint: " + e.getMessage());
		}

		// Тестирование исключений при удалении точек
		System.out.println("\n3. Тестирование исключений при удалении:");
		TabulatedFunction smallFunc = new ArrayTabulatedFunction(0, 2, 2); // Всего 2 точки

		try {
			smallFunc.deletePoint(0); // Останется 1 точка - ошибка
		} catch (IllegalStateException e) {
			System.out.println("Поймано исключение при deletePoint: " + e.getMessage());
		}
	}

	private static void printFunctionValues(TabulatedFunction function) {
		System.out.println("x:\t\ty:");
		for (int i = 0; i < function.getPointsCount(); i++) {
			System.out.printf("%.3f\t\t%.3f\n", function.getPointX(i), function.getPointY(i));
		}
		System.out.println("Область определения: [" + function.getLeftDomainBorder() + ", " + function.getRightDomainBorder() + "]");
		System.out.println("Количество точек: " + function.getPointsCount());
	}
}