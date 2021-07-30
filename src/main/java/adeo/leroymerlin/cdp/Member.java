package adeo.leroymerlin.cdp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member {
    @Id
    //FIX ISSUE 003: change the strategy
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
