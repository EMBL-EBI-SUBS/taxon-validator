package uk.ac.ebi.subs.validator.taxon;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import uk.ac.ebi.subs.validator.taxon.core.Taxonomy;
import uk.ac.ebi.subs.validator.taxon.core.TaxonomyService;
import uk.ac.ebi.subs.validator.taxon.core.TaxonomyValidator;
import uk.ac.ebi.subs.validator.taxon.utils.TestUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaxonomyValidatorTest {

    private final String SUCCESS_MESSAGE = "Valid taxonomy";
    private final String FAILURE_MESSAGE = "Invalid taxonomy";

    private TaxonomyValidator taxonomyValidator;
    private Taxonomy taxonomy;

    public TaxonomyValidatorTest() {
        taxonomyValidator = new TaxonomyValidator();

        TaxonomyService taxonomyService = mock(TaxonomyService.class);
        taxonomyValidator.taxonomyService = taxonomyService;

        taxonomy = TestUtils.createTaxonomy("9606", "Homo sapiens", "human", true);

        when(taxonomyService.getTaxonById(taxonomy.getId())).thenReturn(taxonomy);
        when(taxonomyService.getTaxonById("9600000006")).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void validateTaxonIdSuccessTest() {
        String message = taxonomyValidator.validateTaxonId(taxonomy.getId());
        Assert.assertTrue(message.startsWith(SUCCESS_MESSAGE));
    }

    @Test
    public void validateTaxonFailureTest() {
        String message = taxonomyValidator.validateTaxonId("9600000006");
        Assert.assertTrue(message.startsWith(FAILURE_MESSAGE));
    }

    @Test
    public void validateEmptyTaxonTest() {
        String message = taxonomyValidator.validateTaxonId("");
        Assert.assertTrue(message.startsWith(FAILURE_MESSAGE));
    }

    @Test
    public void validateNullTaxonTest() {
        String message = taxonomyValidator.validateTaxonId(null);
        Assert.assertTrue(message.startsWith(FAILURE_MESSAGE));
    }

}
