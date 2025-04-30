package com.oficina.sistemapecas.service;

import com.oficina.sistemapecas.model.Peca;
import com.oficina.sistemapecas.repository.PecaRepository;
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

    public Peca buscarPorId(Long id){
       return pecaRepository.findById(id).orElse(null);
    }

    public List<Peca> listarPorUsuario(Long usuarioId){
        return pecaRepository.findByUsuarioId(usuarioId);
    }
}
