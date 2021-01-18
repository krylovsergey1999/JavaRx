package ru.education.examples;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ru.education.domain.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Krylov Sergey (17.01.2021)
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class OperatorsExample {
	public static void main(String[] args) throws Exception {
		//simpleExample();
		publisherExample();
		System.in.read();
	}

	public static void simpleExample() {
		List<Person> persons = List.of(
				new Person("John", "Dow", "male", LocalDate.of(1992, 3, 12)),
				new Person("Jane", "Dow", "female", LocalDate.of(2001, 6, 23)),
				new Person("Howard", "Lovecraft", "male", LocalDate.of(1890, 8, 20)),
				new Person("Joanne", "Rowling", "female", LocalDate.of(1965, 6, 30)));

		Observable.fromIterable(persons)
				.filter(
						person -> person.getBirth().isAfter(LocalDate.of(1990, 1, 1))
				)
				.map(p -> p.getFirstName() + " " + p.getLastName())
				.toList()
				.subscribe(System.out::println);

		// Сделаем вывод ПЕРВОГО пользователя женского пола
		/*Observable.fromIterable(persons)
				.filter(
						person -> "female".equals(person.getGender())
				)
				.map(p -> p.getFirstName() + " " + p.getLastName())
				.take(1) // или firstElement
				.subscribe(System.out::println);*/
	}

	public static void publisherExample() throws Exception {
		final Observable<String> ob = magicPublisher();
		System.out.println("First subscribed");
		//ob.subscribe(System.out::println);

		// Когда между двумя событиями/значениями будет больше 900 миллисекунд делаем собственно подписку -> вывод
		ob.debounce(900, TimeUnit.MILLISECONDS).subscribe(System.out::println);

//		Thread.sleep(5000);
//		System.out.println("Second subscribed");
//		ob.subscribe(System.out::println);

		// Что произошло странного/интересного?
	}

	public static Observable<String> magicPublisher() {
		Random r = new Random(1); // генерируем рандомные промежутки времени
		AtomicInteger i = new AtomicInteger(); // atomic чтобы использовать в лямбде
		// Пример конвертации холодного в горячий
		final Observable<String> obs = Observable.<String>generate(emitter ->
				emitter.onNext("" + i.incrementAndGet()))
				.concatMap(s -> Observable.just(s).delay(r.nextInt(1000), TimeUnit.MILLISECONDS))
				.subscribeOn(Schedulers.newThread());
		PublishSubject<String> subject = PublishSubject.create(); // работает как "радио" если опоздали значит опоздали

//        BehaviorSubject<String> subject = BehaviorSubject.create();

//        AsyncSubject<String> subject = AsyncSubject.create();
//        CompletableFuture.runAsync(() -> {
//            try {
//                Thread.sleep(7000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            subject.onComplete();
//        });

//        ReplaySubject<String> subject = ReplaySubject.create();

		// Когда подписались, observable стал горячим и он стал кидать элементы
		obs.subscribe(subject);
		return subject;
	}

	//composeExmaple
	private static ObservableTransformer<String, String> filterAndUpperCase() {
		return upstream -> upstream
				.filter(s -> s.length() >= 4)
				.map(String::toUpperCase);
	}
}