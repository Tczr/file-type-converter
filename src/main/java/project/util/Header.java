package project.util;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;


@Service
public class Header {
    public void setToDefault(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control","no-cache, no-store, must-revalidate");
        headers.add("Pragma","no-cache");
        headers.add("Expires","0");
    }
    /*......*/
}
