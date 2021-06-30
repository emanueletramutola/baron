#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
Since the monthly files are not separated by years the different files do not need to be concatenated into a single array.
This function replaces NaN values and finds the systematic parts of Tmax, Tmin and Tmean using uscrnt2() before using
errfromdaily() to find non-systematic uncertainty contributions for T as well as total Tmax, Tmin and Tmean uncertainty.

The total uncertainty of T is then found using uscrnt2(). After this the outputs from the monthly are shortened so that
only the months which overlap the sub-hourly data, and therefore have a complete uncertainty estimation, are output.

The function input parameters are:
 - sitename
    type: string
    desc: the current site name, part of the filename retrieved
 - years
    type: list
    desc: a list of the years where exists some data for the current site
 - msg
    type: string
    desc: path of the serialized messagepack files local repository


It calls two others functions:
 - uscrnt2(), which is used for calculating and combining different uncertainties;
 - errfromdaily(), which uses the uscrnDaily() to find non-systematic uncertainty contributions for T, Tmax, Tmin, Tmean
"""
from calendar import monthrange
from datetime import datetime, date

import numpy as np

from errfromdaily import errfromdaily
from uscrnt2 import uscrnt2


# ORIGINALE - def uscrnMonthly(conn, sitename, wbanno, years):
def uscrnMonthly(conn, wbanno):
    prefix = "CRNM0102-"
    cur = conn.cursor()

    cur.execute("SELECT * FROM uscrn.monthly WHERE wbanno=%s", (wbanno,))

    result = list(zip(*(cur.fetchall())))

    conn.commit()

    site_cell = {
        'lst_yrmo': [(date.toordinal(datetime.strptime(x, '%Y%m')) + 366) for x in result[4]],
        't_monthly_avg': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[11]],
        't_monthly_max': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[8]],
        't_monthly_min': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[9]],
        't_monthly_mean': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[10]],
        'sur_temp_monthly_avg': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[17]],
        'p_monthly_calc': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[12]],
        'solrad_monthly_avg': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[13]]
    }

    # array to hold number of days in month, used for finding n - leap years compliant

    monlen = np.asarray([monthrange(int(datetime.strftime(date.fromordinal(tik), '%Y%m')[:4]),
                                    int(datetime.strftime(date.fromordinal(tik), '%Y%m')[-2:]))[1]
                         for tik in site_cell['lst_yrmo']], dtype=int)

    monlen = monlen.reshape((monlen.shape[0], 1))

    try:
        (terrpos, terrneg, snchk, precerr, tmaxdlerr, tmaxprtacc, tmaxprtcal, solraderr, tmaxrestol, tmaxresterr,
         tmaxleaderr, snerr) = \
            uscrnt2(conn, site_cell['t_monthly_max'], site_cell['lst_yrmo'], [], [], [], [], [], [], wbanno, 0,
                    npoints=6 * monlen)

        operand_tmaxsysterr = np.vstack([(tmaxdlerr ** 2), (tmaxrestol ** 2), (tmaxresterr ** 2), (tmaxprtacc ** 2)])
        tmaxsysterr = np.sqrt(operand_tmaxsysterr.sum(axis=0) + (tmaxleaderr ** 2) + (tmaxprtcal ** 2))

        (terrpos, terrneg, snchk, precerr, tmindlerr, tminprtacc, tminprtcal, solraderr, tminrestol, tminresterr,
         tminleaderr, snerr) = \
            uscrnt2(conn, site_cell['t_monthly_min'], site_cell['lst_yrmo'], [], [], [], [], [], [], wbanno, 0,
                    npoints=6 * monlen)

        operand_tminsysterr = np.vstack([(tmindlerr ** 2), (tminprtacc ** 2), (tminrestol ** 2)])
        tminsysterr = np.sqrt(operand_tminsysterr.sum(axis=0) + (tminleaderr ** 2) + (tminprtcal ** 2))

        (terrpos, terrneg, snchk, precerr, tmeandlerr, tmeanprtacc, tmeanprtcal, solraderr, tmeanrestol, tmeanresterr,
         tmeanleaderr, snerr) = \
            uscrnt2(conn, site_cell['t_monthly_min'], site_cell['lst_yrmo'], [], [], [], [], [], [], wbanno, 0,
                    npoints=12 * monlen)

        operand_tmeansysterr = np.vstack([(tmeandlerr ** 2), (tmeanprtacc ** 2), (tmeanrestol ** 2)])
        tmeansysterr = np.sqrt(operand_tmeansysterr.sum(axis=0) + (tmeanprtcal ** 2) + (tmeanleaderr ** 2))

        (precerr, tmaxerrpos, tmaxerrneg, tminerrpos, tminerrneg, tcalcerrpos, tcalcerrneg, solraderr, snerr, resterr,
         tconcat2, dateconcat2, arraystartmon, monlen2) = errfromdaily(conn, wbanno,
                                                                       site_cell['lst_yrmo'], [],
                                                                       site_cell['t_monthly_avg'], tmaxsysterr,
                                                                       tminsysterr,
                                                                       tmeansysterr, monlen)

        if monlen2.shape[0] < precerr.shape[0]:
            delta = precerr.shape[0] - monlen2.shape[0]
            monlen2 = np.vstack([monlen2, np.full((delta, 1), np.nan)])

        (terrpos, terrneg, snchk, precerr, dlerr, prtacc, prtcal, solraderr, restol, resterr, leaderr, snerr) \
            = uscrnt2(conn, tconcat2, dateconcat2, [], [], [], solraderr, snerr, resterr, wbanno,
                      precerr / np.sqrt(288 * monlen2), npoints=6 * 288)

        arrayendmon = arraystartmon + len(dateconcat2)

        # shortens srconcat so only parts where there is an uncertainty estimation is output
        site_cell['solrad_monthly_avg'] = site_cell['solrad_monthly_avg'][arraystartmon:arrayendmon]

        # shortens precconcat so only parts where there is an uncertainty estimation is output
        site_cell['p_monthly_calc'] = site_cell['p_monthly_calc'][arraystartmon:arrayendmon]

        # shortens surtconcat so only parts where there is an uncertainty estimation is output
        site_cell['sur_temp_monthly_avg'] = site_cell['sur_temp_monthly_avg'][arraystartmon:arrayendmon]

        # shortens tmaxconcat so only parts where there is an uncertainty estimation is output
        site_cell['t_monthly_max'] = site_cell['t_monthly_max'][arraystartmon:arrayendmon]

        # shortens tminconcat so only parts where there is an uncertainty estimation is output
        site_cell['t_monthly_min'] = site_cell['t_monthly_min'][arraystartmon:arrayendmon]

        # shortens tmeanconcat so only parts where there is an uncertainty estimation is output
        site_cell['t_monthly_mean'] = site_cell['t_monthly_mean'][arraystartmon:arrayendmon]

    except Exception:
        raise

    return tconcat2, dateconcat2, site_cell['t_monthly_max'], site_cell['t_monthly_min'], \
           site_cell['t_monthly_mean'], terrpos, terrneg, tmaxerrpos, tmaxerrneg, tminerrpos, \
           tminerrneg, tcalcerrpos, tcalcerrneg, site_cell['solrad_monthly_avg'], site_cell['p_monthly_calc'], \
           site_cell['sur_temp_monthly_avg'], precerr, prtacc, dlerr, leaderr, restol, solraderr, snerr, resterr, prtcal
