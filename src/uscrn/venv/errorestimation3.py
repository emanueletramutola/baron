#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function calculates the datalogger uncertainty for a specific temperature value using the calibration coefficients
and the datalogger id.

First it finds the resistance of the PRT using the calibration coefficients and the quadratic equation. An if statement
determines the datalogger used, then calculates the voltage measured by the datalogger using the equation in the PTU,
different for each datalogger.
Both dataloggers have different uncertainties over different temperature ranges so these are determined by an if statement.

The process is then effectively done in reverse to find the dataloggers effect uncertainty in temperature.
For the CR3000 the measured voltage is then set to 0, this is done so that the fixed resistor uncertainties, which are
not relevant to measurements recorded using the CR3000, are also 0.

The function input parameters are:
 - t
    type: float
    desc: temperature
 - c0
    type: float
    desc:
 - c1
    type: float
    desc:
 - c2
    type: float
    desc:
 - dlid
    type: float
    desc: datalogger id-type (CR23X, CR3000)

The function output parameters are:
 - terr
    type: numpy array
    desc: datalogger uncertainty
 - measuredv
    type: numpy array
    desc: measured V
 - measuredr
    type: numpy array
    desc: measured R
"""
import numpy as np


def errorestimation3(t, c0, c1, c2, dlid):
    fixedr = 10000  # fixed resistor used in CR23X interface in ohms
    fixedv = 1800  # Voltage used in CR23X interface in mV
    fullscalerange = 400  # full scle range used in CR23X +-200mV

    # estimates the resistance across the sensor by working backwards from the cal. equation
    measuredr = (-c1 + np.sqrt(c1 ** 2 - 4 * c2 * (c0 - t))) / (2 * c2)
    measuredv = 0

    # if dl=CR23X
    if dlid == 1:
        # estimates v across sensor, from the resistance across the sensor and reference v and fixed resistor
        measuredv = (fixedv * measuredr) / (measuredr + fixedr)

        if t >= 0 and t <= 40:
            # u for 0<t<40, see CR23X specification sheet
            dlerr = 0.00015
        else:
            # u outside the range 0<t<40, see CR23X specification sheet
            dlerr = 0.0002

        # u in v = u(dl)*full scale range
        verr = dlerr * fullscalerange
        # uses relationship of v and r to find u(r)
        rerr = -fixedr / (1 - (fixedv / (measuredv + verr)))

    # if dl=CR3000
    elif dlid == 2:
        # estimates v measured by the datalogger using the resistance accross the PRT from the calibration equation
        # the excitation current of 167 micro amps and Ohm's law
        measuredv = 167e-6 * measuredr

        if t >= 0 and t <= 40:
            # u for 0<t<40, see CR3000 specification sheet
            dlerr = 0.0002
        elif t < -25 or t > 50:
            # u for outside the range -25<t<50, see CR3000 specification sheet
            dlerr = 0.0003
        else:
            # u within the range -25<t<50 but outside 0<t<40, see CR3000 specification sheet
            dlerr = 0.00025

        # u in v = u(dl)*(measured v + offset)
        verr = dlerr * (measuredv + 1.5 * (6.67e-6) + 1e-6)
        # error in resistance which is used to calculate the error in temperature using the calibration equation
        rerr = (measuredv + verr) / 167e-6
        # sets measured v output to 0, this is so that when it comes to calculating the uncertainty from the fixed
        # resistor this is 0, as no fixed resistor is used with the CR3000 datalogger
        measuredv = 0

    terr = c0 + (c1) * (rerr) + (c2) * (rerr) ** 2 - t

    return terr, measuredv, measuredr
