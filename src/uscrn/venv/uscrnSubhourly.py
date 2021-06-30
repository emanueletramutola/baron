#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function is used to calculate the uscrn 5min uncertainty, here called subhourly uncertainty. The main purpose is to
take the data from the different yearly sources for the site and combine them into a main dictionary having a key for
each variable list.

This function also replaces the placeholder values in the USCRN files (e.g.-9999 for temperature) into NaN. For surface
temperature and solar radiation it also produces arrays where the NaN values have been replaced by the values used for
uncertainty estimation in the absence of these measurements (described in the PTU).

The function input parameters are:
 - sitename
    type: string
    desc: the current site name, part of the filename retrieved
 - years
    type: list
    desc: a list of the years where exists some data for the current site
 - wbanno
    type: integer
    desc: wban number of the site

The function output parameters are:
 - site_cell['air_temperature']
    type: list
    desc: air temperatures
 - terrpos
    type: numpy array
    desc: positive uncertainty
 - terrneg
    type: numpy array
    desc: negative uncertainty
 - site_cell['utc_date']
    type: list
    desc: dates in utc
 - site_cell['utc_time']
    type: list
    desc: times in utc
 - site_cell['solar_radiation']
    type:
    desc:
 - site_cell['precipitation']
    type:
    desc:
 - site_cell['surface_temperature']
    type:
    desc:
 - snchk
    type:
    desc:
 - precerr
    type:
    desc:
 - dlerr
    type:
    desc: datalogger error
 - prtacc
    type:
    desc:
 - prtcal
    type:
    desc:
 - solraderr
    type:
    desc:
 - restol
    type:
    desc:
 - resterr
    type:
    desc:
 - leaderr
    type:
    desc:
 - snerr
    type:
    desc:
 - site_cell['lst_date']
    type:
    desc:
 - site_cell['lst_time']
    type:
    desc:

It calls two others functions:
 - solradcheck(), which is used in producing the replacement values for the parts of the solar radiation data which are
                  missing;
 - uscrnt2(), which is used for calculating and combining different uncertainties.

"""

from datetime import datetime, date

import numpy as np

from solradcheck import solradcheck
from uscrnt2 import uscrnt2


def uscrnSubhourly(conn, wbanno):
    prefix = "CRNS0101-05-"
    cur = conn.cursor()

    cur.execute("SELECT * FROM uscrn.subhourly WHERE wbanno = %s", (wbanno,))

    result = list(zip(*(cur.fetchall())))
    conn.commit()

    longitude = result[9][0]
    latitude = result[10][0]

    site_cell = {
        'utc_date': [(date.toordinal(datetime.strptime(x, '%Y%m%d')) + 366) for x in result[4]],
        'utc_time': result[5],
        'air_temperature': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[11]],
        'surface_temperature': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[15]],
        'solar_radiation': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[13]],
        'precipitation': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[12]],
        'lst_date': result[6],
        'lst_time': result[7],
        'srest': [(solradcheck(longitude, latitude, result[7][idx], result[5][idx], result[6][idx]))
                  if int(x) == 9999 or int(x) == -9999 else x for idx, x in enumerate(result[13])],
        'surterr': [(result[11][idx] - 2) if int(x) == 9999 or int(x) == -9999 else x for idx, x in
                    enumerate(result[15])]
    }

    # calculates the uncertainty of temperature
    try:
        (terrpos, terrneg, snchk, precerr, dlerr, prtacc, prtcal, solraderr, restol, resterr, leaderr, snerr) = \
            uscrnt2(conn, site_cell['air_temperature'], site_cell['utc_date'], site_cell['srest'],
                    site_cell['precipitation'], site_cell['surterr'], [], [], [], wbanno, 1, npoints=6)
    except Exception:
        raise

    return site_cell['air_temperature'], terrpos, terrneg, site_cell['utc_date'], site_cell['utc_time'], site_cell[
        'solar_radiation'], \
           site_cell['precipitation'], site_cell[
               'surface_temperature'], snchk, precerr, dlerr, prtacc, prtcal, solraderr, \
           restol, resterr, leaderr, snerr, site_cell['lst_date'], site_cell['lst_time']
