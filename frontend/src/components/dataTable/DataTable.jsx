import { useState } from 'react';
import { DataGrid, GridToolbarContainer, GridToolbarExport, GridToolbarFilterButton, GridToolbarQuickFilter } from "@mui/x-data-grid";
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import { Link } from 'react-router-dom';
import "./dataTable.scss";

export const DataTable = (props) => {
  const [confirmationOpen, setConfirmationOpen] = useState(false);
  const [selectedRowIndexToDelete, setSelectedRowIndexToDelete] = useState(null);
  const [showDeleted, setShowDeleted] = useState(false);
  const [returnConfirmationOpen, setReturnConfirmationOpen] = useState(false);
  const [selectedRowToReturn, setSelectedRowToReturn] = useState(null);
  const { currentUser } = props;

  const handleEditClick = (id) => {
    const selectedDoctor = props.rows.find(row => row.id === id);
    if (!selectedDoctor.showDeleted) {
      props.onEditClick(selectedDoctor);
    }
  };

  const handleDeleteClick = (id) => {
    const selectedRowIndex = props.rows.findIndex(row => row.id === id);
    setSelectedRowIndexToDelete(selectedRowIndex);
    setConfirmationOpen(true);
  };

  const handleReturnClick = (id) => {
    const selectedRow = props.deletedRows.find(row => row.id === id);
    setSelectedRowToReturn(selectedRow);
    setReturnConfirmationOpen(true);
  };

  const handleConfirmDelete = () => {
    const selectedRow = props.rows[selectedRowIndexToDelete];
    props.onDeleteClick(selectedRow);
    setConfirmationOpen(false);
    setSelectedRowIndexToDelete(null);
  };

  const handleConfirmReturn = () => {
    props.onReturnClick(selectedRowToReturn);
    setReturnConfirmationOpen(false);
    setSelectedRowToReturn(null);
  };

  const handleCancelDelete = () => {
    setConfirmationOpen(false);
    setSelectedRowIndexToDelete(null);
  };

  const handleCancelReturn = () => {
    setReturnConfirmationOpen(false);
    setSelectedRowToReturn(null);
  };

  const handleShowRemovedClick = () => {
    props.onShowRemovedClick();
    setShowDeleted(!showDeleted)
  };

  function CustomToolbar() {
    return (
      <GridToolbarContainer>
        <div>
        <Button
            onClick={() => handleShowRemovedClick()}
            sx={{ mr: 5, fontSize: 13 }}
          >
            {showDeleted ? 'Ocultar Excluídos' : 'Mostrar excluídos'}
          </Button>
          <GridToolbarExport />
          
        </div>

        <GridToolbarQuickFilter />
      </GridToolbarContainer>
    );
  }

  const actionColumn = {
    field: "action",
    headerName: "Ação",
    width: 200,
    renderCell: (params) => {
      const isCurrentUser = currentUser && params.row.id === currentUser.id;
      return (
<div className="action">
        {!showDeleted && !isCurrentUser && (
          <div className="edit" onClick={() => handleEditClick(params.row.id)}>
            <img className="img" src="./edit.png" alt="Editar" />
          </div>
        )}
        {!showDeleted && !isCurrentUser && ( 
          <div className="delete" onClick={() => handleDeleteClick(params.row.id)}>
            <img className="img" src="./apagar.png" alt="Deletar" />
          </div>
        )}
        {showDeleted ? (
          <div className="return" onClick={() => handleReturnClick(params.row.id)}>
            <img className="img" src="./retornar.png" alt="Retornar" />
          </div>
        ) : null}
        {isCurrentUser && ( 
          <div className="user" onClick={() => handleUserClick(params.row.id)}>
            <Link to="/perfil" className="user">
            <img className="user-icon" src="./user.png" alt="Usuário" />
            </Link>
          </div>
        )}
      </div>
      );
    },
  };
  const colunas = props.columns.filter(coluna => coluna.renderedList !== false);

  return (
    <div className="dataTable" style={{ width: "100%", minWidth: "600px" }}>
      <DataGrid
        rows={showDeleted ? props.deletedRows : props.rows}
        columns={[actionColumn, ...colunas]}
        initialState={{
          pagination: {
            paginationModel: {
              pageSize: 10,
            },
          },
        }}
        slots={{ toolbar: CustomToolbar }}
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
        autoHeight  
        localeText={{
          noRowsLabel: 'Nenhum item encontrado.',
          toolbarExport: 'Exportar',
          toolbarExportLabel: 'Exportar',
          toolbarExportCSV: 'Baixar como CSV',
          columnMenuSortAsc: 'Ordenar crescente',
          columnMenuSortDesc: 'Ordenar decrescente',
          columnMenuUnsort: 'Remover ordenação',
        }}
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

      <Dialog open={returnConfirmationOpen} onClose={handleCancelReturn}>
        <DialogTitle>Confirmar retorno</DialogTitle>
        <DialogContent>
          Tem certeza de que deseja restaurar este item?
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCancelReturn}>Cancelar</Button>
          <Button onClick={handleConfirmReturn} autoFocus>Confirmar</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};
