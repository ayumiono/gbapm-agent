package com.gb.apm.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class BootstrapJarFile {

    private final List<JarFile> jarFileEntry = new ArrayList<JarFile>();

    public BootstrapJarFile() {
    }

    public void append(JarFile jarFile) {
        if (jarFile == null) {
            throw new NullPointerException("jarFile must not be null");
        }

        this.jarFileEntry.add(jarFile);
    }

    public List<JarFile> getJarFileList() {
        return jarFileEntry;
    }

    public List<String> getJarNameList() {
        List<String> bootStrapJarLIst = new ArrayList<String>(jarFileEntry.size());
        for (JarFile jarFile : jarFileEntry) {
            bootStrapJarLIst.add(jarFile.getName());
        }
        return bootStrapJarLIst;
    }

}
