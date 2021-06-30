package cnr.imaa.baron.bll.harmonizer;

import cnr.imaa.baron.bll.converter.HeaderConverter;
import cnr.imaa.baron.bll.converter.MeasurementConverter;
import cnr.imaa.baron.bll.converter.ProductConverter;
import cnr.imaa.baron.commons.AstronomicUtility;
import cnr.imaa.baron.commons.BaronCommons;
import cnr.imaa.baron.commons.IdlCommonFunctions;
import cnr.imaa.baron.model.*;
import cnr.imaa.baron.repository.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.decimal4j.util.DoubleRounder;
import org.jdom2.internal.ArrayCopy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static cnr.imaa.baron.commons.BaronCommons.*;
import static cnr.imaa.baron.model.HarmonizedData.*;
import static java.util.stream.Collectors.groupingBy;

public class IgraHarmonizer {
    private static final Logger log = LoggerFactory.getLogger(IgraHarmonizer.class);

    private final List<Integer> HOURS_FILTER_LIST = new ArrayList<>(Arrays.asList(0, 11, 12, 23));
    private final Double[] press = {1000.0, 925.0, 850.0, 700.0, 500.0, 400.0, 300.0, 250.0, 200.0, 150.0, 100.0, 70.0, 50.0, 30.0, 20.0, 10.0};

    private StationDAO stationDAO;
    private StationToHarmonizeDAO stationToHarmonizeDAO;
    private GuanDataHeaderDAO guanDataHeaderDAO;
    private GuanDataValueDAO guanDataValueDAO;
    private HarmonizationBreakDAO harmonizationBreakDAO;
    private HeaderDAO headerDAO;

    private MeasurementConverter measurementConverter;
    private HeaderConverter headerConverter;
    private ProductConverter productConverter;

    private Station station;
    private StationToHarmonize stationToHarmonize;
    private CommonStaticDataStructure commonStaticDataStructure;
    private Set<Integer> sondeIdList;
    private List<BaronCommons.ECV> ecvList;
    private List<BaronCommons.ZEN> zenList;

    private List<GuanDataHeader> guanDataHeadersFull;
    private List<GuanDataValue> guanDataValuesFull;
    private List<GuanDataValue> guanDataValuesMandatoryLevels;
    private List<Measurement> measurementFull;
    private List<HarmonizedData> harmonizedDataList;
    private List<Header> headerFull;
    private List<Product> productMandatoryLevels;
    private List<HarmonizationSigmaU_V> harmonizationSigmaUVList = new ArrayList<>();
    private Map<Instant, List<HarmonizedData>> harmonizedDataPerDate;

    private final static int ARR_MAX_SIZE = 60000;

    private Double[][] T_H = new Double[ARR_MAX_SIZE][100];
    private Double[][] ERR_T_H = new Double[ARR_MAX_SIZE][100];
    private Integer[][] data_MONTHG_T_H = new Integer[ARR_MAX_SIZE][100];
    private Integer[][] data_DAYG_T_H = new Integer[ARR_MAX_SIZE][100];
    private Integer[][] data_YEARG_T_H = new Integer[ARR_MAX_SIZE][100];
    private Integer[][] data_HOURG_T_H = new Integer[ARR_MAX_SIZE][100];
    private Double[][] data_TEMPG_T_H = new Double[ARR_MAX_SIZE][100];
    private Double[][] T_SOURCE = new Double[ARR_MAX_SIZE][100];

    private Double[][] RH_H = new Double[ARR_MAX_SIZE][100];
    private Double[][] ERR_RH_H = new Double[ARR_MAX_SIZE][100];
    private Integer[][] data_MONTHG_RH_H = new Integer[ARR_MAX_SIZE][100];
    private Integer[][] data_DAYG_RH_H = new Integer[ARR_MAX_SIZE][100];
    private Integer[][] data_YEARG_RH_H = new Integer[ARR_MAX_SIZE][100];
    private Integer[][] data_HOURG_RH_H = new Integer[ARR_MAX_SIZE][100];
    private Double[][] data_TEMPG_RH_H = new Double[ARR_MAX_SIZE][100];
    private Double[][] RH_SOURCE = new Double[ARR_MAX_SIZE][100];

    private Double[][] U_H = new Double[ARR_MAX_SIZE][100];
    private Double[][] ERR_U_H = new Double[ARR_MAX_SIZE][100];
    private Integer[][] data_MONTHG_U_H = new Integer[ARR_MAX_SIZE][100];
    private Integer[][] data_DAYG_U_H = new Integer[ARR_MAX_SIZE][100];
    private Integer[][] data_YEARG_U_H = new Integer[ARR_MAX_SIZE][100];
    private Integer[][] data_HOURG_U_H = new Integer[ARR_MAX_SIZE][100];
    private Double[][] data_TEMPG_U_H = new Double[ARR_MAX_SIZE][100];
    private Double[][] U_SOURCE = new Double[ARR_MAX_SIZE][100];

    private Double[][] V_H = new Double[ARR_MAX_SIZE][100];
    private Double[][] ERR_V_H = new Double[ARR_MAX_SIZE][100];
    private Double[][] U_w_φ_uncertainties = new Double[ARR_MAX_SIZE][100];
    private Double[][] V_w_φ_uncertainties = new Double[ARR_MAX_SIZE][100];
    private Integer[][] data_MONTHG_V_H = new Integer[ARR_MAX_SIZE][100];
    private Integer[][] data_DAYG_V_H = new Integer[ARR_MAX_SIZE][100];
    private Integer[][] data_YEARG_V_H = new Integer[ARR_MAX_SIZE][100];
    private Integer[][] data_HOURG_V_H = new Integer[ARR_MAX_SIZE][100];
    private Double[][] data_TEMPG_V_H = new Double[ARR_MAX_SIZE][100];
    private Double[][] V_SOURCE = new Double[ARR_MAX_SIZE][100];

    private Double[][] ERR_TEMPG1 = new Double[ARR_MAX_SIZE][200];
    private Double[][] ERR_RHG1 = new Double[ARR_MAX_SIZE][200];

    public IgraHarmonizer(StationToHarmonize stationToHarmonize, CommonStaticDataStructure commonStaticDataStructure, DataSource dataSource) {
        this.stationToHarmonize = stationToHarmonize;
        this.commonStaticDataStructure = commonStaticDataStructure;

        this.stationDAO = new StationDAO(dataSource);
        this.stationToHarmonizeDAO = new StationToHarmonizeDAO(dataSource);
        this.guanDataHeaderDAO = new GuanDataHeaderDAO(dataSource);
        this.guanDataValueDAO = new GuanDataValueDAO(dataSource);
        this.harmonizationBreakDAO = new HarmonizationBreakDAO(dataSource);
        this.headerDAO = new HeaderDAO(dataSource);

        this.measurementConverter = new MeasurementConverter();
        this.headerConverter = new HeaderConverter(commonStaticDataStructure);
        this.productConverter = new ProductConverter(commonStaticDataStructure);
    }

    protected void process() {
        Instant start = Instant.now();
        station = getStation();

        initList();

        initHarmonizedData();

        setProductMandatoryLevels();

        boolean isHarmonizable = isHarmonizable();

        if (isHarmonizable) {
            Object[] igraData = read_igra_files_alllevels_sub();

            harmonize(igraData);

            setHarmonizedValues();
        } else {
            Map<Instant, List<HarmonizedData>> harmonizedDataPerDate = harmonizedDataList
                    .parallelStream()
                    .collect(groupingBy(HarmonizedData::getDateOfObservation));

            Map<Instant, List<Product>> productMandatoryLevelsPerDate = productMandatoryLevels
                    .parallelStream()
                    .collect(groupingBy(Product::getDateOfObservation));

            Set<Instant> setDateOfProducts = productMandatoryLevels.parallelStream()
                    .map(Product::getDateOfObservation)
                    .collect(Collectors.toSet());

            setDateOfProducts
                    .stream()
                    .forEach(date -> {
                        List<Product> productsOfTheDate = productMandatoryLevelsPerDate.get(date);
                        List<HarmonizedData> harmonizedDataOftheDate = harmonizedDataPerDate.get(date);

                        if (harmonizedDataOftheDate != null) {
                            Map<Float, List<Product>> productsOfTheDatePerPress = productsOfTheDate
                                    .parallelStream()
                                    .collect(groupingBy(Product::getPress));

                            Map<Float, List<HarmonizedData>> harmonizedDataOftheDatePerPress = harmonizedDataOftheDate
                                    .parallelStream()
                                    .collect(groupingBy(HarmonizedData::getPress));

                            Set<Float> setPress = productsOfTheDate
                                    .parallelStream()
                                    .map(Product::getPress)
                                    .collect(Collectors.toSet());

                            setPress
                                    .parallelStream()
                                    .forEach(pressure -> {
                                        updateHarmonizedDataWithProductValues(harmonizedDataOftheDatePerPress.get(pressure).get(0)
                                                , productsOfTheDatePerPress.get(pressure).get(0)
                                                , isHarmonizable);
                                    });
                        }
                    });
        }

        finalInterpolation();

        wmoCorrectionForSignificanceLevels();

        wmoMinPressureLevelCheck();

        roundValues();

        checkNullvalues();

        fixUnits();

        saveHarmonizedData();

        Instant finish = Instant.now();
        setPhaseProcessingTime(StationToHarmonize.PHASE.TOTAL, Duration.between(start, finish).toMillis());
    }

    private void wmoMinPressureLevelCheck() {
        log.info(station.getIdStation() + " wmoMinPressureLevelCheck");

        sondeIdList.stream()
                .forEach(sondeId -> {
                    //Filter for sonde id
                    List<WmoMinPressureLevel> wmoMinPressureLevelFiltered = commonStaticDataStructure.wmoMinPressureLevelList.stream()
                            .filter(x -> x.getSondeIdList().contains(sondeId))
                            .collect(Collectors.toList());

                    if (wmoMinPressureLevelFiltered.size() > 0) {
                        Set<Instant> dateList = headerFull.stream()
                                .filter(x -> x.getRadiosonde_id() != null && x.getRadiosonde_id().equals(sondeId))
                                .map(x -> x.getDateOfObservation())
                                .collect(Collectors.toSet());

                        Map<Instant, Header> headerMap = headerFull.stream()
                                .filter(x -> dateList.contains(x.getDateOfObservation()))
                                .collect(Collectors.toMap(Header::getDateOfObservation, header -> header));

                        harmonizedDataList.stream()
                                .filter(x -> dateList.contains(x.getDateOfObservation()))
                                .forEach(harmonizedData -> {
                                    Header header = headerMap.get(harmonizedData.getDateOfObservation());

                                    harmonizedData.setSza(header.getSza());
                                });

                        zenList.stream()
                                .forEach(zen -> {
                                    List<WmoMinPressureLevel> wmoMinPressureLevelFiltered_zen = wmoMinPressureLevelFiltered.stream()
                                            .filter(x -> zen.equals(ZEN.DAY) ? x.getDay().equals(true) : x.getDay().equals(false))
                                            .collect(Collectors.toList());

                                    List<HarmonizedData> harmonizedDataListFiltered = harmonizedDataList.stream()
                                            .filter(harmonizedData -> dateList.contains(harmonizedData.getDateOfObservation()))
                                            .filter(harmonizedData -> zen.equals(ZEN.DAY) ? harmonizedData.getSza() >= -5.0f : harmonizedData.getSza() < -5.0f)
                                            .collect(Collectors.toList());

                                    ecvList.stream()
                                            .forEach(ecv -> {
                                                Float minPress = wmoMinPressureLevelFiltered_zen.stream()
                                                        .filter(x -> x.getEcv().equals(ecv.name()))
                                                        .findAny()
                                                        .get()
                                                        .getMin_press();

                                                List<HarmonizedData> harmonizedDataListToRemove = harmonizedDataListFiltered.stream()
                                                        .filter(x -> x.getPress() < minPress)
                                                        .collect(Collectors.toList());

                                                switch (ecv) {
                                                    case TEMP:
                                                        harmonizedDataListToRemove.parallelStream()
                                                                .forEach(harmonizedData -> {
                                                                    harmonizedData.setTemp_product(null);
                                                                    harmonizedData.setTemp_product_cor_temp(null);
                                                                    harmonizedData.setTemp_product_u_cor_temp(null);
                                                                    harmonizedData.setTemp_product_cor_temp_tl(null);
                                                                    harmonizedData.setTemp_product_u_cor_temp_tl(null);
                                                                    harmonizedData.setTemp_product_cor_intercomparison_temp(null);
                                                                    harmonizedData.setTemp_product_u_cor_intercomparison_temp(null);
                                                                    harmonizedData.setTemp_h(null);
                                                                    harmonizedData.setErr_temp_h(null);
                                                                });
                                                        break;
                                                    case RH:
                                                        harmonizedDataListToRemove.parallelStream()
                                                                .forEach(harmonizedData -> {
                                                                    harmonizedData.setRh_product(null);
                                                                    harmonizedData.setRh_product_cor_rh(null);
                                                                    harmonizedData.setRh_product_u_cor_rh(null);
                                                                    harmonizedData.setRh_product_cor_rh_tl(null);
                                                                    harmonizedData.setRh_product_u_cor_rh_tl(null);
                                                                    harmonizedData.setRh_product_cor_intercomparison_rh(null);
                                                                    harmonizedData.setRh_product_u_cor_intercomparison_rh(null);
                                                                    harmonizedData.setRh_h(null);
                                                                    harmonizedData.setErr_rh_h(null);
                                                                });
                                                        break;
                                                    case U:
                                                        harmonizedDataListToRemove.parallelStream()
                                                                .forEach(harmonizedData -> {
                                                                    harmonizedData.setU_product(null);
                                                                    harmonizedData.setU_product_cor_u(null);
                                                                    harmonizedData.setU_product_u_cor_u(null);
                                                                    harmonizedData.setU_product_cor_u_rs92(null);
                                                                    harmonizedData.setU_product_u_cor_u_rs92(null);
                                                                    harmonizedData.setU_product_cor_u_notRs92(null);
                                                                    harmonizedData.setU_product_u_cor_u_notRs92(null);
                                                                    harmonizedData.setU_h(null);
                                                                    harmonizedData.setErr_u_h(null);
                                                                });
                                                        break;
                                                    case V:
                                                        harmonizedDataListToRemove.parallelStream()
                                                                .forEach(harmonizedData -> {
                                                                    harmonizedData.setV_product(null);
                                                                    harmonizedData.setV_product_cor_v(null);
                                                                    harmonizedData.setV_product_u_cor_v(null);
                                                                    harmonizedData.setV_product_cor_v_rs92(null);
                                                                    harmonizedData.setV_product_u_cor_v_rs92(null);
                                                                    harmonizedData.setV_product_cor_v_notRs92(null);
                                                                    harmonizedData.setV_product_u_cor_v_notRs92(null);
                                                                    harmonizedData.setV_h(null);
                                                                    harmonizedData.setErr_v_h(null);
                                                                });
                                                        break;
                                                }

                                                harmonizedDataListToRemove.parallelStream()
                                                        .forEach(harmonizedData -> {
                                                            harmonizedData.setWvmr_product(null);
                                                            harmonizedData.setWvmr_h(null);
                                                            harmonizedData.setWdir_h(null);
                                                            harmonizedData.setErr_wdir_h(null);
                                                            harmonizedData.setWspeed_h(null);
                                                            harmonizedData.setErr_wspeed_h(null);
                                                        });
                                            });
                                });
                    }
                });
    }

    private void saveHarmonizedData() {
        log.info(station.getIdStation() + " saveHarmonizedData");

        Instant start = Instant.now();

        headerFull
                .parallelStream()
                .forEach(header -> {
                    List<HarmonizedData> hList = harmonizedDataPerDate.get(header.getDateOfObservation());

                    header.setData(new HashSet<>(hList));

                    hList.parallelStream()
                            .forEach(x -> {
                                x.setHeader(header);
                                x.setReltime(header.getReltime());
                            });
                });

        headerDAO.save(headerFull, BaseObjDAO.SQL_COMMAND.INSERT);

        Instant finish = Instant.now();

        setPhaseProcessingTime(StationToHarmonize.PHASE.DB, Duration.between(start, finish).toMillis());
    }

    private void fixUnits() {
        log.info(station.getIdStation() + " fixUnits");

        harmonizedDataList.stream().forEach(harmonizedData -> {
            //press
            if (harmonizedData.getPress() != null && Double.isFinite(harmonizedData.getPress()))
                harmonizedData.setPress(harmonizedData.getPress() * 100);

            //RH
            if (harmonizedData.getRh() != null && Double.isFinite(harmonizedData.getRh()))
                harmonizedData.setRh(harmonizedData.getRh() * 100);
            if (harmonizedData.getRh_product() != null && Double.isFinite(harmonizedData.getRh_product()))
                harmonizedData.setRh_product(harmonizedData.getRh_product() * 100);
            if (harmonizedData.getRh_product_cor_rh() != null && Double.isFinite(harmonizedData.getRh_product_cor_rh()))
                harmonizedData.setRh_product_cor_rh(harmonizedData.getRh_product_cor_rh());
            if (harmonizedData.getRh_product_u_cor_rh() != null && Double.isFinite(harmonizedData.getRh_product_u_cor_rh()))
                harmonizedData.setRh_product_u_cor_rh(harmonizedData.getRh_product_u_cor_rh() * 100);
            if (harmonizedData.getRh_product_cor_rh_tl() != null && Double.isFinite(harmonizedData.getRh_product_cor_rh_tl()))
                harmonizedData.setRh_product_cor_rh_tl(harmonizedData.getRh_product_cor_rh_tl());
            if (harmonizedData.getRh_product_u_cor_rh_tl() != null && Double.isFinite(harmonizedData.getRh_product_u_cor_rh_tl()))
                harmonizedData.setRh_product_u_cor_rh_tl(harmonizedData.getRh_product_u_cor_rh_tl() * 100);
            if (harmonizedData.getRh_product_cor_intercomparison_rh() != null && Double.isFinite(harmonizedData.getRh_product_cor_intercomparison_rh()))
                harmonizedData.setRh_product_cor_intercomparison_rh(harmonizedData.getRh_product_cor_intercomparison_rh());
            if (harmonizedData.getRh_product_u_cor_intercomparison_rh() != null && Double.isFinite(harmonizedData.getRh_product_u_cor_intercomparison_rh()))
                harmonizedData.setRh_product_u_cor_intercomparison_rh(harmonizedData.getRh_product_u_cor_intercomparison_rh() * 100);
            if (harmonizedData.getRh_h() != null && Double.isFinite(harmonizedData.getRh_h()))
                harmonizedData.setRh_h(harmonizedData.getRh_h() * 100);
            if (harmonizedData.getErr_rh_h() != null && Double.isFinite(harmonizedData.getErr_rh_h()))
                harmonizedData.setErr_rh_h(harmonizedData.getErr_rh_h() * 100);

            //WVMR
            if (harmonizedData.getWvmr() != null && Double.isFinite(harmonizedData.getWvmr()))
                harmonizedData.setWvmr((harmonizedData.getWvmr() * 28.96f) / 18.01528f);
            if (harmonizedData.getWvmr_h() != null && Double.isFinite(harmonizedData.getWvmr_h()))
                harmonizedData.setWvmr_h((harmonizedData.getWvmr_h() * 28.96f) / 18.01528f);
            if (harmonizedData.getWvmr_product() != null && Double.isFinite(harmonizedData.getWvmr_product()))
                harmonizedData.setWvmr_product((harmonizedData.getWvmr_product() * 28.96f) / 18.01528f);

            //DPDP
            if (harmonizedData.getDpdp() != null && Double.isFinite(harmonizedData.getDpdp())) {
                Float temp = (harmonizedData.getTemp_h() != null && Double.isFinite(harmonizedData.getTemp_h())) ? harmonizedData.getTemp_h() : harmonizedData.getTemp();
                if (temp != null && Double.isFinite(temp)) {
                    harmonizedData.setDpdp(temp - harmonizedData.getDpdp());
                }
            }
        });
    }

    private void checkNullvalues() {
        log.info(station.getIdStation() + " checkNullvalues");

        harmonizedDataList.stream()
                .forEach(harmonizedData -> {
                    Integer[] checkValues = new Integer[NUM_CHECK_FIELDS];

                    checkValues[TIME_CHECK_FIELD_IDX] = checkValue(harmonizedData.getTime());
                    checkValues[GEOPOT_CHECK_FIELD_IDX] = checkValue(harmonizedData.getGeopot());
                    checkValues[DPDP_CHECK_FIELD_IDX] = checkValue(harmonizedData.getDpdp());
                    checkValues[FP_CHECK_FIELD_IDX] = checkValue(harmonizedData.getFp());
                    checkValues[ASC_CHECK_FIELD_IDX] = checkValue(harmonizedData.getAsc());
                    checkValues[SZA_CHECK_FIELD_IDX] = checkValue(harmonizedData.getSza());

                    checkValues[TEMP_CHECK_FIELD_IDX] = checkValue(harmonizedData.getTemp());
                    checkValues[TEMP_PRODUCT_CHECK_FIELD_IDX] = checkValue(harmonizedData.getTemp_product());
                    checkValues[TEMP_PRODUCT_COR_TEMP_CHECK_FIELD_IDX] = checkValue(harmonizedData.getTemp_product_cor_temp());
                    checkValues[TEMP_PRODUCT_U_COR_TEMP_CHECK_FIELD_IDX] = checkValue(harmonizedData.getTemp_product_u_cor_temp());
                    checkValues[TEMP_PRODUCT_COR_TEMP_TL_CHECK_FIELD_IDX] = checkValue(harmonizedData.getTemp_product_cor_temp_tl());
                    checkValues[TEMP_PRODUCT_U_COR_TEMP_TL_CHECK_FIELD_IDX] = checkValue(harmonizedData.getTemp_product_u_cor_temp_tl());
                    checkValues[TEMP_PRODUCT_COR_INTERCOMPARISON_TEMP_CHECK_FIELD_IDX] = checkValue(harmonizedData.getTemp_product_cor_intercomparison_temp());
                    checkValues[TEMP_PRODUCT_U_COR_INTERCOMPARISON_TEMP_CHECK_FIELD_IDX] = checkValue(harmonizedData.getTemp_product_u_cor_intercomparison_temp());
                    checkValues[TEMP_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getTemp_h());
                    checkValues[ERR_TEMP_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getErr_temp_h());

                    checkValues[RH_CHECK_FIELD_IDX] = checkValue(harmonizedData.getRh());
                    checkValues[RH_PRODUCT_CHECK_FIELD_IDX] = checkValue(harmonizedData.getRh_product());
                    checkValues[RH_PRODUCT_COR_RH_CHECK_FIELD_IDX] = checkValue(harmonizedData.getRh_product_cor_rh());
                    checkValues[RH_PRODUCT_U_COR_RH_CHECK_FIELD_IDX] = checkValue(harmonizedData.getRh_product_u_cor_rh());
                    checkValues[RH_PRODUCT_COR_RH_TL_CHECK_FIELD_IDX] = checkValue(harmonizedData.getRh_product_cor_rh_tl());
                    checkValues[RH_PRODUCT_U_COR_RH_TL_CHECK_FIELD_IDX] = checkValue(harmonizedData.getRh_product_u_cor_rh_tl());
                    checkValues[RH_PRODUCT_COR_INTERCOMPARISON_RH_CHECK_FIELD_IDX] = checkValue(harmonizedData.getRh_product_cor_intercomparison_rh());
                    checkValues[RH_PRODUCT_U_COR_INTERCOMPARISON_RH_CHECK_FIELD_IDX] = checkValue(harmonizedData.getRh_product_u_cor_intercomparison_rh());
                    checkValues[RH_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getRh_h());
                    checkValues[ERR_RH_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getErr_rh_h());

                    checkValues[U_CHECK_FIELD_IDX] = checkValue(harmonizedData.getU());
                    checkValues[U_PRODUCT_CHECK_FIELD_IDX] = checkValue(harmonizedData.getU_product());
                    checkValues[U_PRODUCT_COR_U_CHECK_FIELD_IDX] = checkValue(harmonizedData.getU_product_cor_u());
                    checkValues[U_PRODUCT_U_COR_U_CHECK_FIELD_IDX] = checkValue(harmonizedData.getU_product_u_cor_u());
                    checkValues[U_PRODUCT_COR_U_RS92_CHECK_FIELD_IDX] = checkValue(harmonizedData.getU_product_cor_u_rs92());
                    checkValues[U_PRODUCT_U_COR_U_RS92_CHECK_FIELD_IDX] = checkValue(harmonizedData.getU_product_u_cor_u_rs92());
                    checkValues[U_PRODUCT_COR_U_NOT_RS92_CHECK_FIELD_IDX] = checkValue(harmonizedData.getU_product_cor_u_notRs92());
                    checkValues[U_PRODUCT_U_COR_U_NOT_RS92_CHECK_FIELD_IDX] = checkValue(harmonizedData.getU_product_u_cor_u_notRs92());
                    checkValues[U_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getU_h());
                    checkValues[ERR_U_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getErr_u_h());

                    checkValues[V_CHECK_FIELD_IDX] = checkValue(harmonizedData.getV());
                    checkValues[V_PRODUCT_CHECK_FIELD_IDX] = checkValue(harmonizedData.getV_product());
                    checkValues[V_PRODUCT_COR_V_CHECK_FIELD_IDX] = checkValue(harmonizedData.getV_product_cor_v());
                    checkValues[V_PRODUCT_U_COR_V_CHECK_FIELD_IDX] = checkValue(harmonizedData.getV_product_u_cor_v());
                    checkValues[V_PRODUCT_COR_V_RS92_CHECK_FIELD_IDX] = checkValue(harmonizedData.getV_product_cor_v_rs92());
                    checkValues[V_PRODUCT_U_COR_V_RS92_CHECK_FIELD_IDX] = checkValue(harmonizedData.getV_product_u_cor_v_rs92());
                    checkValues[V_PRODUCT_COR_V_NOT_RS92_CHECK_FIELD_IDX] = checkValue(harmonizedData.getV_product_cor_v_notRs92());
                    checkValues[V_PRODUCT_U_COR_V_NOT_RS92_CHECK_FIELD_IDX] = checkValue(harmonizedData.getV_product_u_cor_v_notRs92());
                    checkValues[V_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getV_h());
                    checkValues[ERR_V_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getErr_v_h());

                    checkValues[WVMR_CHECK_FIELD_IDX] = checkValue(harmonizedData.getWvmr());
                    checkValues[WVMR_PRODUCT_CHECK_FIELD_IDX] = checkValue(harmonizedData.getWvmr_product());
                    checkValues[WVMR_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getWvmr_h());

                    checkValues[WDIR_CHECK_FIELD_IDX] = checkValue(harmonizedData.getWdir());
                    checkValues[WDIR_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getWdir_h());
                    checkValues[ERR_WDIR_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getErr_wdir_h());

                    checkValues[WSPEED_CHECK_FIELD_IDX] = checkValue(harmonizedData.getWspeed());
                    checkValues[WSPEED_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getWspeed_h());
                    checkValues[ERR_WSPEED_H_CHECK_FIELD_IDX] = checkValue(harmonizedData.getErr_wspeed_h());

                    checkValues[RELTIME_CHECK_FIELD_IDX] = harmonizedData.getReltime() == null ? VALUE_CHECKED_MISSING : VALUE_CHECKED_VALID;

                    harmonizedData.setCheck(checkValues);

                    for (int i = 0; i < checkValues.length; i++) {
                        if (checkValues[i] > VALUE_CHECKED_VALID) {
                            removeNotValidValue(harmonizedData, i);
                        }
                    }
                });
    }

    private void removeNotValidValue(HarmonizedData harmonizedData, int index) {
        switch (index) {
            case TIME_CHECK_FIELD_IDX:
                harmonizedData.setTime(null);
                break;
            case GEOPOT_CHECK_FIELD_IDX:
                harmonizedData.setGeopot(null);
                break;
            case DPDP_CHECK_FIELD_IDX:
                harmonizedData.setDpdp(null);
                break;
            case FP_CHECK_FIELD_IDX:
                harmonizedData.setFp(null);
                break;
            case ASC_CHECK_FIELD_IDX:
                harmonizedData.setAsc(null);
                break;
            case SZA_CHECK_FIELD_IDX:
                harmonizedData.setSza(null);
                break;
            case TEMP_CHECK_FIELD_IDX:
                harmonizedData.setTemp(null);
                break;
            case TEMP_PRODUCT_CHECK_FIELD_IDX:
                harmonizedData.setTemp_product(null);
                break;
            case TEMP_PRODUCT_COR_TEMP_CHECK_FIELD_IDX:
                harmonizedData.setTemp_product_cor_temp(null);
                break;
            case TEMP_PRODUCT_U_COR_TEMP_CHECK_FIELD_IDX:
                harmonizedData.setTemp_product_u_cor_temp(null);
                break;
            case TEMP_PRODUCT_COR_TEMP_TL_CHECK_FIELD_IDX:
                harmonizedData.setTemp_product_cor_temp_tl(null);
                break;
            case TEMP_PRODUCT_U_COR_TEMP_TL_CHECK_FIELD_IDX:
                harmonizedData.setTemp_product_u_cor_temp_tl(null);
                break;
            case TEMP_PRODUCT_COR_INTERCOMPARISON_TEMP_CHECK_FIELD_IDX:
                harmonizedData.setTemp_product_cor_intercomparison_temp(null);
                break;
            case TEMP_PRODUCT_U_COR_INTERCOMPARISON_TEMP_CHECK_FIELD_IDX:
                harmonizedData.setTemp_product_u_cor_intercomparison_temp(null);
                break;
            case TEMP_H_CHECK_FIELD_IDX:
                harmonizedData.setTemp_h(null);
                break;
            case ERR_TEMP_H_CHECK_FIELD_IDX:
                harmonizedData.setErr_temp_h(null);
                break;
            case RH_CHECK_FIELD_IDX:
                harmonizedData.setRh(null);
                break;
            case RH_PRODUCT_CHECK_FIELD_IDX:
                harmonizedData.setRh_product(null);
                break;
            case RH_PRODUCT_COR_RH_CHECK_FIELD_IDX:
                harmonizedData.setRh_product_cor_rh(null);
                break;
            case RH_PRODUCT_U_COR_RH_CHECK_FIELD_IDX:
                harmonizedData.setRh_product_u_cor_rh(null);
                break;
            case RH_PRODUCT_COR_RH_TL_CHECK_FIELD_IDX:
                harmonizedData.setRh_product_cor_rh_tl(null);
                break;
            case RH_PRODUCT_U_COR_RH_TL_CHECK_FIELD_IDX:
                harmonizedData.setRh_product_u_cor_rh_tl(null);
                break;
            case RH_PRODUCT_COR_INTERCOMPARISON_RH_CHECK_FIELD_IDX:
                harmonizedData.setRh_product_cor_intercomparison_rh(null);
                break;
            case RH_PRODUCT_U_COR_INTERCOMPARISON_RH_CHECK_FIELD_IDX:
                harmonizedData.setRh_product_u_cor_intercomparison_rh(null);
                break;
            case RH_H_CHECK_FIELD_IDX:
                harmonizedData.setRh_h(null);
                break;
            case ERR_RH_H_CHECK_FIELD_IDX:
                harmonizedData.setErr_rh_h(null);
                break;
            case U_CHECK_FIELD_IDX:
                harmonizedData.setU(null);
                break;
            case U_PRODUCT_CHECK_FIELD_IDX:
                harmonizedData.setU_product(null);
                break;
            case U_PRODUCT_COR_U_CHECK_FIELD_IDX:
                harmonizedData.setU_product_cor_u(null);
                break;
            case U_PRODUCT_U_COR_U_CHECK_FIELD_IDX:
                harmonizedData.setU_product_u_cor_u(null);
                break;
            case U_PRODUCT_COR_U_RS92_CHECK_FIELD_IDX:
                harmonizedData.setU_product_cor_u_rs92(null);
                break;
            case U_PRODUCT_U_COR_U_RS92_CHECK_FIELD_IDX:
                harmonizedData.setU_product_u_cor_u_rs92(null);
                break;
            case U_PRODUCT_COR_U_NOT_RS92_CHECK_FIELD_IDX:
                harmonizedData.setU_product_cor_u_notRs92(null);
                break;
            case U_PRODUCT_U_COR_U_NOT_RS92_CHECK_FIELD_IDX:
                harmonizedData.setU_product_u_cor_u_notRs92(null);
                break;
            case U_H_CHECK_FIELD_IDX:
                harmonizedData.setU_h(null);
                break;
            case ERR_U_H_CHECK_FIELD_IDX:
                harmonizedData.setErr_u_h(null);
                break;
            case V_CHECK_FIELD_IDX:
                harmonizedData.setV(null);
                break;
            case V_PRODUCT_CHECK_FIELD_IDX:
                harmonizedData.setV_product(null);
                break;
            case V_PRODUCT_COR_V_CHECK_FIELD_IDX:
                harmonizedData.setV_product_cor_v(null);
                break;
            case V_PRODUCT_U_COR_V_CHECK_FIELD_IDX:
                harmonizedData.setV_product_u_cor_v(null);
                break;
            case V_PRODUCT_COR_V_RS92_CHECK_FIELD_IDX:
                harmonizedData.setV_product_cor_v_rs92(null);
                break;
            case V_PRODUCT_U_COR_V_RS92_CHECK_FIELD_IDX:
                harmonizedData.setV_product_u_cor_v_rs92(null);
                break;
            case V_PRODUCT_COR_V_NOT_RS92_CHECK_FIELD_IDX:
                harmonizedData.setV_product_cor_v_notRs92(null);
                break;
            case V_PRODUCT_U_COR_V_NOT_RS92_CHECK_FIELD_IDX:
                harmonizedData.setV_product_u_cor_v_notRs92(null);
                break;
            case V_H_CHECK_FIELD_IDX:
                harmonizedData.setV_h(null);
                break;
            case ERR_V_H_CHECK_FIELD_IDX:
                harmonizedData.setErr_v_h(null);
                break;
            case WVMR_CHECK_FIELD_IDX:
                harmonizedData.setWvmr(null);
                break;
            case WVMR_PRODUCT_CHECK_FIELD_IDX:
                harmonizedData.setWvmr_product(null);
                break;
            case WVMR_H_CHECK_FIELD_IDX:
                harmonizedData.setWvmr_h(null);
                break;
            case WDIR_CHECK_FIELD_IDX:
                harmonizedData.setWdir(null);
                break;
            case WDIR_H_CHECK_FIELD_IDX:
                harmonizedData.setWdir_h(null);
                break;
            case ERR_WDIR_H_CHECK_FIELD_IDX:
                harmonizedData.setErr_wdir_h(null);
                break;
            case WSPEED_CHECK_FIELD_IDX:
                harmonizedData.setWspeed(null);
                break;
            case WSPEED_H_CHECK_FIELD_IDX:
                harmonizedData.setWspeed_h(null);
                break;
            case ERR_WSPEED_H_CHECK_FIELD_IDX:
                harmonizedData.setErr_wspeed_h(null);
                break;
            case RELTIME_CHECK_FIELD_IDX:
                harmonizedData.setReltime(null);
                break;
        }
    }

    private int checkValue(Float value) {
        int valueChecked;

        if (value == null || value.equals(-9999.0f) || value.equals(Float.NaN)) {
            valueChecked = VALUE_CHECKED_MISSING;
        } else if (value.equals(-8888.0f)) {
            valueChecked = VALUE_CHECKED_DISCARDED;
        } else {
            valueChecked = VALUE_CHECKED_VALID;
        }

        return valueChecked;
    }

    private void roundValues() {
        log.info(station.getIdStation() + " roundValues");

        harmonizedDataList.stream().forEach(harmonizedData -> {

            //TEMP
            harmonizedData.setFp(setRoundedValue(harmonizedData.getFp(), SIGNIFICANT_FIGURES.TEMP));
            harmonizedData.setTemp_product(setRoundedValue(harmonizedData.getTemp_product(), SIGNIFICANT_FIGURES.TEMP));
            harmonizedData.setTemp_product_cor_temp(setRoundedValue(harmonizedData.getTemp_product_cor_temp(), SIGNIFICANT_FIGURES.TEMP));
            harmonizedData.setTemp_product_u_cor_temp(setRoundedValue(harmonizedData.getTemp_product_u_cor_temp(), SIGNIFICANT_FIGURES.TEMP));
            harmonizedData.setTemp_product_cor_temp_tl(setRoundedValue(harmonizedData.getTemp_product_cor_temp_tl(), SIGNIFICANT_FIGURES.TEMP));
            harmonizedData.setTemp_product_u_cor_temp_tl(setRoundedValue(harmonizedData.getTemp_product_u_cor_temp_tl(), SIGNIFICANT_FIGURES.TEMP));
            harmonizedData.setTemp_product_cor_intercomparison_temp(setRoundedValue(harmonizedData.getTemp_product_cor_intercomparison_temp(), SIGNIFICANT_FIGURES.TEMP));
            harmonizedData.setTemp_product_u_cor_intercomparison_temp(setRoundedValue(harmonizedData.getTemp_product_u_cor_intercomparison_temp(), SIGNIFICANT_FIGURES.TEMP));
            harmonizedData.setTemp_h(setRoundedValue(harmonizedData.getTemp_h(), SIGNIFICANT_FIGURES.TEMP));
            harmonizedData.setErr_temp_h(setRoundedValue(harmonizedData.getErr_temp_h(), SIGNIFICANT_FIGURES.TEMP));

            //RH
            harmonizedData.setRh(setRoundedValue(harmonizedData.getRh(), SIGNIFICANT_FIGURES.RH));
            harmonizedData.setRh_product(setRoundedValue(harmonizedData.getRh_product(), SIGNIFICANT_FIGURES.RH));
            harmonizedData.setRh_product_cor_rh(setRoundedValue(harmonizedData.getRh_product_cor_rh(), SIGNIFICANT_FIGURES.RH));
            harmonizedData.setRh_product_u_cor_rh(setRoundedValue(harmonizedData.getRh_product_u_cor_rh(), SIGNIFICANT_FIGURES.RH));
            harmonizedData.setRh_product_cor_rh_tl(setRoundedValue(harmonizedData.getRh_product_cor_rh_tl(), SIGNIFICANT_FIGURES.RH));
            harmonizedData.setRh_product_u_cor_rh_tl(setRoundedValue(harmonizedData.getRh_product_u_cor_rh_tl(), SIGNIFICANT_FIGURES.RH));
            harmonizedData.setRh_product_cor_intercomparison_rh(setRoundedValue(harmonizedData.getRh_product_cor_intercomparison_rh(), SIGNIFICANT_FIGURES.RH));
            harmonizedData.setRh_product_u_cor_intercomparison_rh(setRoundedValue(harmonizedData.getRh_product_u_cor_intercomparison_rh(), SIGNIFICANT_FIGURES.RH));
            harmonizedData.setRh_h(setRoundedValue(harmonizedData.getRh_h(), SIGNIFICANT_FIGURES.RH));
            harmonizedData.setErr_rh_h(setRoundedValue(harmonizedData.getErr_rh_h(), SIGNIFICANT_FIGURES.RH));

            //WIND
            harmonizedData.setU(setRoundedValue(harmonizedData.getU(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setU_product(setRoundedValue(harmonizedData.getU_product(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setU_product_cor_u(setRoundedValue(harmonizedData.getU_product_cor_u(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setU_product_u_cor_u(setRoundedValue(harmonizedData.getU_product_u_cor_u(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setU_product_cor_u_rs92(setRoundedValue(harmonizedData.getU_product_cor_u_rs92(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setU_product_u_cor_u_rs92(setRoundedValue(harmonizedData.getU_product_u_cor_u_rs92(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setU_product_cor_u_notRs92(setRoundedValue(harmonizedData.getU_product_cor_u_notRs92(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setU_product_u_cor_u_notRs92(setRoundedValue(harmonizedData.getU_product_u_cor_u_notRs92(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setU_h(setRoundedValue(harmonizedData.getU_h(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setErr_u_h(setRoundedValue(harmonizedData.getErr_u_h(), SIGNIFICANT_FIGURES.WIND));

            harmonizedData.setV(setRoundedValue(harmonizedData.getV(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setV_product(setRoundedValue(harmonizedData.getV_product(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setV_product_cor_v(setRoundedValue(harmonizedData.getV_product_cor_v(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setV_product_u_cor_v(setRoundedValue(harmonizedData.getV_product_u_cor_v(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setV_product_cor_v_rs92(setRoundedValue(harmonizedData.getV_product_cor_v_rs92(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setV_product_u_cor_v_rs92(setRoundedValue(harmonizedData.getV_product_u_cor_v_rs92(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setV_product_cor_v_notRs92(setRoundedValue(harmonizedData.getV_product_cor_v_notRs92(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setV_product_u_cor_v_notRs92(setRoundedValue(harmonizedData.getV_product_u_cor_v_notRs92(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setV_h(setRoundedValue(harmonizedData.getV_h(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setErr_v_h(setRoundedValue(harmonizedData.getErr_v_h(), SIGNIFICANT_FIGURES.WIND));

            harmonizedData.setWdir(setRoundedValue(harmonizedData.getWdir(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setWdir_h(setRoundedValue(harmonizedData.getWdir_h(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setErr_wdir_h(setRoundedValue(harmonizedData.getErr_wdir_h(), SIGNIFICANT_FIGURES.WIND));

            harmonizedData.setWspeed(setRoundedValue(harmonizedData.getWspeed(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setWspeed_h(setRoundedValue(harmonizedData.getWspeed_h(), SIGNIFICANT_FIGURES.WIND));
            harmonizedData.setErr_wspeed_h(setRoundedValue(harmonizedData.getErr_wspeed_h(), SIGNIFICANT_FIGURES.WIND));

            //WVMR
            harmonizedData.setWvmr(setRoundedValue(harmonizedData.getWvmr(), SIGNIFICANT_FIGURES.WVMR));
            harmonizedData.setWvmr_product(setRoundedValue(harmonizedData.getWvmr_product(), SIGNIFICANT_FIGURES.WVMR));
            harmonizedData.setWvmr_h(setRoundedValue(harmonizedData.getWvmr_h(), SIGNIFICANT_FIGURES.WVMR));
        });
    }

    private Float setRoundedValue(Float value, SIGNIFICANT_FIGURES significantFigure) {
        if (value != null) {
            return new Float((Double.isFinite(value)) ? DoubleRounder.round(value, significantFigure.numSignificantFigures) : value);
        } else {
            return null;
        }
    }

    private void wmoCorrectionForSignificanceLevels() {
        log.info(station.getIdStation() + " wmoCorrectionForSignificanceLevels");

        sondeIdList.stream()
                .forEach(sondeId -> {
                    //filter by sonde ID
                    List<WmoIntercomparison> wmoIntercomparisonListFiltered = commonStaticDataStructure.wmoIntercomparisonList.stream()
                            .filter(x -> x.getSondeIdList().contains(sondeId) && !Arrays.asList(press).contains(x.getPress().doubleValue()))
                            .collect(Collectors.toList());

                    if (wmoIntercomparisonListFiltered.size() > 0) {
                        Set<Instant> dateList = headerFull.stream()
                                .filter(x -> x.getRadiosonde_id() != null && x.getRadiosonde_id().equals(sondeId))
                                .map(x -> x.getDateOfObservation())
                                .collect(Collectors.toSet());

                        Map<Instant, Header> headerMap = headerFull.stream()
                                .filter(x -> dateList.contains(x.getDateOfObservation()))
                                .collect(Collectors.toMap(Header::getDateOfObservation, header -> header));

                        zenList.stream()
                                .forEach(zen -> {
                                    List<WmoIntercomparison> wmoIntercomparisonListFiltered_zen = wmoIntercomparisonListFiltered.stream()
                                            .filter(x -> zen.equals(ZEN.DAY) ? x.getDay().equals(true) : x.getDay().equals(false))
                                            .collect(Collectors.toList());

                                    ecvList.stream()
                                            .forEach(ecv -> {
                                                //filter by ECV
                                                Map<Float, WmoIntercomparison> wmoIntercomparisonListFiltered_zenAndECV = new HashMap<>();
                                                wmoIntercomparisonListFiltered_zen.stream()
                                                        .filter(wmoIntercomparison -> wmoIntercomparison.getEcv().equals(ecv.name()))
                                                        .forEach(x -> {
                                                            wmoIntercomparisonListFiltered_zenAndECV.put(x.getPress(), x);
                                                        });

                                                harmonizedDataList.stream()
                                                        .filter(x -> !Arrays.asList(press).contains(x.getPress().doubleValue()) && dateList.contains(x.getDateOfObservation()))
                                                        .forEach(harmonizedData -> {
                                                            Header header = headerMap.get(harmonizedData.getDateOfObservation());

                                                            harmonizedData.setSza(header.getSza());

                                                            WmoIntercomparison wmoIntercomparison = wmoIntercomparisonListFiltered_zenAndECV.get(harmonizedData.getPress());

                                                            if (wmoIntercomparison != null) {
                                                                switch (ecv) {
                                                                    case TEMP:
                                                                        harmonizedData.setTemp_h(harmonizedData.getTemp() + wmoIntercomparison.getMean());
                                                                        harmonizedData.setErr_temp_h(wmoIntercomparison.getStd_dev());
                                                                        break;
                                                                    case RH:
                                                                        if (harmonizedData.getRh() != null) {
                                                                            harmonizedData.setRh_h(harmonizedData.getRh() + wmoIntercomparison.getMean());
                                                                            harmonizedData.setErr_rh_h(wmoIntercomparison.getStd_dev());
                                                                        }
                                                                        break;
                                                                    case U:
                                                                        harmonizedData.setU_h(harmonizedData.getU() + wmoIntercomparison.getMean());
                                                                        harmonizedData.setErr_u_h(wmoIntercomparison.getStd_dev());
                                                                        break;
                                                                    case V:
                                                                        harmonizedData.setV_h(harmonizedData.getV() + wmoIntercomparison.getMean());
                                                                        harmonizedData.setErr_v_h(wmoIntercomparison.getStd_dev());
                                                                        break;
                                                                }
                                                            }
                                                        });
                                            });
                                });
                    }
                });
    }

    private void finalInterpolation() {
        log.info(station.getIdStation() + " Final interpolation");

        Instant start = Instant.now();

        Map<Instant, List<Product>> productsPerDate = productMandatoryLevels.stream().collect(groupingBy(Product::getDateOfObservation));

        List<Double> pressionMandatoryLevelsList = Arrays.asList(press);

        double[] pressionMandatoryLevelsArr = pressionMandatoryLevelsList.stream().mapToDouble(d -> d).toArray();
        Arrays.sort(pressionMandatoryLevelsArr);

        LinearInterpolator li = new LinearInterpolator();
        SplineInterpolator splineInterpolator = new SplineInterpolator();

        for (Map.Entry me : harmonizedDataPerDate.entrySet()) {
            List<HarmonizedData> harmonizedDataListPerDate = (List<HarmonizedData>) me.getValue();

            List<Product> productListPerDate = productsPerDate.get(me.getKey());

            List<HarmonizedData> harmonizedDataListPerDateOnlyMandatory = harmonizedDataListPerDate.stream()
                    .filter(x -> pressionMandatoryLevelsList.contains(x.getPress().doubleValue()))
                    .sorted(Comparator.comparing(HarmonizedData::getPress))
                    .collect(Collectors.toList());

            List<HarmonizedDataForSignificanceLevelInterpolation> hdSignLevelInterpList = new ArrayList<>();

            harmonizedDataListPerDateOnlyMandatory.forEach(x -> {
                HarmonizedDataForSignificanceLevelInterpolation hdSignLevelInterp = new HarmonizedDataForSignificanceLevelInterpolation();

                hdSignLevelInterp.setPress(x.getPress());
                hdSignLevelInterp.setCor_temp(getValidValueForInterpolation(x.getPress(), x.getTemp_h(), x.getTemp()));
                hdSignLevelInterp.setErr_temp_h(getValidValueForInterpolation(x.getPress(), x.getErr_temp_h(), 0.0f));
                hdSignLevelInterp.setCor_rh(getValidValueForInterpolation(x.getPress(), x.getRh_h(), x.getRh()));
                hdSignLevelInterp.setErr_rh_h(getValidValueForInterpolation(x.getPress(), x.getErr_rh_h(), 0.0f));
                hdSignLevelInterp.setCor_u(getValidValueForInterpolation(x.getPress(), x.getU_h(), x.getU()));
                hdSignLevelInterp.setErr_u_h(getValidValueForInterpolation(x.getPress(), x.getErr_u_h(), 0.0f));
                hdSignLevelInterp.setCor_v(getValidValueForInterpolation(x.getPress(), x.getV_h(), x.getV()));
                hdSignLevelInterp.setErr_v_h(getValidValueForInterpolation(x.getPress(), x.getErr_v_h(), 0.0f));
                hdSignLevelInterp.setErr_wdir_h(getValidValueForInterpolation(x.getPress(), x.getErr_wdir_h(), 0.0f));
                hdSignLevelInterp.setErr_wspeed_h(getValidValueForInterpolation(x.getPress(), x.getErr_wspeed_h(), 0.0f));

                hdSignLevelInterpList.add(hdSignLevelInterp);
            });

            List<Double> press_cor_temp_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getCor_temp() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getPress)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> press_err_temp_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getErr_temp_h() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getPress)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> press_cor_rh_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getCor_rh() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getPress)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> press_err_rh_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getErr_rh_h() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getPress)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> press_cor_u_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getCor_u() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getPress)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> press_err_u_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getErr_u_h() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getPress)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> press_cor_v_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getCor_v() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getPress)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> press_err_v_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getErr_v_h() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getPress)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> press_err_wdir_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getErr_wdir_h() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getPress)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> press_err_wspeed_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getErr_wspeed_h() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getPress)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());

            List<Double> cor_temp_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getCor_temp() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getCor_temp)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> err_temp_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getErr_temp_h() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getErr_temp_h)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> cor_rh_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getCor_rh() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getCor_rh)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> err_rh_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getErr_rh_h() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getErr_rh_h)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> cor_u_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getCor_u() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getCor_u)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> err_u_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getErr_u_h() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getErr_u_h)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> cor_v_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getCor_v() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getCor_v)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> err_v_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getErr_v_h() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getErr_v_h)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> err_wdir_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getErr_wdir_h() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getErr_wdir_h)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());
            List<Double> err_wspeed_h = hdSignLevelInterpList.stream()
                    .filter(x -> x.getErr_wspeed_h() != null)
                    .map(HarmonizedDataForSignificanceLevelInterpolation::getErr_wspeed_h)
                    .map(Float::doubleValue)
                    .collect(Collectors.toList());

            double[] u_cor_t = new double[pressionMandatoryLevelsArr.length];
            double[] u_cor_rh = new double[pressionMandatoryLevelsArr.length];
            double[] u_cor_u = new double[pressionMandatoryLevelsArr.length];
            double[] u_cor_v = new double[pressionMandatoryLevelsArr.length];

            for (int i = 0; i < pressionMandatoryLevelsArr.length; i++) {
                Float u_cor_t_value = getUcorTatPressureLevel(productListPerDate, new Float(pressionMandatoryLevelsArr[i]));

                if (u_cor_t_value != null && !u_cor_t_value.isNaN()) {
                    u_cor_t[i] = new Double(u_cor_t_value).doubleValue();
                } else {
                    u_cor_t[i] = Double.NaN;
                }

                Float u_cor_rh_value = getUcorRHatPressureLevel(productListPerDate, new Float(pressionMandatoryLevelsArr[i]));

                if (u_cor_rh_value != null && !u_cor_rh_value.isNaN()) {
                    u_cor_rh[i] = new Double(u_cor_rh_value).doubleValue();
                } else {
                    u_cor_rh[i] = Double.NaN;
                }

                Float u_cor_u_value = getUcorUatPressureLevel(productListPerDate, new Float(pressionMandatoryLevelsArr[i]));

                if (u_cor_u_value != null && !u_cor_u_value.isNaN()) {
                    u_cor_u[i] = new Double(u_cor_u_value).doubleValue();
                } else {
                    u_cor_u[i] = Double.NaN;
                }

                Float u_cor_v_value = getUcorVatPressureLevel(productListPerDate, new Float(pressionMandatoryLevelsArr[i]));

                if (u_cor_v_value != null && !u_cor_v_value.isNaN()) {
                    u_cor_v[i] = new Double(u_cor_v_value).doubleValue();
                } else {
                    u_cor_v[i] = Double.NaN;
                }
            }

            boolean isInterpolableTemp_h = press_cor_temp_h.size() > 2 ? true : false;
            boolean isInterpolableErr_temp_h = press_err_temp_h.size() > 2 ? true : false;
            boolean isInterpolableRh_h = press_cor_rh_h.size() > 2 ? true : false;
            boolean isInterpolableErr_rh_h = press_err_rh_h.size() > 2 ? true : false;
            boolean isInterpolableU_h = press_cor_u_h.size() > 2 ? true : false;
            boolean isInterpolableErr_u_h = press_err_u_h.size() > 2 ? true : false;
            boolean isInterpolableV_h = press_cor_v_h.size() > 2 ? true : false;
            boolean isInterpolableErr_v_h = press_err_v_h.size() > 2 ? true : false;
            boolean isInterpolableErr_wdir_h = press_err_wdir_h.size() > 2 ? true : false;
            boolean isInterpolableErr_wspeed_h = press_err_wspeed_h.size() > 2 ? true : false;

            PolynomialSplineFunction psfTemp = null;
            PolynomialSplineFunction psfErrT_H = null;
            PolynomialSplineFunction psfRH = null;
            PolynomialSplineFunction psfErrRH_H = null;
            PolynomialSplineFunction psfU = null;
            PolynomialSplineFunction psfErrU_H = null;
            PolynomialSplineFunction psfV = null;
            PolynomialSplineFunction psfErrV_H = null;
            PolynomialSplineFunction psfErrWdir_H = null;
            PolynomialSplineFunction psfErrWspeed_H = null;

            if (isInterpolableTemp_h) {
                psfTemp = li.interpolate(
                        ArrayUtils.toPrimitive(press_cor_temp_h.toArray(new Double[press_cor_temp_h.size()])),
                        ArrayUtils.toPrimitive(cor_temp_h.toArray(new Double[cor_temp_h.size()]))
                );
            }
            if (isInterpolableErr_temp_h) {
                psfErrT_H = splineInterpolator.interpolate(
                        ArrayUtils.toPrimitive(press_err_temp_h.toArray(new Double[press_err_temp_h.size()])),
                        ArrayUtils.toPrimitive(err_temp_h.toArray(new Double[err_temp_h.size()]))
                );
            }
            if (isInterpolableRh_h) {
                psfRH = splineInterpolator.interpolate(
                        ArrayUtils.toPrimitive(press_cor_rh_h.toArray(new Double[press_cor_rh_h.size()])),
                        ArrayUtils.toPrimitive(cor_rh_h.toArray(new Double[cor_rh_h.size()]))
                );
            }
            if (isInterpolableErr_rh_h) {
                psfErrRH_H = splineInterpolator.interpolate(
                        ArrayUtils.toPrimitive(press_err_rh_h.toArray(new Double[press_err_rh_h.size()])),
                        ArrayUtils.toPrimitive(err_rh_h.toArray(new Double[err_rh_h.size()]))
                );
            }
            if (isInterpolableU_h) {
                psfU = splineInterpolator.interpolate(
                        ArrayUtils.toPrimitive(press_cor_u_h.toArray(new Double[press_cor_u_h.size()])),
                        ArrayUtils.toPrimitive(cor_u_h.toArray(new Double[cor_u_h.size()]))
                );
            }

            if (isInterpolableErr_u_h) {
                psfErrU_H = splineInterpolator.interpolate(
                        ArrayUtils.toPrimitive(press_err_u_h.toArray(new Double[press_err_u_h.size()])),
                        ArrayUtils.toPrimitive(err_u_h.toArray(new Double[err_u_h.size()]))
                );
            }
            if (isInterpolableV_h) {
                psfV = splineInterpolator.interpolate(
                        ArrayUtils.toPrimitive(press_cor_v_h.toArray(new Double[press_cor_v_h.size()])),
                        ArrayUtils.toPrimitive(cor_v_h.toArray(new Double[cor_v_h.size()]))
                );
            }
            if (isInterpolableErr_v_h) {
                psfErrV_H = splineInterpolator.interpolate(
                        ArrayUtils.toPrimitive(press_err_v_h.toArray(new Double[press_err_v_h.size()])),
                        ArrayUtils.toPrimitive(err_v_h.toArray(new Double[err_v_h.size()]))
                );
            }
            if (isInterpolableErr_wdir_h) {
                psfErrWdir_H = splineInterpolator.interpolate(
                        ArrayUtils.toPrimitive(press_err_wdir_h.toArray(new Double[press_err_wdir_h.size()])),
                        ArrayUtils.toPrimitive(err_wdir_h.toArray(new Double[err_wdir_h.size()]))
                );
            }
            if (isInterpolableErr_wspeed_h) {
                psfErrWspeed_H = splineInterpolator.interpolate(
                        ArrayUtils.toPrimitive(press_err_wspeed_h.toArray(new Double[press_err_wspeed_h.size()])),
                        ArrayUtils.toPrimitive(err_wspeed_h.toArray(new Double[err_wspeed_h.size()]))
                );
            }

            PolynomialSplineFunction psfUcorT = li.interpolate(pressionMandatoryLevelsArr, u_cor_t);
            PolynomialSplineFunction psfUcorRH = splineInterpolator.interpolate(pressionMandatoryLevelsArr, u_cor_rh);
            PolynomialSplineFunction psfUcorU = splineInterpolator.interpolate(pressionMandatoryLevelsArr, u_cor_u);
            PolynomialSplineFunction psfUcorV = splineInterpolator.interpolate(pressionMandatoryLevelsArr, u_cor_v);

            for (HarmonizedData h : harmonizedDataListPerDate) {
                if (!pressionMandatoryLevelsList.contains(h.getPress().doubleValue())) {
                    Double newTempValue = psfTemp != null && psfTemp.isValidPoint(h.getPress().doubleValue()) ? psfTemp.value(h.getPress().doubleValue()) : null;
                    Double newUcorTValue = psfUcorT.isValidPoint(h.getPress().doubleValue()) ? psfUcorT.value(h.getPress().doubleValue()) : null;
                    Double newErrT_HValue = psfErrT_H != null && psfErrT_H.isValidPoint(h.getPress().doubleValue()) ? psfErrT_H.value(h.getPress().doubleValue()) : null;

                    Double newRhValue = psfRH != null && psfRH.isValidPoint(h.getPress().doubleValue()) ? psfRH.value(h.getPress().doubleValue()) : null;
                    Double newUcorRHValue = psfUcorRH.isValidPoint(h.getPress().doubleValue()) ? psfUcorRH.value(h.getPress().doubleValue()) : null;
                    Double newErrRH_HValue = psfErrRH_H != null && psfErrRH_H.isValidPoint(h.getPress().doubleValue()) ? psfErrRH_H.value(h.getPress().doubleValue()) : null;

                    Double newUValue = psfU != null && psfU.isValidPoint(h.getPress().doubleValue()) ? psfU.value(h.getPress().doubleValue()) : null;
                    Double newUcorUValue = psfUcorU.isValidPoint(h.getPress().doubleValue()) ? psfUcorU.value(h.getPress().doubleValue()) : null;
                    Double newErrU_HValue = psfErrU_H != null && psfErrU_H.isValidPoint(h.getPress().doubleValue()) ? psfErrU_H.value(h.getPress().doubleValue()) : null;

                    Double newVValue = psfV != null && psfV.isValidPoint(h.getPress().doubleValue()) ? psfV.value(h.getPress().doubleValue()) : null;
                    Double newUcorVValue = psfUcorV.isValidPoint(h.getPress().doubleValue()) ? psfUcorV.value(h.getPress().doubleValue()) : null;
                    Double newErrV_HValue = psfErrV_H != null && psfErrV_H.isValidPoint(h.getPress().doubleValue()) ? psfErrV_H.value(h.getPress().doubleValue()) : null;

                    Double newErrWdir_HValue = psfErrWdir_H != null && psfErrWdir_H.isValidPoint(h.getPress().doubleValue()) ? psfErrWdir_H.value(h.getPress().doubleValue()) : null;
                    Double newErrWspeed_HValue = psfErrWspeed_H != null && psfErrWspeed_H.isValidPoint(h.getPress().doubleValue()) ? psfErrWspeed_H.value(h.getPress().doubleValue()) : null;

                    if (isValidValueForInterpolation(newTempValue) && isValidValueForInterpolation(h.getTemp())) {
                        h.setTemp_h(h.getTemp().floatValue() + newTempValue.floatValue());
                    } else {
                        h.setTemp_h(isValidValueForInterpolation(h.getTemp()) ? h.getTemp() : null);
                    }

                    if (newUcorTValue != null && !newUcorTValue.isNaN() && newErrT_HValue != null && !newErrT_HValue.isNaN()) {
                        h.setErr_temp_h(new Float(Math.sqrt(Math.pow(newUcorTValue, 2) + Math.pow(newErrT_HValue, 2) + Math.pow(0.25, 2))));
                    }

                    if (isValidValueForInterpolation(newRhValue) && isValidValueForInterpolation(h.getRh())) {
                        Float rhValue = h.getRh() + newRhValue.floatValue();

                        if (rhValue > 0) {
                            if (rhValue > 1.0f) {
                                rhValue = 1.0f;
                            }

                            h.setRh_h(rhValue);
                        } else {
                            h.setRh_h(null);
                        }
                    } else {
                        h.setRh_h(isValidValueForInterpolation(h.getRh()) ? h.getRh() : null);
                    }

                    if (newUcorRHValue != null && !newUcorRHValue.isNaN() && newErrRH_HValue != null && !newErrRH_HValue.isNaN()) {
                        h.setErr_rh_h(new Float(Math.sqrt(Math.pow(newUcorRHValue, 2) + Math.pow(newErrRH_HValue, 2) + Math.pow(0.005, 2))));
                    }

                    if (isValidValueForInterpolation(newUValue) && isValidValueForInterpolation(h.getU())) {
                        h.setU_h(new Float(h.getU() + newUValue));
                    } else {
                        h.setU_h(isValidValueForInterpolation(h.getU()) ? h.getU() : null);
                    }

                    if (newUcorUValue != null && !newUcorUValue.isNaN() && newErrU_HValue != null && !newErrU_HValue.isNaN()) {
                        h.setErr_u_h(new Float(Math.sqrt(Math.pow(newUcorUValue, 2) + Math.pow(newErrU_HValue, 2) + Math.pow(0.05, 2))));
                    }

                    if (isValidValueForInterpolation(newVValue) && isValidValueForInterpolation(h.getV())) {
                        h.setV_h(new Float(h.getV() + newVValue));
                    } else {
                        h.setV_h(isValidValueForInterpolation(h.getV()) ? h.getV() : null);
                    }

                    if (newUcorVValue != null && !newUcorVValue.isNaN() && newErrV_HValue != null && !newErrV_HValue.isNaN()) {
                        h.setErr_v_h(new Float(Math.sqrt(Math.pow(newUcorVValue, 2) + Math.pow(newErrV_HValue, 2) + Math.pow(0.05, 2))));
                    }

                    if (h.getU_h() != null && !h.getU_h().isNaN() && h.getV_h() != null && !h.getV_h().isNaN()) {
                        Float wspeed_h = new Float(Math.sqrt(Math.pow(h.getU_h(), 2) + Math.pow(h.getV_h(), 2)));
                        if (wspeed_h < 0.1f) {
                            wspeed_h = 0.0f;
                        }

                        h.setWspeed_h(wspeed_h);

                        Integer wdir_h = Math.round(180.0f + new Float(Math.toDegrees(Math.atan2(-h.getV_h(), -h.getU_h()))));

                        h.setWdir_h(wdir_h.floatValue());
                    } else {
                        h.setWspeed_h(null);
                        h.setWdir_h(null);
                    }

                    if (newErrWdir_HValue != null && !newErrWdir_HValue.isNaN() && h.getWdir_h() != null) {
                        if (h.getWdir_h() != null && h.getWdir_h().equals(0.0f)) {
                            newErrWdir_HValue = 0.0;
                        } else if (newErrWdir_HValue < 2.0) {
                            newErrWdir_HValue = 2.0;
                        } else if (newErrWdir_HValue > 180.0) {
                            newErrWdir_HValue = 180.0;
                        }

                        h.setErr_wdir_h(newErrWdir_HValue.floatValue());
                    } else {
                        h.setErr_wdir_h(null);
                    }

                    if (newErrWspeed_HValue != null && !newErrWspeed_HValue.isNaN() && h.getWspeed_h() != null) {
                        if (h.getWspeed_h() != null && h.getWspeed_h().equals(0.0f)) {
                            newErrWspeed_HValue = 0.0;
                        } else if (newErrWspeed_HValue < 0.15) {
                            newErrWspeed_HValue = 0.15;
                        }

                        h.setErr_wspeed_h(newErrWspeed_HValue.floatValue());
                    } else {
                        h.setErr_wspeed_h(null);
                    }
                }

                //both of mandatory and significance levels
                if (h.getTemp_h() != null && !h.getTemp_h().isNaN() && h.getDpdp() != null && !(h.getDpdp().equals(-9999.0f) || h.getDpdp().equals(-8888.0f))) {
                    h.setWvmr_h(BaronCommons.calculateWvmrFromDewpoint(h.getTemp_h() - h.getDpdp(), h.getPress()));
                } else {
                    h.setWvmr_h(null);
                }
            }

            me.setValue(harmonizedDataListPerDate);
        }

        Instant finish = Instant.now();

        setPhaseProcessingTime(StationToHarmonize.PHASE.INTERPFINAL, Duration.between(start, finish).toMillis());
    }

    private Float getUcorTatPressureLevel(List<Product> productListPerDate, Float pressure) {
        Float value = null;

        if (productListPerDate != null && productListPerDate.size() > 0) {
            List<Float> values = productListPerDate.stream().filter(x -> x.getPress().equals(pressure)).map(Product::getU_cor_temp).collect(Collectors.toList());

            if (values != null && values.size() > 0) {
                value = values.get(0);
            }
        }

        return value;
    }

    private Float getUcorRHatPressureLevel(List<Product> productListPerDate, Float pressure) {
        Float value = null;

        if (productListPerDate != null && productListPerDate.size() > 0) {
            List<Float> values = productListPerDate.stream().filter(x -> x.getPress().equals(pressure)).map(Product::getU_cor_rh).collect(Collectors.toList());

            if (values != null && values.size() > 0) {
                return values.get(0);
            }
        }

        return value;
    }

    private Float getUcorUatPressureLevel(List<Product> productListPerDate, Float pressure) {
        Float value = null;

        if (productListPerDate != null && productListPerDate.size() > 0) {
            List<Float> values = productListPerDate.stream().filter(x -> x.getPress().equals(pressure)).map(Product::getU_cor_u).collect(Collectors.toList());

            if (values != null && values.size() > 0) {
                return values.get(0);
            }
        }

        return value;
    }

    private Float getUcorVatPressureLevel(List<Product> productListPerDate, Float pressure) {
        Float value = null;

        if (productListPerDate != null && productListPerDate.size() > 0) {
            List<Float> values = productListPerDate.stream().filter(x -> x.getPress().equals(pressure)).map(Product::getU_cor_v).collect(Collectors.toList());

            if (values != null && values.size() > 0) {
                return values.get(0);
            }
        }

        return value;
    }

    private boolean isValidValueForInterpolation(Float value) {
        if (value == null || value.equals(-8888.0f) || value.equals(-9999.0f) || value.equals(Float.NaN))
            return false;
        else
            return true;
    }

    private boolean isValidValueForInterpolation(Double value) {
        if (value == null || value.equals(-8888.0) || value.equals(-9999.0) || value.equals(Double.NaN))
            return false;
        else
            return true;
    }

    private Float getValidValueForInterpolation(Float press, Float firstValue, Float secondValue) {
        Float value = 0.0f;

        boolean isEdge = press.equals(10.0) || press.equals(1000.0);

        boolean isValidFirstElement = true;
        boolean isValidSecondElement = true;
        if (firstValue == null || firstValue == -8888.0f || firstValue == -9999.0f) {
            isValidFirstElement = false;
        }
        if (secondValue == null || secondValue == -8888.0f || secondValue == -9999.0f) {
            isValidSecondElement = false;
        }

        if (isValidFirstElement && isValidSecondElement) {
            value = firstValue - secondValue;
        } else {
            if (isEdge) {
                value = 0.0f;
            } else {
                value = null;
            }
        }

        return value;
    }

    private void setHarmonizedValues() {
        log.info(station.getIdStation() + " setHarmonizedValues");

        Instant start = Instant.now();

        IntStream.range(0, press.length)
                .forEach(i -> {
                    Map<Instant, List<HarmonizedData>> harmonizedDataByPressPerDate = getHarmonizedDataByPressPerDate(press[i]);
                    Map<Instant, List<Product>> productsByPressPerDate = getProductsByPressPerDate(press[i]);

                    HashMap hm_u = new HashMap();
                    HashMap hm_u_w_φ_uncertainties = new HashMap();
                    HashMap hm_v = new HashMap();
                    HashMap hm_v_w_φ_uncertainties = new HashMap();
                    List<Instant> datelist_u = new ArrayList<>();
                    List<Instant> datelist_v = new ArrayList<>();
                    Integer year;
                    Integer month;
                    Integer day;
                    Integer hour;

                    for (int jj = 0; jj < data_YEARG_T_H.length; jj++) {
                        year = data_YEARG_T_H[jj][i];
                        month = data_MONTHG_T_H[jj][i];
                        day = data_DAYG_T_H[jj][i];
                        hour = data_HOURG_T_H[jj][i];

                        if (year != null
                                && month != null
                                && day != null
                                && hour != null) {

                            Float T_Hvalue = T_H[jj][i].floatValue();
                            Float ERR_T_Hvalue = ERR_T_H[jj][i].floatValue();
                            Float T_source = T_SOURCE[jj][i].floatValue();

                            if (T_source.equals(T_Hvalue)) {
                                ERR_T_Hvalue = null;
                            }

                            setHarmonizedValues(0, year, month, day, hour, T_Hvalue, ERR_T_Hvalue, harmonizedDataByPressPerDate);
                        }

                        year = data_YEARG_RH_H[jj][i];
                        month = data_MONTHG_RH_H[jj][i];
                        day = data_DAYG_RH_H[jj][i];
                        hour = data_HOURG_RH_H[jj][i];

                        if (year != null
                                && month != null
                                && day != null
                                && hour != null) {

                            Float RH_Hvalue = null;
                            Float ERR_RH_Hvalue = null;

                            if (RH_H[jj][i].floatValue() > 0) {
                                RH_Hvalue = RH_H[jj][i].floatValue();

                                if (RH_Hvalue < 0.0001f) {
                                    RH_Hvalue = 0.0f;
                                }

                                ERR_RH_Hvalue = ERR_RH_H[jj][i].floatValue();
                            }

                            Float RH_source = RH_SOURCE[jj][i].floatValue();

                            if (RH_source.equals(RH_Hvalue)) {
                                ERR_RH_Hvalue = null;
                            }

                            setHarmonizedValues(1, year, month, day, hour, RH_Hvalue, ERR_RH_Hvalue, harmonizedDataByPressPerDate);
                        }

                        year = data_YEARG_U_H[jj][i];
                        month = data_MONTHG_U_H[jj][i];
                        day = data_DAYG_U_H[jj][i];
                        hour = data_HOURG_U_H[jj][i];

                        if (year != null
                                && month != null
                                && day != null
                                && hour != null) {

                            Float U_Hvalue = U_H[jj][i].floatValue();
                            Float ERR_U_Hvalue = ERR_U_H[jj][i].floatValue();
                            Float U_source = U_SOURCE[jj][i].floatValue();

                            if (U_source.equals(U_Hvalue)) {
                                ERR_U_Hvalue = null;
                            }

                            Instant date = GuanDataHeader.calculateDate(year, month, day, hour, null);

                            datelist_u.add(date);
                            hm_u.put(date, U_Hvalue);
                            hm_u_w_φ_uncertainties.put(date, U_w_φ_uncertainties[jj][i].floatValue());

                            setHarmonizedValues(2, date, U_Hvalue, ERR_U_Hvalue, harmonizedDataByPressPerDate);
                        }

                        year = data_YEARG_V_H[jj][i];
                        month = data_MONTHG_V_H[jj][i];
                        day = data_DAYG_V_H[jj][i];
                        hour = data_HOURG_V_H[jj][i];

                        if (year != null
                                && month != null
                                && day != null
                                && hour != null) {

                            Float V_Hvalue = V_H[jj][i].floatValue();
                            Float ERR_V_Hvalue = ERR_V_H[jj][i].floatValue();
                            Float V_source = V_SOURCE[jj][i].floatValue();

                            if (V_source.equals(V_Hvalue)) {
                                ERR_V_Hvalue = null;
                            }

                            Instant date = GuanDataHeader.calculateDate(year, month, day, hour, null);

                            datelist_v.add(date);
                            hm_v.put(date, V_Hvalue);
                            hm_v_w_φ_uncertainties.put(date, V_w_φ_uncertainties[jj][i].floatValue());

                            setHarmonizedValues(3, date, V_Hvalue, ERR_V_Hvalue, harmonizedDataByPressPerDate);
                        }
                    }

                    List<Instant> dates_commons_u_v = new ArrayList<>(datelist_u);
                    dates_commons_u_v.retainAll(datelist_v);

                    for (Instant date : dates_commons_u_v) {
                        List<HarmonizedData> harmonizedDataListFounded = harmonizedDataByPressPerDate.get(date);

                        if (harmonizedDataListFounded != null) {
                            HarmonizedData hData = harmonizedDataListFounded.get(0);

                            Float u = (Float) hm_u.get(date);
                            Float v = (Float) hm_v.get(date);
                            Float wspeed_h = new Float(Math.sqrt(Math.pow(u, 2) + Math.pow(v, 2)));
                            Integer wdir_h = Math.round(180.0f + new Float(Math.toDegrees(Math.atan2(-v, -u))));

                            if (wspeed_h < 0.1f) {
                                wspeed_h = 0.0f;
                            }

                            hData.setWspeed_h(wspeed_h);
                            hData.setWdir_h(wdir_h.floatValue());

                            if (wspeed_h.equals(0.0f)) {
                                hData.setErr_wspeed_h(0.0f);
                            }
                            if (wdir_h.equals(0.0f)) {
                                hData.setErr_wdir_h(0.0f);
                            }

                            LocalDateTime localDateTime = LocalDateTime.ofInstant(date, ZoneId.of("UTC"));
                            year = localDateTime.getYear();
                            month = localDateTime.getMonthValue();

                            List<HarmonizationSigmaU_V> harmonizationSigmaUVListFiltered = getHarmonizationSigmaUV(harmonizationSigmaUVList, year, month, press[i]);

                            List<HarmonizationSigmaU_V> harmonizationSigmaU = harmonizationSigmaUVListFiltered.stream()
                                    .filter(x -> x.getEcv().equals(ECV.U))
                                    .collect(Collectors.toList());

                            List<HarmonizationSigmaU_V> harmonizationSigmaV = harmonizationSigmaUVListFiltered.stream()
                                    .filter(x -> x.getEcv().equals(ECV.V))
                                    .collect(Collectors.toList());

                            List<Double> u_data_rma_scalar_1_list = new ArrayList<>();
                            harmonizationSigmaU.forEach(x -> u_data_rma_scalar_1_list.addAll(Arrays.asList(x.getData_rms_scalar_1())));
                            double[] u_data_rma_scalar_1 = ArrayUtils.toPrimitive(u_data_rma_scalar_1_list.toArray(new Double[u_data_rma_scalar_1_list.size()]));

                            List<Double> v_data_rma_scalar_1_list = new ArrayList<>();
                            harmonizationSigmaV.forEach(x -> v_data_rma_scalar_1_list.addAll(Arrays.asList(x.getData_rms_scalar_1())));
                            double[] v_data_rma_scalar_1 = ArrayUtils.toPrimitive(v_data_rma_scalar_1_list.toArray(new Double[v_data_rma_scalar_1_list.size()]));

                            Float u_w_φ_uncertainties = Math.abs((Float) hm_u_w_φ_uncertainties.get(date));
                            Float v_w_φ_uncertainties = Math.abs((Float) hm_v_w_φ_uncertainties.get(date));

                            Double err_u_h;
                            Double err_v_h;

                            List<Product> productList = productsByPressPerDate.get(date);
                            Product product;
                            if (productList != null) {
                                product = productList.get(0);

                                err_u_h = product.getU_cor_u() != null ? product.getU_cor_u().doubleValue() : null;
                                err_v_h = product.getU_cor_v() != null ? product.getU_cor_v().doubleValue() : null;
                            } else {
                                err_u_h = hData.getErr_u_h() != null ? hData.getErr_u_h().doubleValue() : null;
                                err_v_h = hData.getErr_v_h() != null ? hData.getErr_v_h().doubleValue() : null;
                            }

                            //err_wspeed_h and err_wdir_h calculation for mandatory levels
                            if (u_w_φ_uncertainties != null && v_w_φ_uncertainties != null
                                    && err_u_h != null && err_v_h != null
                                    && u_data_rma_scalar_1.length > 0
                                    && v_data_rma_scalar_1.length > 0) {
                                Float err_wspeed_h = null;
                                Float err_wdir_h = null;

                                if (u_w_φ_uncertainties.equals(0.0f) && v_w_φ_uncertainties.equals(0.0f)) {
                                    hData.setWspeed_h(0.0f);
                                    hData.setErr_wspeed_h(0.0f);
                                    hData.setWdir_h(null);
                                    hData.setErr_wdir_h(null);
                                } else {
                                    PearsonsCorrelation p = new PearsonsCorrelation();
                                    Double sigma_u_v = p.correlation(u_data_rma_scalar_1, v_data_rma_scalar_1);

                                    Double eq_err_wspeed_h_1st_el = (Math.pow(err_u_h.doubleValue(), 2)) * (Math.pow(u_w_φ_uncertainties, 2) / (Math.pow(Math.pow(u_w_φ_uncertainties, 2) + Math.pow(v_w_φ_uncertainties, 2), 2)));
                                    Double eq_err_wspeed_h_2nd_el = (Math.pow(err_v_h.doubleValue(), 2)) * (Math.pow(v_w_φ_uncertainties, 2) / (Math.pow(Math.pow(u_w_φ_uncertainties, 2) + Math.pow(v_w_φ_uncertainties, 2), 2)));
                                    Double eq_err_wspeed_h_3rd_el = 2 * ((u_w_φ_uncertainties * v_w_φ_uncertainties) / (Math.pow(Math.pow(u_w_φ_uncertainties, 2) + Math.pow(v_w_φ_uncertainties, 2), 2))) * sigma_u_v;

                                    err_wspeed_h = new Double(2 * Math.sqrt(eq_err_wspeed_h_1st_el + eq_err_wspeed_h_2nd_el + eq_err_wspeed_h_3rd_el)).floatValue();

                                    if (err_wspeed_h < 0.15f) {
                                        err_wspeed_h = 0.15f;
                                    }

                                    if (hData.getErr_wspeed_h() == null) {
                                        hData.setErr_wspeed_h(err_wspeed_h);
                                    }

                                    Double eq_err_wdir_h_1st_el = Math.pow(v_w_φ_uncertainties / (Math.pow(u_w_φ_uncertainties, 2) + Math.pow(v_w_φ_uncertainties, 2)), 2) * Math.pow(err_u_h, 2);
                                    Double eq_err_wdir_h_2nd_el = Math.pow(u_w_φ_uncertainties / (Math.pow(u_w_φ_uncertainties, 2) + Math.pow(v_w_φ_uncertainties, 2)), 2) * Math.pow(err_v_h, 2);
                                    Double eq_err_wdir_h_3rd_el = 2 * ((u_w_φ_uncertainties * v_w_φ_uncertainties) / (Math.pow((Math.pow(u_w_φ_uncertainties, 2) + Math.pow(v_w_φ_uncertainties, 2)), 2))) * sigma_u_v;

                                    err_wdir_h = new Double((180 / Math.PI) * (Math.sqrt(eq_err_wdir_h_1st_el + eq_err_wdir_h_2nd_el - eq_err_wdir_h_3rd_el))).floatValue();

                                    if (err_wdir_h < 2.0f) {
                                        err_wdir_h = 2.0f;
                                    }
                                    if (err_wdir_h > 180.0f) {
                                        err_wdir_h = 180.0f;
                                    }

                                    if (hData.getErr_wdir_h() == null) {
                                        hData.setErr_wdir_h(err_wdir_h);
                                    }
                                }
                            }
                        }
                    }

                    log.info(station.getIdStation() + "Add the values of the product - press: " + press[i]);

                    productsByPressPerDate.entrySet()
                            .parallelStream()
                            .forEach(me -> {
                                Product product = me.getValue().get(0);

                                HarmonizedData harmonizedData = harmonizedDataByPressPerDate.get(product.getDateOfObservation()).get(0);

                                updateHarmonizedDataWithProductValues(harmonizedData, product, true);
                            });
                });

        Instant finish = Instant.now();

        setPhaseProcessingTime(StationToHarmonize.PHASE.ENDSETH, Duration.between(start, finish).toMillis());
    }

    private void updateHarmonizedDataWithProductValues(HarmonizedData harmonizedData, Product product, Boolean isHarmonizable) {
        log.info(station.getIdStation() + " updateHarmonizedDataWithProductValues");

        //restore the values of the products in the harmonized data
        harmonizedData.setTemp_h(product.getTemp());
        harmonizedData.setRh_h(product.getRh());
        harmonizedData.setU_h(product.getU());
        harmonizedData.setV_h(product.getV());

        //restore the error values in the harmonized data
        harmonizedData.setErr_temp_h(product.getU_cor_temp());
        harmonizedData.setErr_rh_h(product.getU_cor_rh());
        harmonizedData.setErr_u_h(product.getU_cor_u());
        harmonizedData.setErr_v_h(product.getU_cor_v());

        //set the values of the product
        harmonizedData.setTemp_product(product.getTemp());
        harmonizedData.setTemp_product_cor_temp(product.getCor_temp());
        harmonizedData.setTemp_product_u_cor_temp(product.getU_cor_temp());
        harmonizedData.setTemp_product_cor_temp_tl(product.getCor_temp_tl());
        harmonizedData.setTemp_product_u_cor_temp_tl(product.getU_cor_temp_tl());
        harmonizedData.setTemp_product_cor_intercomparison_temp(product.getCor_intercomparison_temp());
        harmonizedData.setTemp_product_u_cor_intercomparison_temp(product.getU_cor_intercomparison_temp());

        harmonizedData.setRh_product(product.getRh());
        harmonizedData.setRh_product_cor_rh(product.getCor_rh());
        harmonizedData.setRh_product_u_cor_rh(product.getU_cor_rh());
        harmonizedData.setRh_product_cor_rh_tl(product.getCor_rh_tl());
        harmonizedData.setRh_product_u_cor_rh_tl(product.getU_cor_rh_tl());
        harmonizedData.setRh_product_cor_intercomparison_rh(product.getCor_intercomparison_rh());
        harmonizedData.setRh_product_u_cor_intercomparison_rh(product.getU_cor_intercomparison_rh());

        harmonizedData.setU_product(product.getU());
        harmonizedData.setU_product_cor_u(product.getCor_u());
        harmonizedData.setU_product_u_cor_u(product.getU_cor_u());
        harmonizedData.setU_product_cor_u_rs92(product.getCor_u_rs92());
        harmonizedData.setU_product_u_cor_u_rs92(product.getU_cor_u_rs92());
        harmonizedData.setU_product_cor_u_notRs92(product.getCor_u_notRs92());
        harmonizedData.setU_product_u_cor_u_notRs92(product.getU_cor_u_notRs92());

        harmonizedData.setV_product(product.getV());
        harmonizedData.setV_product_cor_v(product.getCor_v());
        harmonizedData.setV_product_u_cor_v(product.getU_cor_v());
        harmonizedData.setV_product_cor_v_rs92(product.getCor_v_rs92());
        harmonizedData.setV_product_u_cor_v_rs92(product.getU_cor_v_rs92());
        harmonizedData.setV_product_cor_v_notRs92(product.getCor_v_notRs92());
        harmonizedData.setV_product_u_cor_v_notRs92(product.getU_cor_v_notRs92());

        harmonizedData.setWvmr_product(product.getWvmr());

        harmonizedData.setSza(product.getSzaIdl());

        if (!isHarmonizable) {
            harmonizedData.setWdir_h(product.getWdir());
            harmonizedData.setWspeed_h(product.getWspeed());
        }
    }

    private List<HarmonizationSigmaU_V> getHarmonizationSigmaUV(List<HarmonizationSigmaU_V> harmonizationSigmaUVList, Integer year, Integer month, Double press) {
        return harmonizationSigmaUVList.stream()
                .filter(x -> x.getPress().equals(press)
                        && x.getYear().equals(year)
                        && x.getMonth().equals(month))
                .collect(Collectors.toList());
    }

    public void setHarmonizedValues(Integer ECVREF, Instant date,
                                    Float ECVREF_H, Float ERR_ECVREF_H,
                                    Map<Instant, List<HarmonizedData>> harmonizedDataByPressPerDate) {

        List<HarmonizedData> harmonizedDataListFounded = harmonizedDataByPressPerDate.get(date);
        if (harmonizedDataListFounded != null) {
            HarmonizedData harmonizedData = harmonizedDataListFounded.get(0);

            switch (ECVREF) {
                case 0:
                    harmonizedData.setTemp_h(ECVREF_H);
                    harmonizedData.setErr_temp_h(ERR_ECVREF_H);
                    break;
                case 1:
                    harmonizedData.setRh_h(ECVREF_H);
                    harmonizedData.setErr_rh_h(ERR_ECVREF_H);
                    break;
                case 2:
                    harmonizedData.setU_h(ECVREF_H);
                    harmonizedData.setErr_u_h(ERR_ECVREF_H);
                    break;
                case 3:
                    harmonizedData.setV_h(ECVREF_H);
                    harmonizedData.setErr_v_h(ERR_ECVREF_H);
                    break;
            }
        }
    }

    public void setHarmonizedValues(Integer ECVREF, Integer year, Integer month, Integer day, Integer hour,
                                    Float ECVREF_H, Float ERR_ECVREF_H,
                                    Map<Instant, List<HarmonizedData>> harmonizedDataByPressPerDate) {
        Instant dateToCheck = GuanDataHeader.calculateDate(year, month, day, hour, null);

        setHarmonizedValues(ECVREF, dateToCheck, ECVREF_H, ERR_ECVREF_H,
                harmonizedDataByPressPerDate);
    }

    private Map<Instant, List<HarmonizedData>> getHarmonizedDataByPressPerDate(Double pressure) {
        List<HarmonizedData> harmonizedDataListByPress = harmonizedDataList.stream().filter(x -> new Double(x.getPress()).equals(pressure)).collect(Collectors.toList());
        return harmonizedDataListByPress.stream().collect(groupingBy(HarmonizedData::getDateOfObservation));
    }

    private Map<Instant, List<Product>> getProductsByPressPerDate(Double pressure) {
        List<Product> productsByPress = productMandatoryLevels.stream().filter(x -> new Double(x.getPress()).equals(pressure)).collect(Collectors.toList());
        return productsByPress.stream().collect(groupingBy(Product::getDateOfObservation));
    }

    private void harmonize(Object[] igraData) {
        log.info(station.getIdStation() + " harmonize");

        Instant start = Instant.now();

        List<Product> productsByPress;

        Integer ZENREF = 0; //this variable is to separate daytime (0) from night time data (1)

        String[] LABEL1 = (String[]) igraData[0];
        Double[][] pressure = (Double[][]) igraData[1];
        Double[][] TEMPG1 = (Double[][]) igraData[2];
        Double[][] RHG1 = (Double[][]) igraData[3];
        Integer[] YEARG1 = (Integer[]) igraData[4];
        Integer[] MONTHG1 = (Integer[]) igraData[5];
        Integer[] DAYG1 = (Integer[]) igraData[6];
        Integer[] HOURG1 = (Integer[]) igraData[7];
        Double[] ZENITHSUN1 = (Double[]) igraData[8];
        Double[][] WDIR = (Double[][]) igraData[9];
        Double[][] WSPD = (Double[][]) igraData[10];

        Double[] data4 = null;
        Double[] data5 = null;
        Double[] data6 = null;
        Double[] data7 = null;
        Double[] data8 = null;
        Double[] data9 = null;
        Integer[] data10 = null;
        Integer[] data11 = null;
        Integer[] data12 = null;
        Integer[] data13 = null;
        Integer[] data14 = null;
        Integer[] data15 = null;
        Integer[] data16 = null;
        Integer[] data17 = null;
        Double[] data18 = null;
        Double[] data19 = null;
        Integer[] data20 = null;
        Integer[] data21 = null;
        Double[] data22 = null;
        Double[] data23 = null;
        Double[] data24 = null;
        Double[] data25 = null;

        Double[] PRESS = null;
        Double[] TEMPG = null;
        Double[] ERR_TEMPG = null;
        Double[] RHG = null;
        Double[] ERR_RHG = null;
        Integer[] YEARG = null;
        Integer[] MONTHG = null;
        Integer[] DAYG = null;
        Integer[] HOURG = null;
        Double[] ZENITHSUN = null;
        String[] LABEL = null;

        Double devm1 = 0.0;
        Double devm2 = 0.0;

        double delta = 0.2d;
        double widthLowess = 0.3d;

        //******** the variable xyzw only defined the ECVs to process: temperature (0), RH (1), meridional wind (2), zonal wind (3)
        for (int xyzw = 0; xyzw < 4; xyzw++) {
            int ECVREF = xyzw;

            log.info(station.getIdStation() + " ECVREF: " + ECVREF);

            switch (ECVREF) {
                case 1:
                    widthLowess = 0.2d;

                    //this equivalence between the variable is applied to process all the variables using the same dataflow.
                    TEMPG1 = ArrayCopy.copyOfRange(RHG1, 0, RHG1.length);

                    //this equivalence between the variable is applied to process all the variables using the same dataflow.
                    ERR_TEMPG1 = ArrayCopy.copyOfRange(ERR_RHG1, 0, ERR_RHG1.length);

                    break;
                case 2:
                    widthLowess = 0.3d;

                    for (int i = 0; i < WSPD.length; i++) {
                        for (int ii = 0; ii < WSPD[i].length; ii++) {
                            //this equivalence between the variable is applied to process all the variables using the same dataflow.
                            TEMPG1[i][ii] = BaronCommons.calculateU(WSPD[i][ii].floatValue(), WDIR[i][ii].floatValue()).doubleValue();
                        }
                    }

                    break;
                case 3:
                    widthLowess = 0.3d;

                    for (int i = 0; i < WSPD.length; i++) {
                        for (int ii = 0; ii < WSPD[i].length; ii++) {
                            //this equivalence between the variable is applied to process all the variables using the same dataflow.
                            // Wind speed and direction are converted to wind zonal and meridional components with this line and the next
                            TEMPG1[i][ii] = BaronCommons.calculateV(WSPD[i][ii].floatValue(), WDIR[i][ii].floatValue()).doubleValue();
                        }
                    }

                    break;
            }

            //loop on the mandatory pressure levels
            for (int j = 0; j < press.length; j++) {
                data4 = null;
                data5 = null;
                data6 = null;
                data7 = null;
                data8 = null;
                data9 = null;
                data10 = null;
                data11 = null;
                data12 = null;
                data13 = null;
                data14 = null;
                data15 = null;
                data16 = null;
                data17 = null;
                data18 = null;
                data19 = null;
                data22 = null;
                data23 = null;

                log.info(" " + press[j]);

                productsByPress = getProductByPress(press[j]);
                productsByPress.sort(Comparator.comparing(Product::getDateOfObservation));

                List<Double> julianDatesProduct = productsByPress.stream()
                        .map(yy -> getJulianDate(yy.getDateOfObservation()))
                        .distinct()
                        .collect(Collectors.toList());

                if (julianDatesProduct.size() == 0)
                    continue;

                //loop on daytime and night time data
                for (int xy = 0; xy < 2; xy++) {
                    ZENREF = xy;

                    if (ZENREF == 0) {
                        log.info("[☼");
                    } else {
                        log.info("☾]");
                    }

                    PRESS = new Double[YEARG1.length];
                    TEMPG = new Double[YEARG1.length];
                    ERR_TEMPG = new Double[YEARG1.length];
                    RHG = new Double[YEARG1.length];
                    ERR_RHG = new Double[YEARG1.length];
                    YEARG = new Integer[YEARG1.length];
                    MONTHG = new Integer[YEARG1.length];
                    DAYG = new Integer[YEARG1.length];
                    HOURG = new Integer[YEARG1.length];
                    ZENITHSUN = new Double[YEARG1.length];
                    LABEL = new String[YEARG1.length];
                    Double[] data = null;

                    int x = 0;

                    for (int i = 0; i < pressure.length; i++) {
                        for (int k = 0; k < pressure[i].length; k++) {
                            //****** filtering options for T and RH daytime, T must be less than 360K, RH higher than 0, and launch time around noon and midnight  *****
                            if (pressure[i][k] != null && pressure[i][k].equals(press[j])
                                    && ECVREF <= 1
                                    && TEMPG1[i][k] != null
                                    && TEMPG1[i][k] >= 0.0
                                    && TEMPG1[i][k] <= 360.0
                                    && ZENREF == 0
                                    && ZENITHSUN1[i] < 95.0
                                    && (HOURG1[i] == 0
                                    || HOURG1[i] == 11
                                    || HOURG1[i] == 12
                                    || HOURG1[i] == 23)) {

                                PRESS[x] = pressure[i][k];
                                TEMPG[x] = TEMPG1[i][k];
                                ERR_TEMPG[x] = ERR_TEMPG1[i][k];
                                RHG[x] = RHG1[i][k];
                                ERR_RHG[x] = ERR_RHG1[i][k];

                                YEARG[x] = YEARG1[i];
                                MONTHG[x] = MONTHG1[i];
                                DAYG[x] = DAYG1[i];
                                HOURG[x] = HOURG1[i];
                                ZENITHSUN[x] = ZENITHSUN1[i];
                                LABEL[x] = LABEL1[i];

                                x++;
                            }

                            //****** filtering options for T and RH nighttime, T must be less than 360K, RH higher than 0, and launch time around noon and midnight  *****
                            if (pressure[i][k] != null && pressure[i][k].equals(press[j])
                                    && ECVREF <= 1
                                    && TEMPG1[i][k] != null
                                    && TEMPG1[i][k] >= 0.0
                                    && TEMPG1[i][k] <= 360.0
                                    && ZENREF == 1
                                    && ZENITHSUN1[i] >= 95.0
                                    && (HOURG1[i] == 0
                                    || HOURG1[i] == 11
                                    || HOURG1[i] == 12
                                    || HOURG1[i] == 23)) {

                                PRESS[x] = pressure[i][k];
                                TEMPG[x] = TEMPG1[i][k];
                                ERR_TEMPG[x] = ERR_TEMPG1[i][k];
                                RHG[x] = RHG1[i][k];
                                ERR_RHG[x] = ERR_RHG1[i][k];

                                YEARG[x] = YEARG1[i];
                                MONTHG[x] = MONTHG1[i];
                                DAYG[x] = DAYG1[i];
                                HOURG[x] = HOURG1[i];
                                ZENITHSUN[x] = ZENITHSUN1[i];
                                LABEL[x] = LABEL1[i];

                                x++;
                            }

                            //****** filtering options for wind speed and direction in daytime conditions, and launch time around noon and midnight  *****
                            if (pressure[i][k] != null && pressure[i][k].equals(press[j])
                                    && ECVREF > 1
                                    && WDIR[i][k] > -8888
                                    && WSPD[i][k] > -8888.0
                                    && ZENREF == 0
                                    && ZENITHSUN1[i] < 95.0
                                    && (HOURG1[i] == 0
                                    || HOURG1[i] == 11
                                    || HOURG1[i] == 12
                                    || HOURG1[i] == 23)) {

                                PRESS[x] = pressure[i][k];
                                TEMPG[x] = TEMPG1[i][k];
                                ERR_TEMPG[x] = ERR_TEMPG1[i][k];
                                RHG[x] = RHG1[i][k];
                                ERR_RHG[x] = ERR_RHG1[i][k];

                                YEARG[x] = YEARG1[i];
                                MONTHG[x] = MONTHG1[i];
                                DAYG[x] = DAYG1[i];
                                HOURG[x] = HOURG1[i];
                                ZENITHSUN[x] = ZENITHSUN1[i];
                                LABEL[x] = LABEL1[i];

                                x++;
                            }

                            //****** filtering options for wind speed and direction in night time conditions, and launch time around noon and midnight  *****
                            if (pressure[i][k] != null && pressure[i][k].equals(press[j])
                                    && ECVREF > 1
                                    && WDIR[i][k] > -8888
                                    && WSPD[i][k] > -8888.0
                                    && ZENREF == 1
                                    && ZENITHSUN1[i] >= 95.0
                                    && (HOURG1[i] == 0
                                    || HOURG1[i] == 11
                                    || HOURG1[i] == 12
                                    || HOURG1[i] == 23)) {

                                PRESS[x] = pressure[i][k];
                                TEMPG[x] = TEMPG1[i][k];
                                ERR_TEMPG[x] = ERR_TEMPG1[i][k];
                                RHG[x] = RHG1[i][k];
                                ERR_RHG[x] = ERR_RHG1[i][k];

                                YEARG[x] = YEARG1[i];
                                MONTHG[x] = MONTHG1[i];
                                DAYG[x] = DAYG1[i];
                                HOURG[x] = HOURG1[i];
                                ZENITHSUN[x] = ZENITHSUN1[i];
                                LABEL[x] = LABEL1[i];

                                x++;
                            }
                        }
                    }

                    switch (ECVREF) {
                        case 0:
                            //****** additional filter on temperature physically plausible values, and removes zero from the array *****
                            List<Integer> listIndices_goodData_T = new ArrayList<>();

                            List<Integer> listIndices_TEMPG_GE_170 = IdlCommonFunctions.where(TEMPG, IdlCommonFunctions.IDL_OPERATOR_GE, 170.0);
                            List<Integer> listIndices_TEMPG_LE_350 = IdlCommonFunctions.where(TEMPG, IdlCommonFunctions.IDL_OPERATOR_LE, 350.0);

                            for (Integer idx : listIndices_TEMPG_GE_170) {
                                if (listIndices_TEMPG_LE_350.contains(idx)) {
                                    listIndices_goodData_T.add(idx);
                                }
                            }

                            data = IdlCommonFunctions.getValuesByIndices_Double(TEMPG, listIndices_goodData_T);

                            YEARG = IdlCommonFunctions.getValuesByIndices_Integer(YEARG, listIndices_goodData_T);
                            MONTHG = IdlCommonFunctions.getValuesByIndices_Integer(MONTHG, listIndices_goodData_T);
                            DAYG = IdlCommonFunctions.getValuesByIndices_Integer(DAYG, listIndices_goodData_T);
                            HOURG = IdlCommonFunctions.getValuesByIndices_Integer(HOURG, listIndices_goodData_T);
                            TEMPG = IdlCommonFunctions.getValuesByIndices_Double(TEMPG, listIndices_goodData_T);
                            break;
                        case 1:
                            //****** additional filter on humidity physically plausible values, and removes zero from the array *****
                            List<Integer> listIndices_goodData_RH = new ArrayList<>();
                            List<Integer> listIndices_TEMPG_GE_0_01 = IdlCommonFunctions.where(TEMPG, IdlCommonFunctions.IDL_OPERATOR_GE, 0.01);
                            List<Integer> listIndices_TEMPG_LE_1_0 = IdlCommonFunctions.where(TEMPG, IdlCommonFunctions.IDL_OPERATOR_LE, 1.0);
                            List<Integer> listIndices_PRESS_GE_250_0 = IdlCommonFunctions.where(PRESS, IdlCommonFunctions.IDL_OPERATOR_GE, 250.0);

                            for (Integer idx : listIndices_TEMPG_GE_0_01) {
                                if (listIndices_TEMPG_LE_1_0.contains(idx) && listIndices_PRESS_GE_250_0.contains(idx)) {
                                    listIndices_goodData_RH.add(idx);
                                }
                            }

                            data = IdlCommonFunctions.getValuesByIndices_Double(TEMPG, listIndices_goodData_RH);

                            YEARG = IdlCommonFunctions.getValuesByIndices_Integer(YEARG, listIndices_goodData_RH);
                            MONTHG = IdlCommonFunctions.getValuesByIndices_Integer(MONTHG, listIndices_goodData_RH);
                            DAYG = IdlCommonFunctions.getValuesByIndices_Integer(DAYG, listIndices_goodData_RH);
                            HOURG = IdlCommonFunctions.getValuesByIndices_Integer(HOURG, listIndices_goodData_RH);
                            TEMPG = IdlCommonFunctions.getValuesByIndices_Double(TEMPG, listIndices_goodData_RH);

                            break;
                        case 2:
                        case 3:
                            //****** additional filter on wind data, only further removes zero from the array *****
                            List<Integer> listIndices_goodData_WDIR = IdlCommonFunctions.where(YEARG, IdlCommonFunctions.IDL_OPERATOR_GT, 0);

                            data = IdlCommonFunctions.getValuesByIndices_Double(TEMPG, listIndices_goodData_WDIR);

                            YEARG = IdlCommonFunctions.getValuesByIndices_Integer(YEARG, listIndices_goodData_WDIR);
                            MONTHG = IdlCommonFunctions.getValuesByIndices_Integer(MONTHG, listIndices_goodData_WDIR);
                            DAYG = IdlCommonFunctions.getValuesByIndices_Integer(DAYG, listIndices_goodData_WDIR);
                            HOURG = IdlCommonFunctions.getValuesByIndices_Integer(HOURG, listIndices_goodData_WDIR);
                            TEMPG = IdlCommonFunctions.getValuesByIndices_Double(TEMPG, listIndices_goodData_WDIR);

                            break;
                    }

                    if (data.length == 0) {
                        continue;
                    }

                    Double[] TIME = new Double[YEARG.length];

                    //defines the TIME variable from the input data
                    for (int i = 0; i < TIME.length; i++) {
                        TIME[i] = AstronomicUtility.calculateJulianDay(YEARG[i], MONTHG[i], DAYG[i], HOURG[i], 0, 0, 0);
                    }

                    List<Integer> datesPriorToProducts = IdlCommonFunctions.where(TIME, IdlCommonFunctions.IDL_OPERATOR_LE, Collections.min(julianDatesProduct));
                    if (datesPriorToProducts.size() == 0)
                        continue;

                    int beginProducts = Collections.max(datesPriorToProducts);

                    //Insert the values of the product - START
                    Double[] igraJulianDates = new Double[YEARG.length];

                    for (int i = 0; i < igraJulianDates.length; i++) {
                        if (!(YEARG[i] == null || MONTHG[i] == null || DAYG[i] == null || HOURG[i] == null)) {
                            igraJulianDates[i] = AstronomicUtility.calculateJulianDay(YEARG[i], MONTHG[i], DAYG[i], HOURG[i], 0, 0, 0);
                        }
                    }

                    List<Double> julianDates = Arrays.asList(igraJulianDates);
                    List<Double> julianDatesProductFounded = julianDatesProduct.stream().filter(xx -> julianDates.contains(xx)).collect(Collectors.toList());

                    int idxProduct;
                    int idxData;
                    for (Double julianDate : julianDatesProductFounded) {
                        idxProduct = julianDatesProduct.indexOf(julianDate);
                        idxData = julianDates.indexOf(julianDate);

                        switch (ECVREF) {
                            case 0:
                                data[idxData] = new Double(productsByPress.get(idxProduct).getTemp());

                                break;
                            case 1:
                                data[idxData] = new Double(productsByPress.get(idxProduct).getRh());

                                break;
                            case 2:
                                data[idxData] = new Double(productsByPress.get(idxProduct).getU());

                                break;
                            case 3:
                                data[idxData] = new Double(productsByPress.get(idxProduct).getV());

                                break;
                        }
                    }
                    //Insert the values of the product - END

                    Double[] data2 = ArrayCopy.copyOf(data, data.length);
                    Integer nx = 365;

                    Double[] ysmoo = null;
                    Double[] ysmoo1 = null;

                    //calculate the LOWESS for each ECV to smooth only the random noise using a small time window chosen to get residual
                    //as much close as possible to the GRUAN measurement uncertainties
                    ysmoo = ArrayUtils.toObject(IdlCommonFunctions.lowess(ArrayUtils.toPrimitive(TIME), ArrayUtils.toPrimitive(data), widthLowess * data.length, 2));
                    double epsilon = 0.0d;

                    switch (ECVREF) {
                        case 0:
                            epsilon = IdlCommonFunctions.mad(productsByPress.stream()
                                    .filter(product -> product.getU_cor_temp() != null)
                                    .mapToDouble(Product::getU_cor_temp)
                                    .boxed().toArray(Double[]::new));

                            break;
                        case 1:
                            epsilon = IdlCommonFunctions.mad(productsByPress.stream()
                                    .filter(product -> product.getU_cor_rh() != null)
                                    .mapToDouble(Product::getU_cor_rh)
                                    .boxed().toArray(Double[]::new));
                            break;
                        case 2:
                            epsilon = IdlCommonFunctions.mad(productsByPress.stream()
                                    .filter(product -> product.getU_cor_u() != null)
                                    .mapToDouble(Product::getU_cor_u)
                                    .boxed().toArray(Double[]::new));

                            break;
                        case 3:
                            epsilon = IdlCommonFunctions.mad(productsByPress.stream()
                                    .filter(product -> product.getU_cor_v() != null)
                                    .mapToDouble(Product::getU_cor_v)
                                    .boxed().toArray(Double[]::new));

                            break;
                    }

                    int dd = 3;
                    for (; dd <= 100; dd++) {
                        //calculate the LOWESS for each ECV to smooth only the random noise using a small time window chosen to get residual
                        // as much close as possible to the GRUAN measurement uncertainties
                        ysmoo1 = ArrayUtils.toObject(IdlCommonFunctions.lowess(ArrayUtils.toPrimitive(TIME), ArrayUtils.toPrimitive(data), dd, 2));

                        Double[] firstElement = ArrayCopy.copyOfRange(ysmoo1, data.length - beginProducts, data.length);
                        Double[] secondElement = ArrayCopy.copyOfRange(data, data.length - beginProducts, data.length);

                        Double model_err = IdlCommonFunctions.stdDev(IdlCommonFunctions.arrMinus(firstElement, secondElement));

                        if (model_err > epsilon) {
                            break;
                        }
                    }

                    //calculate the residual removing the seasonality
                    data2 = IdlCommonFunctions.arrMinus(data2, ysmoo);

                    //defines the vector for the harmonized data
                    Double[] data3;
                    Double[] data3_denorm;

                    //defines the vector for the harmonized data uncertainty
                    Double[] u_data3 = new Double[data.length];

                    //defines the vectors for the calculation of err_wdir_h e err_vspeed_h
                    Double[] data_rms_scalar_mean_ECV = new Double[data.length];
                    Double[] sigma_u_v = new Double[data.length];

                    Arrays.fill(u_data3, 0.0);

                    //defines a few vector and array for the calculation of uncertainties in the following loop
                    //removing the ysmoo1 and keeping only the random noise in the signal
                    Double m1 = StatUtils.mean(ArrayUtils.toPrimitive(data));
                    double sigma1 = IdlCommonFunctions.mad(ArrayUtils.toPrimitive(data));

                    int p = 0;
                    int y = 0;

                    Integer maxYearG = Collections.max(Arrays.asList(YEARG));
                    Double[][] data_monthly_IGRA = new Double[12][maxYearG + 1];
                    for (int i = 0; i < data_monthly_IGRA.length; i++) {
                        Arrays.fill(data_monthly_IGRA[i], 0.0);
                    }

                    Double[][] data_monthly_T_IGRA = new Double[12][maxYearG + 1];
                    Double[][] rms_scalar = new Double[12][maxYearG + 1];
                    for (int i = 0; i < rms_scalar.length; i++) {
                        Arrays.fill(rms_scalar[i], 0.0);
                    }
                    Double[][] data_rms_scalar_mean = new Double[12][maxYearG + 1];

                    Double[] rms_data_scalar;
                    Double[] data_rms_scalar;
                    Double[] data_rms_scalar_1;
                    Double[] rms;
                    Double[] rms1;
                    Double[] rms_data;

                    //this loop calculates residuals with respect to the two LOWESS smoothin applied in ysmoo and ysmoo1 vectors, the first is not more in sue in this version the second is used
                    //to estimate the uncertainties for the historical part of the time series, where the GRUAN-like data processing within this algorithm cannot be applied
                    for (int g = 1977; g < maxYearG + 2; g++) {
                        for (int jk = 0; jk < 12; jk++) {
                            x = 0;

                            rms = new Double[200];

                            Arrays.fill(rms, 1.0e+6);

                            rms1 = new Double[200];
                            rms_data = new Double[200];
                            data_rms_scalar = new Double[200];

                            int index_i = 0;
                            for (int i = 0; i < TIME.length; i++) {
                                if (YEARG[i] == g && MONTHG[i] == (jk + 1)) {
                                    rms[x] = Math.abs(data2[i]);
                                    rms_data[x] = data[i] - ysmoo1[i];
                                    data_rms_scalar[x] = data[i];
                                    x += 1;
                                    index_i = i;
                                }
                            }

                            //filtering is applied in many step to remove data initialized at certain values of later on to remove outliers
                            //from the mean and stddev using MAD test
                            List<Integer> listIndices_rms_LT_1_0E6 = IdlCommonFunctions.where(rms, IdlCommonFunctions.IDL_OPERATOR_LT, 1.0E+6);
                            rms1 = IdlCommonFunctions.getValuesByIndices_Double(rms, listIndices_rms_LT_1_0E6);
                            rms_data_scalar = IdlCommonFunctions.getValuesByIndices_Double(rms_data, listIndices_rms_LT_1_0E6);
                            data_rms_scalar_1 = IdlCommonFunctions.getValuesByIndices_Double(data_rms_scalar, listIndices_rms_LT_1_0E6);

                            Double[] absdev;
                            double sigma;
                            double sigma_data;
                            Double[] rms1_m;
                            Double[] rms2;

                            //cannot work below 15 elements, the uncertainty has not enough statitiscs to be estimated
                            if (rms1.length >= 15 && listIndices_rms_LT_1_0E6.get(0) >= 0) {
                                Double m = IdlCommonFunctions.median(rms1);

                                absdev = new Double[rms1.length];

                                for (int i = 0; i < absdev.length; i++) {
                                    absdev[i] = Math.abs(rms1[i] - m);
                                }

                                sigma = IdlCommonFunctions.mad(rms1);
                                sigma_data = IdlCommonFunctions.mad(rms_data_scalar);

                                rms1_m = new Double[rms1.length];
                                for (int i = 0; i < rms1_m.length; i++) {
                                    rms1_m[i] = rms1[i] - m;
                                }

                                Double[] arrTemp = new Double[rms1.length];
                                for (int i = 0; i < arrTemp.length; i++) {
                                    arrTemp[i] = rms1_m[i] / sigma;
                                }

                                List<Integer> rms1_good_list = IdlCommonFunctions.where(arrTemp, IdlCommonFunctions.IDL_OPERATOR_LE, 3.0);
                                Integer[] rms1_good = rms1_good_list.stream().mapToInt(rms1_idx -> (rms1_idx == null) ? 0 : rms1_idx).boxed().toArray(Integer[]::new);

                                int tmpValue = index_i - rms1.length;
                                for (int i = 0; i < rms1_good.length; i++) {
                                    rms1_good[i] += tmpValue;
                                }

                                rms2 = IdlCommonFunctions.getValuesByIndices_Double(rms_data_scalar, rms1_good_list);

                                data_monthly_IGRA[jk][g] = StatUtils.mean(ArrayUtils.toPrimitive(rms2));

                                rms1 = IdlCommonFunctions.getValuesByIndices_Double(rms1, rms1_good_list);

                                data_rms_scalar_mean[jk][g] = StatUtils.mean(ArrayUtils.toPrimitive(data_rms_scalar_1));
                                rms_scalar[jk][g] = Math.sqrt(IdlCommonFunctions.total(IdlCommonFunctions.arrPow(rms2, 2)) / (rms2.length - 2));

                                if (ECVREF == 2 || ECVREF == 3) {
                                    HarmonizationSigmaU_V harmonizationSigmaU_v = new HarmonizationSigmaU_V();

                                    harmonizationSigmaU_v.setYear(g);
                                    harmonizationSigmaU_v.setMonth(jk + 1);
                                    harmonizationSigmaU_v.setPress(press[j]);
                                    harmonizationSigmaU_v.setData_rms_scalar_1(data_rms_scalar_1);

                                    switch (ECVREF) {
                                        case 2:
                                            harmonizationSigmaU_v.setEcv(ECV.U);
                                            break;
                                        case 3:
                                            harmonizationSigmaU_v.setEcv(ECV.V);
                                            break;
                                    }

                                    switch (ZENREF) {
                                        case 0:
                                            harmonizationSigmaU_v.setZen(ZEN.DAY);
                                            break;
                                        case 1:
                                            harmonizationSigmaU_v.setZen(ZEN.NIGHT);
                                            break;
                                    }

                                    harmonizationSigmaUVList.add(harmonizationSigmaU_v);
                                }
                            }
                            y++;

                            //this loop put the uncertainty estiamted in the previous look in a vector, missing calues are sued
                            // when a value could not be calculated because of few data available
                            for (int i = 0; i < TIME.length; i++) {
                                if (g > 1977 && YEARG[i] == g && MONTHG[i] == (jk + 1)) {
                                    u_data3[i] = rms_scalar[jk][g];
                                    data_rms_scalar_mean_ECV[i] = data_rms_scalar_mean[jk][g];

                                    if (i > 0 && rms_scalar[jk][g] == 0.0 && u_data3[i - 1] > 0.0) {
                                        u_data3[i] = u_data3[i - 1];
                                    }

                                    if (i > 0 && rms_scalar[jk][g] == 0.0 && u_data3[i - 1] == 0.0) {
                                        u_data3[i] = Double.NaN;
                                    }
                                }
                            }
                        }
                    }
                    data3 = ArrayCopy.copyOf(data, data.length);
                    data3_denorm = new Double[data3.length];

                    Double fabio = 0.0;

                    Double[] data_monthly_IGRA_T = new Double[data3.length];
                    Double[] data_monthly_IGRA_T1 = new Double[data3.length];
                    Double[] data_monthly_IGRA_T2 = new Double[data3.length];

                    Arrays.fill(data_monthly_IGRA_T, 0.0);
                    Arrays.fill(data_monthly_IGRA_T1, 0.0);
                    Arrays.fill(data_monthly_IGRA_T2, 0.0);

                    Double IGRA_BREAK_MEAN = 0.0;
                    Double IGRA_BREAK_MEAN_ref = 0.0;

                    p = data3.length - 1;

                    //to change with a different value if the CUSUM must be referred to a mean different than the mean of the entire time series
                    Integer f = 0;
                    Integer f1 = data3.length - 1;

                    Integer[] outliers = new Integer[data.length];
                    Arrays.fill(outliers, 0);

                    int endProducts;
                    List<Integer> indexOfEndProducts = IdlCommonFunctions.where(TIME, IdlCommonFunctions.IDL_OPERATOR_GE, Collections.max(julianDatesProduct));
                    if (indexOfEndProducts.size() == 0) {
                        endProducts = data3.length - 1;
                    } else {
                        endProducts = indexOfEndProducts.get(0);
                    }

                    //the following loop defined the CUSUM method and the parameters to apply the V-MASK method.
                    //The cumulative sum (CUSUM) control chart is considered to be an alternative or complementary to Shewhart control charts in
                    //statistical process control (SPC) applications, owing to its higher sensitivity to small shifts in the process mean.
                    //It utilizes all the available data rather than the last few ones used in Shewhart control charts for quick decision making.
                    //V-mask is a traditional technique for separating meaningful data from unusual circumstances in a Cumulative Sum (CUSUM) control chart;
                    //for see details about v-mask see Montgomery (1985, ISBN:978-0471656319).
                    //The mask is a V-shaped overlay placed on the CUSUM chart so that one arm of the V lines up with the slope of data points,
                    //making it easy to see data points that lie outside the slope and to determine whether these points should be discarded as random events,
                    //or treated as a performance trend that should be addressed. But, complex computations is one disadvantage V-mask method for detect small changes in mean
                    //using CUSUM control chart. Package 'vMask' can help to the applied users to overcome this challenge by considering six different methods
                    //which each of them are based on different information.

                    Integer period = 0;
                    Double k = 0.0;
                    Double eps = 0.0;
                    Double[] arrTmp;
                    Double stdDevK = 0.0;
                    Double[] data_1 = new Double[0];
                    Double[] data_1_1 = new Double[0];

                    //CUSUM is applied on noise filtered data resulting in supposed purely climate signal (seasonality + trends)
                    if (ECVREF >= 0) {
                        data_1 = ArrayCopy.copyOf(ysmoo, ysmoo.length);
                    }

                    period = 365;
                    k = 0.5d * delta * IdlCommonFunctions.stdDev(Arrays.asList(data_1).subList(f, data_1.length));

                    //A general rule of thumb (Montgomery) if one chooses to design with the h and k approach,
                    //instead of the α and β method illustrated above, is to choose k to be half the δ shift (0.5 in our example) and h to be around 4 or 5.
                    //For more information on CUSUM chart design, see Woodall and Adams (1993).As an alterinative choice eps can be put as eps=d*k where d=(2.0/((delta)^2))*alog((1-0.01)/0.0027) ; delta > 1
                    eps = 4 * k;

                    double mean = StatUtils.mean(ArrayUtils.toPrimitive(ArrayCopy.copyOfRange(data_1, f, data_1.length)));

                    for (int i = data3.length - 2; i > -1; i--) {
                        //here the CUSUM is calculated
                        if (data_monthly_IGRA_T[i + 1] + (data_1[i] - mean - k) > 0.0) {
                            data_monthly_IGRA_T[i] = data_monthly_IGRA_T[i + 1] + (data_1[i] - mean - k);
                        }

                        if (data_monthly_IGRA_T1[i + 1] + (mean - data_1[i] - k) > 0.0) {
                            data_monthly_IGRA_T1[i] = data_monthly_IGRA_T1[i + 1] + (mean - data_1[i] - k);
                        }

                        if (data_monthly_IGRA_T[i + 1] + (data_1[i] - mean - k) <= 0.0) {
                            data_monthly_IGRA_T[i] = 0.0;
                        }

                        if (data_monthly_IGRA_T1[i + 1] + (mean - data_1[i] - k) <= 0.0) {
                            data_monthly_IGRA_T1[i] = 0.0;
                        }
                    }

                    int p1 = data3.length - 1;
                    int flag_p1 = 2;

                    for (int i = data3.length - 2; i > 0; i--) {
                        if (Math.abs(TIME[i] - TIME[i + 1]) < 10) {
                            //Here the start point of the each break is found
                            if ((data_monthly_IGRA_T[i] > eps && data_monthly_IGRA_T[i + 1] <= eps)
                                    || (data_monthly_IGRA_T1[i] > eps && data_monthly_IGRA_T1[i + 1] <= eps)) {

                                p = i;
                                flag_p1 = 0;
                            }

                            //Here the end of the previously detected change is found
                            if ((flag_p1 == 0)
                                    && ((data_monthly_IGRA_T[i] - data_monthly_IGRA_T[i + 1] >= 0 && data_monthly_IGRA_T[i - 1] - data_monthly_IGRA_T[i] < 0)
                                    || (data_monthly_IGRA_T1[i] - data_monthly_IGRA_T1[i + 1] >= 0 && data_monthly_IGRA_T1[i - 1] - data_monthly_IGRA_T1[i] < 0))) {

                                p1 = i;
                                flag_p1 = 1;
                            }

                            //here the possibility to detect two consecutive changes is considered
                            if ((flag_p1 == 1) && (i < p1) && (p1 < p)) {
                                if (((data_monthly_IGRA_T[i] - data_monthly_IGRA_T[i + 1] >= 0 && data_monthly_IGRA_T[i - 1] - data_monthly_IGRA_T[i] < 0)
                                        && (data_monthly_IGRA_T[p1] - data_monthly_IGRA_T[p1 + 1] >= 0 && data_monthly_IGRA_T[p1 - 1] - data_monthly_IGRA_T[p1] < 0))
                                        || ((data_monthly_IGRA_T1[i] - data_monthly_IGRA_T1[i + 1] >= 0 && data_monthly_IGRA_T1[i - 1] - data_monthly_IGRA_T1[i] < 0)
                                        && (data_monthly_IGRA_T1[p1] - data_monthly_IGRA_T1[p1 + 1] >= 0 && data_monthly_IGRA_T1[p1 - 1] - data_monthly_IGRA_T1[p1] < 0))) {
                                    p = p1;
                                    p1 = i;
                                }
                            }

                            Double julianDate_p = AstronomicUtility.calculateJulianDay(YEARG[p], MONTHG[p], DAYG[p], HOURG[p], 0, 0, 0);
                            Double julianDate_p1 = AstronomicUtility.calculateJulianDay(YEARG[p1], MONTHG[p1], DAYG[p1], HOURG[p1], 0, 0, 0);
                            Double julianDate_f1 = AstronomicUtility.calculateJulianDay(YEARG[f1], MONTHG[f1], DAYG[f1], HOURG[f1], 0, 0, 0);
                            Double julianDate_start = AstronomicUtility.calculateJulianDay(YEARG[0], MONTHG[0], DAYG[0], HOURG[0], 0, 0, 0);
                            Double julianDate_beginProducts = AstronomicUtility.calculateJulianDay(YEARG[beginProducts], MONTHG[beginProducts], DAYG[beginProducts], HOURG[beginProducts], 0, 0, 0);
                            Double julianDate_endProducts = AstronomicUtility.calculateJulianDay(YEARG[endProducts], MONTHG[endProducts], DAYG[endProducts], HOURG[endProducts], 0, 0, 0);

                            if ((p < p1 && i == 1) || ((p > p1) && (julianDate_p1 - julianDate_start <= 365) && (i == 1))) {
                                p1 = 0;

                                julianDate_p1 = AstronomicUtility.calculateJulianDay(YEARG[p1], MONTHG[p1], DAYG[p1], HOURG[p1], 0, 0, 0);
                            }

                            if ((i == p1 || p1 == 0)
                                    && (p > p1)
                                    && ((p - p1) / (1 + YEARG[p] - YEARG[p1])) >= 150
                                    && (julianDate_p - julianDate_p1 > 365)
                                    && (julianDate_f1 - julianDate_p > 1825)
                                    && ((f1 - p) / (1 + YEARG[f1] - YEARG[p])) >= 239) {
                                devm1 = 100.0;
                                devm2 = 100.0;

                                //perform an exponential fit of the data in between of two consecutive breaks; the fit is done using a linear robust method applied on the logarithm ofteh considered ECV
                                Double[] x1 = IdlCommonFunctions.arrMinusScalarToArray(ArrayCopy.copyOfRange(TIME, p1, p), TIME[p1]);
                                Double[] y1 = ArrayCopy.copyOfRange(ysmoo, p1, p);
                                y1 = IdlCommonFunctions.arrLog(IdlCommonFunctions.arrSumScalarToArray(y1, 1000.0));

                                IdlCommonFunctions.absdev = 0.0;
                                Double[] guess1 = IdlCommonFunctions.ladfit(x1, y1);
                                devm1 = IdlCommonFunctions.absdev;

                                y1 = IdlCommonFunctions.arrSumScalarToArray(IdlCommonFunctions.arrMultScalarToArray(x1, guess1[1]), guess1[0]);
                                y1 = IdlCommonFunctions.arrMinusScalarToArray(IdlCommonFunctions.arrExp(y1), 1000.0);

                                //perform an exponential fit of the already harmonized portion of the time series between the beginning of the time series and last detected break;
                                // the fit is done using a linear robust method applied on the logarithm ofteh considered ECV

                                //calculate the LOWESS for each ECV to remove the seasonality, the window has been obtanied from the ACF.
                                Double[] ysmoo3 = ArrayUtils.toObject(IdlCommonFunctions.lowess(ArrayUtils.toPrimitive(TIME), ArrayUtils.toPrimitive(data3), widthLowess * data.length, 2));

                                Double[] x2 = IdlCommonFunctions.arrMinusScalarToArray(ArrayCopy.copyOfRange(TIME, p, data3.length), TIME[p]);
                                Double[] y2 = ArrayCopy.copyOfRange(ysmoo3, p, data3.length);

                                int maxYearsDifference = 11;
                                boolean yearFounded = false;
                                boolean go_on = true;
                                do {
                                    if (YEARG[data3.length - 1] - YEARG[p] > maxYearsDifference) {
                                        go_on = false;
                                        List<Integer> listIndices_YEARG = IdlCommonFunctions.where(YEARG, IdlCommonFunctions.IDL_OPERATOR_EQ, YEARG[p] + maxYearsDifference);

                                        if (listIndices_YEARG.size() > 0) {
                                            Double[] x2_1 = ArrayCopy.copyOfRange(TIME, p, listIndices_YEARG.get(0));

                                            x2 = IdlCommonFunctions.arrMinusScalarToArray(x2_1, TIME[p]);
                                            y2 = ArrayCopy.copyOfRange(ysmoo3, p, listIndices_YEARG.get(0));

                                            yearFounded = true;
                                            go_on = true;
                                        }
                                    }

                                    maxYearsDifference--;
                                } while (!yearFounded && maxYearsDifference > 2);

                                if (go_on) {
                                    y2 = IdlCommonFunctions.arrLog(IdlCommonFunctions.arrSumScalarToArray(y2, 1000.0));

                                    //forecast the time series between two consecutive breaks using the exponential trend fitted in the harmonized portion of the time series
                                    IdlCommonFunctions.absdev = 0.0;
                                    Double[] guess2 = IdlCommonFunctions.ladfit(x2, y2);
                                    devm2 = IdlCommonFunctions.absdev;

                                    Double[] x3 = IdlCommonFunctions.arrMinusScalarToArray(ArrayCopy.copyOfRange(TIME, p1, p), TIME[p]);

                                    Double[] y1_1 = IdlCommonFunctions.arrSumScalarToArray(IdlCommonFunctions.arrMultScalarToArray(x3, guess2[1]), guess2[0]);
                                    if ((guess1[1] < 0 && guess2[1] > 0) || (guess1[1] > 0 && guess2[1] < 0)) {
                                        y1_1 = IdlCommonFunctions.arrSumScalarToArray(IdlCommonFunctions.arrMultScalarToArray(x3, guess1[1]), guess2[0]);
                                    }
                                    y1_1 = IdlCommonFunctions.arrMinusScalarToArray(IdlCommonFunctions.arrExp(y1_1), 1000.0);

                                    //set the variables to calculate the next loop on the following break
                                    fabio++;

                                    //this loop applies the adjustments calculated as the mean difference of the forecast and real values of the time series between two consecutive breaks
                                    if (fabio >= 1.0 && devm1 <= 0.1 && devm2 <= 0.1 && StatUtils.mean(ArrayUtils.toPrimitive(y1)) != 0 && StatUtils.mean(ArrayUtils.toPrimitive(y1_1)) != 0) {
                                        for (int z = p; z >= p1; z--) {
                                            //for T considers even changes in their order within the index "fabio" to adjust the data
                                            //from the beginning to the end of each change; for odd changes look at the averages and adjust
                                            //if the average in between significantly differs from the global mean
                                            data3[z] = data[z] - StatUtils.mean(ArrayUtils.toPrimitive(IdlCommonFunctions.arrMinus(y1, y1_1)));

                                            if (ECVREF == 1 && data3[z] > 1.0) {
                                                data3[z] = 1.0;
                                            }
                                        }

                                        //new module with the same shape as the one used above but applied to each interval identified in between of two breaks in the time series.
                                        //module and loop to calculate residuals with respect to the two LOWESS smoothin applied in ysmoo and ysmoo1 vectors, the first is not more in sue in this version the second is used
                                        //to estimate the uncertainties for the historical part of the time series, where the GRUAN-like data processing within this algorithm cannot be applied

                                        //defines a few vector and array for the calculation of uncertainties in the following loop removing the ysmoo1 and keeping only the random noise in the signal
                                        m1 = StatUtils.mean(ArrayUtils.toPrimitive(data3));
                                        sigma1 = IdlCommonFunctions.mad(data3);
                                        data_monthly_IGRA = new Double[12][maxYearG + 1];
                                        data_monthly_T_IGRA = new Double[12][maxYearG + 1];
                                        rms_scalar = new Double[12][maxYearG + 1];

                                        //calculated the residual for each region in between of two breaks optimizing the LOWESS background time series
                                        //on the model_sigma estimated above using the GRUAN uncertainty as a constraint
                                        Double[] ysmoo2 = ArrayUtils.toObject(IdlCommonFunctions.lowess(ArrayUtils.toPrimitive(TIME), ArrayUtils.toPrimitive(data3), dd, 2));

                                        double firstElement = StatUtils.variance(ArrayUtils.toPrimitive(ArrayCopy.copyOfRange(ysmoo1, p, ysmoo2.length)));
                                        double secondElement = StatUtils.variance(ArrayUtils.toPrimitive(ArrayCopy.copyOfRange(ysmoo2, p1, p + 1)));

                                        double model_sigma2 = firstElement / secondElement;

                                        //+++++++++++++++ Applied within two consecutive breaks +++++++++++++++++++
                                        for (int g1 = YEARG[p]; g1 > YEARG[p1] - 1; g1--) {
                                            for (int jk1 = 0; jk1 < 12; jk1++) {
                                                int x1_idx = 0;
                                                rms = new Double[200];
                                                Arrays.fill(rms, 1.0e+6);
                                                rms_data = new Double[500];

                                                int index_i1 = 0;
                                                for (int ir1 = 0; ir1 < TIME.length; ir1++) {
                                                    if (YEARG[ir1] == g1 && MONTHG[ir1] == (jk1 + 1)) {
                                                        rms[x1_idx] = Math.abs(data2[ir1]);
                                                        rms_data[x1_idx] = data3[ir1] - ysmoo2[ir1];
                                                        rms_data[x1_idx] = model_sigma2 * rms_data[x1_idx];
                                                        x1_idx += 1;
                                                        index_i1 = ir1;
                                                    }
                                                }

                                                //filtering is applied in many step to remove data initialized at certain values of later on to remove outliers from the mean and stddev using MAD test
                                                List<Integer> listIndices_rms_LT_1_0E6 = IdlCommonFunctions.where(rms, IdlCommonFunctions.IDL_OPERATOR_LT, 1.0E+6);
                                                rms1 = IdlCommonFunctions.getValuesByIndices_Double(rms, listIndices_rms_LT_1_0E6);
                                                rms_data_scalar = IdlCommonFunctions.getValuesByIndices_Double(rms_data, listIndices_rms_LT_1_0E6);

                                                //cannot work below 15 elements, the uncertainty has not enough statitiscs to be estimated
                                                Double[] absdev;
                                                double sigma;
                                                double sigma_data;
                                                Double[] rms1_m;
                                                Double[] rms2;

                                                if (rms1.length >= 15 && listIndices_rms_LT_1_0E6.get(0) >= 0) {
                                                    Double m = IdlCommonFunctions.median(rms1);

                                                    absdev = new Double[rms1.length];

                                                    for (int ii = 0; ii < absdev.length; ii++) {
                                                        absdev[ii] = Math.abs(rms1[ii] - m);
                                                    }

                                                    sigma = IdlCommonFunctions.mad(rms1);
                                                    sigma_data = IdlCommonFunctions.mad(rms_data_scalar);

                                                    rms1_m = new Double[rms1.length];
                                                    for (int ii = 0; ii < rms1_m.length; ii++) {
                                                        rms1_m[ii] = rms1[ii] - m;
                                                    }

                                                    Double[] arrTemp = new Double[rms1.length];
                                                    for (int ii = 0; ii < arrTemp.length; ii++) {
                                                        arrTemp[ii] = rms1_m[ii] / sigma;
                                                    }

                                                    List<Integer> rms1_good_list = IdlCommonFunctions.where(arrTemp, IdlCommonFunctions.IDL_OPERATOR_LE, 3.0);
                                                    Integer[] rms1_good = rms1_good_list.stream().mapToInt(rms1_idx -> (rms1_idx == null) ? 0 : rms1_idx).boxed().toArray(Integer[]::new);

                                                    int tmpValue = index_i1 - rms1.length;
                                                    for (int ii = 0; ii < rms1_good.length; ii++) {
                                                        rms1_good[ii] += tmpValue;
                                                    }

                                                    rms2 = IdlCommonFunctions.getValuesByIndices_Double(rms_data_scalar, rms1_good_list);
                                                    data_monthly_IGRA[jk1][g1] = StatUtils.mean(ArrayUtils.toPrimitive(rms2));
                                                    rms1 = IdlCommonFunctions.getValuesByIndices_Double(rms1, rms1_good_list);

                                                    rms_scalar[jk1][g1] = Math.sqrt(IdlCommonFunctions.total(IdlCommonFunctions.arrPow(rms2, 2)) / (rms2.length - 2));
                                                }

                                                for (int ic1 = p; ic1 > p1 - 1; ic1--) {
                                                    if (g1 > 1977 && YEARG[ic1] == g1 && MONTHG[ic1] == (jk1 + 1)) {
                                                        u_data3[ic1] = rms_scalar[jk1][g1];

                                                        if ((rms_scalar[jk1][g1] == null || rms_scalar[jk1][g1] == 0.0) && (u_data3[ic1 + 1] != null || u_data3[ic1 + 1] > 0.0)) {
                                                            u_data3[ic1] = u_data3[ic1 + 1];
                                                        }

                                                        if ((rms_scalar[jk1][g1] == null || rms_scalar[jk1][g1] == 0.0) && (u_data3[ic1 + 1] == null || u_data3[ic1 + 1] == 0.0)) {
                                                            u_data3[ic1] = Double.NaN;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    HarmonizationBreak harmonizationBreak = new HarmonizationBreak();

                                    harmonizationBreak.setIdStation(station.getIdStation());
                                    harmonizationBreak.setFabio(fabio);
                                    harmonizationBreak.setEcvref(ECVREF);
                                    harmonizationBreak.setZenref(ZENREF);
                                    harmonizationBreak.setYear(YEARG[i]);
                                    harmonizationBreak.setMonth(MONTHG[i]);
                                    harmonizationBreak.setDay(DAYG[i]);
                                    harmonizationBreak.setHour(HOURG[i]);
                                    harmonizationBreak.setPress(press[j]);

                                    harmonizationBreakDAO.save(harmonizationBreak, BaseObjDAO.SQL_COMMAND.INSERT);
                                }
                            }
                        }

                        //removes outlier for Temperature
                        if (ECVREF == 0
                                && Math.abs(data[i] - StatUtils.mean(ArrayUtils.toPrimitive(ArrayCopy.copyOfRange(data, i, p)))) / IdlCommonFunctions.stdDev(ArrayCopy.copyOfRange(data, i, p)) > 6.0) {
                            outliers[i] = 1;
                        }

                        if (ECVREF == 1 && data3[i] > 1.0) {
                            //set RH values larger 100% to 100%
                            data3[i] = 1.0;
                        }

                        if (ECVREF >= 2 && Math.abs(data3[i]) > 250.0) {
                            outliers[i] = 1;
                        }

                        //find the 1st and 3rd quartiles of the time series and filter out only strong outlier where x<=q1-3.0*(q3-q1) or x>=q3-3.0*(q3-q1)
                        int n = data.length;
                        Double q1 = 0.0;
                        Double q3 = 0.0;
                        if (n > 4) {
                            int mid = n / 2 - 1;

                            List<Integer> listIndices_data_LT_dataMid = IdlCommonFunctions.where(data, IdlCommonFunctions.IDL_OPERATOR_LT, data[mid]);
                            q1 = IdlCommonFunctions.median(IdlCommonFunctions.getValuesByIndices_Double(data, listIndices_data_LT_dataMid));

                            List<Integer> listIndices_data_GE_dataMid = IdlCommonFunctions.where(data, IdlCommonFunctions.IDL_OPERATOR_GE, data[mid]);
                            q3 = IdlCommonFunctions.median(IdlCommonFunctions.getValuesByIndices_Double(data, listIndices_data_GE_dataMid));
                        }

                        //removes RH and wind components stronger outliers, see previous comment
                        if (ECVREF >= 1 && (data3[i] <= q1 - 3.0 * (q3 - q1) || data3[i] >= q3 + 3.0 * (q3 - q1))) {
                            outliers[i] = 1;
                        }

                        //removes RH lower than for 0.0001
                        if (ECVREF == 1 && data3[i] < 0.0001) {
//                                outliers[i] = 1;
                            data3[i] = 0.0001;
                        }

                        //put as outliers only those values which exceeds 1.0 of RH and before the harmonization were not higher than 0.95, this is to keep consistency in the cloudiness conditions
                        if (ECVREF == 1 && data3[i] > 1.0) {
                            data3[i] = 1.0;
                        }
                    }

                    List<Integer> listIndices_outliers_EQ_0 = IdlCommonFunctions.where(outliers, IdlCommonFunctions.IDL_OPERATOR_EQ, 0);
                    data = IdlCommonFunctions.getValuesByIndices_Double(data, listIndices_outliers_EQ_0);
                    data3 = IdlCommonFunctions.getValuesByIndices_Double(data3, listIndices_outliers_EQ_0);
                    u_data3 = IdlCommonFunctions.getValuesByIndices_Double(u_data3, listIndices_outliers_EQ_0);
                    data_rms_scalar_mean_ECV = IdlCommonFunctions.getValuesByIndices_Double(data_rms_scalar_mean_ECV, listIndices_outliers_EQ_0);
                    TIME = IdlCommonFunctions.getValuesByIndices_Double(TIME, listIndices_outliers_EQ_0);
                    ysmoo = IdlCommonFunctions.getValuesByIndices_Double(ysmoo, listIndices_outliers_EQ_0);
                    YEARG = IdlCommonFunctions.getValuesByIndices_Integer(YEARG, listIndices_outliers_EQ_0);
                    MONTHG = IdlCommonFunctions.getValuesByIndices_Integer(MONTHG, listIndices_outliers_EQ_0);
                    DAYG = IdlCommonFunctions.getValuesByIndices_Integer(DAYG, listIndices_outliers_EQ_0);
                    HOURG = IdlCommonFunctions.getValuesByIndices_Integer(HOURG, listIndices_outliers_EQ_0);
                    TEMPG = IdlCommonFunctions.getValuesByIndices_Double(TEMPG, listIndices_outliers_EQ_0);

                    if (ZENREF == 0) {
                        data4 = ArrayCopy.copyOf(data3, data3.length);
                        data6 = ArrayCopy.copyOf(u_data3, u_data3.length);
                        data8 = ArrayCopy.copyOf(data, data.length);
                        data10 = ArrayCopy.copyOf(MONTHG, MONTHG.length);
                        data12 = ArrayCopy.copyOf(DAYG, DAYG.length);
                        data14 = ArrayCopy.copyOf(YEARG, YEARG.length);
                        data16 = ArrayCopy.copyOf(HOURG, HOURG.length);
                        data18 = ArrayCopy.copyOf(TEMPG, TEMPG.length);
                        data22 = ArrayCopy.copyOf(ysmoo, ysmoo.length);
                        data24 = ArrayCopy.copyOf(data_rms_scalar_mean_ECV, data_rms_scalar_mean_ECV.length);
                    }
                    if (ZENREF == 1) {
                        data5 = ArrayCopy.copyOf(data3, data3.length);
                        data7 = ArrayCopy.copyOf(u_data3, u_data3.length);
                        data9 = ArrayCopy.copyOf(data, data.length);
                        data11 = ArrayCopy.copyOf(MONTHG, MONTHG.length);
                        data13 = ArrayCopy.copyOf(DAYG, DAYG.length);
                        data15 = ArrayCopy.copyOf(YEARG, YEARG.length);
                        data17 = ArrayCopy.copyOf(HOURG, HOURG.length);
                        data19 = ArrayCopy.copyOf(TEMPG, TEMPG.length);
                        data23 = ArrayCopy.copyOf(ysmoo, ysmoo.length);
                        data25 = ArrayCopy.copyOf(data_rms_scalar_mean_ECV, data_rms_scalar_mean_ECV.length);
                    }
                }

                Double[] data_f_1 = ArrayUtils.addAll(data4, data5);
                Double[] u_dataf_1 = ArrayUtils.addAll(data6, data7);
                Double[] data_source = ArrayUtils.addAll(data8, data9);
                Integer[] data_MONTHG = ArrayUtils.addAll(data10, data11);
                Integer[] data_DAYG = ArrayUtils.addAll(data12, data13);
                Integer[] data_YEARG = ArrayUtils.addAll(data14, data15);
                Integer[] data_HOURG = ArrayUtils.addAll(data16, data17);
                Double[] data_TEMPG = ArrayUtils.addAll(data18, data19);
                Double[] ysmoo_1 = ArrayUtils.addAll(data22, data23);
                Double[] data_rms_scalar_mean_ECV_1 = ArrayUtils.addAll(data24, data25);

                if (data_source == null || data_source.length == 0) {
                    continue;
                }

                Double[] TIMEf = ArrayUtils.toObject(AstronomicUtility.calculateJulianDay(data_YEARG, data_MONTHG, data_DAYG, data_HOURG));

                List<Harmonization_Data_Tmp> harmonization_data_tmp_list = new ArrayList<>();
                for (int i = 0; i < TIMEf.length; i++) {
                    Harmonization_Data_Tmp harmonization_data_tmp = new Harmonization_Data_Tmp();

                    harmonization_data_tmp.setTimef(TIMEf[i]);
                    harmonization_data_tmp.setData_f(data_f_1[i]);
                    harmonization_data_tmp.setU_dataf(u_dataf_1[i]);
                    harmonization_data_tmp.setData_source(data_source[i]);
                    harmonization_data_tmp.setData_MONTHG(data_MONTHG[i]);
                    harmonization_data_tmp.setData_DAYG(data_DAYG[i]);
                    harmonization_data_tmp.setData_YEARG(data_YEARG[i]);
                    harmonization_data_tmp.setData_HOURG(data_HOURG[i]);
                    harmonization_data_tmp.setData_TEMPG(data_TEMPG[i]);
                    harmonization_data_tmp.setYsmoo_1(ysmoo_1[i]);
                    harmonization_data_tmp.setData_rms_scalar_mean_ECV_1(data_rms_scalar_mean_ECV_1[i]);

                    harmonization_data_tmp_list.add(harmonization_data_tmp);
                }
                harmonization_data_tmp_list.sort(Comparator.comparing(Harmonization_Data_Tmp::getTimef));

                TIMEf = harmonization_data_tmp_list.stream().map(Harmonization_Data_Tmp::getTimef).toArray(Double[]::new);
                Double[] data_f = harmonization_data_tmp_list.stream().map(Harmonization_Data_Tmp::getData_f).toArray(Double[]::new);
                Double[] u_dataf = harmonization_data_tmp_list.stream().map(Harmonization_Data_Tmp::getU_dataf).toArray(Double[]::new);
                data_source = harmonization_data_tmp_list.stream().map(Harmonization_Data_Tmp::getData_source).toArray(Double[]::new);
                data_MONTHG = harmonization_data_tmp_list.stream().map(Harmonization_Data_Tmp::getData_MONTHG).toArray(Integer[]::new);
                data_DAYG = harmonization_data_tmp_list.stream().map(Harmonization_Data_Tmp::getData_DAYG).toArray(Integer[]::new);
                data_YEARG = harmonization_data_tmp_list.stream().map(Harmonization_Data_Tmp::getData_YEARG).toArray(Integer[]::new);
                data_HOURG = harmonization_data_tmp_list.stream().map(Harmonization_Data_Tmp::getData_HOURG).toArray(Integer[]::new);
                data_TEMPG = harmonization_data_tmp_list.stream().map(Harmonization_Data_Tmp::getData_TEMPG).toArray(Double[]::new);
                ysmoo_1 = harmonization_data_tmp_list.stream().map(Harmonization_Data_Tmp::getYsmoo_1).toArray(Double[]::new);
                data_rms_scalar_mean_ECV_1 = harmonization_data_tmp_list.stream().map(Harmonization_Data_Tmp::getData_rms_scalar_mean_ECV_1).toArray(Double[]::new);

                Integer wk = 0;
                Integer xk = 0;
                Integer yk = 0;
                Integer zk = 0;

                for (int i = 0; i < data_f.length; i++) {
                    if (ECVREF == 0) {
                        T_H[wk][j] = data_f[i];
                        ERR_T_H[wk][j] = u_dataf[i];
                        data_MONTHG_T_H[wk][j] = data_MONTHG[i];
                        data_DAYG_T_H[wk][j] = data_DAYG[i];
                        data_YEARG_T_H[wk][j] = data_YEARG[i];
                        data_HOURG_T_H[wk][j] = data_HOURG[i];
                        T_SOURCE[wk][j] = data_source[i];
                        data_TEMPG_T_H[wk][j] = data_TEMPG[i];

                        wk++;
                    }

                    if (ECVREF == 1) {
                        RH_H[xk][j] = data_f[i];
                        ERR_RH_H[xk][j] = u_dataf[i];
                        data_MONTHG_RH_H[xk][j] = data_MONTHG[i];
                        data_DAYG_RH_H[xk][j] = data_DAYG[i];
                        data_YEARG_RH_H[xk][j] = data_YEARG[i];
                        data_HOURG_RH_H[xk][j] = data_HOURG[i];
                        data_TEMPG_RH_H[xk][j] = data_TEMPG[i];
                        RH_SOURCE[xk][j] = data_source[i];

                        xk++;
                    }

                    if (ECVREF == 2) {
                        U_H[yk][j] = data_f[i];
                        ERR_U_H[yk][j] = u_dataf[i];
                        U_w_φ_uncertainties[yk][j] = data_rms_scalar_mean_ECV_1[i];
                        data_MONTHG_U_H[yk][j] = data_MONTHG[i];
                        data_DAYG_U_H[yk][j] = data_DAYG[i];
                        data_YEARG_U_H[yk][j] = data_YEARG[i];
                        data_HOURG_U_H[yk][j] = data_HOURG[i];
                        data_TEMPG_U_H[yk][j] = data_TEMPG[i];
                        U_SOURCE[yk][j] = data_source[i];

                        yk++;
                    }

                    if (ECVREF == 3) {
                        V_H[zk][j] = data_f[i];
                        ERR_V_H[zk][j] = u_dataf[i];
                        V_w_φ_uncertainties[zk][j] = data_rms_scalar_mean_ECV_1[i];
                        data_MONTHG_V_H[zk][j] = data_MONTHG[i];
                        data_DAYG_V_H[zk][j] = data_DAYG[i];
                        data_YEARG_V_H[zk][j] = data_YEARG[i];
                        data_HOURG_V_H[zk][j] = data_HOURG[i];
                        data_TEMPG_V_H[zk][j] = data_TEMPG[i];
                        V_SOURCE[zk][j] = data_source[i];

                        zk++;
                    }
                }
            }
        }

        Instant finish = Instant.now();
        setPhaseProcessingTime(StationToHarmonize.PHASE.CALCH, Duration.between(start, finish).toMillis());
    }

    private static Double getJulianDate(Instant date) {
        return new Double(AstronomicUtility.calculateJulianDay(date));
    }

    public List<Product> getProductByPress(Double pressure) {
        return productMandatoryLevels.stream().filter(x -> new Double(x.getPress()).equals(pressure)).collect(Collectors.toList());
    }

    private Object[] read_igra_files_alllevels_sub() {
        log.info(station.getIdStation() + " read_igra_files_alllevels_sub");

        Instant start = Instant.now();

        List<Object> data = new ArrayList<>();

        List<GuanDataHeader> guanDataHeaders;
        List<GuanDataValue> guanDataValues;

        String[] label = null;
        Double[] zenithsun = null;
        Double[][] pressions = null;
        Double[][] temp = null;
        Double[][] rh_calc = null;
        Double[][] wdir = null;
        Double[][] wspd = null;
        Integer[] years = null;
        Integer[] months = null;
        Integer[] days = null;
        Integer[] hours = null;

        final List<Instant> dates = guanDataValuesMandatoryLevels.stream().map(GuanDataValue::getDateOfObservation).distinct().collect(Collectors.toList());

        guanDataHeaders = guanDataHeadersFull.stream().filter(x -> dates.contains(x.getDateOfObservation())).collect(Collectors.toList());
        guanDataHeaders.sort(Comparator.comparing(GuanDataHeader::getDateOfObservation));

        label = new String[guanDataHeaders.size()];
        pressions = new Double[guanDataHeaders.size()][200];
        temp = new Double[guanDataHeaders.size()][200];
        rh_calc = new Double[guanDataHeaders.size()][200];
        wdir = new Double[guanDataHeaders.size()][200];
        wspd = new Double[guanDataHeaders.size()][200];

        Arrays.fill(label, guanDataHeaders.get(0).getStation().getIdStation().substring(0, 3));
        years = guanDataHeaders.stream().mapToInt(x -> x.getYear()).boxed().toArray(Integer[]::new);
        months = guanDataHeaders.stream().mapToInt(x -> x.getMonth()).boxed().toArray(Integer[]::new);
        days = guanDataHeaders.stream().mapToInt(x -> x.getDay()).boxed().toArray(Integer[]::new);
        hours = guanDataHeaders.stream().mapToInt(x -> x.getHour()).boxed().toArray(Integer[]::new);
        zenithsun = guanDataHeaders.stream().mapToDouble(x -> x.getZenithSun()).boxed().toArray(Double[]::new);

        Map<Instant, List<GuanDataValue>> guanDataValuesPerDate = guanDataValuesMandatoryLevels.stream().collect(groupingBy(GuanDataValue::getDateOfObservation));
        Map<Instant, List<GuanDataValue>> guanDataValuesPerDateSorted = guanDataValuesPerDate.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        int i = 0;
        for (Map.Entry me : guanDataValuesPerDateSorted.entrySet()) {
            guanDataValues = (List<GuanDataValue>) me.getValue();

            guanDataValues.sort(Comparator.comparing(GuanDataValue::getPress_hPa).reversed());

            guanDataValues
                    .parallelStream()
                    .forEach(guanDataValue -> {
                        if (guanDataValue.getWdir() == null) guanDataValue.setWdir(-9999);
                        if (guanDataValue.getWspd() == null) guanDataValue.setWspd(-9999.0f);

                        guanDataValue.setRh(GuanDataValue.getRh_calculated_CDMFormat(guanDataValue.getTemp(), guanDataValue.getRh(), guanDataValue.getDpdp() == null ? null : guanDataValue.getDpdp() - 273.15f));

                        if (guanDataValue.getRh() == null) {
                            guanDataValue.setRh(-9999.0f);
                        }
                    });

            pressions[i] = guanDataValues.stream().mapToDouble(x -> x.getPress_hPa()).boxed().toArray(Double[]::new);
            temp[i] = guanDataValues.stream().mapToDouble(x -> x.getTemp()).boxed().toArray(Double[]::new);
            rh_calc[i] = guanDataValues.stream().mapToDouble(x -> x.getRh()).boxed().toArray(Double[]::new);
            wdir[i] = guanDataValues.stream().mapToDouble(GuanDataValue::getWdir).boxed().toArray(Double[]::new);
            wspd[i] = guanDataValues.stream().mapToDouble(GuanDataValue::getWspd).boxed().toArray(Double[]::new);

            i++;
        }

        data.add(label);
        data.add(pressions);
        data.add(temp);
        data.add(rh_calc);
        data.add(years);
        data.add(months);
        data.add(days);
        data.add(hours);
        data.add(zenithsun);
        data.add(wdir);
        data.add(wspd);

        Instant finish = Instant.now();
        setPhaseProcessingTime(StationToHarmonize.PHASE.READIGRA, Duration.between(start, finish).toMillis());

        return data.toArray();
    }

    private boolean isHarmonizable() {
        boolean isHarmonizable = false;

        if (!station.getIdStation().startsWith("ZZ") && productMandatoryLevels.size() > 0) {
            List<Integer> yearsListProduct = new ArrayList<>(productMandatoryLevels.parallelStream()
                    .map(x -> LocalDateTime.ofInstant(x.getDateOfObservation(), ZoneId.of("UTC")).getYear())
                    .collect(Collectors.toSet()));

            Collections.sort(yearsListProduct);

            int yearPrev = yearsListProduct.get(0);
            int yearCurr;
            int numberConsecutiveYears = 0;

            for (int i = 1; i < yearsListProduct.size(); i++) {
                yearCurr = yearsListProduct.get(i);

                if (yearPrev == yearCurr - 1) {
                    numberConsecutiveYears++;
                } else {
                    numberConsecutiveYears = 0;
                }

                if (numberConsecutiveYears >= 4) {
                    isHarmonizable = true;
                    break;
                }

                yearPrev = yearCurr;
            }
        }

        return isHarmonizable;
    }

    private void setProductMandatoryLevels() {
        log.info(station.getIdStation() + " setProductMandatoryLevels");

        Instant start = Instant.now();

        List<Integer> wmoIntercomparisonSondes_List = Collections.synchronizedList(new ArrayList<>());

        commonStaticDataStructure.wmoIntercomparisonList
                .parallelStream()
                .forEach(x -> {
                    wmoIntercomparisonSondes_List.addAll(x.getSondeIdList());
                });

        Set<Integer> wmoIntercomparisonSondes = wmoIntercomparisonSondes_List
                .parallelStream()
                .collect(Collectors.toSet());

        //filter for the radiosondes for which I can calculate the productMandatoryLevels
        List<Header> headerList = headerFull
                .parallelStream()
                .filter(x -> x.getRadiosonde_code() != null
                        && !x.getRadiosonde_code().equals("NaN")
                        && (wmoIntercomparisonSondes.contains(x.getRadiosonde_id())
                        || commonStaticDataStructure.rs92codesList.contains(x.getRadiosonde_id())
                )).collect(Collectors.toList());

        if (headerList.size() > 0) {
            List<Measurement> measurementMandatoryLevels = measurementFull
                    .parallelStream()
                    .filter(x -> Arrays.asList(press).contains(x.getPress().doubleValue()))
                    .sorted(Comparator.comparing(Measurement::getDateOfObservation))
                    .collect(Collectors.toList());

            //Group together the measurements obtained by date
            Map<Instant, List<Measurement>> measurementsPerDate = measurementMandatoryLevels
                    .parallelStream()
                    .collect(groupingBy(Measurement::getDateOfObservation));

            //Order by ascending date
            Map<Instant, List<Measurement>> measurementsPerDateSorted =
                    measurementsPerDate
                            .entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByKey())
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            List<Measurement> measurementListByDate;
            for (Header header : headerList) {
                measurementListByDate = measurementsPerDateSorted.get(header.getDateOfObservation());

                if (measurementListByDate != null && measurementListByDate.size() > 0) {
                    productMandatoryLevels.addAll(productConverter.build(header.getRadiosonde_id(),
                            header.getYear(), header.getMonth(), header.getDay(), header.getHour(), 0, 0,
                            header.getLat(), header.getLon(), measurementListByDate, productMandatoryLevels));
                }
            }

            Instant finish = Instant.now();

            setPhaseProcessingTime(StationToHarmonize.PHASE.PRODUCT, Duration.between(start, finish).toMillis());
        }
    }

    private void initHarmonizedData() {
        log.info(station.getIdStation() + " initHarmonizedData");

        measurementFull
                .parallelStream()
                .forEach(m -> {
                    HarmonizedData harmonizedData = new HarmonizedData();

                    harmonizedData.setTime(m.getTime());
                    harmonizedData.setPress(m.getPress());
                    harmonizedData.setTemp(m.getTemp());
                    harmonizedData.setWdir(m.getWdir());
                    harmonizedData.setWspeed(m.getWspeed());
                    harmonizedData.setGeopot(m.getGeopot());
                    harmonizedData.setLvltyp1(m.getLvltyp1());
                    harmonizedData.setLvltyp2(m.getLvltyp2());
                    harmonizedData.setPflag(m.getPflag());
                    harmonizedData.setZflag(m.getZflag());
                    harmonizedData.setTflag(m.getTflag());
                    harmonizedData.setDpdp(m.getDpdp());
                    harmonizedData.setU(m.getU());
                    harmonizedData.setV(m.getV());
                    harmonizedData.setFp(m.getFp());
                    harmonizedData.setWvmr(m.getWvmr());
                    harmonizedData.setAsc(m.getAsc());
                    harmonizedData.setDateOfObservation(m.getDateOfObservation());
                    harmonizedData.setStation(m.getStation());
                    harmonizedData.setRh(m.getRh());

                    harmonizedDataList.add(harmonizedData);
                });

        harmonizedDataPerDate = harmonizedDataList
                .parallelStream()
                .collect(groupingBy(HarmonizedData::getDateOfObservation));
    }

    private void initList() {
        log.info(station.getIdStation() + " initList");

        Instant start = Instant.now();

        guanDataHeadersFull = new ArrayList<>();
        guanDataValuesFull = new ArrayList<>();
        guanDataValuesMandatoryLevels = new ArrayList<>();
        measurementFull = new ArrayList<>();
        harmonizedDataList = Collections.synchronizedList(new ArrayList<>());
        headerFull = new ArrayList<>();
        productMandatoryLevels = new ArrayList<>();

        List<Double> pressList = Arrays.asList(press);

        GuanDataHeader guanDataHeader = new GuanDataHeader();
        guanDataHeader.setStation(stationToHarmonize.getStation());
        guanDataHeadersFull = (List<GuanDataHeader>) guanDataHeaderDAO.getByIdStation(guanDataHeader);

        guanDataHeadersFull = guanDataHeadersFull.parallelStream()
                .filter(x -> HOURS_FILTER_LIST.contains(BaronCommons.getHour(x.getDateOfObservation())))
                .collect(Collectors.toList());

        guanDataValuesFull = (List<GuanDataValue>) guanDataValueDAO.getByIdStation(stationToHarmonize);
        guanDataValuesFull = guanDataValuesFull
                .parallelStream()
                .filter(x -> x.getPress() != null
                        && x.getTemp() != null
                        && HOURS_FILTER_LIST.contains(BaronCommons.getHour(x.getDateOfObservation()))
                )
                .filter(y -> y.getPress_hPa() >= Collections.min(pressList) && y.getPress_hPa() <= Collections.max(pressList))
                .collect(Collectors.toList());

        measurementFull = (List<Measurement>) measurementConverter.convert(guanDataValuesFull);
        BaronCommons.fixGeopotentialHeight(station, measurementFull);

        Set<Instant> datesMeasurements = measurementFull
                .parallelStream()
                .map(Measurement::getDateOfObservation)
                .collect(Collectors.toSet());

        headerFull = (List<Header>) headerConverter.convert(guanDataHeadersFull);

        Set<Instant> datesHeaders = headerFull
                .parallelStream()
                .map(Header::getDateOfObservation)
                .collect(Collectors.toSet());

        Set<Instant> datesHeadersToDelete = datesHeaders
                .parallelStream()
                .filter(x -> !datesMeasurements.contains(x))
                .collect(Collectors.toSet());

        headerFull = headerFull
                .parallelStream()
                .filter(x -> !datesHeadersToDelete.contains(x.getDateOfObservation()))
                .collect(Collectors.toList());

        guanDataValuesMandatoryLevels = guanDataValuesFull.parallelStream().filter(x -> Arrays.asList(press).contains(new Double(x.getPress_hPa()))).collect(Collectors.toList());
        guanDataValuesMandatoryLevels.sort(Comparator.comparing(GuanDataValue::getDateOfObservation));

        sondeIdList = headerFull
                .stream()
                .filter(x -> x.getRadiosonde_id() != null)
                .map(Header::getRadiosonde_id).collect(Collectors.toSet());

        ecvList = new ArrayList<>();
        ecvList.add(ECV.TEMP);
        ecvList.add(ECV.RH);
        ecvList.add(ECV.U);
        ecvList.add(ECV.V);

        zenList = new ArrayList<>();
        zenList.add(ZEN.DAY);
        zenList.add(ZEN.NIGHT);

        Instant finish = Instant.now();

        setPhaseProcessingTime(StationToHarmonize.PHASE.INITLIST, Duration.between(start, finish).toMillis());
    }

    private void setPhaseProcessingTime(StationToHarmonize.PHASE phase, long millis) {
        int seconds = Float.valueOf(millis / 1000.0f).intValue();

        switch (phase) {
            case INITLIST:
                stationToHarmonize.setInitlist(seconds);
                break;
            case READIGRA:
                stationToHarmonize.setReadigra(seconds);
                break;
            case PRODUCT:
                stationToHarmonize.setProduct(seconds);
                break;
            case CALCH:
                stationToHarmonize.setCalch(seconds);
                break;
            case ENDSETH:
                stationToHarmonize.setEndseth(seconds);
                break;
            case INTERPFINAL:
                stationToHarmonize.setInterpfinal(seconds);
                break;
            case DB:
                stationToHarmonize.setDb(seconds);
                break;
            case TOTAL:
                stationToHarmonize.setTotal(seconds);
                break;
        }

        stationToHarmonize.setLastupdate(Instant.now());
        stationToHarmonizeDAO.save(stationToHarmonize, BaseObjDAO.SQL_COMMAND.UPDATE);
    }

    private Station getStation() {
        List<Station> stationList = stationDAO.getByIdStationAndNetwork(stationToHarmonize.getStation().getIdStation(), BaronCommons.NETWORKS.GUAN.name());

        if (stationList == null || stationList.size() != 1) {
            throw new RuntimeException("Error with the ID of the station: " + stationToHarmonize.getStation().getIdStation());
        }

        return stationList.get(0);
    }
}
