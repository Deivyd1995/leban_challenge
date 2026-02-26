package com.challenge.leban.service;

import java.util.List;

import com.challenge.leban.dto.DepartamentoDto;
import com.challenge.leban.util.ICrud;

public interface IDepartamentoService extends ICrud<DepartamentoDto> {

    List<DepartamentoDto> filterDepartamentos(String disponible, String precioMin, String precioMax);

}
