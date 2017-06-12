package uk.ac.ebi.subs.validator.taxon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import uk.ac.ebi.subs.validator.taxon.core.Taxonomy;
import uk.ac.ebi.subs.validator.taxon.core.TaxonomyService;

@Service
public class Validator {
    public static final Logger logger = LoggerFactory.getLogger(Validator.class);

    private final String SUCCESS_MESSAGE = "Valid taxonomy";
    private final String FAILURE_MESSAGE = "Invalid taxonomy";
    private final String SERVICE_UNAVAILABLE = "Service unavailable";

    @Autowired
    public TaxonomyService taxonomyService;

    public String validateTaxonId(String id) {
        if (id == null || id.isEmpty()) {
            return FAILURE_MESSAGE + " empty id";
        }
        try {
            Taxonomy taxon = taxonomyService.getTaxonById(id);
            logger.debug(SUCCESS_MESSAGE + " " + taxon);

        } catch (HttpClientErrorException e) {
            logger.debug(FAILURE_MESSAGE + " id [" + id + "]");
            return FAILURE_MESSAGE + " id [" + id + "]";

        } catch (ResourceAccessException e) {
            logger.error(SERVICE_UNAVAILABLE, e);
            return SERVICE_UNAVAILABLE;
        }

        return SUCCESS_MESSAGE + " id [" + id + "]";
    }
}
