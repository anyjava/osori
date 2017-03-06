package in.woowa.platform.osori.admin.manager;

import in.woowa.platform.osori.admin.config.exception.HumanErr;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.entity.AuthorityDefinition;
import in.woowa.platform.osori.admin.entity.Project;
import in.woowa.platform.osori.admin.repository.AuthorityDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by seooseok on 2016. 10. 4..
 */
@Service
@Transactional
public class AuthorityDefinitionManager {

    @Autowired
    private AuthorityDefinitionRepository authorityDefinitionRepository;

    public AuthorityDefinition createBy(Project project, String name){
        AuthorityDefinition authorityDefinition = AuthorityDefinition.of(project, name);

        return authorityDefinitionRepository.save(authorityDefinition);
    }

    public AuthorityDefinition findBy(long defineId){
        Optional<AuthorityDefinition> authorityDefinition = Optional.ofNullable(authorityDefinitionRepository.findOne(defineId));

        if(!authorityDefinition.isPresent())
            throw new ApplicationException(HumanErr.IS_EMPTY);

        return authorityDefinition.get();
    }

    public List<AuthorityDefinition> findListBy(long projectId){
        return authorityDefinitionRepository.findByProjectIdAndStatusTrue(projectId);
    }

    public AuthorityDefinition findByLive(long authorityDefinitionId){
        AuthorityDefinition authorityDefinition = this.findBy(authorityDefinitionId);

        if(!authorityDefinition.isStatus())
            throw new ApplicationException(HumanErr.IS_EXPIRE);

        return authorityDefinition;
    }

    public List<AuthorityDefinition> findBy(List<Long> authorityDefinitionIdGroup){
        return authorityDefinitionRepository.findByIdIn(authorityDefinitionIdGroup);
    }

    public List<AuthorityDefinition> findByLive(List<Long> authorityDefinitionIdGroup){
        List<AuthorityDefinition> authorityDefinitions = this.findBy(authorityDefinitionIdGroup);
        authorityDefinitions.forEach(authorityDefinition -> {
            if(!authorityDefinition.isStatus())
                throw new ApplicationException(HumanErr.IS_EXPIRE, new Object[]{authorityDefinition.getId()});
        });

        return authorityDefinitions;
    }


    public AuthorityDefinition saveBy(AuthorityDefinition authorityDefinition){
        return authorityDefinitionRepository.save(authorityDefinition);
    }
}
