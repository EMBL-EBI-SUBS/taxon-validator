package uk.ac.ebi.subs.validator.taxon.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.subs.data.submittable.Sample;
import uk.ac.ebi.subs.messaging.Exchanges;
import uk.ac.ebi.subs.validator.data.SampleValidationMessageEnvelope;
import uk.ac.ebi.subs.validator.data.SingleValidationResult;
import uk.ac.ebi.subs.validator.data.SingleValidationResultsEnvelope;
import uk.ac.ebi.subs.validator.data.structures.SingleValidationResultStatus;
import uk.ac.ebi.subs.validator.data.structures.ValidationAuthor;

import java.util.Collections;
import java.util.List;

import static uk.ac.ebi.subs.validator.taxon.messaging.TaxonValidatorQueues.TAXON_SAMPLE_VALIDATION;
import static uk.ac.ebi.subs.validator.taxon.messaging.TaxonValidatorRoutingKeys.EVENT_VALIDATION_ERROR;
import static uk.ac.ebi.subs.validator.taxon.messaging.TaxonValidatorRoutingKeys.EVENT_VALIDATION_SUCCESS;

@Service
public class ValidatorListener {
    private static Logger logger = LoggerFactory.getLogger(ValidatorListener.class);

    private RabbitMessagingTemplate rabbitMessagingTemplate;


    private TaxonomyValidator validator;

    public ValidatorListener(RabbitMessagingTemplate rabbitMessagingTemplate, TaxonomyValidator validator) {
        this.rabbitMessagingTemplate = rabbitMessagingTemplate;
        this.validator = validator;
    }

    @RabbitListener(queues = TAXON_SAMPLE_VALIDATION)
    public void handleValidationRequest(SampleValidationMessageEnvelope envelope) {
        logger.info("Got sample to validate with ID: {}.", envelope.getEntityToValidate().getId());

        Sample sample = envelope.getEntityToValidate();
        SingleValidationResult singleValidationResult = validator.validateTaxonomy(sample);

        List<SingleValidationResult> validationResults = Collections.singletonList(singleValidationResult);

        logger.info("Taxonomy validation done.");

        sendResults(
            buildSingleValidationResultsEnvelope(validationResults, envelope.getValidationResultVersion(), envelope.getValidationResultUUID()), hasValidationError(validationResults)
        );
    }

    private void sendResults(SingleValidationResultsEnvelope singleValidationResultsEnvelope, boolean hasValidationError) {
        if (hasValidationError) {
            rabbitMessagingTemplate.convertAndSend(Exchanges.SUBMISSIONS, EVENT_VALIDATION_ERROR, singleValidationResultsEnvelope);
        } else {
            rabbitMessagingTemplate.convertAndSend(Exchanges.SUBMISSIONS, EVENT_VALIDATION_SUCCESS, singleValidationResultsEnvelope);
        }
    }

    private SingleValidationResultsEnvelope buildSingleValidationResultsEnvelope(List<SingleValidationResult> validationResults, int validationResultVersion, String validationResultUUID) {
        return new SingleValidationResultsEnvelope(
                validationResults, validationResultVersion, validationResultUUID, ValidationAuthor.Taxonomy
        );
    }

    boolean hasValidationError(List<SingleValidationResult> validationResults) {
        SingleValidationResult errorValidationResult = validationResults.stream().filter(
                validationResult -> validationResult.getValidationStatus() == SingleValidationResultStatus.Error)
                .findAny()
                .orElse(null);

        return errorValidationResult != null;
    }
}
