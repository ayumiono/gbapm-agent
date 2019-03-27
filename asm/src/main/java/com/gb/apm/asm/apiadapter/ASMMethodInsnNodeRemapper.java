package com.gb.apm.asm.apiadapter;

import org.objectweb.asm.tree.MethodInsnNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jaehong.kim
 */
public class ASMMethodInsnNodeRemapper {
    private List<Filter> filters = new ArrayList<Filter>();
    private String owner;
    private String name;
    private String desc;

    public void addFilter(final String ownerClassInternalName, final String name, final String desc) {
        this.filters.add(new Filter(ownerClassInternalName, name, desc));
    }

    public void setOwner(final String ownerClassInternalName) {
        this.owner = ownerClassInternalName;
    }

    private String mapOwner(final String ownerClassInternalName) {
        return this.owner != null ? this.owner : ownerClassInternalName;
    }

    public void setName(final String name) {
        this.name = name;
    }

    private String mapName(final String name) {
        return this.name != null ? this.name : name;
    }

    public void setDesc(final String desc) {
        this.desc = desc;
    }

    private String mapDesc(final String desc) {
        return this.desc != null ? this.desc : desc;
    }

    public void mapping(final MethodInsnNode methodInsnNode) {
        for (Filter filter : this.filters) {
            if (filter.accept(methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc)) {
                methodInsnNode.owner = mapOwner(methodInsnNode.owner);
                methodInsnNode.name = mapName(methodInsnNode.name);
                methodInsnNode.desc = mapDesc(methodInsnNode.desc);
            }
        }
    }

    private static class Filter {
        private final String owner;
        private final String name;
        private final String desc;

        public Filter(final String owner, final String name, final String desc) {
            this.owner = owner;
            this.name = name;
            this.desc = desc;
        }

        public boolean accept(final String owner, final String name, final String desc) {
            if (this.owner != null && !this.owner.equals(owner)) {
                return false;
            }

            if (this.name != null && !this.name.equals(name)) {
                return false;
            }

            if (this.desc != null && !this.desc.equals(desc)) {
                return false;
            }

            return true;
        }
    }
}