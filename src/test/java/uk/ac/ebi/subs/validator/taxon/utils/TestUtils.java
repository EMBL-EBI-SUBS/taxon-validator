package uk.ac.ebi.subs.validator.taxon.utils;

import uk.ac.ebi.subs.validator.taxon.core.Taxonomy;

public class TestUtils {

    public static Taxonomy createTaxonomy(String id, String scientificName, String commonName, boolean formalName) {
        Taxonomy taxon = new Taxonomy();
        taxon.setId(id);
        taxon.setScientificName(scientificName);
        taxon.setCommonName(commonName);
        taxon.setFormalName(formalName);
        return taxon;
    }
}
