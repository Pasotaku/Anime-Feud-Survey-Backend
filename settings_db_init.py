import sqlite3

"""
Creates and initializes settings database.
"""

app_db_connection = sqlite3.connect('settings.sqlite')
app_db = app_db_connection.cursor()

app_db.execute("CREATE TABLE oauth (site, rate_remaining, rate_reset)")
app_db.execute("INSERT INTO oauth VALUES ('reddit', 30, 60)")

app_db_connection.commit()
app_db_connection.close()
