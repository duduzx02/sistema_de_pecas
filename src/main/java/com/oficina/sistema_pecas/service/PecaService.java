package com.oficina.sistema_pecas.service;

import com.oficina.sistema_pecas.model.Peca;
import com.oficina.sistema_pecas.repository.PecaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PecaService {

    private final PecaRepository pecaRepository;


    public PecaService(PecaRepository pecaRepository) {
        this.pecaRepository = pecaRepository;
    }

    public List<Peca> listarTodas(){
        return pecaRepository.findAll();
    }

    public Peca salvar(Peca peca){
        return pecaRepository.save(peca);
    }

    public void excluir(Long id){
        pecaRepository.deleteById(id);
    }
}
