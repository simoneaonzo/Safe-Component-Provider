package com.uni.ailab.scp.secureManifest;

import com.uni.ailab.scp.policy.Policy;
import com.uni.ailab.scp.receiver.SQLiteHelper;

import java.util.Collection;

public class Component {
    private final String fqdnComponentName;
    private final ComponentType componentType;
    private final Collection<String> permissions;
    private final Collection<Policy> policies;

    public Component(String fqdnComponentName,
                     ComponentType componentType,
                     Collection<String> permissions,
                     Collection<Policy> policies) {
        this.fqdnComponentName = fqdnComponentName;
        this.componentType = componentType;
        this.permissions = permissions;
        this.policies = policies;
    }

    public void storeIntoDB(SQLiteHelper sqlh) {
        long res = sqlh.insertComponent(fqdnComponentName, componentType);

        for (String permission : permissions) {
            res = sqlh.insertPermission(permission);
            sqlh.assignComponentPermission(fqdnComponentName, permission);
        }

        for (Policy policy : policies) {
            res = sqlh.insertPolicy(policy.getScope().toString(), policy.isSticky(), policy.getFormula().toString());
            sqlh.assignComponentPolicy(fqdnComponentName, res);
        }
    }
}
