import { Box, Fab } from "@mui/material";
import { Add as AddIcon } from "@mui/icons-material"; 
import Header from "../../components/headers/Headers";
import React, { useState } from "react";
import { DataTable } from "../../components/dataTable/dataTable";
import "./doctors.scss";
import { doctorsData } from "../../data/dados";
import { Add } from "../../components/add/Add"; 
import { Edit } from "../../components/edit/Edit";

// Definindo as colunas para a tabela
const columns = [
  {
    field: 'nome',
    headerName: 'Nome',
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
    field: 'endereco',
    headerName: 'Endereço',
    type: 'string',
    width: 200
  },
  {
    field: 'cro',
    headerName: 'CRO',
    type: 'string',
    width: 200
  },
];

const Doctors = () => {

  // Definindo os estados usando o Hook do React
  const [open, setOpen] = useState(false);
  const [editDoctor, setEditDoctor] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [initialDoctorsData, setInitialDoctorsData] = useState(doctorsData);

  // Função para abrir o modal de editar
  const handleEditClick = (doctor) => {
    setIsEditing(true);
    setEditDoctor(doctor);
    setOpen(true);
  };

  // Função para abrir o modal de adicionar
  const handleAddClick = () => {
    setIsEditing(false); 
    setOpen(true);
  };

  // Função para salvar as informações do médico
  const handleSaveDoctor = (data) => {
    if (isEditing) {
      // Lógica para atualizar a lista de doutores
      const updatedDoctors = initialDoctorsData.map(doctor => {
        if (doctor.id === editDoctor.id) {
          return {
            ...doctor,
            ...data
          };
        }
        return doctor;
      });

      setInitialDoctorsData(updatedDoctors);
    } else {
      // Lógica para adicionar um novo doutor
      const newDoctor = {
        id: Math.random(),
        ...data
      };
      const updatedDoctors = [...initialDoctorsData, newDoctor];
      setInitialDoctorsData(updatedDoctors);
    }
    setOpen(false);
  };

  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="Doutores" subtitle="Veja todos os nossos doutores" />
      </Box>

      {/* Componente da tabela */}
      <DataTable slug="doctor" columns={columns} rows={initialDoctorsData} onEditClick={handleEditClick} />
      
      {/* Renderização condicional do modal de edição ou adição */}
      {open && isEditing ? (
        <Edit slug="doutor" columns={columns} setOpen={setOpen} onSave={handleSaveDoctor} isEditing={isEditing} doctor={editDoctor} />
      ) : open && !isEditing ? (
          <Add slug="doutor" columns={columns} setOpen={setOpen} onSave={handleSaveDoctor} />
      ) : null}
      
      <Box display="flex" justifyContent="flex-end">
        {/* Botão de adição */}
        <Fab onClick={handleAddClick} size="large" color="primary" aria-label="add" style={{ marginTop: '30px', marginRight: '20px'}}>
          <AddIcon />
        </Fab>
      </Box>
    </Box>
  );
};

export default Doctors;
