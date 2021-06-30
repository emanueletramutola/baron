#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function calculates the precipitation error. This error is applied for the first point of a precipitation event
when measured precipitation is greater than 0 and the event tracker is also 0, then decays linearly over 30 minutes
(6 data points in the USCRN file). The tracker is set to 1 at the first point and does not reset to 0 until after 30
minutes with no precipitation. If there is no precipitation value the maximum uncertainty is applied.

The function input parameter is:
 - prec
    type: list
    desc: precipitation values

The function output parameter is:
 - precerr
    type: numpy array
    desc: precipitation error
"""
import numpy as np

def precchk(prec):

    l_prec = len(prec)
    precerr = np.zeros((l_prec, ))

    # n is the number of points after precipitation in which some uncertainty is still applied, 6 points = 30 minutes
    n = 6
    # the uncertainty value on temperature when precipitation is present
    errval = 0.2
    # the minimum precipitation uncertainty that can be applied
    errvalmin = 0.05
    # used to keep track of whether the unceratinty has been applied for an individual precipitation event
    preceventcheck = 0

    for i in range(0, l_prec):
        if l_prec - (i+1) < 6: #TODO: check if this control is suitable only for Matlab environment
            n = l_prec - (i+1)

        if (prec[i] > 0) and (preceventcheck == 0): # checks for precipitation
            precerr[i] = errval # applies maximum uncertainty during precipitation

            for j in range (1, n+1):
                # uncertainty reduces linearly over following 30 minutes
                precerr[i+j] = ((7-j) / 7) * (errval-errvalmin)+errvalmin

            preceventcheck = 1

        # precipitation event is considered over after 30 minutes of no precipitation
        elif (np.nanmax(prec[i:i+n+1]) == 0) and preceventcheck == 1:
            preceventcheck = 0

            for j in range(i, i+n+1):

                if precerr[j] < errvalmin:
                    precerr[j] = errvalmin

        # applies minimum possible precipitation uncertianty throughout precipitation event, as long as there is
        # currently no greater uncertainty that has already been applied to i
        elif precerr[i] < errvalmin and preceventcheck == 1:

            precerr[i] = errvalmin

        if np.isnan(prec[i]) == 1:
            precerr[i] = errval

    return  precerr
