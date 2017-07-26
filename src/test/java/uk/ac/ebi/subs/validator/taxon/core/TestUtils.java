package uk.ac.ebi.subs.validator.taxon.core;

public class TestUtils {

    public static Taxonomy createTaxonomy(String id, String scientificName, String commonName, boolean formalName, boolean submittable) {
        Taxonomy taxon = new Taxonomy();
        taxon.setTaxId(id);
        taxon.setScientificName(scientificName);
        taxon.setCommonName(commonName);
        taxon.setFormalName(formalName);
        taxon.setSubmittable(submittable);
        return taxon;
    }
}
