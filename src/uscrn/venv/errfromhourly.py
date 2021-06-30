#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function works in a similar way to errfromsubhourly() in the hourly data processing, organising the date into
arrays depending on date and time before processing the arrays into a single value.

Uses the hourly data processing to get uncertainty contributions for precipitation, solar radiation, fixed resistor T
dependence and snow albedo. The snow albedo uncertainty is actually calculated from 5-minute data which is passed
through the hourly data processing. The uncertainties of Tmax, Tmin and Tmean are also found.

Since the daily Tmax is the highest hourly Tmax of that (local) day and the daily Tmin the lowest hourly Tmin these are
simply taken from the hourly files rather than re-calculated. The different combined sr and snow albedo contributions,
fixed resistor contributions and prec contributions to these uncertainties are also found and are used in the calculation
of the Tmean uncertainty as also output so that they can be used in the calculation of monthly Tmax and Tmin uncertainty.
"""
import decimal as dec
from datetime import datetime, date

import numpy as np

from uscrnHourly import uscrnHourly


def errfromhourly(conn, wbanno, dateconcat, timeconcat, tconcat, tmeansysterr):
    try:
        (thour, dateconcat, timeconcat, tmaxhour, tminhour, tcalcconcat, terrpos, terrneg, tmaxhourupos, tmaxhouruneg,
         tminhourupos, tminhouruneg, tcalcerrpos, tcalcerrneg, srconcat, precconcat, surtconcat, t5minarray,
         precerrhour, snchk, dlerr, prterr, solraderrhour, restol, resterrhour, leaderr, snerr, datehourstring,
         timehour,
         snerr5min, tmaxsr5min, tminsr5min, tmaxprec5min, tminprec5min, tmaxrest5min, tminrest5min, prtcal) = \
            uscrnHourly(conn, wbanno)

        datehour = [(date.toordinal(datetime.strptime(datestr, '%Y%m%d')) + 366) for datestr in datehourstring]

        # loop to make sure starting point of the sub-hourly data used is the same as the daily data
        for i in range(0, len(datehour)):

            if (datehour[i] == dateconcat[0]):
                arraystart = i  # arraystart is used to ignore the times for which there is sub-hourly data but no hourly data
                break

        rows = int(dec.Decimal((len(datehour) - arraystart) / 24).to_integral_exact('ROUND_UP'))

        thourarray = np.full((rows + 2, 24), np.nan)  # Hourly T values
        tmaxhourarray = np.full((rows + 2, 24), np.nan)  # Hourly Tmax values
        tmaxhourerrposarray = np.full((rows + 2, 24), np.nan)  # Hourly Tmax positive u
        tmaxhourerrnegarray = np.full((rows + 2, 24), np.nan)  # Hourly Tmax negative u
        tminhourarray = np.full((rows + 2, 24), np.nan)  # Hourly Tmin values
        tminhourerrposarray = np.full((rows + 2, 24), np.nan)  # Hourly Tmin positive u
        tminhourerrnegarray = np.full((rows + 2, 24), np.nan)  # hourly Tmin negative u
        tmaxsrhourarray = np.full((rows + 2, 24), np.nan)  # Hourly Tmax u from SR
        tmaxprechourarray = np.full((rows + 2, 24), np.nan)  # Hourly Tmax u from prec
        tmaxresthourarray = np.full((rows + 2, 24), np.nan)  # Hourly Tmax u from fixed resistor T dependance
        tminsrhourarray = np.full((rows + 2, 24), np.nan)  # Hourly Tmin u from SR
        tminprechourarray = np.full((rows + 2, 24), np.nan)  # Hourly Tmin u from prec
        tminresthourarray = np.full((rows + 2, 24), np.nan)  # Hourly Tmin u from fixed resistor T dependance
        solraderrarray = np.full((rows + 2, 24), np.nan)  # solar radiation uncertainty
        snerrarray = np.full((rows + 2, 288),
                             np.nan)  # snow presence uncertainty (is from 5-minute data so 288 entries per day instead of 24)
        precerrarray = np.full((rows + 2, 24), np.nan)  # precipitation uncertainty
        resterrarray = np.full((rows + 2, 24), np.nan)  # fixed resistor T dependance uncertainty

        for i in range(arraystart, len(datehour)):

            # assigns an array position based on date and hour
            x = (datehour[i] - datehour[arraystart]) + 1
            y = int(timehour[i][0:2])  # assigns an array position based on time

            if y == 0:  # Time given is the end of the time period so an entry at 00 hours is the last from the previous day
                if x == 1: continue

                x = x - 1
                y = 23  # assign to last 5minute position

            ysnerr = 1 + (y - 1) * 12

            try:
                thourarray[x, y] = thour[i]
            except IndexError:
                pass
            try:
                tmaxhourarray[x, y] = tmaxhour[i]
            except IndexError:
                pass
            try:
                tmaxhourerrposarray[x, y] = tmaxhourupos[:, i]
            except IndexError:
                pass
            try:
                tmaxhourerrnegarray[x, y] = tmaxhouruneg[:, i]
            except IndexError:
                pass
            try:
                tminhourarray[x, y] = tminhour[i]
            except IndexError:
                pass
            try:
                tminhourerrposarray[x, y] = tminhourupos[:, i]
            except IndexError:
                pass
            try:
                tminhourerrnegarray[x, y] = tminhouruneg[:, i]
            except IndexError:
                pass
            try:
                solraderrarray[x, y] = solraderrhour[i]
            except IndexError:
                pass
            try:
                snerrarray[x, ysnerr:ysnerr + 12] = snerr5min[i, :]
            except IndexError:
                pass
            try:
                precerrarray[x, y] = precerrhour[i]
            except IndexError:
                pass
            try:
                resterrarray[x, y] = resterrhour[i]
            except IndexError:
                pass
            try:
                tmaxsrhourarray[x, y] = tmaxsr5min[:, i]
            except IndexError:
                pass
            try:
                tmaxprechourarray[x, y] = tmaxprec5min[:, i]
            except IndexError:
                pass
            try:
                tmaxresthourarray[x, y] = tmaxrest5min[i]
            except IndexError:
                pass
            try:
                tminsrhourarray[x, y] = tminsr5min[:, i]
            except IndexError:
                pass
            try:
                tminprechourarray[x, y] = tminprec5min[:, i]
            except IndexError:
                pass
            try:
                tminresthourarray[x, y] = tminrest5min[i]
            except IndexError:
                pass

        dim = len(tconcat)

        precerrarray = precerrarray[0:dim, :]
        thourarray = thourarray[0:dim, :]
        tmaxhourarray = tmaxhourarray[0:dim, :]
        tmaxhourerrposarray = tmaxhourerrposarray[0:dim, :]
        tmaxhourerrnegarray = tmaxhourerrnegarray[0:dim, :]
        tminhourarray = tminhourarray[0:dim, :]
        tminhourerrposarray = tminhourerrposarray[0:dim, :]
        tminhourerrnegarray = tminhourerrnegarray[0:dim, :]
        solraderrarray = solraderrarray[0:dim, :]
        snerrarray = snerrarray[0:dim, :]
        precerrarray = precerrarray[0:dim, :]
        resterrarray = resterrarray[0:dim, :]
        tmaxsrhourarray = tmaxsrhourarray[0:dim, :]
        tmaxprechourarray = tmaxprechourarray[0:dim, :]
        tmaxresthourarray = tmaxresthourarray[0:dim, :]
        tminsrhourarray = tminsrhourarray[0:dim, :]
        tminprechourarray = tminprechourarray[0:dim, :]
        tminresthourarray = tminresthourarray[0:dim, :]

        dim2 = len(precerrarray)

        # prec goes from quasi-systematic to random so the uncertainty from prec is summed in quadrature
        precerr = np.sqrt(np.nansum((precerrarray ** 2), axis=1))

        # quasi-systematic uncertianties of the daily average product are the meaned uncertainties of the sub-hourly product
        resterr = np.nanmean(resterrarray)

        # quasi-systematic uncertianties of the daily average product are the meaned uncertainties of the sub-hourly product
        solraderr = np.nanmean(solraderrarray)
        # quasi-systematic uncertianties of the daily average product are the meaned uncertainties of the sub-hourly product
        snerr = np.nanmean(snerrarray)

        tmaxerrpos = np.full((1, np.fmin(dim2, dim)), np.nan)
        tmaxerrneg = np.full((1, np.fmin(dim2, dim)), np.nan)
        tminerrpos = np.full((1, np.fmin(dim2, dim)), np.nan)
        tminerrneg = np.full((1, np.fmin(dim2, dim)), np.nan)
        tminsrerr = np.full((1, np.fmin(dim2, dim)), np.nan)
        tmaxsr = np.full((1, np.fmin(dim2, dim)), np.nan)
        tmaxrest = np.full((1, np.fmin(dim2, dim)), np.nan)
        tminrest = np.full((1, np.fmin(dim2, dim)), np.nan)
        tminsr = np.full((1, np.fmin(dim2, dim)), np.nan)
        tmaxprec = np.full((1, np.fmin(dim2, dim)), np.nan)
        tminprec = np.full((1, np.fmin(dim2, dim)), np.nan)

        for i in range(0, np.fmin(tmaxerrpos.shape[1], dim)):

            for j in range(0, 24):

                if tminhourarray[i, j] == np.nanmin(tminhourarray[i, :]):

                    tminerrpos[:, i] = tminhourerrposarray[
                        i, j]  # positive uncertainty of sub-hourly T value used for Tmin
                    tminerrneg[:, i] = tminhourerrnegarray[
                        i, j]  # negative uncertainty of sub-hourly T value used for Tmin
                    tminsr[:, i] = tminsrhourarray[i, j]  # U contribution from SR
                    tminprec[:, i] = tminprechourarray[i, j]  # U contribution from prec
                    tminrest[:, i] = tminresthourarray[i, j]  # U contribution from fixed R T dependance

                elif tmaxhourarray[i, j] == np.nanmax(tmaxhourarray[i, :]):

                    tmaxerrpos[:, i] = tmaxhourerrposarray[
                        i, j]  # positive uncertainty of sub-hourly T value used for Tmax
                    tmaxerrneg[:, i] = tmaxhourerrnegarray[
                        i, j]  # negative uncertainty of sub-hourly T value used for Tmax
                    tmaxsr[:, i] = tmaxsrhourarray[i, j]  # U contribution from SR
                    tmaxprec[:, i] = tmaxprechourarray[i, j]  # U contribution from prec
                    tmaxrest[:, i] = tmaxresthourarray[i, j]  # U contribution from fixed R T dependance

        # tmean is (tmax+tmin)/2 and the uncertianty is the average of the uncertaities of tmax and tmin

        tmeansysterr = tmeansysterr.reshape((1, tmeansysterr.shape[0]))
        if tmeansysterr.shape[1] > tmaxrest.shape[1]:
            delta = tmeansysterr.shape[1] - tmaxrest.shape[1]
            tmaxrest = np.hstack([tmaxrest, np.full((1, delta), np.nan)])
        if tmeansysterr.shape[1] > tminrest.shape[1]:
            delta = tmeansysterr.shape[1] - tminrest.shape[1]
            tminrest = np.hstack([tminrest, np.full((1, delta), np.nan)])
        if tmeansysterr.shape[1] > tmaxsr.shape[1]:
            delta = tmeansysterr.shape[1] - tmaxsr.shape[1]
            tmaxsr = np.hstack([tmaxsr, np.full((1, delta), np.nan)])
        if tmeansysterr.shape[1] > tminsr.shape[1]:
            delta = tmeansysterr.shape[1] - tminsr.shape[1]
            tminsr = np.hstack([tminsr, np.full((1, delta), np.nan)])

        tmeanerrpos = np.sqrt((tmeansysterr ** 2) + (((tmaxrest + tminrest) / 2) ** 2) + (((tmaxsr + tminsr) / 2) ** 2))

        # as above, nut for the negative uncertainty
        if tmeansysterr.shape[1] > tmaxprec.shape[1]:
            delta = tmeansysterr.shape[1] - tmaxprec.shape[1]
            tmaxprec = np.hstack([tmaxprec, np.full((1, delta), np.nan)])
        if tmeansysterr.shape[1] > tminprec.shape[1]:
            delta = tmeansysterr.shape[1] - tminprec.shape[1]
            tminprec = np.hstack([tminprec, np.full((1, delta), np.nan)])
        tmeanerrneg = np.sqrt(
            (tmeansysterr ** 2) + (((tmaxrest + tminrest) / 2) ** 2) + ((((tmaxprec + tminprec) / 2) ** 2) / 2))

    except Exception:
        raise

    return precerr, tmaxerrpos, tmaxerrneg, tminerrpos, tminerrneg, tmeanerrpos, tmeanerrneg, solraderr, snerr, resterr, thourarray, snerrarray, \
           tmaxsr, tmaxhour, tmaxprec, tmaxrest, tminsr, tminprec, tminrest
