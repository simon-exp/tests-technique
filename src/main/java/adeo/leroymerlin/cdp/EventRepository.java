package adeo.leroymerlin.cdp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
/*public interface EventRepository extends Repository<Event, Long> {
FIX ISSUE 003: change the extended interface Repository with CrudRepository.
CrudRepository implements basic CRUD operations, including count, delete, deleteById, save, saveAll, findById,
and findAll.*/
public interface EventRepository extends CrudRepository<Event, Long> {


    //FIX ISSUE 002: auto method builder need to precise the field on with the filter is done => replace by void deleteEventById(Long eventId);
    //void delete(Long eventId);
    //FIX ISSUE 004: this method is no more used => replace by deleteById
    //void deleteEventById(Long eventId);


    List<Event> findAllBy();

}
