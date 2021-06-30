#!python
# 2018 - Alessandro Di Filippo
# 2019 - Emanuele Tramutola
"""
This is the main module of the USCRN uncertainty computing.
It's intended to be executed via CLI in a Python3 environment.

The specific python 3rd-party modules dependencies to satisfy are:
- numpy, for numerical python
- multiprocessing, for parallelization solution using a pool of async process

If these modules are not installed in the system, the software will not work properly.
"""
import logging
import warnings

from baron_db import baronconnect
from paraldaily import paraldaily
from paralhourly import paralhourly
from paralmonthly import paralmonthly
from paralsubhourly import paralsubhourly

warnings.filterwarnings("ignore", category=RuntimeWarning)


def chain():
    try:
        conn = baronconnect()
        cur = conn.cursor()
        cur.execute("SELECT * FROM stations_to_process order by 2")

        data_row = cur.fetchall()
        conn.commit()
        conn.close()

        for x in data_row:
            paralsubhourly(x[0], x[1])
            paralhourly(x[0], x[1])
            paraldaily(x[0], x[1])
            paralmonthly(x[0], x[1])

    except:
        logging.error('ERROR: ', exc_info=True)


if __name__ == '__main__':
    logging.basicConfig(filename='/tmp/uscrn.log', level=logging.INFO)

    chain()
