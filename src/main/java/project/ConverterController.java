package project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import project.storage.SystemStorage;
import project.util.FileService;
import project.util.Header;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.Properties;

@Controller
public class ConverterController {

//    private final Logger log = LoggerFactory.getLogger(ConverterController.class);
    @Autowired
    private final FileService fileService;
    @Autowired
    private final Header headerSetting ;

    public ConverterController(FileService fileService, Header header)
    {
        this.fileService = fileService;
        headerSetting=header;
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
            headerSetting.setToDefault();
            if (!file.isEmpty()){
                byte[] data = file.getBytes();

                final Resource originalFile =  fileService.copy(data,file.getOriginalFilename());

                System.out.println("is file exists: "+originalFile.exists());
                System.out.println("file path: "+originalFile.getURL());
                if(originalFile!=null)
                {

                    final File convertedFile=  fileService.convert(conversionType, originalType).getFile();

                    System.out.println("----Converted file----");
                    System.out.println("is file exists: "+convertedFile.exists());
                    System.out.println("file path: "+convertedFile.getAbsolutePath());

                    model.addAttribute("originalFile",originalFile.getFile());
                    model.addAttribute("convertedFile",convertedFile);
                    model.addAttribute("originalType",originalType);
                    model.addAttribute("conversionType",conversionType);
                }
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
                               RedirectAttributes redirect){


        try{
            headerSetting.setToDefault();

            InputStream defaultFile = this.getClass().getResourceAsStream(filePath);

            if (defaultFile!=null){
                System.out.println("default file path is:"+ filePath);
            }
            byte[] data = defaultFile.readAllBytes();
            Resource resource = fileService.copy(data,"testCase."+originalType);
            if(resource!=null && resource.exists())
            {
                System.out.println( "created a File on [ "+ resource.getURL()+ " ]" ) ;

                final File convertedFile=  fileService.convert(conversionType, originalType).getFile();

                redirect.addFlashAttribute("originalFile",resource.getFile());
                redirect.addFlashAttribute("convertedFile",convertedFile);
                redirect.addFlashAttribute("originalType",originalType);
                redirect.addFlashAttribute("conversionType",conversionType);
            }
            else System.out.println("Error creating filename: "+filePath);

        }catch (IOException exc){
            exc.printStackTrace();
        }


        return "redirect:/";
    }


    @GetMapping("/file/{filename}")
    @ResponseBody
    public void  downloadFile(@PathVariable("filename") String file, HttpServletResponse response){
        System.out.println("file download::"+file);
        String fileDirectory = fileService. getDirectoryPath();
        System.out.println("directory Path:"+fileDirectory);


        File file1 = new File(fileDirectory+ File.separator+file);
        System.out.println("is file exists:"+file1.exists());

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment; filename="+file);
        response.setHeader("Content-Transfer-Encoding","binary");
        try {
            BufferedOutputStream buffered = new BufferedOutputStream(response.getOutputStream());
            FileInputStream fis= new FileInputStream(file1);
            byte[] data = fis.readAllBytes();
            if(data.length>0){
                System.out.println("writing...");
                buffered.write(data);
            }
            buffered.close();
            response.flushBuffer();
        }catch (IOException exc){
            exc.printStackTrace();
        }
    }
}
