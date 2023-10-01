import React, { useState, useEffect, forwardRef, useImperativeHandle  } from 'react';
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

export const BookingForm = forwardRef(({ modalOpen, handleCloseModal, selectedEvent, selectedDate, calendarRef }, ref) => {
    const [consultaId, setConsultaId] = useState()
    const carregarDados = (id) => {
        setConsultaId(id)

        api.get("/agendamento/consultar/" + id)
            .then((response) => preencherForm(response.data))
            .catch((err) => {
            console.error("ops! ocorreu um erro" + err);
        });
    };

    const limparDados = () => {
        const formulario = {};
    
        formulario.status = '';
        formulario.dataHoraInicio = null;
        formulario.dataHoraFim = null;
        formulario.doutorId = '';
        formulario.pacienteId = '';
        formulario.procedimentosIds = [];
        formulario.valorTotal = '';
        formulario.tempoAproximado = '';
    
        setConsultaForm(formulario);
        setConsultaId('')
    }

    useImperativeHandle(ref, () => ({
        carregarDados,
        limparDados,
    }));

    const preencherForm = (data) => {
        const formulario = {};
    
        formulario.status = data.status;
        formulario.dataHoraInicio = moment(data.dataHoraInicio).toDate();

        const novaDataHoraFim = new Date(data.dataHoraFim);
        novaDataHoraFim.setMinutes(novaDataHoraFim.getMinutes() + 1);
        formulario.dataHoraFim = novaDataHoraFim;
        formulario.doutorId = data.doutorId;
        formulario.pacienteId = data.pacienteId;
        formulario.procedimentosIds = data.procedimentos.map(item => item.id);
        formulario.valorTotal = data.valorTotal;
        formulario.tempoAproximado = data.tempoAproximado;
    
        setConsultaForm(formulario);

        getDoutores();
    }

    const [procedimentos, setProcedimentos] = useState();
    const [pacientes, setPacientes] = useState([]);
    useEffect(() => {
      api
        .get("procedimento/consultar?tratamento")
        .then((response) => setProcedimentos(response.data))
        .catch((err) => {
          console.error("ops! ocorreu um erro" + err);
        });

       api
        .get("/paciente/consultar/agendamento?nome")
        .then((response) => setPacientes(response.data))
        .catch((err) => {
          console.error("ops! ocorreu um erro" + err);
        });
    }, []);
    
    const [doutores, setDoutores] = useState([]);
    const getDoutores = async () => {
        try {
            const response = await api.post("/doutor/consultar/agendamento", {
                nome: "",
                procedimentos: consultaForm.procedimentosIds
            });
            setDoutores(response.data);
        } catch (error) {
            console.error("Ops! Ocorreu um erro: " + error);
        }
    };
    
    const [consultaForm, setConsultaForm] = useState({ status: '', dataHoraInicio: null, dataHoraFim: null,
        doutorId: '', pacienteId: '', procedimentosIds: [],
        valorTotal: '', tempoAproximado: '' });

    const atualizarConsulta = (atributo, novoValor) => {
        setConsultaForm({
            ...consultaForm,
            [atributo]: novoValor
        });
    };

    useEffect(() => {
        getDoutores();
    }, [consultaForm.procedimentosIds]);

    const handleAgendar = async (e) => {
        e.preventDefault();

        if (!validarFormulario()) {
            return;
        }

        const [year, month, day] = selectedDate.split('-').map(Number);
        const dataInicio = new Date(year, month - 1, day);
        const dataFinal = new Date(year, month - 1, day);

        dataInicio.setHours(consultaForm.dataHoraInicio.getHours());
        dataInicio.setMinutes(consultaForm.dataHoraInicio.getMinutes());

        dataFinal.setHours(consultaForm.dataHoraFim.getHours());
        dataFinal.setMinutes(consultaForm.dataHoraFim.getMinutes() - 1);

        if(!selectedEvent) {
            gravar(dataInicio, dataFinal);
        }
        else {
            atualizar(dataInicio, dataFinal);

            const eventos = calendarRef.current.getApi().getEvents();
            const eventoExistente = eventos.find((evento) => evento.extendedProps.consulta_id === consultaId);
            
            if (eventoExistente) {
              eventoExistente.setStart(dataInicio);
              eventoExistente.setEnd(dataFinal);
            }
        }
    }

    const gravar = async (args1, args2) => {
        try {
            const startTimeFormatada = format(args1, 'yyyy-MM-dd HH:mm:ss');
            const endTimeFormatada = format(args2, 'yyyy-MM-dd HH:mm:ss');

            await api.post("/agendamento/cadastro", {
                status: null,
                dataHoraInicio: startTimeFormatada,
                dataHoraFim: endTimeFormatada,
                doutorId: consultaForm.doutorId,
                pacienteId: consultaForm.pacienteId,
                procedimentosIds: consultaForm.procedimentosIds
            });

            calendarRef.current.getApi().addEvent({
                title: pacientes.find((paciente) => paciente.id === consultaForm.pacienteId)?.nome,
                start: dataInicio,
                end: dataFinal
            });
            
            limparDados();
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

    const atualizar = async (args1, args2) => {
        try {
            const startTimeFormatada = format(args1, 'yyyy-MM-dd HH:mm:ss');
            const endTimeFormatada = format(args2, 'yyyy-MM-dd HH:mm:ss');

            await api.put("/agendamento/atualizar", {
                id: consultaId,
                status: consultaForm.status,
                dataHoraInicio: startTimeFormatada,
                dataHoraFim: endTimeFormatada,
                doutorId: consultaForm.doutorId,
                pacienteId: consultaForm.pacienteId,
                procedimentosIds: consultaForm.procedimentosIds
            });
            
            limparDados();
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
  
      if (consultaForm.procedimentosIds.length === 0) {
        novosErros.procedimento = 'Deve ser informado';
      }
  
      if (!Number.isInteger(consultaForm.doutorId) || consultaForm.doutorId <= 0) {
        novosErros.doutor = 'Deve ser informado';
      }

      if (!Number.isInteger(consultaForm.pacienteId) || consultaForm.pacienteId <= 0) {
        novosErros.paciente = 'Deve ser informado';
      }

      if (!moment(consultaForm.dataHoraInicio).isValid()) {
        novosErros.startTime = 'Campo inválido';
      }
  
      if (!moment(consultaForm.dataHoraFim).isValid()) {
        novosErros.endTime = 'Campo inválido';
      }
  
      if (moment(consultaForm.dataHoraInicio).isAfter(consultaForm.dataHoraFim)) {
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
                                value={consultaForm.procedimentosIds}
                                onChange={(e) => atualizarConsulta('procedimentosIds', e.target.value)}
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
                                        <Checkbox checked={consultaForm.procedimentosIds.indexOf(proc.id) > -1} />
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
                                value={consultaForm.doutorId}
                                onChange={(e) => atualizarConsulta('doutorId', e.target.value)}
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
                                value={consultaForm.pacienteId}
                                onChange={(e) => atualizarConsulta('pacienteId', e.target.value)}
                                label="Paciente"
                                disabled={Number.isInteger(consultaId) && consultaId > 0}
                                sx={{ my: 2, color: '#333' }}
                            >
                                {pacientes?.map((paciente) => (
                                    <MenuItem key={paciente.id} value={paciente.id}>
                                        {paciente.nome} - {paciente.cpf}
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
                                    value={consultaForm.dataHoraInicio}
                                    onChange={(newTime) => atualizarConsulta('dataHoraInicio', newTime)}
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
                                    value={consultaForm.dataHoraFim}
                                    onChange={(newTime) => atualizarConsulta('dataHoraFim', newTime)}
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
            </Box>
        </Modal>
    );
});
