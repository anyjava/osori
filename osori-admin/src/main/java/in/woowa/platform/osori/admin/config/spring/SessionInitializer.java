package in.woowa.platform.osori.admin.config.spring;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;


public class SessionInitializer extends AbstractHttpSessionApplicationInitializer {

    public SessionInitializer() {
        super(SessionConfiguration.class);
    }
}
