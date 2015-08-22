# -*- coding: utf-8 -*-

"""
    Eve Demo
    ~~~~~~~~
    A demostration of a simple API powered by Eve REST API.
    The live demo is available at eve-demo.herokuapp.com. Please keep in mind
    that the it is running on Heroku's free tier using a free MongoHQ
    sandbox, which means that the first request to the service will probably
    be slow. The database gets a reset every now and then.
    :copyright: (c) 2015 by Nicola Iarocci.
    :license: BSD, see LICENSE for more details.
"""

import os
from eve import Eve
from eve_auth_jwt import JWTAuth




# Heroku support: bind to PORT if defined, otherwise default to 5000.
if 'PORT' in os.environ:
    port = int(os.environ.get('PORT'))
    # use '0.0.0.0' to ensure your REST API is reachable from all your
    # network (and not only your computer).
    host = '0.0.0.0'
else:
    port = 5000
    host = '127.0.0.1'

app = Eve(auth=JWTAuth)


@app.after_request
def after_request(response):
    response.headers.add('X-user', 'User')
    response.headers.add('X-pwd', 'Password')
    return response

if __name__ == '__main__':
    app.run(host=host, port=port)