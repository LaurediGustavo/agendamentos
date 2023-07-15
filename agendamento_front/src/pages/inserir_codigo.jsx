import React from "react";
import { useNavigate } from "react-router-dom";

import CentralizarElementos from "../components/centralizar_elementos";

import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import { CardActions, Button } from "@mui/material";

function InserirCodigoRecuperaSenha() {
  const navigate = useNavigate();

  const abrirTelaNovaSenha = () => {
    navigate("/nova-senha");
  };

  return (
    <CentralizarElementos>
      <Card
        sx={{
          minWidth: 275,
          maxWidth: 800,
          padding: 2,
          width: 300,
        }}
      >
        <CardContent>
          <Typography
            sx={{ fontSize: 14 }}
            color="text.primary"
            gutterBottom={true}
          >
            insira o código recebido
          </Typography>
          <TextField
            sx={{ marginTop: 1, width: "100%" }}
            required
            id="outlined-required"
            label="Required"
            placeholder="insira o código"
          />
        </CardContent>
        <CardActions sx={{ marginLeft: 0, padding: 2 }}>
          <Button onClick={abrirTelaNovaSenha} variant="contained" sx={{ width: "100%" }}>
            VALIDAR CÓDIGO
          </Button>
        </CardActions>
      </Card>
    </CentralizarElementos>
  );
}

export default InserirCodigoRecuperaSenha;
