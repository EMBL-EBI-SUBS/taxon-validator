package uk.ac.ebi.subs.validator.taxon.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Taxonomy {

    private String taxId;
    private String scientificName;
    private String commonName;
    private boolean formalName;
    private boolean submittable;

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

    public boolean isSubmittable() {
        return submittable;
    }

    public void setSubmittable(boolean submittable) {
        this.submittable = submittable;
    }

    @Override
    public String toString() {
        return "Taxonomy{" +
                "taxId='" + taxId + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", commonName='" + commonName + '\'' +
                ", formalName=" + formalName +
                ", submittable=" + submittable +
                '}';
    }
}