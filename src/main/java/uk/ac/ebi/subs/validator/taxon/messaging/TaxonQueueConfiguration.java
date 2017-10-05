package uk.ac.ebi.subs.validator.taxon.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.subs.messaging.ExchangeConfig;
import uk.ac.ebi.subs.messaging.Queues;

import static uk.ac.ebi.subs.validator.taxon.messaging.TaxonValidatorQueues.TAXON_SAMPLE_VALIDATION;
import static uk.ac.ebi.subs.validator.taxon.messaging.TaxonValidatorRoutingKeys.EVENT_TAXON_SAMPLE_VALIDATION;

@Configuration
@ComponentScan(basePackageClasses = ExchangeConfig.class)
public class TaxonQueueConfiguration {

    /**
     * Instantiate a {@link Queue} to publish samples for taxonomy validation.
     *
     * @return an instance of a {@link Queue} to publish samples.
     */
    @Bean
    public Queue taxonSampleValidationQueue() {
        return Queues.buildQueueWithDlx(TAXON_SAMPLE_VALIDATION);
    }

    /**
     * Create a {@link Binding} between the validation exchange and Taxonomy sample validation queue
     * using the taxonomy routing key for validation events of samples.
     *
     * @param taxonSampleValidationQueue to validate samples taxonomy entries.
     * @param submissionExchange {@link TopicExchange} for validation
     * @return a {@link Binding}  between the validation exchange and Taxonomy sample validation queue
     * using the routing key of created samples.
     */
    @Bean
    public Binding taxonSampleValidationBinding(Queue taxonSampleValidationQueue, TopicExchange submissionExchange) {
        return BindingBuilder.bind(taxonSampleValidationQueue).to(submissionExchange).with(EVENT_TAXON_SAMPLE_VALIDATION);
    }
}
