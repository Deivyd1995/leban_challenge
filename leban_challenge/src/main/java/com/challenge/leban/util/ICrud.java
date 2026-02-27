package com.challenge.leban.util;

import java.util.List;

public interface ICrud<T> {

    T add(T t);

    List<T> getAll();

}
