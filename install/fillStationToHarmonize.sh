#!/bin/sh

#PATHSCRIPT="/home/emanuele/ownCloud/src/baron/src/main/resources/SQL/RHARM"
PATHSCRIPT="/Dati_2TB/owncloud/src/baron/src/main/resources/SQL/RHARM"

psql baron -q -f $PATHSCRIPT/fillStationToHarmonizeTable.sql

psql baron -q -c "alter table station_to_harmonize owner to baron"