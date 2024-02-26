import React, { useState, useEffect } from "react";
import { Box, Fab } from "@mui/material";
import { Add as AddIcon } from "@mui/icons-material";
import Header from "../../components/headers/Headers";
import { DataTable } from "../../components/dataTable/dataTable";
import "./employees.scss";
import Action from "../../components/action/Action";
import api from '../../services/api';
import { formatarData_yyyy_MM_dd } from '../../services/dateFormat';
import { employeesData } from "../../data/dados"; // Importando o mock de dados de funcionários

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
    field: 'genero',
    headerName: 'Gênero',
    width: 200,
    type: 'string'
  },
  {
    field: 'especialidade',
    headerName: 'Especialidade',
    width: 200,
    type: 'string',
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

  const getEmployees = async () => {
    try {
      // Utilizando o mock de dados de funcionários
      const employees = employeesData.map(employee => ({
        id: employee.id,
        nome: employee.nome,
        sobrenome: employee.sobrenome,
        dataDeNascimento: employee.dataDeNascimento,
        cpf: employee.cpf,
        genero: employee.genero,
        telefone: employee.telefone,
        cep: employee.cep,
        cidade: employee.cidade,
        bairro: employee.bairro,
        logradouro: employee.logradouro,
        numero: employee.numero,
        bloco: employee.bloco,
        especialidade: employee.especialidade
      }));
      setInitialEmployeesData(employees);
    } catch (error) {
      console.error("Ops! Ocorreu um erro: " + error);
    }
  };

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
        // Atualizar funcionário
        await api.put(`/funcionario/atualizar/${editEmployee.id}`, data);
        const updatedEmployees = initialEmployeesData.map(employee => {
          if (employee.id === editEmployee.id) {
            return { ...employee, ...data };
          }
          return employee;
        });
        setInitialEmployeesData(updatedEmployees);
      } else {
        // Adicionar novo funcionário
        const response = await api.post("/funcionario/cadastro", data);
        const newEmployee = { id: response.data.id, ...data };
        setInitialEmployeesData([...initialEmployeesData, newEmployee]);
      }
    } catch (error) {
      console.error("Erro ao salvar funcionário: " + error);
    }
    setOpen(false);
  };

  const handleDeleteClick = (employee) => {
    setRemovedItems([...removedItems, employee]);
    const updatedEmployees = initialEmployeesData.filter(e => e.id !== employee.id);
    setInitialEmployeesData(updatedEmployees);
  };



  const handleReturnClick = (employee) => {
    const updatedRemovedItems = removedItems.filter(item => item.id !== employee.id);
    setRemovedItems(updatedRemovedItems);
    setInitialEmployeesData([...initialEmployeesData, employee]);
  };

  const returnEmployee = async (employee) => {
    // Lógica para retornar um funcionario excluído
    try {
    } catch (error) {
      console.error("Erro ao retornar funcionario: " + error);
    }
  };

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