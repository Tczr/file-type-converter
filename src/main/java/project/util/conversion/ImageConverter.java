package project.util.conversion;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class ImageConverter implements Conversion{

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
        File convertedFile = new File(originalFile.getPath()+"."+conversionType);

        if (!convertedFile.exists())
            throw new RuntimeException("Error creating file [ "+ convertedFile +" ] on ["+ originalFile.getPath()+" ]");

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

    /**
     *
     * @param originalFile
     * @return
     */
    //TODO(Tomorrow): do the implementation
    private Resource convertToPDF(File originalFile) {
        return null;
    }




}
