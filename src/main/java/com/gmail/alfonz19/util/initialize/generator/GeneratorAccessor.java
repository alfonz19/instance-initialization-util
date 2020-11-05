package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.InitializationConfiguration;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneratorAccessor {

    /**
     * Class cheating java visibility, to allow only 'wanted' method to be visible in builders.
     *
     * Regarding {@link Generator; generators}. Methods to be available in all generators implement interface
     * {@link Generator}. Might be even empty just to have some nice type to pass around. All generators then have to
     * implement {@link Generator}, which forces implementor some required methods to be implemented, like
     * {@link AbstractGenerator#create(InitializationConfiguration, PathNode)}, but these are protected not to be visible in builders syntax. Most
     * typically they will be called just from subclasses and everything is fine. But sometimes we need to call them
     * somehow even if they are not accessible. This is purpose of this class; if developer know he should call them, he
     * can do that via this mediator.
     *
     * @param generator generator to get {@link CalculatedNodeData} from
     * @return CalculatedNodeData for given generator.
     */
    public static CalculatedNodeData getCalculatedNodeData(Generator<?> generator) {
        return castGenerator(generator).getCalculatedNodeData();
    }

    public static <T> T create(Generator<T> generator,
                               InitializationConfiguration initializationConfiguration,
                               PathNode pathNode) {
        return castGenerator(generator).create(initializationConfiguration, pathNode);
    }

    public static <T> List<T> create(Generator<T> generator,
                                     int number,
                                     InitializationConfiguration initializationConfiguration,
                                     PathNode pathNode) {
        return castGenerator(generator).create(number, initializationConfiguration, pathNode);
    }

    public static <T> AbstractGenerator<T> castGenerator(Generator<T> generator) {
        //noinspection unchecked
        return (AbstractGenerator<T>) generator;
    }
}
