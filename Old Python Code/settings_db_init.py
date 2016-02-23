import sqlite3
import os


def init():
    """
    Creates and initializes settings database.

    Doesn't do anything if the file already exists. Remove the local copy to recreate the database.
    """
    if not os.path.isfile("settings.sqlite"):
        app_db_connection = sqlite3.connect('settings.sqlite')
        app_db = app_db_connection.cursor()

        app_db.execute("CREATE TABLE oauth (site, rate_remaining, rate_reset)")
        app_db.execute("INSERT INTO oauth VALUES ('reddit', 30, 60)")

        app_db_connection.commit()
        app_db_connection.close()


if __name__ == "__main__":
    init()
