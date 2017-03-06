package in.woowa.platform.osori.admin.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by seooseok on 2016. 7. 5..
 * History Auditor Addition Entity
 */
@SuppressWarnings("unused")
@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
abstract class AuditEntity {

    @CreatedBy
    @Column(nullable = false, length = 50)
    protected String createUser;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    protected Date created;     //생성일

    @Audited
    @LastModifiedBy
    @Column(length = 50)
    protected String modifiedUser;

    @Audited
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modified;     //수정일

    @Audited
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    protected Date expired;     //만료일

}
