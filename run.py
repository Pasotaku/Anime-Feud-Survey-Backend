# -*- coding: utf-8 -*-

import os
from eve import Eve
from eve.auth import TokenAuth
from flask import request, redirect
# from eve_auth_jwt import JWTAuth  # Re-enable when auth is implemented correctly

import oauth
import settings_db_init

import jwt


settings_db_init.init()  # Create settings database if it doesn't exist

# Heroku support: bind to PORT if defined, otherwise default to 5000.
if 'PORT' in os.environ:
    port = int(os.environ.get('PORT'))
    # use '0.0.0.0' to ensure your REST API is reachable from all your
    # network (and not only your computer).
    host = '0.0.0.0'
else:
    port = 5000
    host = '127.0.0.1'

class RolesAuth(TokenAuth):

    def check_auth(self, token, allowed_roles, resource, method):
        users = app.data.driver.db['users']
        # Add check of user credentials by decoding JWT
        user = users.find_one({'token': token})
        return user


def add_token(documents):
    for document in documents:
        payload = {'username': document['username']}
        document["token"] = jwt.encode(payload, 'secret')
        try:
            payload = jwt.decode(document["token"],'secret')
            print(payload)
        except jwt.InvalidTokenError:
            print("We failed!")

# app = Eve(auth=JWTAuth)  # Re-enable when auth is implemented correctly
app = Eve(auth=RolesAuth)
app.on_insert_users += add_token


@app.route("/login/reddit")
def login_reddit():
    return redirect(oauth.reddit_login())


@app.route("/oauth/reddit")
def oauth_reddit():
    username = oauth.reddit_username(request.args.get('code'))
    if username:
        return redirect("/test?name=" + username)
    else:
        return redirect("/test?name=" + "ReachedRateLimit")


@app.route("/test")
def test_route():
    return request.args.get("name")


@app.after_request
def after_request(response):
    response.headers.add('X-user', 'User')
    response.headers.add('X-pwd', 'Password')
    return response


if __name__ == '__main__':
    app.run(host=host, port=port)
