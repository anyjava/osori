package in.woowa.platform.osori.admin.entity;

import in.woowa.platform.osori.admin.commons.utils.EncryptUtil;
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
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
@Builder
@Table(indexes = {@Index(name = "IDX_APIKEY", columnList = "apiKey" ,unique = true)})
public class Project extends AuditEntity{

    @Id
    @GeneratedValue
    private Long id;

    @Audited
    @Setter
    @Column(nullable = false, length = 50)
    private String name;

    @Setter
    @Column(length = 500)
    private String description;

    @Column(length = 100)
    private String apiKey;

    @Audited
    private boolean status;


    @Where(clause = "status = true")
    @OneToMany(mappedBy = "project", fetch = LAZY, cascade = {PERSIST, MERGE, REFRESH})
    private List<MenuNavigation> menuNavigations = new ArrayList<>();


    @Tolerate
    public Project(){}

    public static Project of(String name, String description){
        long time = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return builder()
                .name(name)
                .description(description)
                .apiKey(EncryptUtil.sha256(name + time))
                .status(true)
                .menuNavigations(new ArrayList<>())
                .build();
    }

    public void expire(){
        super.expired = new Date();
        status = false;


    }

    public void addBy(MenuNavigation menuNavigation){
        this.menuNavigations.add(menuNavigation);
        if(!this.equals(menuNavigation.getProject()))
            menuNavigation.setBy(this);
    }

    public List<MenuNavigation> getMenuNavigations() {
        return this.menuNavigations;
    }
}
