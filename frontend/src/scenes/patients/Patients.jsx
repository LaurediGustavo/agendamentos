import { useState, useEffect } from "react";
import { Box, Fab } from "@mui/material";
import { Add as AddIcon } from "@mui/icons-material";
import Header from "../../components/headers/Headers";
import { DataTable } from "../../components/dataTable/dataTable";
import Action from "../../components/action/Action";
import api from '../../services/api';
import { formatarData_yyyy_MM_dd, formatarData_dd_MM_yyyy } from '../../services/dateFormat';

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
    field: 'cep',
    headerName: 'CEP',
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
  {
    field: 'informacoesAdicionais',
    headerName: 'Informações Adicionais',
    type: 'string',
    width: 300
  },
  {
    field: 'responsavelLegal',
    headerName: 'Responsável Legal',
    type: 'boolean',
    width: 200
  },
  {
    field: 'relacaoResponsavel',
    headerName: 'Relação com o Paciente',
    type: 'string',
    width: 200
  },
  {
    field: 'responsavel',
    headerName: 'Responsável',
    type: 'string',
    width: 200,
    renderedList: false
  },
  {
    field: 'responsavelNome',
    headerName: 'Responsável',
    type: 'string',
    width: 200,
    renderedForm: false
  },
  {
    field: 'telefoneResponsavel',
    headerName: 'Telefone do Responsável',
    type: 'string',
    width: 200
  },
];

const Patients = () => {
  const [open, setOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editPatient, setEditPatient] = useState(null);
  const [initialPatientsData, setInitialPatientsData] = useState([]);
  const [removedItems, setRemovedItems] = useState([]);
  const [showDeleted, setShowDeleted] = useState(false);


  const getPacientes = async () => {
    try {
      const response = await api.get("/paciente/consultar?cpf=&nome=");
      const pacientes = response.data.map((paciente) => ({
        id: paciente.id,
        nome: paciente.nome,
        sobrenome: paciente.sobrenome,
        dataDeNascimento: formatarData_dd_MM_yyyy(paciente.dataDeNascimento),
        cpf: mascaraCpf(paciente.cpf),
        genero: paciente.genero,
        telefone: paciente.telefone,
        informacoesAdicionais: paciente.informacoesAdicionais,
        cep: paciente.cep,
        logradouro: paciente.logradouro,
        bairro: paciente.bairro,
        numero: paciente.numero,
        bloco: paciente.bloco,
        responsavelLegal: paciente.responsavel !== null,
        relacaoResponsavel: paciente.parentesco,
        responsavel: paciente.responsavel !== null ? paciente.responsavel.id : null,
        responsavelNome: paciente.responsavel !== null ? paciente.responsavel.nome + " - " + mascaraCpf(paciente.responsavel.cpf) : null,
        telefoneResponsavel: paciente.responsavel !== null ? paciente.responsavel.telefone : null,
      }));
      setInitialPatientsData(pacientes);
    } catch (error) {
      console.error("Ops! Ocorreu um erro: " + error);
    }
  };

  const handleRemovedItensClick = async () => {
    try {
      const response = await api.get("/paciente/consultar?cpf=&nome=&desabilitado=true");
      const pacientes = response.data.map((paciente) => ({
        id: paciente.id,
        nome: paciente.nome,
        sobrenome: paciente.sobrenome,
        dataDeNascimento: formatarData_dd_MM_yyyy(paciente.dataDeNascimento),
        cpf: mascaraCpf(paciente.cpf),
        genero: paciente.genero,
        telefone: paciente.telefone,
        informacoesAdicionais: paciente.informacoesAdicionais,
        cep: paciente.cep,
        logradouro: paciente.logradouro,
        bairro: paciente.bairro,
        numero: paciente.numero,
        bloco: paciente.bloco,
        responsavelLegal: paciente.responsavel !== null,
        relacaoResponsavel: paciente.parentesco,
        responsavel: paciente.responsavel !== null ? paciente.responsavel.id : null,
        responsavelNome: paciente.responsavel !== null ? paciente.responsavel.nome + " - " + mascaraCpf(paciente.responsavel.cpf) : null,
        telefoneResponsavel: paciente.responsavel !== null ? paciente.responsavel.telefone : null,
      }));
      setRemovedItems(pacientes);
    } catch (error) {
      console.error("Ops! Ocorreu um erro: " + error);
    }
  };

  const mascaraCpf = (value) => {
    const cleanedCPF = value.replace(/[^\d]/g, '');
    return cleanedCPF.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  useEffect(() => {
    getPacientes();
  }, []);

  const handleEditClick = (patient) => {
    setIsEditing(true);
    setEditPatient(patient);
    setOpen(true);
  };

  const handleAddClick = () => {
    setIsEditing(false);
    setOpen(true);
  };

  const handleSavePatient = async (data) => {
    if (isEditing) {
      const updatedPatients = initialPatientsData.map(patient => {
        if (patient.id === editPatient.id) {
          const updatedPatient = {
            ...patient,
            ...data
          };

          atualizar(updatedPatient);

          return updatedPatient;
        }
        return patient;
      });

      setInitialPatientsData(updatedPatients);
    } else {
      const newPatient = {
        ...data
      };

      const id = await gravar(newPatient);
      newPatient.id = id;

      const updatedPatients = [...initialPatientsData, newPatient];
      setInitialPatientsData(updatedPatients);
    }
    setOpen(false);
  };

  const gravar = async (paciente) => {
    try {
      const response = await api.post("/paciente/cadastro", {
        nome: paciente.nome,
        sobrenome: paciente.sobrenome,
        dataDeNascimento: formatarData_yyyy_MM_dd(paciente.dataDeNascimento),
        cpf: paciente.cpf,
        genero: paciente.genero,
        telefone: paciente.telefone,
        informacoesAdicionais: paciente.informacoesAdicionais,
        cep: paciente.cep,
        logradouro: paciente.logradouro,
        bairro: paciente.bairro,
        numero: paciente.numero,
        bloco: paciente.bloco,
        responsavel_paciente_id: paciente.responsavel,
        parentesco: paciente.relacaoResponsavel,
      });
      return response.data.id;
    } catch (error) {
      throw new Error("Erro ao gravar paciente: " + error.message);
    }
  };

  const atualizar = async (paciente) => {
    try {
      await api.put("/paciente/atualizar", {
        id: paciente.id,
        nome: paciente.nome,
        sobrenome: paciente.sobrenome,
        dataDeNascimento: formatarData_yyyy_MM_dd(paciente.dataDeNascimento),
        cpf: paciente.cpf,
        genero: paciente.genero,
        telefone: paciente.telefone,
        informacoesAdicionais: paciente.informacoesAdicionais,
        cep: paciente.cep,
        logradouro: paciente.logradouro,
        bairro: paciente.bairro,
        numero: paciente.numero,
        bloco: paciente.bloco,
        responsavel_paciente_id: paciente.responsavel,
        parentesco: paciente.relacaoResponsavel,
      });
    } catch (error) {
      throw new Error("Erro ao atualizar paciente: " + error);
    }
  };

  const handleDeleteClick = (patient) => {
    setRemovedItems([...removedItems, patient]);
    const updatedPatients = initialPatientsData.filter(p => p.id !== patient.id);
    setInitialPatientsData(updatedPatients);
    remove(patient);
  };

  const handleReturnClick = (patient) => {
    const updatedRemovedItems = removedItems.filter(item => item.id !== patient.id);
    setRemovedItems(updatedRemovedItems);
    setInitialPatientsData([...initialPatientsData, patient]);
    returnPatient(patient);
  };

  const remove = async (patient) => {
    try {
      await api.delete("/paciente/delete/" + patient.id);
    } catch (error) {
      throw new Error("Erro ao remover paciente: " + error);
    }
  };

  const returnPatient = async (patient) => {
    // Lógica para retornar um paciente excluído
    try {
      await api.post("/paciente/revertdelete/" + patient.id);
    } catch (error) {
      console.error("Erro ao retornar paciente: " + error);
    }
  };

  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="Pacientes" subtitle="Registre e gerencie seus pacientes." />
      </Box>
      <DataTable
        columns={columns}
        rows={showDeleted ? [...initialPatientsData, ...removedItems] : initialPatientsData}
        deletedRows={removedItems}
        onEditClick={handleEditClick}
        onDeleteClick={handleDeleteClick}
        onReturnClick={handleReturnClick}
        onShowRemovedClick={handleRemovedItensClick}
      />
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

export default Patients;
