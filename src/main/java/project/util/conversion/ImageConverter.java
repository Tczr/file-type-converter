package project.util.conversion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class ImageConverter implements Conversion{
//    private final Logger  log= LoggerFactory.getLogger(ImageConverter.class);
    @Override
    public Resource convert(File originalFile, String conversionType) {
        return chooseRightMethodToConvert(originalFile,conversionType);
    }

    private Resource chooseRightMethodToConvert(File originalFile,  String conversionType) {
        switch (conversionType){
            case "png": case "PNG" : case "jpg": case "JPG": return convertToImage(originalFile,conversionType);
            case "pdf": case"PDF": return convertToPDF(originalFile);
            default: return null;
        }
    }

    private Resource convertToImage(File originalFile, String conversionType) {
        File convertedFile = new File(originalFile.getPath().split("\\.")[0]+"-converted."+conversionType);

//        log.info("Created a file at [ "+convertedFile.getPath() +" ]");
        System.out.println("Created a file at [ "+convertedFile.getPath() +" ]");


        try {
            InputStream reader = new FileInputStream(originalFile);
            OutputStream converted = new FileOutputStream(convertedFile);

            converted.write(reader.readAllBytes());

            reader.close();
            converted.close();
            return new FileSystemResource(convertedFile);

        }catch (IOException exc){exc.printStackTrace();}

        return null;
    }


    //TODO(Tomorrow): do the implementation
    private Resource convertToPDF(File originalFile) {
        return null;
    }




}
