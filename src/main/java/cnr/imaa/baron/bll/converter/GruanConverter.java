package cnr.imaa.baron.bll.converter;

import cnr.imaa.baron.model.GruanDataHeader;
import cnr.imaa.baron.model.GruanDataValue;
import cnr.imaa.baron.model.Station;
import com.google.common.primitives.Floats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.ma2.Array;
import ucar.nc2.Group;
import ucar.nc2.NetcdfFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

public class GruanConverter implements Converter {
    private static final Logger log = LoggerFactory.getLogger(GruanConverter.class);

    List<Float> altList;
    List<Float> ascList = null;
    List<Float> cor_rhList = null;
    List<Float> cor_tempList = null;
    List<Float> fpList = null;
    List<Float> geopotList = null;
    List<Float> latList = null;
    List<Float> lonList = null;
    List<Float> pressList = null;
    List<Float> res_rhList = null;
    List<Float> rhList = null;
    List<Float> swradList = null;
    List<Float> tempList = null;
    List<Float> timeList = null;
    List<Float> uList = null;
    List<Float> u_altList = null;
    List<Float> u_cor_rhList = null;
    List<Float> u_cor_tempList = null;
    List<Float> u_pressList = null;
    List<Float> u_rhList = null;
    List<Float> u_std_rhList = null;
    List<Float> u_std_tempList = null;
    List<Float> u_swradList = null;
    List<Float> u_tempList = null;
    List<Float> u_wdirList = null;
    List<Float> u_wspeedList = null;
    List<Float> vList = null;
    List<Float> wdirList = null;
    List<Float> wspeedList = null;
    List<Float> wvmrList = null;

    @Override
    public Object convert(Object input) {
        GruanDataHeader gruanDataHeader;

        try {
            List<GruanDataValue> gruanDataValueList;

            NetcdfFile ncfile = (NetcdfFile) input;
            final Group rootGroup = ncfile.getRootGroup();

            gruanDataHeader = buildGruanDataHeader(rootGroup);
            gruanDataValueList = buildGruanDataValues(rootGroup, gruanDataHeader);

            ncfile.close();

            gruanDataHeader.setData(new HashSet<>(gruanDataValueList));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return gruanDataHeader;
    }

    private GruanDataHeader buildGruanDataHeader(Group rootGroup) {
        GruanDataHeader gruanDataHeader = new GruanDataHeader();

        rootGroup.getAttributes()
                .parallelStream()
                .forEach(attribute -> {
                    switch (attribute.getShortName().toLowerCase()) {
                        case "comment":
                            gruanDataHeader.setComment(attribute.getStringValue());
                            break;
                        case "conventions":
                            gruanDataHeader.setConventions(attribute.getStringValue());
                            break;
                        case "g.ascent.balloonnumber":
                            gruanDataHeader.setG_ascent_balloon_number(attribute.getStringValue());
                            break;
                        case "g.ascent.balloontype":
                            gruanDataHeader.setG_ascent_balloon_type(attribute.getStringValue());
                            break;
                        case "g.ascent.burstpointaltitude":
                            gruanDataHeader.setG_ascent_burstpoint_altitude(attribute.getStringValue());
                            break;
                        case "g.ascent.burstpointpressure":
                            gruanDataHeader.setG_ascent_burstpoint_pressure(attribute.getStringValue());
                            break;
                        case "g.ascent.fillingweight":
                            gruanDataHeader.setG_ascent_filling_weight(attribute.getStringValue());
                            break;
                        case "g.ascent.grossweight":
                            gruanDataHeader.setG_ascent_gross_weight(attribute.getStringValue());
                            break;
                        case "g.ascent.id":
                            gruanDataHeader.setG_ascent_id(attribute.getStringValue());
                            break;
                        case "g.ascent.includedescent":
                            gruanDataHeader.setG_ascent_include_descent(attribute.getStringValue());
                            break;
                        case "g.ascent.payload":
                            gruanDataHeader.setG_ascent_payload(attribute.getStringValue());
                            break;
                        case "g.ascent.precipitablewatercolumn":
                            gruanDataHeader.setG_ascent_precipitable_water_column(attribute.getStringValue());
                            break;
                        case "g.ascent.precipitablewatercolumnu":
                            gruanDataHeader.setG_ascent_precipitable_water_columnu(attribute.getStringValue());
                            break;
                        case "g.ascent.standardtime":
                            gruanDataHeader.setG_ascent_standard_time(attribute.getStringValue());
                            break;
                        case "g.ascent.starttime":
                            gruanDataHeader.setG_ascent_start_time(attribute.getStringValue());
                            break;
                        case "g.ascent.tropopauseheight":
                            gruanDataHeader.setG_ascent_tropopause_height(attribute.getStringValue());
                            break;
                        case "g.ascent.tropopausepottemperature":
                            gruanDataHeader.setG_ascent_tropopause_pot_temperature(attribute.getStringValue());
                            break;
                        case "g.ascent.tropopausepressure":
                            gruanDataHeader.setG_ascent_tropopause_pressure(attribute.getStringValue());
                            break;
                        case "g.ascent.tropopausetemperature":
                            gruanDataHeader.setG_ascent_tropopause_temperature(attribute.getStringValue());
                            break;
                        case "g.ascent.unwindertype":
                            gruanDataHeader.setG_ascent_unwinder_type(attribute.getStringValue());
                            break;
                        case "g.general.filetypeversion":
                            gruanDataHeader.setG_general_file_type_version(attribute.getStringValue());
                            break;
                        case "g.general.sitecode":
                            Station station = new Station();
                            station.setIdStation(attribute.getStringValue());

                            gruanDataHeader.setStation(station);
                            break;
                        case "g.general.siteinstitution":
                            gruanDataHeader.setG_general_site_institution(attribute.getStringValue());
                            break;
                        case "g.general.sitename":
                            gruanDataHeader.setG_general_site_name(attribute.getStringValue());
                            break;
                        case "g.general.sitewmoid":
                            gruanDataHeader.setG_general_site_wmo_id(attribute.getStringValue());
                            break;
                        case "g.general.timestamp":
                            gruanDataHeader.setG_general_timestamp(attribute.getStringValue());
                            break;
                        case "g.instrument.comment":
                            gruanDataHeader.setG_instrument_comment(attribute.getStringValue());
                            break;
                        case "g.instrument.manufacturer":
                            gruanDataHeader.setG_instrument_manufacturer(attribute.getStringValue());
                            break;
                        case "g.instrument.serialnumber":
                            gruanDataHeader.setG_instrument_serial_number(attribute.getStringValue());
                            break;
                        case "g.instrument.softwareversion":
                            gruanDataHeader.setG_instrument_software_version(attribute.getStringValue());
                            break;
                        case "g.instrument.telemetrysonde":
                            gruanDataHeader.setG_instrument_telemetry_sonde(attribute.getStringValue());
                            break;
                        case "g.instrument.type":
                            gruanDataHeader.setG_instrument_type(attribute.getStringValue());
                            break;
                        case "g.instrument.typefamily":
                            gruanDataHeader.setG_instrument_type_family(attribute.getStringValue());
                            break;
                        case "g.instrument.weight":
                            gruanDataHeader.setG_instrument_weight(attribute.getStringValue());
                            break;
                        case "g.measuringsystem.altitude":
                            gruanDataHeader.setG_measuring_system_altitude(new Float(attribute.getStringValue().replace("m", "").trim()));
                            break;
                        case "g.measuringsystem.id":
                            gruanDataHeader.setG_measuring_system_id(attribute.getStringValue());
                            break;
                        case "g.measuringsystem.latitude":
                            gruanDataHeader.setG_measuring_system_latitude(new Float(attribute.getStringValue().replace("°", "").trim()));
                            break;
                        case "g.measuringsystem.longitude":
                            gruanDataHeader.setG_measuring_system_longitude(new Float(attribute.getStringValue().replace("°", "").trim()));
                            break;
                        case "g.measuringsystem.type":
                            gruanDataHeader.setG_measuring_system_type(attribute.getStringValue());
                            break;
                        case "g.product.code":
                            gruanDataHeader.setG_product_code(attribute.getStringValue());
                            break;
                        case "g.product.history":
                            gruanDataHeader.setG_product_history(attribute.getStringValue());
                            break;
                        case "g.product.id":
                            gruanDataHeader.setG_product_id(attribute.getNumericValue().longValue());
                            break;
                        case "g.product.level":
                            gruanDataHeader.setG_product_level(attribute.getStringValue());
                            break;
                        case "g.product.leveldescription":
                            gruanDataHeader.setG_product_level_description(attribute.getStringValue());
                            break;
                        case "g.product.name":
                            gruanDataHeader.setG_product_name(attribute.getStringValue());
                            break;
                        case "g.product.orgresolution":
                            gruanDataHeader.setG_product_org_resolution(attribute.getStringValue());
                            break;
                        case "g.product.processingcode":
                            gruanDataHeader.setG_product_processing_code(attribute.getStringValue());
                            break;
                        case "g.product.producer":
                            gruanDataHeader.setG_product_producer(attribute.getStringValue());
                            break;
                        case "g.product.references":
                            gruanDataHeader.setG_product_references(attribute.getStringValue());
                            break;
                        case "g.product.status":
                            gruanDataHeader.setG_product_status(attribute.getStringValue());
                            break;
                        case "g.product.statusdescription":
                            gruanDataHeader.setG_product_status_description(attribute.getStringValue());
                            break;
                        case "g.product.version":
                            gruanDataHeader.setG_product_version(attribute.getStringValue());
                            break;
                        case "g.surfaceobs.pressure":
                            gruanDataHeader.setG_surface_obs_pressure(attribute.getStringValue());
                            break;
                        case "g.surfaceobs.relativehumidity":
                            gruanDataHeader.setG_surface_obs_relative_humidity(attribute.getStringValue());
                            break;
                        case "g.surfaceobs.temperature":
                            gruanDataHeader.setG_surface_obs_temperature(attribute.getStringValue());
                            break;
                        case "history":
                            gruanDataHeader.setHistory(attribute.getStringValue());
                            break;
                        case "institution":
                            gruanDataHeader.setInstitution(attribute.getStringValue());
                            break;
                        case "references":
                            gruanDataHeader.setReferences_(attribute.getStringValue());
                            break;
                        case "source":
                            gruanDataHeader.setSource(attribute.getStringValue());
                            break;
                        case "title":
                            gruanDataHeader.setTitle(attribute.getStringValue());
                            break;
                    }
                });

        gruanDataHeader.setDateOfObservation(Instant.parse(gruanDataHeader.getG_ascent_standard_time() + "Z"));

        return gruanDataHeader;
    }

    private List<GruanDataValue> buildGruanDataValues(Group rootGroup, GruanDataHeader gruanDataHeader) {
        List<GruanDataValue> gruanDataValueList = Collections.synchronizedList(new ArrayList<>());

        rootGroup.getVariables()
                .parallelStream()
                .forEach(variable -> {
                    try {
                        Array data = variable.read();

                        switch (variable.getShortName().toLowerCase()) {
                            case "alt":
                                altList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "asc":
                                ascList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "cor_rh":
                                cor_rhList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "cor_temp":
                                cor_tempList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "fp":
                                fpList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "geopot":
                                geopotList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "lat":
                                latList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "lon":
                                lonList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "press":
                                pressList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "res_rh":
                                res_rhList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "rh":
                                rhList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "swrad":
                                swradList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "temp":
                                tempList = Floats.asList(((float[]) data.copyTo1DJavaArray()));
                                break;
                            case "time":
                                timeList = Floats.asList(((float[]) data.copyTo1DJavaArray()));
                                break;
                            case "u":
                                uList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "u_alt":
                                u_altList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "u_cor_rh":
                                u_cor_rhList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "u_cor_temp":
                                u_cor_tempList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "u_press":
                                u_pressList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "u_rh":
                                u_rhList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "u_std_rh":
                                u_std_rhList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "u_std_temp":
                                u_std_tempList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "u_swrad":
                                u_swradList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "u_temp":
                                u_tempList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "u_wdir":
                                u_wdirList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "u_wspeed":
                                u_wspeedList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "v":
                                vList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "wdir":
                                wdirList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "wspeed":
                                wspeedList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                            case "wvmr":
                                wvmrList = Floats.asList((float[]) data.copyTo1DJavaArray());
                                break;
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });

        IntStream.range(0, timeList.size())
                .parallel()
                .forEach(i -> {
                    GruanDataValue gruanDataValue = new GruanDataValue();

                    if (altList != null && !altList.get(i).isNaN()) gruanDataValue.setAlt(altList.get(i));
                    if (ascList != null && !ascList.get(i).isNaN()) gruanDataValue.setAsc_(ascList.get(i));
                    if (cor_rhList != null && !cor_rhList.get(i).isNaN())
                        gruanDataValue.setCor_rh(cor_rhList.get(i) * 100);
                    if (cor_tempList != null && !cor_tempList.get(i).isNaN())
                        gruanDataValue.setCor_temp(cor_tempList.get(i));
                    if (fpList != null && !fpList.get(i).isNaN()) gruanDataValue.setFp(fpList.get(i));
                    if (geopotList != null && !geopotList.get(i).isNaN()) gruanDataValue.setGeopot(geopotList.get(i));
                    if (latList != null && !latList.get(i).isNaN()) gruanDataValue.setLat(latList.get(i));
                    if (lonList != null && !lonList.get(i).isNaN()) gruanDataValue.setLon(lonList.get(i));
                    if (pressList != null && !pressList.get(i).isNaN()) gruanDataValue.setPress(pressList.get(i) * 100);
                    if (res_rhList != null && !res_rhList.get(i).isNaN()) gruanDataValue.setRes_rh(res_rhList.get(i));
                    if (rhList != null && !rhList.get(i).isNaN()) gruanDataValue.setRh(rhList.get(i) * 100);
                    if (swradList != null && !swradList.get(i).isNaN()) gruanDataValue.setSwrad(swradList.get(i));
                    if (tempList != null && !tempList.get(i).isNaN()) gruanDataValue.setTemp(tempList.get(i));
                    if (timeList != null && !timeList.get(i).isNaN()) gruanDataValue.setTime(timeList.get(i));
                    if (uList != null && !uList.get(i).isNaN()) gruanDataValue.setU(uList.get(i));
                    if (u_altList != null && !u_altList.get(i).isNaN()) gruanDataValue.setU_alt(u_altList.get(i));
                    if (u_cor_rhList != null && !u_cor_rhList.get(i).isNaN())
                        gruanDataValue.setU_cor_rh(u_cor_rhList.get(i) * 100);
                    if (u_cor_tempList != null && !u_cor_tempList.get(i).isNaN())
                        gruanDataValue.setU_cor_temp(u_cor_tempList.get(i));
                    if (u_pressList != null && !u_pressList.get(i).isNaN())
                        gruanDataValue.setU_press(u_pressList.get(i) * 100);
                    if (u_rhList != null && !u_rhList.get(i).isNaN()) gruanDataValue.setU_rh(u_rhList.get(i) * 100);
                    if (u_std_rhList != null && !u_std_rhList.get(i).isNaN())
                        gruanDataValue.setU_std_rh(u_std_rhList.get(i) * 100);
                    if (u_std_tempList != null && !u_std_tempList.get(i).isNaN())
                        gruanDataValue.setU_std_temp(u_std_tempList.get(i));
                    if (u_swradList != null && !u_swradList.get(i).isNaN())
                        gruanDataValue.setU_swrad(u_swradList.get(i));
                    if (u_tempList != null && !u_tempList.get(i).isNaN()) gruanDataValue.setU_temp(u_tempList.get(i));
                    if (u_wdirList != null && !u_wdirList.get(i).isNaN()) gruanDataValue.setU_wdir(u_wdirList.get(i));
                    if (u_wspeedList != null && !u_wspeedList.get(i).isNaN())
                        gruanDataValue.setU_wspeed(u_wspeedList.get(i));
                    if (vList != null && !vList.get(i).isNaN()) gruanDataValue.setV(vList.get(i));
                    if (wdirList != null && !wdirList.get(i).isNaN()) gruanDataValue.setWdir(wdirList.get(i));
                    if (wspeedList != null && !wspeedList.get(i).isNaN()) gruanDataValue.setWspeed(wspeedList.get(i));
                    if (wvmrList != null && !wvmrList.get(i).isNaN()) gruanDataValue.setWvmr(wvmrList.get(i));

                    gruanDataValue.setHeader(gruanDataHeader);
                    gruanDataValue.setDateOfObservation(gruanDataHeader.getDateOfObservation());

                    gruanDataValue.setStation(gruanDataHeader.getStation());

                    gruanDataValueList.add(gruanDataValue);
                });

        return gruanDataValueList;
    }
}
