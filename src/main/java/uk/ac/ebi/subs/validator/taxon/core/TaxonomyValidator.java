package uk.ac.ebi.subs.validator.taxon.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import uk.ac.ebi.subs.data.submittable.Sample;
import uk.ac.ebi.subs.validator.data.SingleValidationResult;
import uk.ac.ebi.subs.validator.data.structures.SingleValidationResultStatus;
import uk.ac.ebi.subs.validator.data.structures.ValidationAuthor;

/**
 * Validates the given sample's {@link Taxonomy} data retrieved from ENA's database.
 */
@Service
public class TaxonomyValidator {
    public static final Logger logger = LoggerFactory.getLogger(TaxonomyValidator.class);

    private final String FAILURE_MESSAGE = "Invalid taxonomy: ";

    @Autowired
    public TaxonomyService taxonomyService;

    public SingleValidationResult validateTaxonomy(Sample sample) {

        if (sample.getTaxonId() == null) {
            logger.trace("Taxon ID can't be null.");
            return generateSingleValidationResult(sample, FAILURE_MESSAGE + "No taxonID provided.", SingleValidationResultStatus.Error);
        }

        if (sample.getTaxon() == null || sample.getTaxon().isEmpty()) {
            logger.trace("Taxon can't be null or empty.");
            return generateSingleValidationResult(sample, FAILURE_MESSAGE + "No taxon provided.", SingleValidationResultStatus.Error);
        }

        try {
            Taxonomy taxonomy = taxonomyService.getTaxonById(sample.getTaxonId().toString());

            if (taxonomy == null) {
                logger.trace("Invalid Taxonomy ID: {}.", sample.getTaxonId());
                return generateSingleValidationResult(sample, FAILURE_MESSAGE + "Invalid taxonomy ID: {}." + sample.getTaxonId(), SingleValidationResultStatus.Error);
            }

            if (!taxonomy.isSubmittable()) {
                logger.trace(FAILURE_MESSAGE + "Taxonomy not submittable.");
                return generateSingleValidationResult(sample, FAILURE_MESSAGE + "Taxonomy not submittable.", SingleValidationResultStatus.Error);
            }

            if (sample.getTaxon() != null && ! taxonomy.getScientificName().equals(sample.getTaxon()) ){
                logger.trace(FAILURE_MESSAGE + "Taxonomy not submittable.");

                String errorMessage = MessageFormatter.arrayFormat(
                        FAILURE_MESSAGE + "Taxon should match the scientific name from the INSDC taxonomy database ({}), but is {}",
                        new String[]{taxonomy.getScientificName(),sample.getTaxon()}
                ).getMessage();

                return generateSingleValidationResult(sample, errorMessage, SingleValidationResultStatus.Error);
            }

            logger.debug("Valid taxonomy: " + taxonomy);
            return generateSingleValidationResult(sample, null, SingleValidationResultStatus.Pass);

        } catch (HttpClientErrorException e) {
            logger.debug(FAILURE_MESSAGE + " id [" + sample.getId() + "]", e);
            return generateSingleValidationResult(
                    sample,
                    FAILURE_MESSAGE + "Taxonomy not found.",
                    SingleValidationResultStatus.Error
            );
        }
    }

    private SingleValidationResult generateSingleValidationResult(Sample sample, String message, SingleValidationResultStatus status) {
        SingleValidationResult result = new SingleValidationResult();
        result.setEntityUuid(sample.getId());
        result.setMessage(message);
        result.setValidationAuthor(ValidationAuthor.Taxonomy);
        result.setValidationStatus(status);
        return result;
    }
}
