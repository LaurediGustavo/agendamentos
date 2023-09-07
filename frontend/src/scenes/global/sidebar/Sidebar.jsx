import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Sidebar, Menu, MenuItem } from "react-pro-sidebar";
import { Box, IconButton, Typography } from "@mui/material";
import MenuOutlinedIcon from "@mui/icons-material/MenuOutlined";
import HomeOutlinedIcon from "@mui/icons-material/HomeOutlined";
import MedicationlinedIcon from "@mui/icons-material/Medication";
import DescriptionlinedIcon from "@mui/icons-material/Description";
import PersonlinedIcon from "@mui/icons-material/Person";
import "./sidebar.scss";

const Item = ({ title, to, icon, selected, setSelected }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    setSelected(title);
    navigate(to);
  };

  return (
    <MenuItem
      active={selected === title}
      onClick={handleClick}
      icon={icon}
      className="menu-item"
    >
      <Typography className="menu-item-title-text">{title}</Typography>
    </MenuItem>
  );
};

const CustomSidebar = () => {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const [selected, setSelected] = useState("Dashboard");

  return (
    <Box className="box-sidebar">
      <Sidebar
        collapsed={isCollapsed}
        className="custom-sidebar sidebar-background"
      >
        <Menu iconShape="round">
          <MenuItem
            onClick={() => setIsCollapsed(!isCollapsed)}
            icon={isCollapsed ? <MenuOutlinedIcon /> : undefined}
            className="menu-item"
          >
            {!isCollapsed && (
              <Box className="menu-item-title">
                <Typography className="menu-item-title-text">
                  Administrador
                </Typography>
                <IconButton onClick={() => setIsCollapsed(!isCollapsed)}>
                  <MenuOutlinedIcon />
                </IconButton>
              </Box>
            )}
          </MenuItem>
          {/* ITENS DO MENU*/}
          <Box padding={`${isCollapsed ? "0" : "0 8%"}`}>
            <Item
              title="Dashboard"
              to="/"
              icon={<HomeOutlinedIcon className="custom-icon" />}
              selected={selected}
              setSelected={setSelected}
            />

            <Item
              title="Doutores"
              to="/doutores"
              icon={<MedicationlinedIcon className="custom-icon" />}
              selected={selected}
              setSelected={setSelected}
            />

            <Item
              title="Pacientes"
              to="/pacientes"
              icon={<PersonlinedIcon className="custom-icon" />}
              selected={selected}
              setSelected={setSelected}
            />

            <Item
              title="Procedimentos"
              to="/procedimentos"
              icon={<DescriptionlinedIcon className="custom-icon" />}
              selected={selected}
              setSelected={setSelected}
            />
          </Box>
        </Menu>
      </Sidebar>
    </Box>
  );
};

export default CustomSidebar;
