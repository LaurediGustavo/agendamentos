// components/Home.jsx
import React, { useState, useRef, useEffect } from 'react';
import { Box } from "@mui/material";
import Header from "../../components/headers/Headers";
import { Calendar } from '../../components/calendar/Calendar';
import { BookingForm } from '../../components/bookingForm/BookingForm';
import moment from 'moment';


export const Home = () => {
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null);
  const calendarRef = useRef();
  const childRef = useRef(null);

  const handleOpenModal = () => {
    setModalOpen(true);
  }

  const handleCloseModal = () => {
    setModalOpen(false);
  }

  const handleEventClick = (arg) => {
    setSelectedEvent(arg.event);
    handleOpenModal();
  };

  useEffect(() => {
    if (calendarRef.current) {
      const calendarApi = calendarRef.current.getApi();

      calendarApi.setOption('eventClick', function(info) {
        const idConsulta = info.event.extendedProps.consulta_id;
        
        if (childRef.current) {
          childRef.current.carregarDados(idConsulta);
        }

        setSelectedDate(moment(info.event.startStr).format("YYYY-MM-DD"))
        handleEventClick(info);
      });
    }
  }, [calendarRef]);

  const handleEventSelect = (arg) => {
    const selectedDate = new Date(arg.start);
    selectedDate.setHours(0, 0, 0, 0);

    const currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0);

    if (selectedDate >= currentDate) {
      setSelectedDate(arg.startStr);
      handleEventClick(arg);
  
      if (childRef.current) {
        childRef.current.limparDados();
      }
    }
  };

  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="Home" subtitle="Agende seu horário" />
      </Box>

      <Calendar
        calendarRef={calendarRef}
        handleEventClick={handleEventClick}
        handleEventSelect={handleEventSelect}
      />

      <BookingForm
        modalOpen={modalOpen}
        handleCloseModal={handleCloseModal}
        selectedEvent={selectedEvent}
        calendarRef={calendarRef}
        selectedDate={selectedDate}
        ref={childRef}
      />
    </Box>

  );
}
