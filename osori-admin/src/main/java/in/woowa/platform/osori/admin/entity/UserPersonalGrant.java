package in.woowa.platform.osori.admin.entity;

import lombok.Getter;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;

/**
 * Created by seooseok on 2016. 10. 5..
 */
@Entity
@Getter
@Audited
@Table(name = "UserHasMenuNavigation")
public class UserPersonalGrant extends AuditEntity{

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menuNavigationId", nullable = false, updatable = false)
    private MenuNavigation menuNavigation;

    private boolean status;

    public UserPersonalGrant() {}


    public UserPersonalGrant(User user, MenuNavigation menuNavigation){
        this.user = user;
        this.menuNavigation = menuNavigation;
        this.status = true;
    }

    public void expire(){
        super.expired = new Date();
        status = false;
    }
}
