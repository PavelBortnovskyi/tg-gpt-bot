package com.neo.gpt_bot_test.containers.db.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ServiceInterface<E> {
  E save(E entity);

  void delete(E entity);

  void deleteById(Long id);

  List<E> findAll();

  Optional<E> findById(Long id);

  List<E> findAllById(Iterable<Long> listOfIds);
}
