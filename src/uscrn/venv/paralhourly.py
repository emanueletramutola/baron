#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function is called by main elab. chain and it's used for atomic computing of the hourly uncertainty.
The function itself call the uscrnHourly() and use the output parameters to fill the result fields in the DB.

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
from uscrnHourly import uscrnHourly

logging.basicConfig(filename='/tmp/uscrn.log', level=logging.INFO)


def paralhourly(wbanno, site):
    try:
        conn = baronconnect()
    except psycopg2.Error:
        logging.error('ERROR: ', exc_info=True)
        print("\nFATAL: unable to connect to DB from a paral- routine")
        logging.error("\nFATAL: unable to connect to DB from a paral- routine")
        raise

    try:
        (tconcat, dateconcat, timeconcat, tmaxconcat, tminconcat, tcalcconcat, terrpos, terrneg, tmaxerrpos, tmaxerrneg,
         tminerrpos, tminerrneg, tcalcerrpos, tcalcerrneg, srconcat, precconcat, surtconcat, t5minarray, precerr, snchk,
         dlerr, prterr, solraderr, restol, resterr, leaderr, snerr, localdateconcat, localtimeconcat, snerr5min,
         tmaxsrerr,
         tminsrerr, tmaxprecerr, tminprecerr, tmaxresterr, tminresterr, prtcal) = \
            uscrnHourly(conn, wbanno)

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

        # -ve Quasi-systematic
        quasi_sys_neg = 0

        # +ve Quasi-systematic
        quasi_sys_pos = precerr / np.sqrt(72)  # ATTENTION: n=72 for mean

        # +ve/-ve Random
        trandom = prterr

        # attention: the positive and negative contributions are switched just before the output print, due to NPL
        # internal discussion about positive and negative uncertainties
        rows = zip_longest(datestr, timeconcat, tconcat, terrneg, terrpos,
                           tmaxerrneg.ravel().tolist(), tmaxerrpos.ravel().tolist(), tminerrneg.ravel().tolist(),
                           tminerrpos.ravel().tolist(), tcalcerrneg, tcalcerrpos, trandom, sys_pos, sys_neg,
                           quasi_sys_pos, [quasi_sys_neg], fillvalue=0)

        cur = conn.cursor()

        # random 8 chars name generation for the temporary table
        temp_table = ''.join(random.choice(string.ascii_lowercase) for i in range(8))

        # temporary table creation and after drop the primary key field id of the source one
        cur.execute(sql.SQL("CREATE TEMP TABLE {} AS SELECT * FROM uscrn.unc_hourly LIMIT 0").format(
            sql.Identifier(temp_table)))
        cur.execute(sql.SQL("ALTER TABLE {} DROP COLUMN id").format(sql.Identifier(temp_table)))
        conn.commit()

        # iterating the zip object and populating the temp table row-by-row
        for row in rows:
            cur.execute(
                sql.SQL("INSERT INTO {} (wbanno, sitename, date, time, t, terr_p, terr_m, tmaxerr_p, tmaxerr_m, "
                        "tminerr_p, tminerr_m, tcalcerr_p, tcalcerr_m, random_pm, sys_p, sys_m, quasisys_p, "
                        "quasisys_m) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
                        ).format(sql.Identifier(temp_table)), [wbanno,
                                                               site, row[0], row[1], row[2], row[3], row[4],
                                                               row[5], row[6], row[7], row[8], row[9], row[10],
                                                               row[11], row[12], row[13], row[14], row[15]])
        conn.commit()
        # insert into the main unc_hourly table of the new values selected from the temp table
        cur.execute(sql.SQL("INSERT INTO uscrn.unc_hourly (wbanno, sitename, date, time, t, terr_p, terr_m, tmaxerr_p, "
                            "tmaxerr_m, tminerr_p, tminerr_m, tcalcerr_p, tcalcerr_m, random_pm, sys_p, sys_m, "
                            "quasisys_p, quasisys_m) "
                            "SELECT * FROM {}"
                            ).format(sql.Identifier(temp_table)))

        cur.execute(sql.SQL("DROP TABLE IF EXISTS {}").format(sql.Identifier(temp_table)))
        conn.commit()
        conn.close()

        print("Hourly chain - processed: {}".format(site))
        logging.info("Hourly chain - processed: {}".format(site))

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
