import React, { useState } from "react";
import {
  TextField,
  Button,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  CardContent,
  Card
} from "@mui/material";
import CentralizarElementos from "../components/centralizar_elementos";

const Cadastrar = () => {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [nome, setNome] = useState("");
  const [sobrenome, setSobrenome] = useState("");
  const [idade, setIdade] = useState("");
  const [cpf, setCpf] = useState("");
  const [sexo, setSexo] = useState("");
  const [telefone, setTelefone] = useState("");
  const [logradouro, setLogradouro] = useState("");
  const [bairro, setBairro] = useState("");
  const [numeroCasaApartamento, setNumeroCasaApartamento] = useState("");
  const [bloco, setBloco] = useState("");

  const handleFormSubmit = (e) => {
    e.preventDefault();

    // Validação e lógica de envio do formulário
    // ...

    // Resetar os campos após o envio do formulário
    setEmail("");
    setSenha("");
    setNome("");
    setSobrenome("");
    setIdade("");
    setCpf("");
    setSexo("");
    setTelefone("");
    setLogradouro("");
    setBairro("");
    setNumeroCasaApartamento("");
    setBloco("");
  };

  return (
    <CentralizarElementos>
      <Card sx={{ minWidth: 275, maxWidth: 800, padding: 2, width: 300 }}>
        <CardContent>
          <form onSubmit={handleFormSubmit}>
            <TextField
              label="Email"
              variant="outlined"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <TextField
              label="Senha"
              variant="outlined"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              required
            />
            <TextField
              label="Nome"
              variant="outlined"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              required
            />
            <TextField
              label="Sobrenome"
              variant="outlined"
              value={sobrenome}
              onChange={(e) => setSobrenome(e.target.value)}
              required
            />
            <TextField
              label="Idade"
              variant="outlined"
              value={idade}
              onChange={(e) => setIdade(e.target.value)}
              required
            />
            <TextField
              label="CPF"
              variant="outlined"
              value={cpf}
              onChange={(e) => setCpf(e.target.value)}
              required
            />
            <FormControl variant="outlined" required>
              <InputLabel id="sexo-label">Sexo</InputLabel>
              <Select
                labelId="sexo-label"
                label="Sexo"
                value={sexo}
                onChange={(e) => setSexo(e.target.value)}
              >
                <MenuItem value="masculino">Masculino</MenuItem>
                <MenuItem value="feminino">Feminino</MenuItem>
              </Select>
            </FormControl>
            <TextField
              label="Telefone"
              variant="outlined"
              value={telefone}
              onChange={(e) => setTelefone(e.target.value)}
              required
            />
            <TextField
              label="Logradouro"
              variant="outlined"
              value={logradouro}
              onChange={(e) => setLogradouro(e.target.value)}
              required
            />
            <TextField
              label="Bairro"
              variant="outlined"
              value={bairro}
              onChange={(e) => setBairro(e.target.value)}
              required
            />
            <TextField
              label="Número Casa/Apartamento"
              variant="outlined"
              value={numeroCasaApartamento}
              onChange={(e) => setNumeroCasaApartamento(e.target.value)}
              required
            />
            <TextField
              label="Bloco"
              variant="outlined"
              value={bloco}
              onChange={(e) => setBloco(e.target.value)}
            />
            <Button type="submit" variant="contained" color="primary">
              Cadastrar
            </Button>
          </form>
        </CardContent>
      </Card>
    </CentralizarElementos>
  );
};

export default Cadastrar;
