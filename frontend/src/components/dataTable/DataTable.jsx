import { useState } from 'react';
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import "./dataTable.scss";

export const DataTable = (props) => {
  const [confirmationOpen, setConfirmationOpen] = useState(false);
  const [selectedIdToDelete, setSelectedIdToDelete] = useState(null);

  const handleEditClick = (id) => {
    const selectedDoctor = props.rows.find(row => row.id === id);
    props.onEditClick(selectedDoctor);
  };
  
  const handleDeleteClick = (id) => {
    setSelectedIdToDelete(id);
    setConfirmationOpen(true);
  };

  const handleConfirmDelete = () => {
    const selectedDoctor = props.rows.find(row => row.id === selectedIdToDelete);
    props.onDeleteClick(selectedDoctor);
    setConfirmationOpen(false);
  };

  const handleCancelDelete = () => {
    setConfirmationOpen(false);
    setSelectedIdToDelete(null);
  };

  const actionColumn = {
    field: "action",
    headerName: "Ação",
    width: 200,
    renderCell: (params) => {
      return (
        <div className="action">
          <div className="edit" onClick={() => handleEditClick(params.row.id)}>
            <img className="img" src="./edit.png" alt="Editar" />
          </div>
          <div className="delete" onClick={() => handleDeleteClick(params.row.id)}>
            <img className="img" src="./apagar.png" alt="Deletar" />
          </div>
        </div>
      );
    },
  };

  return (
    <div className="dataTable" style={{ width: "100%", minWidth: "600px" }}>
      <DataGrid
        rows={props.rows}
        columns={[...props.columns, actionColumn]}
        initialState={{
          pagination: {
            paginationModel: {
              pageSize: 10,
            },
          },
        }}
        slots={{ toolbar: GridToolbar }}
        slotProps={{
          toolbar: {
            showQuickFilter: true,
            quickFilterProps: { debounceMs: 500 },
          },
        }}
        pageSizeOptions={[10]}
        disableRowSelectionOnClick
        disableColumnFilter
        disableDensitySelector
        disableColumnSelector
      />

      <Dialog open={confirmationOpen} onClose={handleCancelDelete}>
        <DialogTitle>Confirmar exclusão</DialogTitle>
        <DialogContent>
          Tem certeza de que deseja excluir este item?
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCancelDelete}>Cancelar</Button>
          <Button onClick={handleConfirmDelete} autoFocus>Confirmar</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};
