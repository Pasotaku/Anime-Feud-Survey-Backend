import requests
import sqlite3
import time
import json

with open("../Pasotaku-Secrets/Anime-Feud-Survey-Backend.json", "r") as secrets_file:
    secrets = json.load(secrets_file)

app_db_connection = sqlite3.connect('settings.sqlite')
app_db = app_db_connection.cursor()


def reddit_login():
    return "https://www.reddit.com/api/v1/authorize" + \
           "?client_id=" + secrets["reddit-id"] + \
           "&response_type=code" + \
           "&state=dev_state" + \
           "&redirect_uri=" + secrets["reddit-redirect"] + \
           "&duration=temporary" + \
           "&scope=identity"


def reddit_username(code):
    """
    OAuth2 configuration for reddit.

    Authenticates using the code given in the callback url. Also ensures the API calls adhere to the rate limits.

    :param code: value of "code" parameter from http get url.
    :return: Returns username if the app hasn't hit the reddit rate limit. Otherwise, returns None.
    """
    app_db.execute("SELECT rate_remaining, rate_reset FROM oauth WHERE site='reddit'")
    rate_remaining, rate_reset = app_db.fetchone()
    answer = requests.post("https://www.reddit.com/api/v1/access_token",
                           data={
                               "grant_type": "authorization_code",
                               "code": code,
                               "redirect_uri": secrets["reddit-redirect"]
                           },
                           headers={
                               "user-agent": "Python:com.pasotaku.anime-feud:v0.0.1 " +
                                             "(by /u/Pasotaku_SnipersCode)"
                           },
                           auth=(secrets["reddit-id"], secrets["reddit-secret"]))
    if rate_remaining > 60 or rate_reset < time.time():
        user_answer = requests.get("https://oauth.reddit.com/api/v1/me",
                                   headers={
                                       "Authorization": "bearer " + answer.json()["access_token"],
                                       "user-agent": "Python:com.pasotaku.anime-feud:v0.0.1 " +
                                                     "(by /u/Pasotaku_SnipersCode)"
                                   })
        app_db.execute("UPDATE oauth SET rate_remaining=?, rate_reset=? WHERE site='reddit'",
                       (int(float(user_answer.headers["x-ratelimit-remaining"])),
                        float(user_answer.headers["x-ratelimit-reset"]) + time.time() + 10))
        app_db_connection.commit()
        return user_answer.json()["name"]
    else:
        return None
