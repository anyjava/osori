package in.woowa.platform.osori.admin.entity;

import in.woowa.platform.osori.admin.commons.constant.OsoriConstant.NavigationType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.hibernate.envers.Audited;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;

/**
 * Created by HanJaehyun on 2016. 9. 21..
 */
@Entity
@Getter
public class MenuNavigation extends AuditEntity{

    @Id
    @GeneratedValue
    private Long id;

    private String treeId;

    @Setter
    private String parentTreeId;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @Column(nullable = false, length = 50)
    @Setter
    private String name;

    @Audited
    @ManyToOne
    @JoinColumn(name = "parentId")
    private MenuNavigation parentMenuNavigation;

    @OneToMany(mappedBy = "parentMenuNavigation", fetch = LAZY, cascade = {PERSIST, MERGE, REFRESH,DETACH})
    private List<MenuNavigation> menuNavigationGroup = new ArrayList<>();

    @OneToMany(mappedBy = "menuNavigation", fetch = LAZY, cascade = {PERSIST, MERGE, REFRESH,DETACH})
    private List<AuthorityNavigationUnit> authorityNavigationUnits = new ArrayList<>();

    @OneToMany(mappedBy = "menuNavigation", fetch = LAZY, cascade = {PERSIST, MERGE, REFRESH,DETACH})
    private List<UserPersonalGrant> userPersonalGrants = new ArrayList<>();

    //uri에서 접근제어를 하기 위한 일부 블럭 ex) /super/management/smartmenu 인 경우 navigation을 구성 할 때 권한을 체크하는 일부 uri block(management, smartmenu ..)
    @Setter
    private String uriBlock;

    @Setter
    @Enumerated(EnumType.STRING)
    private NavigationType type;

    @Setter
    @Enumerated(EnumType.STRING)
    private RequestMethod methodType;

    private boolean status;

    @Tolerate
    public MenuNavigation(){}

    /**
     * 메뉴 네비게이션 생성
     * @param project
     * @param name
     * @param uriBlock
     * @param navigationType
     * @param requestMethod
     */
    public MenuNavigation(Project project, String treeId, String parentTreeId, String name, String uriBlock, NavigationType navigationType, RequestMethod requestMethod){
        this.setBy(project);

        this.treeId = treeId;
        this.parentTreeId = parentTreeId;
        this.name = name;
        this.uriBlock = uriBlock;
        this.type = navigationType;
        this.methodType = requestMethod;
        this.status = true;
    }

    public void setBy(MenuNavigation parentMenuNavigation){
        this.parentMenuNavigation = parentMenuNavigation;

        if(!parentMenuNavigation.getMenuNavigationGroup().contains(this))
            parentMenuNavigation.addBy(this);
    }

    public void setBy(Project project){
        this.project = project;

        if(!project.getMenuNavigations().contains(this))
            project.addBy(this);
    }

    public void addBy(AuthorityNavigationUnit authorityNavigationUnit){
        this.authorityNavigationUnits.add(authorityNavigationUnit);
    }

    public void addBy(MenuNavigation menuNavigation){
        menuNavigationGroup.add(menuNavigation);
        if(!this.equals(menuNavigation.getParentMenuNavigation()))
            menuNavigation.setBy(this);
    }

    public void expire(){
        List<UserPersonalGrant> userPersonalGrants = this.getUserPersonalGrants();

        userPersonalGrants.forEach(grant -> {
            grant.expire();
        });

        this.status = false;
        super.expired = new Date();
    }
}
