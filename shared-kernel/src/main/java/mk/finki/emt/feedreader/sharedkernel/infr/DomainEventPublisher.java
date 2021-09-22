package mk.finki.emt.feedreader.sharedkernel.infr;

import mk.finki.emt.feedreader.sharedkernel.domain.events.DomainEvent;

public interface DomainEventPublisher {
  void publish(DomainEvent event);
}
