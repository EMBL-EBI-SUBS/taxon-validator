package uk.ac.ebi.subs.validator.taxon.core;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import uk.ac.ebi.subs.data.submittable.Sample;
import uk.ac.ebi.subs.validator.data.SingleValidationResult;
import uk.ac.ebi.subs.validator.data.ValidationStatus;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaxonomyValidatorTest {

    private final String SUCCESS_MESSAGE = "Valid taxonomy";
    private final String FAILURE_MESSAGE = "Invalid taxonomy: ";

    private TaxonomyValidator taxonomyValidator;
    private Taxonomy taxonomy;
    private Sample sample;

    public TaxonomyValidatorTest() {
        taxonomyValidator = new TaxonomyValidator();

        TaxonomyService taxonomyService = mock(TaxonomyService.class);
        taxonomyValidator.taxonomyService = taxonomyService;

        taxonomy = TestUtils.createTaxonomy("9606", "Homo sapiens", "human", true, true);

        sample = new Sample();
        sample.setId(UUID.randomUUID().toString());

        when(taxonomyService.getTaxonById(taxonomy.getTaxId())).thenReturn(taxonomy);
        when(taxonomyService.getTaxonById(String.valueOf(96000006L))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(taxonomyService.getTaxonByTaxonomicName("human")).thenReturn(taxonomy);
    }

    @Test
    public void validateTaxonIdSuccessTest() {
        sample.setTaxonId(Long.valueOf(taxonomy.getTaxId()));

        SingleValidationResult result = taxonomyValidator.validateTaxonomy(sample);
        Assert.assertTrue(result.getValidationStatus().equals(ValidationStatus.Pass));
    }

    @Test
    public void validateTaxonFailureTest() {
        sample.setTaxonId(96000006L);

        SingleValidationResult result = taxonomyValidator.validateTaxonomy(sample);
        Assert.assertTrue(result.getMessage().startsWith(FAILURE_MESSAGE));
    }

    @Test
    public void validateNullTaxonTest() {
        SingleValidationResult result = taxonomyValidator.validateTaxonomy(sample);
        Assert.assertTrue(result.getMessage().startsWith(FAILURE_MESSAGE));
    }

    @Test
    public void validateByTaxonomicName() {
        sample.setTaxon("human");

        SingleValidationResult result = taxonomyValidator.validateTaxonomy(sample);
        Assert.assertTrue(result.getValidationStatus().equals(ValidationStatus.Error));
    }
}
