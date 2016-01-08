package com.uni.ailab.scp.secureManifest;

public enum ComponentType {
    ACTIVITY, SERVICE, RECEIVER, PROVIDER;

    public static ComponentType getEnum(String str) {
        str = str.toUpperCase();
        for(ComponentType ct : values())
            if(ct.name().equals(str))
                return ct;
        throw new IllegalArgumentException();
    }

    public static boolean contains(String str) {
        str = str.toUpperCase();
        for (ComponentType ct : values()) {
            if (ct.name().equals(str)) {
                return true;
            }
        }
        return false;
    }
}
