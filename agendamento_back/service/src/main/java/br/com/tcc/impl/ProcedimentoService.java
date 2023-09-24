package br.com.tcc.impl;

import br.com.tcc.repository.ProcedimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ProcedimentoService")
public class ProcedimentoService {

    @Autowired
    private ProcedimentoRepository procedimentoRepository;


}

