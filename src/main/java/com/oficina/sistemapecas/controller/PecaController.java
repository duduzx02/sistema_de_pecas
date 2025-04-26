package com.oficina.sistemapecas.controller;

import com.oficina.sistemapecas.model.Peca;
import com.oficina.sistemapecas.service.PecaService;
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
