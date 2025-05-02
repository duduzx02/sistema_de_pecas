package com.oficina.sistemapecas.repository;

import com.oficina.sistemapecas.model.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PecaRepository extends JpaRepository<Peca, Long> {
    List<Peca> findByUsuarioId(Long usuarioId);

    long countByUsuarioId(long usuarioId);
}
