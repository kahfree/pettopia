package a2.service;

import a2.model.Customer;
import a2.model.Event;
import a2.model.Orders;
import a2.repository.CustomerRepository;
import a2.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> findAll() { return (List<Event>) eventRepository.findAll(); }
    public Optional<Event> getEventByID(int id){ return eventRepository.findById(id);}
    public Page<Event> findAllPageable(int pageNo, int pageSize){
        Pageable paging = PageRequest.of(pageNo,pageSize);
        return eventRepository.findAll(paging);
    }
    public Event addEvent(Event event){ return eventRepository.save(event);}
}
