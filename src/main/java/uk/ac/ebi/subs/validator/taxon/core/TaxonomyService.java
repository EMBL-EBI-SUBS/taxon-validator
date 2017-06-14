package uk.ac.ebi.subs.validator.taxon.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaxonomyService {

    private final String ENA_TAXONOMY_SERVICE = "http://www.ebi.ac.uk/ena/data/taxonomy/v1/taxon/";

    @Autowired
    private RestTemplate restTemplate;

    public Taxonomy getTaxonById(String id) {
        return restTemplate.getForObject(ENA_TAXONOMY_SERVICE + "tax-id/" + id, Taxonomy.class);
    }

    public Taxonomy getTaxonByScientificName(String scientificName) {
        Taxonomy[] array = restTemplate.getForObject(ENA_TAXONOMY_SERVICE + "scientific-name/" + scientificName, Taxonomy[].class);
        return array[0];
    }

    public Taxonomy getTaxonByTaxonomicName(String taxonomicName) {
        Taxonomy[] array = restTemplate.getForObject(ENA_TAXONOMY_SERVICE + "any-name/" + taxonomicName, Taxonomy[].class);
        return array[0];
    }
}
