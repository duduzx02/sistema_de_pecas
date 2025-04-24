package com.oficina.sistema_pecas.controller;

import com.oficina.sistema_pecas.model.Peca;
import com.oficina.sistema_pecas.service.PecaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pecas")
public class PecaController {

    private final PecaService pecaService;

    public PecaController(PecaService pecaService) {
        this.pecaService = pecaService;
    }

    @GetMapping
    public List<Peca> listarTodas(){
        return pecaService.listarTodas();
    }

    @PostMapping
    public Peca cadastrar(@RequestBody Peca peca){
        return pecaService.salvar(peca);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        pecaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
