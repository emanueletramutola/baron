#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function takes the input USCRN data and calculates the uncertainty combination.

First it calls the function coeffdb() that finds the relevant metadata information from the related tables.
The first date of the imported USCRN data is used to make sure the correct metadata is used for the first data point.
This metadata contains calibration coefficients and the datalogger id. There are some if statements which are used to
put the calibration coefficients into a uniform format.

Individual datalogger uncertainties are then calculated for each sensor and t value separately using the function
errorestimation3(): the final datalogger uncertainty is based on the mean of these. This function also estimates the
voltage measured by the datalogger and the resistance across the PRT: these values are used in the sensitivity
coefficients of some of the other uncertainties. The PRT accuracy uncertainty is then estimated from the output of this
function as well as the sensitivity coefficients.

The rest of this function is the calculation of the different uncertainty elements and the overall uncertainty
combinations, which are the output of this function.

The function input parameters are:
 - temp
    type: list
    desc: the values of the air temperature
 - date:
    type: list
    desc: list of dates
 - sr
    type: list
    desc: values of solar radiation
 - prec
    type: list
    desc: values of precipitation
 - surtemp
    type: list
    desc: values of surface temperature
 - wbanno
    type: string
    desc: the wbanno code of the station site
 - npoints
    type: int
    desc:

Depending on the scope from which the function is invoked, it has some input-output parameters:
 - solraderr
    type: list (input case)/ numpy array (output case)
    desc: values of solar radiation - input if uscrnt2() is called from uscrnDaily(), output in all other cases
 - snerr
    type: list (input case)/ numpy array (output case)
    desc: uncertainty on t as a result of solar radiation reflected from the surface
 - resterr
    type: list (input case)/ numpy array (output case)
    desc: uncertainty from the fixed resistor temperature dependance
 - precerr
    type: int (input case)/ numpy array (output case)
    desc: estimation of the uncertainty on t as a result of precipitation
"""
import numpy as np

from coeffdb import coeffdb
from errorestimation3 import errorestimation3
from precchk import precchk
from snowcheck import snowcheck


def uscrnt2(conn, temp, date, sr, prec, surtemp, solraderr, snerr, resterr, wbanno, precerr, npoints):
    try:
        # coeffgrid finds the calibration and datalogger information for the station from the metadata tables
        (curve, coeff) = coeffdb(conn, wbanno)

        # following declares variable names and sizes to hold individual sensor
        # uncertainty estimates, prt u estimates and sensitivity coefficient values
        prtacc = np.full((len(temp), 3), np.nan)
        senseco = np.full((len(temp), 3), np.nan)
        terrind = np.full((len(temp), 3), np.nan)

        # ncurve is used to track which datalogger and calibration curves are used for a specific calculation
        ncurve = curve.shape[0] - 1
        # similar to ncurve but for the calibration coefficients
        ncoeff = coeff.shape[0] - 1

        # his loop compares the starting date of the data to the corresponding datalogger and calib.
        # curve dates so that the correct starting values can be chosen
        for i in range(0, (curve.shape[0] - 1)):
            if date[0] < curve[i + 1, 0]:  # checks the date of the 1st measurment against the date of the calib. curves
                ncurve = i
                break  # stops the for loop when the correct starting calibration curve is found

        for i in range(0, (coeff.shape[0] - 1)):
            if date[0] < int(coeff[i + 1, 0]):
                ncoeff = i
                break

        # starts loop going through T values from imported data
        for i in range(0, len(temp)):
            if ncurve < curve.shape[0] - 1:  # checks ncurve is not on its last possible value
                # checks that the current ncurve is correct for the date of the T value
                if date[i] >= curve[ncurve + 1, 0]:
                    ncurve += 1

            if ncoeff < coeff.shape[0] - 1:  # checks ncoeff is not on its last possible value
                # checks that the current ncoeff is correct for the date of the T value
                if date[i] >= int(coeff[ncoeff + 1, 0]):
                    ncoeff += 1

            dlid = curve[ncurve, 4] - 4  # datalogger id from metadata tables, 1=CR23X, 2=CR3000

            for j in range(0, 3):
                # finds relevant data for c0, c1, c2 from metadata
                c0 = coeff[ncoeff, (2 + (j * 4)) - 1]
                c1 = coeff[ncoeff, (3 + (j * 4)) - 1]
                c2 = coeff[ncoeff, (4 + (j * 4)) - 1]

                if type(c0) is np.str_ or type(c0) is str:
                    c0 = float(c0.replace(' ', ''))

                if type(c1) is np.str_ or type(c1) is str:
                    c1 = float(c1.replace(' ', ''))

                if type(c2) is np.str_ or type(c2) is str:
                    c2 = float(c2.replace(' ', ''))

                # the coefficients are sometimes reported in the tables with
                # different formats, the following if statements are to put the
                # coefficients into the correct format for the calculations

                if c1 > 1:
                    if c1 < 1000:
                        c1 = c1 / 1e3
                    else:
                        c1 = c1 / 1e4

                if c2 > 1:
                    if c2 < 1000:
                        c2 = c2 / 1e6
                    else:
                        c2 = c2 / 1e8

                # error estimation finds datalogger uncertainty for sensor reading value T and also outputs the V and R
                # of the system
                (tind, vol, res) = errorestimation3(temp[i], c0, c1, c2, dlid)
                terrind[i, j] = tind

                # calculates uncertainty of the PRT from calculated R and calibration coefficients
                prtacc[i, j] = c1 * ((1 + 0.0004) * res) + c2 * ((1 + 0.0004) * res) ** 2 - (c1 * res + c2 * res ** 2)

                try:
                    senseco[i, j] = (2 * c2 * res + c1) / ((1800 / vol) - 1)
                except ZeroDivisionError:
                    senseco[i, j] = 0

        # snchk is a 1 or 0 value for the expected presence of snow, snerr is the uncertainty of the temperature
        # reading as a result of snchk

        # snowchek not present in daily computation
        if surtemp and prec and sr:
            (snchk, snerr) = snowcheck(surtemp, prec, sr)
        else:
            snchk = np.empty((0, 0))
            snerr = np.empty((0, 0))

        senseco = np.nanmean(senseco, 1)
        dlerr = np.nanmean(terrind, 1)
        prtacc = np.nanmean(prtacc, 1) / np.sqrt(npoints)  # PRT uncertainty
        prtcal = 0.68 * 0.03  # PRT calibration uncertainty
        prterr = np.sqrt(prtcal ** 2 + prtacc ** 2)  # total PRT contribution to uncertainty

        # uncertainty from local effects and system design, currently only an approximation of solar radiation error
        # solraderr not present in daily computation
        if (type(solraderr) is not np.ndarray) and (type(solraderr) is not np.float_):
            if sr:
                solraderr = 0.05 * (np.asarray(sr) / 1600)
            elif not sr and not solraderr:
                solraderr = np.zeros((len(temp), 1))
            elif not sr and not prec:
                solraderr = np.zeros((len(temp), 1))

        elif type(solraderr) is np.ndarray:
            if sr:
                solraderr = 0.05 * (np.asarray(sr) / 1600)
            elif not sr and not solraderr.all():
                solraderr = np.zeros((len(temp), 1))
            elif not sr and not prec:
                solraderr = np.zeros((len(temp), 1))

        elif type(solraderr) is np.float_:
            value = solraderr
            solraderr = np.full((len(temp), 1), value)

        # estimation of the uncertainty on t as a result of precipitation
        if type(precerr) is int and precerr == 1:
            precerr = precchk(prec)
        elif type(precerr) is int and precerr == 0:
            precerr = np.zeros((len(prec),))

        leaderr = 0.68 * 0.015  # uncertainty from the lead resistance
        restol = 0.68 * senseco * 1  # uncertainty from the fixed resistor tolerance

        # uncertainty from the fixed resistor temperature dependance
        if type(resterr) is list and len(resterr) == 0:
            resterr = 0.68 * senseco * (25 - np.asarray(temp)) * 0.02

        if resterr is not np.float_ and isinstance(resterr, np.ndarray):
            if resterr.shape[0] < restol.shape[0]:
                delta = restol.shape[0] - resterr.shape[0]
                resterr = np.hstack([resterr, np.full((delta,), 0)])

        # total uncertainty contribution of the sensor/datalogger interface
        interr = np.sqrt(leaderr ** 2 + restol ** 2 + resterr ** 2)

        # operand_pos and operand_neg are built to avoid memory error using numpy array stacking and subsequently
        # the numpy inplace sum of elements axis wide
        if type(npoints) is not int:
            if snerr.shape[0] == 0:
                operand_pos = np.vstack([(prterr ** 2),
                                         (interr ** 2).reshape((1, interr.shape[0])),
                                         (dlerr ** 2).reshape((1, dlerr.shape[0])),
                                         (solraderr ** 2).reshape((1, solraderr.shape[0]))])
            else:
                operand_pos = np.vstack([(prterr ** 2),
                                         (interr ** 2).reshape((1, interr.shape[0])),
                                         (dlerr ** 2).reshape((1, dlerr.shape[0])),
                                         (solraderr ** 2).reshape((1, solraderr.shape[0])),
                                         (snerr ** 2).reshape((1, snerr.shape[0]))])
        else:
            if snerr.shape[0] == 0:
                operand_pos = np.vstack([(prterr ** 2).reshape((1, prterr.shape[0])),
                                         (interr ** 2).reshape((1, interr.shape[0])),
                                         (dlerr ** 2).reshape((1, dlerr.shape[0])),
                                         (solraderr ** 2).reshape((1, solraderr.shape[0]))])
            else:
                operand_pos = np.vstack([(prterr ** 2).reshape((1, prterr.shape[0])),
                                         (interr ** 2).reshape((1, interr.shape[0])),
                                         (dlerr ** 2).reshape((1, dlerr.shape[0])),
                                         (solraderr ** 2).reshape((1, solraderr.shape[0])),
                                         (snerr ** 2).reshape((1, snerr.shape[0]))])

        terrpos = np.sqrt(operand_pos.sum(axis=0))  # combination of positive uncertainties for T

        if type(npoints) is not int:
            if precerr.shape[0] == 0:
                operand_neg = np.vstack([(prterr ** 2),
                                         (interr ** 2).reshape((1, interr.shape[0])),
                                         (dlerr ** 2).reshape((1, dlerr.shape[0]))])
            else:
                operand_neg = np.vstack([(prterr ** 2),
                                         (interr ** 2).reshape((1, interr.shape[0])),
                                         (dlerr ** 2).reshape((1, dlerr.shape[0])),
                                         (precerr ** 2).reshape((1, precerr.shape[0]))])
        else:
            if precerr.shape[0] == 0:
                operand_neg = np.vstack([(prterr ** 2).reshape((1, prterr.shape[0])),
                                         (interr ** 2).reshape((1, interr.shape[0])),
                                         (dlerr ** 2).reshape((1, dlerr.shape[0]))])
            else:

                if precerr.shape[0] < prterr.shape[0]:
                    if precerr.ndim > 1:
                        precerr = precerr.reshape((precerr.shape[0],))
                    delta = prterr.shape[0] - precerr.shape[0]
                    precerr = np.hstack([precerr, np.full((delta,), 0)])

                operand_neg = np.vstack([(prterr ** 2).reshape((1, prterr.shape[0])),
                                         (interr ** 2).reshape((1, interr.shape[0])),
                                         (dlerr ** 2).reshape((1, dlerr.shape[0])),
                                         (precerr ** 2).reshape((1, precerr.shape[0]))])

        terrneg = np.sqrt(operand_neg.sum(axis=0))  # combination of negative uncertainties for T

        return terrpos, terrneg, snchk, precerr, dlerr, prtacc, prtcal, solraderr, restol, resterr, leaderr, snerr

    except Exception as ex:
        raise
