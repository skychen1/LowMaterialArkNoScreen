package org.wh.engineer.utils;

import java.io.File;

import static org.wh.engineer.base.App.REMOVE_LOGFILE_TIME;

public class LogFileUtils {

    public static void RemoveLogFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        deleteFilesByTimeOfDay(file, REMOVE_LOGFILE_TIME);
    }

    private static long getFileSize(File dir) {
        long fileSize = 0;
        if (!dir.isDirectory()) {
            fileSize += dir.length();
        } else {
            if (dir.listFiles() != null) {
                for (File file : dir.listFiles()) {
                    if (!file.isDirectory()) {
                        fileSize += file.length();
                    } else {
                        getFileSize(file);
                    }
                }
            }
        }
        return fileSize;
    }

    private static boolean deleteFiles(File dir) {
        if (dir == null && !dir.exists()) {
            return false;
        }
        if (dir.isFile() || dir.listFiles() == null) {
            dir.delete();
            return true;
        } else {
            int MAX_DELETE = 5;
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    if (MAX_DELETE == 0) {
                        break;
                    } else {
                        file.delete();
                        MAX_DELETE--;
                    }
                } else if (file.isDirectory()) {
                    deleteFiles(file);
                }
            }
        }
        return true;
    }

    private static boolean deleteFilesByTimeOfDay(File dir, int offDay) {
        if (dir == null && !dir.exists()) {
            return false;
        }
        if (dir.isFile() || dir.listFiles() == null) {
            dir.delete();
            return true;
        } else {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    if (isOffectDay(file, offDay)) {
                        continue;
                    } else {
                        file.delete();
                    }
                } else if (file.isDirectory()) {
                    deleteFiles(file);
                }
            }
        }
        return true;
    }

    private static boolean deleteFilesByTimeOfHour(File dir, int offHour) {
        if (dir == null && !dir.exists()) {
            return false;
        }
        if (dir.isFile() || dir.listFiles() == null) {
            dir.delete();
            return true;
        } else {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    if (!isOffectHour(file, offHour)) {
                        continue;
                    } else {
                        file.delete();
                    }
                } else if (file.isDirectory()) {
                    deleteFiles(file);
                }
            }
        }
        return true;
    }

    /**
     * 是否为指定天数前文件
     *
     * @param file
     * @param offDay
     * @return
     */
    private static boolean isOffectDay(File file, int offDay) {
        return TimeUtil.getOffectDay(System.currentTimeMillis(), file.lastModified()) < offDay;
    }

    /**
     * 是否为指定小时数前文件
     *
     * @param file
     * @param offHour
     * @return
     */
    private static boolean isOffectHour(File file, int offHour) {
        return TimeUtil.getOffectHour(System.currentTimeMillis(), file.lastModified()) > offHour;
    }

}
