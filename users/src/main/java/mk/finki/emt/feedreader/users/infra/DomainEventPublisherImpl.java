package mk.finki.emt.feedreader.users.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.sharedkernel.domain.events.DomainEvent;
import mk.finki.emt.feedreader.sharedkernel.infr.DomainEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DomainEventPublisherImpl implements DomainEventPublisher {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public void publish(DomainEvent event) {
    this.kafkaTemplate.send(event.topic(), event.toJson());
  }

  public String toJson(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
