package uk.ac.ebi.subs.validator.taxon.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.subs.validator.messaging.Queues;
import uk.ac.ebi.subs.validator.messaging.RoutingKeys;
import uk.ac.ebi.subs.validator.messaging.ValidationExchangeConfig;

@Configuration
@ComponentScan(basePackageClasses = ValidationExchangeConfig.class)
public class TaxonQueueConfiguration {

    @Bean
    public Jackson2JsonMessageConverter messageConverter() { return new Jackson2JsonMessageConverter();
    }

    /**
     * Instantiate a {@link Queue} to publish samples for taxonomy validation.
     *
     * @return an instance of a {@link Queue} to publish samples.
     */
    @Bean
    public Queue taxonSampleValidationQueue() {
        return new Queue(Queues.TAXON_SAMPLE_VALIDATION, true);
    }

    /**
     * Create a {@link Binding} between the validation exchange and Taxonomy sample validation queue
     * using the taxonomy routing key for validation events of samples.
     *
     * @param taxonSampleValidationQueue to validate samples taxonomy entries.
     * @param validationExchange {@link TopicExchange} for validation
     * @return a {@link Binding}  between the validation exchange and Taxonomy sample validation queue
     * using the routing key of created samples.
     */
    @Bean
    public Binding taxonSampleValidationBinding(Queue taxonSampleValidationQueue, TopicExchange validationExchange) {
        return BindingBuilder.bind(taxonSampleValidationQueue).to(validationExchange).with(RoutingKeys.EVENT_TAXON_SAMPLE_VALIDATION);
    }
}
