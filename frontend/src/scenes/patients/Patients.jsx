import { useState, useEffect } from "react";
import { patientsData } from "../../data/dados";
import "./patients.scss";
import { DataTable } from "../../components/dataTable/dataTable";
import { Box, Fab } from "@mui/material";
import { Add as AddIcon } from "@mui/icons-material";
import Header from "../../components/headers/Headers";
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
    field: 'nomeResponsavel',
    headerName: 'Nome do Responsável',
    type: 'string',
    width: 200
  },
  {
    field: 'sobrenomeResponsavel',
    headerName: 'Sobrenome do Responsável',
    type: 'string',
    width: 200
  },
  {
    field: 'relacaoResponsavel',
    headerName: 'Relação com o Paciente',
    type: 'string',
    width: 200
  },
  {
    field: 'cpfResponsavel',
    headerName: 'CPF do Responsável',
    type: 'string',
    width: 200
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

  const getPacientes = async () => {
    try {
      const response = await api.get("/paciente/consultar?cpf=&nome=");
      const pacientes = response.data.map((paciente) => ({
        id: paciente.id,
        nome: paciente.nome,
        sobrenome: paciente.sobrenome,
        dataDeNascimento: formatarData_dd_MM_yyyy(paciente.dataDeNascimento),
        cpf: paciente.cpf,
        genero: paciente.genero,
        telefone: paciente.telefone,
        informacoesAdicionais: paciente.informacoesAdicionais,
        cep: paciente.cep,
        logradouro: paciente.logradouro,
        bairro: paciente.bairro,
        numero: paciente.numero,
        bloco: paciente.bloco,
        // Usando dados do mock para campos relacionados ao responsável APAGARRR
        responsavelLegal: patientsData.find(data => data.id === paciente.id)?.responsavelLegal || false,
        nomeResponsavel: patientsData.find(data => data.id === paciente.id)?.nomeResponsavel || '',
        sobrenomeResponsavel: patientsData.find(data => data.id === paciente.id)?.sobrenomeResponsavel || '',
        relacaoResponsavel: patientsData.find(data => data.id === paciente.id)?.relacaoResponsavel || '',
        cpfResponsavel: patientsData.find(data => data.id === paciente.id)?.cpfResponsavel || '',
        telefoneResponsavel: patientsData.find(data => data.id === paciente.id)?.telefoneResponsavel || '',
      }));
      setInitialPatientsData(pacientes);
    } catch (error) {
      console.error("Ops! Ocorreu um erro: " + error);
    }
  };

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
          const updatedPatients = {
            ...patient,
            ...data
          };

          atualizar(updatedPatients);

          return updatedPatients;
        }
        return patient;
      });

      setInitialPatientsData(updatedPatients);
    } else {
      const newPatient = {
        ...data
      };

      const id = await gravar(newPatient);
      newPatient.id = id

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
        responsavelLegal: paciente.responsavelLegal,
        nomeResponsavel: paciente.nomeResponsavel,
        sobrenomeResponsavel: paciente.sobrenomeResponsavel,
        relacaoResponsavel: paciente.relacaoResponsavel,
        cpfResponsavel: paciente.cpfResponsavel,
        telefoneResponsavel: paciente.telefoneResponsavel,
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
        responsavelLegal: paciente.responsavelLegal,
        nomeResponsavel: paciente.nomeResponsavel,
        sobrenomeResponsavel: paciente.sobrenomeResponsavel,
        relacaoResponsavel: paciente.relacaoResponsavel,
        cpfResponsavel: paciente.cpfResponsavel,
        telefoneResponsavel: paciente.telefoneResponsavel,
      });
    } catch (error) {
      throw new Error("Erro ao atualizar procedimento: " + error);
    }
  };

  const handleDeleteClick = (paciente) => {
    remove(paciente)

    const updatedPacientes = initialPatientsData.filter(p => p.id !== paciente.id);
    setInitialPatientsData(updatedPacientes);
  };

  const remove = async (paciente) => {
    try {
      await api.delete("/paciente/delete/" + paciente.id);
    } catch (error) {
      throw new Error("Erro ao atualizar paciente: " + error);
    }
  };

  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="Pacientes" subtitle="Registre e gerencie seus pacientes." />
      </Box>

      <DataTable slug="patients" columns={columns} rows={initialPatientsData} onEditClick={handleEditClick} onDeleteClick={handleDeleteClick} />
      
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
