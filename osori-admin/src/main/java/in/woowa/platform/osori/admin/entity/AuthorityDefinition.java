package in.woowa.platform.osori.admin.entity;

import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.config.exception.ProcessErr;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

/**
 * Created by HanJaehyun on 2016. 9. 21..
 * 권한 정의 그룹
 */
@Entity
@Getter
@Builder
public class AuthorityDefinition extends AuditEntity{

    @Id
    @GeneratedValue
    private Long id;

    //FIXME: LAZY로 하면 created 가지고 올 때 null 에러가 남.;
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "projectId")
    private Project project;

    @Audited
    @Setter
    @Column(nullable = false, length = 50)
    private String name;

    @Audited
    private boolean status;

    @Tolerate
    public AuthorityDefinition() {}

    @Where(clause = "status = true")
    @OneToMany(mappedBy = "authorityDefinition", fetch = LAZY, cascade = {PERSIST, REFRESH, MERGE, DETACH} )
    public List<AuthorityNavigationUnit> authorityNavigationUnits = new ArrayList<>();

    @OneToMany(mappedBy = "authorityDefinition", fetch = LAZY)
    @Where(clause = "status = true")
    public List<UserAuthorityGrant> userAuthorityGrants = new ArrayList<>();

    public static AuthorityDefinition of(Project project, String name){
        return builder()
                .project(project)
                .name(name)
                .authorityNavigationUnits(new ArrayList<>())
                .status(true)
                .build();
    }

    protected List<UserAuthorityGrant> getUserAuthorityGrants() {
        return userAuthorityGrants;
    }

    public void expire(){
        super.expired = new Date();
        status = false;

        this.getAuthorityNavigationUnits().forEach(unit -> {
            unit.expire();
        });

        this.getUserAuthorityGrants().forEach(grant -> {
            grant.expire();
        });
    }

    public List<MenuNavigation> getMenuNavigations(){
        return this.getAuthorityNavigationUnits()
                .stream()
                .map(AuthorityNavigationUnit::getMenuNavigation)
                .collect(Collectors.toList());
    }

    public void addBy(MenuNavigation menuNavigation){
        if(menuNavigation.getId() == null)
            throw new ApplicationException(ProcessErr.WRONG_DEVELOP_PROCESS);

        if(this.getMenuNavigations().contains(menuNavigation))
            throw new IllegalArgumentException("이미 해당 메뉴는 등록되어 있습니다.");

        AuthorityNavigationUnit unit = new AuthorityNavigationUnit(this, menuNavigation);
        this.getAuthorityNavigationUnits().add(unit);
    }

    public void removeBy(MenuNavigation menuNavigation){
        this.getAuthorityNavigationUnits().forEach(unit -> {
            if(unit.getMenuNavigation().equals(menuNavigation))
                unit.expire();
        });
    }

}
