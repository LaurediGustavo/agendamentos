import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import "./dataTable.scss";


export const DataTable = (props) => {

  const handleEditClick = (id) => {
    const selectedDoctor = props.rows.find(row => row.id === id);
    props.onEditClick(selectedDoctor);
  };
  
  const handleDelete = (id) => {
      console.log(id + " deletado");
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
              <div className="delete" onClick={() => handleDelete(params.row.id)}>
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
      </div>
    );
};

