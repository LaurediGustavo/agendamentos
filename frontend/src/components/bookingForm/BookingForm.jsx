import React, { useState, useEffect } from 'react';
import { format } from 'date-fns';
import { LocalizationProvider, TimePicker } from '@mui/x-date-pickers';
import CloseIcon from '@mui/icons-material/Close';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import './bookingForm.scss'
import api from '../../services/api';

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
    const [selectedDetails, setSelectedDetails] = useState(null);

    useEffect(() => {
        getDoutores();
    }, [procedure]);

    const handleAgendar = () => {
        console.log("Agendado:", { doctor, procedure, patient, startTime, endTime, selectedDate });

        if (!selectedDate || !startTime || !endTime) {
            console.error("Data e/ou horário não selecionados.");
            return;
        }

        const [year, month, day] = selectedDate.split('-').map(Number);
        const dataInicio = new Date(year, month - 1, day);
        const dataFinal = new Date(year, month - 1, day);
        const informa = patient.nome

        dataInicio.setHours(startTime.getHours());
        dataInicio.setMinutes(startTime.getMinutes());

        dataFinal.setHours(endTime.getHours());
        dataFinal.setMinutes(endTime.getMinutes());

        calendarRef.current.getApi().addEvent({
            title: 'Agendado',
            start: dataInicio,
        });

        setSelectedDetails({ doctor, procedure, patient, dataInicio, dataFinal, selectedDate });

        const startTimeFormatada = format(dataInicio, 'yyyy-MM-dd HH:mm:ss');
        const endTimeFormatada = format(dataFinal, 'yyyy-MM-dd HH:mm:ss');
        api.post("/agendamento/cadastro", {
            status: null,
            dataHoraInicio: startTimeFormatada,
            dataHoraFim: endTimeFormatada,
            doutorId: doctor,
            pacienteId: patient,
            procedimentosIds: procedure
        }).then((response) => {
            console.log("Resposta do servidor:", response.data);
        })
        .catch((error) => {
            console.error("Erro na requisição:", error);
        });

        // Resetar os estados do formulário
        setDoctor('');
        setProcedure([]);
        setPatient('');
        setStartTime(null);
        setEndTime(null);

        handleCloseModal();
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
                                <label>Procedimento:</label>
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
                                <label>Doutor:</label>
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
                                <label>Paciente:</label>
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
                                <div className="label">
                                <label>Horário de Início:</label>
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
                            <div className="label">
                                <label>Horário de Término:</label>
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
