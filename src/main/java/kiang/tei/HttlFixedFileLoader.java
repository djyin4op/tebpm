package kiang.tei;

import httl.Resource;
import httl.spi.loaders.AbstractLoader;
import httl.spi.loaders.resources.FileResource;
import httl.util.UrlUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by wyyindongjiang on 2014/12/15.
 */
public class HttlFixedFileLoader extends AbstractLoader {

    public List<String> doList(String directory, String suffix) throws IOException {
        File file = new File(directory);
        return UrlUtils.listFile(file, suffix);
    }

    protected Resource doLoad(String name, Locale locale, String encoding, String path) throws IOException {
        return new FileResource(getEngine(), name, locale, encoding, path);
    }

    public boolean doExists(String name, Locale locale, String path) throws IOException {
        return new File(path).exists();
    }

}