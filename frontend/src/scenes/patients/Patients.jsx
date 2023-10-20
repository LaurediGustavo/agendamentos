import { useState } from "react";
import "./patients.scss";
import { DataTable } from "../../components/dataTable/dataTable";
import { Box, Fab } from "@mui/material";
import { Add as AddIcon } from "@mui/icons-material";
import Header from "../../components/headers/Headers";
import { patientsData } from "../../data/dados";
import Action from "../../components/action/Action"; 

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
        <Header title="Pacientes" subtitle="Registre e gerencie seus pacientes." />
      </Box>

      <DataTable slug="patients" columns={columns} rows={initialPatientsData} onEditClick={handleEditClick} />
      
      {open && (
        <Action
          slug="paciente"
          columns={columns}
          setOpen={setOpen}
          onSave={handleSavePatient}
          isEditing={isEditing}
          initialData={isEditing ? editPatient : null}
        />
      )}
      
      <Box display="flex" justifyContent="flex-end">
        <Fab onClick={handleAddClick} size="large" color="primary" aria-label="adicionar pacientes" style={{ marginTop: '30px', marginRight: '20px', backgroundColor:"#3fbabf"}}>
          <AddIcon />
        </Fab>
      </Box>
    </Box>
  );
};

export default Patients;
