package project.util.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversionPicker {

    private final PdfConverter pdfConverter;

    private final ImageConverter imageConverter;

    @Autowired
    public ConversionPicker(PdfConverter pdfConverter, ImageConverter imageConverter) {
        this.pdfConverter = pdfConverter;
        this.imageConverter = imageConverter;
    }

    public Conversion getConversion(String originalType){
        switch (originalType){
            case "png": case "PNG": case "jpg": case "JPG": return imageConverter;
            case "pdf" : case "PDF" : return pdfConverter;
            default : return null;
        }
    }
}
