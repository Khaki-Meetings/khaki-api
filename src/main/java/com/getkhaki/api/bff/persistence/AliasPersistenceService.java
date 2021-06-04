package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.AliasDm;
import com.getkhaki.api.bff.domain.persistence.AliasPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.AliasDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.repositories.AliasRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.EmailRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AliasPersistenceService implements AliasPersistenceInterface {

    private final AliasRepositoryInterface aliasRepository;
    private final EmailRepositoryInterface emailRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public AliasPersistenceService(AliasRepositoryInterface aliasRepository, EmailRepositoryInterface emailRepository, ModelMapper modelMapper) {
        this.aliasRepository = aliasRepository;
        this.emailRepository = emailRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<AliasDm> getAliases(UUID emailId) {
        List<AliasDao> results = this.aliasRepository.findByEmailId(emailId).get();

        return results.stream()
                .map(alias -> modelMapper.map(alias, AliasDm.class))
                .collect(Collectors.toSet());
    }

    @Override
    public AliasDm addAlias(AliasDm aliasDm, UUID emailId) {

        Optional<EmailDao> emailDao = this.emailRepository.findById(emailId);

        if (emailDao.isPresent()) {
            aliasDm.setEmail(emailDao.get().getEmailString());
            AliasDao dao = modelMapper.map(aliasDm, AliasDao.class);
            AliasDao newAliasDao = this.aliasRepository.save(dao);
            return modelMapper.map(newAliasDao, AliasDm.class);
        }

        return null;
    }
}
