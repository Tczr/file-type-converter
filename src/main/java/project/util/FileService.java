package project.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import project.storage.SystemStorage;
import project.util.conversion.Conversion;
import project.util.conversion.ConversionPicker;

import java.io.*;
import java.net.URL;

@Service
public class FileService {
    private final URL HOME_DIRECTORY=this.getClass().getResource(SystemStorage.IMAGES_HOME.getPath());
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

    public boolean copy(InputStream stream, String fileName) {
         originalFile = new File(HOME_DIRECTORY.getPath() + (fileName.toUpperCase()));

        if (makeDirectory(originalFile)) {
            try (OutputStream writable = new FileOutputStream(originalFile)) {
                writable.write(stream.readAllBytes());
                return true;
            } catch (IOException exc) {
                throw new RuntimeException("Error happening while copping file name [ " + fileName + " ] Exception:", exc);
            }
        }
        return false;
    }

    public Resource convert(String conversionType, String originalType){
        if(conversionType.equalsIgnoreCase(originalType)) return null;

        conversionMethod = conversionPicker.getConversion(originalType);
        return conversionMethod.convert(originalFile, conversionType);
    }

    public boolean makeDirectory(File file){
        directory = new File(HOME_DIRECTORY.getPath()+file.getName().toUpperCase());
        if (!directory.isDirectory()){
            if(!directory.mkdir())
                throw new RuntimeException
                        ("Cannot create a directory for [ "+file.getName()+" ]" +
                        " on path [ "+HOME_DIRECTORY.getPath()+" ]");
            //return false;
        }
       return true;
    }

}
