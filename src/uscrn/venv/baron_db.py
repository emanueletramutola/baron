# simple module used for set up the DB connection

import psycopg2


def baronconnect():

    # return psycopg2.connect("dbname=baron user=baron_uscrn password=YVvy38T87xsxVDxS host=150.145.73.252")
    return psycopg2.connect("dbname=baron user=baron_uscrn password=YVvy38T87xsxVDxS host=localhost")
