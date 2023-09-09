import React, { useState } from 'react';
import { doctorsData, patientsData, proceduresData } from '../../data/dados';
import { LocalizationProvider, TimePicker } from '@mui/x-date-pickers';
import CloseIcon from '@mui/icons-material/Close';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import './bookingForm.scss'

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

export const BookingForm = ({ modalOpen, handleCloseModal, selectedEvent, calendarRef, selectedDate }) => {
    const [doctor, setDoctor] = useState('');
    const [procedure, setProcedure] = useState([]);
    const [patient, setPatient] = useState('');
    const [selectedTime, setSelectedTime] = useState(null);
    const [selectedDetails, setSelectedDetails] = useState(null);

    const handleAgendar = () => {
        console.log("Agendado:", { doctor, procedure, patient, selectedTime, selectedDate });

        if (!selectedDate || !selectedTime) {
            console.error("Data e/ou horário não selecionados.");
            return;
        }

        const [year, month, day] = selectedDate.split('-').map(Number);
        const dataAgendamento = new Date(year, month - 1, day);
        const informa = patient.nome

        dataAgendamento.setHours(selectedTime.getHours());
        dataAgendamento.setMinutes(selectedTime.getMinutes());

        calendarRef.current.getApi().addEvent({
            title: 'Agendado',
            start: dataAgendamento,
        });

        setSelectedDetails({ doctor, procedure, patient, selectedTime, selectedDate });

        // Resetar os estados do formulário
        setDoctor('');
        setProcedure([]);
        setPatient('');
        setSelectedTime(null);

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
                                <label>Doutor:</label>
                                <Select
                                    fullWidth
                                    value={doctor}
                                    onChange={(e) => setDoctor(e.target.value)}
                                    label="Doutor"
                                    sx={{ my: 2, color: '#333' }}
                                >
                                    {doctorsData.map((doctor) => (
                                        <MenuItem key={doctor.id} value={doctor.nome}>
                                            {doctor.nome}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </div>

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
                                        selected.length === proceduresData.length ? 'Todos' : `${selected.length} selecionados`
                                    )}
                                >
                                    {proceduresData.map((proc) => (
                                        <MenuItem key={proc.id} value={proc.procedimento}>
                                            <Checkbox checked={procedure.indexOf(proc.procedimento) > -1} />
                                            <ListItemText primary={proc.procedimento} />
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
                                    {patientsData.map((patient) => (
                                        <MenuItem key={patient.id} value={patient.nome}>
                                            {patient.nome}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </div>
                            <div className="item2">
                                <label>Horário:</label>
                            </div>
                            <LocalizationProvider dateAdapter={AdapterDateFns} >
                                <TimePicker
                                    label="Horário"
                                    textField={(params) => (
                                        <TextField
                                            {...params}
                                            variant="outlined"
                                            fullWidth
                                            sx={{ my: 2 }}
                                            InputLabelProps={{ shrink: true, color: '#333' }}
                                            label="Horário"
                                        />
                                    )}
                                    value={selectedTime}
                                    onChange={(newTime) => setSelectedTime(newTime)}
                                />
                            </LocalizationProvider>

                            <Button className="btn" variant="contained" color="primary" type="submit">Cadastrar</Button>
                        </form>
                    </>
                )}
            </Box>
        </Modal>
    );
};
