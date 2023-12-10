package com.api.hateoas.apirestcuentas.controller;

import com.api.hateoas.apirestcuentas.model.Cuenta;
import com.api.hateoas.apirestcuentas.model.Monto;
import com.api.hateoas.apirestcuentas.service.ICuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private ICuentaService cuentaService;

    @GetMapping("/traer")
    public ResponseEntity<List<Cuenta>> listarCuentas(){
        List<Cuenta> cuentas = cuentaService.listAll();

        if (cuentas.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        for (Cuenta cuenta:cuentas){
            cuenta.add(linkTo(methodOn(CuentaController.class).traerCuenta(cuenta.getId())).withSelfRel());
            cuenta.add(linkTo(methodOn(CuentaController.class).depositar(cuenta.getId(), null)).withRel("depósitos"));
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withSelfRel());
        }

        CollectionModel<Cuenta> modelo =  CollectionModel.of(cuentas);
        modelo.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withSelfRel());

        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @GetMapping("/traer/{id}")
    public ResponseEntity<Cuenta> traerCuenta(@PathVariable Integer id){

        try {
            Cuenta cuenta = cuentaService.getCuenta(id);
            cuenta.add(linkTo(methodOn(CuentaController.class).traerCuenta(cuenta.getId())).withSelfRel());
            cuenta.add(linkTo(methodOn(CuentaController.class).depositar(cuenta.getId(), null)).withRel("depósitos"));
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));

            return new ResponseEntity<>(cuenta, HttpStatus.OK);
        }

        catch (Exception exception){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<Cuenta> guardarCuenta(@RequestBody Cuenta cuenta){
        Cuenta cuentaBD = cuentaService.saveCuenta(cuenta);

        cuentaBD.add(linkTo(methodOn(CuentaController.class).traerCuenta(cuentaBD.getId())).withSelfRel());
        cuentaBD.add(linkTo(methodOn(CuentaController.class).depositar(cuentaBD.getId(), null)).withRel("depósitos"));
        cuentaBD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));

        return ResponseEntity.created(linkTo(methodOn(CuentaController.class).traerCuenta(cuentaBD.getId())).toUri()).body(cuentaBD);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Cuenta> editarCuenta(@RequestBody Cuenta cuenta){
        Cuenta cuentaBD = cuentaService.saveCuenta(cuenta);

        cuentaBD.add(linkTo(methodOn(CuentaController.class).traerCuenta(cuentaBD.getId())).withSelfRel());
        cuentaBD.add(linkTo(methodOn(CuentaController.class).depositar(cuentaBD.getId(), null)).withRel("depósitos"));
        cuentaBD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));

        return new ResponseEntity<>(cuentaBD, HttpStatus.CREATED);
    }

    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable Integer id){
        try {
            cuentaService.deleteCuenta(id);
            return ResponseEntity.noContent().build();
        }

        catch (Exception exception){
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/deposito")
    public ResponseEntity<Cuenta> depositar(@PathVariable Integer id, @RequestBody Monto monto){
        Cuenta cuentaBD = cuentaService.depositar(monto.getMonto(), id);

        cuentaBD.add(linkTo(methodOn(CuentaController.class).traerCuenta(cuentaBD.getId())).withSelfRel());
        cuentaBD.add(linkTo(methodOn(CuentaController.class).depositar(cuentaBD.getId(), null)).withRel("depósitos"));
        cuentaBD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));

        return new ResponseEntity<>(cuentaBD, HttpStatus.OK);
    }

    @PatchMapping("/{id}/retiro")
    public ResponseEntity<Cuenta> retirar(@PathVariable Integer id, @RequestBody Monto monto){
        Cuenta cuentaBD = cuentaService.retirar(monto.getMonto(), id);

        cuentaBD.add(linkTo(methodOn(CuentaController.class).traerCuenta(cuentaBD.getId())).withSelfRel());
        cuentaBD.add(linkTo(methodOn(CuentaController.class).depositar(cuentaBD.getId(), null)).withRel("depósitos"));
        cuentaBD.add(linkTo(methodOn(CuentaController.class).retirar(cuentaBD.getId(), null)).withRel("retiros"));
        cuentaBD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));

        return new ResponseEntity<>(cuentaBD, HttpStatus.OK);
    }
}
