package uk.ac.ebi.subs.validator.taxon.core;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.subs.validator.taxon.TaxonomyValidatorApplication;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TaxonomyService.class})
public class TaxonomyServiceTest {

    @Autowired
    private TaxonomyService taxonomyService;

    @MockBean
    private RestTemplate restTemplate;

    private Taxonomy taxonomy = new Taxonomy();

    @Test
    public void test_cache() {

        when(restTemplate.getForObject(Mockito.anyString(), eq(Taxonomy.class))).thenReturn(taxonomy);

        taxonomyService.getTaxonById("9606");
        taxonomyService.getTaxonById("9606");

        verify(restTemplate, times(1)).getForObject(Mockito.anyString(), eq(Taxonomy.class));


    }


}
