import React, { useState, useEffect, forwardRef, useImperativeHandle } from 'react';
import { format } from 'date-fns';
import { LocalizationProvider, TimePicker, DatePicker } from '@mui/x-date-pickers';
import CloseIcon from '@mui/icons-material/Close';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import './bookingForm.scss'
import api from '../../services/api';
import moment from 'moment';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import {
    Box,
    Modal,
    Chip,
    Select,
    MenuItem,
    Button,
    ListItemText,
    Input,
    Tooltip,
    TextField,
    Autocomplete
} from "@mui/material";
import { formatarData_dd_MM_yyyy } from '../../services/dateFormat';

export const BookingForm = forwardRef(({ modalOpen, handleCloseModal, selectedEvent, selectedDate, calendarRef }, ref) => {

    const [novaData, setNovaData] = useState(null);
    const [novoHorarioInicio, setNovoHorarioInicio] = useState(null);
    const [novoHorarioTermino, setNovoHorarioTermino] = useState(null);
    const [situacaoOriginal, setSituacaoOriginal] = useState(null);
    const [consultaContinua, setConsultaContinua] = useState(false); //novo
    const [consultasEmAndamento, setConsultasEmAndamento] = useState([]); //novo
    const [consultaSelecionada, setConsultaSelecionada] = useState(null); //novo

    // Estado local do componente
    const [consultaId, setConsultaId] = useState()
    // Função para carregar dados de uma consulta específica
    const carregarDados = (id) => {
        setConsultaId(id)
        setErros({});

        // Chama a API para obter os detalhes da consulta
        api.get("/agendamento/consultar/" + id)
            .then((response) => preencherForm(response.data))
            .catch((err) => {
                console.error("ops! ocorreu um erro" + err);
            });
    };

    // Função para limpar os dados do formulário
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
        setErros({});

        buscarProcedimentos();
        buscarPacientes();
    }

    useImperativeHandle(ref, () => ({
        carregarDados,
        limparDados,
    }));

    const exibirBotaoGravar = (data) => {
        return data.status !== 'REMARCADO' || situacaoOriginal !== 'REMARCADO'
    }

    const disabilitarSituacao = (data) => {
        return data.status === 'REMARCADO'
    }

    const preencherForm = async (data) => {
        const formulario = {};

        formulario.status = data.status;
        formulario.dataHoraInicio = moment(data.dataHoraInicio).toDate();

        const novaDataHoraFim = new Date(data.dataHoraFim);
        novaDataHoraFim.setMinutes(novaDataHoraFim.getMinutes() + 1);
        formulario.dataHoraFim = novaDataHoraFim;
        formulario.doutorId = data.doutorId;

        formulario.pacienteId = data.pacienteId;
        await buscarPacientesSelecionado(formulario.pacienteId);

        formulario.procedimentosIds = data.procedimentos.map(item => item.id);
        await buscarProcedimentoSelecionado(formulario.procedimentosIds);

        formulario.valorTotal = formatarValor(data.valorTotal);
        formulario.tempoAproximado = data.tempoAproximado;
        setSituacaoOriginal(data.status);
        setConsultaContinua(data.consultaEstendidaDe? true : false)
        setConsultaSelecionada(data.consultaEstendidaDe);

        if (data.remercado) {
            setNovaData(moment(data.remercado.dataHoraInicio).toDate())
            setNovoHorarioInicio(moment(data.remercado.dataHoraInicio).toDate())
        }
        else {
            setNovaData()
            setNovoHorarioInicio()
        }

        setConsultaForm(formulario);
    }

    const [procedimentos, setProcedimentos] = useState();
    const [pacientes, setPacientes] = useState([]);
    useEffect(() => {
        buscarProcedimentos();
        buscarPacientes();
    }, []);

    const buscarProcedimentos = async () => {
        try {
            const response = await api.get("/procedimento/consultar?tratamento");
            setProcedimentos(response.data);

            return response.data;
        } catch (err) {
            console.error("ops! ocorreu um erro" + err);
            return null;
        }
    }

    const buscarPacientes = async () => {
        try {
            const response = await api.get("/paciente/consultar/agendamento?nome");
            setPacientes(response.data);

            return response.data;
        } catch (err) {
            console.error("ops! ocorreu um erro" + err);
            return null;
        }
    };

    const [doutores, setDoutores] = useState([]);
    const getDoutores = async () => {
        try {
            const response = await api.post("/doutor/consultar/agendamento", {
                nome: "",
                procedimentos: consultaForm.procedimentosIds
            });
            setDoutores(response.data);

            buscarDoutoresSelecionado(response.data);
        } catch (error) {
            console.error("Ops! Ocorreu um erro: " + error);
        }
    };

    const statusOpcoes = [
        { key: 'AGUARDANDO', label: 'Aguardando' },
        { key: 'ENVIADO', label: 'Enviado' },
        { key: 'CONFIRMADO', label: 'Confirmado' },
        { key: 'CANCELADO', label: 'Cancelado' },
        { key: 'REMARCADO', label: 'Remarcado' },
        { key: 'FINALIZADO', label: 'Finalizado' },
        { key: 'EM_ANDAMENTO', label: 'Em andamento' }
    ];

    const [erros, setErros] = useState({
        procedimento: '', doutor: '', paciente: '',
        horarioInicio: ''
    });

    const [consultaForm, setConsultaForm] = useState({
        status: '', dataHoraInicio: null, dataHoraFim: null,
        doutorId: '', pacienteId: '', procedimentosIds: [],
        valorTotal: '', tempoAproximado: ''
    });

    const atualizarConsulta = (atributo, novoValor) => {
        setConsultaForm({
            ...consultaForm,
            [atributo]: novoValor
        });

        if (atributo === 'pacienteId') {
            buscarConsultasEmAndamento(novoValor)
        }
    };

    useEffect(() => {
        const calcular = async () => {
            const tempoAproximado = calcularTempo();
            const valorTotal = calcularValor();

            setConsultaForm((prevConsultaForm) => ({
                ...prevConsultaForm,
                tempoAproximado,
                valorTotal,
            }));
        };

        getDoutores();
        calcular();
    }, [consultaForm.procedimentosIds]);


    const calcularTempo = () => {
        const procedimentosSelecionados = procedimentos?.filter((proc) =>
            consultaForm.procedimentosIds.includes(proc.id)
        );

        const somaDosTemposEmSegundos = procedimentosSelecionados?.reduce((total, proc) => {
            const tempoEmSegundos = parseFloat(proc.tempo) * 60;
            return isNaN(tempoEmSegundos) ? total : total + tempoEmSegundos;
        }, 0);

        const horas = Math.floor(somaDosTemposEmSegundos / 3600);
        const minutos = Math.floor((somaDosTemposEmSegundos % 3600) / 60);
        const segundos = somaDosTemposEmSegundos % 60;

        const tempoFormatado = `${horas.toString().padStart(2, '0')}:${minutos.toString().padStart(2, '0')}:${segundos.toString().padStart(2, '0')}`;

        return tempoFormatado;
    };

    const calcularHorarioTermino = (horarioInicio) => {
        if (!horarioInicio || !consultaForm.tempoAproximado) return null;

        const horaInicio = horarioInicio.getHours();
        const minutosInicio = horarioInicio.getMinutes();
        const tempoAproximado = consultaForm.tempoAproximado.split(':');
        const horas = parseInt(tempoAproximado[0]);
        const minutos = parseInt(tempoAproximado[1]);

        const novoHorarioTermino = new Date(horarioInicio);
        novoHorarioTermino.setHours(horaInicio + horas);
        novoHorarioTermino.setMinutes(minutosInicio + minutos);

        return novoHorarioTermino;
    };

    useEffect(() => {
        atualizarConsulta('dataHoraFim', calcularHorarioTermino(consultaForm.dataHoraInicio));
    }, [consultaForm.dataHoraInicio, consultaForm.tempoAproximado]);

    useEffect(() => {
        setNovoHorarioTermino(calcularHorarioTermino(novoHorarioInicio));
    }, [novoHorarioInicio, consultaForm.tempoAproximado]);


    const calcularValor = () => {
        const procedimentosSelecionados = procedimentos?.filter((proc) =>
            consultaForm.procedimentosIds.includes(proc.id)
        );

        const somaDosValores = procedimentosSelecionados?.reduce((total, proc) => {
            const valor = parseFloat(proc.valor);
            return isNaN(valor) ? total : total + valor;
        }, 0);

        return formatarValor(somaDosValores);
    };

    const formatarValor = (valor) => {
        const valorFormatado = valor?.toLocaleString('pt-BR', {
            style: 'currency',
            currency: 'BRL',
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
        });

        return valorFormatado;
    }

    const handleAgendar = async (e) => {
        e.preventDefault();

        if (!validarFormulario()) {
            return;
        }

        const [year, month, day] = selectedDate.split('-').map(Number);
        const dataInicio = new Date(year, month - 1, day);
        const dataFinal = new Date(year, month - 1, day);

        if (consultaForm.status === "REMARCADO" && exibirNovosCampos) {
            if (!novaData || !novoHorarioInicio || !novoHorarioTermino) {
                W
                console.error("Por favor, preencha todos os novos campos.");
                return;
            }

            // Usar os novos horários de início e término se estiver remarcando
            dataInicio.setDate(novaData.getDate());
            dataInicio.setHours(novoHorarioInicio.getHours());
            dataInicio.setMinutes(novoHorarioInicio.getMinutes());

            dataFinal.setDate(novaData.getDate());
            dataFinal.setHours(novoHorarioTermino.getHours());
            dataFinal.setMinutes(novoHorarioTermino.getMinutes() - 1);

            // Atualizar os valores no estado do formulário para refletir os novos horários
            atualizarConsulta('dataHoraInicio', dataInicio);
            atualizarConsulta('dataHoraFim', dataFinal);
        }
        else {
            dataInicio.setHours(consultaForm.dataHoraInicio.getHours());
            dataInicio.setMinutes(consultaForm.dataHoraInicio.getMinutes());

            dataFinal.setHours(consultaForm.dataHoraFim.getHours());
            dataFinal.setMinutes(consultaForm.dataHoraFim.getMinutes() - 1);
        }

        if (!selectedEvent) {
            gravar(dataInicio, dataFinal);
        } else {
            atualizar(dataInicio, dataFinal);

            const eventos = calendarRef.current.getApi().getEvents();
            const eventoExistente = eventos.find((evento) => evento.extendedProps.consulta_id === consultaId);

            if (eventoExistente) {
                eventoExistente.setStart(dataInicio);
                eventoExistente.setEnd(dataFinal);
            }
        }
    };

    const gravar = async (args1, args2) => {
        try {
            const startTimeFormatada = format(args1, 'yyyy-MM-dd HH:mm:ss');
            const endTimeFormatada = format(args2, 'yyyy-MM-dd HH:mm:ss');

            const data = await api.post("/agendamento/cadastro", {
                status: null,
                dataHoraInicio: startTimeFormatada,
                dataHoraFim: endTimeFormatada,
                doutorId: consultaForm.doutorId,
                pacienteId: consultaForm.pacienteId,
                procedimentosIds: consultaForm.procedimentosIds,
                consultaEstendidaDeId: consultaSelecionada?.id
            });

            calendarRef.current.getApi().addEvent({
                title: pacientes.find((paciente) => paciente.id === consultaForm.pacienteId)?.nome,
                start: args1,
                end: args2,
                consulta_id: data.data.id,
                classNames: eventClass(data.data.status),
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
                procedimentosIds: consultaForm.procedimentosIds,
                consultaEstendidaDeId: consultaSelecionada?.id
            });

            limparDados();
            setErros({});
            handleCloseModal();
            window.location.reload();
        } catch (error) {
            if (error.response && error.response.data) {
                tratarErrosDeIntegracao(error.response.data.errors);
            } else {
                console.log(error);
            }
        }
    }

    const eventClass = (status) => {
        switch (status) {
          case 'CANCELADO':
            return 'event-cancelado';
          case 'REMARCADO':
            return 'event-remarcado';
          case 'FINALIZADO':
            return 'event-finalizado';
          case 'AGUARDANDO':
            return 'event-aguardando';
          case 'ENVIADO':
            return 'event-enviado';
          case 'CONFIRMADO':
            return 'event-confirmado';
          case 'EM_ANDAMENTO': 
            return 'event-em_andamento';
          default:
            return 'event-default';
        }
    }

    const validarFormulario = () => {
        const novosErros = {};

        if (consultaForm.procedimentosIds.length === 0) {
            novosErros.procedimento = 'Deve ser informado';
        }

        if (!Number.isInteger(consultaForm.doutorId) || consultaForm.doutorId <= 0 || doutores.length === 0) {
            novosErros.doutor = 'Deve ser informado';
        }

        if (!Number.isInteger(consultaForm.pacienteId) || consultaForm.pacienteId <= 0) {
            novosErros.paciente = 'Deve ser informado';
        }

        if (!moment(consultaForm.dataHoraInicio).isValid()) {
            novosErros.startTime = 'Campo Horário de Inicio é inválido';
        }

        if (!moment(consultaForm.dataHoraFim).isValid()) {
            novosErros.startTime = 'Campo Horário de Término é inválido';
        }

        if (moment(consultaForm.dataHoraInicio).isAfter(consultaForm.dataHoraFim)) {
            novosErros.startTime = 'A data de início deve ser anterior à data de término';
        }

        if (consultaForm.status === "REMARCADO" && exibirNovosCampos) {
            if (!novaData) {
                novosErros.novaData = 'A nova data deve ser informada';
            }

            if (!novoHorarioInicio) {
                novosErros.novoHorario = 'O novo horário de início deve ser informado';
            }

            if (!novoHorarioTermino) {
                novosErros.novoHorario = 'O novo horário de término deve ser informado';
            }
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

    const formatarCPF = (cpf) => {
        if (!cpf || typeof cpf !== 'string') {
            return 'CPF Inválido';
        }

        return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    };

    const eventColor = (status) => {
        switch (status) {
            case 'CANCELADO':
                return 'rgba(235, 14, 14, 0.589)';
            case 'REMARCADO':
                return 'rgb(255, 136, 0)';
            case 'FINALIZADO':
                return 'rgba(0, 128, 0, 0.61)';
            case 'CONFIRMADO':
                return 'rgba(231, 235, 26, 0.582)'
            case 'ENVIADO':
                return 'rgba(208, 8, 248, 0.767)'
            case 'AGUARDANDO':
                return 'rgba(0, 191, 255, 0.637)'
            case 'EM_ANDAMENTO':
                return 'rgba(255, 182, 193, 0.7)'
        }
    }

    const handleDelete = (valueToDelete) => {
        atualizarConsulta('procedimentosIds', consultaForm.procedimentosIds.filter((value) => value !== valueToDelete));
    };

    const formatTime = (minutes) => {
        const hours = Math.floor(minutes / 60);
        const remainingMinutes = minutes % 60;
        return `${hours.toString().padStart(2, '0')}:${remainingMinutes.toString().padStart(2, '0')}`;
    };

    const [exibirNovosCampos, setExibirNovosCampos] = useState(false);

    useEffect(() => {
        if (consultaForm.status === "REMARCADO") {
            setExibirNovosCampos(true);
        } else {
            setExibirNovosCampos(false);
        }
    }, [consultaForm.status]);


    // Função para alternar o estado da opção "Consulta Contínua"
    const toggleConsultaContinua = () => {  //novo
        setConsultaContinua(!consultaContinua);
        buscarConsultasEmAndamento(consultaForm.pacienteId);
    };

    const buscarConsultasEmAndamento = async (id) => { //novo
        // Lógica para buscar as consultas em andamento 
        // definir o estado `consultasEmAndamento` com os dados retornados pela API

        try {
            const response = await api.get("/agendamento/consultarstatuspaciente?pacienteId=" + id + "&status=EM_ANDAMENTO");
            setConsultasEmAndamento(response.data); // Atualize o estado com os dados das consultas em andamento
        } catch (error) {
            console.error("Erro ao buscar consultas em andamento:", error);
        }
    };


    // Função para lidar com a seleção de uma consulta em andamento
    const handleSelecionarConsulta = (consulta) => {
        setConsultaSelecionada(consulta);
        console.log(consultaSelecionada)

        atualizarConsulta('procedimentosIds', consulta.procedimentos.map(p => p.id))
        // Aqui eu vou habilitar novamente o campo procedimento e preencher ele com os dados da consulta selecionada
    };

    const buscarProcedimentoSelecionado = async (ids) => {
        const procedimentosBusca = await buscarProcedimentos();

        const promessas = [];
    
        for (const id of ids) {
            const procedimento = procedimentosBusca?.find(proc => proc.id === id);
            
            if (!procedimento) {
                const fetchPromise = api.get("procedimento/consultar/" + id)
                    .then((response) => {
                        setProcedimentos(prevProcedimentos => [...prevProcedimentos, response.data]);
                    })
                    .catch((err) => {
                        console.error("Ops! Ocorreu um erro: " + err);
                    });
                promessas.push(fetchPromise);
            }
        }
    
        await Promise.all(promessas);
    }
    
    const buscarPacientesSelecionado = async (id) => {
        const pacientesBusca = await buscarPacientes();
        const paciente = pacientesBusca?.find(proc => proc.id === id);

        if (!paciente) {
            await api.get("paciente/consultar/" + id)
                .then((response) => {
                    setPacientes(pacientes => [...pacientes, response.data]);
                })
                .catch((err) => {
                    console.error("Ops! Ocorreu um erro: " + err);
                });
        }
    }

    const buscarDoutoresSelecionado = async (doutoresBusca) => {
        if (consultaForm.doutorId) {

            const doutor = doutoresBusca?.find(proc => proc.id === consultaForm.doutorId);
    
            if (!doutor) {
                await api.get("doutor/consultar/agendamento/" + consultaForm.doutorId)
                    .then((response) => {
                        setDoutores(doutores => [...doutores, response.data]);
                    })
                    .catch((err) => {
                        console.error("Ops! Ocorreu um erro: " + err);
                    });
            }
        }
    }

    return (
        <Modal open={modalOpen} onClose={handleCloseModal}>
            <Box className="md">
                <span className="close" onClick={() => handleCloseModal()}><CloseIcon /></span>
                <>
                    <h1>{selectedEvent ? 'Alterar agendamento' : 'Agendar consulta'}</h1>
                    <form onSubmit={handleAgendar}>
                        {consultaId > 0 && (
                            <div className="item">
                                <div className="label">
                                    <label>Situação:</label>
                                </div>
                                <Select
                                    disabled={disabilitarSituacao(consultaForm)}
                                    fullWidth
                                    value={consultaForm.status}
                                    onChange={(e) => atualizarConsulta('status', e.target.value)}
                                    label="Situação"
                                    sx={{ my: 2, color: '#333' }}
                                >
                                    {statusOpcoes.map((option) => (
                                        <MenuItem key={option.key} value={option.key}>
                                            <span
                                                style={{
                                                    display: 'inline-block',
                                                    height: '10px',
                                                    width: '10px',
                                                    borderRadius: '50%',
                                                    backgroundColor: eventColor(option.key),
                                                    marginRight: '10px'
                                                }}
                                            ></span>
                                            {option.label}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </div>
                        )}

                        <div className="item">
                            <div className="label">
                                <label>Paciente:</label>
                                {erros.paciente && <div className="error-message">{erros.paciente}</div>}
                            </div>
                            <Autocomplete
                                fullWidth
                                value={pacientes.find(paciente => paciente.id === consultaForm.pacienteId) || null}
                                onChange={(event, newValue) => {
                                    atualizarConsulta('pacienteId', newValue ? newValue.id : null);
                                }}
                                getOptionLabel={(option) => `${option.nome} - ${formatarCPF(option.cpf)}`}
                                disabled={Number.isInteger(consultaId) && consultaId > 0}
                                options={pacientes}
                                onFocus={(e) => e.stopPropagation()}
                                renderInput={(params) => <TextField {...params} sx={{ my: 2, color: '#333' }} />}
                            />
                        </div>

                        {!selectedEvent && (
                            <>
                                <div className="item">
                                    <div className="label">
                                        <FormControlLabel
                                            control={
                                                <Checkbox
                                                    checked={consultaContinua}
                                                    onChange={toggleConsultaContinua}
                                                    name="consultaContinua"
                                                    color="primary"
                                                />
                                            }
                                            label="Consulta Contínua"
                                            style={{ marginBottom: '20px' }}
                                        />
                                    </div>

                                    {consultaContinua && (
                                        <div className="item">
                                            <div className="label">
                                                <label>Consulta Estendida:</label>
                                            </div>
                                            <Select
                                                fullWidth
                                                value={consultaSelecionada}
                                                onChange={(e) => handleSelecionarConsulta(e.target.value)}
                                                label="Consulta Estendida"
                                                sx={{ my: 2, color: '#333' }}
                                            >{consultasEmAndamento.length === 0 ? (
                                                <MenuItem disabled >
                                                    Não há consultas estendidas disponíveis para este paciente
                                                </MenuItem>
                                            ) : (
                                                consultasEmAndamento.map((consulta) => (
                                                    <MenuItem key={consulta.id} value={consulta}>
                                                        {formatarData_dd_MM_yyyy(consulta.dataHoraInicio)} - {consulta.procedimentos[0].tratamento}
                                                    </MenuItem>
                                                ))
                                            )}
                                            </Select>
                                        </div>
                                    )}
                                </div>
                            </>
                        )}


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
                                    <div>
                                        {/* Mapeia e renderiza os procedimentos selecionados como chips */}
                                        {selected.map((value) => {
                                            const procedimento = procedimentos.find(proc => proc.id === value);
                                            // Verifica se o procedimento deve exibir o botão de remoção (x)
                                            const shouldShowDelete = !consultaContinua || !consultasEmAndamento.some(consulta => consulta.procedimentos.some(p => p.id === procedimento.id));

                                            return (
                                                <Tooltip
                                                    key={value}
                                                    title={`Valor: R$${procedimento.valor} - Tempo Estimado: ${formatTime(procedimento.tempo)}`}
                                                    placement="top"
                                                >
                                                    <Chip
                                                        label={procedimento.tratamento}
                                                        // Adiciona a função de exclusão se o botão deve ser exibido
                                                        onDelete={shouldShowDelete ? (event) => {
                                                            event.stopPropagation();
                                                            handleDelete(value);
                                                        } : undefined}
                                                        // Impede o clique se o procedimento estiver em consultasEmAndamento
                                                        onClick={consultasEmAndamento.some(consulta => consulta.procedimentos.some(p => p.id === procedimento.id)) ? undefined : () => handleProcedureClick(value)}
                                                        onMouseDown={(event) => {
                                                            event.stopPropagation();
                                                        }}
                                                        // Altera o cursor apenas se o procedimento for clicável
                                                        style={{
                                                            backgroundColor: '#2ab7bd80',
                                                            marginRight: '2px',
                                                            cursor: consultasEmAndamento.some(consulta => consulta.procedimentos.some(p => p.id === procedimento.id)) ? 'not-allowed' : (shouldShowDelete ? 'pointer' : 'default')
                                                        }}
                                                    />
                                                </Tooltip>
                                            );
                                        })}
                                    </div>
                                )}
                            >
                                {/* Mapeia e renderiza os procedimentos disponíveis como itens de menu */}
                                {procedimentos?.map((proc) => (
                                    <MenuItem key={proc.id} value={proc.id} disabled={consultasEmAndamento.some(consulta => consulta.procedimentos.some(p => p.id === proc.id))}>
                                        <Checkbox checked={consultaForm.procedimentosIds.indexOf(proc.id) > -1} />
                                        <ListItemText primary={proc.tratamento} />
                                    </MenuItem>
                                ))}
                            </Select>
                        </div>

                        {consultaForm.procedimentosIds && consultaForm.procedimentosIds.length > 0 && (
                            <div className="item itemContainer">
                                <div className="label">
                                    Valor Total: {consultaForm.valorTotal}
                                </div>
                                <div className="label">
                                    Tempo Aproximado: {consultaForm.tempoAproximado}
                                </div>
                            </div>
                        )}

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
                                {doutores?.length > 0 ? (
                                    doutores.map((doctor) => (
                                        <MenuItem key={doctor.id} value={doctor.id}>
                                            {doctor.nome} {doctor.sobreNome}
                                        </MenuItem>
                                    ))
                                ) : (
                                    <MenuItem disabled>Nenhum doutor disponível para os procedimentos selecionados.</MenuItem>
                                )}
                            </Select>
                        </div>

                        {consultaForm.status === "REMARCADO" && (
                            <div>
                                <div className="label label-data">
                                    <label>Nova Data:</label>
                                    {erros.novaData && <div className="error-message">{erros.novaData}</div>}
                                </div>
                                <div className="item-container-data">
                                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                                        <DatePicker
                                            value={novaData}
                                            onChange={(newDate) => setNovaData(newDate)}
                                            renderInput={(params) => (
                                                <TextField {...params} variant="outlined" fullWidth sx={{ my: 2 }} />
                                            )}
                                        />
                                    </LocalizationProvider>
                                </div>

                                <div>
                                    <div className="label label-data">
                                        <label>Novos Horários:</label>
                                        {erros.novoHorario && <div className="error-message">{erros.novoHorario}</div>}
                                    </div>
                                    <div className="item-container">
                                        <div className="item2">
                                            <LocalizationProvider dateAdapter={AdapterDateFns}>
                                                <TimePicker
                                                    label="Novo Horário de Início"
                                                    value={novoHorarioInicio}
                                                    onChange={(newTime) => setNovoHorarioInicio(newTime)}
                                                    ampm={false}
                                                    renderInput={(params) => (
                                                        <TextField {...params} variant="outlined" fullWidth sx={{ my: 2 }} />
                                                    )}
                                                />
                                            </LocalizationProvider>
                                        </div>
                                        <div>
                                            <LocalizationProvider dateAdapter={AdapterDateFns}>
                                                <TimePicker
                                                    label="Novo Horário de Término"
                                                    value={novoHorarioTermino}
                                                    onChange={(newTime) => setNovoHorarioTermino(newTime)}
                                                    ampm={false}
                                                    renderInput={(params) => (
                                                        <TextField {...params} variant="outlined" fullWidth sx={{ my: 2 }} />
                                                    )}
                                                />
                                            </LocalizationProvider>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}
                        <div>
                            <div className="label label-data">
                                <label>Horários:</label>
                                {erros.startTime && <div className="error-message">{erros.startTime}</div>}
                            </div>
                            <div className="item-container">
                                <div className="item2">
                                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                                        <TimePicker
                                            label="Horário de Início"
                                            value={consultaForm.dataHoraInicio}
                                            onChange={(newTime) => atualizarConsulta('dataHoraInicio', newTime)}
                                            ampm={false}
                                            disabled={["REMARCADO", "FINALIZADO", "CANCELADO", "CONFIRMADO", "EM_ANDAMENTO"].includes(consultaForm.status)}
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

                                <div>
                                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                                        <TimePicker
                                            label="Horário de Término"
                                            value={consultaForm.dataHoraFim}
                                            onChange={(newTime) => atualizarConsulta('dataHoraFim', newTime)}
                                            ampm={false}
                                            disabled={["REMARCADO", "FINALIZADO", "CANCELADO", "CONFIRMADO", "EM_ANDAMENTO"].includes(consultaForm.status)}
                                            renderInput={(params) => (
                                                <TextField
                                                    {...params}
                                                    variant="outlined"
                                                    fullWidth
                                                    sx={{ my: 2 }}
                                                    InputLabelProps={{ shrink: false }}
                                                    label="Horário de Término"
                                                />
                                            )}
                                        />
                                    </LocalizationProvider>
                                </div>
                            </div>
                        </div>

                        {exibirBotaoGravar(consultaForm) && (
                            <Button className="btn" variant="contained" color="primary" type="submit">Salvar</Button>
                        )}
                    </form>
                </>
            </Box>
        </Modal >
    );
});

