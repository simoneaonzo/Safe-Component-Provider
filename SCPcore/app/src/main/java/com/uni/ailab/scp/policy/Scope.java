package com.uni.ailab.scp.policy;


public enum Scope {

    DIRECT, LOCAL, GLOBAL;

    @Override
    public String toString() {
        switch (this) {
            case DIRECT:
            case LOCAL:
            case GLOBAL:
                return this.name();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Scope getEnum(String str) {
        str = str.toUpperCase();
        for(Scope ct : values())
            if(ct.name().equals(str))
                return ct;
        throw new IllegalArgumentException();
    }

    public static boolean contains(String str) {
        str = str.toUpperCase();
        for (Scope scope : Scope.values()) {
            if (scope.name().equals(scope)) {
                return true;
            }
        }
        return false;
    }
}
