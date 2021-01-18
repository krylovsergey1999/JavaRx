package ru.education.examples;

import io.reactivex.Observable;

/**
 * @author Krylov Sergey (17.01.2021)
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class CreateExamples {

	public static void main(String[] args) {
		Observable<String> obs = justExample();
		//Observable<String> obs = createExample();
		//Observable<String> obs = deferExample();
		obs.forEach(System.out::println); // Подписка => alias для subscribe
		obs.forEach(System.out::println);

//		Observable<String> obs1 = createExample();
//		Disposable disposable = obs1.forEach(System.out::println);
//		disposable.dispose();
	}

	public static Observable<String> justExample() {
		// Observable который моментально вернет 3 элемента
		// Он содержит внутри себя все данные и каждому новому подписчику будет возвращать все эти данные
		return Observable.just("one", "two", "three");
	}

	public static Observable<String> createExample() {
		// emitter - который позволит событие дальше из observable
		return Observable.create(emitter -> {
			// Проверяем не отписались ли мы
			if (emitter.isDisposed()) {
				return;
			}
			emitter.onNext("one"); // кидаем следующий объект
			emitter.onNext("two"); //!
			emitter.onNext("three");
			if (!emitter.isDisposed()) {
				emitter.onComplete();
			}
		});
	}

	public static Observable<String> deferExample() {
		// Откладывает создание Observable до момента как мы на него подпишемся, т.е немного ленивый
		return Observable.defer(() -> Observable.just("one", "two", "three"));
	}
}