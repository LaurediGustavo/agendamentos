import React, { useState, useEffect } from 'react';
import './action.scss';
import { Button, TextField, Select, MenuItem, FormControlLabel, Checkbox, Autocomplete } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import InputMask from 'react-input-mask';
import api from '../../services/api';
import axios from 'axios';


const Action = (props) => {
  const [formData, setFormData] = useState({
    ...props.initialData,
    pacienteId: props.initialData?.pacienteId || ''
  });
  const [errors, setErrors] = useState({});
  const [showResponsavelCampos, setShowResponsavelCampos] = useState(formData.responsavelLegal || false);

  // Estado para armazenar os pacientes e o paciente selecionado
  const [pacientes, setPacientes] = useState([]);
  const [consultaForm, setConsultaForm] = useState({ pacienteId: '' });
  const [searchTerm, setSearchTerm] = useState('');

  // Função para atualizar o paciente selecionado
  const atualizarConsulta = (atributo, novoValor) => {
    setConsultaForm({ ...consultaForm, [atributo]: novoValor });
  };

  const [cepValido, setCepValido] = useState(true);

  // Função para buscar os dados do CEP
  const buscarCEP = async (cep) => {
    try {
      const response = await axios.get(`https://viacep.com.br/ws/${cep}/json/`);
      return response.data;
    } catch (error) {
      console.error("Erro ao buscar CEP: ", error);
    }
  };

  useEffect(() => {
    if (formData.cep && formData.cep.replace(/\D/g, '').length === 8) {
      buscarCEP(formData.cep).then((data) => {
        if (!data.erro) {
          setFormData({
            ...formData,
            logradouro: data.logradouro,
            bairro: data.bairro,
          });
          setCepValido(true);
        } else {
          setCepValido(false);
        }
      });
    }
  }, [formData.cep]);


  const handleAutocompleteChange = (event, newValue) => {
    // Adicione uma verificação de nulo para newValue
    if (newValue) {
      // Atualiza o estado formData com o paciente selecionado
      setFormData({
        ...formData,
        pacienteId: newValue.id || '', 
        responsavel: newValue.id || '',
        telefoneResponsavel: newValue.telefone || '',
        responsavelNome: `${newValue.nome} - ${newValue.cpf}` || '',
      });
    } else {

      setFormData({
        ...formData,
        pacienteId: '',
        responsavel: '',
        telefoneResponsavel: '',
        responsavelNome: '',
      });
    }
  };
  

  useEffect(() => {
    setFormData(props.initialData || {});
  }, [props.initialData]);

  useEffect(() => {
    // Chamada à API para obter os pacientes
    api.get(`/paciente/consultar/agendamento?nome=${searchTerm}`)
      .then((response) => setPacientes(response.data))
      .catch((err) => {
        console.error("ops! ocorreu um erro" + err);
      });
  }, [searchTerm]);

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;

    // Verifica se o campo é um checkbox
    if (type === 'checkbox') {
      // Atualiza a visibilidade dos campos relacionados
      if (name === 'responsavelLegal') {
        setShowResponsavelCampos(checked);

        // Atualiza o estado formData e limpa os campos relacionados quando o responsavelLegal for false
        setFormData(prevData => ({
          ...prevData,
          [name]: checked,
          relacaoResponsavel: checked ? prevData.relacaoResponsavel : '',
          responsavel: checked ? prevData.responsavel : null,
          telefoneResponsavel: checked ? prevData.telefoneResponsavel : '',
          responsavelNome: checked ? prevData.responsavelNome : ''
        }));
      } else {
        // Se não for um checkbox, atualiza o estado diretamente
        setFormData({
          ...formData,
          [name]: checked // Define o valor como true ou false, dependendo se o checkbox está marcado ou não
        });
      }
    } else {
      if (name === 'nome' || name === 'sobrenome') {

        if (/^[a-zA-ZÀ-ú\s-]*$/.test(value)) {
          setFormData({
            ...formData,
            [name]: value
          });
        }
      } else if (name === 'cpf') {
        // Limpa o valor do CPF e aplica a máscara
        const cleanedCPF = value.replace(/[^\d]/g, '');
        const maskedCPF = cleanedCPF.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');

        // Atualiza o estado formData com o CPF formatado
        setFormData({
          ...formData,
          [name]: maskedCPF
        });
      } else if (name === 'cro') {
        // Limite o campo CRO para 10 caracteres
        const croValue = value.slice(0, 10);
        setFormData({
          ...formData,
          [name]: croValue
        });
      } else if (name === 'numero') {
        // Para o campo 'numero', remova todos os caracteres não numéricos
        const numericValue = value.replace(/[^\d]/g, '');

        // Atualize diretamente o estado formData com o valor numérico
        setFormData({
          ...formData,
          [name]: numericValue
        });
      } else if (name === 'tempo') {
        const cleanedTime = value.replace(/[^\d]/g, '');
        const maskedTime = cleanedTime.replace(/(\d{2})(\d{2})/, '$1:$2');

        setFormData({
          ...formData,
          [name]: maskedTime
        });
      } else if (name === 'especialidade') {
        const arrayFiltrado = props.procedures.filter(item => value.includes(item.id));

        const especialidadesNomes = arrayFiltrado.map(p => p.tratamento);

        setFormData({
          ...formData,
          [name]: value,
          'especialidadeNome': especialidadesNomes,
        });
      } else if (name === 'valor') {
        // Remova todos os caracteres não numéricos
        const numericValue = value.replace(/[^\d]/g, '');

        // Adicione vírgula como separador de milhar
        const formattedValue = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(numericValue / 100);

        // Atualize diretamente o estado formData com o valor formatado
        setFormData({
          ...formData,
          [name]: formattedValue
        });
      } else {
        // Para outros campos, atualize diretamente o estado formData
        setFormData({
          ...formData,
          [name]: value
        });
      }
    }
  }




  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      props.onSave({ ...formData });
    }
  }

  const validateForm = () => {
    let valid = true;
    const newErrors = {};

    props.columns.filter(coluna => coluna.renderedForm !== false).forEach(column => {
      const { field, headerName } = column;

      if (!formData[field] && field !== 'bloco' && field !== 'responsavelLegal' && field !== 'informacoesAdicionais' && (showResponsavelCampos || !['relacaoResponsavel', 'telefoneResponsavel', 'responsavel'].includes(field))) {
        newErrors[field] = `O campo ${headerName} é obrigatório`;
        valid = false;
      } else if (field === 'nome' && formData[field].length < 2) {
        newErrors[field] = `O ${headerName} deve ter pelo menos 2 caracteres`;
        valid = false;
      } else if (field === 'telefone' && formData[field].replace(/\D/g, '').length !== 11) {
        newErrors[field] = 'Telefone inválido'; // ou outra mensagem de erro apropriada
        valid = false;
      } else if (field === 'cpf' && !validateCPF(formData[field])) {
        newErrors[field] = 'CPF inválido';
        valid = false;
      } else if (field === 'dataDeNascimento' && !validateDate(formData[field])) {
        newErrors[field] = 'Data de nascimento inválida';
        valid = false;
      } else if (field === 'email' && !validateEmail(formData[field])) {
        newErrors[field] = 'E-mail inválido'; // ou outra mensagem de erro apropriada
        valid = false;
      } else if (field === 'valor' && Number(formData[field].replace(/[^\d]/g, '')) === 0) {
        newErrors[field] = 'Valor inválido';
        valid = false;
      } else if (field === 'cep') {
        if (formData[field].replace(/\D/g, '').length !== 8) { // Verifica se o CEP tem 8 dígitos numéricos
          newErrors[field] = 'CEP deve ter 8 dígitos';
          valid = false;
        } else if (!cepValido) {
          newErrors[field] = 'CEP inválido';
          valid = false;
        }
      } else if (field === 'tempo') {
        const cleanedTime = formData[field].replace(/[^\d]/g, '');
        if (cleanedTime.length === 4 && cleanedTime !== '0000') {
        } else {
          newErrors[field] = 'Tempo inválido';
          valid = false;
        }
      }
    });

    setErrors(newErrors);
    return valid;
  }

  const validateCPF = (cpf) => {
    const cpfClean = cpf.replace(/[^\d]/g, '');

    if (cpfClean.length !== 11 || /^(.)\1+$/.test(cpfClean)) {
      return false;
    }

    let sum = 0;
    for (let i = 0; i < 9; i++) {
      sum += parseInt(cpfClean[i]) * (10 - i);
    }

    let remainder = sum % 11;
    if (remainder < 2) {
      if (parseInt(cpfClean[9]) !== 0) {
        return false;
      }
    } else {
      if (parseInt(cpfClean[9]) !== 11 - remainder) {
        return false;
      }
    }

    sum = 0;
    for (let i = 0; i < 10; i++) {
      sum += parseInt(cpfClean[i]) * (11 - i);
    }

    remainder = sum % 11;
    if (remainder < 2) {
      if (parseInt(cpfClean[10]) !== 0) {
        return false;
      }
    } else {
      if (parseInt(cpfClean[10]) !== 11 - remainder) {
        return false;
      }
    }

    return true;
  }

  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }


  const validateDate = (date) => {
    const parts = date.split('/');
    if (parts.length !== 3) {
      return false;
    }

    const day = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10);
    const year = parseInt(parts[2], 10);

    if (isNaN(day) || isNaN(month) || isNaN(year)) {
      return false;
    }

    if (day < 1 || day > 31 || month < 1 || month > 12) {
      return false;
    }

    const lastDayOfMonth = new Date(year, month, 0).getDate();
    if (day > lastDayOfMonth) {
      return false;
    }

    const currentYear = new Date().getFullYear();
    if (year < 1900 || year > currentYear) {
      return false;
    }

    return true;
  };


  const mascaraCpf = (value) => {
    const cleanedCPF = value.replace(/[^\d]/g, '');
    return cleanedCPF.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  //Para impedir rolagem se o modal estiver aberto:
  useEffect(() => {
    document.body.classList.add('modal-open');
    return () => {
      document.body.classList.remove('modal-open');
    };
  }, []);



  return (
    <div className="activated">
      <div className="modal">
        <span className="close" onClick={() => props.setOpen(false)}><CloseIcon /></span>
        <h1>{props.isEditing ? `Editar ${props.slug}` : `Cadastrar um novo ${props.slug}`}</h1>
        <form onSubmit={handleSubmit}>
          {props.columns.filter(coluna => coluna.renderedForm !== false).map(column => (
            <React.Fragment key={column.field}>
              {column.field === 'responsavelLegal' ? (
                <div style={{ width: '100%', marginBottom: '10px' }}>
                  <FormControlLabel
                    control={
                      <Checkbox
                        checked={showResponsavelCampos}
                        onChange={handleInputChange}
                        name={column.field}
                      />
                    }
                    label={column.headerName}
                  />
                </div>
              ) : (
                showResponsavelCampos || !['relacaoResponsavel', 'telefoneResponsavel', 'responsavel'].includes(column.field)) && (
                <div className="item">
                  {column.field === 'responsavel' ? (
                    <><label>{column.headerName}:</label>
                      <Autocomplete
                        value={pacientes.find(paciente => paciente.id === formData.responsavel) || null}
                        onChange={handleAutocompleteChange} // Aqui está passando a função handleInputChange para o onChange
                        options={pacientes}
                        getOptionLabel={(option) => `${option.nome} - ${mascaraCpf(option.cpf)}`}
                        renderInput={(params) => (
                          <TextField
                            {...params}
                            variant="outlined"
                            className={`${errors['responsavel'] ? 'error' : ''} meuCampo`}
                            error={!!errors['responsavel']}
                            style={{ width: '100%', marginTop: '10px' }}
                          />
                        )}
                      />
                    </>
                  ) : (
                    <>
                      <label>{column.headerName}:</label>
                      {column.field === 'relacaoResponsavel' ? (
                        <Select
                          name={column.field}
                          value={formData[column.field] || ''}
                          onChange={handleInputChange}
                          className={errors[column.field] ? 'error' : ''}
                          style={{ width: '100%', marginTop: '10px' }}
                        >
                          <MenuItem value="Pai/Mãe">Pai/Mãe</MenuItem>
                          <MenuItem value="Cônjuge">Cônjuge</MenuItem>
                          <MenuItem value="Filho/Filha">Filho/Filha</MenuItem>
                          <MenuItem value="Irmão/Irmã">Irmão/Irmã</MenuItem>
                          <MenuItem value="Outro">Outro</MenuItem>
                        </Select>
                      ) : column.field === 'cpf' ? (
                        <InputMask
                          className="cpf-input"
                          mask="999.999.999-99"
                          placeholder={column.headerName}
                          name={column.field}
                          defaultValue={formData[column.field] || ''}
                          onChange={handleInputChange}
                          disabled={props.isEditing}
                          style={{ width: '100%', marginTop: '10px' }}
                        >
                          {(inputProps) => (
                            <TextField
                              {...inputProps}
                              className={errors[column.field] ? 'error' : ''}
                              disabled={props.isEditing}
                            />
                          )}
                        </InputMask>
                      ) : column.field === 'tempo' ? (
                        <InputMask
                          className="time-input"
                          mask="99:99"
                          placeholder={column.headerName}
                          name={column.field}
                          defaultValue={formData[column.field] || ''}
                          onChange={handleInputChange}
                          style={{ width: '100%', marginTop: '10px' }}
                        >
                          {(inputProps) => (
                            <TextField
                              {...inputProps}
                              className={errors[column.field] ? 'error' : ''}
                            />
                          )}
                        </InputMask>
                      ) : column.field === 'telefone' ? (
                        <InputMask
                          mask="(99) 99999-9999"
                          placeholder={column.headerName}
                          name={column.field}
                          value={formData[column.field] || ''}
                          onChange={handleInputChange}
                          style={{ width: '100%', marginTop: '10px' }}
                          className={errors[column.field] ? 'error' : ''}
                        >
                          {(inputProps) => (
                            <TextField
                              {...inputProps}
                            />
                          )}
                        </InputMask>
                      ) : column.field === 'telefoneResponsavel' ? (
                        <InputMask
                          mask="(99) 99999-9999"
                          placeholder={column.headerName}
                          name={column.field}
                          value={formData[column.field] || ''}
                          onChange={handleInputChange}
                          className={errors[column.field] ? 'error' : ''}
                          style={{ width: '100%', marginTop: '10px' }}
                          disabled={true}
                        >
                          {(inputProps) => (
                            <TextField
                              {...inputProps}
                              disabled={true}
                            />
                          )}
                        </InputMask>
                      ) : column.field === 'cep' ? (
                        <InputMask
                          mask="99999-999"
                          placeholder={column.headerName}
                          name={column.field}
                          value={formData[column.field] || ''}
                          onChange={handleInputChange}
                          style={{ width: '100%', marginTop: '10px' }}
                          className={errors[column.field] ? 'error' : ''}
                        >
                          {(inputProps) => (
                            <TextField
                              {...inputProps}
                            />
                          )}
                        </InputMask>
                      ) : column.field === 'dataDeNascimento' ? (
                        <InputMask
                          mask="99/99/9999"
                          placeholder={column.headerName}
                          name={column.field}
                          value={formData[column.field] || ''}
                          onChange={handleInputChange}
                          style={{ width: '100%', marginTop: '10px' }}
                          className={errors[column.field] ? 'error' : ''}
                        >
                          {(inputProps) => (
                            <TextField
                              {...inputProps}
                            />
                          )}
                        </InputMask>
                      ) : column.field === 'genero' ? (
                        <Select
                          name={column.field}
                          value={formData[column.field] || ''}
                          onChange={handleInputChange}
                          className={errors[column.field] ? 'error' : ''}
                          style={{ width: '100%', marginTop: '10px' }}
                        >
                          <MenuItem value="Masculino">Masculino</MenuItem>
                          <MenuItem value="Feminino">Feminino</MenuItem>
                          <MenuItem value="Outros">Outros</MenuItem>
                        </Select>
                      ) : column.field === 'especialidade' ? (
                        <Select
                          multiple
                          name={column.field}
                          value={formData[column.field] || []}
                          onChange={handleInputChange}
                          className={errors[column.field] ? 'error' : ''}
                          style={{ width: '100%', marginTop: '10px' }}
                        >
                          {props.procedures && props.procedures.map((procedure) => (
                            <MenuItem key={procedure.id} value={procedure.id}>
                              {procedure.tratamento}
                            </MenuItem>
                          ))}
                        </Select>
                      ) : column.field === 'logradouro' || column.field === 'bairro' || column.field === 'numero' || column.field === 'cidade' || column.field === 'estado' ? (
                        <TextField
                          type="text"
                          placeholder={column.headerName}
                          style={{ width: '100%', marginTop: '10px' }}
                          name={column.field}
                          value={formData[column.field] || ''}
                          onChange={handleInputChange}
                          className={errors[column.field] ? 'error' : ''}
                        />
                      ) : (
                        <TextField
                          type={column.type}
                          placeholder={column.headerName}
                          style={{ width: '100%', marginTop: '10px' }}
                          name={column.field}
                          value={formData[column.field] || ''}
                          onChange={handleInputChange}
                          className={errors[column.field] ? 'error' : ''}
                        />
                      )}
                    </>
                  )}
                  {errors[column.field] && <span className="error-message">{errors[column.field]}</span>}
                </div>
              )}
            </React.Fragment>
          ))}
          <Button className="custom-button" type="submit" variant="contained" color="primary" style={{ marginTop: '15px' }}>
            {props.isEditing ? 'Atualizar' : 'Salvar'}
          </Button>
        </form>
      </div>
    </div>
  );

}

export default Action;