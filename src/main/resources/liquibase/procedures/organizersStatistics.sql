create or replace procedure organizerStatistics(IN orgId binary(16))
begin
    select pd.first_name,
           pd.last_name,
           concat(ed.user, '@', domain.name) as email,
           count(ced.id)                     as totalMeetings,
           (
               select count(*)
               from calendar_event_participant_dao cepd2
               where cepd2.calendar_event_id = ced.id
           )                                 as participantCount,
           sum((
               select timestampdiff(hour, start, end) * participantCount
               from calendar_event_dao ced2
               where ced2.id = ced.id
           ))                                as totalHours,
           sum((
               select sum(sgd.salary)
               from calendar_event_dao ced3
                        left join calendar_event_participant_dao cepd on ced3.id = cepd.calendar_event_id
                        left join participant_type_dao ptd on ptd.id = cepd.participant_type_id
                        left join email_dao ed on cepd.email_id = ed.id
                        left join email_dao_people edp on ed.id = edp.emails_id
                        left join person_dao pd on edp.people_id = pd.id
                        left join employee_dao e on pd.id = e.person_id
                        left join salary_group_dao sgd on e.salary_group_id = sgd.id
               where ced3.id = ced.id
           ))                                as totalCost
    from calendar_event_dao ced
             left join calendar_event_participant_dao cepd on ced.id = cepd.calendar_event_id
             left join participant_type_dao ptd on cepd.participant_type_id = ptd.id
             left join email_dao ed on cepd.email_id = ed.id
             left join domain_dao domain on ed.domain_id = domain.id
             left join email_dao_people edp on ed.id = edp.emails_id
             left join person_dao pd on edp.people_id = pd.id
             left join employee_dao e on pd.id = e.person_id
             left join department_dao dd on e.department_id = dd.id
             left join organization_dao od on dd.organization_id = od.id
    where od.id = orgId
      and ptd.name = 'Organizer'
    group by ed.id;
end;