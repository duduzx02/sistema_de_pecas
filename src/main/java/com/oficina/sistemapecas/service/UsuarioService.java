package com.oficina.sistemapecas.service;

import com.oficina.sistemapecas.model.Usuario;
import com.oficina.sistemapecas.repository.PecaRepository;
import com.oficina.sistemapecas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PecaRepository pecaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PecaRepository pecaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pecaRepository = pecaRepository;
    }

    public Usuario salvar(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void excluir(Long id){
        long totalPecas = pecaRepository.countByUsuarioId(id);
        if (totalPecas > 0){
            throw new IllegalArgumentException("Esse usuário possui peças associadas e não pode ser exluída.");
        }
        usuarioRepository.deleteById(id);
    }

}
