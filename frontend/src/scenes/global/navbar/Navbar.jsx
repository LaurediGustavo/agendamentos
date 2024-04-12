import React, { useState, useEffect } from 'react';
import { Box, IconButton, Typography, Menu, MenuItem } from "@mui/material";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import './navbar.scss'; 
import Logo from '../../../assets/logo.png'; // Importe a imagem da logo
import { logout } from '../../../services/auth_service';
import { useNavigate } from 'react-router-dom';
import { getUserName } from '../../../services/auth_service';
import api from '../../../services/api';
import Avatar from '@mui/material/Avatar';


export const Navbar = () => {
  const [anchorEl, setAnchorEl] = useState(null); // Estado para controlar a abertura do menu
  const [image, setImage] = useState();
  
  const navigate = useNavigate();

  const userName = getUserName();
  

  const imagem = async () => {
    try {
        const response = await api.get("/usuario/imagem", { responseType: 'arraybuffer' });

        if (response.data.byteLength > 0) {
            const blob = new Blob([response.data], { type: 'image/png' }); // ou 'image/png', dependendo do tipo de imagem
            const imageUrl = URL.createObjectURL(blob);
            setImage(imageUrl);
        }
    } catch (error) {
        console.error("Ops! Ocorreu um erro: " + error);
    }
  };

  useEffect(() => {
      imagem();
  }, []);

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
        <Typography variant="subtitle1" className="navbar-username-text">{userName}</Typography>
        <IconButton
          className="navbar-icon-button"
          onClick={handleMenuClick}
        >
          <Avatar src={image} alt="Perfil" />
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
