package com.tun.casestudy1.service;

import java.util.List;

public interface IService<T> {
    List<T> findAll();

    T find(int id);

    void delete(int id);

    void save(T t);

}
