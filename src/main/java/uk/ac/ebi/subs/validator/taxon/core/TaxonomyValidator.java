package uk.ac.ebi.subs.validator.taxon.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import uk.ac.ebi.subs.data.submittable.Sample;
import uk.ac.ebi.subs.validator.data.SingleValidationResult;
import uk.ac.ebi.subs.validator.data.ValidationAuthor;
import uk.ac.ebi.subs.validator.data.ValidationStatus;

import java.util.UUID;

@Service
public class TaxonomyValidator {
    public static final Logger logger = LoggerFactory.getLogger(TaxonomyValidator.class);

    private final String FAILURE_MESSAGE = "Invalid taxonomy: ";

    @Autowired
    public TaxonomyService taxonomyService;

    public SingleValidationResult validateTaxonomy(Sample sample) {

        if (sample.getTaxonId() == null) {
            logger.error("Taxonomy ID can't be null.");
            return generateSingleValidationResult(sample, FAILURE_MESSAGE + "No taxonomy ID provided.", ValidationStatus.Error);
        }

        try {
            Taxonomy taxonomy = taxonomyService.getTaxonById(sample.getTaxonId().toString());

            if (taxonomy == null) {
                logger.error("Ivalid Taxonomy ID: {}.", sample.getTaxonId());
                return generateSingleValidationResult(sample, FAILURE_MESSAGE + "Invalid taxonomy ID: {}." + sample.getTaxonId(), ValidationStatus.Error);
            }

            if (!taxonomy.isSubmittable()) {
                logger.debug(FAILURE_MESSAGE + "Taxonomy not submittable.");
                return generateSingleValidationResult(sample, FAILURE_MESSAGE + "Taxonomy not submittable.", ValidationStatus.Error);
            }

            logger.debug("Valid taxonomy: " + taxonomy);
            return generateSingleValidationResult(sample, null, ValidationStatus.Pass);

        } catch (HttpClientErrorException e) {
            logger.debug(FAILURE_MESSAGE + " id [" + sample.getId() + "]", e);
            return generateSingleValidationResult(
                    sample,
                    FAILURE_MESSAGE + "Taxonomy not found.",
                    ValidationStatus.Error
            );
        }

    }

    private SingleValidationResult generateSingleValidationResult(Sample sample, String message, ValidationStatus status) {
        SingleValidationResult result = new SingleValidationResult();
        result.setUuid(UUID.randomUUID().toString());
        result.setEntityUuid(sample.getId());
        result.setMessage(message);
        result.setValidationAuthor(ValidationAuthor.Taxonomy);
        result.setValidationStatus(status);
        return result;
    }
}
