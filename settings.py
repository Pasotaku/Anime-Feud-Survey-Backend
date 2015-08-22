# -*- coding: utf-8 -*-

import os
import json


with open("../Pasotaku-Secrets/Anime-Feud-Survey-Backend.json", "r") as secrets_file:
    secrets = json.load(secrets_file)

# We want to seamlessly run our API both locally and on Heroku. If running on
# Heroku, sensible DB connection settings are stored in environment variables.
MONGO_HOST = os.environ.get('MONGO_HOST', secrets["mongo-host"])
MONGO_PORT = os.environ.get('MONGO_PORT', secrets["mongo-port"])
MONGO_USERNAME = os.environ.get('MONGO_USERNAME', secrets["mongo-user"])
MONGO_PASSWORD = os.environ.get('MONGO_PASSWORD', secrets["mongo-password"])
MONGO_DBNAME = os.environ.get('MONGO_DBNAME', secrets["mongo-db"])


# Enable reads (GET), inserts (POST) and DELETE for resources/collections
# (if you omit this line, the API will default to ['GET'] and provide
# read-only access to the endpoint).
# RESOURCE_METHODS = ['GET', 'POST', 'DELETE']

# Enable reads (GET), edits (PATCH) and deletes of individual items
# (defaults to read-only item access).
# ITEM_METHODS = ['GET', 'PATCH', 'DELETE']

# We enable standard client cache directives for all resources exposed by the
# API. We can always override these global settings later.
CACHE_CONTROL = 'max-age=20'
CACHE_EXPIRES = 20

JWT_SECRET = "JimmyKun"
JWT_ISSUER = "Test"

tos = {
    'item_title': 'Terms Of Service',
    'schema': {
        'tos_text': {
            'type': 'string',
            'required': True,
        },
        'tos_displayed': {
            'type': 'boolean',
            'required': True,
        },
        'tos_accepted': {
            'type': 'boolean',
            'required': True,
        },
        'display_again': {
            'type': 'boolean',
            'required': True,
        },
    }
}

survey = {
    'item_title': 'Survey',
    'schema': {
        'ID': {
            'type': 'integer',
            'required': True,
        },
        'Question': {
            'type': 'string',
            'required': True,
        },
        'Type': {
            'type':  'string',
            'required': True,
        },
        'Answered': {
            'type': 'boolean',
            'required': True,
        }
    }
}

# The DOMAIN dict explains which resources will be available and how they will
# be accessible to the API consumer.
DOMAIN = {
    'users/username/tos': tos,
    'users/usernames/survey': survey,
    'anime/search/{animename}': {},
}
