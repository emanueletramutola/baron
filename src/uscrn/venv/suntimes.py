#!python
# 2018 - Alessandro Di Filippo - CNR IMAA
"""
This function is just a series of calculations which takes longitude, latitude, timezone and date to find the sunrise
and sunset times. This is based on calculation in a spreadsheet made by NOAA found at:
https://www.esrl.noaa.gov/gmd/grad/solcalc/calcdetails.html
"""
import math
from datetime import datetime, date


def suntimes(lon, lat, zone, datein):

    julianday = (((date.toordinal(datetime.strptime(str(datein), '%Y%m%d')) + 366) -
                  (date.toordinal(datetime.strptime('18991231', '%Y%m%d')) + 366)) + 2415018.5 - zone) / 24

    juliancent = (julianday - 2451545) / 36525

    gmls = (280.46646 + juliancent * (36000.76983 + juliancent * 0.0003032)) % 360

    gmas = 357.52911 + juliancent * (35999.05029-0.0001537 * juliancent)

    ecc = 0.016708634 - juliancent * (0.000042037 + 0.0000001267 * juliancent)

    eqsun = math.sin(math.radians(gmas)) * (1.914602 - juliancent * (0.004817 + 0.000014 * juliancent)) \
            + math.sin(math.radians(2 * gmas) * 0.019993-0.000101 * juliancent) + math.sin(math.radians(3 * gmas)) * 0.000289

    lonsuntrue = gmls + eqsun

    lonsunapp = lonsuntrue - 0.00569 - 0.00478 * math.sin(math.radians(125.04 - 1954.136 * juliancent))

    ecl = 23 + (26 + (21.448 * juliancent * (46.815 * juliancent * (0.0059 - juliancent * 0.001813))) / 60) / 60

    oc = ecl + 0.00256 * math.cos(math.radians(125.04 - 1934.16 * juliancent))

    dec = math.degrees(math.asin(math.sin(math.radians(oc)) * math.sin(math.radians(lonsunapp))))

    vary = (math.tan(math.radians(oc) / 2)) ** 2

    eot = 4 * math.degrees(vary * math.sin(2 * math.radians(gmls)) - 2 * ecc * math.sin(math.radians(gmas)) +
          4 * ecc * vary * math.sin(math.radians(gmas)) * math.cos(2 * math.radians(gmls)) -
          0.5 * math.sin(4 * math.radians(gmls)) * vary ** 2 - 1.25 * math.sin(2 * math.radians(gmas)) * ecc ** 2)

    HAS = math.degrees(math.acos(math.cos(math.radians(90.833)) / (math.cos(math.radians(lat)) * math.cos(math.radians(dec)))
                     - math.tan(math.radians(lat)) * math.tan(math.radians(dec))))

    solarnoon = (720 - 4 * lon - eot + zone * 60) / 1440

    sunrise = solarnoon - HAS * 4 / 1440

    sunset = solarnoon + HAS * 4 / 1440

    sunrise = sunrise * 24

    sunset = sunset * 24

    return sunrise, sunset
