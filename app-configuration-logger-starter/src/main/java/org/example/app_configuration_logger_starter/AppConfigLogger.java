package org.example.app_configuration_logger_starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

public class AppConfigLogger implements ApplicationListener<ApplicationReadyEvent> {

    private final static Logger logger = LoggerFactory.getLogger(AppConfigLogger.class);

    private final Environment environment;

    private final ObjectProvider<BuildProperties> buildPropertiesProvider;

    public AppConfigLogger(Environment environment, ObjectProvider<BuildProperties> buildProperties) {
        this.environment = environment;
        this.buildPropertiesProvider = buildProperties;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        BuildProperties buildProperties = buildPropertiesProvider.getIfAvailable();

        String activeProfiles = environment.getActiveProfiles().length == 0 ?
                "default" :
                String.join(",", environment.getActiveProfiles());

        String appVersion = buildProperties != null ?
                buildProperties.getVersion() :
                "not avaliable";

        String datasourceUrl = environment.getProperty("spring.datasource.url", "not configured")
                .replaceFirst("\\?.*$", ""); // удаляем хвост из url

        String datasourceDriverClassName = environment.getProperty(
                "spring.datasource.driver-class-name",
                "not configured"
        );

        logger.info("Application start report log: profiles: {}, version: {}, datasourceUrl={}, driver={}",
                activeProfiles,
                appVersion,
                datasourceUrl,
                datasourceDriverClassName
        );

    }
}
