package com.neo.gpt_bot_test.facade;

import com.neo.gpt_bot_test.service.ServiceInterface;
import com.neo.gpt_bot_test.model.BaseEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Component
@RequiredArgsConstructor
public abstract class GeneralFacade<E extends BaseEntity, I, O> {

  private final ModelMapper mm;

  private final ServiceInterface<E> service;

  private Class<E> getClassE() {
    return (Class<E>) ((ParameterizedType) getClass()
      .getGenericSuperclass()).getActualTypeArguments()[0];
  }

  private Class<O> getClassO() {
    return (Class<O>) ((ParameterizedType) getClass()
      .getGenericSuperclass()).getActualTypeArguments()[2];
  }

  public O convertToDto(E entity) {
    return mm.map(entity, getClassO());
  }

  public E convertToEntity(I rqDto) {
    return mm.map(rqDto, getClassE());
  }

  public E mapToEntity(I entityDTO, E entity) {
    mm.map(entityDTO, entity);
    return entity;
  }

  public O save(E entity) {
    return convertToDto(service.save(entity));
  }

  public void delete(E entity) {
    service.delete(entity);
  }

  public List<O> findAll() {
    List<E> entities = service.findAll();
    List<O> entitiesRs = entities.stream()
      .map(this::convertToDto)
      .collect(Collectors.toList());
    return entitiesRs;
  }

  public void deleteById(Long id) {
    service.deleteById(id);
  }
}
