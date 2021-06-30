package cnr.imaa.baron.bll.retriever;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RetrieverFTP extends Retriever {
    private static final Logger log = LoggerFactory.getLogger(RetrieverFTP.class);

    private final FTPClient ftp = new FTPClient();

    private final String serverFTP = this.getFileResourceUtils().getConfig().get("serverFTP");
    private final String usernameFTP = this.getFileResourceUtils().getConfig().get("usernameFTP");
    private final String passwordFTP = this.getFileResourceUtils().getConfig().get("passwordFTP");

    protected void importFromRemote(String remotePathFTP) {
        List<String> ftpFiles = retrieveFileList(remotePathFTP);

        for (String ftpFile : ftpFiles) {
            byte[] arr = retrieveFile(ftpFile);

            process(arr, ftpFile, remotePathFTP);
        }
    }

    @Override
    public List<String> retrieveFileList(String remotePathFTP) {
        log.debug("retrieve START");

        if (!ftp.isConnected()) {
            connect();
        }

        List<String> ftpFiles = new ArrayList<>(retrieveFileListFromServer(remotePathFTP));

        disconnect();

        log.debug("retrieve END");

        return ftpFiles;
    }

    private List<String> retrieveFileListFromServer(String path) {
        List<String> ftpFiles = new ArrayList<>();

        try {
            String filenameToSearch = null;
            if (path.indexOf(".") >= 0) {
                //download a single file
                ftp.changeWorkingDirectory(path.substring(0, path.lastIndexOf("/")));
                filenameToSearch = (path.substring(path.lastIndexOf("/") + 1)).toUpperCase();
            } else {
                ftp.changeWorkingDirectory(path);
            }

            FTPFile[] filesList = ftp.listFiles();
            String workingDirectory = ftp.printWorkingDirectory();

            if (filenameToSearch != null) {
                final String filename = filenameToSearch;
                List<FTPFile> ftpFilesFound = Arrays.stream(filesList)
                        .filter(x -> x.getName().toUpperCase()
                                .equals(filename)).collect(Collectors.toList());

                if (ftpFilesFound != null && ftpFilesFound.size() == 1) {
                    ftpFiles.add(workingDirectory + "/" + ftpFilesFound.get(0).getName());
                }
            } else {
                for (FTPFile file : filesList) {
                    if (file.isDirectory()) {
                        log.debug(file.getName());
                        ftpFiles.addAll(retrieveFileListFromServer(file.getName()));

                        ftp.changeToParentDirectory();
                    } else {
                        ftpFiles.add(workingDirectory + "/" + file.getName());
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return ftpFiles;
    }

    protected byte[] retrieveFile(String urlFile) {
        InputStream inputStream;
        byte[] targetArray;

        try {
            if (!ftp.isConnected()) {
                connect();
            }

            inputStream = ftp.retrieveFileStream(urlFile);

            targetArray = IOUtils.toByteArray(inputStream);

            inputStream.close();

            ftp.completePendingCommand();

            disconnect();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return targetArray;
    }

    private void connect() {
        try {
            FTPClientConfig conf = new FTPClientConfig();
            conf.setServerTimeZoneId("UTC");

            ftp.configure(conf);

            ftp.connect(serverFTP);

            ftp.login(usernameFTP, passwordFTP);

            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();

            log.debug(ftp.getReplyString());

            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                log.error("FTP server refused connection.");
                System.exit(1);
            }

            log.debug("Connection established");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void disconnect() {
        if (ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (Exception ex) {
                log.error("ERROR: " + ex.getMessage());
            }
        }
    }
}
