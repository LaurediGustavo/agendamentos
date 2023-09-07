import React from "react";
import { Typography, Box } from "@mui/material";
import "./headers.scss"

const Header = ({ title, subtitle }) => {
  return (
    <Box mb="30px">
      <Typography className="Header-title" fontWeight="bold" sx={{ mb: "5px" }}>
        {title}
      </Typography>
      <Typography className="Header-subtitle" sx={{ color: "grey.400" }}>
        {subtitle}
      </Typography>
    </Box>
  );
};

export default Header;
