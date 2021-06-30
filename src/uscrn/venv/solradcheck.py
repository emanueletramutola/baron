#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function is used to determine if the temperature took place during the day or night in the absence of solar
radiation measurements. It first uses the UTC and local time to find the timezone, then calls on the function suntimes()
which provides the sunrise and sunset times for that date and location.

There are then a series of conditional check that determine if the measurement falls between these times, taking into
account that at high latitudes sunrise/sunset can be on the previous/following day.

If there is constant night or day, then the output of suntimes is complex: for constant night the real parts of the
complex numbers are the same and for constant day the real parts of the complex number are different.

The function input parameters are:
 - lon
    type: float
    desc: longitude value
 - lat
    type: float
    desc: latitude value
 - lsttime
    type: string
    desc: local time value
 - utctime
    type: string
    desc: utc time value
 - localdate
    type: string
    desc: local date value

The function output parameter is:
 - solradcheck
    type: int
    desc: flag on presence/bsence of solar radiation
"""
import numpy as np
from suntimes import suntimes
from datetime import datetime, date


def solradcheck(lon, lat, lsttime, utctime, localdate):

    # calculates timezone base on local and UTC time
    timezone = ( float(lsttime) - float(utctime) ) / 100

    # if local date is different from UTC date timezone from previous calculation will be wrong by 24 hours
    if abs(timezone) > 12:
        timezone = timezone - 24

    # calculates sunrise and sunset times
    (sunrise, sunset) = suntimes(lon, lat, timezone, localdate)

    numtime = (float(lsttime[:2]) + float(lsttime[2:])) / 60

    # checks sunrise and sunset are not complex, complex times indicates constant day/night
    if np.isrealobj(sunrise) and np.isrealobj(sunset):

        # if the sunrise actually occurs the previous day (can happen depending on location within the timezone and with short nights)
        if sunrise < 0:

            (sunrise, sunset) = suntimes(lon, lat, timezone, ((date.toordinal(datetime.strptime(localdate, '%Y%m%d')) + 366) + 1))
            sunrise = sunrise + 24

            if (timezone < sunrise) and (timezone > sunset):
                # 0 for if measurement time does not occur when the sun is up
                solradchk = 0
            else:
                # 1 for if measurement time does occur when the sun is up
                solradchk = 1

        # if sunset occurs the following day (if location on edge of timezone with long days)
        elif sunset > 24:

            (sunrise, sunset) = suntimes(lon, lat, timezone, ((date.toordinal(datetime.strptime(localdate, '%Y%m%d')) + 366) - 1))
            sunset = sunset - 24

            if (timezone < sunrise) and (timezone > sunset):
                solradchk = 0
            else:
                solradchk = 1
        # checks measurement time is between sunrise and sunset
        elif (numtime >= sunrise) and (numtime <= sunset):
            solradchk = 1
        else:
            solradchk = 0
    # if the real parts of sunrise and sunset are the same then that date is constantly dark
    elif not np.isrealobj(sunrise) and (np.real(sunrise) == np.real(sunset)):
        solradchk = 0
    # if the real parts of sunrise and sunset are different then that day is constantly light
    elif not np.isrealobj(sunrise) and (np.real(sunrise) != np.real(sunset)):
        solradchk = 1
    else:
        solradchk = -1

    return solradchk
