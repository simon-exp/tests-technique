package adeo.leroymerlin.cdp;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import javax.persistence.*;
import java.util.Set;

@Entity
public class Event {
    @Id
    //FIX ISSUE 003: change the strategy
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String imgUrl;

    //FIX ISSUE 003: change the type link and the cascade strategy
    @OneToMany(fetch=FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    private Set<Band> bands;

    private Integer nbStars;

    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<Band> getBands() {
        return bands;
    }

    public void setBands(Set<Band> bands) {
        this.bands = bands;
    }

    public Integer getNbStars() {
        return nbStars;
    }

    public void setNbStars(Integer nbStars) {
        this.nbStars = nbStars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "[id:" + id + " title:" + title + " nbStars:" + nbStars + " comment:" + comment + "]";
    }
}
