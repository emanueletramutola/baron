#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function uses the WBANNO number from the USCRN source files to find the metadata from the metadata tables.

Some very early curves for a few sites have 4 coefficients but this is before 2006, where the earliest sub-hourly data
is available, so the 4th coefficients are unused. There is then a loop for each array. Because information is only
entered in the metadata table when there is a change, the array contains NaN for dates where there has been no change
in that specific coeff/curve/etc. the NaN entries are replaced by the previous relative known entry

The function output parameters are:
 - curveinfo
    type: numpy array
    desc: calibration curves

 - coeffinfo
    type: numpy array
    desc: calibration coeffs
"""

import numpy as np
import psycopg2
from datetime import datetime, date

def coeffdb(conn, wbanno):

    cur = conn.cursor()
    cur.execute("SELECT statid FROM uscrn.wbanno WHERE wbanno = %s", (wbanno,))

    try:
        statid = cur.fetchone()[0] # raise an exception on empty resultset subscription

        # finds the calibration curves and datalogger from the station id
        # and checks for NaN in the calib. curve table, which indicates that it has not changed from the previous date
        # replaces NaN with values from previous date
        cur.execute("SELECT date_change, curve1, curve2, curve3, datalogger FROM uscrn.curves WHERE statid = %s "
                    "ORDER BY date_change ASC", (statid,))

        dimrows = cur.rowcount
        curves = [list(row) for row in list(zip(*(cur.fetchall())))]
        dimfields = len(curves)

        # date for which the change in curve or datalogger occured
        curves[0] = [(date.toordinal(datetime.strptime(str(x), '%Y%m%d')) + 366) for x in curves[0]]
        # datalogger type, can be CR23X or CR3000, therefore determined by the length of string

        for i in range(1, dimfields):
            for j in range(dimrows-1, -1, -1):
                if not curves[i][j]:
                    tmp = j-1
                    while not curves[i][tmp]:
                        tmp -= 1
                    curves[i][j] = len(curves[i][tmp])
                else:
                    curves[i][j] = len(curves[i][j])

        conn.commit()
        # finds the calibration coefficients
        # and checks for NaN in the calib. coeff. table, which indicates it has not changed from the previous date
        # replaces NaN with values from previous date
        cur.execute("SELECT date_change, coeff1, coeff2, coeff3, coeff4, coeff5, coeff6, coeff7, coeff8, coeff9, "
                    "coeff10, coeff11, coeff12 FROM uscrn.coeffs WHERE statid = %s "
                    "ORDER BY date_change ASC", (statid,))

        dimrows = cur.rowcount
        coeffs = [list(row) for row in list(zip(*(cur.fetchall())))]
        dimfields = len(coeffs)

        # date on which change in cal. coeffs. occured
        coeffs[0] = [(date.toordinal(datetime.strptime(str(x), '%Y%m%d')) + 366) for x in coeffs[0]]
        # 4th calibration coeff is only in some sites for data before sub-hourly data is available, it is not
        # implemented in this data processing
        for i in range(1, dimfields):
            for j in range(dimrows - 1, -1, -1):
                if not coeffs[i][j]:
                    tmp = j - 1
                    while not coeffs[i][tmp] and tmp > 0:
                        tmp -= 1
                    coeffs[i][j] = coeffs[i][tmp]

        conn.commit()

        return np.array(list(zip(*curves))), np.array(list(zip(*coeffs)))

    except TypeError:
        # if the wbanno is not present in the metadata table, also there isn't the statid
        # this mean that the current site cannot be processed (ex. experimental site)
        #print('\x1b[0;37m' + 'Unable to process this site: WBANNO {} not in the list'.format(wbanno) + '\x1b[0m')
        raise
