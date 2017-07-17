package uk.ac.ebi.subs.validator.taxon.core;

import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.messaging.converter.MessageConverter;
import uk.ac.ebi.subs.validator.data.SingleValidationResult;
import uk.ac.ebi.subs.validator.data.ValidationAuthor;
import uk.ac.ebi.subs.validator.data.ValidationStatus;

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
public class ValidatorListenerTest {

    @Test
    public void testValidationError() {
        List<SingleValidationResult> validationResults = new ArrayList<>();
        validationResults.add(generateSingleValidationResult(ValidationStatus.Pass));
        validationResults.add(generateSingleValidationResult(ValidationStatus.Error));

        ValidatorListener listener =
                new ValidatorListener(mock(RabbitMessagingTemplate.class));

        assertThat(listener.hasValidationError(validationResults), is(equalTo(true)));
    }

    private SingleValidationResult generateSingleValidationResult(ValidationStatus validationStatus) {
        SingleValidationResult singleValidationResult =
                new SingleValidationResult(ValidationAuthor.Taxonomy, UUID.randomUUID().toString());
        singleValidationResult.setMessage("Test message");
        singleValidationResult.setValidationStatus(validationStatus);

        return singleValidationResult;
    }
}
