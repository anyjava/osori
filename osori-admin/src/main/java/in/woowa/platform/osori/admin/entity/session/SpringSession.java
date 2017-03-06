package in.woowa.platform.osori.admin.entity.session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by seooseok on 2016. 9. 28..
 * Spring session 에서 사용 할 DB 테이블
 * @link org/springframework/session/jdbc/schema-h2.sql
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "SPRING_SESSION")
public class SpringSession {

    @Id
    @Column(name = "SESSION_ID", length = 36)
    private String sessionId;

    @Column(name = "CREATION_TIME", nullable = false, length = 19)
    private long creationTime;

    @Column(name = "LAST_ACCESS_TIME", nullable = false, length = 19)
    private long lastAccessTime;

    @Column(name = "MAX_INACTIVE_INTERVAL", nullable = false, length = 10)
    private int maxInactiveInterval;

    @Column(name = "PRINCIPAL_NAME", length = 100)
    private String principalName;
}
