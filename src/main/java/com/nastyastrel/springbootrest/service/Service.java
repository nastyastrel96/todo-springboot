package com.nastyastrel.springbootrest.service;


import java.util.List;

public interface Service<T> {
    List<T> findAll();

    void save(T item);

    List<T> findSpecificItem(String wordToBeFound);

    void changeStateToDone(int serialNumber);

    void deleteItem(int serialNumber);
}
