package org.rij.minecraft.PineTreePlugin.PineTree;

import org.rij.minecraft.PineTree.Annotations.GetMapping;
import org.rij.minecraft.PineTree.Annotations.PostMapping;
import org.rij.minecraft.PineTree.Annotations.RestController;
import org.rij.minecraft.PineTree.REST.Spark;
import org.rij.minecraft.PineTreePlugin.PineTree.Annotations.PTPaper;
import org.rij.minecraft.PineTreePlugin.PineTree.Annotations.PTVelocity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Registrator {

    public List<String> pluginNames = new ArrayList<>();
    private PTScanType type = PTScanType.VELOCITY;

    public void setType(PTScanType type) {
        this.type = type;
    }

    public void register(Class<?> clazz) throws IOException, ClassNotFoundException, URISyntaxException {
        List<Class> m = getClasses(clazz.getPackageName());

        m.forEach(aClass -> {
            if(!aClass.isAnnotationPresent(RestController.class))
                return;

            pluginNames.add(clazz.getAnnotation(RestController.class).name());

            for (Method method : aClass.getMethods())
                if (method.isAnnotationPresent(GetMapping.class))
                    Spark.addGetEndpoint(method);
                else if(method.isAnnotationPresent(PostMapping.class))
                    Spark.addPostEndpoint(method);
        });
    }

    public void register(Object obj){
        Class<?> clazz = obj.getClass();

        if(!clazz.isAnnotationPresent(RestController.class))
            return;

        pluginNames.add(clazz.getAnnotation(RestController.class).name());

        for (Method method : clazz.getMethods())
            if (method.isAnnotationPresent(GetMapping.class))
                Spark.addGetEndpoint(obj, method);
            else if(method.isAnnotationPresent(PostMapping.class))
                Spark.addPostEndpoint(obj, method);



        if(!clazz.isAnnotationPresent(PTVelocity.class) && !clazz.isAnnotationPresent(PTPaper.class))
            return;

        if(clazz.isAnnotationPresent(PTVelocity.class) && type.equals(PTScanType.VELOCITY)) {
            for (Method method : clazz.getMethods())
                if (method.isAnnotationPresent(GetMapping.class))
                    Spark.addGetEndpoint(obj, method);
                else if (method.isAnnotationPresent(PostMapping.class))
                    Spark.addPostEndpoint(obj, method);
        }
        else if(clazz.isAnnotationPresent(PTPaper.class) && type.equals(PTScanType.PAPER)){
            for (Method method : clazz.getMethods())
                if (method.isAnnotationPresent(GetMapping.class))
                    Spark.addGetEndpoint(obj, method);
                else if (method.isAnnotationPresent(PostMapping.class))
                    Spark.addPostEndpoint(obj, method);
        }


    }


    /**
     * Scans all classes accessible from the context class loader which belong
     * to the given package and subpackages.
     *
     * @param packageName
     *            The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements())
        {
            URL resource = resources.nextElement();
            URI uri = new URI(resource.toString());
            dirs.add(new File(uri.getPath()));
        }
        List<Class> classes = new ArrayList<Class>();
        for (File directory : dirs)
        {
            classes.addAll(findClasses(directory, packageName));
        }

        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and
     * subdirs.
     *
     * @param directory
     *            The base directory
     * @param packageName
     *            The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException
    {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists())
        {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            }
            else if (file.getName().endsWith(".class"))
            {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }




}
