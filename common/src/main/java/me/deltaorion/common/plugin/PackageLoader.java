package me.deltaorion.common.plugin;

import me.deltaorion.common.plugin.depend.MissingDependencyException;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PackageLoader {

    private final String packageName;
    private final String folderName;
    private final ClassLoader jar;

    public PackageLoader(String packageName, ClassLoader jar) {
        this.packageName = packageName;
        this.folderName = toFolder(packageName);
        this.jar = jar;
    }

    public Collection<Class<?>> load() throws MissingDependencyException {
        CodeSource src = PackageLoader.class.getProtectionDomain().getCodeSource();
        List<Class<?>> classes = new ArrayList<>();
        try {
            if (src != null) {
                URL jar = src.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null)
                        break;
                    String name = e.getName();
                    Class<?> clazz = handleEntry(name);
                    if(clazz!=null)
                        classes.add(clazz);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }
        return classes;
    }

    @Nullable
    private Class<?> handleEntry(String name) throws ClassNotFoundException {
        if(!name.startsWith(folderName))
            return null;

        if(!name.endsWith(".class"))
            return null;

        System.out.println("Loading - " + name);
        return jar.loadClass(fileToPackage(name));
    }

    private String fileToPackage(String folderName) {
        String pack = toPackage(folderName);
        System.out.println("Folder Name: " + folderName);
        System.out.println("Package Name: " + pack);
        return pack.substring(0,pack.length()-6);
    }

    private String toFolder(String packageName) {
        return packageName.replace('.','/');
    }

    private String toPackage(String folderName) {
        return folderName.replace('/','.');
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFolderName() {
        return folderName;
    }
}
