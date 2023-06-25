package br.com.tcc.impl;

import br.com.tcc.dto.AgendamentoDto;
import br.com.tcc.entity.Consulta;
import br.com.tcc.interfaces.ConsultaServiceInterface;
import br.com.tcc.repository.ConsultaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ConsultaService")
public class ConsultaService implements ConsultaServiceInterface {

	@Autowired
	private ConsultaRepository consultaRepository;
	
	@Override
	public void persistir(AgendamentoDto agendamentoDto) {
		Consulta consulta = new Consulta();
		BeanUtils.copyProperties(agendamentoDto, consulta);
		consultaRepository.save(consulta);
	}

}
