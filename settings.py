# -*- coding: utf-8 -*-

import os

# We want to seamlessy run our API both locally and on Heroku. If running on
# Heroku, sensible DB connection settings are stored in environment variables.
#MONGO_HOST = os.environ.get('MONGO_HOST', 'localhost')
#MONGO_PORT = os.environ.get('MONGO_PORT', 27017)
#MONGO_USERNAME = os.environ.get('MONGO_USERNAME', 'user')
#MONGO_PASSWORD = os.environ.get('MONGO_PASSWORD', 'user')
#MONGO_DBNAME = os.environ.get('MONGO_DBNAME', 'evedemo')


# Enable reads (GET), inserts (POST) and DELETE for resources/collections
# (if you omit this line, the API will default to ['GET'] and provide
# read-only access to the endpoint).
#RESOURCE_METHODS = ['GET', 'POST', 'DELETE']

# Enable reads (GET), edits (PATCH) and deletes of individual items
# (defaults to read-only item access).
#ITEM_METHODS = ['GET', 'PATCH', 'DELETE']

# We enable standard client cache directives for all resources exposed by the
# API. We can always override these global settings later.
CACHE_CONTROL = 'max-age=20'
CACHE_EXPIRES = 20

JWT_SECRET ="JimmyKun"
JWT_ISSUER ="Test" 

tos = {
 'item_title': 'Terms Of Service',
    'schema':{
        'tos_text' : {
            'type' : 'string',
            'required' : True,
        },
        'tos_displayed': {
            'type': 'boolean',
            'required' : True,
        },
        'tos_accepted': {
            'type': 'boolean',
            'required' : True,
        },
        'display_again': {
            'type': 'boolean',
            'required' : True,
        },
    }
}

survey = {
 'item_title': 'Survey',
    'schema' : {
        'ID': {
            'type': 'integer',
            'required' : True,
        },
        'Question' : {
            'type' : 'string',
            'required' : True,
        },
        'Type': {
            'type' :  'string',
            'required': True,
        },
        'Answered' : {
            'type': 'boolean',
            'required': True,
        }
    }
}

# The DOMAIN dict explains which resources will be available and how they will
# be accessible to the API consumer.
DOMAIN = {
    'users/username/tos' : tos,
    'users/usernames/survey' : survey,
    'anime/search/{animename}' : {},

}