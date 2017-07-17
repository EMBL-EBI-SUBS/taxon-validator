package uk.ac.ebi.subs.validator.taxon.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.subs.data.submittable.Sample;
import uk.ac.ebi.subs.validator.data.*;
import uk.ac.ebi.subs.validator.messaging.Exchanges;
import uk.ac.ebi.subs.validator.messaging.Queues;
import uk.ac.ebi.subs.validator.messaging.RoutingKeys;

import java.util.Collections;
import java.util.List;

@Service
public class ValidatorListener {
    private static Logger logger = LoggerFactory.getLogger(ValidatorListener.class);

    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @Autowired
    private TaxonomyValidator validator;

    @Autowired
    public ValidatorListener(RabbitMessagingTemplate rabbitMessagingTemplate) {
        this.rabbitMessagingTemplate = rabbitMessagingTemplate;
    }

    @RabbitListener(queues = Queues.TAXON_SAMPLE_VALIDATION)
    public void handleValidationRequest(ValidationMessageEnvelope<Sample> envelope) {
        logger.debug("Got sample to validate taxonomy.");

        Sample sample = (Sample) envelope.getEntityToValidate();
        SingleValidationResult singleValidationResult = validator.validateTaxonomy(sample);

        logger.debug("Taxonomy validation done.");
        
        singleValidationResult.setValidationResultUUID(envelope.getValidationResultUUID());

        List<SingleValidationResult> validationResults = Collections.singletonList(singleValidationResult);

        sendResults(
            buildSingleValidationResultsEnvelope(validationResults, envelope.getValidationResultVersion(), envelope.getValidationResultUUID()),
            hasValidationError(validationResults)
        );
    }

    private void sendResults(SingleValidationResultsEnvelope singleValidationResultsEnvelope, boolean hasValidationError) {
        if (hasValidationError) {
            rabbitMessagingTemplate.convertAndSend(
                    Exchanges.VALIDATION, RoutingKeys.EVENT_VALIDATION_ERROR, singleValidationResultsEnvelope);
        } else {
            rabbitMessagingTemplate.convertAndSend(
                    Exchanges.VALIDATION, RoutingKeys.EVENT_VALIDATION_SUCCESS, singleValidationResultsEnvelope);

        }
    }

    private SingleValidationResultsEnvelope buildSingleValidationResultsEnvelope(
            List<SingleValidationResult> validationResults, int validationResultVersion, String validationResultUUID) {

        return new SingleValidationResultsEnvelope(
                validationResults, validationResultVersion, validationResultUUID, ValidationAuthor.Taxonomy
        );
    }

    boolean hasValidationError(List<SingleValidationResult> validationResults) {
        SingleValidationResult errorValidationResult = validationResults.stream().filter(
                validationResult -> validationResult.getValidationStatus() == ValidationStatus.Error)
                .findAny()
                .orElse(null);

        return errorValidationResult != null;
    }
}
