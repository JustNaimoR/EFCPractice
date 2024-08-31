package edu.mod7.kafkaappender;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Plugin(
        name = "CustomAppender",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE,
        printObject = true
)
public class KafkaAppender extends AbstractAppender {
    private KafkaProducerService kafkaProducerService;

    private String KAFKA_LOG_TOPIC_NAME;        // my_logs

    public KafkaAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
    }



    @PluginFactory
    public static KafkaAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout
    ) {
        return new KafkaAppender(name, filter, layout);
    }

    @Override
    public void append(LogEvent event) {
        System.out.println("It's working!!!");
    }
}