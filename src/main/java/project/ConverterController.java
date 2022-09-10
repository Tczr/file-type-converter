package project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import project.util.FileService;

import java.io.*;

@Controller
public class ConverterController {

//    private final Logger log = LoggerFactory.getLogger(ConverterController.class);
    @Autowired
    private final FileService fileService;

    public ConverterController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(path={"/",""})
    public String getHomePage(){
        return "home";
    }

    @PostMapping("/")
    public String doConversion(@RequestParam(name = "original-type") String originalType,
                               @RequestParam(name = "conversion-type") String conversionType,
                               @RequestParam(name = "file")MultipartFile file,
                               Model model) {


        try{
            final Resource originalFile =  fileService.copy(file.getInputStream(),file.getOriginalFilename());
            if(originalFile!=null)
            {

                final File convertedFile=  fileService.convert(conversionType, originalType).getFile();

                boolean isFilesDeleted=fileService.deleteAll();

                if(isFilesDeleted) System.out.println("Folder and its files deleted" );
                else System.out.println("Folder does not deleted");

                model.addAttribute("originalFile",originalFile.getFile());
                model.addAttribute("convertedFile",convertedFile);
                model.addAttribute("originalType",originalType);
                model.addAttribute("conversionType",conversionType);
            }

        }catch (IOException exc){
            exc.printStackTrace();
        }


        return "home";
    }

    @PostMapping("/default-conversion")
    public String defaultConversion(@RequestParam(name = "original-type") String originalType,
                               @RequestParam(name = "conversion-type") String conversionType,
                               @RequestParam(name = "file-path")String filePath,
                               RedirectAttributes redirect) {

        try{
            String path = this.getClass().getResource(filePath).getPath();
            File defaultFile = new File(path);

            if(defaultFile.isFile())
                System.out.println( "created a File on [ "+ path+ " ]" ) ;
            else
                System.out.println("Error creating filename: "+defaultFile.getName() );

            if(defaultFile!=null && defaultFile.isFile())
            {
                fileService.loadFile(defaultFile);
                final File convertedFile=  fileService.convert(conversionType, originalType).getFile();
                boolean isFilesDeleted=fileService.delete(convertedFile);

                if(isFilesDeleted) System.out.println( "Folder and its files deleted" );
                else System.out.println("Folder does not deleted");

                redirect.addFlashAttribute("originalFile",defaultFile);
                redirect.addFlashAttribute("convertedFile",convertedFile);
                redirect.addFlashAttribute("originalType",originalType);
                redirect.addFlashAttribute("conversionType",conversionType);
            }

        }catch (IOException exc){
            exc.printStackTrace();
        }


        return "redirect:/";
    }
}
