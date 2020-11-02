package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.util.ClassDataCache;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;
import com.gmail.alfonz19.util.initialize.util.TypeVariableAssignments;

import java.util.function.Supplier;

public class NewInstanceGenerator extends AbstractGenerator<Object> {
    private Supplier<?> supplier;

    public NewInstanceGenerator() {
    }

    //most probably pointless
    public NewInstanceGenerator(Class<?> classType) {
        this.supplier = createNewInstanceCreatingSupplier(classType);
    }

    @Override
    protected Object create(PathNode pathNode) {
        CalculatedNodeData calculatedNodeData = pathNode.getCalculatedNodeData();
        if (supplier == null) {
            Class<?> classType = calculatedNodeData.getClassType();
            supplier = createNewInstanceCreatingSupplier(classType);
        }

        TypeVariableAssignments typeVariableAssignment =
                ReflectUtil.typeVariableAssignment(calculatedNodeData.getClassType(),
                        calculatedNodeData.getGenericClassType());

        InstanceGenerator instanceGenerator = new InstanceGenerator(calculatedNodeData.getClassType(),
                calculatedNodeData.getGenericClassType(),
                supplier,
                typeVariableAssignment);

        return instanceGenerator.create(pathNode);
    }

    public Supplier<?> createNewInstanceCreatingSupplier(Class<?> classType) {
        return () -> ReflectUtil.callNewInstance(ClassDataCache.getNoArgConstructor(classType));
    }
}
