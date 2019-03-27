package com.gb.apm.asm.apiadapter;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

/**
 * @author jaehong.kim
 */
public class ASMFieldNodeAdapter {
    private FieldNode fieldNode;

    public ASMFieldNodeAdapter(final FieldNode fieldNode) {
        this.fieldNode = fieldNode;
    }

    public String getName() {
        return this.fieldNode.name;
    }

    public String getClassName() {
        Type type = Type.getType(this.fieldNode.desc);
        return type.getClassName();
    }

    public String getDesc() {
        return this.fieldNode.desc;
    }

    public boolean isStatic() {
        return (this.fieldNode.access & Opcodes.ACC_STATIC) != 0;
    }

    public boolean isFinal() {
        return (this.fieldNode.access & Opcodes.ACC_FINAL) != 0;
    }

    public void setAccess(final int access) {
        this.fieldNode.access = access;
    }

    public int getAccess() {
        return this.fieldNode.access;
    }
}
