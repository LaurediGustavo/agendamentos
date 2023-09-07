import React from "react";


import CentralizarElementos from "../components/centralizar_elementos";

import {
  Card,
  CardContent,
  Typography,
  TextField,
  CardActions,
  Button,
} from "@mui/material";

export default function InserirNovaSenha() {
   

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
          >
            insira a nova senha
          </Typography>
          <TextField
            sx={{ marginTop: 1, width: "100%" }}
            required
            id="outlined-required"
            label="Required"
            type="password"
            placeholder="insira a nova senha"
          />
          <Typography
            sx={{ fontSize: 14, marginTop: 3 }}
            color="text.primary"
          >
            repita a senha
          </Typography>
          <TextField
            sx={{ marginTop: 1, width: "100%" }}
            required
            id="outlined-required"
            label="Required"
            type="password"
            placeholder="repita a nova senha"
          />
        </CardContent>
        <CardActions sx={{ marginLeft: 0, padding: 2 }}>
          <Button variant="contained" sx={{ width: "100%" }}>
            CONFIRMAR
          </Button>
        </CardActions>
      </Card>
    </CentralizarElementos>
  );
}
