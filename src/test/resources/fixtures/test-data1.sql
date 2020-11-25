-- calendar events
set @Meeting1Id = unhex(replace('1d3014ac-a685-4d46-bf81-f36671e8ffe1', '-', ''));
set @Meeting2Id = unhex(replace('c3e3b240-898d-442b-a465-782a09cdc8c5', '-', ''));
set @Meeting3Id = unhex(replace('53af002d-57a4-43b0-b02c-8627b7cbea37', '-', ''));
insert into calendar_event_dao (id, created, end, google_calendar_id, start, summary)
values (@Meeting1Id,
        '2020-11-16 18:15:56.000000',
        '2020-11-16 19:00:00.000000',
        '1',
        '2020-11-16 21:0:00.000000',
        'cool meeting 1');
insert into calendar_event_dao (id, created, end, google_calendar_id, start, summary)
values (@Meeting2Id,
        '2020-11-16 18:15:56.000000',
        '2020-11-17 14:0:0.000000',
        '2',
        '2020-11-16 13:00:00.000000',
        'cool meeting 2');
insert into calendar_event_dao (id, created, end, google_calendar_id, start, summary)
values (@Meeting3Id,
        '2020-11-16 18:15:56.000000',
        '2020-11-16 16:00:00.000000',
        '3',
        '2020-11-16 15:00:00.000000',
        'cool meeting 3');

-- domain
set @Domain2112Id = unhex(replace('63b56252-7e11-4ba7-b3bc-e794a8a21fbe', '-', ''));
insert into domain_dao (id, name)
values (@Domain2112Id,
        '2112.com');

-- email
set @BobJonesEmailId = unhex(replace('416285a0-9a93-49dc-8e19-bd447c4d39db', '-', ''));
set @TedJonesEmailId = unhex(replace('e19aa104-e818-48b1-861f-93747e96f84f', '-', ''));
set @FredJonesEmailId = unhex(replace('63a204d9-7ce3-4bb6-bf1c-727bec833709', '-', ''));
insert into email_dao (id, user, domain_id)
values (@BobJonesEmailId,
        'bob',
        @Domain2112Id);
insert into email_dao (id, user, domain_id)
values (@TedJonesEmailId,
        'Ted',
        @Domain2112Id);
insert into email_dao (id, user, domain_id)
values (@FredJonesEmailId,
        'Fred',
        @Domain2112Id);


-- person
set @BobJonesPersonId = unhex(replace('fdcfbc7a-b925-401b-8d84-9a06e5ffab94', '-', ''));
insert into person_dao (id, first_name, last_name)
values (@BobJonesPersonId,
        'Bob',
        'Jones');
set @TedJonesPersonId = unhex(replace('b9827fd8-08c0-4627-b56f-209a5aa27b3b', '-', ''));
insert into person_dao (id, first_name, last_name)
values (@TedJonesPersonId,
        'Ted',
        'Jones');
set @FredJonesPersonId = unhex(replace(uuid(), '-', ''));
insert into person_dao (id, first_name, last_name)
values (@FredJonesPersonId,
        'Fred',
        'Jones');

-- email_dao_people
insert into email_dao_people (emails_id, people_id)
values (@BobJonesEmailId,
        @BobJonesPersonId);
insert into email_dao_people (emails_id, people_id)
values (@TedJonesEmailId,
        @TedJonesPersonId);
insert into email_dao_people (emails_id, people_id)
values (@FredJonesEmailId,
        @FredJonesPersonId);

-- participant type
set @OrganizerTypeId = unhex(replace('e661d9b1-d081-49f1-ae92-5cf1d364b7c9', '-', ''));
set @OrganizerName = 'Organizer';
insert into participant_type_dao (id, name)
VALUES (@OrganizerTypeId,
        @OrganizerName);
set @ParticipantTypeId = unhex(replace('53a25c19-4b6c-4394-bba0-9fc58a24f384', '-', ''));
set @ParticipantName = 'Participant';
insert into participant_type_dao (id, name)
VALUES (@ParticipantTypeId,
        @ParticipantName);

-- calendar event participant
set @Meeting1BobJonesParticipantId = unhex(replace('9aa07109-abdc-4293-adce-1e2b1538f650', '-', ''));
insert into calendar_event_participant_dao (id, calendar_event_id, email_id, participant_type_id)
values (@Meeting1BobJonesParticipantId,
        @Meeting1Id,
        @BobJonesEmailId,
        @OrganizerTypeId);
set @Meeting1TedJonesParticipantId = unhex(replace(uuid(), '-', ''));
insert into calendar_event_participant_dao (id, calendar_event_id, email_id, participant_type_id)
values (@Meeting1TedJonesParticipantId,
        @Meeting1Id,
        @TedJonesEmailId,
        @ParticipantTypeId);
set @Meeting1FredJonesParticipantId = unhex(replace(uuid(), '-', ''));
insert into calendar_event_participant_dao (id, calendar_event_id, email_id, participant_type_id)
values (@Meeting1FredJonesParticipantId,
        @Meeting1Id,
        @FredJonesEmailId,
        @ParticipantTypeId);
set @Meeting2BobJonesParticipantId = unhex(replace(uuid(), '-', ''));
insert into calendar_event_participant_dao (id, calendar_event_id, email_id, participant_type_id)
values (@Meeting2BobJonesParticipantId,
        @Meeting2Id,
        @BobJonesEmailId,
        @OrganizerTypeId);
set @Meeting2TedJonesParticipantId = unhex(replace(uuid(), '-', ''));
insert into calendar_event_participant_dao (id, calendar_event_id, email_id, participant_type_id)
values (@Meeting2TedJonesParticipantId,
        @Meeting2Id,
        @FredJonesEmailId,
        @ParticipantTypeId);
set @Meeting3BobJonesParticipantId = unhex(replace(uuid(), '-', ''));
insert into calendar_event_participant_dao (id, calendar_event_id, email_id, participant_type_id)
values (@Meeting3BobJonesParticipantId,
        @Meeting3Id,
        @BobJonesEmailId,
        @OrganizerTypeId);
set @Meeting3FredJonesParticipantId = unhex(replace(uuid(), '-', ''));
insert into calendar_event_participant_dao (id, calendar_event_id, email_id, participant_type_id)
values (@Meeting3FredJonesParticipantId,
        @Meeting3Id,
        @FredJonesEmailId,
        @ParticipantTypeId);

-- salary group
set @SalaryGroup1Id = unhex(replace(uuid(), '-', ''));
insert into salary_group_dao (id, role, salary)
VALUES (@SalaryGroup1Id,
        'Cool guys',
        95000);
set @SalaryGroup2Id = unhex(replace(uuid(), '-', ''));
insert into salary_group_dao (id, role, salary)
VALUES (@SalaryGroup2Id,
        'Lame guys',
        55000);

-- organization
set @ThatCoolCompanyId = unhex(replace('d713ace2-0d30-43be-b4ba-db973967d6d4', '-', ''));
insert into organization_dao (id, name)
VALUES (@ThatCoolCompanyId,
        'That Cool Company');

-- department
set @ThatCoolCompanyItId = unhex(replace(uuid(), '-', ''));
insert into department_dao (id, name, organization_id)
VALUES (@ThatCoolCompanyItId,
        'IT',
        @ThatCoolCompanyId);
set @ThatCoolCompanyHrId = unhex(replace(uuid(), '-', ''));
insert into department_dao (id, name, organization_id)
VALUES (@ThatCoolCompanyHrId,
        'HR',
        @ThatCoolCompanyId);

-- employee
set @BobJonesEmployeeId = unhex(replace(uuid(), '-', ''));
insert into employee_dao (id, department_id, person_id, salary_group_id)
VALUES (@BobJonesEmployeeId,
        @ThatCoolCompanyHrId,
        @BobJonesPersonId,
        @SalaryGroup1Id);
set @TedJonesEmployeeId = unhex(replace(uuid(), '-', ''));
insert into employee_dao (id, department_id, person_id, salary_group_id)
VALUES (@TedJonesEmployeeId,
        @ThatCoolCompanyItId,
        @TedJonesPersonId,
        @SalaryGroup1Id);
set @FredJonesEmployeeId = unhex(replace(uuid(), '-', ''));
insert into employee_dao (id, department_id, person_id, salary_group_id)
VALUES (@FredJonesEmployeeId,
        @ThatCoolCompanyHrId,
        @FredJonesPersonId,
        @SalaryGroup2Id);
