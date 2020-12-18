package ru.ndg.shop.config.mapper;

import java.util.List;

public interface MapperFacade {

    <S, D> D map(S sourceObject, Class<D> destinationClass);

    <S, D> void map(S sourceObject, D destinationObject);

    <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass);

    <S, D> void mapObject(S sourceObject, D destinationObject);
}
