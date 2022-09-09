package project.util.conversion;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

@Service
public class PdfConverter implements Conversion{

    private Logger log= LoggerFactory.getLogger(PdfConverter.class);
    @Override
    public Resource convert(File originalFile, String conversionType) {
        return chooseRightMethodToConvert(originalFile,conversionType);
    }

    private Resource chooseRightMethodToConvert(File originalFile, String conversionType) {
        switch (conversionType){
            case "png": case "PNG" : case "jpg": case "JPG": return convertToImage(originalFile,conversionType);
            case "docx": case"DOCX": return convertToDOCX(originalFile);
            default: return null;
        }
    }


    //TODO(Tomorrow): Do the implementation
    private Resource convertToDOCX(File originalFile) {
        return null;
    }

    private Resource convertToImage(File originalFile, String conversionType) {
        try {
             File convertedFile = new File(originalFile
                    .getPath()
                    .split("\\.")[0]
                    +"-converted"+"."+conversionType);

            log.info("converting from pdf -> image the path of converted file in [ "+convertedFile.getPath()+" ] " +
                    "name of the file [ "+convertedFile.getName()+" ]");

            PDDocument document = PDDocument.load(originalFile);

            BufferedImage generatedImage  = renderPdf(document).renderImageWithDPI(0, 300);

            ImageIO.write(generatedImage,
                    conversionType,
                    convertedFile
                   );

            document.close();
            return new FileSystemResource(convertedFile);

        }catch (IOException exc){exc.printStackTrace();}

        return null;
        //To be completed....
    }

    private PDFRenderer renderPdf(PDDocument document) {
        return new PDFRenderer(document);
    }
}
