import React from "react";
import { useNavigate } from 'react-router-dom';

import CentralizarElementos from "../components/centralizar_elementos";

import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import { CardActions, Button } from "@mui/material";

function EsqueciMinhaSenha() {
  const navigate = useNavigate();

  const abrirTelaInserirCodigo = () => {
    navigate('/inserir-codigo');
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
            insira o e-mail cadastrado
          </Typography>
          <TextField
            sx={{marginTop: 1, width:"100%"}}
            required
            id="outlined-required"
            label="Required"
            placeholder="insira o e-mail"
          />
        </CardContent>
        <CardActions sx={{marginLeft: 0, padding: 2}}>
            <Button onClick={abrirTelaInserirCodigo} variant="contained" sx={{width: "100%"}}>ENVIAR</Button>
        </CardActions>
      </Card>
    </CentralizarElementos>
  );
}

export default EsqueciMinhaSenha;
