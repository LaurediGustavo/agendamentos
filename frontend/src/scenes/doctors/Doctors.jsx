import React, { useState, useEffect } from "react";
import { Box, Fab } from "@mui/material";
import { Add as AddIcon } from "@mui/icons-material";
import Header from "../../components/headers/Headers";
import { DataTable } from "../../components/dataTable/dataTable";
import "./doctors.scss";
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
    field: 'cro',
    headerName: 'CRO',
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


const Doctors = () => {

  const [open, setOpen] = useState(false);
  const [editDoctor, setEditDoctor] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [initialDoctorsData, setInitialDoctorsData] = useState([]);
  const [initialProceduresData, setInitialProceduresData] = useState([]);

  const getDoutores = async () => {
    try {
      const response = await api.get("/doutor/consultar?nome=");
      const doutores = response.data.map((doutore) => ({
        id: doutore.funcionarioResponse.id,
        nome: doutore.funcionarioResponse.nome,
        sobrenome: doutore.funcionarioResponse.sobrenome,
        dataDeNascimento: formatarData_dd_MM_yyyy(doutore.funcionarioResponse.dataDeNascimento),
        cpf: doutore.funcionarioResponse.cpf,
        genero: doutore.funcionarioResponse.genero,
        telefone: doutore.funcionarioResponse.telefone,
        cep: doutore.funcionarioResponse.cep,
        logradouro: doutore.funcionarioResponse.logradouro,
        bairro: doutore.funcionarioResponse.bairro,
        numero: doutore.funcionarioResponse.numero,
        bloco: doutore.funcionarioResponse.bloco,
        cro: doutore.cro,
        especialidade: doutore.procedimentos.map(procedimento => procedimento.id),
        especialidadeNome: doutore.procedimentos.map(procedimento => procedimento.tratamento),
      }));
      setInitialDoctorsData(doutores);
    } catch (error) {
      console.error("Ops! Ocorreu um erro: " + error);
    }
  };

  const getProcedures = async () => {
    try {
      const response = await api.get("/procedimento/consultar?tratamento= ");
      const procedimentos = response.data.map((procedimento) => ({
        id: procedimento.id,
        tratamento: procedimento.tratamento,
        valor: procedimento.valor,
        tempo: procedimento.tempo,
      }));
      setInitialProceduresData(procedimentos);
    } catch (error) {
      console.error("Ops! Ocorreu um erro: " + error);
    }
  };

  useEffect(() => {
    getDoutores();
    getProcedures();
  }, []);

  const handleEditClick = (doctor) => {
    setIsEditing(true);
    setEditDoctor(doctor);
    setOpen(true);
  };

  const handleAddClick = () => {
    setIsEditing(false);
    setOpen(true);
  };

  const handleSaveDoctor = async (data) => {
    if (isEditing) {
      const updatedDoctors = initialDoctorsData.map(doctor => {
        if (doctor.id === editDoctor.id) {
          const updatedDoctors = {
            ...doctor,
            ...data
          };

          atualizar(updatedDoctors)

          return updatedDoctors;
        }
        return doctor;
      });

      setInitialDoctorsData(updatedDoctors);
    } else {
      const newDoctor = {
        id: Math.random(),
        ...data
      };

      const id = await gravar(newDoctor)
      newDoctor.id = id

      const updatedDoctors = [...initialDoctorsData, newDoctor];
      setInitialDoctorsData(updatedDoctors);
    }
    setOpen(false);
  };

  const gravar = async (doutore) => {
    try {
      const response = await api.post("/doutor/cadastro", {
        funcionario: {
          nome: doutore.nome,
          sobrenome: doutore.sobrenome,
          dataDeNascimento: formatarData_yyyy_MM_dd(doutore.dataDeNascimento),
          cpf: doutore.cpf,
          genero: doutore.genero,
          telefone: doutore.telefone,
          cep: doutore.cep,
          logradouro: doutore.logradouro,
          bairro: doutore.bairro,
          numero: doutore.numero,
          bloco: doutore.bloco,
        },
        cro: doutore.cro,
        procedimentos: doutore.especialidade
      });
      return response.data.id;
    } catch (error) {
      throw new Error("Erro ao gravar doutor: " + error.message);
    }
  };

  const atualizar = async (doutore) => {
    try {
      await api.put("/doutor/atualizar", {
        id: doutore.id,
        funcionario: {
          id: doutore.id,
          nome: doutore.nome,
          sobrenome: doutore.sobrenome,
          dataDeNascimento: formatarData_yyyy_MM_dd(doutore.dataDeNascimento),
          cpf: doutore.cpf,
          genero: doutore.genero,
          telefone: doutore.telefone,
          cep: doutore.cep,
          logradouro: doutore.logradouro,
          bairro: doutore.bairro,
          numero: doutore.numero,
          bloco: doutore.bloco,
        },
        cro: doutore.cro,
        procedimentos: doutore.especialidade
      });
    } catch (error) {
      throw new Error("Erro ao atualizar doutor: " + error);
    }
  };

  const handleDeleteClick = (doutor) => {
    remove(doutor)

    const updatedDoctors = initialDoctorsData.filter(p => p.id !== doutor.id);
    setInitialDoctorsData(updatedDoctors);
  };

  const remove = async (doutor) => {
    try {
      await api.delete("/doutor/delete/" + doutor.id);
    } catch (error) {
      throw new Error("Erro ao atualizar doutor: " + error);
    }
  };

  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="Doutores" subtitle="Registre e gerencie seus doutores." />
      </Box>

      <DataTable slug="doctor" columns={columns} rows={initialDoctorsData} onEditClick={handleEditClick} onDeleteClick={handleDeleteClick} />

      {open && (
        <Action
          slug="doutor"
          columns={columns}
          setOpen={setOpen}
          onSave={handleSaveDoctor}
          isEditing={isEditing}
          initialData={isEditing ? editDoctor : null}
          procedures={initialProceduresData} // Passando os procedimentos como propriedade
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
            zIndex: '500' // Deve ser uma string
          }}
        >
          <AddIcon />
        </Fab>
      </Box>
    </Box>
  );
};

export default Doctors;
