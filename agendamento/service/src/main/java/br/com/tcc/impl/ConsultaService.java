package br.com.tcc.impl;

import br.com.tcc.dto.ConsultaDto;
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
	public void persistir(ConsultaDto consultaDto) {
		Consulta consulta = new Consulta();
		BeanUtils.copyProperties(consultaDto, consulta);
	}

}
