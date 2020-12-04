package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.PersonDm;
import com.getkhaki.api.bff.domain.services.PersonService;
import com.getkhaki.api.bff.web.models.PersonDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public class PersonControllerUnitTests {
    private PersonController underTest;

    private PersonService personService;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        personService = mock(PersonService.class);
        modelMapper = mock(ModelMapper.class);
        underTest = new PersonController(this.personService, this.modelMapper);
    }

    @Test
    public void getPerson() {
        var email = "noone@nowhere.com";
        var mockPersonDm = mock(PersonDm.class);
        var mockPersonDto = mock(PersonDto.class);

        when(this.personService.getPerson(email))
                .thenReturn(mockPersonDm);

        when(this.modelMapper.map(mockPersonDm, PersonDto.class))
                .thenReturn(mockPersonDto);

        PersonDto response = this.underTest.getPerson(email);

        assertSame(response, mockPersonDto);
    }

    @Test
    public void updatePerson() {
        var mockPersonDm = mock(PersonDm.class);
        var mockUpdatedPersonDm = mock(PersonDm.class);
        var mockIncomingPersonDto = mock(PersonDto.class);
        var mockOutgoingPersonDto = mock(PersonDto.class);

        when(this.modelMapper.map(mockIncomingPersonDto, PersonDm.class))
                .thenReturn(mockPersonDm);

        when(this.personService.updatePerson(mockPersonDm))
                .thenReturn(mockUpdatedPersonDm);

        when(this.modelMapper.map(mockUpdatedPersonDm, PersonDto.class))
                .thenReturn(mockOutgoingPersonDto);

        PersonDto response = this.underTest.updatePerson(mockIncomingPersonDto);

        assertSame(response, mockOutgoingPersonDto);
    }
}
