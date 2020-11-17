-- calendar events
insert into calendar_event_dao
values (unhex(replace('1d3014ac-a685-4d46-bf81-f36671e8ffe1', '-', '')),
        '2020-11-16 18:15:56.000000',
        '2020-11-16 18:15:56.000000',
        '1',
        '2020-11-16 18:15:56.000000',
        'bob');
insert into calendar_event_dao
values (unhex(replace('c3e3b240-898d-442b-a465-782a09cdc8c5', '-', '')),
        '2020-11-16 18:15:56.000000',
        '2020-11-17 13:0:0.000000',
        '1',
        '2020-11-16 14:00:00.000000',
        'bob');
insert into calendar_event_dao
values (unhex(replace('53af002d-57a4-43b0-b02c-8627b7cbea37', '-', '')),
        '2020-11-16 18:15:56.000000',
        '2020-11-16 15:00:00.000000',
        '1',
        '2020-11-16 16:00:00.000000',
        'bob');

-- domain
insert into domain_dao
values (unhex(replace('63b56252-7e11-4ba7-b3bc-e794a8a21fbe', '-', '')),
        '2112.com');

-- email
insert into email_dao
values (unhex(replace('416285a0-9a93-49dc-8e19-bd447c4d39db', '-', '')),
        'bob',
        unhex(replace('63b56252-7e11-4ba7-b3bc-e794a8a21fbe', '-', '')));
insert into email_dao
values (unhex(replace('e19aa104-e818-48b1-861f-93747e96f84f', '-', '')),
        'bob',
        unhex(replace('63b56252-7e11-4ba7-b3bc-e794a8a21fbe', '-', '')));
insert into email_dao
values (unhex(replace('63a204d9-7ce3-4bb6-bf1c-727bec833709', '-', '')),
           'bob',
           unhex(replace('63b56252-7e11-4ba7-b3bc-e794a8a21fbe', '-', '')));


