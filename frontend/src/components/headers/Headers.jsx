import React from "react";
import { Typography, Box } from "@mui/material";
import "./headers.scss"

const Header = ({ title, subtitle }) => {
  return (
    <Box mb="30px">
      <div className="Header-title">
        {title}
      </div>
      <div className="Header-subtitle">
        {subtitle}
      </div>
    </Box>
  );
};

export default Header;
