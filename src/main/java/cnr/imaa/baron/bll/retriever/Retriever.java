package cnr.imaa.baron.bll.retriever;

import cnr.imaa.baron.bll.Task;
import cnr.imaa.baron.commons.Storage;
import cnr.imaa.baron.model.BaseData;
import cnr.imaa.baron.model.BaseHeader;
import cnr.imaa.baron.repository.BaseDataDAO;
import cnr.imaa.baron.repository.BaseHeaderDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public abstract class Retriever extends Task {
    private static final Logger log = LoggerFactory.getLogger(Retriever.class);

    private boolean importDataFromDisk = Boolean.parseBoolean(this.getFileResourceUtils().getConfig().get("importDataFromDisk"));
    private String pathData = this.getFileResourceUtils().getConfig().get("pathData");
    private Boolean saveSourceFile = Boolean.parseBoolean(this.getFileResourceUtils().getConfig().get("saveSourceFile"));
    private String pathDestination = this.getFileResourceUtils().getConfig().get("pathDestination");
    private String remotePath = this.getFileResourceUtils().getConfig().get("remotePath");

    @Override
    public void run(String... args) {
        log.debug("Retriever START");
        Instant start = Instant.now();

        //The sw in its normal operation loads the parameters from the configuration file but also allows you to value the same variables
        // by taking them as input from the command line.
        loadArgs(args);

        if (importDataFromDisk) {
            importFromDisk();
        } else {
            importFromRemote(remotePath);
        }

        Instant finish = Instant.now();
        log.info("Elapsed time: " + Duration.between(start, finish).toMillis());

        log.debug("Retriever END");
    }

    protected void process(byte[] arr, String file, String remotePath) {
        if (saveSourceFile) {
            saveOnDisk(file, arr, remotePath);
        }
        Object obj = convertFileToBean(arr, file);

        if (!isBeanEmpty(obj)) {
            saveBean(obj);

            log.info("SAVED: " + file);
        } else {
            log.info("SKIPPED: " + file);
        }
    }

    private Boolean isBeanEmpty(Object obj) {
        if (obj == null)
            return true;

        return obj instanceof List && ((List<Object>) obj).size() <= 0;
    }

    private void saveOnDisk(String pathFile, byte[] arr, String remotePath) {
        try {
            String filename = pathFile.substring(pathFile.lastIndexOf("/") + 1);

            Storage.checkDirectory(pathDestination + "/" + pathFile.replace(remotePath + "/", "").replace(filename, ""));
            String localePathFull = pathDestination + "/" + pathFile.replace(remotePath + "/", "");

            OutputStream os = new FileOutputStream(localePathFull);

            os.write(arr);
            os.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadArgs(String... args) {
        for (int i = ARGS.IMPORT_DATA_FROM_DISK.position; i < ARGS.values().length && args.length > i; i++) {
            if (i == ARGS.IMPORT_DATA_FROM_DISK.position && args[ARGS.IMPORT_DATA_FROM_DISK.position] != null) {
                importDataFromDisk = Boolean.parseBoolean(args[ARGS.IMPORT_DATA_FROM_DISK.position].trim());
            } else if (i == ARGS.PATH_DATA.position && args[ARGS.PATH_DATA.position] != null) {
                pathData = args[ARGS.PATH_DATA.position].trim();
            } else if (i == ARGS.SAVE_SOURCE_FILE.position && args[ARGS.SAVE_SOURCE_FILE.position] != null) {
                saveSourceFile = Boolean.parseBoolean(args[ARGS.SAVE_SOURCE_FILE.position].trim());
            } else if (i == ARGS.PATH_DESTINATION.position && args[ARGS.PATH_DESTINATION.position] != null) {
                pathDestination = args[ARGS.PATH_DESTINATION.position].trim();
            } else if (i == ARGS.REMOTE_PATH.position && args[ARGS.REMOTE_PATH.position] != null) {
                remotePath = args[ARGS.REMOTE_PATH.position].trim();
            }
        }
    }

    private void importFromDisk() {
        Path start = Paths.get(pathData);
        Stream<Path> stream;
        try {
            stream = Files.walk(start, Integer.MAX_VALUE);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        List<String> collect = stream
                .map(String::valueOf)
                .sorted()
                .collect(Collectors.toList());

        collect
//                .stream()
                .parallelStream()
                .forEach(file -> {
                    try {
                        if (!Files.isDirectory(Paths.get(file))) {
                            process(Files.readAllBytes(Paths.get(file)), file, null);
                        }
                    } catch (Exception ex) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        ex.printStackTrace(pw);
                        String sStackTrace = sw.toString();

                        log.error("ERROR: " + sStackTrace);
                    }
                });
    }

    protected abstract List<String> retrieveFileList(String remotePath);

    protected abstract byte[] retrieveFile(String urlFile);

    protected abstract Object convertFileToBean(byte[] arr, String file);

    protected abstract void saveBean(Object obj);

    protected abstract void importFromRemote(String remotePath);

    protected abstract BaseHeaderDAO getBaseHeaderDAO();

    protected abstract BaseDataDAO getBaseDataDAO();

    protected List<BaseData> getBaseHeadersToSave(List<BaseData> baseDataList) {
        Map<BaseHeader, List<BaseData>> baseDataListGroupedByBaseHeader = baseDataList
                .parallelStream()
                .collect(groupingBy(BaseData::getHeader));

        BaseData baseData = baseDataList.get(0);

        List<BaseHeader> baseHeaderDBList = (List<BaseHeader>) getBaseHeaderDAO().getByIdStation(baseData.getHeader());
        if (baseHeaderDBList == null || baseHeaderDBList.size() == 0) {
            return baseDataList;
        } else {
            List<BaseData> baseDataListDB = (List<BaseData>) getBaseDataDAO().getByIdStation(baseData);

            Map<Instant, List<BaseData>> baseDataListDBGroupedByDate = baseDataListDB
                    .parallelStream()
                    .collect(groupingBy(BaseData::getDateOfObservation));

            Set<Instant> dateListDB = baseDataListDB
                    .parallelStream()
                    .map(BaseData::getDateOfObservation)
                    .collect(Collectors.toSet());

            final List<BaseData> baseDataListToSave = Collections.synchronizedList(new ArrayList<>());

            baseDataListGroupedByBaseHeader.entrySet()
                    .parallelStream()
                    .forEach(k -> {
                        if (!dateListDB.contains(k.getKey().getDateOfObservation())) {
                            baseDataListToSave.addAll(k.getValue());
                        } else {
                            List<Object> fieldUniqueDBList = baseDataListDBGroupedByDate.get(k.getKey().getDateOfObservation())
                                    .parallelStream()
                                    .map(x -> x.getFieldUnique())
                                    .collect(Collectors.toList());

                            List<Object> fieldUniqueList = k.getValue()
                                    .parallelStream()
                                    .map(x -> x.getFieldUnique())
                                    .collect(Collectors.toList());

                            List<Object> fieldUniqueToSave = fieldUniqueList
                                    .parallelStream()
                                    .filter(press -> !fieldUniqueDBList.contains(press))
                                    .collect(Collectors.toList());

                            if (fieldUniqueToSave != null && fieldUniqueToSave.size() > 0) {
                                baseDataListToSave.addAll(
                                        k.getValue()
                                                .parallelStream()
                                                .filter(x -> fieldUniqueToSave.contains(x.getFieldUnique()))
                                                .collect(Collectors.toList())
                                );
                            }
                        }
                    });

            return baseDataListToSave;
        }
    }
}
