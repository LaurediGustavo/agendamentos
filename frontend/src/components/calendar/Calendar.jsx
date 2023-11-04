import React, { useState } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import listPlugin from '@fullcalendar/list';
import ptBrLocale from '@fullcalendar/core/locales/pt-br';
import "./calendar.scss"
import { format } from 'date-fns';
import api from '../../services/api';

export const Calendar = ({ calendarRef, handleEventClick, handleEventSelect }) => {

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
      default:
        return 'event-default';
    }
  }

  const getConsultas = async (args) => {
    try {
      calendarRef.current?.getApi().removeAllEvents();
      const data = new Date(args);
      const dataFormatada = format(data, 'yyyy-MM-dd HH:mm:ss');
      const response = await api.get("/agendamento/consultar?horario=" + dataFormatada);
      response.data.map((consulta) => (
        calendarRef.current.getApi().addEvent({
          title: consulta.pacienteNome,
          start: consulta.dataHoraInicio,
          end: consulta.dataHoraFim,
          consulta_id: consulta.id,
          classNames: eventClass(consulta.status), // Adicione esta linha
        })
      ))
    } catch (error) {
      console.error("Ops! Ocorreu um erro: " + error);
    }
  };

  return (
    <div className="calendar-container">
      <FullCalendar
        ref={calendarRef}
        plugins={[dayGridPlugin, timeGridPlugin, listPlugin, interactionPlugin]}
        initialView="dayGridMonth"
        locale={ptBrLocale}
        headerToolbar={{
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,listMonth'
        }}
        selectable={true}
        dayMaxEventRows={true}
        select={handleEventSelect}
        eventClick={handleEventClick}
        editable={false}
        datesSet={(dateInfo) => {
          getConsultas(dateInfo.view.currentStart)
        }}
        eventTimeFormat={{
          hour: 'numeric',
          minute: '2-digit',
          omitZeroMinute: false,
          meridiem: false,
          hour12: false,
        }}
      />
      <div className="legend">
        <div><span className="bullet-point event-aguardando"></span> Aguardando</div>
        <div><span className="bullet-point event-enviado"></span> Enviado</div>
        <div><span className="bullet-point event-confirmado"></span> Confirmado</div>
        <div><span className="bullet-point event-cancelado"></span> Cancelado</div>
        <div><span className="bullet-point event-remarcado"></span> Remarcado</div>
        <div><span className="bullet-point event-finalizado"></span> Finalizado</div>
      </div>
    </div>
  );

}
