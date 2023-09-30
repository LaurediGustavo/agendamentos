import React, { useState, useEffect } from 'react';
import { format } from 'date-fns';
import { LocalizationProvider, TimePicker } from '@mui/x-date-pickers';
import CloseIcon from '@mui/icons-material/Close';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import './bookingForm.scss'
import api from '../../services/api';
import moment from 'moment';

import {
    Box,
    Typography,
    Modal,
    Select,
    MenuItem,
    Button,
    Checkbox,
    ListItemText,
    Input,
    TextField,
} from "@mui/material";
import { SignalCellularNull } from '@mui/icons-material';

export const BookingForm = ({ modalOpen, handleCloseModal, selectedEvent, calendarRef, selectedDate }) => {
    const [procedimentos, setProcedimentos] = useState();
    useEffect(() => {
      api
        .get("procedimento/consultar?tratamento")
        .then((response) => setProcedimentos(response.data))
        .catch((err) => {
          console.error("ops! ocorreu um erro" + err);
        });
    }, []);
    
    const [doutores, setDoutores] = useState([]);
    const getDoutores = async () => {
        try {
            const response = await api.post("/doutor/consultar/agendamento", {
                nome: "",
                procedimentos: procedure
            });
            setDoutores(response.data);
        } catch (error) {
            console.error("Ops! Ocorreu um erro: " + error);
        }
    };

    const [pacientes, setPacientes] = useState([]);
    useEffect(() => {
        api
          .get("/paciente/consultar/agendamento?nome")
          .then((response) => setPacientes(response.data))
          .catch((err) => {
            console.error("ops! ocorreu um erro" + err);
          });
    }, []);
    
    const [doctor, setDoctor] = useState('');
    const [procedure, setProcedure] = useState([]);
    const [patient, setPatient] = useState('');
    const [startTime, setStartTime] = useState(null);
    const [endTime, setEndTime] = useState(null);

    useEffect(() => {
        getDoutores();
    }, [procedure]);

    const handleAgendar = async (e) => {
        e.preventDefault();

        if (!validarFormulario()) {
            return;
        }

        const [year, month, day] = selectedDate.split('-').map(Number);
        const dataInicio = new Date(year, month - 1, day);
        const dataFinal = new Date(year, month - 1, day);

        dataInicio.setHours(startTime.getHours());
        dataInicio.setMinutes(startTime.getMinutes());

        dataFinal.setHours(endTime.getHours());
        dataFinal.setMinutes(endTime.getMinutes());

        const startTimeFormatada = format(dataInicio, 'yyyy-MM-dd HH:mm:ss');
        const endTimeFormatada = format(dataFinal, 'yyyy-MM-dd HH:mm:ss');

        try {
            const response = await api.post("/agendamento/cadastro", {
                status: null,
                dataHoraInicio: startTimeFormatada,
                dataHoraFim: endTimeFormatada,
                doutorId: doctor,
                pacienteId: patient,
                procedimentosIds: procedure
            });
    
            calendarRef.current.getApi().addEvent({
                title: pacientes.find((paciente) => paciente.id === patient)?.nome,
                start: dataInicio,
                end: dataFinal
            });
            
            setDoctor('');
            setProcedure([]);
            setPatient('');
            setStartTime(null);
            setEndTime(null);
            setErros({});
            handleCloseModal();
        } catch (error) {
            if (error.response && error.response.data) {
                tratarErrosDeIntegracao(error.response.data.errors);
            } else {
                console.log(error);
            }
        }
    }

    const [erros, setErros] = useState({ procedimento: '', doutor: '', paciente: '',
                                        horarioInicio: '', horarioFim: '' });
  
    const validarFormulario = () => {
      const novosErros = {};
  
      if (procedure.length === 0) {
        novosErros.procedimento = 'Deve ser informado';
      }
  
      if (!Number.isInteger(doctor) || doctor <= 0) {
        novosErros.doutor = 'Deve ser informado';
      }

      if (!Number.isInteger(patient) || patient <= 0) {
        novosErros.paciente = 'Deve ser informado';
      }

      if (!moment(startTime).isValid()) {
        novosErros.startTime = 'Campo inválido';
      }
  
      if (!moment(endTime).isValid()) {
        novosErros.endTime = 'Campo inválido';
      }
  
      if (moment(startTime).isAfter(endTime)) {
        novosErros.startTime = 'A data de início deve ser anterior à data de término';
      }
  
      setErros(novosErros);

      return Object.keys(novosErros).length === 0;
    };

    const tratarErrosDeIntegracao = (erros) => {
        const novosErros = {};

        erros.forEach((erro) => {
            novosErros.startTime = erro.message;
            console.log(erro)
        });

        setErros(novosErros);
    }

    return (
        <Modal open={modalOpen} onClose={handleCloseModal}>
            <Box className="md">
                <span className="close" onClick={() => handleCloseModal()}><CloseIcon /></span>
                {selectedEvent ? (
                  <>
                  <Typography variant="h6" sx={{ marginBottom: 2, color: '#333' }}>
                      Detalhes do Agendamento
                  </Typography>
                  <Typography variant="body1" sx={{ color: '#333' }}>
                      <strong>Título:</strong> {selectedEvent.title}
                  </Typography>
                  <Typography variant="body1" sx={{ color: '#333' }}>
                      <strong>Data e Hora:</strong> {selectedEvent.start.toLocaleString()}
                  </Typography>
          
                  {/* Adicione as informações personalizadas aqui */}
                  <Typography variant="body1" sx={{ color: '#333' }}>
                      <strong>Doutor:</strong> {selectedDetails.doctor}
                  </Typography>
                  <Typography variant="body1" sx={{ color: '#333' }}>
                      <strong>Procedimento:</strong> {selectedDetails.procedure.join(', ')}
                  </Typography>
                  <Typography variant="body1" sx={{ color: '#333' }}>
                      <strong>Paciente:</strong> {selectedDetails.patient}
                  </Typography>

              </>
                ) : (
                    <>
                        <Typography variant="h6" sx={{ marginBottom: 2, color: '#333' }}>
                            Agendar Horário
                        </Typography>
                        <form onSubmit={handleAgendar}>

                            <div className="item">
                                <div className="label">
                                    <label>Procedimento:</label>
                                    {erros.procedimento && <div className="error-message">{erros.procedimento}</div>}
                                </div>
                                <Select
                                    fullWidth
                                    value={procedure}
                                    onChange={(e) => setProcedure(e.target.value)}
                                    label="Procedimento"
                                    multiple
                                    sx={{ my: 2, color: '#333' }}
                                    input={<Input />}
                                    renderValue={(selected) => (
                                        selected.length === procedimentos?.length ? 'Todos' : `${selected.length} selecionados`
                                    )}
                                >
                                    {procedimentos?.map((proc) => (
                                        <MenuItem key={proc.id} value={proc.id}>
                                            <Checkbox checked={procedure.indexOf(proc.id) > -1} />
                                            <ListItemText primary={proc.tratamento} />
                                        </MenuItem>
                                    ))}
                                </Select>
                            </div>

                            <div className="item">
                                <div className="label">
                                    <label>Doutor:</label>
                                    {erros.doutor && <div className="error-message">{erros.doutor}</div>}
                                </div>
                                <Select
                                    fullWidth
                                    value={doctor}
                                    onChange={(e) => setDoctor(e.target.value)}
                                    label="Doutor"
                                    sx={{ my: 2, color: '#333' }}
                                >
                                    {doutores?.map((doctor) => (
                                        <MenuItem key={doctor.id} value={doctor.id}>
                                            {doctor.nome} {doctor.sobreNome}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </div>

                            <div className="item">
                                <div className="label">
                                    <label>Paciente:</label>
                                    {erros.paciente && <div className="error-message">{erros.paciente}</div>}
                                </div>
                                <Select
                                    fullWidth
                                    value={patient}
                                    onChange={(e) => setPatient(e.target.value)}
                                    label="Paciente"
                                    sx={{ my: 2, color: '#333' }}
                                >
                                    {pacientes?.map((pacient) => (
                                        <MenuItem key={pacient.id} value={pacient.id}>
                                            {pacient.nome} - {pacient.cpf}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </div>
                            
                            <div className="item2">
                                <div className="label label-data">
                                    <label>Horário de Início:</label>
                                    {erros.startTime && <div className="error-message">{erros.startTime}</div>}
                                </div>
                                <LocalizationProvider dateAdapter={AdapterDateFns} >
                                    <TimePicker
                                        label="Horário de Início"
                                        value={startTime}
                                        onChange={(newTime) => setStartTime(newTime)}
                                        renderInput={(params) => (
                                            <TextField
                                                {...params}
                                                variant="outlined"
                                                fullWidth
                                                sx={{ my: 2 }}
                                                InputLabelProps={{ shrink: true, color: '#333' }}
                                                label="Horário de Início"
                                            />
                                        )}
                                    />
                                </LocalizationProvider>
                            </div>

                            <div className="item2">
                                <div className="label label-data">
                                    <label>Horário de Término:</label>
                                    {erros.endTime && <div className="error-message">{erros.endTime}</div>}
                                </div>
                                <LocalizationProvider dateAdapter={AdapterDateFns} >
                                    <TimePicker
                                        label="Horário de Término"
                                        value={endTime}
                                        onChange={(newTime) => setEndTime(newTime)}
                                        renderInput={(params) => (
                                            <TextField
                                                {...params}
                                                variant="outlined"
                                                fullWidth
                                                sx={{ my: 2 }}
                                                InputLabelProps={{ shrink: true, color: '#333' }}
                                                label="Horário de Término"
                                            />
                                        )}
                                    />
                                </LocalizationProvider>
                            </div>
                            
                            <Button className="btn" variant="contained" color="primary" type="submit">Cadastrar</Button>
                        </form>
                    </>
                )}
            </Box>
        </Modal>
    );
};
