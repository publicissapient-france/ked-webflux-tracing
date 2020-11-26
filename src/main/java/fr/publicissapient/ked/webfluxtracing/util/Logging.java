package fr.publicissapient.ked.webfluxtracing.util;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Logging {

	public static final String CORRELATION_ID = "correlationId";

	private Logging() {
	}

	public static <T> Consumer<Signal<T>> logOnError(Consumer<T> logStatement) {
		return logIfSignal(logStatement, Signal::isOnError);
	}

	public static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
		return logIfSignal(logStatement, Signal::isOnNext);
	}

	private static <T> Consumer<Signal<T>> logIfSignal(Consumer<T> logStatement, Predicate<Signal<T>> signalPredicate) {
		return signal -> {
			if (signalPredicate.test(signal)) {
				Optional<String> correlationIdMaybe = signal.getContextView().getOrEmpty(CORRELATION_ID);

				correlationIdMaybe.ifPresentOrElse(correlationId -> {
					try (MDC.MDCCloseable ignored = MDC.putCloseable(CORRELATION_ID, correlationId)) {
						logStatement.accept(signal.get());
					}
				}, () -> logStatement.accept(signal.get()));
			}
		};
	}
}
