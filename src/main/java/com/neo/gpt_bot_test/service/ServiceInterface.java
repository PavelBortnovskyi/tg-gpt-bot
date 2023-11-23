package com.neo.gpt_bot_test.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ServiceInterface<E> {
  E save(E entity);

  void delete(E entity);

  void deleteById(Long id);

  List<E> findAll();

  Page<E> findAllPageable(Pageable pageable);

  Optional<E> findById(Long id);

  List<E> findAllById(Iterable<Long> listOfIds);
}