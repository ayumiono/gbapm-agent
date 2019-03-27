package com.gb.apm.profiler.util;

/**
 * @author Woonduk Kang(emeroad)
 */
public class FileBinary {
    private final String className;

    private final byte[] fileBinary;

    FileBinary(String fileName, byte[] fileBinary) {
        if (fileName == null) {
            throw new NullPointerException("fileName must not be null");
        }
        if (fileBinary == null) {
            throw new NullPointerException("fileBinary must not be null");
        }
        this.className = fileName;
        this.fileBinary = fileBinary;
    }

    public String getFileName() {
        return className;
    }

    public byte[] getFileBinary() {
        return fileBinary;
    }

    @Override
    public String toString() {
        return "FileBinary{" +
                "className='" + className + '\'' +
                ", fileBinarySize=" + fileBinary.length +
                '}';
    }
}
