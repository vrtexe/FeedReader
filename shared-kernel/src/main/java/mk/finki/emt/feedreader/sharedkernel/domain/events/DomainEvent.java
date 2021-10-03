package mk.finki.emt.feedreader.sharedkernel.domain.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import lombok.Getter;

/**
 * The base class representing a domain event that can be published
 * it contains the topic and the instant it was sent as a string value that can later be parsed
 */
@Getter
public class DomainEvent {

  private String topic;

  private String occurredOn;

  public DomainEvent(String topic) {
    this.occurredOn = Instant.now().toString();
    this.topic = topic;
  }

  public String toJson() {
    ObjectMapper objectMapper = new ObjectMapper();
    String output = null;
    try {
      output = objectMapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {}
    return output;
  }

  public String topic() {
    return topic;
  }

  public static <E extends DomainEvent> E fromJson(
    String json,
    Class<E> eventClass
  )
    throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(json, eventClass);
  }
}
