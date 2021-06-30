package cnr.imaa.baron.bll.converter;

import cnr.imaa.baron.bll.harmonizer.CommonStaticDataStructure;
import cnr.imaa.baron.bll.harmonizer.ProductVariables;
import cnr.imaa.baron.commons.BaronCommons;
import cnr.imaa.baron.commons.BilinearInterpolator;
import cnr.imaa.baron.model.*;
import org.apache.commons.math3.analysis.BivariateFunction;
import org.decimal4j.util.DoubleRounder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductConverter {
    private final Logger log = LoggerFactory.getLogger(IgraConverter.class);

    private Instant[] dateOfObservation;
    private Float[] press;
    private Float[] geopot;
    private Float[] temp;
    private Float[] rh;
    private Float[] wspeed;
    private Float[] wvmr;
    private Float[] dpdp;
    private Float[] u;
    private Float[] v;
    private Float sza;
    private Float szaIdl;
    private Float f = 0.0f;
    private Float uF = 0.0f;
    private BilinearInterpolator bilinearInterpolator = new BilinearInterpolator();
    private BivariateFunction bivariateFunction_solarRadiationTable;

    private ArrayList<Float> temperatureCorrected_list;
    private ArrayList<Float> cor_temp_list;
    private ArrayList<Float> rhCorrected_list;
    private ArrayList<Float> cor_rh_list;
    private ArrayList<Float> wvmr_corrected_list;
    private ArrayList<Float> u_cor_temp_list;
    private ArrayList<Float> u_cor_rh_list;
    private ArrayList<Float> u_cor_u_list;
    private ArrayList<Float> u_cor_v_list;
    private ArrayList<Float> cor_u_list;
    private ArrayList<Float> uCorrected_list;
    private ArrayList<Float> cor_v_list;
    private ArrayList<Float> vCorrected_list;

    //for RS92 and RS41 radiosondes
    private ArrayList<Float> cor_temp_tl_list;
    private ArrayList<Float> u_cor_temp_tl_list;
    private ArrayList<Float> cor_rh_tl_list;
    private ArrayList<Float> u_cor_rh_tl_list;
    private ArrayList<Float> cor_u_list_rs92;
    private ArrayList<Float> u_cor_u_list_rs92;
    private ArrayList<Float> cor_v_list_rs92;
    private ArrayList<Float> u_cor_v_list_rs92;

    //for other radiosondes
    private ArrayList<Float> cor_intercomparison_temp_list;
    private ArrayList<Float> u_cor_intercomparison_temp_list;
    private ArrayList<Float> cor_intercomparison_rh_list;
    private ArrayList<Float> u_cor_intercomparison_rh_list;
    private ArrayList<Float> cor_u_list_notRs92;
    private ArrayList<Float> u_cor_u_list_notRs92;
    private ArrayList<Float> cor_v_list_notRs92;
    private ArrayList<Float> u_cor_v_list_notRs92;

    private CommonStaticDataStructure commonStaticDataStructure;

    public ProductConverter(CommonStaticDataStructure commonStaticDataStructure) {
        this.commonStaticDataStructure = commonStaticDataStructure;
    }

    private final double[][] solarRadiation_matrix_2005 = new double[][]{
            {0.45, 0.41, 0.63, 0.68, 0.69, 0.7, 0.67, 0.67, 0.69},
            {0.35, 0.38, 0.58, 0.63, 0.65, 0.65, 0.62, 0.62, 0.63},
            {0.25, 0.35, 0.53, 0.58, 0.61, 0.6, 0.57, 0.57, 0.57},
            {0.11, 0.27, 0.44, 0.49, 0.53, 0.52, 0.5, 0.49, 0.5},
            {0.0, 0.21, 0.36, 0.42, 0.45, 0.45, 0.43, 0.43, 0.44},
            {0.0, 0.15, 0.28, 0.34, 0.38, 0.39, 0.37, 0.37, 0.37},
            {0.0, 0.05, 0.18, 0.23, 0.27, 0.28, 0.27, 0.27, 0.27},
            {0.0, 0.01, 0.11, 0.15, 0.19, 0.21, 0.2, 0.2, 0.21},
            {0.0, 0.0, 0.04, 0.07, 0.11, 0.14, 0.14, 0.14, 0.14},
            {0.0, 0.0, 0.0, 0.0, 0.01, 0.04, 0.06, 0.07, 0.1}
    };

    private final double[][] solarRadiation_matrix_2010 = new double[][]{
            {-0.07, 0.64, 0.77, 0.86, 0.94, 0.98, 0.98, 0.98, 0.98, 0.98},
            {-0.06, 0.55, 0.68, 0.77, 0.84, 0.89, 0.89, 0.89, 0.89, 0.89},
            {-0.04, 0.37, 0.56, 0.64, 0.70, 0.78, 0.78, 0.78, 0.78, 0.78},
            {-0.03, 0.18, 0.48, 0.55, 0.59, 0.69, 0.69, 0.69, 0.69, 0.69},
            {-0.02, 0.05, 0.37, 0.45, 0.51, 0.60, 0.60, 0.60, 0.60, 0.60},
            {0.00, 0.00, 0.21, 0.28, 0.35, 0.45, 0.46, 0.47, 0.48, 0.48},
            {0.00, 0.00, 0.06, 0.11, 0.20, 0.32, 0.36, 0.37, 0.38, 0.39},
            {0.00, 0.00, 0.00, 0.02, 0.05, 0.20, 0.25, 0.27, 0.29, 0.31},
            {0.00, 0.00, 0.00, 0.00, 0.00, 0.04, 0.12, 0.15, 0.17, 0.19},
            {0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.04, 0.07, 0.09, 0.10}
    };

    private final double[] solarRadiation_xRow_2005 = new double[]{-5.0, -3.0, 0.0, 3.0, 10.0, 30.0, 45.0, 60.0, 90.0};
    private double[] solarRadiation_yRow_2005 = new double[]{0.0, 5.0, 10.0, 20.0, 30.0, 50.0, 100.0, 200.0, 500.0, 1013.25};

    private final double[] solarRadiation_xRow_2010 = new double[]{-5.1, -4.0, -2.0, 0.0, 3.0, 10.0, 30.0, 45.0, 60.0, 90.0};
    private double[] solarRadiation_yRow_2010 = new double[]{1.0, 2.0, 5.0, 10.0, 20.0, 50.0, 100.0, 200.0, 500.0, 1013.25};

    public List<Product> build(Integer radiosondeId, Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second, Float latitude, Float longitude,
                               List<Measurement> measurements, List<Product> previousProducts) {
        Boolean isRS92 = false;
        List<Product> productList = new ArrayList<>();

        if (radiosondeId == null) {
            return productList;
        }

        //Order the measurements for decreasing pressure
        measurements.sort(Comparator.comparing(Measurement::getPress).reversed());

        initLists();

        //loads the vectors of the variables
        loadVariablesVectors(measurements);

        //Calculate the variables that will be used for the calculations
        setVariablesForCalculations(year, month, day, hour, minute, second, latitude, longitude);

        isRS92 = isRS92(radiosondeId);

        List<WmoIntercomparison> wmoIntercomparisons = null;

        if (!isRS92) {
            wmoIntercomparisons = commonStaticDataStructure.wmoIntercomparisonList
                    .parallelStream()
                    .filter(x -> x.getSondeIdList().contains(radiosondeId))
                    .collect(Collectors.toList());
        }

        if (isRS92 || (wmoIntercomparisons != null && wmoIntercomparisons.size() > 0)) {
            List<ProductVariables> listProductVariables = new ArrayList();

            Integer indexStartCloud = null;
            Integer indexEndCloud = null;
            for (int i = 0; i < rh.length; i++) {
                if (rh[i] != null && !rh[i].equals(Float.NaN)) {
                    if (rh[i] >= 0.99f && indexStartCloud == null) {
                        indexStartCloud = i;
                    } else if (rh[i] < 0.99f && indexStartCloud != null) {
                        indexEndCloud = i - 1;
                    }
                }

                if (indexStartCloud != null && indexEndCloud != null) {
                    break;
                }
            }
            if (indexStartCloud != null && indexEndCloud == null) {
                indexEndCloud = rh.length - 1;
            }

            for (int i = 0; i < measurements.size(); i++) {
                ProductVariables productVariables = getProductVariables(i, isRS92, wmoIntercomparisons, indexStartCloud, indexEndCloud, year, radiosondeId);

                doCorrection_temp(i, productVariables);
                doCorrection_rh(i, productVariables);
                doCorrection_u(i, productVariables);
                doCorrection_v(i, productVariables);
                doCorrection_wvmr(i, productVariables);
                doCorrection_u_cor_temp(productVariables, previousProducts);
                doCorrection_u_cor_rh(i, productVariables, previousProducts);
                doCorrection_u_cor_u(productVariables);
                doCorrection_u_cor_v(productVariables);

                listProductVariables.add(productVariables);
            }

            productList.addAll(getProducts(measurements, isRS92, listProductVariables, radiosondeId));
        }

        return productList;
    }

    private List<Product> getProducts(List<Measurement> measurementList, Boolean isRS92, List<ProductVariables> listProductVariables, Integer radiosondeId) {
        List<Product> productList = new ArrayList<>();

        Measurement measurement;
        for (int i = 0; i < measurementList.size(); i++) {
            measurement = measurementList.get(i);

            ProductVariables productVariables = listProductVariables.get(i);

            Product product = new Product();

            product.setTime(measurement.getTime());
            product.setPress(measurement.getPress());
            product.setWdir(measurement.getWdir());
            product.setWspeed(measurement.getWspeed());
            product.setGeopot(measurement.getGeopot());
            product.setLvltyp1(measurement.getLvltyp1());
            product.setLvltyp2(measurement.getLvltyp2());
            product.setPflag(measurement.getPflag());
            product.setZflag(measurement.getZflag());
            product.setTflag(measurement.getTflag());
            product.setDpdp(measurement.getDpdp());
            product.setFp(measurement.getFp());
            product.setAsc(measurement.getAsc());
            product.setDateOfObservation(measurement.getDateOfObservation());
            product.setTemp(temperatureCorrected_list.get(i));
            product.setCor_temp(cor_temp_list.get(i));
            product.setU_cor_temp(u_cor_temp_list.get(i));
            product.setRh(rhCorrected_list.get(i));
            product.setCor_rh(cor_rh_list.get(i));
            product.setU_cor_rh(u_cor_rh_list.get(i));
            product.setU(uCorrected_list.get(i));
            product.setCor_u(cor_u_list.get(i));
            product.setV(vCorrected_list.get(i));
            product.setCor_v(cor_v_list.get(i));

            Float u_cor_u = u_cor_u_list.get(i);
            Float u_cor_v = u_cor_v_list.get(i);
            product.setU_cor_u(u_cor_u);
            product.setU_cor_v(u_cor_v);

            product.setWvmr(wvmr_corrected_list.get(i));
            product.setRadiosondeId(radiosondeId);
            if (isRS92) {
                product.setCor_temp_tl(cor_temp_tl_list.get(i));
                product.setU_cor_temp_tl(u_cor_temp_tl_list.get(i));
                product.setCor_rh_tl(cor_rh_tl_list.get(i));
                product.setU_cor_rh_tl(u_cor_rh_tl_list.get(i));
                product.setCor_u_rs92(cor_u_list_rs92.get(i));
                product.setU_cor_u_rs92(u_cor_u_list_rs92.get(i));
                product.setCor_v_rs92(cor_v_list_rs92.get(i));
                product.setU_cor_v_rs92(u_cor_v_list_rs92.get(i));
            } else {
                product.setCor_intercomparison_temp(cor_intercomparison_temp_list.get(i));
                product.setU_cor_intercomparison_temp(u_cor_intercomparison_temp_list.get(i));
                product.setCor_intercomparison_rh(cor_intercomparison_rh_list.get(i));
                product.setU_cor_intercomparison_rh(u_cor_intercomparison_rh_list.get(i));
                product.setCor_u_notRs92(cor_u_list_notRs92.get(i));
                product.setU_cor_u_notRs92(u_cor_u_list_notRs92.get(i));
                product.setCor_v_notRs92(cor_v_list_notRs92.get(i));
                product.setU_cor_v_notRs92(u_cor_v_list_notRs92.get(i));
            }

            product.setSza(sza);
            product.setSzaIdl(szaIdl);
            product.setDeltaTgruan(productVariables.getDeltaTgruan());
            product.setDeltaTvaisala(productVariables.getDeltaTvaisala());
            product.setF(productVariables.getF());
            product.setCor_temp_calc(productVariables.getCor_temp());
            product.setpSatNumeratore(productVariables.getpSatNumeratore());
            product.setpSatDenominatore(productVariables.getpSatDenominatore());
            product.setCor_rh_calc(productVariables.getCor_rh());
            product.setIsCloudy(productVariables.getCloudy());

            productList.add(product);
        }

        return productList;
    }

    private void doCorrection_u_cor_u(ProductVariables productVariables) {
        Boolean isRS92 = productVariables.getRS92();
        Float u_cor_u = 0.0f;
        Float randomErr_u = 0.15f;

        Optional<WindRs92Corr> windRs92CorrOptional = productVariables.getWindRs92CorrOptional();
        Float u_cor_u_windRs92Corr = windRs92CorrOptional.isPresent() ? windRs92CorrOptional.get().getU_cor_u() : 0.0f;

        if (isRS92) {
            u_cor_u = (float) Math.sqrt(Math.pow(u_cor_u_windRs92Corr, 2) + Math.pow(randomErr_u, 2));
            u_cor_u_list_rs92.add(u_cor_u);
        } else {
            u_cor_u = (float) Math.sqrt(Math.pow(productVariables.getWmoIntercomparison_U().getStd_dev(), 2) + Math.pow(u_cor_u_windRs92Corr, 2) + Math.pow(randomErr_u, 2));

            u_cor_u_list_notRs92.add(u_cor_u);
        }

        productVariables.setU_cor_u(u_cor_u);

        if (!u_cor_u.isNaN()) {
            u_cor_u = new Float(DoubleRounder.round(u_cor_u, 2));
        }

        u_cor_u_list.add(u_cor_u);

        productVariables.setU_cor_u(u_cor_u);
    }

    private void doCorrection_u_cor_v(ProductVariables productVariables) {
        Boolean isRS92 = productVariables.getRS92();
        Float u_cor_v = 0.0f;
        Float randomErr_v = 0.15f;

        Optional<WindRs92Corr> windRs92CorrOptional = productVariables.getWindRs92CorrOptional();

        Float u_cor_v_windRs92Corr = windRs92CorrOptional.isPresent() ? windRs92CorrOptional.get().getU_cor_v() : 0.0f;

        if (isRS92) {
            u_cor_v = (float) Math.sqrt(Math.pow(u_cor_v_windRs92Corr, 2) + Math.pow(randomErr_v, 2));

            u_cor_v_list_rs92.add(u_cor_v);
        } else {
            u_cor_v = (float) Math.sqrt(Math.pow(productVariables.getWmoIntercomparison_V().getStd_dev(), 2) + Math.pow(u_cor_v_windRs92Corr, 2) + Math.pow(randomErr_v, 2));

            u_cor_v_list_notRs92.add(u_cor_v);
        }

        productVariables.setU_cor_v(u_cor_v);

        if (!u_cor_v.isNaN()) {
            u_cor_v = new Float(DoubleRounder.round(u_cor_v, 2));
        }

        u_cor_v_list.add(u_cor_v);

        productVariables.setU_cor_v(u_cor_v);
    }

    private void doCorrection_u_cor_rh(Integer index, ProductVariables productVariables, List<Product> previousProducts) {
        Float u_cor_rh_tl = null;
        Float temperature_corrected;
        Float u_cor_temp;
        Float u_cor_rh = null;
        Float u_cor_intercomparison_rh = null;
        Float deltaTgruan = productVariables.getDeltaTgruan();
        Float randomErr_rh = 0.02f;

        Boolean isRS92 = productVariables.getRS92();
        Optional<TlSmoothRs92> tlSmoothRs92Optional = productVariables.getTlSmoothRs92Optional();

        Float cor_temp = productVariables.getCor_temp();

        if (rh[index] != null && !rh[index].isNaN() && rh[index] > -8888.0f && (press[index] >= 250.0f)) {
            if (sza < -5.0f) { //NIGHT
                u_cor_rh_tl = tlSmoothRs92Optional.isPresent() ? tlSmoothRs92Optional.get().getU_cor_rh_tl_night() : 0.0f;
            } else {
                u_cor_rh_tl = tlSmoothRs92Optional.isPresent() ? tlSmoothRs92Optional.get().getU_cor_rh_tl_day() : 0.0f;
            }

            Float cor_rh_tl = cor_rh_tl_list.get(cor_rh_tl_list.size() - 1);
            Float rh_corrected = rhCorrected_list.get(rhCorrected_list.size() - 1);

            if (cor_rh_tl.equals(0.0f)) {
                u_cor_rh_tl = 0.0f;
            } else {
                u_cor_rh_tl = (u_cor_rh_tl / cor_rh_tl) * rh_corrected;
            }

            if (isRS92) {
                temperature_corrected = temperatureCorrected_list.get(temperatureCorrected_list.size() - 1);
                u_cor_temp = u_cor_temp_list.get(u_cor_temp_list.size() - 1);

                Float Uc_RCt = rh[index] *
                        (float) (BaronCommons.calculatePsat(new Double(temperature_corrected + f * (cor_temp + u_cor_temp)))
                                - BaronCommons.calculatePsat(new Double(temperature_corrected + f * (cor_temp - u_cor_temp))))
                        / (float) (2 * BaronCommons.calculatePsat(new Double(temperature_corrected)));

                Float Uc_RCf = rh[index] *
                        (float) (BaronCommons.calculatePsat(new Double(temperature_corrected + cor_temp * (f + uF)))
                                - BaronCommons.calculatePsat(new Double(temperature_corrected + cor_temp * (f - uF))))
                        / (float) (2 * BaronCommons.calculatePsat(new Double(temperature_corrected)));

                if (sza < -5.0f) { //NIGHT
                    u_cor_rh = (float) Math.sqrt(Math.pow(u_cor_rh_tl, 2) + Math.pow(randomErr_rh, 2));
                } else {
                    if (deltaTgruan.isNaN() || deltaTgruan.equals(0.0f)) {
                        Float u_cor_rh_previousProfile = getPrevious_u_cor_ECV(productVariables, previousProducts, BaronCommons.ECV.RH);

                        u_cor_rh = (float) Math.sqrt(Math.pow(u_cor_rh_previousProfile, 2));
                    } else {
                        u_cor_rh = (float) Math.sqrt(Math.pow(Uc_RCt, 2) + Math.pow(Uc_RCf, 2) + Math.pow(u_cor_rh_tl, 2) + Math.pow(randomErr_rh, 2));
                    }
                }
            } else {
                u_cor_intercomparison_rh = (float) Math.sqrt(Math.pow(productVariables.getWmoIntercomparison_RH().getStd_dev() * rh[index], 2) + Math.pow(u_cor_rh_tl, 2) + Math.pow(randomErr_rh, 2));

                u_cor_rh = u_cor_intercomparison_rh;

                productVariables.setU_cor_intercomparison_rh(u_cor_intercomparison_rh);
            }
        }

        u_cor_rh_tl_list.add(u_cor_rh_tl);
        productVariables.setU_cor_rh_tl(u_cor_rh_tl);
        u_cor_intercomparison_rh_list.add(u_cor_intercomparison_rh);

        if (u_cor_rh != null && !u_cor_rh.isNaN()) {
            u_cor_rh = new Float(DoubleRounder.round(u_cor_rh, 4));
        }

        u_cor_rh_list.add(u_cor_rh);

        productVariables.setU_cor_rh(u_cor_rh);
    }

    private void doCorrection_u_cor_temp(ProductVariables productVariables, List<Product> previousProducts) {
        Float u_cor_temp_tl;
        Float u_cor_temp;
        Float cor_temp_toPrint;
        Float randomErr_temp = 0.15f;

        Boolean isRS92 = productVariables.getRS92();
        Optional<TlSmoothRs92> tlSmoothRs92Optional = productVariables.getTlSmoothRs92Optional();

        Float cor_temp = productVariables.getCor_temp();
        Float iaCloudy = productVariables.getIaCloudy();
        Float iaClear = productVariables.getIaClear();
        Float ia = productVariables.getIa();
        Float deltaTgruan = productVariables.getDeltaTgruan();

        u_cor_temp_tl = tlSmoothRs92Optional.isPresent() ? tlSmoothRs92Optional.get().getU_cor_temp_tl() : 0.0f;

        if (isRS92) {
            u_cor_temp = 0.0f;

            cor_temp_toPrint = cor_temp_list.get(cor_temp_list.size() - 1);

            if (cor_temp_toPrint != 0.0f) {
                Float Uc_of_Ia = Math.abs(iaCloudy - iaClear) / (float) (2 * Math.sqrt(3));
                Float Uc_Ia = 0.0f;

                //fixed ascending speed 5.0 m/s
                Float U_v = 1.0f;
                Float Uc_Vent = 0.0f;

                Uc_Vent = cor_temp * U_v / 5.0f;
                Float u_rot = (2 * cor_temp) / (float) Math.sqrt(3);

                if (sza < -5.0f) { //NIGHT
                    u_cor_temp = (float) Math.sqrt(Math.pow(Uc_Vent, 2) + Math.pow(u_cor_temp_tl, 2) + Math.pow(randomErr_temp, 2) + Math.pow(u_rot, 2));
                } else if (((Uc_of_Ia == 0 && ia == 0) || (((Uc_of_Ia / ia) >= 1) || (deltaTgruan.isNaN() || deltaTgruan.equals(0.0f))))) {
                    Float u_cor_temp_previousProfile = getPrevious_u_cor_ECV(productVariables, previousProducts, BaronCommons.ECV.TEMP);

                    u_cor_temp = u_cor_temp_previousProfile;
                } else if ((Uc_of_Ia / ia) < 1) {
                    if (ia > 0) {
                        Uc_Ia = cor_temp * (Uc_of_Ia) / ia;
                    }

                    Float Uc_RC = 0.2f;

                    u_cor_temp = (float) Math.sqrt(Math.pow(Uc_Ia, 2) + Math.pow(Uc_Vent, 2) + Math.pow(Uc_RC, 2) + Math.pow(u_cor_temp_tl, 2) + Math.pow(randomErr_temp, 2) + Math.pow(u_rot, 2));
                }
            }
        } else {
            u_cor_temp = (float) Math.sqrt(Math.pow(productVariables.getWmoIntercomparison_TEMP().getStd_dev(), 2) + Math.pow(u_cor_temp_tl, 2) + Math.pow(randomErr_temp, 2));

            u_cor_intercomparison_temp_list.add(u_cor_temp);

            productVariables.setU_cor_intercomparison_temp(u_cor_temp);
        }

        u_cor_temp_tl_list.add(u_cor_temp_tl);
        productVariables.setU_cor_temp_tl(u_cor_temp_tl);

        if (!u_cor_temp.isNaN()) {
            u_cor_temp = new Float(DoubleRounder.round(u_cor_temp, 2));
        }

        u_cor_temp_list.add(u_cor_temp);

        productVariables.setU_cor_temp(u_cor_temp);
    }

    private Float getPrevious_u_cor_ECV(ProductVariables productVariables, List<Product> previousProducts, BaronCommons.ECV ecv) {
        Float u_cor_ECV_previousProfile = null;

        //Looking for the closest profile in the past at the same pressure and in the same cloud conditions
        List<Product> previousProductsFiltered = previousProducts.parallelStream()
                .filter(x -> x.getPress().equals(productVariables.getPress())
                        && x.getIsCloudy().equals(productVariables.getCloudy()))
                .sorted(Comparator.comparing(Product::getDateOfObservation))
                .collect(Collectors.toList());

        for (int i = previousProductsFiltered.size() - 1; i > -1 && previousProductsFiltered.size() > 0; i--) {
            Product previousProduct = previousProductsFiltered.get(i);

            Float previousProduct_u_cor_ECV = null;
            switch (ecv) {
                case TEMP:
                    previousProduct_u_cor_ECV = previousProduct.getU_cor_temp();
                    break;
                case RH:
                    previousProduct_u_cor_ECV = previousProduct.getU_cor_rh();
                    break;
            }

            if (previousProduct_u_cor_ECV != null && previousProduct_u_cor_ECV != Float.NaN) {
                if (previousProduct_u_cor_ECV > 0) {
                    u_cor_ECV_previousProfile = previousProduct_u_cor_ECV;
                    break;
                }
            }
        }
        if (u_cor_ECV_previousProfile == null) {
            switch (ecv) {
                case TEMP:
                    u_cor_ECV_previousProfile = 0.5f;
                    break;
                case RH:
                    u_cor_ECV_previousProfile = 0.05f;
                    break;
            }
        }

        return u_cor_ECV_previousProfile;
    }

    private void doCorrection_wvmr(Integer index, ProductVariables productVariables) {
        Float wvmr_corrected = null;

        if (dpdp[index] != null) {
            Float temperature_corrected = temperatureCorrected_list.get(temperatureCorrected_list.size() - 1);
            Float dewpoint_temperature_corrected = temperature_corrected - dpdp[index];

            wvmr_corrected = BaronCommons.calculateWvmrFromDewpoint(dewpoint_temperature_corrected, press[index]);
        }

        wvmr_corrected_list.add(wvmr_corrected);

        productVariables.setWvmrCorrected(wvmr_corrected);
    }

    private void doCorrection_v(Integer index, ProductVariables productVariables) {
        Boolean isRS92 = productVariables.getRS92();
        Float cor_v = null;
        Float cor_v_toPrint = 0.0f;
        Float v_corrected = null;

        Optional<WindRs92Corr> windRs92CorrOptional = productVariables.getWindRs92CorrOptional();

        if (!(v[index] == null || v[index].isNaN() || v[index].equals(-9999.0f) || v[index].equals(-8888.0f))) {
            if (isRS92) {
                cor_v = windRs92CorrOptional.isPresent() ? windRs92CorrOptional.get().getCor_u() : 0.0f;
            } else {
                cor_v = productVariables.getWmoIntercomparison_V().getMean();
            }

            cor_v_toPrint = cor_v;

            v_corrected = v[index] + cor_v;

            productVariables.setCor_v(cor_v);
        }

        cor_v_list_rs92.add(cor_v);
        cor_v_list_notRs92.add(cor_v);

        if (!cor_v_toPrint.isNaN()) {
            cor_v_toPrint = new Float(DoubleRounder.round(cor_v_toPrint, 2));
        }

        if (v_corrected != null) {
            v_corrected = new Float(DoubleRounder.round(v_corrected, 2));
        }

        cor_v_list.add(cor_v_toPrint);
        vCorrected_list.add(v_corrected);

        productVariables.setCor_v(cor_v);
        productVariables.setV_corrected(v_corrected);
        productVariables.setCor_v_toPrint(cor_v_toPrint);
    }

    private void doCorrection_u(Integer index, ProductVariables productVariables) {
        Boolean isRS92 = productVariables.getRS92();
        Float cor_u = null;
        Float cor_u_toPrint = 0.0f;
        Float u_corrected = null;

        Optional<WindRs92Corr> windRs92CorrOptional = productVariables.getWindRs92CorrOptional();

        if (!(u[index] == null || u[index].isNaN() || u[index].equals(-9999.0f) || u[index].equals(-8888.0f))) {
            if (isRS92) {
                cor_u = windRs92CorrOptional.isPresent() ? windRs92CorrOptional.get().getCor_u() : 0.0f;
            } else {
                cor_u = productVariables.getWmoIntercomparison_U().getMean();
            }

            cor_u_toPrint = cor_u;

            u_corrected = u[index] + cor_u;

            productVariables.setCor_u(cor_u);
        }

        cor_u_list_rs92.add(cor_u);
        cor_u_list_notRs92.add(cor_u);

        if (!cor_u_toPrint.isNaN()) {
            cor_u_toPrint = new Float(DoubleRounder.round(cor_u_toPrint, 2));
        }

        if (u_corrected != null) {
            u_corrected = new Float(DoubleRounder.round(u_corrected, 2));
        }

        cor_u_list.add(cor_u_toPrint);
        uCorrected_list.add(u_corrected);

        productVariables.setCor_u(cor_u);
        productVariables.setU_corrected(u_corrected);
        productVariables.setCor_u_toPrint(cor_u_toPrint);
    }

    private void doCorrection_rh(Integer index, ProductVariables productVariables) {
        Float cor_rh_tl = null;
        Float cor_rh = null;
        Float rh_corrected = null;
        Float temperature_corrected;
        Float cor_rh_toPrint = null;
        Float cor_intercomparison_rh = null;

        Boolean isRS92 = productVariables.getRS92();
        Optional<TlSmoothRs92> tlSmoothRs92Optional = productVariables.getTlSmoothRs92Optional();

        if (rh[index] != null && !rh[index].isNaN() && rh[index] > -8888.0f && (press[index] >= 250.0f)) {
            if (sza < -5.0f) { //is NIGHT
                cor_rh_tl = tlSmoothRs92Optional.isPresent() ? tlSmoothRs92Optional.get().getCor_rh_tl_night() : 0.0f;
            } else {
                cor_rh_tl = tlSmoothRs92Optional.isPresent() ? tlSmoothRs92Optional.get().getCor_rh_tl_day() : 0.0f;
            }

            if (isRS92) {
                if (productVariables.getDeltaTgruan().isNaN() || productVariables.getDeltaTgruan().equals(0.0f)) {
                    productVariables.setCor_temp(productVariables.getCor_temp());
                }

                if (productVariables.getDeltaTgruan().isNaN() || productVariables.getDeltaTgruan().equals(0.0f)) {
                    cor_rh = 0.0f;
                    rh_corrected = rh[index] * cor_rh_tl;
                } else {
                    temperature_corrected = temperatureCorrected_list.get(temperatureCorrected_list.size() - 1);

                    Float cor_temp_tl = cor_temp_tl_list.get(cor_temp_tl_list.size() - 1);

                    Double pSatNumeratore = BaronCommons.calculatePsat(new Double(temperature_corrected + (f * (temperature_corrected - temp[index] - cor_temp_tl))));
                    Double pSatDenominatore = BaronCommons.calculatePsat(new Double(temperature_corrected));
                    productVariables.setpSatNumeratore(pSatNumeratore);
                    productVariables.setpSatDenominatore(pSatDenominatore);

                    cor_rh = (float) (BaronCommons.calculatePsat(new Double(temperature_corrected + (f * (temperature_corrected - temp[index] - cor_temp_tl))))
                            / BaronCommons.calculatePsat(new Double(temperature_corrected)));

                    rh_corrected = (rh[index] * cor_rh_tl) * cor_rh;
                }

                cor_rh_toPrint = (rh_corrected - rh[index]) * 100;

                if (cor_rh_toPrint.equals(0.0f)) {
                    cor_rh_tl = 0.0f;
                }

                productVariables.setCor_rh_tl(cor_rh_tl);
            } else {
                cor_intercomparison_rh = productVariables.getWmoIntercomparison_RH().getMean();

                cor_rh = cor_intercomparison_rh;
                cor_rh_toPrint = cor_intercomparison_rh;

                rh_corrected = rh[index] * cor_rh;

                productVariables.setCor_intercomparison_rh(cor_intercomparison_rh);
            }
        }

        cor_rh_tl_list.add(cor_rh_tl);
        cor_intercomparison_rh_list.add(cor_intercomparison_rh);

        if (cor_rh_toPrint != null && !cor_rh_toPrint.isNaN()) {
            cor_rh_toPrint = new Float(DoubleRounder.round(cor_rh_toPrint, 4));
        }

        if (rh_corrected != null) {
            rh_corrected = new Float(DoubleRounder.round(rh_corrected, 4));

            if (rh_corrected > 1.0f) {
                rh_corrected = 1.0f;
            }
        }

        cor_rh_list.add(cor_rh_toPrint);
        rhCorrected_list.add(rh_corrected);

        productVariables.setCor_rh(cor_rh);
        productVariables.setRhCorrected(rh_corrected);
        productVariables.setCor_rh_toPrint(cor_rh_toPrint);
    }

    private void doCorrection_temp(Integer index, ProductVariables productVariables) {
        Float cor_temp_tl = null;
        Float cor_temp = 0.0f;
        Float temperature_corrected = 0.0f;
        Float cor_temp_toPrint = 0.0f;
        Float cor_intercomparison_temp = null;

        Boolean isRS92 = productVariables.getRS92();
        Optional<TlSmoothRs92> tlSmoothRs92Optional = productVariables.getTlSmoothRs92Optional();
        Float deltaTgruan = productVariables.getDeltaTgruan();
        Float deltaTvaisala = productVariables.getDeltaTvaisala();

        if (!(temp[index].isNaN() || temp[index].equals(-9999.0f) || temp[index].equals(-8888.0f))) {
            if (isRS92) {
                cor_temp_tl = tlSmoothRs92Optional.isPresent() ? tlSmoothRs92Optional.get().getCor_temp_tl_bias() : 0.0f;

                if (deltaTgruan.isNaN() || deltaTgruan == 0.0f) {
                    cor_temp = deltaTvaisala == null ? 0.0f : deltaTvaisala;
                    temperature_corrected = temp[index] + cor_temp_tl;
                } else {
                    cor_temp = ((deltaTgruan + deltaTvaisala) / 2);
                    temperature_corrected = temp[index] + deltaTvaisala - cor_temp + cor_temp_tl;
                }
                cor_temp_toPrint = temperature_corrected - temp[index];

                productVariables.setCor_temp_tl(cor_temp_tl);
            } else {
                cor_intercomparison_temp = productVariables.getWmoIntercomparison_TEMP().getMean();

                cor_temp = cor_intercomparison_temp;
                cor_temp_toPrint = cor_intercomparison_temp;

                temperature_corrected = temp[index] + cor_temp;

                productVariables.setCor_intercomparison_temp(cor_intercomparison_temp);
            }
        }

        cor_temp_tl_list.add(cor_temp_tl);
        cor_intercomparison_temp_list.add(cor_intercomparison_temp);

        if (!temperature_corrected.isNaN()) {
            temperature_corrected = new Float(DoubleRounder.round(temperature_corrected, 2));
        }
        if (!cor_temp_toPrint.isNaN()) {
            cor_temp_toPrint = new Float(DoubleRounder.round(cor_temp_toPrint, 2));
        }

        temperatureCorrected_list.add(temperature_corrected);
        cor_temp_list.add(cor_temp_toPrint);

        productVariables.setCor_temp(cor_temp);
        productVariables.setTempCorrected(temperature_corrected);
        productVariables.setCor_temp_toPrint(cor_temp_toPrint);
    }

    private ProductVariables getProductVariables(Integer index, Boolean isRS92, List<WmoIntercomparison> wmoIntercomparisonList,
                                                 Integer indexStartCloud, Integer indexEndCloud, Integer year,
                                                 Integer radiosondeId) {
        ProductVariables productVariables = new ProductVariables();
        Float quota = Float.NaN;
        Float deltaTvaisala = null;
        Float iaClear = 0.0f;
        Float iaCloudy = 0.0f;
        Float ia = 0.0f;
        Float deltaTgruan = Float.NaN;
        Boolean isCloudy = false;

        Optional<TlSmoothRs92> tlSmoothRs92Optional = getTlSmoothRs92Corr(press[index]);
        Optional<WindRs92Corr> windRs92CorrOptional = getWindRs92Corr(press[index]);

        if (isRS92) {
            if (sza < -5.0f) {
                deltaTvaisala = 0.0f;
                iaClear = 0.0f;
                iaCloudy = 0.0f;
                ia = 0.0f;
            } else {
                quota = getQuota(index);

                if (quota != null && !quota.isNaN()) {
                    if (quota < 0.0f) {
                        quota = 0.0f;
                    }

                    deltaTvaisala = getDeltaTvaisala(sza, press[index], year);
                    iaClear = (float) commonStaticDataStructure.bivariateFunction_gruanRadiation_clear.value(sza, quota);
                    iaCloudy = (float) commonStaticDataStructure.bivariateFunction_gruanRadiation_cloudy.value(sza, quota);

                    ia = iaClear;
                    if (indexStartCloud != null && index >= indexStartCloud && index <= indexEndCloud) {
                        ia = iaCloudy;
                        isCloudy = true;
                    }

                    if (!ia.equals(0.0f)) {
                        //assumed ventilation speed 5.0 m/s
                        deltaTgruan = 0.18f * ((float) Math.pow(ia / (press[index] * 5.0f), 0.55));
                    } else {
                        deltaTgruan = 0.0f;
                    }

                }
            }

            productVariables.setDeltaTgruan(deltaTgruan);
            productVariables.setQuota(quota);
            productVariables.setDeltaTvaisala(deltaTvaisala);
            productVariables.setIaClear(iaClear);
            productVariables.setIaCloudy(iaCloudy);
            productVariables.setIa(ia);
        } else {
            productVariables.setWmoIntercomparison_TEMP(getWmoIntercomparison(wmoIntercomparisonList, press[index], BaronCommons.ECV.TEMP, radiosondeId));
            productVariables.setWmoIntercomparison_RH(getWmoIntercomparison(wmoIntercomparisonList, press[index], BaronCommons.ECV.RH, radiosondeId));
            productVariables.setWmoIntercomparison_U(getWmoIntercomparison(wmoIntercomparisonList, press[index], BaronCommons.ECV.U, radiosondeId));
            productVariables.setWmoIntercomparison_V(getWmoIntercomparison(wmoIntercomparisonList, press[index], BaronCommons.ECV.V, radiosondeId));
        }

        productVariables.setWindRs92CorrOptional(windRs92CorrOptional);
        productVariables.setTlSmoothRs92Optional(tlSmoothRs92Optional);
        productVariables.setCloudy(isCloudy);
        productVariables.setRS92(isRS92);
        productVariables.setSza(sza);
        productVariables.setF(f);
        productVariables.setTempRaw(temp[index]);
        productVariables.setPress(press[index]);
        productVariables.setGeopot(geopot[index]);
        productVariables.setWspeed(wspeed[index]);
        productVariables.setRhRaw(rh[index]);
        productVariables.setWvmrRaw(wvmr[index]);

        productVariables.setDateOfObservation(dateOfObservation[index]);

        return productVariables;
    }

    private WmoIntercomparison getWmoIntercomparison(List<WmoIntercomparison> wmoIntercomparisonList, Float press, BaronCommons.ECV ecv, Integer radiosondeId) {
        Optional<WmoIntercomparison> wmoIntercomparisonOptional = wmoIntercomparisonList.parallelStream()
                .filter(x -> x.getEcv().equals(ecv.name())
                        && x.getPress().equals(press)
                        && x.getDay().equals(sza < -5.0f ? false : true)
                        && x.getMean() > 0.0f
                        && x.getStd_dev() > 0.0f)
                .findAny();

        if (wmoIntercomparisonOptional.isPresent()) {
            return wmoIntercomparisonOptional.get();
        } else {
            return commonStaticDataStructure.wmoIntercomparisonList.parallelStream()
                    .filter(x -> x.getEcv().equals(ecv.name())
                            && x.getDay().equals(sza < -5.0f ? false : true)
                            && x.getSondeId().contains(radiosondeId.toString()))
                    .collect(Collectors.toList()).get(0);
        }
    }

    private float getDeltaTvaisala(double sza, double press, Integer year) {
        if (year <= 2009) {
            if (press > solarRadiation_yRow_2005[solarRadiation_yRow_2005.length - 1]) {
                solarRadiation_yRow_2005[solarRadiation_yRow_2005.length - 1] = press;

                bivariateFunction_solarRadiationTable = bilinearInterpolator.interpolate(solarRadiation_yRow_2005, solarRadiation_xRow_2005, solarRadiation_matrix_2005);
            }
        } else {
            if (press > solarRadiation_yRow_2010[solarRadiation_yRow_2010.length - 1]) {
                solarRadiation_yRow_2010[solarRadiation_yRow_2010.length - 1] = press;

                bivariateFunction_solarRadiationTable = bilinearInterpolator.interpolate(solarRadiation_yRow_2010, solarRadiation_xRow_2010, solarRadiation_matrix_2010);
            }
        }

        return (float) bivariateFunction_solarRadiationTable.value(press, sza);
    }

    private Float getQuota(Integer index) {
        Float quota = geopot[index];

        Integer indexValidGeopot;
        if (quota == null || new Double(quota).isNaN()) {
            indexValidGeopot = getIndexValidGeopot(index);

            if (indexValidGeopot < 0) {
                quota = null;
            } else {
                if (indexValidGeopot > index)
                    quota = getZ0_barometricFormula(geopot[indexValidGeopot], temp[indexValidGeopot], temp[index], press[indexValidGeopot], press[index]);
                else
                    quota = getZ_barometricFormula(geopot[indexValidGeopot], temp[index], temp[index], press[index], press[indexValidGeopot]);
            }
        }

        return quota;
    }

    private float getZ0_barometricFormula(float z, float t, float t0, float p, float p0) {
        /*
        (z - z0) = log(P/P0)*(- (RT/mg))

        z = height level z
        z0 = height level z0
        P = press height z
        P0 = press height z0
        R = universal gas constant: 8.3144598 N·m/(mol·K)
        T = Absolute temperature
        m = molar mass of Earth's air: 0.0289644 kg/mol
        g = gravitational acceleration: 9.80665 m/s2
         */

        return (float) (z - (Math.log((p / p0))) * (-((8.3144598 * t) / (0.0289644 * 9.80665))));
    }

    private float getZ_barometricFormula(float z0, float t, float t0, float p, float p0) {
        return (float) (z0 + (Math.log((p / p0))) * (-((8.3144598 * t) / (0.0289644 * 9.80665))));
    }

    private int getIndexValidGeopot(int currentIndex) {
        int indexValidGeopot = -1;

        for (int i = currentIndex; i < geopot.length; i++) {
            if (geopot[i] != null
                    && !new Double(geopot[i]).isNaN()
                    && !new Double(temp[i]).isNaN()
                    && !new Double(press[i]).isNaN()) {

                indexValidGeopot = i;
                break;
            }
        }

        if (indexValidGeopot == -1) {
            for (int i = currentIndex; i > -1; i--) {
                if (geopot[i] != null
                        && !new Double(geopot[i]).isNaN()
                        && !new Double(temp[i]).isNaN()
                        && !new Double(press[i]).isNaN()) {

                    indexValidGeopot = i;
                    break;
                }
            }
        }

        return indexValidGeopot;
    }

    private Optional<TlSmoothRs92> getTlSmoothRs92Corr(Float press) {
        return commonStaticDataStructure.tlSmoothRs92FullList
                .parallelStream()
                .filter(x -> x.getPressure().equals(press))
                .findAny();
    }

    private Optional<WindRs92Corr> getWindRs92Corr(Float press) {
        return commonStaticDataStructure.windRs92CorrFullList
                .parallelStream()
                .filter(x -> x.getPressure().equals(press) && x.getDay().equals(sza < -5.0f ? false : true))
                .findAny();
    }

    private Boolean isRS92(Integer radiosondeId) {
        return commonStaticDataStructure.rs92codesList.contains(radiosondeId);
    }

    private void setVariablesForCalculations(Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second, Float latitude, Float longitude) {
        sza = BaronCommons.calculateSza(year, month, day, hour, minute, second, latitude, longitude);
        szaIdl = BaronCommons.calculateSza_IDL(year, month, day, hour, minute, second, latitude, longitude);

        calculateFandUf(year);

        setupSolarRadiationTable(year);
    }

    private void calculateFandUf(Integer year) {
        if (year < 2006) {
            f = 13.0f;
            uF = 4.0f;
        } else if (year >= 2006 && year <= 2008) {
            f = 10.0f;
            uF = 3.0f;
        } else if (year >= 2009) {
            f = 6.5f;
            uF = 2.0f;
        }
    }

    private void setupSolarRadiationTable(Integer year) {
        if (year <= 2009) {
            bivariateFunction_solarRadiationTable = bilinearInterpolator.interpolate(solarRadiation_yRow_2005, solarRadiation_xRow_2005, solarRadiation_matrix_2005);
        } else {
            bivariateFunction_solarRadiationTable = bilinearInterpolator.interpolate(solarRadiation_yRow_2010, solarRadiation_xRow_2010, solarRadiation_matrix_2010);
        }
    }

    private void loadVariablesVectors(List<Measurement> measurements) {
        geopot = measurements.stream().map(x -> x.getGeopot()).toArray(Float[]::new);
        temp = measurements.stream().map(x -> x.getTemp()).toArray(Float[]::new);
        press = measurements.stream().map(x -> x.getPress()).toArray(Float[]::new);
        rh = measurements.stream().map(Measurement::getRh).toArray(Float[]::new);
        wspeed = measurements.stream().map(x -> x.getWspeed()).toArray(Float[]::new);
        wvmr = measurements.stream().map(x -> x.getWvmr()).toArray(Float[]::new);
        dpdp = measurements.stream().map(x -> x.getDpdp()).toArray(Float[]::new);
        u = measurements.stream().map(x -> x.getU()).toArray(Float[]::new);
        v = measurements.stream().map(x -> x.getV()).toArray(Float[]::new);
        dateOfObservation = measurements.stream().map(x -> x.getDateOfObservation()).toArray(Instant[]::new);
    }

    private void initLists() {
        temperatureCorrected_list = new ArrayList<>();
        cor_temp_list = new ArrayList<>();
        rhCorrected_list = new ArrayList<>();
        cor_rh_list = new ArrayList<>();
        wvmr_corrected_list = new ArrayList<>();
        u_cor_temp_list = new ArrayList<>();
        u_cor_rh_list = new ArrayList<>();
        cor_temp_tl_list = new ArrayList<>();
        u_cor_temp_tl_list = new ArrayList<>();
        cor_rh_tl_list = new ArrayList<>();
        u_cor_rh_tl_list = new ArrayList<>();
        cor_intercomparison_temp_list = new ArrayList<>();
        u_cor_intercomparison_temp_list = new ArrayList<>();
        cor_intercomparison_rh_list = new ArrayList<>();
        u_cor_intercomparison_rh_list = new ArrayList<>();
        cor_u_list_rs92 = new ArrayList<>();
        u_cor_u_list_rs92 = new ArrayList<>();
        cor_v_list_rs92 = new ArrayList<>();
        u_cor_v_list_rs92 = new ArrayList<>();
        cor_u_list_notRs92 = new ArrayList<>();
        u_cor_u_list_notRs92 = new ArrayList<>();
        cor_v_list_notRs92 = new ArrayList<>();
        u_cor_v_list_notRs92 = new ArrayList<>();
        u_cor_u_list = new ArrayList<>();
        u_cor_v_list = new ArrayList<>();
        cor_u_list = new ArrayList<>();
        uCorrected_list = new ArrayList<>();
        cor_v_list = new ArrayList<>();
        vCorrected_list = new ArrayList<>();
    }
}
