import React, { useState } from 'react';
import { Box, IconButton, Typography, Menu, MenuItem } from "@mui/material";
import { AccountCircle } from "@mui/icons-material";
import './navbar.scss'; 
export const Navbar = () => {
  const username = "Usuário";
  const [anchorEl, setAnchorEl] = useState(null); // Estado para controlar a abertura do menu

  const handleMenuClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    // lógica para efetuar o logout
  
  };

  return (
    <Box className="navbar">
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
          <MenuItem onClick={handleLogout}>Sair</MenuItem>
        </Menu>
      </Box>
    </Box>
  );
};
