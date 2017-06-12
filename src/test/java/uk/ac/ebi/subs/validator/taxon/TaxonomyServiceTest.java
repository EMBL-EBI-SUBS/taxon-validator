package uk.ac.ebi.subs.validator.taxon;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.subs.validator.taxon.core.Taxonomy;
import uk.ac.ebi.subs.validator.taxon.core.TaxonomyService;
import uk.ac.ebi.subs.validator.taxon.utils.TestUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaxonomyServiceTest {

    private final String HUMAN_TAXON = "Taxonomy{id='9606', scientificName='Homo sapiens', commonName='human', formalName=true}";

    private TaxonomyService taxonomyService;
    private Taxonomy taxonomy;

    public TaxonomyServiceTest() {
        taxonomyService = mock(TaxonomyService.class);

        taxonomy = TestUtils.createTaxonomy("9606", "Homo sapiens", "human", true);

        when(taxonomyService.getTaxonById(taxonomy.getId())).thenReturn(taxonomy);
        when(taxonomyService.getTaxonByScientificName(taxonomy.getScientificName())).thenReturn(taxonomy);
        when(taxonomyService.getTaxonByTaxonomicName(taxonomy.getCommonName())).thenReturn(taxonomy);
    }

    @Test
    public void getTaxonbyIdTest() {
        Taxonomy taxonomy = taxonomyService.getTaxonById("9606");
        Assert.assertEquals(HUMAN_TAXON, taxonomy.toString());
    }

    @Test
    public void getTaxonByScientificNameTest() {
        Taxonomy taxonomy = taxonomyService.getTaxonByScientificName("Homo sapiens");
        Assert.assertEquals(HUMAN_TAXON, taxonomy.toString());
    }

    @Test
    public void getTaxonByTaxonomicNameTest() {
        Taxonomy taxonomy = taxonomyService.getTaxonByTaxonomicName("human");
        Assert.assertEquals(HUMAN_TAXON, taxonomy.toString());
    }

}
