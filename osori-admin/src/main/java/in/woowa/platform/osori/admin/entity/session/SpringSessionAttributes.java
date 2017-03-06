package in.woowa.platform.osori.admin.entity.session;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Blob;

import static javax.persistence.FetchType.LAZY;

/**
 * Created by seooseok on 2016. 9. 28..
 * Spring session 에서 사용 할 DB 테이블
 * @link org/springframework/session/jdbc/schema-h2.sql
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "SPRING_SESSION_ATTRIBUTES",
        indexes = {@Index(name = "IDX_SPRING_SESSION_ATTRIBUTES", columnList = "SESSION_ID")})
@IdClass(SessionAttributeId.class)
public class SpringSessionAttributes {

    @Id
    @Column(name = "ATTRIBUTE_NAME", length = 200)
    private String attributeName;

    @Id
    @ManyToOne(fetch = LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "SESSION_ID", foreignKey = @ForeignKey(name = "FK_SESSION_ID"))
    private SpringSession springSession;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @Column(name = "ATTRIBUTE_BYTES")
    private Blob attributeBytes;
}
