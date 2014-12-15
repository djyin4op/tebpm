package kiang.tei;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Resource;
import org.beetl.core.ResourceLoader;
import org.beetl.core.fun.FileFunctionWrapper;
import org.beetl.core.resource.ClasspathResource;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.File;
import java.net.URL;
import java.util.Map;

/**
 * Created by wyyindongjiang on 2014/12/15.<br/>
 * <p/>
 * 解决 org.beetl.core.resource.ClasspathResourceLoader 类中各种蛋疼问题..<br/>
 * 还必须得继承ClasspathResourceLoader...
 */
public class BeetlFixedClassResourceLoader extends ClasspathResourceLoader implements ResourceLoader {
    private String root = null;
    boolean autoCheck = false;
    protected String charset = "UTF-8";
    String functionRoot = "functions";
    String functionSuffix = "html";
    GroupTemplate gt = null;
    ClassLoader classLoader = null;

    public BeetlFixedClassResourceLoader() {
        //保留，用于通过配置构造一个ResouceLoader
        classLoader = this.getClass().getClassLoader();
        this.root = "";

    }

    /**
     * @param root ，前缀，其后的resourceId对应的路径是prefix+"/"+resourceId
     */
    public BeetlFixedClassResourceLoader(String root) {

        this();
        if (root.equals("/")) {
            this.root = "";
        } else {
            this.root = root;
        }

    }

    public BeetlFixedClassResourceLoader(String root, String charset) {

        this(root);
        this.charset = charset;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.beetl.core.ResourceLoader#getResource(java.lang.String)
     */
    @Override
    public Resource getResource(String key) {

        Resource resource = new ClasspathResource(key, root + key, this);
        return resource;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.beetl.core.ResourceLoader#close()
     */
    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isModified(Resource key) {
        if (this.autoCheck) {
            return key.isModified();
        } else {
            return false;
        }
    }

    public boolean isAutoCheck() {
        return autoCheck;
    }

    public void setAutoCheck(boolean autoCheck) {
        this.autoCheck = autoCheck;
    }

    public String getRoot() {
        return root;
    }

    @Override
    public void init(GroupTemplate gt) {
        Map<String, String> resourceMap = gt.getConf().getResourceMap();
        if (resourceMap.get("root") != null) {
            String temp = resourceMap.get("root");
            if (temp.equals("/") || temp.length() == 0) {

            } else {

                if (this.root.endsWith("/")) {
                    this.root = this.root + resourceMap.get("root");
                } else {
                    this.root = this.root + "/" + resourceMap.get("root");
                }

            }

        }
        // fixed 解决配置的默认值不会被覆盖的问题
        if (resourceMap.get("charset") != null) {
            this.charset = resourceMap.get("charset");
        }
        if (resourceMap.get("functionSuffix") != null) {
            this.functionSuffix = resourceMap.get("functionSuffix");
        }
        if (resourceMap.get("functionRoot") != null) {
            this.functionRoot = resourceMap.get("functionRoot");
        }
        if (resourceMap.get("autoCheck") != null) {
            this.autoCheck = Boolean.parseBoolean(resourceMap.get("autoCheck"));
        }

        this.gt = gt;
        //初始化functions
        // fixed 解决在jar模式运行下, 会出现NullPointException的问题
        URL url = BeetlFixedClassResourceLoader.class.getResource(this.functionRoot);
        if (url != null) {
            File root = new File(BeetlFixedClassResourceLoader.class.getResource(this.functionRoot).getPath());
            if (root.exists()) {
                String ns = "";
                String path = "/".concat(this.functionRoot).concat("/");
                readFuntionFile(root, ns, path);
            }
        }

    }


    protected void readFuntionFile(File funtionRoot, String ns, String path) {
        String expected = ".".concat(this.functionSuffix);
        File[] files = funtionRoot.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                readFuntionFile(f, f.getName().concat("."), path.concat(f.getName()).concat("/"));
            } else if (f.getName().endsWith(functionSuffix)) {
                String resourceId = path + f.getName();
                String fileName = f.getName();
                fileName = fileName.substring(0, (fileName.length() - functionSuffix.length() - 1));
                String functionName = ns.concat(fileName);
                FileFunctionWrapper fun = new FileFunctionWrapper(resourceId);
                gt.registerFunction(functionName, fun);
            }
        }
    }

    @Override
    public boolean exist(String key) {
        return this.classLoader.getClass().getResource(root + key) != null;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

}
