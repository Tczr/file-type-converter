package project.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import project.storage.SystemStorage;
import project.util.conversion.Conversion;
import project.util.conversion.ConversionPicker;

import java.io.*;
import java.net.URL;
import java.util.Arrays;

@Service
public class FileService {
//    private final Logger log = LoggerFactory.getLogger(FileService.class);
    private final String HOME_DIRECTORY=SystemStorage.SYSTEM_DIR.getPath();
    private final ConversionPicker conversionPicker;
    private Conversion conversionMethod;
    private File originalFile;
    private File directory;

    @Autowired
    public FileService(ConversionPicker conversionPicker) {
        this.conversionPicker = conversionPicker;
    }


    public void loadFile(File file){

        this.originalFile=file;
    }

    public Resource copy(InputStream stream, String fileName) {

        String path = fileName.split("\\.")[0].replace(" ","-");

        if (makeDirectory(  new File(HOME_DIRECTORY + path)) ) {
            originalFile = new File(directory.getPath()+"\\"+fileName);
            try (OutputStream writable = new FileOutputStream(originalFile)) {
                writable.write(stream.readAllBytes());
                return new FileSystemResource(originalFile);
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
        return null;
    }
    public Resource copy(byte[] data, String fileName) {

        String path = fileName.split("\\.")[0].replace(" ","-");

        if (makeDirectory(  new File(HOME_DIRECTORY +File.separator+ path)) ) {
            originalFile = new File(directory.getPath()+File.separator+fileName);
            try (BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(originalFile)) ) {
                stream.write(data);
                stream.close();
                return new FileSystemResource(originalFile);
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
        return null;
    }

    public Resource convert(String conversionType, String originalType){
        if(conversionType.equalsIgnoreCase(originalType)) return null;

        conversionMethod = conversionPicker.getConversion(originalType);
        return conversionMethod.convert(originalFile, conversionType);
    }
    public String getDirectoryPath(){
        if(directory.exists()){
        return directory.getPath();}
        return null;
    }
    private boolean makeDirectory(File file){
        directory = new File(HOME_DIRECTORY+File.separator+file.getName().toUpperCase());
        if (!directory.exists()){
            directory.mkdir();
            if(!directory.exists()){
                throw new RuntimeException("Cannot create a directory for [ "+file.getName()+" ]" +
                        " on path [ "+file.getAbsolutePath()+" ]");}
              /*   log.error("Cannot create a directory for [ "+file.getName()+" ]" +
                        " on path [ "+HOME_DIRECTORY.getPath()+" ]");
            return false;*/
        }
       return true;
    }

    public boolean delete(File file){
        return file.delete();
    };
    public boolean deleteAll(){
        boolean deleted = false;

        if(directory!=null && directory.isDirectory()){
            if(directory.listFiles()!=null){
                for(File file : directory.listFiles()){
                    deleted=file.delete();
                }
            }
            deleted=directory.delete();
        }

        return deleted;
    }
}
