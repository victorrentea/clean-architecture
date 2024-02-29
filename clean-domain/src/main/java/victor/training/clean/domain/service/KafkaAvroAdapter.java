package victor.training.clean.domain.service;

import victor.training.clean.domain.model.DomainEvent73;

public interface KafkaAvroAdapter {
  void publishEvent(DomainEvent73 email);
}

