package uk.ac.ebi.subs.validator.taxon.core;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import uk.ac.ebi.subs.validator.data.SingleValidationResult;
import uk.ac.ebi.subs.validator.data.structures.SingleValidationResultStatus;
import uk.ac.ebi.subs.validator.data.structures.ValidationAuthor;
import uk.ac.ebi.subs.validator.taxon.core.config.RabbitMQDependentTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by karoly on 07/07/2017.
 */
@Category(RabbitMQDependentTest.class)
public class ValidatorListenerTest {

    @Test
    public void testValidationError() {
        List<SingleValidationResult> validationResults = new ArrayList<>();
        validationResults.add(generateSingleValidationResult(SingleValidationResultStatus.Pass));
        validationResults.add(generateSingleValidationResult(SingleValidationResultStatus.Error));

        ValidatorListener listener =
                new ValidatorListener(mock(RabbitMessagingTemplate.class));

        assertThat(listener.hasValidationError(validationResults), is(equalTo(true)));
    }

    private SingleValidationResult generateSingleValidationResult(SingleValidationResultStatus validationStatus) {
        SingleValidationResult singleValidationResult =
                new SingleValidationResult(ValidationAuthor.Taxonomy, UUID.randomUUID().toString());
        singleValidationResult.setMessage("Test message");
        singleValidationResult.setValidationStatus(validationStatus);

        return singleValidationResult;
    }
}
