package uk.ac.ebi.subs.validator.taxon.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Taxonomy {

    private String id;
    private String scientificName;
    private String commonName;
    private boolean formalName;
    private boolean submittable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
                "id='" + id + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", commonName='" + commonName + '\'' +
                ", formalName=" + formalName +
                ", submittable=" + submittable +
                '}';
    }
}
