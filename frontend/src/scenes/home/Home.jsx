// components/Home.jsx
import React, { useState, useRef } from 'react';
import { Box } from "@mui/material";
import Header from "../../components/headers/Headers";
import { Calendar } from '../../components/calendar/Calendar';
import { BookingForm } from '../../components/bookingForm/BookingForm';
import { doctorsData, patientsData, proceduresData } from '../../data/dados';



export const Home = () => {
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null);
  const calendarRef = useRef();

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

  const handleEventSelect = (arg) => {
    setSelectedDate(arg.startStr);
    handleEventClick(arg);
  };

  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="Home" subtitle="Agende seu horario" />
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
        doctorsData={doctorsData}
        proceduresData={proceduresData}
        patientsData={patientsData}
      />
    </Box>
  );
}
