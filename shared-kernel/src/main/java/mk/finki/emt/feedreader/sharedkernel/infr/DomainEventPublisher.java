package mk.finki.emt.feedreader.sharedkernel.infr;

import mk.finki.emt.feedreader.sharedkernel.domain.events.DomainEvent;

/**
 * The base interface used to emit domain events, that can later be intercepted by listeners
 */
public interface DomainEventPublisher {
  void publish(DomainEvent event);
}
