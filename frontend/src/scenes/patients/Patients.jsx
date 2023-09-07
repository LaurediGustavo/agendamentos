import { useState } from "react";
import "./patients.scss";
import { DataTable } from "../../components/dataTable/dataTable";
import { Add } from "../../components/add/Add";
import { Box, Fab } from "@mui/material";
import { Add as AddIcon } from "@mui/icons-material";
import Header from "../../components/headers/Headers";
import { patientsData } from "../../data/dados";
import { Edit } from "../../components/edit/Edit";

const columns = [
  {
    field: 'nome',
    headerName: 'Nome',
    width: 200,
    type: 'string'
  },
  {
    field: 'idade',
    headerName: 'Idade',
    width: 150,
    type: 'number'
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
    field: 'informacoesAdicionais',
    headerName: 'Informações Adicionais',
    type: 'string',
    width: 300
  },
];

const Patients = () => {
  const [open, setOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editPatient, setEditPatient] = useState(null);
  const [initialPatientsData, setInitialPatientsData] = useState(patientsData);

  const handleEditClick = (patient) => {
    setIsEditing(true);
    setEditPatient(patient);
    setOpen(true);
  };

  const handleAddClick = () => {
    setIsEditing(false); 
    setOpen(true);
  };

  const handleSavePatient = (data) => {
    if (isEditing) {
      const updatedPatients = initialPatientsData.map(patient => {
        if (patient.id === editPatient.id) {
          return {
            ...patient,
            ...data
          };
        }
        return patient;
      });

      setInitialPatientsData(updatedPatients);
    } else {
      const newPatient = {
        id: Math.random(),
        ...data
      };
      const updatedPatients = [...initialPatientsData, newPatient];
      setInitialPatientsData(updatedPatients);
    }
    setOpen(false);
  };

  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="Pacientes" subtitle="Veja todos os nossos pacientes" />
      </Box>

      <DataTable slug="patients" columns={columns} rows={initialPatientsData} onEditClick={handleEditClick} />
      
      {open && isEditing ? (
        <Edit slug="paciente" columns={columns} setOpen={setOpen} onSave={handleSavePatient} isEditing={isEditing} doctor={editPatient} />
      ) : open && !isEditing ? (
        <Add slug="paciente" columns={columns} setOpen={setOpen} onSave={handleSavePatient} />
      ) : null}
      
      <Box display="flex" justifyContent="flex-end">
        <Fab onClick={handleAddClick} size="large" color="primary" aria-label="add" style={{ marginTop: '30px', marginRight: '20px'}}>
          <AddIcon />
        </Fab>
      </Box>
    </Box>
  );
};

export default Patients;
