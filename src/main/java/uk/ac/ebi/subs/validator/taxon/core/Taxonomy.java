package uk.ac.ebi.subs.validator.taxon.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Taxonomy {

    String taxId;
    String scientificName;
    String commonName;
    boolean formalName;

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public boolean isFormalName() {
        return formalName;
    }

    public void setFormalName(boolean formalName) {
        this.formalName = formalName;
    }

    @Override
    public String toString() {
        return "Taxonomy{" +
                "taxId='" + taxId + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", commonName='" + commonName + '\'' +
                ", formalName=" + formalName +
                '}';
    }
}
