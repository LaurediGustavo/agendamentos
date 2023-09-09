import React, { useState, useEffect } from 'react';
import '../edit/edit.scss';
import Button from '@mui/material/Button';
import CloseIcon from '@mui/icons-material/Close';
import InputMask from 'react-input-mask';

export function Edit(props) {
  const [formData, setFormData] = useState(props.doctor || {}); // Estado para os dados do formulário
  const [errors, setErrors] = useState({}); // Estado para erros de validação


    useEffect(() => {
      setFormData(props.doctor || {}); // Atualiza o estado quando props.doctor muda
    }, [props.doctor]);

    const handleInputChange = (e) => {
      const { name, value } = e.target;
      if (name !== 'cpf') {
          setFormData({
              ...formData,
              [name]: value
          });
      }
  }

    const handleSubmit = (e) => {
        e.preventDefault();
        if (validateForm()) {
            props.onSave(formData);
        }
    }
// Funções de validação para CPF e data de nascimento
    const validateForm = () => {
        let valid = true;
        const newErrors = {};

        props.columns.forEach(column => {
            const { field, headerName } = column;

            if (!formData[field]) {
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

    const currentYear = new Date().getFullYear(); // Obtém o ano atual
    if (year > currentYear) { // Verifica se o ano de nascimento é maior que o atual
        return false;
    }

    return true;
}


  return (
    <div className="add">
        <div className="modal">
            <span className="close" onClick={() => props.setOpen(false)}><CloseIcon /></span>
            <h1>Editar {props.slug}</h1>
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
                          defaultValue={formData[column.field] || ''} // Use defaultValue em vez de value
                          onChange={handleInputChange}
                          disabled 
                          style={{
                            pointerEvents: 'none', // Desabilita eventos de clique
                            backgroundColor: '#dbd8d8' // Define a cor de fundo cinza
                          }}
                      >
                          {(inputProps) => (
                              <input
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
                            />
                        ) : column.field === 'dataDeNascimento' ? (
                            <InputMask
                                mask="99/99/9999"
                                placeholder={column.headerName}
                                name={column.field}
                                value={formData[column.field] || ''}
                                onChange={handleInputChange}
                                style={{ width: '100%' }}
                                className={errors[column.field] ? 'error' : ''}
                            />
                        ) : column.field === 'informacoesAdicionais' || column.field === 'procedimento' ? (
                            <textarea
                                placeholder={column.headerName}
                                className={`${column.field} largeTextarea ${errors[column.field] ? 'error' : ''}`}
                                name={column.field}
                                value={formData[column.field] || ''}
                                onChange={handleInputChange}
                            />
                        ) : (
                            <input
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