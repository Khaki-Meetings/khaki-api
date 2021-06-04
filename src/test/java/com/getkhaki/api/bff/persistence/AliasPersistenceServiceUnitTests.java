package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.AliasDm;
import com.getkhaki.api.bff.persistence.models.AliasDao;
import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.repositories.AliasRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.EmailRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)

public class AliasPersistenceServiceUnitTests {
    private AliasPersistenceService underTest;

    @Mock
    private AliasRepositoryInterface mockAliasRepository;

    @Mock
    private EmailRepositoryInterface mockEmailRepository;

    @Mock
    private ModelMapper mockMapper;

    @BeforeEach
    public void setup() {
        underTest = new AliasPersistenceService(mockAliasRepository, mockEmailRepository, mockMapper);
    }

    @Test
    void test() {

        UUID id = UUID.randomUUID();

        EmailDao emailDao = new EmailDao()
                .setDomain(new DomainDao().setName("test.com"))
                .setUser("user");
        emailDao.setId(id);

        AliasDao dao = new AliasDao()
                .setEmail(emailDao)
                .setAlias("testalias");
        dao.setId(id);

        when(mockAliasRepository.save(dao)).thenReturn(dao);

        AliasDao result = mockAliasRepository.save(dao);

        assertNotNull(result);
        assertEquals(dao.getAlias(), result.getAlias());
    }

    @Test
    void testGetAliases() {
        UUID id = UUID.randomUUID();

        EmailDao emailDao = new EmailDao()
                .setDomain(new DomainDao().setName("test.com"))
                .setUser("user");
        emailDao.setId(id);

        AliasDao dao = new AliasDao()
                .setEmail(emailDao)
                .setAlias("testalias");
        dao.setId(id);

        List<AliasDao> daoList = new ArrayList<AliasDao>();
        daoList.add(dao);

        when(mockAliasRepository.findByEmailId(id)).thenReturn(Optional.of(daoList));

        Set<AliasDm> result = underTest.getAliases(id);

        Set<AliasDm> list = new HashSet<AliasDm>();
        list.add(mockMapper.map(dao, AliasDm.class));

        assertEquals(list, result);
    }

    @Test
    void testAddAlias() {
        UUID id = UUID.randomUUID();

        EmailDao emailDao = new EmailDao()
                .setDomain(new DomainDao().setName("test.com"))
                .setUser("user");
        emailDao.setId(id);

        AliasDao dao = new AliasDao()
                .setEmail(emailDao)
                .setAlias("testalias");
        dao.setId(id);

        AliasDm dm = new AliasDm()
                .setAlias("testalias")
                .setEmail(emailDao.getEmailString());

        when(mockEmailRepository.findById(id)).thenReturn(Optional.of(emailDao));

        when(mockAliasRepository.save(any(AliasDao.class))).thenReturn(dao);

        when(mockMapper.map(any(AliasDao.class), eq(AliasDm.class))).thenReturn(dm);

        when(mockMapper.map(any(AliasDm.class), eq(AliasDao.class))).thenReturn(dao);

        AliasDm result = underTest.addAlias(dm, id);

        assertEquals(dm, result);
    }

}
