package project.util.conversion;

import org.springframework.core.io.Resource;

import java.io.File;

public interface Conversion {
    Resource convert(File originalFile, String conversionType);
}
