package project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import project.util.FileService;

@Controller
public class ConverterController {

    @Autowired
    private final FileService fileService;

    public ConverterController(FileService fileService) {
        this.fileService = fileService;
    }

}
