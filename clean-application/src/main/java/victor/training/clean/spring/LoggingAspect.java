package victor.training.clean.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletOutputStream;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.stream.Collectors.joining;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
	@Target(METHOD)
	@Retention(RUNTIME)
	public @interface NotLogged {
	}

	private final ObjectMapper jackson = new ObjectMapper();
	
	@PostConstruct
	public void configureMapper() {
		if (log.isTraceEnabled()) {
			jackson.enable(SerializationFeature.INDENT_OUTPUT);
		}
	}
	@Around("@within(victor.training.clean.application.ApplicationService))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		logBefore(joinPoint);
		long t0 = System.currentTimeMillis();
		
		Object returnedObject;
		try {
			returnedObject = joinPoint.proceed();
		} catch (Exception e) {
			long deltaMillis = System.currentTimeMillis() - t0;
			log.error("Facade threw exception ({}): {}: {}",
					formatDeltaTime(deltaMillis),
					e.getClass(),
					e.getMessage());
			throw e;
		}
		logAfter(joinPoint, returnedObject, t0);
		return returnedObject;
	}
	public static String formatDeltaTime(long millis) {
		return String.format("%.03f sec", millis / 1000f);
	}

	private boolean shouldNotBeLogged(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		return method.isAnnotationPresent(NotLogged.class);
	}

	private void logBefore(ProceedingJoinPoint joinPoint) {		
		if (log.isDebugEnabled() ) {
			String separator = log.isTraceEnabled() ? "\n" : ", ";
			String argListConcat;
			if (shouldNotBeLogged(joinPoint)) {
				argListConcat = "@NotLogged";
			} else {
				argListConcat = Stream.of(joinPoint.getArgs())
						.map(this::objectToJson)
						.map(Object::toString)
						.collect(joining(separator));
			}

			String currentUser = "TBD"; // TODO from SecurityContextHolder or from a custom Thread scoped bean
			log.debug("Invoking {}.{}(..) (user:{}): {}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName(),

				currentUser, argListConcat);
		}
	}
	
	private void logAfter(ProceedingJoinPoint joinPoint, Object returnedObject, long t0) {
		if (log.isDebugEnabled()) {
			long deltaMillis = System.currentTimeMillis() - t0;
			String returnString = "<not logged>";
			if (!shouldNotBeLogged(joinPoint)) {
				returnString = objectToJson(returnedObject);
			}
			log.debug("Return from {}.{} ({}): {}",
					joinPoint.getTarget().getClass().getSimpleName(),
					joinPoint.getSignature().getName(),
					formatDeltaTime(deltaMillis),
					returnString);
		}
	}

	private String objectToJson(Object object) {
		try {
			if (object instanceof ServletOutputStream) {
				return "<ServletOutputStream>";
			} else if (object instanceof InputStream) {
				return "<InputStream>";
			} else {
				return jackson.writeValueAsString(object);
			}
		} catch (JsonProcessingException e) {
			log.warn("Could not serialize as JSON (stacktrace on TRACE): " + e);
			log.trace("Cannot serialize value: " + e, e);

			return "<ERR>";
		}
	}

}
