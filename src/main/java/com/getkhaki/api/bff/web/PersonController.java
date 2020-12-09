package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.PersonDm;
import com.getkhaki.api.bff.domain.services.PersonService;
import com.getkhaki.api.bff.web.models.PersonDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/persons")
@RestController
@CrossOrigin(origins = "*")
public class PersonController {
    private final PersonService personService;
    private final ModelMapper modelMapper;

    @Autowired
    public PersonController(PersonService personService, ModelMapper modelMapper) {
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{email}")
    public PersonDto getPerson(@PathVariable String email) {
        return this.modelMapper.map(
                personService.getPerson(email), PersonDto.class
        );
    }

    @PostMapping
    public PersonDto updatePerson(@RequestBody PersonDto personDto) {
        return this.modelMapper.map(
                this.personService.updatePerson(
                        this.modelMapper.map(personDto, PersonDm.class)
                ), PersonDto.class
        );
    }
}
