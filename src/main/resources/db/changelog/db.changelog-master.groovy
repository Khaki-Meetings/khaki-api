package db.changelog

databaseChangeLog {
  changeSet(id: '''1602029682505-1''', author: '''lord_baine (generated)''') {
    createTable(tableName: '''CalendarEventDao''') {
      column(name: '''id''', type: '''UUID''') {
        constraints(nullable: false, primaryKey: true, primaryKeyName: '''CalendarEventDaoPK''')
      }
      column(name: '''created''', type: '''timestamp''')
      column(name: '''summary''', type: '''VARCHAR(255)''')
    }
  }
}
