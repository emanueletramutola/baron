#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function implement a series of conditional check that work if the conditions mean it is possible that snow is
present on the ground and reflecting solar radiation. These conditions are detailed in the PTU.

The function input parameters are:
 - surt
    type: list
    desc: surface temperature
 - prec
    type: list
    desc: precipitation
 - sr
    type: list
    desc: solar radiation

The function output parameters are:
 - snchk
    type: int
    desc: is 1 if the uncertianty is present and 0 if not
 - snerr
    type: int
    desc: is 0.4 if the uncertainty is present and 0 if not
"""
import numpy as np

def snowcheck (surt, prec, sr):

    l_surt = len(surt)
    # preallocates space for array containing 1 or 0 depending on if the albedo uncertainty is applied
    snchk = np.zeros((l_surt, 1))
    # uncertainty on t as a result of solar radiation reflected from the surface
    snerr = np.zeros((l_surt, 1))

    errval = 0.4
    snowdepth = 0

    for i in range(0, len(prec)):
        if np.isnan(prec[i]):
            prec[i] = 10

    for i in range(0, l_surt):
        if np.isnan(surt[i]):
            surt[i] = 0

        if surt[i] <= 0:
            snowdepth = snowdepth + 10 * prec[i]
        elif surt[i] > 0:
            snowdepth = 0

        if snowdepth >= 10 and sr[i] > 0:
            snerr[i] = errval
            snchk[i] = 1
        else:
            snerr[i] = 0
            snchk[i] = 0

    return snchk, snerr
