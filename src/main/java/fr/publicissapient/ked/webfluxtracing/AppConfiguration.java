package fr.publicissapient.ked.webfluxtracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.Optional;

@Configuration
public class AppConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Logger logger(InjectionPoint injectionPoint) {
        return Optional
                .ofNullable(injectionPoint.getMethodParameter())
                .map(AppConfiguration::getTargetClass)
                .map(LoggerFactory::getLogger)
                .orElseThrow(() -> {
                    var constructorMissingParameterError = "Could not inject logger through constructor: missing parameter";
                    return new UnsupportedOperationException(constructorMissingParameterError);
                });
    }

    private static Class<?> getTargetClass(MethodParameter methodParameter) {
        var beanProducingMethodReturnType = Optional.ofNullable(methodParameter.getMethod())
                .map(Method::getReturnType);
        if (beanProducingMethodReturnType.isPresent()) {
            return beanProducingMethodReturnType.get();
        }
        return methodParameter.getContainingClass();
    }
}
