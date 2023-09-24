import React, {useState} from 'react';
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

  const getConsultas = async (args) => {
    try {
        calendarRef.current?.getApi().removeAllEvents();
        const data = new Date(args);
        const dataFormatada = format(data, 'yyyy-MM-dd HH:mm:ss');
        const response = await api.get("/agendamento/consultar?horario=" + dataFormatada);

        response.data.map((consulta) => (
          calendarRef.current.getApi().addEvent({
            title: 'Agendamento',
            start: consulta.dataHoraInicio
          })
        ))
    } catch (error) {
        console.error("Ops! Ocorreu um erro: " + error);
    }
  };

  return (
    <FullCalendar
      ref={calendarRef}
      plugins={[dayGridPlugin, timeGridPlugin, listPlugin, interactionPlugin]}
      initialView="dayGridMonth"
      locale={ptBrLocale} // Adicione esta linha
      headerToolbar={{
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,listMonth'
      }}
      selectable={true}
      dayMaxEventRows={true}
      select={handleEventSelect}
      eventClick={handleEventClick}
      editable={false} // Adicione esta linha para habilitar a interação com os eventos
      datesSet={(dateInfo) => {
        getConsultas(dateInfo.view.currentStart)
    }}
    />
  );
}