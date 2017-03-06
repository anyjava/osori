package in.woowa.platform.osori.admin.service;

import in.woowa.platform.osori.admin.commons.constant.OsoriConstant;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.config.exception.ProcessErr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by seooseok on 2016. 10. 27..
 * 요청 방법 확인
 */
@Service
public class AccessCheckerHandler {

    @Autowired
    @Qualifier("idBaseAccessChecker")
    private AccessChecker idBaseAccessChecker;
    @Autowired
    @Qualifier("uriBaseAccessChecker")
    private AccessChecker uriBaseAccessChecker;

    public AccessChecker handle(OsoriConstant.AuthenticationCheckType checkType){

        if(checkType == OsoriConstant.AuthenticationCheckType.id)
            return idBaseAccessChecker;

        if(checkType == OsoriConstant.AuthenticationCheckType.uri)
            return uriBaseAccessChecker;

        throw new ApplicationException(ProcessErr.WRONG_DEVELOP_PROCESS);
    }


}
