#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function is called by main elab. chain and it's used for atomic computing of the subohurly uncertainty.
The function itself call the uscrnSubhourly() and use the output parameters to fill the result fields in the DB.

Here is done also the computation of the different categories of uncertainty, starting from the relevant uncertainty
contributions as computed by the chain.

For each site of the USCRN network the logic on behind, is that the computing of uncertainty is done over the
years, site by site

A try-fail approach is used, due to the fact that not all the sites are used for the uncertainty computing: the sites
whose stat-id (or wbanno) is not in the list included in the metadata tables will be not processed.

"""

import logging
import random
import string
from datetime import date, datetime
from itertools import zip_longest

import numpy as np
import psycopg2
from psycopg2 import sql

from baron_db import baronconnect
from uscrnSubhourly import uscrnSubhourly

logging.basicConfig(filename='/tmp/uscrn.log', level=logging.INFO)


def paralsubhourly(wbanno, site):
    try:
        conn = baronconnect()
    except psycopg2.Error:
        logging.error('ERROR: ', exc_info=True)
        print("\nFATAL: unable to connect to DB from a paral- routine")
        logging.error("\nFATAL: unable to connect to DB from a paral- routine")
        raise

    try:
        (tconcat, terrpos, terrneg, dateconcat, timeconcat, srconcat, precconcat, surtconcat, snchk,
         precerr, dlerr, prtacc, prtcal, solraderr, restol, resterr, leaderr, snerr, localdateconcat,
         localtimeconcat) = uscrnSubhourly(conn, wbanno)

        datestr = [datetime.strftime(date.fromordinal(i - 366), '%Y%m%d') for i in dateconcat]

        # -ve Systematic
        sys_neg_operand = np.vstack([
            np.power(dlerr, 2),
            np.full((dlerr.shape[0],), prtcal),
            np.power(solraderr, 2),
            np.power(restol, 2),
            np.power(resterr, 2),
            np.full((dlerr.shape[0],), leaderr),
            np.power(snerr, 2).reshape((dlerr.shape[0],))
        ])
        sys_neg = np.sqrt(sys_neg_operand.sum(axis=0))

        # +ve Systematic
        sys_pos_operand = np.vstack([
            np.power(precerr, 2),
            np.power(dlerr, 2),
            np.full((dlerr.shape[0],), prtcal),
            np.power(restol, 2),
            np.power(resterr, 2),
            np.full((dlerr.shape[0],), leaderr)
        ])
        sys_pos = np.sqrt(sys_pos_operand.sum(axis=0))

        # +ve/-ve Quasi-systematic
        quasi_sys = 0

        # +ve/-ve Random
        random_ve = prtacc

        # attention: the positive and negative contributions are switched just before the output print, due to NPL
        # internal discussion about positive and negative uncertainties
        rows = zip_longest(datestr, timeconcat, tconcat, terrneg, terrpos, random_ve, sys_pos, sys_neg, [quasi_sys],
                           fillvalue=quasi_sys)

        cur = conn.cursor()

        # random 8 chars name generation for the temporary table
        temp_table = ''.join(random.choice(string.ascii_lowercase) for i in range(8))

        # temporary table creation and after drop the primary key field id of the source one
        cur.execute(sql.SQL("CREATE TEMP TABLE {} AS SELECT * FROM uscrn.unc_subhourly LIMIT 0").format(
            sql.Identifier(temp_table)))
        cur.execute(sql.SQL("ALTER TABLE {} DROP COLUMN id").format(sql.Identifier(temp_table)))
        conn.commit()

        # iterating the zip object and populating the temp table row-by-row
        for row in rows:
            cur.execute(sql.SQL("INSERT INTO {} (wbanno, sitename, date, time, t, terr_p, terr_m, random_pm, sys_p, "
                                "sys_m, quasisys_pm) "
                                "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
                                ).format(sql.Identifier(temp_table)), [wbanno,
                                                                       site, row[0], row[1], row[2], row[3], row[4],
                                                                       row[5], row[6], row[7], row[8]])

        conn.commit()
        # insert into the main unc_subhourly table of the new values selected from the temp table
        cur.execute(sql.SQL("INSERT INTO uscrn.unc_subhourly (wbanno, sitename, date, time, t, terr_p, terr_m, "
                            "random_pm, sys_p, sys_m, quasisys_pm) "
                            "SELECT * FROM {}"
                            ).format(sql.Identifier(temp_table)))

        cur.execute(sql.SQL("DROP TABLE IF EXISTS {}").format(sql.Identifier(temp_table)))
        conn.commit()
        conn.close()
        print("Subhourly chain - processed: {}".format(site))
        logging.info("Subhourly chain - processed: {}".format(site))

    except TypeError:
        logging.error('ERROR: ', exc_info=True)
        print('\x1b[0;37m' + 'Unable to process this site: WBANNO {} not in the list'.format(
            wbanno) + '\x1b[0m')
        logging.error('\x1b[0;37m' + 'Unable to process this site: WBANNO {} not in the list'.format(
            wbanno) + '\x1b[0m')

    except Exception as ex:
        logging.error('ERROR: ', exc_info=True)
        print('\x1b[0;37m' + 'Unable to process {}:{}'.format(site, ex) + '\x1b[0m')
        logging.error('\x1b[0;37m' + 'Unable to process {}:{}'.format(site, ex) + '\x1b[0m')
