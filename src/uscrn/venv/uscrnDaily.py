#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function is very similar to the uscrnHourly() in the hourly processing. It first concatenates files into a single
array for each variable before using separate functions for the uncertainty estimation.

The main difference from uscrnHourly() is that it does not find the systematic uncertaitnies of Tmax and Tmin, but does
for Tmean, using uscrnt2. The non-systematic uncertainties are then found using errfromhourly, along with the
uncertainties of Tmax, Tmin and Tmean, before uscrnt2 is used to find the total uncertainty.

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
 - errfromhourly(), which uses the uscrnHourly()
"""
from datetime import datetime, date

import numpy as np

from errfromhourly import errfromhourly
from uscrnt2 import uscrnt2


def uscrnDaily(conn, wbanno):
    prefix = "CRND0103-"
    cur = conn.cursor()

    cur.execute("SELECT * FROM uscrn.daily WHERE wbanno = %s", (wbanno,))
    result = list(zip(*(cur.fetchall())))
    conn.commit()

    longitude = result[6][0]
    latitude = result[7][0]

    site_cell = {
        'lst_date': [(date.toordinal(datetime.strptime(x, '%Y%m%d')) + 366) for x in result[4]],
        't_daily_max': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[8]],
        't_daily_min': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[9]],
        't_daily_mean': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[10]],
        't_daily_avg': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[11]],
        'p_daily_calc': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[12]],
        'solarad_daily': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[13]],
        'sur_temp_daily_avg': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[17]]
    }

    try:
        (terrpos, terrneg, snchk, precerr, tmeandlerr, tmeanprtacc, tmeanprtcal, solraderr, tmeanrestol, tmaxresterr,
         tmeanleaderr, snerr) = \
            uscrnt2(conn, site_cell['t_daily_mean'], site_cell['lst_date'], [], [], [], [], [], [], wbanno, 0,
                    npoints=12)

        tmeansysterr = np.sqrt(
            (tmeandlerr ** 2) + (tmeanprtacc ** 2) + (tmeanrestol ** 2) + (tmeanleaderr ** 2) + (tmeanprtcal ** 2))

        (precerr, tmaxerrpos, tmaxerrneg, tminerrpos, tminerrneg, tmeanerrpos, tmeanerrneg, solraderr, snerr, resterr,
         thourarray, snerrarray, tmaxsr, tmax, tmaxprec, tmaxrest, tminsr, tminprec, tminrest) = \
            errfromhourly(conn, wbanno, site_cell['lst_date'], [], site_cell['t_daily_avg'], tmeansysterr)

        (terrpos, terrneg, snchk, precerr, dlerr, prtacc, prtcal, solraderr, restol, resterr, leaderr, snerr) \
            = uscrnt2(conn, site_cell['t_daily_avg'], site_cell['lst_date'], [], [], [], solraderr, snerr, resterr,
                      wbanno,
                      np.sqrt(precerr / 288), npoints=6 * 288)
    except Exception:
        raise

    return site_cell['t_daily_avg'], site_cell['lst_date'], site_cell['t_daily_max'], site_cell['t_daily_min'], \
           site_cell['t_daily_mean'], terrpos, terrneg, tmaxerrpos, tmaxerrneg, tminerrpos, \
           tminerrneg, tmeanerrpos, tmeanerrneg, site_cell['solarad_daily'], site_cell['p_daily_calc'], \
           site_cell['sur_temp_daily_avg'], thourarray, precerr, solraderr, \
           snerr, resterr, snerrarray, tmaxsr, resterr, tmaxrest, tminsr, tminprec, tminrest, prtacc, dlerr, leaderr, restol, prtcal
