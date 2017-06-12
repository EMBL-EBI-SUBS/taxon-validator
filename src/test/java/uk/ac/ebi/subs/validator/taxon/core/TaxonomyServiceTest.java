package uk.ac.ebi.subs.validator.taxon.core;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TaxonomyServiceTest {

    private final String HUMAN_TAXON = "Taxonomy{id='9606', scientificName='Homo sapiens', commonName='human', formalName=true}";

    @Autowired
    TaxonomyService taxonomyService;

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
