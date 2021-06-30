#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function is mostly the same as for the sub-hourly. Imports data from the messagepack files and concatenates into a
single data series. The Tmax, Tmin, and Tcalc are included and treated in a similar way to T. Before the uncertainty of
the average is calculated, uscrnt2() is run using both Tmax and Tmin as an input so that the systematic parts of their
uncertainties can be calculated.

The total uncertainties for Tmax, Tmin and Tcalc are then calculated using the errfromsubhourly function along with the
precipitation uncertainty part for the average temperature.

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


It calls three others functions:
 - solradcheck(), which is used in producing the replacement values for the parts of the solar radiation data which are
                  missing;
 - uscrnt2(), which is used for calculating and combining different uncertainties;
 - errfromsubhourly(), which uses the uscrnSubhourly() function to calculate parts of the hourly uncertainties.
"""
from datetime import datetime, date

import numpy as np

from errfromsubhourly import errfromsubhourly
from solradcheck import solradcheck
from uscrnt2 import uscrnt2


def uscrnHourly(conn, wbanno):
    prefix = "CRNH0203-"
    cur = conn.cursor()

    cur.execute("SELECT * FROM uscrn.hourly WHERE wbanno = %s", (wbanno,))
    result = list(zip(*(cur.fetchall())))
    conn.commit()

    longitude = result[9][0]
    latitude = result[10][0]

    site_cell = {
        'utc_date': [(date.toordinal(datetime.strptime(x, '%Y%m%d')) + 366) for x in result[4]],
        'utc_time': result[5],
        'lst_date': result[6],
        'lst_time': result[7],
        't_calc': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[11]],
        't_hr_avg': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[12]],
        't_max': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[13]],
        't_min': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[14]],
        'p_calc': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[15]],
        'solarad': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[16]],
        'sur_temp': [np.nan if int(x) == 9999 or int(x) == -9999 else x for x in result[23]],
        'srest': [(solradcheck(longitude, latitude, result[7][idx], result[5][idx], result[6][idx]) * 1600)
                  if int(x) == 9999 or int(x) == -9999 else x for idx, x in enumerate(result[16])],
        'surterr': [(result[23][idx] - 2) if int(x) == 9999 or int(x) == -9999 else x for idx, x in
                    enumerate(result[23])]
    }

    try:
        # uncertainties in tmax which are related to temperature
        # when precerr doesn't have to be calculated, it's set to 0 and passed as input (second-last in the list of input parameters)
        (terrpos, terrneg, snchk, precerr, tmaxdlerr, tmaxprtacc, prtcal, solraderr, tmaxrestol, tmaxresterr,
         tmaxleaderr, snerr) = \
            uscrnt2(conn, site_cell['t_max'], site_cell['utc_date'], site_cell['srest'], site_cell['p_calc'],
                    site_cell['surterr'], [], [], [], wbanno, 0, npoints=6)

        # the systematic part of the tmax uncertainty
        operand_tmaxsysterr = np.vstack([(tmaxdlerr ** 2), (tmaxprtacc ** 2), (tmaxrestol ** 2), (tmaxresterr ** 2)])
        tmaxsysterr = np.sqrt(operand_tmaxsysterr.sum(axis=0) + (tmaxleaderr ** 2))

        # uncertainties in tmin which are related to temperature
        (terrpos, terrneg, snchk, precerr, tmindlerr, tminprtacc, prtcal, solraderr, tminrestol, tminresterr,
         tminleaderr, snerr) = \
            uscrnt2(conn, site_cell['t_min'], site_cell['utc_date'], site_cell['srest'], site_cell['p_calc'],
                    site_cell['surterr'], [], [], [], wbanno, 0, npoints=6)

        # the systematic part of the tmin uncertainty
        operand_tminsysterr = np.vstack([(tmindlerr ** 2), (tminprtacc ** 2), (tminrestol ** 2), (tminresterr ** 2)])
        tminsysterr = np.sqrt(operand_tminsysterr.sum(axis=0) + (tminleaderr ** 2))

        # calculates part of the Hourly data product uncertainty using the sub-hourly data

        (precerr, tmaxerrpos, tmaxerrneg, tminerrpos, tminerrneg, tcalcerrpos, tcalcerrneg, t5minarray, snerr5min,
         tmaxsrerr,
         tminsrerr, tmaxprecerr, tminprecerr) = \
            errfromsubhourly(conn, wbanno, site_cell['utc_date'], site_cell['utc_time'],
                             site_cell['t_hr_avg'], site_cell['t_max'], site_cell['t_min'], tmaxsysterr, tminsysterr)

        # calculates the uncertainty of temperature
        (terrpos, terrneg, snchk, precerr, dlerr, prtacc, prtcal, solraderr, restol, resterr, leaderr, snerr) = \
            uscrnt2(conn, site_cell['t_hr_avg'], site_cell['utc_date'], site_cell['srest'], site_cell['p_calc'],
                    site_cell['surterr'], [], [], [], wbanno, precerr, npoints=6)

    except Exception:
        raise

    return site_cell['t_hr_avg'], site_cell['utc_date'], site_cell['utc_time'], site_cell['t_max'], site_cell['t_min'], \
           site_cell[
               't_calc'], terrpos, terrneg, tmaxerrpos, tmaxerrneg, tminerrpos, tminerrneg, tcalcerrpos, tcalcerrneg, \
           site_cell['solarad'], site_cell['p_calc'], site_cell['sur_temp'], t5minarray, precerr, snchk, dlerr, prtacc, \
           solraderr, restol, resterr, leaderr, snerr, site_cell['lst_date'], site_cell[
               'lst_time'], snerr5min, tmaxsrerr, \
           tminsrerr, tmaxprecerr, tminprecerr, tmaxresterr, tminresterr, prtcal
