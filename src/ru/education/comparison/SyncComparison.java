package ru.education.comparison;

/**
 * Упрощенный пример. Типичный пример логики приложения.
 *
 * @author Krylov Sergey (17.01.2021)
 */
public class SyncComparison {

	public static void main(String[] args) {
		final long timeStarted = System.currentTimeMillis();
		System.out.println(controller());
		System.out.println(System.currentTimeMillis() - timeStarted);
	}

	static String controller() {
		return service();
	}

	static String service() {
		return repository();
	}

	static String repository() {
		return database();
	}

	static String database() {
		try {
			Thread.sleep(4000);
		} catch (Exception e) {
			System.out.println("Don't do this");
		}
		return "Result";
	}
}