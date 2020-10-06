package db.changelog

databaseChangeLog {
  changeSet(id: '''1601945434293-1''', author: '''lord_baine (generated)''') {
    createTable(tableName: '''CalendarEventDao''') {
      column(name: '''id''', type: '''BLOB''') {
        constraints(nullable: false, primaryKey: true, primaryKeyName: '''CalendarEventDaoPK''')
      }
      column(name: '''created''', type: '''timestamp''')
      column(name: '''summary''', type: '''VARCHAR(255)''')
    }
  }
}
