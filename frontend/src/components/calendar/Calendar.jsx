import React from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import listPlugin from '@fullcalendar/list';
import ptBrLocale from '@fullcalendar/core/locales/pt-br';
import "./calendar.scss"



export const Calendar = ({ calendarRef, handleEventClick, handleEventSelect }) => {
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
      editable={true} // Adicione esta linha para habilitar a interação com os eventos
    />
  );
}