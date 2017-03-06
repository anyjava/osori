package in.woowa.platform.osori.admin.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;

/**
 * Created by HanJaehyun on 2016. 9. 21..
 */
@Entity
@Getter
@Builder
public class User extends AuditEntity{

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Setter
    @Column(nullable = false, length = 30)
    private String name;

    @Audited
    @Setter
    @Column(length = 50)
    private String department;

    @Audited
    @Setter
    private boolean isPrivacy;

    @Audited
    @Setter
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Where(clause = "status = true")
    @OneToMany(mappedBy = "user", fetch = LAZY, cascade = {PERSIST, REFRESH, MERGE, DETACH})
    private List<UserAuthorityGrant> userAuthorityGrants = new ArrayList<>();

    @Where(clause = "status = true")
    @OneToMany(mappedBy = "user", fetch = LAZY, cascade = {PERSIST, REFRESH, MERGE, DETACH})
    private List<UserPersonalGrant> userPersonalGrants = new ArrayList<>();

    @Tolerate
    public User(){}

    public static User of(String email, String name){
        return builder()
                .email(email)
                .name(name)
                .isPrivacy(false)
                .status(Status.wait)
                .userAuthorityGrants(new ArrayList<>())
                .userPersonalGrants(new ArrayList<>())
                .build();
    }

    public static User of(String email, String name, Status status){
        return builder()
                .email(email)
                .name(name)
                .isPrivacy(false)
                .status(status)
                .userAuthorityGrants(new ArrayList<>())
                .userPersonalGrants(new ArrayList<>())
                .build();
    }

    public void expire(){
        this.status = Status.expire;
        super.expired = new Date();
    }

    public void addBy(AuthorityDefinition authorityDefinition){
        if(this.getAuthorityDefinitions().contains(authorityDefinition))
            throw new IllegalArgumentException("이미 해당 권한 그룹은 허용되어 있습니다.");

        UserAuthorityGrant userAuthorityGrant = new UserAuthorityGrant(this, authorityDefinition);
        this.userAuthorityGrants.add(userAuthorityGrant);
    }

    public void removeBy(AuthorityDefinition authorityDefinition){

        this.getUserAuthorityGrants().forEach(grant -> {
            if(grant.getAuthorityDefinition().equals(authorityDefinition))
                grant.expire();
        });
    }

    public void addBy(MenuNavigation menuNavigation){
        if(this.getMenuNavigations().contains(menuNavigation))
            throw new IllegalArgumentException("이미 해당 권한 그룹은 허용되어 있습니다.");

        UserPersonalGrant userPersonalGrant = new UserPersonalGrant(this, menuNavigation);
        this.userPersonalGrants.add(userPersonalGrant);
    }

    public void removeBy(MenuNavigation menuNavigation){
        this.getUserPersonalGrants().forEach(grant ->{
            if(grant.getMenuNavigation().equals(menuNavigation))
                grant.expire();
        });
    }

    /**
     * Client_User Status
     */
    public enum Status {
        /* 허가 */
        allow,
        /* 불가 */
        reject,
        /* 대기 */
        wait,
        /* 만료 */
        expire
    }

    public List<AuthorityDefinition> getAuthorityDefinitions(){
        return this.getUserAuthorityGrants()
                .stream()
                .map(UserAuthorityGrant::getAuthorityDefinition)
                .collect(Collectors.toList());
    }

    public List<MenuNavigation> getMenuNavigations(){
        return this.getUserPersonalGrants()
                .stream()
                .map(UserPersonalGrant::getMenuNavigation)
                .collect(Collectors.toList());
    }

    public List<Project> getProjects(){
        List<Project> projects = new ArrayList<>();

        this.getAuthorityDefinitions().forEach(authorityDefinition -> {
            Project project = authorityDefinition.getProject();
            if(!projects.contains(project))
                projects.add(project);
        });

        return projects;
    }

    public List<UserAuthorityGrant> getUserAuthorityGrants() {
        return userAuthorityGrants;
    }

    public List<UserPersonalGrant> getUserPersonalGrants() {
        return userPersonalGrants;
    }
}
