package com.api.hateoas.apirestcuentas.service;

import com.api.hateoas.apirestcuentas.exception.CuentaNotFoundException;
import com.api.hateoas.apirestcuentas.model.Cuenta;

import java.util.List;

public interface ICuentaService {

    public List<Cuenta> listAll();
    public Cuenta getCuenta(Integer id);
    public Cuenta saveCuenta(Cuenta cuenta);
    public void deleteCuenta(Integer id) throws CuentaNotFoundException;
    public Cuenta depositar(float monto, Integer id);
    public Cuenta retirar(float monto, Integer id);
}
