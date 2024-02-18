import React, { useState, useEffect } from "react";
import "./procedures.scss";
import { Box, Fab } from "@mui/material";
import { Add as AddIcon } from "@mui/icons-material";
import Header from "../../components/headers/Headers";
import { DataTable } from "../../components/dataTable/dataTable";
import Action from "../../components/action/Action";
import api from '../../services/api';

const columns = [
  {
    field: 'tratamento',
    headerName: 'Procedimento',
    width: 200,
    type: 'string'
  },
  {
    field: 'valor',
    headerName: 'Valor',
    width: 200,
    type: 'number'
  },
  {
    field: 'tempo',
    headerName: 'Tempo',
    width: 200,
    type: 'string'
  },
];

const Procedures = () => {
  const [open, setOpen] = useState(false);
  const [editProcedure, setEditProcedure] = useState(null); 
  const [isEditing, setIsEditing] = useState(false);
  const [initialProceduresData, setInitialProceduresData] = useState([]);

  const getConsultas = async () => {
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
    getConsultas();
  }, []);

  const handleEditClick = (procedure) => {
    setIsEditing(true);
    setEditProcedure(procedure);
    setOpen(true);
  };

  const handleAddClick = () => {
    setIsEditing(false);
    setOpen(true); // Definir open como true ao clicar no botão de adicionar
  };

  const handleSaveProcedure = async (data) => {
    if (isEditing) {
      const updatedProcedures = initialProceduresData.map(procedure => {
        if (procedure.id === editProcedure.id) {
          const updatedProcedures = {
            ...procedure,
            ...data
          };

          atualizar(updatedProcedures);

          return updatedProcedures;
        }
        return procedure;
      });

      setInitialProceduresData(updatedProcedures);
    } else {
      const newProcedure = {
        ...data
      };

      const id = await gravar(newProcedure);
      newProcedure.id = id;

      const updatedProcedures = [...initialProceduresData, newProcedure];
      setInitialProceduresData(updatedProcedures);
    }
    setOpen(false);
  };

  const gravar = async (procedimento) => {
    try {
      const response = await api.post("/procedimento/cadastro", {
        tratamento: procedimento.tratamento,
        valor: parseFloat(procedimento.valor),
        tempo: procedimento.tempo,
        desabilitado: false,
      });
      return response.data.id;
    } catch (error) {
      throw new Error("Erro ao gravar procedimento: " + error.message);
    }
  };

  const atualizar = async (procedimento) => {
    try {
      await api.put("/procedimento/atualizar", {
        id: procedimento.id,
        tratamento: procedimento.tratamento,
        valor: parseFloat(procedimento.valor),
        tempo: procedimento.tempo,
        desabilitado: false,
      });
    } catch (error) {
      throw new Error("Erro ao atualizar procedimento: " + error);
    }
  };

  const handleDeleteClick = (procedure) => {
    remove(procedure)

    const updatedProcedures = initialProceduresData.filter(p => p.id !== procedure.id);
    setInitialProceduresData(updatedProcedures);
  };

  const remove = async (procedure) => {
    try {
      await api.delete("/procedimento/delete/" + procedure.id);
    } catch (error) {
      throw new Error("Erro ao atualizar procedimento: " + error);
    }
  };

  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="Procedimentos" subtitle="Registre e gerencie seus procedimentos." />
      </Box>
      <DataTable slug="procedures" columns={columns} rows={initialProceduresData} onEditClick={handleEditClick} onDeleteClick={handleDeleteClick} />
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
