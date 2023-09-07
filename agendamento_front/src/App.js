import React from "react";
import { Routes, Route } from "react-router-dom";
import Login from "./pages/login";
import HomePage from "./pages/home";
import './App.css';

function App() {
  return (
    <>
      <sid_bar></sid_bar>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<HomePage/>} />
      </Routes>
    </>
  );
}

export default App;
