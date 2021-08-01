package adeo.leroymerlin.cdp;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventServiceTest {

    Logger logger = LoggerFactory.getLogger(EventServiceTest.class);

    @Mock
    private EventRepository eventRepository;

    private EventService eventService;

    @BeforeEach
    void initUseCase() {
        eventService = new EventService(eventRepository);
    }

    @Test
    @Order(1)
    public void testGetEvents(){
        logger.info("start getEvents endpoint Test");

        when(eventRepository.findAllBy()).thenReturn(return2Events());

        try {
            List<Event> events =  eventService.getEvents();
            assertTrue(events != null);
            assertTrue(events.size() == 2);
        } catch (Exception e) {
            logger.error(""+e);
            logger.info("end getEvents endpoint Test");
            assert false;
        }

        logger.info("end getEvents endpoint Test");
    }

    @Test
    @Order(2)
    public void testGetFilteredEvents(){
        logger.info("start getFilteredEvents endpoint Test");

        when(eventRepository.findAllBy()).thenReturn(return2Events());

        try {
            List<Event> events =  eventService.getFilteredEvents("Eleanor");
            assertTrue(events != null);
            assertTrue(events.size() == 1);
            assertThat(events.get(0).getTitle()).isEqualTo("Alcatraz Fest[1]");
            assertTrue(events.get(0).getBands().size() == 1);
            Band band = events.get(0).getBands().iterator().next();
            assertThat(band.getName()).isEqualTo("Megadeth[1]");
            assertTrue(band.getMembers().size() == 1);
            assertThat(band.getMembers().iterator().next().name).isEqualTo("Queen Eleanor Fisher (Ellie)");
        } catch (Exception e) {
            logger.error(""+e);
            logger.info("end getFilteredEvents endpoint Test");
            assert false;
        }

        logger.info("end getFilteredEvents endpoint Test");
    }

    @Test
    @Order(3)
    public void testDeleteEvents(){
        logger.info("start delete endpoint Test");

        eventService.delete(1L);

        verify(eventRepository, times(1)).deleteById(1L);

        logger.info("end delete endpoint Test");
    }

    @Test
    @Order(4)
    public void testUpdateReviewEvent(){
        logger.info("start updateReviewEvent endpoint Test");

        when(eventRepository.findById(any(Long.class))).thenReturn(return1Events());
        when(eventRepository.save(any(Event.class))).thenReturn(null);

        Event newEvent = new Event();
        newEvent.setId(1000L);
        newEvent.setComment("title test ok");
        newEvent.setNbStars(3);

        Event verifEvent = return1Events().get();
        verifEvent.setComment(newEvent.getComment());
        verifEvent.setNbStars(newEvent.getNbStars());

        newEvent.setTitle(verifEvent.getTitle());

        try {

            eventService.updateReviewEvent(1000L, newEvent);

            verify(eventRepository, times(1)).save(any(Event.class));
            ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
            verify(eventRepository).save(argument.capture());
            assertEquals(1000L, argument.getValue().getId());
            assertEquals("title test ok", argument.getValue().getComment());
            assertEquals(3, argument.getValue().getNbStars());

        } catch (Exception e) {
            logger.error(""+e);
            logger.info("end updateReviewEvent endpoint Test");
            assert false;
        }



        logger.info("end updateReviewEvent endpoint Test");
    }

    private Optional<Event> return1Events() {
        Band band1000 = new Band(); band1000.setName("Pink Floyd");
        Band band1001 = new Band(); band1001.setName("Guns n roses");
        Band band1002 = new Band(); band1002.setName("Metallica");
        Band band1003 = new Band(); band1003.setName("Rolling Stones");
        Band band1004 = new Band(); band1004.setName("The Ramones");
        Band band1005 = new Band(); band1005.setName("Megadeth");

        //Members creation
        Member member1001 = new Member(); member1001.setName("Queen Frankie Gross (Fania)");
        Member member1002 = new Member();member1002.setName("Queen Genevieve Clark");
        Member member1003 = new Member();member1003.setName("Queen Veronica Graves");
        Member member1004 = new Member();member1004.setName("Queen Stacey ODoherty (Asya)");
        Member member1005 = new Member();member1005.setName("Queen Gertrude Hudson");
        Member member1006 = new Member();member1006.setName("Queen Madeleine Taylor");
        Member member1007 = new Member();member1007.setName("Queen Jasmine Collier");
        Member member1008 = new Member();member1008.setName("Queen Daisy Burke");
        Member member1009 = new Member();member1009.setName("Queen Aaliyah York");
        Member member1010 = new Member();member1010.setName("Queen Anika Walsh");
        Member member1011 = new Member();member1011.setName("Queen Katy Stone");
        Member member1012 = new Member();member1012.setName("Queen Aliyah Jarvis");
        Member member1013 = new Member();member1013.setName("Queen Constance Carroll");
        Member member1014 = new Member();member1014.setName("Queen Talia Bush");
        Member member1015 = new Member();member1015.setName("Queen Ava Dunlap");
        Member member1016 = new Member();member1016.setName("Queen Haleema Poole");
        Member member1017 = new Member();member1017.setName("Queen Robbie Bender");
        Member member1018 = new Member();member1018.setName("Queen Laila Shelton");
        Member member1019 = new Member();member1019.setName("Queen Eleanor Fisher (Ellie)");

        Set<Member> memberList1000 = new HashSet<>();
        memberList1000.add(member1001);
        memberList1000.add(member1002);
        memberList1000.add(member1003);
        memberList1000.add(member1004);
        memberList1000.add(member1005);
        memberList1000.add(member1006);
        band1000.setMembers(memberList1000);

        Set<Member> memberList1002 = new HashSet<>();
        memberList1002.add(member1010);
        memberList1002.add(member1011);
        memberList1002.add(member1012);
        memberList1002.add(member1013);
        band1002.setMembers(memberList1002);

        Set<Member> memberList1003 = new HashSet<>();
        memberList1003.add(member1014);
        band1003.setMembers(memberList1003);

        Set<Member> memberList1004 = new HashSet<>();
        memberList1004.add(member1015);
        memberList1004.add(member1016);
        band1004.setMembers(memberList1004);



        Event event1000 = new Event();event1000.setId((long)1000); event1000.setTitle("GrasPop Metal Meeting"); event1000.setImgUrl("img/1000.jpeg");

        Set<Band> bandList1000 = new HashSet<>();
        bandList1000.add(band1000);
        bandList1000.add(band1001);
        bandList1000.add(band1002);
        bandList1000.add(band1003);
        bandList1000.add(band1004);
        event1000.setBands(bandList1000);

        Optional<Event> optonnalreturn = Optional.of(event1000);

        return optonnalreturn;
    }

    private List<Event> return2Events() {

        //Band creations
        Band band1000 = new Band(); band1000.setName("Pink Floyd");
        Band band1001 = new Band(); band1001.setName("Guns n roses");
        Band band1002 = new Band(); band1002.setName("Metallica");
        Band band1003 = new Band(); band1003.setName("Rolling Stones");
        Band band1004 = new Band(); band1004.setName("The Ramones");
        Band band1005 = new Band(); band1005.setName("Megadeth");

        //Members creation
        Member member1001 = new Member(); member1001.setName("Queen Frankie Gross (Fania)");
        Member member1002 = new Member();member1002.setName("Queen Genevieve Clark");
        Member member1003 = new Member();member1003.setName("Queen Veronica Graves");
        Member member1004 = new Member();member1004.setName("Queen Stacey ODoherty (Asya)");
        Member member1005 = new Member();member1005.setName("Queen Gertrude Hudson");
        Member member1006 = new Member();member1006.setName("Queen Madeleine Taylor");
        Member member1007 = new Member();member1007.setName("Queen Jasmine Collier");
        Member member1008 = new Member();member1008.setName("Queen Daisy Burke");
        Member member1009 = new Member();member1009.setName("Queen Aaliyah York");
        Member member1010 = new Member();member1010.setName("Queen Anika Walsh");
        Member member1011 = new Member();member1011.setName("Queen Katy Stone");
        Member member1012 = new Member();member1012.setName("Queen Aliyah Jarvis");
        Member member1013 = new Member();member1013.setName("Queen Constance Carroll");
        Member member1014 = new Member();member1014.setName("Queen Talia Bush");
        Member member1015 = new Member();member1015.setName("Queen Ava Dunlap");
        Member member1016 = new Member();member1016.setName("Queen Haleema Poole");
        Member member1017 = new Member();member1017.setName("Queen Robbie Bender");
        Member member1018 = new Member();member1018.setName("Queen Laila Shelton");
        Member member1019 = new Member();member1019.setName("Queen Eleanor Fisher (Ellie)");

        Set<Member> memberList1000 = new HashSet<>();
        memberList1000.add(member1001);
        memberList1000.add(member1002);
        memberList1000.add(member1003);
        memberList1000.add(member1004);
        memberList1000.add(member1005);
        memberList1000.add(member1006);
        band1000.setMembers(memberList1000);

        Set<Member> memberList1001 = new HashSet<>();
        memberList1001.add(member1007);
        memberList1001.add(member1008);
        memberList1001.add(member1009);
        band1001.setMembers(memberList1001);


        Set<Member> memberList1002 = new HashSet<>();
        memberList1002.add(member1010);
        memberList1002.add(member1011);
        memberList1002.add(member1012);
        memberList1002.add(member1013);
        band1002.setMembers(memberList1002);

        Set<Member> memberList1003 = new HashSet<>();
        memberList1003.add(member1014);
        band1003.setMembers(memberList1003);

        Set<Member> memberList1004 = new HashSet<>();
        memberList1004.add(member1015);
        memberList1004.add(member1016);
        band1004.setMembers(memberList1004);

        Set<Member> memberList1005 = new HashSet<>();
        memberList1005.add(member1017);
        memberList1005.add(member1018);
        memberList1005.add(member1019);
        band1005.setMembers(memberList1005);

        Event event1000 = new Event();event1000.setId((long)1000); event1000.setTitle("GrasPop Metal Meeting"); event1000.setImgUrl("img/1000.jpeg");
        Event event1001 = new Event();event1001.setId((long)1000); event1001.setTitle("Alcatraz Fest"); event1001.setImgUrl("img/1001.jpeg");

        Set<Band> bandList1000 = new HashSet<>();
        bandList1000.add(band1000);
        bandList1000.add(band1001);
        bandList1000.add(band1002);
        bandList1000.add(band1003);
        bandList1000.add(band1004);
        event1000.setBands(bandList1000);

        Set<Band> bandList1001 = new HashSet<>();
        bandList1001.add(band1005);
        event1001.setBands(bandList1001);

        List<Event> returnList = new ArrayList<>();
        returnList.add(event1000);
        returnList.add(event1001);

        return returnList;
    }

}
