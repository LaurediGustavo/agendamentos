import { useState } from "react";
import "./procedures.scss";
import { Box, Fab } from "@mui/material";
import { Add as AddIcon } from "@mui/icons-material";
import Header from "../../components/headers/Headers";
import { DataTable } from "../../components/dataTable/dataTable";
import { proceduresData } from "../../data/dados";
import Action from "../../components/action/Action";


const columns = [
  {
    field: 'procedimento',
    headerName: 'Procedimento',
    width: 200,
    type: 'string'
  },
];

const Procedures = () => {
  const [open, setOpen] = useState(false);
  const [editProcedure, setEditProcedure] = useState(null); 
  const [isEditing, setIsEditing] = useState(false);
  const [initialProceduresData, setInitialProceduresData] = useState(proceduresData);
  

  const handleEditClick = (procedure) => {
    setIsEditing(true);
    setEditProcedure(procedure);
    setOpen(true);
  };

  const handleAddClick = () => {
    setIsEditing(false);
    setOpen(true);
  };

  const handleSaveProcedure = (data) => {
    if (isEditing) {
      const updatedProcedures = initialProceduresData.map(procedure => {
        if (procedure.id === editProcedure.id) {
          return {
            ...procedure,
            ...data
          };
        }
        return procedure;
      });

      setInitialProceduresData(updatedProcedures);
    } else {
      const newProcedure = {
        id: Math.random(),
        ...data
      };
      const updatedProcedures = [...initialProceduresData, newProcedure];
      setInitialProceduresData(updatedProcedures);
    }
    setOpen(false);
  };

  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="Procedimentos" subtitle="Registre e gerencie seus procedimentos." />
      </Box>
      <DataTable slug="procedures" columns={columns} rows={initialProceduresData} onEditClick={handleEditClick} />
      {open && (
        <Action
          slug="procedimento"
          columns={columns}
          setOpen={setOpen}
          onSave={handleSaveProcedure}
          isEditing={isEditing}
          initialData={isEditing ? editProcedure : null}
        />
      )}
      <Box display="flex" justifyContent="flex-end">
      <Fab onClick={handleAddClick} size="large" color="primary" aria-label="adicionar procedimento" style={{ marginTop: '30px', marginRight: '20px', backgroundColor:"#3fbabf"}}>
        <AddIcon />
      </Fab>
      </Box>
    </Box>
  );
};

export default Procedures;





