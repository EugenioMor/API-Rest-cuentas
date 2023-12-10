package com.api.hateoas.apirestcuentas.service;

import com.api.hateoas.apirestcuentas.exception.CuentaNotFoundException;
import com.api.hateoas.apirestcuentas.model.Cuenta;
import com.api.hateoas.apirestcuentas.repository.CuentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CuentaService implements ICuentaService{

    @Autowired
    private CuentaRepository cuentaRepository;
    @Override
    public List<Cuenta> listAll() {
        return cuentaRepository.findAll();
    }

    @Override
    public Cuenta getCuenta(Integer id) {
        Cuenta cuenta = cuentaRepository.findById(id).orElse(null);
        return cuenta;
    }

    @Override
    public Cuenta saveCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    @Override
    public void deleteCuenta(Integer id) throws CuentaNotFoundException {
        if (!cuentaRepository.existsById(id)){
            throw new CuentaNotFoundException("Cuenta no encontrada con el ID : " + id);
        }
        cuentaRepository.deleteById(id);
    }

    @Override
    public Cuenta depositar(float monto, Integer id) {
        cuentaRepository.actualizarMonto(monto, id);
        return cuentaRepository.findById(id).get();
    }

    @Override
    public Cuenta retirar(float monto, Integer id) {
        cuentaRepository.actualizarMonto(-monto, id);
        return cuentaRepository.findById(id).get();
    }
}
