package uk.ac.ebi.subs.validator.taxon.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import uk.ac.ebi.subs.data.component.Archive;
import uk.ac.ebi.subs.data.submittable.Sample;
import uk.ac.ebi.subs.validator.data.SingleValidationResult;
import uk.ac.ebi.subs.validator.data.ValidationStatus;

import java.util.UUID;

@Service
public class TaxonomyValidator {
    public static final Logger logger = LoggerFactory.getLogger(TaxonomyValidator.class);

    private final String SUCCESS_MESSAGE = "Valid taxonomy";
    private final String FAILURE_MESSAGE = "Invalid taxonomy: ";
    private final String SERVICE_UNAVAILABLE = "Service unavailable";

    @Autowired
    public TaxonomyService taxonomyService;

    //TODO - improve
    public SingleValidationResult validateTaxonomy(Sample sample) {

        if (sample.getTaxonId() == null && (sample.getTaxon() == null || sample.getTaxon().isEmpty())) {
            logger.error("Taxonomy can't be null.");

            return generateSingleValidationResult(
                    sample,
                    FAILURE_MESSAGE + "No taxonomy provided.",
                    ValidationStatus.Error);
        }

        try {
            Taxonomy taxonomy;

            if(sample.getTaxonId() != null) {
                taxonomy = taxonomyService.getTaxonById(sample.getTaxonId().toString());
            } else {
                taxonomy = taxonomyService.getTaxonByTaxonomicName(sample.getTaxon());
                sample.setTaxonId(Long.valueOf(taxonomy.getId())); // Are we supposed to do this ???
            }

            if(taxonomy.isSubmittable()) {
                logger.debug(SUCCESS_MESSAGE + " " + taxonomy);
                return generateSingleValidationResult(
                        sample,
                        SUCCESS_MESSAGE,
                        ValidationStatus.Pass
                );
            } else {
                logger.debug(FAILURE_MESSAGE + "Taxonomy not submittable.");
                return generateSingleValidationResult(
                        sample,
                        FAILURE_MESSAGE + "Taxonomy not submittable.",
                        ValidationStatus.Error
                );
            }

        } catch (HttpClientErrorException e) {
            logger.debug(FAILURE_MESSAGE + " id [" + sample.getId() + "]", e);
            return generateSingleValidationResult(
                    sample,
                    FAILURE_MESSAGE + "Taxonomy not found.",
                    ValidationStatus.Error
            );

        } catch (ResourceAccessException e) {
            logger.error(SERVICE_UNAVAILABLE, e);
            return generateSingleValidationResult(
                    sample,
                    FAILURE_MESSAGE + "Service not available",
                    ValidationStatus.Error
            );
        }

    }

    private SingleValidationResult generateSingleValidationResult(Sample sample, String message, ValidationStatus status) {
        SingleValidationResult result = new SingleValidationResult();
        result.setUuid(UUID.randomUUID().toString());
        result.setEntityUuid(sample.getId());
        result.setMessage(message);
        result.setArchive(Archive.Usi); //FIXME - Replace field archive with validation identification
        result.setValidationStatus(status);
        return result;
    }
}
