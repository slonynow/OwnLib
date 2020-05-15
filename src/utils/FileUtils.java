package utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class FileUtils {
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                return true;
            } else {
                System.out.println("Not Exist");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(String path) {
        File f = new File(path);
        if (!f.exists())
            return false;
        if (f.isFile()) {
            f.delete();
            return true;
        } else if (f.isDirectory()) {
            if (f.list().length != 0) {
                for (String s : f.list()) {
                    if (!delete(f.getPath() + File.separator + s))
                        return false;
                }
                f.delete();
                return true;
            } else {
                f.delete();
                return true;
            }
        } else
            return false;
    }

    public static boolean mkdir(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
            return true;
        }
        return false;
    }

    public static String createPasswordProtectedZip(String filepath, String zippath, String password) throws ZipException {
        //创建zip文件
        ZipFile zipFile = new ZipFile(zippath);
        //增加文件到zip中
        ArrayList<File> filesToAdd = new ArrayList<File>();
        filesToAdd.add(new File(filepath));
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        parameters.setPassword(password);
        zipFile.addFiles(filesToAdd, parameters);
        return zipFile.getFile().getPath();
    }
}
