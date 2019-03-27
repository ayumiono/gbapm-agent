package com.gb.apm.common.trace;

/**
 * @author emeroad
 */
public class HistogramSlot {

    private final short slotTime;
    private final SlotType slotType;
    private final String slotName;

    public HistogramSlot(short slotTime, SlotType slotType, String slotName) {
        if (slotType == null) {
            throw new NullPointerException("slotType must not be null");
        }
        if (slotName == null) {
            throw new NullPointerException("slotName must not be null");
        }
        this.slotTime = slotTime;
        this.slotType = slotType;
        this.slotName = slotName;
    }

    public short getSlotTime() {
        return slotTime;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public String getSlotName() {
        return slotName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HistogramSlot{");
        sb.append("slotTime=").append(slotTime);
        sb.append(", slotType=").append(slotType);
        sb.append(", slotName='").append(slotName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
