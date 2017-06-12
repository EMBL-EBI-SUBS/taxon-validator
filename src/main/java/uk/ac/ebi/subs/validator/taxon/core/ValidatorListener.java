package uk.ac.ebi.subs.validator.taxon.core;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.stereotype.Service;
import uk.ac.ebi.subs.validator.data.ValidationMessageEnvelope;
import uk.ac.ebi.subs.validator.messaging.Queues;

@Service
public class ValidatorListener {

    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @Autowired
    public ValidatorListener(RabbitMessagingTemplate rabbitMessagingTemplate, MessageConverter messageConverter) {
        this.rabbitMessagingTemplate = rabbitMessagingTemplate;
        this.rabbitMessagingTemplate.setMessageConverter(messageConverter);
    }

    @RabbitListener(queues = Queues.TAXON_SAMPLE_VALIDATION)
    public void handleValidationRequest(ValidationMessageEnvelope envelope) {
        //TODO
    }

}
