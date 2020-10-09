databaseChangeLog {
  changeSet(id: '''1602208412343-1''', author: '''jacob (generated)''') {
    createTable(tableName: '''calendar_event_dao''') {
      column(name: '''id''', type: '''BINARY(255)''') {
        constraints(nullable: false, primaryKey: true, primaryKeyName: '''calendar_event_daoPK''')
      }
      column(name: '''created''', type: '''datetime''')
      column(name: '''google_calendar_id''', type: '''VARCHAR(255)''')
      column(name: '''summary''', type: '''VARCHAR(255)''')
    }
  }

}
