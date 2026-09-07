package org.example.app_configuration_logger_starter;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@AutoConfiguration
@ConditionalOnClass(ApplicationReadyEvent.class)
@ConditionalOnProperty(prefix = "app-configuration-logger", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AppConfigurationLoggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AppConfigLogger.class)
    public AppConfigLogger appConfigLogger(
            Environment environment,
            ObjectProvider<BuildProperties> buildProperties
    ) {

        return new AppConfigLogger(environment,buildProperties);


    }

}
