databaseChangeLog {
  changeSet(id: '''1605397319609-3''', author: '''jacob (generated)''') {
    createTable(tableName: '''calendar_event_participant_dao''') {
      column(name: '''id''', type: '''BINARY(255)''') {
        constraints(nullable: false, primaryKey: true, primaryKeyName: '''calendar_event_participant_daoPK''')
      }
      column(name: '''calendar_event_id''', type: '''BINARY(255)''')
      column(name: '''email_id''', type: '''BINARY(255)''')
      column(name: '''participant_type_id''', type: '''BINARY(255)''')
    }
  }

  changeSet(id: '''1605397319609-4''', author: '''jacob (generated)''') {
    createTable(tableName: '''department_statistics_dao''') {
      column(name: '''id''', type: '''BINARY(255)''') {
        constraints(nullable: false, primaryKey: true, primaryKeyName: '''department_statistics_daoPK''')
      }
      column(name: '''average_cost''', type: '''BIGINT''') {
        constraints(nullable: false)
      }
      column(name: '''department''', type: '''VARCHAR(255)''')
      column(name: '''meeting_count''', type: '''BIGINT''') {
        constraints(nullable: false)
      }
      column(name: '''total_cost''', type: '''BIGINT''') {
        constraints(nullable: false)
      }
      column(name: '''total_hours''', type: '''BIGINT''') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '''1605397319609-5''', author: '''jacob (generated)''') {
    createTable(tableName: '''domain_dao''') {
      column(name: '''id''', type: '''BINARY(255)''') {
        constraints(nullable: false, primaryKey: true, primaryKeyName: '''domain_daoPK''')
      }
      column(name: '''name''', type: '''VARCHAR(255)''')
    }
  }

  changeSet(id: '''1605397319609-6''', author: '''jacob (generated)''') {
    createTable(tableName: '''email_dao''') {
      column(name: '''id''', type: '''BINARY(255)''') {
        constraints(nullable: false, primaryKey: true, primaryKeyName: '''email_daoPK''')
      }
      column(name: '''user''', type: '''VARCHAR(255)''')
      column(name: '''domain_id''', type: '''BINARY(255)''')
      column(name: '''person_id''', type: '''BINARY(255)''')
    }
  }

  changeSet(id: '''1605397319609-7''', author: '''jacob (generated)''') {
    createTable(tableName: '''organizer_statistics_dao''') {
      column(name: '''id''', type: '''BINARY(255)''') {
        constraints(nullable: false, primaryKey: true, primaryKeyName: '''organizer_statistics_daoPK''')
      }
      column(name: '''total_cost''', type: '''BIGINT''')
      column(name: '''total_hours''', type: '''INT''')
      column(name: '''total_meetings''', type: '''INT''')
      column(name: '''email_id''', type: '''BINARY(255)''')
    }
  }

  changeSet(id: '''1605397319609-8''', author: '''jacob (generated)''') {
    createTable(tableName: '''participant_type_dao''') {
      column(name: '''id''', type: '''BINARY(255)''') {
        constraints(nullable: false, primaryKey: true, primaryKeyName: '''participant_type_daoPK''')
      }
      column(name: '''name''', type: '''VARCHAR(255)''')
    }
  }

  changeSet(id: '''1605397319609-9''', author: '''jacob (generated)''') {
    createTable(tableName: '''person_dao''') {
      column(name: '''id''', type: '''BINARY(255)''') {
        constraints(nullable: false, primaryKey: true, primaryKeyName: '''person_daoPK''')
      }
      column(name: '''first_name''', type: '''VARCHAR(255)''')
      column(name: '''last_name''', type: '''VARCHAR(255)''')
    }
  }

  changeSet(id: '''1605397319609-10''', author: '''jacob (generated)''') {
    createTable(tableName: '''time_block_summary_dao''') {
      column(name: '''id''', type: '''BINARY(255)''') {
        constraints(nullable: false, primaryKey: true, primaryKeyName: '''time_block_summary_daoPK''')
      }
      column(name: '''average_cost''', type: '''BIGINT''') {
        constraints(nullable: false)
      }
      column(name: '''interval''', type: '''VARCHAR(255)''')
      column(name: '''meeting_count''', type: '''BIGINT''') {
        constraints(nullable: false)
      }
      column(name: '''total_cost''', type: '''BIGINT''') {
        constraints(nullable: false)
      }
      column(name: '''total_time''', type: '''BIGINT''') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '''1605397319609-11''', author: '''jacob (generated)''') {
    createTable(tableName: '''trailing_statistics_dao''') {
      column(name: '''id''', type: '''BINARY(255)''') {
        constraints(nullable: false, primaryKey: true, primaryKeyName: '''trailing_statistics_daoPK''')
      }
    }
  }

  changeSet(id: '''1605397319609-12''', author: '''jacob (generated)''') {
    addUniqueConstraint(columnNames: '''name''', constraintName: '''UC_DOMAIN_DAONAME_COL''', tableName: '''domain_dao''')
  }

  changeSet(id: '''1605397319609-13''', author: '''jacob (generated)''') {
    addUniqueConstraint(columnNames: '''name''', constraintName: '''UC_PARTICIPANT_TYPE_DAONAME_COL''', tableName: '''participant_type_dao''')
  }

  changeSet(id: '''1605397319609-14''', author: '''jacob (generated)''') {
    addForeignKeyConstraint(baseColumnNames: '''domain_id''', baseTableName: '''email_dao''', constraintName: '''FK1ye11fk8v2fo54naahchni1pl''', deferrable: false, initiallyDeferred: false, referencedColumnNames: '''id''', referencedTableName: '''domain_dao''', validate: true)
  }

  changeSet(id: '''1605397319609-15''', author: '''jacob (generated)''') {
    addForeignKeyConstraint(baseColumnNames: '''email_id''', baseTableName: '''organizer_statistics_dao''', constraintName: '''FK4vrd53ly5lmyhlags773e5fo0''', deferrable: false, initiallyDeferred: false, referencedColumnNames: '''id''', referencedTableName: '''email_dao''', validate: true)
  }

  changeSet(id: '''1605397319609-16''', author: '''jacob (generated)''') {
    addForeignKeyConstraint(baseColumnNames: '''email_id''', baseTableName: '''calendar_event_participant_dao''', constraintName: '''FKdq3bjnds2fn4h20qdkucxda3w''', deferrable: false, initiallyDeferred: false, referencedColumnNames: '''id''', referencedTableName: '''email_dao''', validate: true)
  }

  changeSet(id: '''1605397319609-17''', author: '''jacob (generated)''') {
    addForeignKeyConstraint(baseColumnNames: '''person_id''', baseTableName: '''email_dao''', constraintName: '''FKewsu4gb4hxr0x68xpoim0ud9''', deferrable: false, initiallyDeferred: false, referencedColumnNames: '''id''', referencedTableName: '''person_dao''', validate: true)
  }

  changeSet(id: '''1605397319609-18''', author: '''jacob (generated)''') {
    addForeignKeyConstraint(baseColumnNames: '''participant_type_id''', baseTableName: '''calendar_event_participant_dao''', constraintName: '''FKl1o1y3p9o47492eotovk80n42''', deferrable: false, initiallyDeferred: false, referencedColumnNames: '''id''', referencedTableName: '''participant_type_dao''', validate: true)
  }

  changeSet(id: '''1605397319609-19''', author: '''jacob (generated)''') {
    addForeignKeyConstraint(baseColumnNames: '''calendar_event_id''', baseTableName: '''calendar_event_participant_dao''', constraintName: '''FKnd2hn7u88sjmmwakii626wtsp''', deferrable: false, initiallyDeferred: false, referencedColumnNames: '''id''', referencedTableName: '''calendar_event_dao''', validate: true)
  }

  changeSet(id: '''1605397319609-1''', author: '''jacob (generated)''') {
    dropDefaultValue(columnDataType: '''varchar(255)''', columnName: '''google_calendar_id''', tableName: '''calendar_event_dao''')
  }

  changeSet(id: '''1605397319609-2''', author: '''jacob (generated)''') {
    dropDefaultValue(columnDataType: '''varchar(255)''', columnName: '''summary''', tableName: '''calendar_event_dao''')
  }

}
