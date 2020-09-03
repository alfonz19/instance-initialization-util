package com.gmail.alfonz19.util.initialize.builder;

import com.gmail.alfonz19.util.initialize.exception.IncorrectContextPathException;
import lombok.Getter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PathComponents {
    @Getter
    private final List<AbstractPathComponent<? extends Comparable<?>>> components;

    public PathComponents(String path) {
        components = Collections.unmodifiableList(createPathComponents(path));
    }

    private List<AbstractPathComponent<? extends Comparable<?>>> createPathComponents(String path) {
        if (path == null) {
            return Collections.emptyList();
        }

        int pathLength = path.length();
        if (pathLength == 0 || path.equals(".")) {
            return Collections.emptyList();
        }

        List<AbstractPathComponent<? extends Comparable<?>>> result = new LinkedList<>();
        int lastIndex = 0;
        for (int i = 1; i < pathLength; i++) {
            char charAtI = path.charAt(i);
            boolean wordEnd = charAtI == '.' || charAtI == '[';
            if (wordEnd && i > 1) {
                String pathPart = path.substring(lastIndex, i);

                lastIndex = i;
                result.add(processPathPart(pathPart));
            }
        }

        if (lastIndex < pathLength) {
            String pathPart = path.substring(lastIndex, pathLength);
            result.add(processPathPart(pathPart));
        }

        return result;
    }

    private AbstractPathComponent<?> processPathPart(String pathPart) {
        char pathPartStartChar = pathPart.charAt(0);
        if (pathPartStartChar == '.') {
            String pathPartWithoutLeadingDot = pathPart.substring(1);
            if (pathPartWithoutLeadingDot.isEmpty()
                    || pathPartWithoutLeadingDot.contains(".")
                    || pathPartWithoutLeadingDot.contains("[")
                    || pathPartWithoutLeadingDot.contains("]")) {
                throw new IncorrectContextPathException();
            }
            return new PropertyPathComponent(pathPartWithoutLeadingDot);
        } else if (pathPartStartChar == '[') {
            int pathPartLastCharIndex = pathPart.length() - 1;
            if(pathPart.charAt(pathPartLastCharIndex) != ']') {
                throw new IncorrectContextPathException();
            } else {
                String pathPartWithoutBrackets = pathPart.substring(1, pathPartLastCharIndex);
                try {
                    int index = Integer.parseInt(pathPartWithoutBrackets);
                    return new ArryIndexPathComponent(index);
                } catch (NumberFormatException e) {
                    return new MapKeyPathComponent(pathPartWithoutBrackets);
                }
            }
        } else {
            throw new IncorrectContextPathException();
        }
    }

    public PathComponent getLastPathComponent() {
        int size = components.size();
        if (size == 0) {
            return null;
        }
        return components.get(size - 1);
    }
}
