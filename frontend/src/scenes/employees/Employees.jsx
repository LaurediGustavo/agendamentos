import React, { useState, useEffect } from "react";
import { Box, Fab } from "@mui/material";
import { Add as AddIcon } from "@mui/icons-material";
import Header from "../../components/headers/Headers";
import { DataTable } from "../../components/dataTable/dataTable";
import "./employees.scss";
import Action from "../../components/action/Action";
import api from '../../services/api';
import { formatarData_yyyy_MM_dd, formatarData_dd_MM_yyyy } from '../../services/dateFormat';

const columns = [
  {
    field: 'nome',
    headerName: 'Nome',
    width: 200,
    type: 'string'
  },
  {
    field: 'sobrenome',
    headerName: 'Sobrenome',
    width: 200,
    type: 'string'
  },
  {
    field: 'email',
    headerName: 'E-mail',
    width: 200,
    type: 'string'
  },
  {
    field: 'genero',
    headerName: 'Gênero',
    width: 200,
    type: 'string'
  },
  {
    field: 'cpf',
    headerName: 'CPF',
    type: 'string',
    width: 200
  },
  {
    field: 'telefone',
    headerName: 'Telefone',
    type: 'string',
    width: 200
  },
  {
    field: 'dataDeNascimento',
    headerName: 'Data de Nascimento',
    type: 'string',
    width: 200
  },
  {
    field: 'cep',
    headerName: 'CEP',
    type: 'string',
    width: 200
  },
  {
    field: 'bairro',
    headerName: 'Bairro',
    type: 'string',
    width: 200
  },
  {
    field: 'logradouro',
    headerName: 'Logradouro',
    type: 'string',
    width: 200
  },
  {
    field: 'numero',
    headerName: 'Número',
    type: 'string',
    width: 200
  },
  {
    field: 'bloco',
    headerName: 'Bloco',
    type: 'string',
    width: 200
  },
];

const Employees = () => {
  const [open, setOpen] = useState(false);
  const [editEmployee, setEditEmployee] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [initialEmployeesData, setInitialEmployeesData] = useState([]);
  const [initialProceduresData, setInitialProceduresData] = useState([]);
  const [removedItems, setRemovedItems] = useState([]);
  const [showDeleted, setShowDeleted] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);


  const getEmployees = async () => {
    try {
      const response = await api.get("/funcionario/consultar?nome=");

      const employees = response.data.map(employee => ({
        id: employee.id,
        nome: employee.nome,
        sobrenome: employee.sobrenome,
        dataDeNascimento: formatarData_dd_MM_yyyy(employee.dataDeNascimento),
        cpf: mascaraCpf(employee.cpf),
        genero: employee.genero,
        telefone: employee.telefone,
        cep: employee.cep,
        bairro: employee.bairro,
        logradouro: employee.logradouro,
        numero: employee.numero,
        bloco: employee.bloco,
        email: employee.email
      }));
      
      setInitialEmployeesData(employees);
    } catch (error) {
      console.error("Ops! Ocorreu um erro: " + error);
    }
  };

  const handleRemovedItensClick = async () => {
    try {
      const response = await api.get("/funcionario/consultar?nome=&desabilitado=true");

      const employees = response.data.map(employee => ({
        id: employee.id,
        nome: employee.nome,
        sobrenome: employee.sobrenome,
        dataDeNascimento: formatarData_dd_MM_yyyy(employee.dataDeNascimento),
        cpf: mascaraCpf(employee.cpf),
        genero: employee.genero,
        telefone: employee.telefone,
        cep: employee.cep,
        bairro: employee.bairro,
        logradouro: employee.logradouro,
        numero: employee.numero,
        bloco: employee.bloco,
        email: employee.email
      }));
      setRemovedItems(employees);
    } catch (error) {
      console.error("Ops! Ocorreu um erro: " + error);
    }
  };

  const mascaraCpf = (value) => {
    const cleanedCPF = value.replace(/[^\d]/g, '');
    return cleanedCPF.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  useEffect(() => {
    getEmployees();
  }, []);

  const handleEditClick = (employee) => {
    setIsEditing(true);
    setEditEmployee(employee);
    setOpen(true);
  };

  const handleAddClick = () => {
    setIsEditing(false);
    setOpen(true);
  };

  const handleSaveEmployee = async (data) => {
    try {
      if (isEditing) {
        const updatedEmployees = initialEmployeesData.map(employee => {
          if (employee.id === editEmployee.id) {
            const updatedEmployee = {
              ...employee,
              ...data
            };
  
            atualizar(updatedEmployee)
  
            return updatedEmployee;
          }
          return employee;
        });
        setInitialEmployeesData(updatedEmployees);
      } else {
        const newEmployees = {
          id: Math.random(),
          ...data
        };
  
        const id = await gravar(newEmployees)
        newEmployees.id = id
  
        const updatedEmployee = [...initialEmployeesData, newEmployees];
        setInitialEmployeesData(updatedEmployee);
      }
    } catch (error) {
      console.error("Erro ao salvar funcionário: " + error);
    }
    setOpen(false);
  };

  const gravar = async (employee) => {
    try {
      const response = await api.post("/funcionario/cadastro", {
          nome: employee.nome,
          sobrenome: employee.sobrenome,
          dataDeNascimento: formatarData_yyyy_MM_dd(employee.dataDeNascimento),
          cpf: employee.cpf,
          genero: employee.genero,
          telefone: employee.telefone,
          cep: employee.cep,
          logradouro: employee.logradouro,
          bairro: employee.bairro,
          numero: employee.numero,
          bloco: employee.bloco,
          email: employee.email,
      });
      return response.data.id;
    } catch (error) {
      throw new Error("Erro ao gravar funcionario: " + error.message);
    }
  };

  const atualizar = async (employee) => {
    try {
      await api.put("/funcionario/atualizar", {
        id: employee.id,
        nome: employee.nome,
        sobrenome: employee.sobrenome,
        dataDeNascimento: formatarData_yyyy_MM_dd(employee.dataDeNascimento),
        cpf: employee.cpf,
        genero: employee.genero,
        telefone: employee.telefone,
        cep: employee.cep,
        logradouro: employee.logradouro,
        bairro: employee.bairro,
        numero: employee.numero,
        bloco: employee.bloco,
        email: employee.email,
      });
    } catch (error) {
      throw new Error("Erro ao atualizar funcionario: " + error);
    }
  };

  const handleDeleteClick = (employee) => {
    setRemovedItems([...removedItems, employee]);
    const updatedEmployees = initialEmployeesData.filter(d => d.id !== employee.id);
    setInitialEmployeesData(updatedEmployees);
    remove(employee);
  };

  const remove = async (doutor) => {
    try {
      await api.delete("/funcionario/delete/" + doutor.id);
    } catch (error) {
      throw new Error("Erro ao atualizar funcionario: " + error);
    }
  };

  const handleReturnClick = (employee) => {
    const updatedRemovedItems = removedItems.filter(item => item.id !== employee.id);
    setRemovedItems(updatedRemovedItems);
    setInitialEmployeesData([...initialEmployeesData, employee]);
    returnEmployee(employee);
  };

  const returnEmployee = async (employee) => {
    try {
      await api.post("/funcionario/revertdelete/" + employee.id);
    } catch (error) {
      console.error("Erro ao retornar funcionario: " + error);
    }
  };

    const getCurrentUser = async () => {
      try {
        const response = await api.get("/usuario/usuariologado");
        setCurrentUser(response.data);
      } catch (error) {
        console.error("Erro ao obter usuário atual: " + error);
      }
    };
  
    useEffect(() => {
      getCurrentUser();
      getEmployees();
    }, []);
  

  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="Funcionários" subtitle="Registre e gerencie seus funcionários." />
      </Box>
      <DataTable
        slug="employee"
        columns={columns}
        rows={showDeleted ? [...initialEmployeesData, ...removedItems] : initialEmployeesData}
        deletedRows={removedItems}
        onEditClick={handleEditClick}
        onDeleteClick={handleDeleteClick}
        onReturnClick={handleReturnClick}
        onShowRemovedClick={handleRemovedItensClick}
        currentUser={currentUser} 
        />
      {open && (
        <Action
          slug="funcionário"
          columns={columns}
          setOpen={setOpen}
          onSave={handleSaveEmployee}
          isEditing={isEditing}
          initialData={isEditing ? editEmployee : null}
          procedures={initialProceduresData}
        />
      )}

      <Box display="flex" justifyContent="flex-end">
        <Fab
          onClick={handleAddClick}
          size="large"
          color="primary"
          aria-label="adicionar pacientes"
          style={{
            marginTop: '30px',
            marginRight: '20px',
            backgroundColor: "#3fbabf",
            zIndex: '500' 
          }}
        >
          <AddIcon />
        </Fab>
      </Box>
    </Box>
  );
};

export default Employees;