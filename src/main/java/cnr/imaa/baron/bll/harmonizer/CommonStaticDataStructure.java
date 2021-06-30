package cnr.imaa.baron.bll.harmonizer;

import cnr.imaa.baron.commons.BilinearInterpolator;
import cnr.imaa.baron.model.*;
import cnr.imaa.baron.repository.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.analysis.BivariateFunction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommonStaticDataStructure {
    private DataSource dataSource;

    public List<SondeHistory> sondeHistory_all;
    public List<SondeHistoryECMWF> sondeHistoryECMWF_all;
    public List<Table3685> table3685List;
    public List<WndeqHistory> wndeq_all;
    public List<WmoIntercomparison> wmoIntercomparisonList;
    public List<WmoMinPressureLevel> wmoMinPressureLevelList;
    public Map<String, String> guanDataHeaderSourceMap;
    public List<TlSmoothRs92> tlSmoothRs92FullList;
    public List<WindRs92Corr> windRs92CorrFullList;
    public ArrayList<Double> pressionMandatoryLevelsList;
    public List<Integer> rs92codesList;
    public BivariateFunction bivariateFunction_gruanRadiation_clear;
    public BivariateFunction bivariateFunction_gruanRadiation_cloudy;

    public CommonStaticDataStructure(String rs92codes, DataSource dataSourceInput) {
        this.dataSource = dataSourceInput;

        table3685List = (List<Table3685>) new Table3685DAO(dataSource).getAll();
        sondeHistory_all = (List<SondeHistory>) new SondeHistoryDAO(dataSource).getAll();
        sondeHistoryECMWF_all = (List<SondeHistoryECMWF>) new SondeHistoryECMWFDAO(dataSource).getAll();
        wndeq_all = (List<WndeqHistory>) new WndeqHistoryDAO(dataSource).getAll();
        wmoMinPressureLevelList = (List<WmoMinPressureLevel>) new WmoMinPressureLevelDAO(dataSource).getAll();

        tlSmoothRs92FullList = (List<TlSmoothRs92>) new TlSmoothRs92DAO(dataSource).getAll();
        windRs92CorrFullList = (List<WindRs92Corr>) new WindRs92CorrDAO(dataSource).getAll();

        wmoIntercomparisonList = Collections.synchronizedList(new ArrayList<>());
        wmoIntercomparisonList.addAll((List<WmoIntercomparison>) new WmoIntercomparisonDAO(dataSource).getAll());

        pressionMandatoryLevelsList = new ArrayList<>();
        pressionMandatoryLevelsList.add(1000.0);
        pressionMandatoryLevelsList.add(925.0);
        pressionMandatoryLevelsList.add(850.0);
        pressionMandatoryLevelsList.add(700.0);
        pressionMandatoryLevelsList.add(500.0);
        pressionMandatoryLevelsList.add(400.0);
        pressionMandatoryLevelsList.add(300.0);
        pressionMandatoryLevelsList.add(250.0);
        pressionMandatoryLevelsList.add(200.0);
        pressionMandatoryLevelsList.add(150.0);
        pressionMandatoryLevelsList.add(100.0);
        pressionMandatoryLevelsList.add(70.0);
        pressionMandatoryLevelsList.add(50.0);
        pressionMandatoryLevelsList.add(30.0);
        pressionMandatoryLevelsList.add(20.0);
        pressionMandatoryLevelsList.add(10.0);

        //filter the data corresponding to the mandatory pressures
        tlSmoothRs92FullList = tlSmoothRs92FullList
                .parallelStream()
                .filter(x -> pressionMandatoryLevelsList.contains(x.getPressure().doubleValue()))
                .collect(Collectors.toList());

        windRs92CorrFullList = windRs92CorrFullList
                .parallelStream()
                .filter(x -> pressionMandatoryLevelsList.contains(x.getPressure().doubleValue()))
                .collect(Collectors.toList());

        //filter also form mean and stdDev > 0
        wmoIntercomparisonList = wmoIntercomparisonList
                .stream()
                .filter(x -> pressionMandatoryLevelsList.contains(x.getPress().doubleValue())
                        && x.getMean() > 0
                        && x.getStd_dev() > 0)
                .collect(Collectors.toList());

        List<WmoIntercomparison> wmoIntercomparisonListWithSondeIdToSplit = wmoIntercomparisonList
                .parallelStream()
                .filter(x -> x.getSondeId().contains(","))
                .collect(Collectors.toList());

        wmoIntercomparisonList.removeIf(x -> x.getSondeId().contains(","));

        wmoIntercomparisonListWithSondeIdToSplit
                .stream()
                .forEach(x -> {
                    wmoIntercomparisonList.add(
                            new WmoIntercomparison(
                                    x.getId(), x.getPress(),
                                    x.getSondeId().split(",")[0],
                                    x.getSonde_code(), x.getSonde_name(), x.getMean(), x.getStd_dev(), x.getEcv(), x.getDay())
                    );
                    wmoIntercomparisonList.add(
                            new WmoIntercomparison(
                                    x.getId(), x.getPress(),
                                    x.getSondeId().split(",")[1],
                                    x.getSonde_code(), x.getSonde_name(), x.getMean(), x.getStd_dev(), x.getEcv(), x.getDay())
                    );
                });

        rs92codesList = Arrays.asList(
                rs92codes.split(","))
                .parallelStream()
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());

        guanDataHeaderSourceMap = ((List<GuanDataHeaderSource>) new GuanDataHeaderSourceDAO(dataSource).getAll())
                .parallelStream()
                .collect(Collectors.toMap(GuanDataHeaderSource::getCode, GuanDataHeaderSource::getValue));

        setupMatrixGruanRadiation();
    }

    private void setupMatrixGruanRadiation() {
        List<GruanRadiation> gruanRadiationList = (List<GruanRadiation>) new GruanRadiationDAO(dataSource).getAll();
        Set<Double> gruanRadiation_setSza = Collections.synchronizedSet(new HashSet<>());
        Set<Double> gruanRadiation_setQuota = Collections.synchronizedSet(new HashSet<>());

        gruanRadiationList
                .stream()
                .forEach(x -> {
                    gruanRadiation_setSza.add(x.getSza());
                    gruanRadiation_setQuota.add(x.getQuota());
                });

        double[][] matrixGruanRadiation_Clear = new double[gruanRadiation_setSza.size()][gruanRadiation_setQuota.size()];
        double[][] matrixGruanRadiation_Cloudy = new double[gruanRadiation_setSza.size()][gruanRadiation_setQuota.size()];

        final List<Double> gruanRadiation_listSza = gruanRadiation_setSza
                .stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        final List<Double> gruanRadiation_listQuota = gruanRadiation_setQuota
                .stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        IntStream.range(0, gruanRadiation_listSza.size())
                .forEach(i -> {
                    List<GruanRadiation> gruanRadiationListFilteredBySza = gruanRadiationList
                            .stream()
                            .filter(x -> x.getSza().equals(gruanRadiation_listSza.get(i)))
                            .collect(Collectors.toList());
                    IntStream.range(0, gruanRadiation_listQuota.size())
                            .forEach(j -> {
                                Map<Double, GruanRadiation> gruanRadiationListFilteredBySzaMappedByQuota = gruanRadiationListFilteredBySza
                                        .stream()
                                        .collect(Collectors.toMap(GruanRadiation::getQuota, gruanRadiation -> gruanRadiation));

                                matrixGruanRadiation_Clear[i][j] = gruanRadiationListFilteredBySzaMappedByQuota
                                        .get(gruanRadiation_listQuota.get(j))
                                        .getClear();

                                matrixGruanRadiation_Cloudy[i][j] = gruanRadiationListFilteredBySzaMappedByQuota
                                        .get(gruanRadiation_listQuota.get(j))
                                        .getCloudy();
                            });
                });

        Double[] xVector_gruanRadiation = gruanRadiation_listSza.toArray(new Double[gruanRadiation_listSza.size()]);
        Double[] yVector_gruanRadiation = gruanRadiation_listQuota.toArray(new Double[gruanRadiation_listQuota.size()]);

        BilinearInterpolator bilinearInterpolator = new BilinearInterpolator();

        bivariateFunction_gruanRadiation_clear = bilinearInterpolator.interpolate(ArrayUtils.toPrimitive(xVector_gruanRadiation), ArrayUtils.toPrimitive(yVector_gruanRadiation), matrixGruanRadiation_Clear);
        bivariateFunction_gruanRadiation_cloudy = bilinearInterpolator.interpolate(ArrayUtils.toPrimitive(xVector_gruanRadiation), ArrayUtils.toPrimitive(yVector_gruanRadiation), matrixGruanRadiation_Cloudy);
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
