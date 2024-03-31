import React, { useState } from 'react';
import { Box, IconButton, Typography, Menu, MenuItem } from "@mui/material";
import AccountCircle from '@mui/icons-material/AccountCircle';
import './navbar.scss'; 
import Logo from '../../../assets/logo.png'; // Importe a imagem da logo
import { logout } from '../../../services/auth_service';
import { useNavigate } from 'react-router-dom';

export const Navbar = () => {
  const username = "Usuário";
  const [anchorEl, setAnchorEl] = useState(null); // Estado para controlar a abertura do menu
  const navigate = useNavigate();

  const handleMenuClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleProfileClick = () => {
    navigate('/perfil');
    handleClose(); 
  };

  return (
    <Box className="navbar">
      {/* Logo */}
      <img src={Logo} alt="Logo" className="navbar-logo" />

      {/* Nome do usuário */}
      <Box className="navbar-username">
        <Typography variant="subtitle1" className="navbar-username-text">{username}</Typography>
        <IconButton
          className="navbar-icon-button"
          onClick={handleMenuClick}
        >
          <AccountCircle />
        </IconButton>
        <Menu
          anchorEl={anchorEl}
          open={Boolean(anchorEl)}
          onClose={handleClose}

        >
          <MenuItem onClick={handleProfileClick}>Perfil</MenuItem>
          <MenuItem onClick={handleLogout}>Sair</MenuItem>
        </Menu>
      </Box>
    </Box>
  );
};
