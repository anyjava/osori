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
@Table(name = "UserHasAuthorityDefinition")
public class UserAuthorityGrant extends AuditEntity{

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId",updatable = false, nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "authorityDefinitionId",updatable = false, nullable = false)
    private AuthorityDefinition authorityDefinition;

    private boolean status;

    public UserAuthorityGrant() {}

    public UserAuthorityGrant(User user, AuthorityDefinition authorityDefinition){
        this.user = user;
        this.authorityDefinition = authorityDefinition;
        this.status = true;
    }

    public void expire(){
        super.expired = new Date();
        status = false;
    }
}
