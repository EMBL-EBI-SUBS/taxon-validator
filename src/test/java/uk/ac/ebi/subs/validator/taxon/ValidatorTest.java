package uk.ac.ebi.subs.validator.taxon;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ValidatorTest {

    private final String SUCCESS_MESSAGE = "Valid taxonomy";
    private final String FAILURE_MESSAGE = "Invalid taxonomy";

    @Autowired
    Validator validator;

    @Test
    public void validateTaxonIdSuccessTest() {
        String message = validator.validateTaxonId("9606");
        Assert.assertTrue(message.startsWith(SUCCESS_MESSAGE));
    }

    @Test
    public void validateTaxonFailureTest() {
        String message = validator.validateTaxonId("9600000006");
        Assert.assertTrue(message.startsWith(FAILURE_MESSAGE));
    }

    @Test
    public void validateEmptyTaxonTest() {
        String message = validator.validateTaxonId("");
        Assert.assertTrue(message.startsWith(FAILURE_MESSAGE));
    }

    @Test
    public void validateNullTaxonTest() {
        String message = validator.validateTaxonId(null);
        Assert.assertTrue(message.startsWith(FAILURE_MESSAGE));
    }
}
