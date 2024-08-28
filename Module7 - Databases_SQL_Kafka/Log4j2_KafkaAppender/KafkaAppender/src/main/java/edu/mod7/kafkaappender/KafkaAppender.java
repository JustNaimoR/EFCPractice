package edu.mod7.kafkaappender;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Plugin(
        name = "CustomLogAppender",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE
)
@Component
public class KafkaAppender extends AbstractAppender {
    private KafkaProducerService kafkaProducerService;

    public KafkaAppender(String name, Filter filter) {
        super(name, filter, null);
    }

    private String KAFKA_LOG_TOPIC_NAME;        // my_logs


    @PluginFactory
    public static KafkaAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter
    ) {
        return new KafkaAppender(name, filter);
    }

    @Override
    public void append(LogEvent event) {
        System.out.println("It's working!!!");
    }

    @Autowired
    public void kafkaProducerService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }
}