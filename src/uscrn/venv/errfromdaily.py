#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function works on a similar principle to errfromsubhourly() and errfromhourly(). Data from the daily data processing
is put into an array of month/day values before being processed into a single value depending on if the uncertainty
contribution is random or quasi-systematic. Because the Tmin and Tmax are the mean of daily Tmin and Tmax for that month
the way the uncertainty is calculated uses the previously calculated systematic uncertainty contributions and
non-systematic contributions found from the daily processing.
"""
from datetime import date

import numpy as np

from uscrnDaily import uscrnDaily


def errfromdaily(conn, wbanno, dateconcat, timeconcat, tconcat, tmaxsysterr, tminsysterr, tmeansysterr, monlen):
    try:
        (tconcat, dateday, tmaxconcat, tminconcat, tmeanconcat, terrpos, terrneg, tmaxerrpos, tmaxerrneg, tminerrpos,
         tminerrneg, tmeanerrpos, tmeanerrneg, srconcat, precconcat, surtconcat, thourarray, precdayu, solraddayu,
         sndayu,
         restdayu, snerrarray, tmaxsrday, tmaxprecday, tmaxrestday, tminsrday, tminprecday, tminrestday, prtacc, dlerr,
         leaderr, restol, prtcal) = uscrnDaily(conn, wbanno)

        startcheck = 0

        # loop to make sure starting point of the sub-hourly data used is the same as the daily data
        for i in range(0, len(dateday)):

            for j in range(0, len(dateconcat)):

                if (dateday[i] == dateconcat[j]):
                    arraystartday = i
                    arraystartmon = j
                    startcheck = 1
                    break

            if startcheck == 1:
                break

        # following shortens input from the monthly files so that only the parts
        # ovelapping the daily data is kept

        tout = tconcat[arraystartmon:len(tconcat) + 1]  # T
        dateout = dateconcat[arraystartmon:len(dateconcat) + 1]  # date
        tmaxsysterr = tmaxsysterr[arraystartmon:len(tconcat) + 1]  # systematic U of Tmax
        tminsysterr = tminsysterr[arraystartmon:len(tconcat) + 1]  # systematic U of Tmin
        tmeansysterr = tmeansysterr[arraystartmon:len(tconcat) + 1]  # systematic U of Tmean
        monlengthsout = monlen[arraystartmon:len(tconcat) + 1]  # Days in each month

        # following assigns space for arrays which store the outputs/variables used
        # to claculate outputs

        rows = len(dateconcat)

        tmaxsrdayarray = np.full((rows, 31), np.nan)  # Tmax u from SR
        tmaxprecdayarray = np.full((rows, 31), np.nan)  # Tmax u from prec
        tmaxrestdayarray = np.full((rows, 31), np.nan)  # Tmax u from fixed resistor T dependance
        tminsrdayarray = np.full((rows, 31), np.nan)  # Tmin u from SR
        tminprecdayarray = np.full((rows, 31), np.nan)  # Tmin u from prec
        tminrestdayarray = np.full((rows, 31), np.nan)  # Tmin u from fixed resistor T dependance
        solraderrarray = np.full((rows, 31), np.nan)  # solar radiation uncertainty
        snerrarray = np.full((rows, 31), np.nan)  # snow presence uncertainty of T
        precerrarray = np.full((rows, 31), np.nan)  # precipitation uncertainty of T
        resterrarray = np.full((rows, 31), np.nan)  # fixed resistor T dependance uncertainty of T

        arraystart = 12 * (int(date.fromordinal(dateday[arraystartday] - 366).strftime('%Y'))) + (
            int(date.fromordinal(dateday[arraystartday] - 366).strftime('%m')))

        for i in range(arraystartday, len(dateday)):

            # assigns an array position based on date
            x = 12 * int(date.fromordinal(dateday[i] - 366).strftime('%Y')) + int(
                date.fromordinal(dateday[i] - 366).strftime('%m')) - arraystart
            # assigns an array position based on time
            y = int(date.fromordinal(dateday[i] - 366).strftime('%d')) - 1

            # try-fail approach for dynamic array dimensions memory reallocation
            try:
                dummy = solraderrarray[x, :]
            except IndexError as ex:
                solraderrarray = np.vstack([solraderrarray, np.full((1, solraderrarray.shape[1]), np.nan)])
                snerrarray = np.vstack([snerrarray, np.full((1, snerrarray.shape[1]), np.nan)])
                precerrarray = np.vstack([precerrarray, np.full((1, precerrarray.shape[1]), np.nan)])
                resterrarray = np.vstack([resterrarray, np.full((1, resterrarray.shape[1]), np.nan)])
                tmaxsrdayarray = np.vstack([tmaxsrdayarray, np.full((1, tmaxsrdayarray.shape[1]), np.nan)])
                tmaxprecdayarray = np.vstack([tmaxprecdayarray, np.full((1, tmaxprecdayarray.shape[1]), np.nan)])
                tmaxrestdayarray = np.vstack([tmaxrestdayarray, np.full((1, tmaxrestdayarray.shape[1]), np.nan)])
                tminsrdayarray = np.vstack([tminsrdayarray, np.full((1, tminsrdayarray.shape[1]), np.nan)])
                tminprecdayarray = np.vstack([tminprecdayarray, np.full((1, tminprecdayarray.shape[1]), np.nan)])
                tminrestdayarray = np.vstack([tminrestdayarray, np.full((1, tminrestdayarray.shape[1]), np.nan)])

            finally:

                solraderrarray[x, y] = solraddayu[i, :]
                if sndayu.size == 0:
                    snerrarray[x, y] = 0
                else:
                    snerrarray[x, y] = sndayu[i]
                precerrarray[x, y] = precdayu[i]
                if type(restdayu) is np.float_:
                    resterrarray[x, y] = restdayu
                else:
                    resterrarray[x, y] = restdayu[i]
                tmaxsrdayarray[x, y] = tmaxsrday[:, i]
                if type(tmaxprecday) is np.float_:
                    tmaxprecdayarray[x, y] = tmaxprecday
                else:
                    tmaxprecdayarray[x, y] = tmaxprecday[i]
                tmaxrestdayarray[x, y] = tmaxrestday[:, i]
                tminsrdayarray[x, y] = tminsrday[:, i]
                tminprecdayarray[x, y] = tminprecday[:, i]
                tminrestdayarray[x, y] = tminrestday[:, i]

        dim = len(tout)  # in the source

        precerrarray = precerrarray[0:len(monlengthsout), :]
        solraderrarray = solraderrarray[0:len(monlengthsout), :]
        snerrarray = snerrarray[0:len(monlengthsout), :]
        precerrarray = precerrarray[0:len(monlengthsout), :]
        resterrarray = resterrarray[0:len(monlengthsout), :]
        tmaxsrdayarray = tmaxsrdayarray[0:len(monlengthsout), :]
        tmaxprecdayarray = tmaxprecdayarray[0:len(monlengthsout), :]
        tmaxrestdayarray = tmaxrestdayarray[0:len(monlengthsout), :]
        tminsrdayarray = tminsrdayarray[0:len(monlengthsout), :]
        tminprecdayarray = tminprecdayarray[0:len(monlengthsout), :]
        tminrestdayarray = tminrestdayarray[0:len(monlengthsout), :]

        # random uncertianties of the daily average product are the meaned uncertainties of the sub-hourly product
        # the following workaround (operation segmentation) is used to bypass the shape mismatch and the broadcast problem
        op1 = np.nanmean((precerrarray ** 2), axis=1)
        op1 = op1.reshape((op1.shape[0], 1))
        op2 = np.sqrt(monlengthsout)
        if op2.shape[0] < op1.shape[0]:
            delta = op1.shape[0] - op2.shape[0]
            op2 = np.vstack([op2, np.full((delta, op2.shape[1]), np.nan)])

        precerr = op1 / op2
        # quasi-systematic uncertianties of the daily average product are the meaned uncertainties of the sub-hourly product
        resterr = np.nanmean(resterrarray, axis=1)
        # quasi-systematic uncertianties of the daily average product are the meaned uncertainties of the sub-hourly product
        solraderr = np.nanmean(solraderrarray, axis=1)
        # quasi-systematic uncertianties of the daily average product are the meaned uncertainties of the sub-hourly product
        snerr = np.nanmean(snerrarray, axis=1)

        tmaxsru = np.nanmean(tmaxsrdayarray, axis=1)  # quasi systematic uncertainties are meaned
        tmaxrestu = np.nanmean(tmaxrestdayarray, axis=1)  # quasi systematic uncertainties are meaned
        # prec u in tmax goes from systematic to random
        tmaxprecu = np.sqrt(np.nansum(tmaxprecdayarray ** 2, axis=1)) / np.sqrt(
            monlengthsout.reshape(monlengthsout.shape[0], ))

        tminsru = np.nanmean(tminsrdayarray, axis=1)  # quasi systematic uncertainties are meaned
        tminrestu = np.nanmean(tminrestdayarray, axis=1)  # quasi systematic uncertainties are meaned
        # prec u in tmax goes from systematic to random
        tminprecu = np.sqrt(np.nansum(tminprecdayarray ** 2, axis=1)) / np.sqrt(
            monlengthsout.reshape(monlengthsout.shape[0], ))

        if tmaxsysterr.shape[0] < tmaxrestu.shape[0]:
            delta = tmaxrestu.shape[0] - tmaxsysterr.shape[0]
            tmaxsysterr = np.hstack([tmaxsysterr, np.full((delta,), 0)])

        # assigns space for the positive uncertianty of tmax
        tmaxerrpos = np.sqrt(np.power(tmaxsysterr, 2) + np.power(tmaxrestu, 2) + np.power(tmaxsru, 2))

        # assigns space for the negative uncertianty of tmax
        tmaxerrneg = np.sqrt(np.power(tmaxsysterr, 2) + np.power(tmaxrestu, 2) + np.power(tmaxprecu, 2))

        if tminsysterr.shape[0] < tminrestu.shape[0]:
            delta = tminrestu.shape[0] - tminsysterr.shape[0]
            tminsysterr = np.hstack([tminsysterr, np.full((delta,), np.nan)])
        tminerrpos = np.sqrt(
            tminsysterr ** 2 + tminrestu ** 2 + tminsru ** 2)  # assigns space for the positive uncertianty of tmin
        tminerrneg = np.sqrt(
            tminsysterr ** 2 + tminrestu ** 2 + tminprecu ** 2)  # assigns space for the negative uncertianty of tmin

        if tmeansysterr.shape[0] < tmaxrestu.shape[0]:
            delta = tmaxrestu.shape[0] - tmeansysterr.shape[0]
            tmeansysterr = np.hstack([tmeansysterr, np.full((delta,), np.nan)])
        tmeanerrpos = np.sqrt(tmeansysterr ** 2 + ((tmaxrestu + tminrestu) / 2) ** 2 + ((tmaxsru + tminsru) / 2) ** 2)
        tmeanerrneg = np.sqrt(
            tmeansysterr ** 2 + ((tmaxrestu + tminrestu) / 2) ** 2 + (((tmaxprecu + tminprecu) / 2) ** 2) / 2)

    except Exception:
        raise

    return precerr, tmaxerrpos, tmaxerrneg, tminerrpos, tminerrneg, tmeanerrpos, tmeanerrneg, solraderr, snerr, resterr, \
           tout, dateout, arraystartmon, monlengthsout
