package uk.ac.ebi.subs.validator.taxon.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.stereotype.Service;
import uk.ac.ebi.subs.data.submittable.Sample;
import uk.ac.ebi.subs.validator.data.SingleValidationResult;
import uk.ac.ebi.subs.validator.data.ValidationMessageEnvelope;
import uk.ac.ebi.subs.validator.data.ValidationStatus;
import uk.ac.ebi.subs.validator.messaging.Exchanges;
import uk.ac.ebi.subs.validator.messaging.Queues;
import uk.ac.ebi.subs.validator.messaging.RoutingKeys;

@Service
public class ValidatorListener {
    private static Logger logger = LoggerFactory.getLogger(ValidatorListener.class);

    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @Autowired
    private TaxonomyValidator validator;

    @Autowired
    public ValidatorListener(RabbitMessagingTemplate rabbitMessagingTemplate, MessageConverter messageConverter) {
        this.rabbitMessagingTemplate = rabbitMessagingTemplate;
        this.rabbitMessagingTemplate.setMessageConverter(messageConverter);
    }

    @RabbitListener(queues = Queues.TAXON_SAMPLE_VALIDATION)
    public void handleValidationRequest(ValidationMessageEnvelope envelope) {
        logger.debug("Got sample to validate taxonomy.");

        Sample sample = (Sample) envelope.getEntityToValidate();
        SingleValidationResult result = validator.validateTaxonomy(sample);

        logger.debug("Taxonomy validation done.");
        
        result.setValidationResultUUID(envelope.getValidationResultUUID());

        sendResults(result);
    }

    private void sendResults(SingleValidationResult singleValidationResult) {
        if (singleValidationResult.getValidationStatus().equals(ValidationStatus.Error)) {
            rabbitMessagingTemplate.convertAndSend(Exchanges.VALIDATION, RoutingKeys.EVENT_VALIDATION_ERROR, singleValidationResult);
        } else {
            rabbitMessagingTemplate.convertAndSend(Exchanges.VALIDATION, RoutingKeys.EVENT_VALIDATION_SUCCESS, singleValidationResult);

        }
    }

}
