package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;
import lombok.Getter;

import static com.gmail.alfonz19.util.initialize.util.GenericUtil.nvl;

@Getter
public class SizeSpecification {
    private final int minSize;
    private final int maxSize;
    private final int sizeToUseWhenSpecificationIsNotConfigured;
    private Integer requestedMinSize;
    private Integer requestedMaxSize;
    private Integer requestedSize;

    public SizeSpecification(int minSize, int maxSize, int sizeToUseWhenSpecificationIsNotConfigured) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.sizeToUseWhenSpecificationIsNotConfigured = sizeToUseWhenSpecificationIsNotConfigured;
    }

    public void setRequestedMinSize(Integer requestedMinSize) {
        if (requestedMinSize < minSize) {
            throw new InitializeException("Min size cannot be smaller than " + minSize);
        }

        this.requestedMinSize = requestedMinSize;
        validateSizeSpecification();
    }

    public void setRequestedMaxSize(Integer requestedMaxSize) {
        if (requestedMaxSize > maxSize) {
            throw new InitializeException("Max size cannot be bigger than " + maxSize);
        }

        this.requestedMaxSize = requestedMaxSize;
        validateSizeSpecification();
    }

    public void setRequestedSize(Integer requestedSize) {
        if (requestedSize < minSize || requestedSize > maxSize) {
            throw new InitializeException(String.format("Size has to be in range <%d, %d>", minSize, maxSize));
        }

        this.requestedSize = requestedSize;
        validateSizeSpecification();
    }

    private void validateSizeSpecification() {
        if (requestedSize != null && (requestedMinSize != null || requestedMaxSize != null)) {
            //TODO MMUCHA: better message using path.
            throw new InitializeException("Specified both min-max size bounds and exact size size.");
        }
    }

    public int getRandomSizeAccordingToSpecification() {
        if (this.requestedSize != null) {
            return this.requestedSize;
        }

        if (requestedMinSize == null && requestedMaxSize == null) {
            return sizeToUseWhenSpecificationIsNotConfigured;
        }

        return RandomUtil.INSTANCE.intFromClosedRange(nvl(requestedMinSize, minSize),
                nvl(requestedMaxSize, maxSize));
    }

}
