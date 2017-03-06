package in.woowa.platform.osori.admin.entity;

import lombok.Getter;
import lombok.experimental.Tolerate;
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
 * Created by seooseok on 2016. 9. 29..
 * 권한 그룹과 네비게이션 매핑
 */
@Entity
@Getter
@Audited
@Table(name = "AuthorityDefinitionHasMenuNavigation")
public class AuthorityNavigationUnit extends AuditEntity{

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "authorityDefinitionId",updatable = false, nullable = false)
    private AuthorityDefinition authorityDefinition;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menuNavigationId", updatable = false, nullable =  false)
    private MenuNavigation menuNavigation;

    private boolean status;

    @Tolerate
    public AuthorityNavigationUnit() {}

    public AuthorityNavigationUnit(AuthorityDefinition authorityDefinition, MenuNavigation menuNavigation){
        this.authorityDefinition = authorityDefinition;
        this.menuNavigation = menuNavigation;
        this.status = true;
    }

    public void expire(){
        super.expired = new Date();
        status = false;
    }

}
