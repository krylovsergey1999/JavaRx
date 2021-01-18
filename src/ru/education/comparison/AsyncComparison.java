package ru.education.comparison;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;

/**
 * Упрощенный пример.
 *
 * @author Krylov Sergey (17.01.2021)
 */
public class AsyncComparison {

	public static void main(String[] args) throws IOException {
		final long timeStarted = System.currentTimeMillis();
		final Observable<String> obs = controller();
		obs.subscribe(System.out::println); // Когда придут данные выведи их
		System.out.println("Wait time " + (System.currentTimeMillis() - timeStarted));
		System.in.read(); // Это для того чтобы программа сразу не остановилась
	}

	static Observable<String> controller() {
		return service();
	}

	static Observable<String> service() {
		return repository();
	}

	static Observable<String> repository() {
		return database();
	}

	static Observable<String> database() {
		return Observable.defer(() -> {
			try {
				Thread.sleep(4000);
			} catch (Exception e) {
				System.out.println("Don't do this");
			}
			return Observable.just("Result");
		}).subscribeOn(Schedulers.newThread()); // При таком подходе у нас будет в новом потоке выполняться
		// Если же сделать так: .subscribeOn() то будет в том же потоке синхронно и время будет около 4100
	}
}