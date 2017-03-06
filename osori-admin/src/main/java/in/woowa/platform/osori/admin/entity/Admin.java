package in.woowa.platform.osori.admin.entity;

import in.woowa.platform.osori.admin.commons.utils.EncryptUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;

/**
 * Created by HanJaehyun on 2016. 9. 20..
 */
@Entity
@Getter
@Builder
public class Admin {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    @Audited
    @Column(nullable = false)
    private String pw;

    @Setter
    private String description;

    @Setter
    private String img;

    @Audited
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "parentId")
    private Admin parentAdmin;

    @OneToMany(mappedBy = "parentAdmin", fetch = LAZY, cascade = {PERSIST, MERGE, REFRESH,DETACH})
    private List<Admin> admins = new ArrayList<>();

    @Tolerate
    public Admin() {}


    public static Admin of(String email, String pw, String name, String description, String img){
        return builder()
                .email(email)
                .pw(EncryptUtil.sha256(pw))
                .img(img)
                .description(description)
                .name(name)
                .status(true)
                .build();
    }

    public void setBy(Admin parentAdmin){
        this.parentAdmin = parentAdmin;
        if(!parentAdmin.getAdmins().contains(this))
            parentAdmin.addBy(this);
    }

    public void addBy(Admin admin){
        this.admins.add(admin);
        if(!this.equals(admin.getParentAdmin()))
            admin.setBy(this);
    }

    public void setPw(String pw){
        this.pw = EncryptUtil.sha256(pw);
    }
}
