import React, { useState, useEffect } from 'react';
import './action.scss';
import { Button, TextField, Select, MenuItem, TextareaAutosize } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import InputMask from 'react-input-mask';

const Action = (props) => {
  const [formData, setFormData] = useState(props.initialData || {});
  const [errors, setErrors] = useState({});

  useEffect(() => {
    setFormData(props.initialData || {}); // Atualiza dados do formulário com props iniciais
  }, [props.initialData]); // Apenas quando as props iniciais mudam

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name !== 'cpf') {
      setFormData({
        ...formData, // Mantém os dados existentes
        [name]: value // Atualiza o campo específico
      });
    } else {
      // Remove a formatação do CPF antes de definir o valor no estado
      const cleanedCPF = value.replace(/[^\d]/g, '');
      setFormData({
        ...formData,
        [name]: cleanedCPF
      });
    }
  }

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      props.onSave(formData);
    }
  }

  // Função para validar o formulário
  const validateForm = () => {
    let valid = true; // Assume que o formulário é válido
    const newErrors = {}; // Cria um novo objeto para erros

    props.columns.forEach(column => {
      const { field, headerName } = column;

      if (!formData[field] && field !== 'bloco') {
        newErrors[field] = `O campo ${headerName} é obrigatório`;
        valid = false;
      } else if (field === 'nome' && formData[field].length < 2) {
        newErrors[field] = `O ${headerName} deve ter pelo menos 2 caracteres`;
        valid = false;
      } else if (field === 'especialidade' && formData[field].length < 2) {
        newErrors[field] = `A ${headerName} deve ter pelo menos 2 caracteres`;
        valid = false;
      } else if (field === 'cpf' && !validateCPF(formData[field])) {
        newErrors[field] = 'CPF inválido';
        valid = false;
      } else if (field === 'dataDeNascimento' && !validateDate(formData[field])) {
        newErrors[field] = 'Data de nascimento inválida';
        valid = false;
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

  const validateDate = (date) => {
    const parts = date.split('/');
    if (parts.length !== 3) {
      return false;
    }

    const day = parseInt(parts[0]);
    const month = parseInt(parts[1]);
    const year = parseInt(parts[2]);

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

    return true;
  }

  return (
    <div className="activated">
      <div className="modal">
        <span className="close" onClick={() => props.setOpen(false)}><CloseIcon /></span>
        <h1>{props.isEditing ? `Editar ${props.slug}` : `Cadastrar um novo ${props.slug}`}</h1>
        <form onSubmit={handleSubmit}>
          {props.columns.map(column => (
            <div className="item" key={column.field}>
              <label>{column.headerName}:</label>
              {column.field === 'cpf' ? (
                <InputMask
                  className="cpf-input"
                  mask="999.999.999-99"
                  placeholder={column.headerName}
                  name={column.field}
                  defaultValue={formData[column.field] || ''}
                  onChange={handleInputChange}
                  disabled={props.isEditing}
                  style={props.isEditing ? { pointerEvents: 'none', backgroundColor: '#eeeeee', width: '100%' } : { width: '100%' }}
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
                  style={{ width: '100%' }}
                  className={errors[column.field] ? 'error' : ''}
                >
                  {(inputProps) => (
                    <TextField
                      {...inputProps}
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
                  style={{ width: '100%' }}
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
                  style={{ width: '100%' }}
                  className={errors[column.field] ? 'error' : ''}
                >
                  {(inputProps) => (
                    <TextField
                      {...inputProps}
                    />
                  )}
                </InputMask>
              ) : column.field === 'tempo' ? ( 
                <InputMask
                  mask="99:99"
                  placeholder={column.headerName}
                  name={column.field}
                  value={formData[column.field] || ''}
                  onChange={handleInputChange}
                  style={{ width: '100%' }}
                  className={errors[column.field] ? 'error' : ''}
                >
                  {(inputProps) => (
                    <TextField
                      {...inputProps}
                    />
                  )}
                </InputMask>
              ) : column.field === 'informacoesAdicionais' || column.field === 'procedimento' ? (
                <TextareaAutosize
                  placeholder={column.headerName}
                  className={`${column.field} largeTextarea ${errors[column.field] ? 'error' : ''}`}
                  name={column.field}
                  value={formData[column.field] || ''}
                  onChange={handleInputChange}
                />
              ) : column.field === 'genero' ? (
                <Select
                  name={column.field}
                  value={formData[column.field] || ''}
                  onChange={handleInputChange}
                  className={errors[column.field] ? 'error' : ''}
                  style={{ width: '100%', height: '60%' }}
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
                  style={{ width: '100%', height: '60%' }}
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
                  style={{ width: '100%' }}
                  name={column.field}
                  value={formData[column.field] || ''}
                  onChange={handleInputChange}
                  className={errors[column.field] ? 'error' : ''}
                />
              ) : (
                <TextField
                  type={column.type}
                  placeholder={column.headerName}
                  style={{ width: '100%' }}
                  name={column.field}
                  value={formData[column.field] || ''}
                  onChange={handleInputChange}
                  className={errors[column.field] ? 'error' : ''}
                />
              )}
              {errors[column.field] && <span className="error-message">{errors[column.field]}</span>}
            </div>
          ))}
          <Button variant="contained" color="primary" type="submit">
            {props.isEditing ? 'Salvar' : 'Cadastrar'}
          </Button>
        </form>
      </div>
    </div>
  );
}  

export default Action;
