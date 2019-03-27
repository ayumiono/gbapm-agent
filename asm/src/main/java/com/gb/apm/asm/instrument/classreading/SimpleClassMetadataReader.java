package com.gb.apm.asm.instrument.classreading;

import org.objectweb.asm.ClassReader;

/**
 * @author Woonduk Kang(emeroad)
 */
public class SimpleClassMetadataReader {

    private final SimpleClassMetadata simpleClassMetadata;

    public static SimpleClassMetadata readSimpleClassMetadata(byte[] classBinary) {
        SimpleClassMetadataReader simpleClassMetadataReader = new SimpleClassMetadataReader(classBinary);
        return simpleClassMetadataReader.getSimpleClassMetadata();
    }

    SimpleClassMetadataReader(byte[] classBinary) {
        if (classBinary == null) {
            throw new NullPointerException("classBinary must not be null");
        }

        final ClassReader classReader = new ClassReader(classBinary);

        int accessFlag = classReader.getAccess();
        String className = classReader.getClassName();
        String superClassName = classReader.getSuperName();
        String[] interfaceNameList = classReader.getInterfaces();

//        int offset = 0;
//        int version = classReader.readShort(offset + 6);
//         offset is zero
        int version = classReader.readShort(6);

        this.simpleClassMetadata = new DefaultSimpleClassMetadata(version, accessFlag, className, superClassName, interfaceNameList, classBinary);
    }

    public SimpleClassMetadata getSimpleClassMetadata() {
        return simpleClassMetadata;
    }


}
