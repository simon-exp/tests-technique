package adeo.leroymerlin.cdp;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import javax.persistence.*;
import java.util.Set;

@Entity
public class Band {

    @Id
    //FIX ISSUE 003: change the strategy
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //FIX ISSUE 003: change the type link and the cascade strategy
    @OneToMany(fetch=FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    private Set<Member> members;

    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
