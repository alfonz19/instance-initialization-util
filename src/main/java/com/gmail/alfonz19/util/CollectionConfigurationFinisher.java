package com.gmail.alfonz19.util;

@SuppressWarnings("squid:S119")//type variables
public interface CollectionConfigurationFinisher<RETURN_TYPE> {
    RETURN_TYPE toSize(int i);

    RETURN_TYPE toSize(int min, int max);

    RETURN_TYPE asEmpty(int min, int max);

    RETURN_TYPE asNull(int min, int max);
}
