import React, { useState } from "react";
import { Box, Fab } from "@mui/material";
import { Add as AddIcon } from "@mui/icons-material"; 
import Header from "../../components/headers/Headers";
import { DataTable } from "../../components/dataTable/dataTable";
import "./doctors.scss";
import { doctorsData } from "../../data/dados";
import Action from "../../components/action/Action"; 
import { proceduresData } from "../../data/dados"; // Importando os dados dos procedimentos


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
    field: 'estado',
    headerName: 'Estado',
    type: 'string',
    width: 200
  },
  {
    field: 'cidade',
    headerName: 'Cidade',
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
  const [initialDoctorsData, setInitialDoctorsData] = useState(doctorsData);

  const handleEditClick = (doctor) => {
    setIsEditing(true);
    setEditDoctor(doctor);
    setOpen(true);
  };

  const handleAddClick = () => {
    setIsEditing(false); 
    setOpen(true);
  };

  const handleSaveDoctor = (data) => {
    if (isEditing) {
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
        <Header title="Doutores" subtitle="Registre e gerencie seus doutores." />
      </Box>

      <DataTable slug="doctor" columns={columns} rows={initialDoctorsData} onEditClick={handleEditClick} />
      
      {open && (
        <Action
          slug="doutor"
          columns={columns}
          setOpen={setOpen}
          onSave={handleSaveDoctor}
          isEditing={isEditing}
          initialData={isEditing ? editDoctor : null}
          procedures={proceduresData} // Passando os procedimentos como propriedade
        />
      )}
      
      <Box display="flex" justifyContent="flex-end">
        <Fab onClick={handleAddClick} size="large" color="primary" aria-label="adicionar doutores" style={{ marginTop: '30px', marginRight: '20px', backgroundColor:"#3fbabf"}}>
          <AddIcon />
        </Fab>
      </Box>
    </Box>
  );
};

export default Doctors;
