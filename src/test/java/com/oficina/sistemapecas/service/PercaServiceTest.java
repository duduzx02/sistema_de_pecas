package com.oficina.sistemapecas.service;

import com.oficina.sistemapecas.model.Peca;
import com.oficina.sistemapecas.model.Urgencia;
import com.oficina.sistemapecas.repository.PecaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PercaServiceTest {

    @Autowired
    private PecaRepository pecaRepository;

    private PecaService pecaService;

    @BeforeEach
    public void setUp(){
        pecaService = new PecaService(pecaRepository);
    }

    @Test
    public void testSalvarPeca(){
        Peca peca = new Peca();
        peca.setNome("Pastilha de freio");
        peca.setDescricao("Pastilha dianteira para carro compacto");
        peca.setValor(new BigDecimal("120.00"));
        peca.setUrgencia(Urgencia.VERMELHO);

        Peca salva = pecaService.salvar(peca);

        assertNotNull(salva.getId());
        assertEquals("Pastilha de freio", salva.getNome());
        assertEquals(new BigDecimal("120.00"), salva.getValor());

    }

    @Test
    public void testBuscasTodasPecas(){
        Peca peca1 = new Peca();
        peca1.setNome("Óleo");
        peca1.setDescricao("Óleo sintético 5w30");
        peca1.setValor(new BigDecimal("59.90"));
        peca1.setUrgencia(Urgencia.VERDE);

        Peca peca2 = new Peca();
        peca2.setNome("Filtro de ar");
        peca2.setDescricao("Filtro de ar para motor 1.6");
        peca2.setValor(new BigDecimal("39.99"));
        peca2.setUrgencia(Urgencia.LARANJA);

        pecaService.salvar(peca1);
        pecaService.salvar(peca2);

        List<Peca> pecas = pecaService.listarTodas();

        assertEquals(2, pecas.size());
    }

    @Test
    public void testDeletarPeca(){
        Peca peca = new Peca();
        peca.setNome("Amortecedor");
        peca.setDescricao("Amortecedor traseiro");
        peca.setValor(new BigDecimal("250.00"));
        peca.setUrgencia(Urgencia.AZUL);

        Peca salva = pecaService.salvar(peca);

        Long id = salva.getId();
        pecaService.excluir(id);

        assertNull(pecaService.buscarPorId(id));

    }

}
