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
  const [removedItems, setRemovedItems] = useState([]);
  const [showDeleted, setShowDeleted] = useState(false);

  const getConsultas = async () => {
    try {
      const response = await api.get("/procedimento/consultar?tratamento=");
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

  const handleRemovedItensClick = async () => {
    try {
      const response = await api.get("/procedimento/consultar?tratamento=&desabilitado=true");
      const procedimentos = response.data.map((procedimento) => ({
        id: procedimento.id,
        tratamento: procedimento.tratamento,
        valor: procedimento.valor,
        tempo: procedimento.tempo,
      }));
      setRemovedItems(procedimentos);
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
    setOpen(true);
  };

  const handleSaveProcedure = async (data) => {
    if (isEditing) {
      const updatedProcedures = initialProceduresData.map(procedure => {
        if (procedure.id === editProcedure.id) {
          const updatedProcedure = {
            ...procedure,
            ...data
          };

          atualizar(updatedProcedure);

          return updatedProcedure;
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
    setRemovedItems([...removedItems, procedure]);
    const updatedProcedures = initialProceduresData.filter(p => p.id !== procedure.id);
    setInitialProceduresData(updatedProcedures);
    remove(procedure);
  };

  const handleReturnClick = (procedure) => {
    const updatedRemovedItems = removedItems.filter(item => item.id !== procedure.id);
    setRemovedItems(updatedRemovedItems);
    setInitialProceduresData([...initialProceduresData, procedure]);
    returnProcedure(procedure);
  };

  const remove = async (procedure) => {
    try {
      await api.delete("/procedimento/delete/" + procedure.id);
    } catch (error) {
      throw new Error("Erro ao remover procedimento: " + error);
    }
  };

  const returnProcedure = async (procedure) => {
    // Lógica para retornar um procedimento excluído
    try {
      await api.post("/procedimento/revertdelete/" + procedure.id);
    } catch (error) {
      console.error("Erro ao retornar procedimento: " + error);
    }
  };

  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="Procedimentos" subtitle="Registre e gerencie seus procedimentos." />
      </Box>
      <DataTable
        columns={columns}
        rows={showDeleted ? [...initialProceduresData, ...removedItems] : initialProceduresData}
        deletedRows={removedItems}
        onEditClick={handleEditClick}
        onDeleteClick={handleDeleteClick}
        onReturnClick={handleReturnClick}
        onShowRemovedClick={handleRemovedItensClick}
      />
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

export default Procedures;
