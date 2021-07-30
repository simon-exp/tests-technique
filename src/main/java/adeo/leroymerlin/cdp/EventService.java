package adeo.leroymerlin.cdp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventService {

    Logger logger = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    public void delete(Long id) {
        //FIX ISSUE 002: call the nex method withthe filter on id field.
        //eventRepository.delete(id);
        eventRepository.deleteById(id);
    }

    /**
     * method use to retrieve filtered content based on the member names filtering
     * The events are displayed only if at least one band has a member with the name matching the given
     * pattern.
     * @param query String used to filter the event.
     * @return a list of events if and with only the bands containing members who are matching with the filtering value.
     * @throws Exception exception throws when parameters are not valid
     */
    public List<Event> getFilteredEvents(String query) throws Exception {
        logger.debug("start getFilteredEvents method with query: {}", query);

        //check if query is empty or null
        if (query == null){
            logger.error("ERROR when try to get filtered events: query is null");
            throw new Exception("ERROR when try to get filtered events: query is null");
        } else if ("".equals(query)){
            logger.error("ERROR when try to get filtered events: query is empty");
            throw new Exception("ERROR when try to get filtered events: query is empty");
        }

        List<Event> events = eventRepository.findAllBy();
        // Filter the events list in pure JAVA here
        //NEW FEATURE 001: We would like to enable a new route for the API `/search/{query}`. It will allow us to display filtered `events`.
        List<Event> returnEventList = new ArrayList<>();

        /*
        loop on each event and for each event loop on each band
        for each band member if he's match with query the member is saved with only his name.
        each member saved are added to a filtered band (only name and members)
        each filtered bands are setted on event
        the event is added to the filtered event list
         */

        //loop on event (code using fonctionnal concept)
        events.forEach(event -> {
            //create bandList for save the band to display
            Set<Band> newBandList = new HashSet<>();
            //loop on event band list
            event.getBands().forEach(band -> {
                //create filtered member list
                Set<Member> newMemberList = new HashSet<>();
                //loop on band members
                band.getMembers().forEach(member -> {
                    if (member.getName() != null && member.getName().contains(query)) {
                        // save the member with only his name in the  filtered member list
                        Member newMembre = new Member();
                        newMembre.setName(member.getName());
                        newMemberList.add(newMembre);
                    }
                });
                //if we found at least one matching member
                if (newMemberList.size() > 0) {
                    //save band with its name and the filtered member list in the filtered band list
                    Band newBand = new Band();
                    //NEW FEATURE 002: add count of members
                    newBand.setName(band.getName() + "[" + newMemberList.size() + "]");
                    newBand.setMembers(newMemberList);
                    newBandList.add(newBand);
                }
            });
            //if we found at least one matching band
            if(newBandList.size() > 0){
                //change the event band list and save the event in the filtered event list
                event.setBands(newBandList);
                //NEW FEATURE 002: add count of band
                event.setTitle(event.getTitle()+ "[" + newBandList.size() + "]");
                returnEventList.add(event);
            }
        });


        return returnEventList;
    }

    /**
     * FIX ISSUE 003: adding review does not work
     * method used to update an event designated by its id with the value of the event object passed as a parameter
     * @param eventId identifier of the event to update
     * @param event event object containing the values to update
     */
    public void updateReviewEvent (Long eventId, Event event) throws Exception {
        logger.debug("start updateEvent method with eventId: {} and event {}", eventId, event);
        //check if eventId is null
        if(eventId == null) {
            //if eventId is null throw an exception
            logger.error("ERROR when try to update event: eventId is null");
            throw new Exception("ERROR when try to update event: eventId is null");
        }
        //check if event is null
        if(event == null) {
            //if event is null throw an exception
            logger.error("ERROR when try to update event: event is null");
            throw new Exception("ERROR when try to update event: event is null");
        }

        //check if eventId is equals to the id of the event
        if(!eventId.equals(event.getId())) {
            //if event is null throw an exception
            logger.error("ERROR when try to update event: event id is not equals to eventId");
            throw new Exception("ERROR when try to update event: event id is not equals to eventId");
        }

        //check if event exist with the id
        Event existingEvent = eventRepository.findById(eventId).get();
        if (existingEvent != null){
            logger.info("save event (only comment, title and stars) with id {}", eventId);
            existingEvent.setComment(event.getComment());
            existingEvent.setNbStars(event.getNbStars());
            existingEvent.setTitle(event.getTitle());
            eventRepository.save(existingEvent);
        } else {
            logger.info("event with id {} not exist: nothing to do", eventId);
        }
    }
}
