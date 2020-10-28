package com.gmail.alfonz19.util.initialize.context;

import com.gmail.alfonz19.util.initialize.exception.IncorrectContextPathException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Object representation of {@link Path}.
 */
public class PathComponents {
    @Getter
    private final List<PathComponent> components;

    /**
     * Parses string representation of {@link Path} and creates {@link PathComponents}.
     * @param path path to parse.
     */
    public PathComponents(String path) {
        components = Collections.unmodifiableList(createPathComponents(path));
    }

    /**
     * creates 'deeper' {@link PathComponents} by adding one more component at the end of given one.
     * @param existingPathComponents existing path components
     * @param componentToAdd component to add to the end to form new {@link PathComponents}
     */
    public PathComponents(PathComponents existingPathComponents, PathComponent componentToAdd) {
        int size = existingPathComponents.components.size();
        this.components = new ArrayList<>(size+1);
        components.addAll(existingPathComponents.components);
        components.add(componentToAdd);
    }

    private List<AbstractPathComponent> createPathComponents(String path) {
        if (path == null) {
            return Collections.emptyList();
        }

        int pathLength = path.length();
        if (pathLength == 0 || path.equals(Path.PATH_SEPARATOR_AS_STRING)) {
            return Collections.emptyList();
        }

        List<AbstractPathComponent> result = new LinkedList<>();

        if (path.charAt(0) != Path.PATH_SEPARATOR) {
            throw new IncorrectContextPathException();
        }

        int lastIndex = 0;
        for (int i = 1; i < pathLength; i++) {
            char charAtI = path.charAt(i);
            //first `.` does not end word
            boolean wordEnd = charAtI == Path.PATH_SEPARATOR || (charAtI == '[' && i > 1);
            if (wordEnd) {
                String pathPart = path.substring(lastIndex, i);

                result.add(processPathPart(pathPart, lastIndex == 0));
                lastIndex = i;
            }
        }

        String pathPart = path.substring(lastIndex, pathLength);
        result.add(processPathPart(pathPart, lastIndex == 0));

        return result;
    }

    private AbstractPathComponent processPathPart(String pathPart, boolean firstWord) {
        if (pathPart.length() == 1) {
            throw new IncorrectContextPathException();
        }
        char pathPartStartChar = pathPart.charAt(0);
            if (pathPartStartChar == Path.PATH_SEPARATOR) {
                if (firstWord && pathPart.charAt(1) == '[') {
                    return processArrayPart(pathPart.substring(1));
                } else {
                    String pathPartWithoutLeadingDot = pathPart.substring(1);
                    if (pathPartWithoutLeadingDot.contains(Path.PATH_SEPARATOR_AS_STRING) || pathPartWithoutLeadingDot.contains("]")) {
                        throw new IncorrectContextPathException();
                    }
                    return new PropertyPathComponent(pathPartWithoutLeadingDot);
                }


            } else {
                if (pathPartStartChar == '[') {
                    return processArrayPart(pathPart);
                } else {
                    throw new IncorrectContextPathException();
                }
            }
    }

    private AbstractPathComponent processArrayPart(String pathPart) {
        if (pathPart.length() <= 2) {
            throw new IncorrectContextPathException();
        }
        int pathPartLastCharIndex = pathPart.length() - 1;
        if(pathPart.charAt(pathPartLastCharIndex) != ']') {
            throw new IncorrectContextPathException();
        } else {
            String pathPartWithoutBrackets = pathPart.substring(1, pathPartLastCharIndex);
            try {
                int index = Integer.parseInt(pathPartWithoutBrackets);
                return new ArrayIndexPathComponent(index);
            } catch (NumberFormatException e) {
                return new AssociativeArrayPathComponent(pathPartWithoutBrackets);
            }
        }
    }

    public PathComponent getLastPathComponent() {
        int size = components.size();
        if (size == 0) {
            return null;
        }
        return components.get(size - 1);
    }

    public int getNumberOfComponents() {
        return this.components.size();
    }

    public PathComponents append(PathComponent pathComponent) {
        return new PathComponents(this, pathComponent);
    }

    public PathComponents appendPropertyPathComponent(String propertyName) {
        return append(new PropertyPathComponent(propertyName));
    }

    public PathComponents appendArrayIndexPathComponent(int index) {
        return append(new ArrayIndexPathComponent(index));
    }

    public PathComponents appendAssociativeArrayPathComponent(String key) {
        return append(new AssociativeArrayPathComponent(key));
    }
}
