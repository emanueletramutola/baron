#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function uses uscrnSubhourly() to calculate parts of the hourly uncertainties. After running the subhourly function
it uses a loop to find the point where data exists for both hourly and sub-hourly. The data from the sub hourly is then
assigned to arrays which are where the rows are date+hour and the columns are each five minute section of that hour.
If the minute time is 00 that measurement gets grouped with the previous hour because the time given by USCRN is the end
of the 5 minute slot.

After the different data has been sorted into the different arrays, it is made sure that they do not run longer than the
hourly data. The uncertainty contribution of precipitation to the hourly average is calculated as the mean of the
different 5 minute precipitation uncertainties of that hour. The Tmax and Tmin uncertainties are calculated assuming the
worst-case solar radiation, snow albedo and precipitation uncertainty for that hour because it is not known what time
during the hour the maximum or minimum were recorded. There is an exception to this were sometimes the lowest or highest
5 minute average is used instead and here U(Tmax) or U(Tmin) is replaced with the uncertainty for that 5 minute period.

Tcalc is the average temperature of the last 5 min of the hour and its uncertainty is the same as from uscrnSubhourly().
"""
import decimal as dec

import numpy as np

from uscrnSubhourly import uscrnSubhourly


def errfromsubhourly(conn, wbanno, dateconcat, timeconcat, tconcat, tmaxconcat, tminconcat, tmaxsysterr, tminsysterr):
    try:
        (t5min, t5minerrpos, t5minerrneg, date5min, time5min, srconcat, precconcat, surtconcat, snchk,
         precerr5min, dlerr, prtacc, prtcal, solraderr, restol, resterr, leaderr, snerr, localdateconcat,
         localtimeconcat) = uscrnSubhourly(conn, wbanno)

        for i in range(0, len(date5min)):

            if (date5min[i] == dateconcat[0]) and (time5min[i] == timeconcat[0]):
                arraystart = i  # arraystart is used to ignore the times for which there is sub-hourly data but no hourly data
                break

        rows = int(dec.Decimal((len(date5min) - arraystart) / 12).to_integral_exact('ROUND_UP'))
        t5minarray = np.full((rows, 12), np.nan)  # allocates space for array of sub-hourly T data sorted into hours
        t5minerrposarray = np.full((rows, 12),
                                   np.nan)  # allocates space for array of sub-hourly T positive uncertainty data sorted into hours
        t5minerrnegarray = np.full((rows, 12),
                                   np.nan)  # allocates space for array of sub-hourly T negative uncertainty data sorted into hours
        solraderrarray = np.full((rows, 12),
                                 np.nan)  # allocates space for array of sub-hourly SR uncertainty data sorted into hours
        snerrarray = np.full((rows, 12),
                             np.nan)  # allocates space for array of sub-hourly snow uncertainty data sorted into hours
        precerrarray = np.full((rows, 12),
                               np.nan)  # allocates space for array of sub-hourly precipitation uncertainty data sorted into hours

        # using the Decimal to_integral_exact workaround for align Python rounding behavior to the Matlab one (ex. round(0.5) = 1)
        # finds how many time steps in the sub-hourly data are ignored
        x1 = (date5min[arraystart] - date5min[0]) * 24 + int(
            (dec.Decimal(int(time5min[arraystart]) / 100 + 0.9)).to_integral_exact('ROUND_HALF_UP'))

        for i in range(arraystart, len(date5min)):  # adding a +1 as offset because python range [...) interval

            # assigns an array position based on date and hour
            x = (date5min[i] - date5min[0]) * 24 + int(
                (dec.Decimal(int(time5min[i]) / 100 + 0.9)).to_integral_exact('ROUND_HALF_UP'))
            y = np.mod(int(time5min[i]), 100) // 5  # assigns an array position based on minutes past hour

            if y == 0:
                x = x - 1
                y = 11

            try:
                t5minarray[x - x1, y] = t5min[i]  # assigns tempertaure data to position in array
            except IndexError:
                pass
            try:
                t5minerrposarray[x - x1, y] = t5minerrpos[i]  # assigns positive uncertainty data to position in array
            except IndexError:
                pass
            try:
                t5minerrnegarray[x - x1, y] = t5minerrneg[i]  # assigns negative uncertainty data to position in array
            except IndexError:
                pass
            try:
                solraderrarray[x - x1, y] = solraderr[i]  # assigns SR uncertianty data to position in array
            except IndexError:
                pass
            try:
                snerrarray[x - x1, y] = snerr[i]  # assigns snow presence uncertainty data to postion in array
            except IndexError:
                pass
            try:
                precerrarray[x - x1, y] = precerr5min[i]  # assigns prec uncertainty data to position in array
            except IndexError:
                pass

        dim = len(tconcat)

        precerrarray = precerrarray[0:dim]
        t5minarray = t5minarray[0:dim]
        t5minerrposarray = t5minerrposarray[0:dim]
        t5minerrnegarray = t5minerrnegarray[0:dim]
        solraderrarray = solraderrarray[0:dim]
        snerrarray = snerrarray[0:dim]

        dim2 = len(precerrarray)

        precerr = np.nanmean(precerrarray, axis=1)

        tmaxerrpos = np.full((1, np.fmin(dim2, dim)), np.nan)
        tmaxerrneg = np.full((1, np.fmin(dim2, dim)), np.nan)
        tminerrpos = np.full((1, np.fmin(dim2, dim)), np.nan)
        tminerrneg = np.full((1, np.fmin(dim2, dim)), np.nan)
        tminsrerr = np.full((1, np.fmin(dim2, dim)), np.nan)
        tmaxsrerr = np.full((1, np.fmin(dim2, dim)), np.nan)
        tminprecerr = np.full((1, np.fmin(dim2, dim)), np.nan)
        tmaxprecerr = np.full((1, np.fmin(dim2, dim)), np.nan)

        for i in range(0, np.fmin(len(t5minerrposarray), dim)):

            # positive uncertainties is the sum in quadrature sytematic uncertainties, maximum SR uncertainty
            # for that hour and the maximum snow presence uncertainty for that hour
            tmaxerrpos[:, i] = np.sqrt(
                tmaxsysterr[i] ** 2 + np.nanmax(solraderrarray[i, :]) ** 2 + np.nanmax(snerrarray[i, :]) ** 2)

            # U from SR parts in Tmax
            tmaxsrerr[:, i] = np.sqrt(np.nanmax(solraderrarray[i, :]) ** 2 + np.nanmax(snerrarray[i, :]) ** 2)

            # negative uncertainty is the sum in quadrature of the systematic uncertainties and precipitation uncertainty
            tmaxerrneg[:, i] = np.sqrt(tminsysterr[i] ** 2 + np.nanmax(precerrarray[i, :]) ** 2)

            # U from prec of Tmax
            tmaxprecerr[:, i] = np.nanmax(precerrarray[i, :])

            # positive uncertainties is the sum in quadrature sytematic uncertainties, maximum SR uncertainty for that hour
            # and the maximum snow presence uncertainty for that hour
            tminerrpos[:, i] = np.sqrt(
                tmaxsysterr[i] ** 2 + np.nanmax(solraderrarray[i, :]) ** 2 + np.nanmax(snerrarray[i, :]) ** 2)

            # U from SR parts in Tmin
            tminsrerr[:, i] = np.sqrt(np.nanmax(solraderrarray[i, :]) ** 2 + np.nanmax(snerrarray[i, :]) ** 2)

            # negative uncertainty is the sum in quadrature of the systematic uncertainties and precipitation uncertainty
            tminerrneg[:, i] = np.sqrt(tminsysterr[i] ** 2 + np.nanmax(precerrarray[i, :]) ** 2)

            # U from prec of Tmin
            tminprecerr[:, i] = np.nanmax(precerrarray[i, :])

            for j in range(0, 12):

                # if Tmin from usual method is greater than one of the average 5min values (see USCRN data ingest document)
                # that 5min value is used instead. the uncertianty of Tmin will in this case be the same as for that 5 min value
                if (t5minarray[i, j] == np.nanmin(t5minarray[i, :])) and (np.nanmin(t5minarray[i, :]) == tminconcat[i]):

                    # positive uncertainty of sub-hourly T value used for Tmin
                    tminerrpos[:, i] = np.sqrt(tminsysterr[i] ** 2 + solraderrarray[i, j] ** 2 + snerrarray[i, j] ** 2)

                    # U from SR parts in Tmin
                    tminsrerr[:, i] = np.sqrt(solraderrarray[i, j] ** 2 + snerrarray[i, j] ** 2)

                    # negative uncertainty of sub-hourly T value used for Tmin
                    tminerrneg[:, i] = np.sqrt(tminsysterr[i] ** 2 + precerrarray[i, j] ** 2)

                    tminprecerr[:, i] = precerrarray[i, j]  # U from prec of Tmin

                # if Tmax from usual method is less than one of the average 5min values (see USCRN data ingest document)
                # that 5min value is used instead. the uncertianty of Tmax will in this case be the same as for that 5 min value
                elif (t5minarray[i, j] == np.amax(t5minarray[i, :])) and np.amax(t5minarray[i, :]) == tmaxconcat[i]:

                    # positive uncertainty of sub-hourly T value used for Tmax
                    tmaxerrpos[:, i] = np.sqrt(
                        tmaxsysterr[:, i] ** 2 + solraderrarray[i, j] ** 2 + snerrarray[i, j] ** 2)

                    tmaxsrerr[:, i] = np.sqrt(
                        solraderrarray[i, j] ** 2 + snerrarray[i, j] ** 2)  # U from SR parts in Tmax

                    # negative uncertainty of sub-hourly T value used for Tmax
                    tmaxerrneg[:, i] = np.sqrt(tmaxsysterr[:, i] ** 2 + precerrarray[i, j] ** 2)

                    tmaxprecerr[:, i] = precerrarray[i, j]  # U from prec of Tmax

        tcalcerrpos = t5minerrposarray[:,
                      11]  # Tcalc is the sub-hourly value of the last 5 minutes of the hour so the uncertainty is the same
        tcalcerrneg = t5minerrnegarray[:,
                      11]  # Tcalc is the sub-hourly value of the last 5 minutes of the hour so the uncertainty is the same

    except Exception:
        raise

    return precerr, tmaxerrpos, tmaxerrneg, tminerrpos, tminerrneg, tcalcerrpos, tcalcerrneg, t5minarray, snerrarray, \
           tmaxsrerr, tminsrerr, tmaxprecerr, tminprecerr
