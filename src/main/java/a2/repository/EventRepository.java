package a2.repository;

import a2.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EventRepository extends CrudRepository<Event, Integer>, PagingAndSortingRepository<Event, Integer> {
}
