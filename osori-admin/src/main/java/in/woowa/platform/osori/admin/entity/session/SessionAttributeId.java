package in.woowa.platform.osori.admin.entity.session;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by seooseok on 2016. 9. 28..
 * Spring session의 session table과 session attribute table 의 복합키
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@EqualsAndHashCode(of ={"springSession","attributeName"})
class SessionAttributeId implements Serializable{

    private SpringSession springSession;
    private String attributeName;
}
