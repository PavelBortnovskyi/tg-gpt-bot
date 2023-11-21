package com.neo.gpt_bot_test.containers.db.service;


import com.neo.gpt_bot_test.model.BaseEntity;
import com.neo.gpt_bot_test.repository.RepositoryInterface;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Log4j2
@NoArgsConstructor
@AllArgsConstructor
public abstract class GeneralService<E extends BaseEntity> implements ServiceInterface<E> {

    @Autowired
    private RepositoryInterface<E> repository;

    @Override
    public E save(E entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(E entity) {
        repository.delete(entity);
    }

    @Override
    public List<E> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.findById(id).ifPresentOrElse(this::delete, () ->
                log.info(String.format("Entity with id %d was not found.", id))
        );
    }

    @Override
    public Optional<E> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<E> findAllById(Iterable<Long> listOfIds) {
        return repository.findAllById(listOfIds);
    }
}
